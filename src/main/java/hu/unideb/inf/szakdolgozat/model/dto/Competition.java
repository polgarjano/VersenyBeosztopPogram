package hu.unideb.inf.szakdolgozat.model.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
public class Competition {

    private String name;
    private LocalDateTime timeOfBeginning;
    private int NumberOfLanes;
    private LocalTime DelayBetweenRelays;
    private Set<CompetitionEvent> CompetitionEvents;
    private List<Schedule> schedules;
}
