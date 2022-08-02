package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.validator.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Collectors;

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
    @FXML
    public ToggleGroup pistolOrRifle;
    @FXML
    public RadioButton pistolRadioButton;

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        initLabels();
    }

    @FXML
    public void saveButton(ActionEvent actionEvent) throws IOException {
        initLabels();
        if (validate()) {
            EventType eventType = new EventType();

            eventType.setName(nameTextField.getText());

            int time = Integer.parseInt(preparationTimeTextField.getText());
            eventType.setPreparationTime(Duration.ofMinutes(time));

            time = Integer.parseInt(preparationAndSightingTimeTextField.getText());
            eventType.setPreparationAndSightingTime(Duration.ofMinutes(time));

            time = Integer.parseInt(competitionTimeTextField.getText());

            eventType.setCompetitionTime(Duration.ofMinutes(time));

            eventType.setEventGroup(Integer.parseInt(eventGroupTextField.getText()));

            eventType.setIsPistolEvent(pistolRadioButton.isSelected());

            System.out.println(eventType.isIsPistolEvent());

            getCompetition().addEventType(eventType);

            super.loadView("addCompetitorView.fxml", actionEvent);
        }
    }


    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        super.loadView("addCompetitorView.fxml", actionEvent);
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
        nameValidator.add(new IsUniqueValidator<String>(
                getCompetition().getEventTypes()
                        .stream()
                        .map(x -> x.getName())
                        .collect(Collectors.toList())
                )
        );

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
