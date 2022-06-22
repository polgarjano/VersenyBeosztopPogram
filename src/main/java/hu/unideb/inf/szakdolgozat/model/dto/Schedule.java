package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    private List<Relay> relays;
}
