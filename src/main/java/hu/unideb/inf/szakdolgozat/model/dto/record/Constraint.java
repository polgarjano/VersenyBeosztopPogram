package hu.unideb.inf.szakdolgozat.model.dto.record;

import java.time.LocalDateTime;

public record Constraint(LocalDateTime availableFromThatTime,
         LocalDateTime availableUntilThisTime) {
}
