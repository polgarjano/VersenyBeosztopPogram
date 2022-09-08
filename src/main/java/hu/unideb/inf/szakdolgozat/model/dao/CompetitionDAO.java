package hu.unideb.inf.szakdolgozat.model.dao;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterArgumentFactory(DurationArgumentFactory.class)
@RegisterColumnMapper(DurationColumnMapper.class)
@RegisterBeanMapper(Competition.class)
public interface CompetitionDAO extends SqlObject {

    @SqlUpdate("""
                    CREATE TABLE competition(
                    id IDENTITY primary key,
                    name VARCHAR,
                    timeOfBeginning DATETIME,
                    numberOfLanes INTEGER,
                    delayBetweenRelays INTEGER
                    )
            """)
    void createTable();

    @SqlQuery("SELECT * FROM competition ORDER BY name")
    List<Competition> getCompetitionList();


    @SqlUpdate("INSERT INTO competition (name,timeOfBeginning,numberOfLanes,delayBetweenRelays) VALUES (:name,:timeOfBeginning,:numberOfLanes,:delayBetweenRelays)")
    @GetGeneratedKeys
    long saveCompetition(@BindBean Competition competition);

    @SqlUpdate("""
            UPDATE competition SET  name =:name, 
                                    timeOfBeginning=:timeOfBeginning, 
                                    numberOfLanes=:numberOfLanes,
                                    delayBetweenRelays=:delayBetweenRelays
            WHERE id=:id""")
    void updateCompetition(@BindBean Competition competition);
}
