package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Constraint {
    private LocalDateTime availableFromThatTime;
    private LocalDateTime availableUntilThisTime;


}
