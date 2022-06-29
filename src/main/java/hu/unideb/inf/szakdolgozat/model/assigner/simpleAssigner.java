package hu.unideb.inf.szakdolgozat.model.assigner;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Relay;
import hu.unideb.inf.szakdolgozat.model.dto.Schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class simpleAssigner {

    private List<Relay> relays = new LinkedList<>();

    public Schedule creatStartList(Competition competition) {
        List<Competitor> allCompetitor = competition.getCompetitors()
                .stream().sorted((o1, o2) -> o1.getEventTypeName().compareTo(o2.getEventTypeName()))
                .collect(Collectors.toList());
        int numberOfRelays = allCompetitor.size() / competition.getNumberOfLanes();
        for (int i = 1; i <= numberOfRelays; i++) {
            Relay actualRelay = new Relay();
            List<Competitor> competitorsInRelay = new LinkedList<>();
            for (int j = 1; j <= competition.getNumberOfLanes(); j++) {
                competitorsInRelay.add(allCompetitor.get((competition.getNumberOfLanes() * (i - 1)) + j - 1));
            }
            actualRelay.setCompetitors(competitorsInRelay);
            actualRelay.setNumberOfTheRelay(i);
            relays.add(actualRelay);
        }

        relays.get(0).setStartTime(competition.getTimeOfBeginning().toLocalTime());
        for (int i = 0; i < relays.size(); i++) {
            relays.get(i).setEndTime( relays.get(i).getStartTime().plus(getDurationOfTheRelay(relays.get(i).getCompetitors())));
            if ((i + 1) < relays.size()) {
                relays.get(1+1).setStartTime(relays.get(i).getEndTime().plus(competition.getDelayBetweenRelays()));
            }
        }

        return new Schedule(relays);
    }

    private Duration getDurationOfTheRelay(List<Competitor> competitors) {
        return  competitors
                .stream()
                .map(x -> x.getEventType().getDuration())
                .max(Duration::compareTo).orElse(Duration.ZERO);

    }
}
