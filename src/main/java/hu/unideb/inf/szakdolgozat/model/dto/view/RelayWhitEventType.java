package hu.unideb.inf.szakdolgozat.model.dto.view;

import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.dto.Relay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RelayWhitEventType extends Relay {
    private HashMap<EventType, Integer> eventTypes;

    public RelayWhitEventType(int numberOfTheRelay, LocalTime startTime, LocalTime endTime, List<Competitor> competitors,  HashMap<EventType, Integer> eventTypes) {
        super(numberOfTheRelay, startTime, endTime, competitors);
        this.eventTypes = eventTypes;
    }

    public Integer addEventType(EventType eventType, Integer NumberOfShooter){
        return eventTypes.put(eventType,NumberOfShooter);
    }

    @Override
    public String toString() {
        return super.toString()+" {" +
                "eventTypes=" + eventTypes +
                '}';
    }
}
