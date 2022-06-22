package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.validator.AbstractValidator;
import hu.unideb.inf.szakdolgozat.model.validator.StringNotEmptyValidator;
import hu.unideb.inf.szakdolgozat.model.validator.StringToNumberValidator;
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
    @FXML
    private Label label;

    public void next(ActionEvent actionEvent) {
        competition = new Competition();
        AbstractValidator NameValidator = new StringNotEmptyValidator();
        if (NameValidator.execute(CompetitionNameLabel.getText(), label)) {
            System.out.println(CompetitionNameLabel.getText());
        } else {
            System.out.println("Üresvolt");
        }

        AbstractValidator numberOfLanesValidator = new StringNotEmptyValidator();
        numberOfLanesValidator.add(new StringToNumberValidator());

        if (numberOfLanesValidator.execute(numberOfLanes.getText(), label)) {
            System.out.println(Integer.parseInt(numberOfLanes.getText()));
        } else {
            System.out.println("nem szam");
        }


    }
}
