package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class Relay {
    private int numberOfTheRelay;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Competitor> competitors;
}
