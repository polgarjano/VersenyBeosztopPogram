import hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner.MiAssigner;
import hu.unideb.inf.szakdolgozat.model.dto.*;
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
    void creatCompetitors() {
        var duration = Duration.ofMinutes(10);
        var duration2 = Duration.ofMinutes(3);
        eventTypes.add(new EventType("a1", duration, duration, duration, 1, false));
        eventTypes.add(new EventType("b1", duration, duration, duration, 1, false));
        eventTypes.add(new EventType("c1", duration2, duration2, duration2, 1, true));
        eventTypes.add(new EventType("d1", duration, duration, duration, 1, true));
        eventTypes.add(new EventType("a2", duration, duration, duration, 2, true));
        eventTypes.add(new EventType("b2", duration2, duration2, duration2, 2, false));
        eventTypes.add(new EventType("b21", duration, duration, duration, 2, true));
        eventTypes.add(new EventType("b211", duration2, duration2, duration2, 2, false));

        competitors.add(new Competitor("ba", 2000, "club", eventTypes.get(2), true,
                new Constraint(LocalDateTime.of(2022, 8, 4, 11, 00), LocalDateTime.of(2022, 8, 4, 12, 00))));
        competitors.add(new Competitor("aaa", 2000, "club", eventTypes.get(2), false, null));
        competitors.add(new Competitor("ad", 2000, "club", eventTypes.get(3), false, null));
        competitors.add(new Competitor("a1a", 2000, "club", eventTypes.get(1), false, null));
        competitors.add(new Competitor("a", 2000, "club", eventTypes.get(1), false, null));
        competitors.add(new Competitor("c", 2000, "club", eventTypes.get(1), false, null));

        competitors.add(new Competitor("a", 2001, "club", eventTypes.get(2), false, null));
        competitors.add(new Competitor("a", 2002, "club", eventTypes.get(3), false, null));
        competitors.add(new Competitor("a", 2003, "club", eventTypes.get(2), false, null));
        competitors.add(new Competitor("a", 2004, "club", eventTypes.get(4), true,
                new Constraint(LocalDateTime.of(2022, 8, 4, 10, 00), LocalDateTime.of(2022, 8, 4, 11, 00))));
        competitors.add(new Competitor("a", 2005, "club", eventTypes.get(4), true,
                new Constraint(LocalDateTime.of(2022, 8, 4, 10, 00), LocalDateTime.of(2022, 8, 4, 11, 00))));

        competitors.add(new Competitor("a", 2006, "club", eventTypes.get(4), false, null));


    }

    @Test
    void MiAssignerTest() {
        //Given

        Competition competition = new Competition();
        competition.setNumberOfLanes(4);
        competition.setTimeOfBeginning(LocalDateTime.of(2022, 8, 4, 8, 0));
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
