package hu.unideb.inf.szakdolgozat.model.dto.view;

import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Relay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ScheduledCompetitor {
    private String relayNumber;
    private String startTime;
    private String endTime;
    private String fireingPalce;
    private String name;
    private String club;
    private String birthYear;
    private String eventType;


    public static ScheduledCompetitor getRootSchedule() {
        return new ScheduledCompetitor("Schedules", "", "", "", "", "", "", "");
    }

    public static ScheduledCompetitor getScheduleRoot(int i) {
        return new ScheduledCompetitor("Schedule " + i, "", "", "", "", "", "", "");
    }

    public static ScheduledCompetitor getRelayRoot(Relay relay) {
        String eventTypeNames = relay.getCompetitors().stream()
                .map(Competitor::getEventTypeName)
                .map(x -> x + " \n ")
                .reduce("", String::concat);
        return new ScheduledCompetitor(String.valueOf(relay.getNumberOfTheRelay()), relay.getStartTime().toString(), relay.getEndTime().toString(), "", "", "", "", eventTypeNames);
    }

    public static ScheduledCompetitor getCompetitor(int i, Competitor competitor) {
        return new ScheduledCompetitor("","","",String.valueOf(i),competitor.getName(),competitor.getClub(),competitor.getBirthYear().toString(), competitor.getEventTypeName());
    }
}
