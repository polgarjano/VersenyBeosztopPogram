package hu.unideb.inf.szakdolgozat.model.dao;


import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
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
/**
    default List<Competitor> competitors(Long id, List<EventType> eventTypes) {
        class EventTypeColumnMapper implements ColumnMapper<EventType> {
            @OverrideMissing named parameter 'timeFrom' in binding:
            public EventType map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
                return eventTypes.stream().filter(x -> {
                    try {
                        return x.getName() == r.getString(columnNumber);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).findFirst().get();
            }
        }

        return getHandle().registerColumnMapper(new EventTypeColumnMapper())
                .createQuery("SELECT * FROM competitors WHERE competitionId = :id")
                .mapToBean(Competitor.class)
                .list();
    }
**/
}
