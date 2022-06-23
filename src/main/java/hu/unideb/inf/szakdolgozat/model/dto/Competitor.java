package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Competitor {
    private String name;
    private int birthYear;
    private String club;
    private EventType eventType;
    private List<Constraint> Constrains;
}
