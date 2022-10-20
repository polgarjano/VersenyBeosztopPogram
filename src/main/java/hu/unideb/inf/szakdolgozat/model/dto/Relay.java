package hu.unideb.inf.szakdolgozat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relay {
    private Long id;
    private int numberOfTheRelay;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Competitor> competitors = new ArrayList<>();

    public Relay(int numberOfTheRelay, LocalTime startTime, LocalTime endTime, List<Competitor> competitors) {
        this.numberOfTheRelay = numberOfTheRelay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.competitors = competitors;
    }

    public boolean addCompetitor(Competitor competitor){
        return competitors.add(competitor);
    }
    public int size(){
        return competitors.size();
    }
}
