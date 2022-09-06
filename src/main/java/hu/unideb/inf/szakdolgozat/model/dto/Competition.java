package hu.unideb.inf.szakdolgozat.model.dto;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Competition {

    private Long id;
    private String name;
    private LocalDateTime timeOfBeginning;
    private Integer NumberOfLanes;
    private Duration DelayBetweenRelays;
    private ObservableList<Competitor> competitors = FXCollections.observableList(new ArrayList<>());
    private List<Schedule> schedules = new LinkedList<>();
    private List<EventType> eventTypes = new LinkedList<>();

    public boolean addCompetitor(Competitor competitor){
        if(competitors.contains(competitor)) {
            return false;
        }
        competitors.add(competitor);
        return  true;
    }

    public  boolean addEventType(EventType eventType){
        return  eventTypes.add(eventType);
    }
}
