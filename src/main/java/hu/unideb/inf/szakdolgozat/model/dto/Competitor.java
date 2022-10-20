package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import hu.unideb.inf.szakdolgozat.model.dto.record.Constraint;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Competitor {
    private Long id;
    private Long competitionId;
    private String name;
    private Integer birthYear;
    private String club;
    private EventType eventType;
    @EqualsAndHashCode.Exclude
    private boolean constrained;
    @EqualsAndHashCode.Exclude
    private Constraint constrain;

    public Competitor(Long competitionId, String name, Integer birthYear, String club, EventType eventType, boolean constrained, Constraint constrain) {
        this.competitionId = competitionId;
        this.name = name;
        this.birthYear = birthYear;
        this.club = club;
        this.eventType = eventType;
        this.constrained = constrained;
        this.constrain = constrain;
    }

    public LocalDateTime getTimeFrom() {
        System.out.println("get time frome");
        if(constrain==null){
            System.out.println("Constrain is null");
            return null;
        }

        return constrain.availableFromThatTime();
    }

    public void setTimeFrom(LocalDateTime timeFrom) {
        if(constrain==null){
            constrain = new Constraint(timeFrom,null);
        }
        constrain = new Constraint(timeFrom,constrain.availableUntilThisTime());
    }

    public LocalDateTime getTimeUntil() {
        System.out.println("get time until");
        if(constrain==null){
            System.out.println("Constrain is null");
            return null;
        }

        return constrain.availableUntilThisTime();
    }

    public void setTimeUntil(LocalDateTime timeUntil) {
        if(constrain==null){
            constrain = new Constraint(null,timeUntil);
        }
        constrain = new Constraint(constrain.availableFromThatTime(),timeUntil);
    }


    public Competitor(String name, Integer birthYear, String club, EventType eventType, boolean constrained, Constraint constrain) {
        this.name = name;
        this.birthYear = birthYear;
        this.club = club;
        this.eventType = eventType;
        this.constrained = constrained;
        this.constrain = constrain;
    }

    public String getEventTypeName() {
        return eventType.getName();
    }




    protected boolean canEqual(final Object other) {
        return other instanceof Competitor;
    }


    public boolean sameCompetitor(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Competitor that = (Competitor) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(birthYear, that.birthYear)) return false;
        return Objects.equals(club, that.club);
    }

    public String getConstrainInString() {
        if (constrained) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return constrain.availableFromThatTime().format(formatter) + " - " + constrain.availableUntilThisTime().format(formatter);
        } else {

            return "Not constrained";
        }
    }

    @Override
    public String toString() {
        return
                name + ", " +
                        club + ", " +
                        birthYear + ", " +
                        eventType;
    }
}
