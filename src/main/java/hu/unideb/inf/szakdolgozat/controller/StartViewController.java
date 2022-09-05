package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;


public class StartViewController extends  AbstractController {
    @FXML
    public ComboBox competitionComboBox;

    @FXML
    public void loadButton(ActionEvent actionEvent) {
    }

    @FXML
    public void newCompetitionButton(ActionEvent actionEvent) throws IOException {
        setCompetition(null);
        loadView("competiton-view.fxml",actionEvent);
    }

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
    }
}
