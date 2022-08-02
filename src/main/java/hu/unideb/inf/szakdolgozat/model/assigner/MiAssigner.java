package hu.unideb.inf.szakdolgozat.model.assigner;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.dto.Schedule;

import java.util.LinkedList;
import java.util.List;

public class MiAssigner {

    private List<List<CompetitionEvent>> choseAbelGroups = new LinkedList<>();


    public Schedule creatStartList(Competition competition) {
        creatChoseAbleGroups(competition);




        return  null;
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
        for(int i = 0; i < eventTypes.size(); i++){
            if(actualGroupNumber != eventTypes.get(i).getEventGroup()){
                actualGroupNumber = eventTypes.get(i).getEventGroup();
                choseAbelGroups.add(new LinkedList<>());
                actualGroup = choseAbelGroups.get(choseAbelGroups.size()-1);
            }
            int finalI = i;
            Long numberOfCompetitors = competition.getCompetitors()
                    .stream()
                    .filter(x -> x.getEventType() == eventTypes.get(finalI))
                    .count();
            CompetitionEvent competitionEvent = new CompetitionEvent(eventTypes.get(i),numberOfCompetitors,numberOfCompetitors);
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
}
