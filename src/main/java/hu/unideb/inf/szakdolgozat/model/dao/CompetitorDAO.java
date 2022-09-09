package hu.unideb.inf.szakdolgozat.model.dao;


import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Constraint;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;


import java.sql.SQLException;

import java.time.LocalDateTime;
import java.util.List;

@RegisterBeanMapper(Competitor.class)
@RegisterArgumentFactory(EventTypeArgumentFactory.class)
public interface CompetitorDAO extends SqlObject {

    @SqlUpdate("""
                    CREATE TABLE competitors(
                    competitionId INTEGER,
                    name VARCHAR,
                    birthYear INTEGER,
                    club VARCHAR,
                    eventType VARCHAR,
                    constrained BOOLEAN,
                    timeFrom DATETIME,
                    timeUntil DATETIME,
                    PRIMARY KEY (competitionId,name,birthYear,club)
                    )
            """)
    void createTable();

    @SqlUpdate("INSERT INTO competitors VALUES ( :competitionId,:name,:birthYear,:club,:eventType,:constrained,:timeFrom,:timeUntil)")
    void saveCompetitor(@BindBean Competitor competitor);


    @SqlUpdate("DELETE FROM competitors WHERE competitionId = :id")
    void clearCompetitors(@Bind("id") Long id);


    default List<Competitor> getCompetitors(Long competitionId, List<EventType> eventTypes) {
        return getHandle()
                .select("SELECT competitionId, name,  birthYear, club, eventType, constrained, timeFrom, timeUntil " +
                        "FROM competitors WHERE competitionId = ?", competitionId)
                .map((rs, ctx) ->{
                    EventType eventType = new EventType();
                    eventType = eventTypes.stream().filter(e-> {
                        try {
                            return e.getName()== rs.getString("eventType");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        return false;
                    }).findFirst().get();

                    return  new Competitor(rs.getLong("competitionId"),rs.getString("name"),
                            rs.getInt("birthYear"),rs.getString("club"),eventType,rs.getBoolean("constrained"),
                            new Constraint(LocalDateTime.of(rs.getDate("timeFrom").toLocalDate(),rs.getTime("timeFrom").toLocalTime()),
                                    LocalDateTime.of(rs.getDate("timeUntil").toLocalDate(),rs.getTime("timeUntil").toLocalTime())));
                })
                .list();
    }

}
