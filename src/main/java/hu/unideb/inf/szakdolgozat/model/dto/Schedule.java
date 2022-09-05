package hu.unideb.inf.szakdolgozat.model.dto;

import hu.unideb.inf.szakdolgozat.model.dto.view.RelayWhitEventType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Schedule {
    private List<Relay> relays;
}
