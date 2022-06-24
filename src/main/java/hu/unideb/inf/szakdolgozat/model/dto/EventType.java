package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EventType {
    private String name;
    private LocalTime preparationTime;
    private LocalTime preparationAndSightingTime;
    private LocalTime competitionTime;
    private int eventGroup;
}
