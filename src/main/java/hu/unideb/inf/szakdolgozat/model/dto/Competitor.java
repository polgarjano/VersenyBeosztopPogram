package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.*;

import java.util.List;

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
    private List<Constraint> Constrains;


    public String  getEventTypeName(){
        return  eventType.getName();
    }


    protected boolean canEqual(final Object other) {
        return other instanceof Competitor;
    }

}
