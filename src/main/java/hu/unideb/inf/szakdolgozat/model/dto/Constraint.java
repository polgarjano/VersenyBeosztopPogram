package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Constraint {
    private LocalDateTime availableFromThatTime;
    private LocalDateTime availableUntilThisTime;
}
