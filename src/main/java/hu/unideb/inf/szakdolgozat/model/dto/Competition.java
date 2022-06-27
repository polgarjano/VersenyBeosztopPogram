package hu.unideb.inf.szakdolgozat.model.dto;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Data
public class Competition {

    private String name;
    private LocalDateTime timeOfBeginning;
    private Integer NumberOfLanes;
    private LocalTime DelayBetweenRelays;
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
