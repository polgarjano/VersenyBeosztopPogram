package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.validator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalTime;

public class AddNewEventType extends AbstractController {


    @FXML
    public TextField eventGroupTextField;
    @FXML
    public TextField competitionTimeTextField;
    @FXML
    public TextField preparationAndSightingTimeTextField;
    @FXML
    public TextField preparationTimeTextField;
    @FXML
    public TextField nameTextField;

    @FXML
    public Label eventGroupException;
    @FXML
    public Label competitionTimeException;
    @FXML
    public Label preparationAndSightingTimeException;
    @FXML
    public Label preparationTimeException;
    @FXML
    public Label nameExceptionLabel;

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        initLabels();
    }

    @FXML
    public void saveButton(ActionEvent actionEvent) throws IOException {
        if (validate()) {
            EventType eventType = new EventType();

            eventType.setName(nameTextField.getText());

            int time = Integer.parseInt(preparationTimeTextField.getText());
            eventType.setPreparationTime(getLocalTimeFromMinutes(time));

            time = Integer.parseInt(preparationAndSightingTimeTextField.getText());
            eventType.setPreparationAndSightingTime(getLocalTimeFromMinutes(time));

            time = Integer.parseInt(competitionTimeTextField.getText());
            eventType.setPreparationAndSightingTime(getLocalTimeFromMinutes(time));

            eventType.setEventGroup(Integer.parseInt(eventGroupTextField.getText()));

            getCompetition().addEventType(eventType);

            super.loadView("addCompetitorView.fxml", actionEvent);
        }
    }

    private LocalTime getLocalTimeFromMinutes(int time) {
        return LocalTime.of(time / 60, time % 60);
    }

    @FXML
    public void backButton(ActionEvent actionEvent) {
    }

    private void initLabels() {
        eventGroupException.setText("");
        competitionTimeException.setText("");
        preparationAndSightingTimeException.setText("");
        preparationTimeException.setText("");
        nameExceptionLabel.setText("");
    }

    private boolean validate() {
        AbstractValidator<String> nameValidator = new StringNotEmptyValidator();
        nameValidator.add(new IsUniqueValidator<EventType>(getCompetition().getEventTypes()));

        AbstractValidator<String> timesValidator = new StringNotEmptyValidator();
        timesValidator.add(new StringToIntegerValidator());
        timesValidator.add(new MaxIntegerValueValidator(
                (LocalTime.MAX.getHour() * 60) + LocalTime.MAX.getMinute()));

        AbstractValidator<String> eventGroupValidator = new StringNotEmptyValidator();
        eventGroupValidator.add(new StringToIntegerValidator());

        return (nameValidator.execute(nameTextField.getText(), nameExceptionLabel)
                & timesValidator.execute(preparationTimeTextField.getText(), preparationTimeException)
                & timesValidator.execute(preparationAndSightingTimeTextField.getText(), preparationAndSightingTimeException)
                & timesValidator.execute(competitionTimeTextField.getText(), competitionTimeException))
                & eventGroupValidator.execute(eventGroupTextField.getText(), eventGroupException);
    }


}
