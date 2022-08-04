package hu.unideb.inf.szakdolgozat.model.assigner;

import hu.unideb.inf.szakdolgozat.model.dto.*;

import java.time.LocalTime;
import java.util.*;

public class MiAssigner {
    public MiAssigner(Competition competition) {
        this.competition = competition;
    }

    private List<List<CompetitionEvent>> choseAbelGroups = new LinkedList<>();

    private List<List<EventType>> ScheduleByEvent;

    private EventNode root = new EventNode(null, null, 0, 0, 0, 0, null, null);
    private LinkedList<EventNode> perem = new LinkedList<>();
    private LinkedList<EventNode> celAllapot = new LinkedList<>();
    private int numberOfTheLanes;
    private Competition competition;

    public Schedule creatStartList() {
        numberOfTheLanes = competition.getNumberOfLanes();
        creatChoseAbleGroups(competition);
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
                while (numberOfTheNotScheduledCompetitors >= 0) {
                    numberOfTheRelay++;
                    freeLanes = numberOfTheLanes - numberOfTheNotScheduledCompetitors;
                    numberOfTheNotScheduledCompetitors = numberOfTheNotScheduledCompetitors - numberOfTheLanes;
                    int competitors;
                    if (freeLanes < 0) {
                        competitors = numberOfTheLanes;
                    } else {
                        competitors = numberOfTheLanes - freeLanes;
                    }
                    node = node.add(eventType, numberOfTheRelay, freeLanes, numberOfTheNotScheduledCompetitors, competitors);
                }
                perem.add(node);
            }

        }
        creatTree();

        var actual = root;


        do {
            actual = actual.childrenNodes.get(0);
            for (int i = 0; i < actual.numberOfRelay; i++) {
                System.out.print("                          ");
            }
            System.out.println(actual+" "+ actual.startTime+" "+actual.endTime);

        }
        while (!actual.childrenNodes.isEmpty());


        celAllapot.forEach(x -> System.out.println(x.endTime));

        return null;
    }

    private void creatTree() {
        while (perem.size() != 0) {
            System.out.println("---------------------");
            System.out.println(perem);
            System.out.println("---------------------");


            EventNode eventNode = perem.poll();

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
                    perem.addLast(eventNode.add(event.eventType, numberOfTheRelay, actualFreeLanes,
                            event.numberOfNotScheduledCompetitors.intValue() - freeLanes, competitors));
                    added = true;
                }
            }
            if (added) {
                break;
            }
        }
        if(!added){
            celAllapot.add(eventNode);
        }
    }

    private void SameEventTypeNewRelayOperator(EventNode eventNode, EventType eventType, int freeLanes, int numberOfTheNotScheduledCompetitors, int numberOfTheRelay) {
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
        return -1;
    }

    private void creatChoseAbleGroups(Competition competition) {
        List<EventType> eventTypes = competition.getCompetitors()
                .stream()
                .map(Competitor::getEventType)
                .distinct()
                .sorted((x, y) -> Integer.compare(x.getEventGroup(), y.getEventGroup()))
                .toList();

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


    private class CompetitionEvent {
        EventType eventType;
        Long numberOfNotScheduledCompetitors;
        Long numberOfCompetitors;


        public CompetitionEvent(EventType eventType, Long numberOfNotScheduledCompetitors, Long numberOfCompetitors) {
            this.eventType = eventType;
            this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
            this.numberOfCompetitors = numberOfCompetitors;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompetitionEvent that = (CompetitionEvent) o;

            return eventType.equals(that.eventType);
        }

        @Override
        public int hashCode() {
            return eventType.hashCode();
        }
    }


    private class EventNode {
        EventNode parent;

        EventType eventType;

        int numberOfRelay;

        int freeLaneAfterThisNode;

        int numberOfNotScheduledCompetitors;

        int numberOfScheduledCompetitors;

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

        public String toString() {
            return eventType.toString() + " r:" + numberOfRelay + " S:" + numberOfScheduledCompetitors;
        }

        public EventNode(EventNode parent, EventType eventType, int numberOfRelay, int freeLaneAfterThisNode,
                         int numberOfNotScheduledCompetitors, int numberOfScheduledCompetitors, LocalTime startTime, LocalTime endTime) {
            this.parent = parent;
            this.eventType = eventType;
            this.numberOfRelay = numberOfRelay;
            this.freeLaneAfterThisNode = freeLaneAfterThisNode;
            this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
            this.numberOfScheduledCompetitors = numberOfScheduledCompetitors;
            this.startTime = startTime;
            this.endTime = endTime;
            childrenNodes = new ArrayList<>();
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
            }else {
                this.startTime=parent.endTime.plus(competition.getDelayBetweenRelays());
                this.endTime = this.startTime.plus(eventType.getDuration());
            }
        }
    }

}
