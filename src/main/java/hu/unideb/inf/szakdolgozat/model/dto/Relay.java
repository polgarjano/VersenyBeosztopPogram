package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Relay {
    private int numberOfTheRelay;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Competitor> competitors = new ArrayList<>();

    public boolean addCompetitor(Competitor competitor){
        return competitors.add(competitor);
    }
    public int size(){
        return competitors.size();
    }
}
