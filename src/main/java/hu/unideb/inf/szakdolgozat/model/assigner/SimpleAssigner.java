package hu.unideb.inf.szakdolgozat.model.assigner;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Relay;
import hu.unideb.inf.szakdolgozat.model.dto.Schedule;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAssigner {

    private List<Relay> relays = new LinkedList<>();

    public Schedule creatStartList(Competition competition) {
        List<Competitor> allCompetitor = competition.getCompetitors()
                .stream()
                .sorted((o1, o2) -> o1.getEventTypeName().compareTo(o2.getEventTypeName()))
                .toList();

        int indexOfTheRelay = 0;
        Relay currentRelay = new Relay();
        currentRelay.setNumberOfTheRelay(indexOfTheRelay);
        relays.add(currentRelay);
        for (Competitor competitor : allCompetitor) {
            if (relays.get(indexOfTheRelay).size() == competition.getNumberOfLanes()) {
                currentRelay = new Relay();
                currentRelay.setNumberOfTheRelay(indexOfTheRelay+1);
                indexOfTheRelay = ++indexOfTheRelay;
                relays.add(currentRelay);
            }
            relays.get(indexOfTheRelay).addCompetitor(competitor);
        }

        relays.get(0).setStartTime(competition.getTimeOfBeginning().toLocalTime());
        for (int i = 0; i < relays.size(); i++) {
            relays.get(i).setEndTime(relays.get(i).getStartTime().plus(getDurationOfTheRelay(relays.get(i).getCompetitors())));
            if ((i + 1) < relays.size()) {
                relays.get(i + 1).setStartTime(relays.get(i).getEndTime().plus(competition.getDelayBetweenRelays()));
            }
        }
        return new Schedule(relays);
    }

    private Duration getDurationOfTheRelay(List<Competitor> competitors) {
        return competitors
                .stream()
                .map(x -> x.getEventType().getDuration())
                .max(Duration::compareTo).orElse(Duration.ZERO);

    }
}
