package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Competitor {
    private String name;
    private Integer birthYear;
    private String club;
    private EventType eventType;
    @EqualsAndHashCode.Exclude
    private boolean constrained;
    @EqualsAndHashCode.Exclude
    private Constraint constrain;


    public String getEventTypeName() {
        return eventType.getName();
    }


    void setTimeFrom(LocalDateTime from){
        if(constrain==null){
            constrain= new Constraint();
        }
        constrain.setAvailableFromThatTime(from);
    }


    void setTimeUntil(LocalDateTime Until){
        if(constrain==null){
            constrain= new Constraint();
        }
        constrain.setAvailableFromThatTime(Until);
    }

    LocalDateTime getTimeUntil(){
        if(constrain==null){
            return null;
        }
        return constrain.getAvailableFromThatTime();
    }

    LocalDateTime getTimeFrom(){
        if(constrain==null){
            return null;
        }
        return constrain.getAvailableUntilThisTime();
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
            return constrain.getAvailableFromThatTime().format(formatter) + " - " + constrain.getAvailableUntilThisTime().format(formatter);
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
