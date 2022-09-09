package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dao.CompetitionDAO;
import hu.unideb.inf.szakdolgozat.model.dao.CompetitorDAO;
import hu.unideb.inf.szakdolgozat.model.dao.EventTypeDAO;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;


public class StartViewController extends  AbstractController {
    @FXML
    public ComboBox<Competition> competitionComboBox;

    @FXML
    public void loadButton(ActionEvent actionEvent) throws IOException {
        if(competitionComboBox.getValue()!=null){
            setCompetition(competitionComboBox.getValue());
            System.out.println("loadEventType");
            EventTypeDAO dao = getHandle().attach(EventTypeDAO.class);
            getCompetition().setEventTypes(dao.getEventTypeList());
            System.out.println("loadCompetitors");
            CompetitorDAO competitorDao = getHandle().attach(CompetitorDAO.class);
            competitorDao.getCompetitors(getCompetition().getId(),getCompetition().getEventTypes()).forEach(getCompetition()::addCompetitor);


            loadView("competiton-view.fxml",actionEvent);
        }
    }

    @FXML
    public void newCompetitionButton(ActionEvent actionEvent) throws IOException {
        setCompetition(null);
        loadView("competiton-view.fxml",actionEvent);
    }

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        ObservableList<Competition> competitions = FXCollections.observableList(getHandle().attach(CompetitionDAO.class).getCompetitionList());
        competitionComboBox.setItems(competitions);
    }
}
