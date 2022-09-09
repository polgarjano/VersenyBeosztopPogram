package hu.unideb.inf.szakdolgozat.test;

import hu.unideb.inf.szakdolgozat.model.dao.CompetitionDAO;
import hu.unideb.inf.szakdolgozat.model.dao.CompetitorDAO;
import hu.unideb.inf.szakdolgozat.model.dao.EventTypeDAO;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Constraint;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDateTime;

public class CompetitorDaoTest {

    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());
        Handle handle = jdbi.open();
        CompetitorDAO competitorDAO = handle.attach(CompetitorDAO.class);
        competitorDAO.createTable();
        Competitor a = new Competitor("nev",200,"Klub",null,false,new Constraint(LocalDateTime.now(),LocalDateTime.now()));
        a.setCompetitionId(10L);
        competitorDAO.saveCompetitor(a);
    }
}
