package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.validator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CompetitionController extends AbstractController {

    @FXML
    public Label nameExceptionLabel;
    @FXML
    public Label timeExceptionLabel;
    @FXML
    public Label lanesExceptionLabel;
    @FXML
    public Label delayExceptionLabel;

    @FXML
    private TextField competitionName;
    @FXML
    private TextField numberOfLanes;
    @FXML
    private TextField delayBetweenRelays;
    @FXML
    private DatePicker startTimeDate;
    @FXML
    private TextField startTimeHour;
    @FXML
    private TextField startTimeMinute;


    @FXML
    public void next(ActionEvent actionEvent) throws IOException {
        resetExceptionLabels();

        if (validate()) {
            creatCompetition();
            super.loadView("addCompetitorView.fxml", actionEvent);
        }
    }

    private void resetExceptionLabels() {
        nameExceptionLabel.setText("");
        timeExceptionLabel.setText("");
        lanesExceptionLabel.setText("");
        delayExceptionLabel.setText("");
    }


    private boolean validate() {
        AbstractValidator<String> NameValidator = new StringNotEmptyValidator();

        AbstractValidator<Object> startDateValidator = new NotNullValidator();

        AbstractValidator<String> startTimeHourValidator = new StringNotEmptyValidator();
        startTimeHourValidator.add(new StringToIntegerValidator());
        startTimeHourValidator.add(new HourValidator());

        AbstractValidator<String> startTimeMinuteValidator = new StringNotEmptyValidator();
        startTimeMinuteValidator.add(new StringToIntegerValidator());
        startTimeMinuteValidator.add(new MinuteValidator());

        AbstractValidator<String> numberOfLaneValidator = new StringNotEmptyValidator();
        numberOfLaneValidator.add(new StringToIntegerValidator());
        numberOfLaneValidator.add(new PositiveIntegerValidator());

        AbstractValidator<String> delayValidator = new StringNotEmptyValidator();
        delayValidator.add(new StringToIntegerValidator());
        delayValidator.add(new PositiveIntegerValidator());

        return (NameValidator.execute(competitionName.getText(), nameExceptionLabel)
                & startDateValidator.execute(startTimeDate.getValue(), timeExceptionLabel)
                & startTimeHourValidator.execute(startTimeHour.getText(), timeExceptionLabel)
                & startTimeMinuteValidator.execute(startTimeMinute.getText(), timeExceptionLabel)
                & numberOfLaneValidator.execute(numberOfLanes.getText(), lanesExceptionLabel)
                & delayValidator.execute(delayBetweenRelays.getText(), delayExceptionLabel));
    }

    private void creatCompetition() {
        if (getCompetition() == null) {
            setCompetition(new Competition());
        }
        getCompetition().setName(competitionName.getText());
        getCompetition().setDelayBetweenRelays(Duration.ofMinutes(Integer.parseInt(delayBetweenRelays.getText())));
        getCompetition().setNumberOfLanes(Integer.parseInt(numberOfLanes.getText()));
        getCompetition().setTimeOfBeginning(LocalDateTime.of(startTimeDate.getValue(),
                LocalTime.of(Integer.parseInt(startTimeHour.getText()), Integer.parseInt(startTimeMinute.getText()))));
    }

    @Override
    public void init(Competition competition) {
        if (competition != null) {
            setCompetition(competition);
            competitionName.setText(competition.getName());
            numberOfLanes.setText(competition.getNumberOfLanes().toString());
            delayBetweenRelays.setText(String.valueOf(competition.getDelayBetweenRelays().toMinutes()));
            startTimeDate.setValue(competition.getTimeOfBeginning().toLocalDate());
            startTimeHour.setText(String.valueOf(competition.getTimeOfBeginning().getHour()));
            startTimeMinute.setText(String.valueOf(competition.getTimeOfBeginning().getMinute()));
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        loadView("startView.fxml", actionEvent);
    }
}
