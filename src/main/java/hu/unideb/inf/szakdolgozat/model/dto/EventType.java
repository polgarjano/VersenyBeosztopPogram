package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EventType {
    private LocalTime preparationTime;
    private LocalTime preparationAndSightingTime;
    private LocalTime competitionTime;
    private int eventGroup;
}
