package hu.unideb.inf.szakdolgozat.model.dao;


import hu.unideb.inf.szakdolgozat.model.dto.*;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;


import java.sql.SQLException;
import java.time.Duration;
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
                    endTime TIME
                    )
            """)
    void createRelayTable();

    @SqlUpdate("""
                    CREATE TABLE relayToCompetitor(
                    competitionId INTEGER,
                    relayId Integer,
                    competitorId Integer
                    )
            """)
    void createCompetitorToRelayTable();

    default void createTables(){
        createCompetitorToRelayTable();
        createRelayTable();
    }

    @SqlUpdate("DELETE FROM relay WHERE competitionId = :id")
    void clearRelays(@Bind("id") Long id);

    @SqlUpdate("DELETE FROM relayToCompetitor WHERE competitionId = :id")
    void clearCompetitors(@Bind("id") Long id);

    default void clearSchedule(Long competitionId){
        clearRelays(competitionId);
        clearCompetitors(competitionId);
    }


    @SqlUpdate("INSERT INTO relay ( competitionId, scheduleId, numberOfTheRelay, startTime, endTime)VALUES ( :competitionId, :scheduleId, :numberOfTheRelay, :startTime, :endTime)")
    @GetGeneratedKeys
    Long insertLineToRelay(@Bind("competitionId") Long competitionId, @Bind("scheduleId") Integer scheduleId, @Bind("numberOfTheRelay")
            Integer numberOfTheRelay, @Bind("startTime") LocalTime startTime, @Bind("endTime") LocalTime endTime);

    @SqlUpdate("INSERT INTO relayToCompetitor VALUES (:competitionId, :relayId,:competitorId  )")
    void insertCompetitor(@Bind("competitionId")Long competitionId,@Bind("relayId") Long relayId, @Bind("competitorId") Long competitorId);


    default void saveSchedules(Long competitionId, List<Schedule> schedules) {
        System.out.println(schedules.size());
        for (int i = 0; i < schedules.size(); i++) {
            int finalI = i;
            schedules.get(i).getRelays()
                    .forEach(x -> {
                        Long id = insertLineToRelay(competitionId, finalI, x.getNumberOfTheRelay(), x.getStartTime(), x.getEndTime());
                        x.getCompetitors().forEach(c -> insertCompetitor(competitionId,id, c.getId()));
                    });
        }

    }

    default List<Schedule> getSchedules(Long competitionId, List<Competitor> competitors) {
        List<Integer> nOSchedule = getHandle()
                .select("SELECT DISTINCT  scheduleId from relay where competitionId = ?", competitionId)
                .map((rs,col, ctx) -> rs.getInt("scheduleId"))
                .stream()
                .toList();
        List<Schedule> scheduleList = new LinkedList<>();
        System.out.println(nOSchedule.size());
        for (int i = 0; i < nOSchedule.size(); i++) {
            Schedule currentSchedule = new Schedule();
            var currentRelays = getHandle().select("SELECT id, numberOfTheRelay,startTime,endTime from relay " +
                            "where competitionId=? and scheduleId=?", competitionId, i)
                    .mapToBean(Relay.class)
                    .stream()
                    .sorted(Comparator.comparingInt(Relay::getNumberOfTheRelay))
                    .toList();

            for (Relay relay : currentRelays) {
                relay.setCompetitors(getHandle()
                        .select("SELECT competitorId FROM relayToCompetitor WHERE relayId =?", relay.getId())
                        .map((rs, col, ctx) ->competitors.stream().filter(x -> {
                            try {
                                return x.getId() == rs.getInt(col);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }).findFirst()
                                .orElse(new Competitor("Deleted Competitor",0,"unknown",
                                        new EventType("unknown", Duration.ZERO,Duration.ZERO,
                                                Duration.ZERO,-1,true),false,null)))
                        .list()
                );
            }

            currentSchedule.setRelays(currentRelays);
            scheduleList.add(currentSchedule);

        }
        return scheduleList;
    }
}
