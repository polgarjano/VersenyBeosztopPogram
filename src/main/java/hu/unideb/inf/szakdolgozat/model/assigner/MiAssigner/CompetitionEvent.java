package hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner;

import hu.unideb.inf.szakdolgozat.model.dto.EventType;

public class CompetitionEvent {
    public EventType eventType;
    public Long numberOfNotScheduledCompetitors;
    public Long numberOfCompetitors;


    public CompetitionEvent(EventType eventType, Long numberOfNotScheduledCompetitors, Long numberOfCompetitors) {
        this.eventType = eventType;
        this.numberOfNotScheduledCompetitors = numberOfNotScheduledCompetitors;
        this.numberOfCompetitors = numberOfCompetitors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompetitionEvent that = (CompetitionEvent) o;

        return eventType.equals(that.eventType);
    }

    @Override
    public int hashCode() {
        return eventType.hashCode();
    }

    @Override
    public String toString() {
        return "CompetitionEvent{" +
                "eventType=" + eventType +
                ", numberOfNotScheduledCompetitors=" + numberOfNotScheduledCompetitors +
                ", numberOfCompetitors=" + numberOfCompetitors +
                '}';
    }
}
