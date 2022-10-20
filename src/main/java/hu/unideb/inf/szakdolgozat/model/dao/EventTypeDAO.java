package hu.unideb.inf.szakdolgozat.model.dao;

import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
@RegisterArgumentFactory(DurationArgumentFactory.class)
@RegisterColumnMapper(DurationColumnMapper.class)
@RegisterBeanMapper(EventType.class)
public interface EventTypeDAO {
    @SqlUpdate("""
            CREATE TABLE eventtype(
            name  VARCHAR primary key,
            preparationTime INTEGER NOT NULL,
            preparationAndSightingTime INTEGER NOT NULL,
            competitionTime INTEGER NOT NULL,
            eventGroup INTEGER NOT NULL,
            IsPistolEvent BOOLEAN
            
            )""")
    void createTable();

    @SqlUpdate("INSERT INTO eventtype VALUES (:name,:preparationTime,:preparationAndSightingTime,:competitionTime,:eventGroup,:isPistolEvent)")
    void saveEventType(@BindBean EventType eventType);

    @SqlQuery("SELECT * FROM eventtype ORDER BY name")
    List<EventType> getEventTypeList();
}
