package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Competitor {
    private String name;
    private Integer birthYear;
    private String club;
    private EventType eventType;
    private List<Constraint> Constrains;

    public String  getEventTypeName(){
        return  eventType.getName();
    }
}
