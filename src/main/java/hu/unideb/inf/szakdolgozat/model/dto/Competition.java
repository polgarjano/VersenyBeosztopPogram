package hu.unideb.inf.szakdolgozat.model.dto;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Competition {

    private Long id;
    private String name;
    private LocalDateTime timeOfBeginning;
    private Integer numberOfLanes;
    private Duration delayBetweenRelays;
    private ObservableList<Competitor> competitors = FXCollections.observableList(new ArrayList<>());
    private List<Schedule> schedules = new LinkedList<>();

    @Override
    public String toString() {
        return id +
                ", " + name +
                ", " + timeOfBeginning;
    }

    private List<EventType> eventTypes = new LinkedList<>();

    public boolean addCompetitor(Competitor competitor) {
        if (competitors.contains(competitor)) {
            return false;
        }
        competitors.add(competitor);
        return true;
    }

    public boolean addEventType(EventType eventType) {
        return eventTypes.add(eventType);
    }
}
