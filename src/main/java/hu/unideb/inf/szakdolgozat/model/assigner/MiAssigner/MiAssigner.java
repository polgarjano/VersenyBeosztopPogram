package hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner;

import hu.unideb.inf.szakdolgozat.model.dto.*;
import hu.unideb.inf.szakdolgozat.model.dto.view.RelayWhitEventType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MiAssigner {
    private static final Double TIME_MULTIPLIER = 1.0;
    private static final Double REQUEST_MULTIPLIER = 600.0;
    private static final Double SHATTERED_EVENTS_MULTIPLIER = 1.0;

    private static final String BREAK = "break";

    private static final Integer BREAK_TIME = 40;

    public MiAssigner(Competition competition) {
        this.competition = competition;
        this.eventTypes = competition.getCompetitors()
                .stream()
                .map(Competitor::getEventType)
                .distinct()
                .sorted(Comparator.comparingInt(EventType::getEventGroup))
                .toList();
        this.root = new EventNode(null, null, -1, 0, 0, 0, LocalTime.MIN, LocalTime.MIN);

    }

    private final List<EventType> eventTypes;
    private final List<List<CompetitionEvent>> choseAbelGroups = new LinkedList<>();
    private ArrayList<ArrayList<Integer>> conflictMap;


    private final EventNode root;
    private final LinkedList<EventNode> perem = new LinkedList<>();
    private final LinkedList<EventNode> celAllapot = new LinkedList<>();
    private int numberOfTheLanes;
    private final Competition competition;


    public Schedule creatStartList() {
        numberOfTheLanes = competition.getNumberOfLanes();
        creatChoseAbleGroups();
        System.out.println(choseAbelGroups);
        creatConflictMap();

        for (int i = 0; i < choseAbelGroups.get(0).size(); i++) {
            EventType eventType = choseAbelGroups.get(0).get(i).eventType;
            int numberOfTheCompetitors = choseAbelGroups.get(0).get(i).numberOfCompetitors.intValue();
            int freeLanes = numberOfTheLanes - numberOfTheCompetitors;
            int numberOfTheNotScheduledCompetitors = numberOfTheCompetitors - numberOfTheLanes;
            int numberOfTheRelay = 1;

            if (numberOfTheNotScheduledCompetitors <= 0) {
                perem.add(root.add(eventType, numberOfTheRelay, freeLanes, numberOfTheNotScheduledCompetitors,
                        numberOfTheCompetitors,
                        competition.getTimeOfBeginning().toLocalTime(),
                        competition.getTimeOfBeginning().plus(eventType.getDuration()).toLocalTime()));
            } else {
                var node = root.add(eventType, numberOfTheRelay, freeLanes, numberOfTheNotScheduledCompetitors,
                        numberOfTheLanes,
                        competition.getTimeOfBeginning().toLocalTime(),
                        competition.getTimeOfBeginning().plus(eventType.getDuration()).toLocalTime());
                while (numberOfTheNotScheduledCompetitors > 0) {
                    numberOfTheRelay++;
                    freeLanes = numberOfTheLanes - numberOfTheNotScheduledCompetitors;
                    numberOfTheNotScheduledCompetitors = numberOfTheNotScheduledCompetitors - numberOfTheLanes;
                    int competitors;
                    if (freeLanes < 0) {
                        competitors = numberOfTheLanes;
                    } else {
                        competitors = numberOfTheLanes - freeLanes;
                    }
                    node.CalculateJosagErtek();
                    node = node.add(eventType, numberOfTheRelay, freeLanes, numberOfTheNotScheduledCompetitors, competitors);
                }
                perem.add(node);
            }


        }

        EventType brake = new EventType(BREAK, Duration.ZERO, Duration.ZERO,
                Duration.ofMinutes(BREAK_TIME), 0, true);
        perem.add(root.add(brake, 0, 0, 0, 0,
                competition.getTimeOfBeginning().toLocalTime(), competition.getTimeOfBeginning().plus(brake.getDuration()).toLocalTime()));
        brake = new EventType(BREAK, Duration.ZERO, Duration.ZERO,
                Duration.ofMinutes(BREAK_TIME), 0, false);
        perem.add(root.add(brake, 0, 0, 0, 0,
                competition.getTimeOfBeginning().toLocalTime(), competition.getTimeOfBeginning().plus(brake.getDuration()).toLocalTime()));

        creatTree();

        var actual = celAllapot.get(0);
        LinkedList<EventNode> megoldas = new LinkedList<>();

        while (actual.parent != null) {
            megoldas.addFirst(actual);
            actual = actual.parent;
        }
        System.out.println("Megoldas:");
        for (EventNode eventNode :
                megoldas) {
            for (int i = 0; i < eventNode.numberOfRelay; i++) {

                System.out.print("                    ");
            }
            System.out.println(eventNode);
        }

        celAllapot.forEach(x -> System.out.println(x.endTime + " j: " + x.josagErtek + " " + "r:" + x.numberOfRelay));

        return creatSchedule(megoldas);
    }

    private Schedule creatSchedule(LinkedList<EventNode> megoldas) {
        LinkedList<RelayWhitEventType> relays = new LinkedList<>();
        int realNumberOfTheRelay = 0;
        int numberOfTheRelay = 0;
        RelayWhitEventType actualRelay = null;
        for (EventNode eventNode :
                megoldas) {
            if (eventNode.eventType.getName() != BREAK) {
                if (numberOfTheRelay < eventNode.numberOfRelay) {
                    if (actualRelay != null) {
                        relays.addLast(actualRelay);
                    }
                    numberOfTheRelay = eventNode.numberOfRelay;
                    realNumberOfTheRelay++;
                    actualRelay = new RelayWhitEventType(realNumberOfTheRelay, eventNode.startTime, eventNode.endTime, new LinkedList<>(), new HashMap<>());
                    actualRelay.addEventType(eventNode.eventType, eventNode.numberOfScheduledCompetitors);
                } else {
                    actualRelay.addEventType(eventNode.eventType, eventNode.numberOfScheduledCompetitors);
                    if (actualRelay.getEndTime().isBefore(eventNode.endTime)) {
                        actualRelay.setEndTime(eventNode.endTime);
                    }
                }
            }
        }
        if (actualRelay != null) {
            relays.addLast(actualRelay);
        }

        List<Competitor> competitors = new LinkedList<>(competition.getCompetitors());
        List<Competitor> constrainedCompetitors = competitors
                .stream()
                .filter(Competitor::isConstrained)
                .toList();
        for (Competitor competitor : constrainedCompetitors) {
            for (int i = 0; i < relays.size(); i++) {
                if (relays.get(i).getStartTime().isAfter(competitor.getConstrain().getAvailableFromThatTime().toLocalTime()) &&
                        relays.get(i).getStartTime().isBefore(competitor.getConstrain().getAvailableUntilThisTime().toLocalTime())) {
                    if (relays.get(i).getEventTypes().get(competitor.getEventType()) != null &&
                            relays.get(i).getEventTypes().get(competitor.getEventType()) > 0)
                    {
                        relays.get(i).addCompetitor(competitor);
                        competitors.remove(competitor);
                        relays.get(i).getEventTypes().replace(competitor.getEventType(), relays.get(i).getEventTypes().get(competitor.getEventType()) - 1);
                    }
                }
            }

        }

        for (Competitor competitor : competitors) {
            boolean placed=false;
            for (RelayWhitEventType relay : relays) {
                for (EventType eventType : relay.getEventTypes().keySet()) {
                    if (eventType == competitor.getEventType() && relay.getEventTypes().get(eventType)>0 ){
                        relay.addCompetitor(competitor);
                        relay.getEventTypes().replace(eventType,relay.getEventTypes().get(eventType)-1);
                        placed=true;
                    }
                    if(placed){
                        break;
                    }
                }
                if(placed){
                    break;
                }
            }
        }


        System.out.println(relays);

        return new Schedule(relays.stream().map(x -> (Relay) x).toList());
    }


    private void creatChoseAbleGroups() {
        int actualGroupNumber = eventTypes.get(0).getEventGroup();
        choseAbelGroups.add(new LinkedList<>());
        var actualGroup = choseAbelGroups.get(0);
        for (int i = 0; i < eventTypes.size(); i++) {
            if (actualGroupNumber != eventTypes.get(i).getEventGroup()) {
                actualGroupNumber = eventTypes.get(i).getEventGroup();
                choseAbelGroups.add(new LinkedList<>());
                actualGroup = choseAbelGroups.get(choseAbelGroups.size() - 1);
            }
            int finalI = i;
            Long numberOfCompetitors = competition.getCompetitors()
                    .stream()
                    .filter(x -> x.getEventType() == eventTypes.get(finalI))
                    .count();
            CompetitionEvent competitionEvent = new CompetitionEvent(eventTypes.get(i), numberOfCompetitors, numberOfCompetitors);
            actualGroup.add(competitionEvent);

        }
    }

    private void creatConflictMap() {

        conflictMap = new ArrayList<>(eventTypes.size());
        for (int i = 0; i <= eventTypes.size(); i++) {
            conflictMap.add(new ArrayList<>(eventTypes.size()));
            for (int j = 0; j <= eventTypes.size(); j++) {
                conflictMap.get(i).add(0);
            }

        }


        for (int i = 0; i < eventTypes.size(); i++) {
            for (int j = 0; j < eventTypes.size(); j++) {

                long conflict = 0;
                if (i != j) {
                    int finalI = i;
                    var eventType1 = competition.getCompetitors()
                            .stream()
                            .filter(x -> x.getEventType() == eventTypes.get(finalI))
                            .toList();
                    int finalJ = j;
                    var eventType2 = competition.getCompetitors()
                            .stream()
                            .filter(x -> x.getEventType() == eventTypes.get(finalJ))
                            .toList();

                    conflict = eventType1
                            .stream()
                            .filter(x -> eventType2
                                    .stream()
                                    .anyMatch(y -> y.sameCompetitor(x))
                            )
                            .count();
                }
                conflictMap.get(i).set(j, (int) conflict);
            }

        }
        conflictMap
                .forEach(System.out::println);

    }

    private void creatTree() {

        while (celAllapot.size() == 0) {

            perem
                    .stream()
                    .filter(x -> x.josagErtek == null)
                    .forEach(EventNode::CalculateJosagErtek);

//            System.out.println("---------------------");
//            System.out.println(perem.size());
//            System.out.println("---------------------");


            EventNode eventNode = perem
                    .stream()
                    .min(Comparator.comparingDouble(EventNode::getJosagErtek))
                    .get();

            perem.remove(eventNode);

            EventType eventType = eventNode.eventType;
            int freeLanes = eventNode.freeLaneAfterThisNode;
            int numberOfTheNotScheduledCompetitors = eventNode.numberOfNotScheduledCompetitors;
            int numberOfTheRelay = eventNode.numberOfRelay;

            if (freeLanes <= 0) {

                numberOfTheRelay++;

                freeLanes = numberOfTheLanes;
            }

            if (eventNode.numberOfNotScheduledCompetitors > 0) {

                SameEventTypeNewRelayOperator(eventNode, eventType, freeLanes, numberOfTheNotScheduledCompetitors, numberOfTheRelay);
            } else {
                newEventTypeOperator(eventNode, eventType, freeLanes, numberOfTheRelay);

            }

        }
    }

    private void newEventTypeOperator(EventNode eventNode, EventType eventType, int freeLanes, int numberOfTheRelay) {
        Set<EventType> scheduledEvents = new HashSet<>();
        scheduledEvents.add(eventType);
        EventNode actualParent = eventNode.parent;
        while (actualParent.eventType != null) {
            scheduledEvents.add(actualParent.eventType);
            actualParent = actualParent.parent;
        }
        boolean nextEventIsPistol = nextEventIsPistol(scheduledEvents, eventType.isIsPistolEvent());

        int groupPosition;

        if (nextEventIsPistol != eventType.isIsPistolEvent()) {
            groupPosition = 0;
        } else {
            groupPosition = findGroup(eventType.getEventGroup());
        }
        boolean added = false;
        for (int i = groupPosition; i < choseAbelGroups.size(); i++) {

            for (CompetitionEvent event :
                    choseAbelGroups.get(i)) {
                if (event.eventType.isIsPistolEvent() == nextEventIsPistol && !scheduledEvents.contains(event.eventType)) {


                    int actualFreeLanes = freeLanes - event.numberOfCompetitors.intValue();
                    int competitors;
                    if (actualFreeLanes < 0) {
                        competitors = freeLanes;
                    } else {
                        competitors = freeLanes - actualFreeLanes;
                    }
                    int numberOfNotScheduledCompetitors = event.numberOfNotScheduledCompetitors.intValue() - freeLanes;

                    var currentEventNumber = eventTypes.indexOf(event.eventType);

                    var parentNode = eventNode;
                    var eventsInTheRelay = new LinkedList<Integer>();
                    while (parentNode.numberOfRelay == numberOfTheRelay) {
                        var s = eventTypes.indexOf(parentNode.eventType);
                        if (s == -1) {
                            s = eventTypes.size();
                        }
                        eventsInTheRelay.add(s);
                        parentNode = parentNode.parent;
                    }
                    for (int j = 0; j < eventsInTheRelay.size(); j++) {
                        var countOfTheConflicts = conflictMap.get(currentEventNumber).get(eventsInTheRelay.get(j));
                        if (0 < countOfTheConflicts) {
                            if (nextEventIsPistol != eventType.isIsPistolEvent()) {
                                numberOfTheRelay++;
                                freeLanes = competition.getNumberOfLanes();
                                actualFreeLanes = freeLanes - event.numberOfCompetitors.intValue();
                                numberOfNotScheduledCompetitors = event.numberOfNotScheduledCompetitors.intValue() - freeLanes;
                                if (actualFreeLanes < 0) {
                                    competitors = freeLanes;
                                } else {
                                    competitors = freeLanes - actualFreeLanes;
                                }
                            } else {
                                if (numberOfNotScheduledCompetitors < countOfTheConflicts) {

                                    int shootersInThePreviousRelay = 0;
                                    if (parentNode.eventType != null && parentNode.eventType.equals(eventTypes.get(eventsInTheRelay.get(j)))) {
                                        shootersInThePreviousRelay = parentNode.numberOfScheduledCompetitors;
                                    }
                                    if (shootersInThePreviousRelay < countOfTheConflicts) {
                                        numberOfTheRelay++;
                                        freeLanes = competition.getNumberOfLanes();
                                        actualFreeLanes = freeLanes - event.numberOfCompetitors.intValue();
                                        numberOfNotScheduledCompetitors = event.numberOfNotScheduledCompetitors.intValue() - freeLanes;
                                        if (actualFreeLanes < 0) {
                                            competitors = freeLanes;
                                        } else {
                                            competitors = freeLanes - actualFreeLanes;
                                        }
                                    }

                                }
                            }
                        }
                    }
                    perem.addLast(eventNode.add(event.eventType, numberOfTheRelay, actualFreeLanes,
                            numberOfNotScheduledCompetitors, competitors));
                    added = true;
                }
            }
            if (added) {

                int currentNumberOfRelay;
                if (actualParent.eventType != null && actualParent.eventType.getName() == BREAK) {
                    currentNumberOfRelay = numberOfTheRelay;
                } else {
                    currentNumberOfRelay = numberOfTheRelay + 1;
                }

                perem.addLast(eventNode.add(new EventType(BREAK, Duration.ZERO, Duration.ZERO,
                                Duration.ofMinutes(BREAK_TIME), eventType.getEventGroup(), eventType.isIsPistolEvent()),
                        currentNumberOfRelay, 0,
                        0, 0));
                break;
            }
        }
        if (!added) {
            celAllapot.add(eventNode);
        }
    }

    private void SameEventTypeNewRelayOperator(EventNode eventNode, EventType eventType, int freeLanes,
                                               int numberOfTheNotScheduledCompetitors, int numberOfTheRelay) {
        int actualFreeLanes = freeLanes - numberOfTheNotScheduledCompetitors;
        numberOfTheNotScheduledCompetitors = numberOfTheNotScheduledCompetitors - freeLanes;

        int competitors;
        if (actualFreeLanes < 0) {
            competitors = freeLanes;
        } else {
            competitors = freeLanes - actualFreeLanes;
        }

        perem.addLast(eventNode.add(eventType, numberOfTheRelay, actualFreeLanes, numberOfTheNotScheduledCompetitors, competitors));
    }

    private boolean nextEventIsPistol(Set<EventType> scheduledEvents, boolean isPistolEvent) {

        if (scheduledEvents.stream().allMatch(x -> x.isIsPistolEvent() == isPistolEvent)) {

            for (List<CompetitionEvent> events : choseAbelGroups) {
                for (CompetitionEvent event : events) {
                    if (!scheduledEvents.contains(event.eventType) && isPistolEvent == event.eventType.isIsPistolEvent()) {
                        return isPistolEvent;
                    }
                }
            }
            return !isPistolEvent;
        } else {
            return isPistolEvent;
        }

    }


    private int findGroup(int eventGroup) {
        for (int i = 0; i < choseAbelGroups.size(); i++) {
            if (choseAbelGroups.get(i).get(0).eventType.getEventGroup() == eventGroup) {
                return i;
            }
        }
        return 0;
    }


    private class EventNode {
        EventNode parent;

        EventType eventType;

        int numberOfRelay;

        int freeLaneAfterThisNode;

        int numberOfNotScheduledCompetitors;

        int numberOfScheduledCompetitors;

        public Double getJosagErtek() {
            return josagErtek;
        }

        public int getNumberOfRelay() {
            return numberOfRelay;
        }

        Double josagErtek = null;

        private LocalTime startTime;
        private LocalTime endTime;

        ArrayList<EventNode> childrenNodes;

        EventNode add(EventType eventType, int numberOfRelay, int freeLaneAfterThisNode, int numberOfNotScheduledCompetitors,
                      int numberOfScheduledCompetitors, LocalTime startTime, LocalTime endTime) {
            var newNode = new EventNode(this, eventType, numberOfRelay, freeLaneAfterThisNode,
                    numberOfNotScheduledCompetitors, numberOfScheduledCompetitors, startTime, endTime);
            childrenNodes.add(newNode);
            return newNode;
        }

        EventNode add(EventType eventType, int numberOfRelay, int freeLaneAfterThisNode, int numberOfNotScheduledCompetitors,
                      int numberOfScheduledCompetitors) {
            var newNode = new EventNode(this, eventType, numberOfRelay, freeLaneAfterThisNode,
                    numberOfNotScheduledCompetitors, numberOfScheduledCompetitors);
            childrenNodes.add(newNode);
            return newNode;
        }

        void CalculateJosagErtek() {

            var time = ((parent.endTime.until(endTime, ChronoUnit.MINUTES)) +
                    competition.getDelayBetweenRelays().multipliedBy((numberOfRelay - parent.numberOfRelay) - 1).toMinutes());
            var shatterEvent = (numberOfNotScheduledCompetitors > 0 ? 1 : 0);
            var acceptedRequest = acceptedPersonalRequests();
            josagErtek = parent.josagErtek + time * TIME_MULTIPLIER + shatterEvent * SHATTERED_EVENTS_MULTIPLIER + acceptedRequest * REQUEST_MULTIPLIER;

        }

        private int acceptedPersonalRequests() {

            int akku = 0;

            var actualNode = this;


            var competitors = competition.getCompetitors()
                    .stream()
                    .filter(competitor -> competitor.getEventType().equals(eventType))
                    .filter(Competitor::isConstrained)
                    .toList();
            for (Competitor competitor :
                    competitors) {
                if (!startTime.isAfter(competitor.getConstrain().getAvailableFromThatTime().toLocalTime()) ||
                        !startTime.isBefore(competitor.getConstrain().getAvailableUntilThisTime().toLocalTime())) {
                    akku++;
                }
            }

            return akku;
        }


        public String toString() {
            return startTime + " " + endTime + " " + eventType.toString() + " j: " + getJosagErtek() + " r:" + numberOfRelay + " S:" + numberOfScheduledCompetitors;
        }

        public EventNode getRoot() {
            return new EventNode(null, -1, 0, 0, 0, LocalTime.MIN, LocalTime.MIN);
        }

        private EventNode(EventType eventType, int numberOfRelay, int freeLaneAfterThisNode, int numberOfNotScheduledCompetitors,
                          int numberOfScheduledCompetitors, LocalTime startTime, LocalTime endTime) {
            this.parent = null;
            this.eventType = eventType;
            this.numberOfRelay = numberOfRelay;
            this.freeLaneAfterThisNode = freeLaneAfterThisNode;
            this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
            this.numberOfScheduledCompetitors = numberOfScheduledCompetitors;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public EventNode(EventNode parent, EventType eventType, int numberOfRelay, int freeLaneAfterThisNode,
                         int numberOfNotScheduledCompetitors, int numberOfScheduledCompetitors, LocalTime startTime, LocalTime
                                 endTime) {
            this.parent = parent;
            this.eventType = eventType;
            this.numberOfRelay = numberOfRelay;
            this.freeLaneAfterThisNode = freeLaneAfterThisNode;
            this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
            this.numberOfScheduledCompetitors = numberOfScheduledCompetitors;
            this.startTime = startTime;
            this.endTime = endTime;
            childrenNodes = new ArrayList<>();
            this.josagErtek = Duration.between(startTime, endTime).toMinutes()
                    * TIME_MULTIPLIER +
                    (numberOfNotScheduledCompetitors > 0 ? 1 : 0) * SHATTERED_EVENTS_MULTIPLIER +
                    (acceptedPersonalRequests() * REQUEST_MULTIPLIER);
        }

        public EventNode(EventNode parent, EventType eventType, int numberOfRelay, int freeLaneAfterThisNode,
                         int numberOfNotScheduledCompetitors, int numberOfScheduledCompetitors) {
            this.parent = parent;
            this.eventType = eventType;
            this.numberOfRelay = numberOfRelay;
            this.freeLaneAfterThisNode = freeLaneAfterThisNode;
            this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
            this.numberOfScheduledCompetitors = numberOfScheduledCompetitors;
            childrenNodes = new ArrayList<>();

            if (numberOfRelay == parent.numberOfRelay) {
                this.startTime = parent.startTime;

                if (0 < this.eventType.getCompetitionTime().compareTo(parent.eventType.getCompetitionTime())) {
                    this.endTime = parent.startTime.plus(eventType.getDuration());
                } else {
                    this.endTime = parent.endTime;
                }
            } else {
                this.startTime = parent.endTime.plus(competition.getDelayBetweenRelays());
                this.endTime = this.startTime.plus(eventType.getDuration());
            }

        }
    }

}
