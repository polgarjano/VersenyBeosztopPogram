package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dao.CompetitionDAO;
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
