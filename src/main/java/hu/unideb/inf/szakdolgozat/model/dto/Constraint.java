package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Constraint {
    private LocalDateTime availableFromThatTime;
    private LocalDateTime availableUntilThisTime;
}
