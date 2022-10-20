package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventType {
    private String name;
    private Duration preparationTime;
    private Duration preparationAndSightingTime;
    private Duration competitionTime;
    private int eventGroup;
    private boolean IsPistolEvent;

    public boolean isIsPistolEvent() {
        return IsPistolEvent;
    }

    public void setIsPistolEvent(boolean pistolEvent) {
        IsPistolEvent = pistolEvent;
    }

    public boolean getIsPistolEvent() {
        return IsPistolEvent;
    }


    @Override
    public String toString() {
        return name;
    }

    public Duration getDuration() {
        return preparationTime.plus(preparationAndSightingTime).plus(competitionTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (eventGroup != eventType.eventGroup) return false;
        if (IsPistolEvent != eventType.IsPistolEvent) return false;
        if (name != null ? !name.equals(eventType.name) : eventType.name != null) return false;
        if (preparationTime != null ? !preparationTime.equals(eventType.preparationTime) : eventType.preparationTime != null)
            return false;
        if (preparationAndSightingTime != null ? !preparationAndSightingTime.equals(eventType.preparationAndSightingTime) : eventType.preparationAndSightingTime != null)
            return false;
        return competitionTime != null ? competitionTime.equals(eventType.competitionTime) : eventType.competitionTime == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (preparationTime != null ? preparationTime.hashCode() : 0);
        result = 31 * result + (preparationAndSightingTime != null ? preparationAndSightingTime.hashCode() : 0);
        result = 31 * result + (competitionTime != null ? competitionTime.hashCode() : 0);
        result = 31 * result + eventGroup;
        result = 31 * result + (IsPistolEvent ? 1 : 0);
        return result;
    }
}
