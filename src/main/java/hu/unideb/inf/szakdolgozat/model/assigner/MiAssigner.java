package hu.unideb.inf.szakdolgozat.model.assigner;

import hu.unideb.inf.szakdolgozat.model.dto.*;
import lombok.Data;

import java.util.*;

public class MiAssigner {

    private List<List<CompetitionEvent>> choseAbelGroups = new LinkedList<>();

    private List<List<EventType>> ScheduleByEvent;

    private EventNode root = new EventNode(null, null, 0, 0);
    private LinkedList<EventNode> perem = new LinkedList<>();

    public Schedule creatStartList(Competition competition) {
        creatChoseAbleGroups(competition);
        for (int i = 0; i < choseAbelGroups.get(0).size(); i++) {

            perem.add(root.add(choseAbelGroups.get(0).get(i).eventType, 1, 0));

        }
        creatTree();


        return null;
    }

    private void creatTree() {
        while (perem.size() != 0) {
            EventNode eventNode = perem.poll();
            Set<EventType> scheduledEvents = new HashSet<>();
            scheduledEvents.add(eventNode.eventType);
            EventNode actualParent = eventNode.parent;
            while (actualParent.eventType != null) {
                scheduledEvents.add(actualParent.eventType);
                actualParent = actualParent.parent;
            }
            boolean nextEventIsPistol = nextEventIsPistol(scheduledEvents, eventNode.eventType.isIsPistolEvent());

            int groupPosition = findGroup(eventNode.eventType.getEventGroup());

            if (nextEventIsPistol != eventNode.eventType.isIsPistolEvent()) {
                groupPosition = 0;
            }
            for (int i = groupPosition; i < choseAbelGroups.size(); i++) {
                boolean added = false;
                for (CompetitionEvent event :
                        choseAbelGroups.get(i)) {
                    if (event.eventType.isIsPistolEvent() == nextEventIsPistol && !scheduledEvents.contains(event.eventType)) {
                        perem.addLast(eventNode.add(event.eventType, 1, 0));
                        added = true;
                    }
                }
                if (added) {
                    break;
                }
            }

        }
    }

    private boolean nextEventIsPistol(Set<EventType> scheduledEvents, boolean isPistolEvent) {

        if (scheduledEvents.stream().allMatch(x -> x.isIsPistolEvent() == isPistolEvent)) {

            for (List<CompetitionEvent> events : choseAbelGroups) {
                for (CompetitionEvent event : events) {
                    if (!scheduledEvents.contains(event) && isPistolEvent == event.eventType.isIsPistolEvent()) {
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
    }


    private class EventNode {
        EventNode parent;

        EventType eventType;

        int numberOfRelay;

        int freeLaneAfterThisNode;

        ArrayList<EventNode> childrenNodes;

        EventNode add(EventType eventType, int numberOfRelay, int usedLanesInThisNode) {
            var newNode = new EventNode(this, eventType, numberOfRelay, usedLanesInThisNode);
            childrenNodes.add(newNode);
            return newNode;
        }

        public EventNode(EventNode parent, EventType eventType, int numberOfRelay, int freeLaneAfterThisNode) {
            this.parent = parent;
            this.eventType = eventType;
            this.numberOfRelay = numberOfRelay;
            this.freeLaneAfterThisNode = freeLaneAfterThisNode;
            childrenNodes = new ArrayList<>();
        }
    }

}
