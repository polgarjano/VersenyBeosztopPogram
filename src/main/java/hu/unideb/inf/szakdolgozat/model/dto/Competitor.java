package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.*;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
@ToString
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
    private TimeConstrain constrain;


    public String  getEventTypeName(){
        return  eventType.getName();
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


}
