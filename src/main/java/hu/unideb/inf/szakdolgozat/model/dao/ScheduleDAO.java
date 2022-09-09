package hu.unideb.inf.szakdolgozat.model.dao;


import hu.unideb.inf.szakdolgozat.model.dto.*;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import hu.unideb.inf.szakdolgozat.model.dto.Competitor;


@RegisterBeanMapper(Relay.class)
public interface ScheduleDAO extends SqlObject {


    @SqlUpdate("""
                    CREATE TABLE relay(
                    id IDENTITY primary key,
                    competitionId INTEGER,
                    scheduleId INTEGER,
                    numberOfTheRelay INTEGER,
                    startTime TIME,
                    endTime TIME,
                    )
            """)
    void createRelayTable();

    @SqlUpdate("""
                    CREATE TABLE relayToCompetitor(
                    relayId Integer,
                    competitorId Integer
                    )
            """)
    void createCompetitorToRelayTable();

    @SqlUpdate("INSERT INTO relay VALUES ( :competitionId, :scheduleId, :numberOfTheRelay, :startTime, :endTime)")
    @GetGeneratedKeys
    Long insertLineToRelay(@Bind("competitionId") Long competitionId, @Bind("scheduleId") Integer scheduleId, @Bind("numberOfTheRelay")
            Integer numberOfTheRelay, @Bind("startTime") LocalTime startTime, @Bind("endTime") LocalTime endTime);

    @SqlUpdate("INSERT INTO relayToCompetitor VALUES ( :relayId,:competitorId  )")
    void insertCompetitor(@Bind("relayId") Long relayId, @Bind("competitorId") Long competitorId);


    default void saveSchedules(Long competitionId, List<Schedule> schedules) {

        for (int i = 0; i < schedules.size(); i++) {
            int finalI = i;
            schedules.get(i).getRelays()
                    .forEach(x -> {
                        Long id = insertLineToRelay(competitionId, finalI, x.getNumberOfTheRelay(), x.getStartTime(), x.getEndTime());
                        x.getCompetitors().forEach(c -> insertCompetitor(id, c.getId()));
                    });
        }

    }

    default List<Schedule> getSchedules(Long competitionId, List<Competitor> competitors) {
        List<Integer> nOSchedule = getHandle()
                .select("SELECT COUNT(scheduleId) from relay where competitionId = ?", competitionId)
                .map((rs, ctx) -> rs.getInt("scheduleId"))
                .stream()
                .toList();
        List<Schedule> scheduleList = new LinkedList<>();
        for (int i = 0; i < nOSchedule.size(); i++) {
            Schedule currentSchedule = new Schedule();
            //TODO
            /* felszedem a relayeket bele rakom A scheduelbe és felszedem a relay hez ha lövőket a paraméterbőlmeg a kapcsoló táblából*/

            scheduleList.add(currentSchedule);

        }
        return scheduleList;
    }
}
