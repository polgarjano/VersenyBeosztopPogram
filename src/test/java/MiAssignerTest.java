import hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


public class MiAssignerTest {

    final List<EventType> eventTypes = new LinkedList<>();
    final List<Competitor> competitors = new LinkedList<>();

    @BeforeAll
    void creatCompetitors(){
        var duration = Duration.ofMinutes(10);
        var duration2 = Duration.ofMinutes(20);
        eventTypes.add(new EventType("a1",duration,duration,duration,1,true));
        eventTypes.add(new EventType("b1",duration,duration,duration,1,true));
        eventTypes.add(new EventType("c1",duration2,duration2,duration2,1,false));
        eventTypes.add(new EventType("d1",duration,duration,duration,1,false));//
        eventTypes.add(new EventType("a2",duration,duration,duration,2,true));
        eventTypes.add(new EventType("b2",duration2,duration2,duration2,2,false));
        eventTypes.add(new EventType("b21",duration,duration,duration,2,true));
        eventTypes.add(new EventType("b211",duration2,duration2,duration2,2,false));

        competitors.add(new Competitor("Competitor",2000,"club",eventTypes.get(1),null));
        competitors.add(new Competitor("Competitor1",2000,"club",eventTypes.get(1),null));
        competitors.add(new Competitor("Competitor3",2000,"club",eventTypes.get(1),null));
        competitors.add(new Competitor("Competitor4",2000,"club",eventTypes.get(2),null));
        competitors.add(new Competitor("Competitor5",2000,"club",eventTypes.get(2),null));
        competitors.add(new Competitor("Competitor6",2000,"club",eventTypes.get(3),null));
        competitors.add(new Competitor("Competitor7",2000,"club",eventTypes.get(3),null));
        competitors.add(new Competitor("Competitor8",2000,"club",eventTypes.get(4),null));
        competitors.add(new Competitor("Competitor9",2000,"club",eventTypes.get(4),null));
        competitors.add(new Competitor("Competitor10",2000,"club",eventTypes.get(5),null));
        competitors.add(new Competitor("Competitor11",2000,"club",eventTypes.get(5),null));
        competitors.add(new Competitor("Competitor12",2000,"club",eventTypes.get(6),null));
        competitors.add(new Competitor("Competitor121",2000,"club",eventTypes.get(0),null));
        competitors.add(new Competitor("Competitor1211",2000,"club",eventTypes.get(0),null));
        competitors.add(new Competitor("Competitor12111",2000,"club",eventTypes.get(7),null));
        competitors.add(new Competitor("Competitor121111",2000,"club",eventTypes.get(7),null));
        competitors.add(new Competitor("Competitor1211111",2000,"club",eventTypes.get(7),null));


    }

    @Test
    void MiAssignerTest(){
        //Given

        Competition competition =new Competition();
        competition.setNumberOfLanes(100);
        competition.setTimeOfBeginning(LocalDateTime.of(2022,8,4,8,0));
        competition.setDelayBetweenRelays(Duration.ofMinutes(10));
        competitors.forEach(competition::addCompetitor);
        System.out.println(competition);
        MiAssigner assigner = new MiAssigner(competition);
        //When
        assigner.creatStartList();
        //Then



    }

    public static void main(String[] args) {
        MiAssignerTest miAssignerTest = new MiAssignerTest();
        miAssignerTest.creatCompetitors();
        miAssignerTest.MiAssignerTest();
    }

}
