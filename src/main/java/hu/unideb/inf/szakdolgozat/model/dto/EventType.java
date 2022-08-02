package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventType {
    private String name;
    private Duration preparationTime;
    private Duration preparationAndSightingTime;
    private Duration competitionTime;
    private int eventGroup;
    private boolean IsPistolEvent;

    @Override
    public String toString(){
        return name;
    }

    public Duration getDuration(){
        return preparationTime.plus(preparationAndSightingTime).plus(competitionTime);
    }
}
