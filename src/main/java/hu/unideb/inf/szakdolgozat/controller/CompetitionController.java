package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CompetitionController {

    private Competition competition;

    @FXML
    private TextField CompetitionNameLabel;
    @FXML
    private TextField numberOfLanes;
    @FXML
    private TextField DelayBetweenRelays;
    @FXML
    private DatePicker startTimeDate;
    @FXML
    private TextField startTimeHour;
    @FXML
    private TextField startTimeMinute;

    public void next(ActionEvent actionEvent) {
        competition = new Competition();

            System.out.println(CompetitionNameLabel.getText());

    }
}
