package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.assigner.SimpleAssigner;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.validator.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddCompetitorViewController extends AbstractController {

    @FXML
    public Label competitionNameLabel;
    @FXML
    public Label startTimeLabel;
    @FXML
    public Label numberOfLanesLabel;
    @FXML
    public Label delayBetweenRelaysLabel;

    @FXML
    public Label nameExceptionLabel;
    @FXML
    public Label clubExceptionLabel;
    @FXML
    public Label birthDayExceptionLabel;
    @FXML
    public Label eventExceptionLabel;
    @FXML
    public Label addExceptionLabel;

    @FXML
    public TextField nameTextField;
    @FXML
    public TextField clubTextField;
    @FXML
    public TextField birthYearTextField;
    @FXML
    public ComboBox<EventType> eventTypeComboBox;

    @FXML
    public TableColumn<Competitor, String> nameTableColum;
    @FXML
    public TableColumn<Competitor, String> clubTableColum;
    @FXML
    public TableColumn<Competitor, Integer> birthYearTableColum;
    @FXML
    public TableColumn<Competitor, String> EventTypeTableColum;
    @FXML
    public TableView<Competitor> table;

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        initLabels();
        initTable();
        initEventComboBox();
    }

    private void initTable() {

        nameTableColum.setCellValueFactory(new PropertyValueFactory<>("name"));
        clubTableColum.setCellValueFactory(new PropertyValueFactory<>("club"));
        birthYearTableColum.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        EventTypeTableColum.setCellValueFactory(new PropertyValueFactory<>("eventTypeName"));
        ObservableList<Competitor> observableList = FXCollections.observableList(getCompetition().getCompetitors());
        table.setItems(observableList);

    }

    private void initLabels() {
        competitionNameLabel.setText(getCompetition().getName());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        startTimeLabel.setText(getCompetition().getTimeOfBeginning().format(dateFormat));
        numberOfLanesLabel.setText(getCompetition().getNumberOfLanes().toString());
        delayBetweenRelaysLabel.setText(String.valueOf(getCompetition().getDelayBetweenRelays().toMinutes()));

        resetExceptionLabels();
    }

    private void initEventComboBox() {
        ObservableList<EventType> eventType = FXCollections.observableList(getCompetition().getEventTypes());
        eventTypeComboBox.setItems(eventType);
    }

    private void resetExceptionLabels() {
        nameExceptionLabel.setText("");
        clubExceptionLabel.setText("");
        birthDayExceptionLabel.setText("");
        eventExceptionLabel.setText("");
        addExceptionLabel.setText("");
    }

    @FXML
    public void AddCompetitor(ActionEvent actionEvent) {
        resetExceptionLabels();
        if (validate()) {
            Competitor competitor = new Competitor(nameTextField.getText(),
                    Integer.parseInt(birthYearTextField.getText()),
                    clubTextField.getText(),
                    eventTypeComboBox.getValue(),
                    null
            );

            if (!getCompetition().addCompetitor(competitor)) {
                addExceptionLabel.setText("Competitor already in the list");
            }
        }
        table.refresh();
        System.out.println(getCompetition());
    }

    @FXML
    public void LoadNewEventView(ActionEvent actionEvent) throws IOException {
        super.loadView("AddNewEventType.fxml", actionEvent);
    }

    @FXML
    public void Back(ActionEvent actionEvent) throws IOException {
        super.loadView("competiton-view.fxml", actionEvent);
    }

    private boolean validate() {
        AbstractValidator<String> notEmptyString = new StringNotEmptyValidator();

        AbstractValidator<String> birthYearValidator = new StringNotEmptyValidator();
        birthYearValidator.add(new StringToIntegerValidator());
        birthYearValidator.add(new MaxIntegerValueValidator(LocalDate.now().getYear()));

        AbstractValidator<Object> eventTypeValidator = new NotNullValidator();


        return (notEmptyString.execute(nameTextField.getText(), nameExceptionLabel)
                & notEmptyString.execute(clubTextField.getText(), clubExceptionLabel)
                & birthYearValidator.execute(birthYearTextField.getText(), birthDayExceptionLabel)
                & eventTypeValidator.execute(eventTypeComboBox.getValue(), eventExceptionLabel));
    }

    @FXML
    public void DeletCompetitor(ActionEvent actionEvent) {
    }

    @FXML
    public void NextButten(ActionEvent actionEvent) throws IOException {
        loadView("Schedule-view.fxml", actionEvent);
    }

    @FXML
    public void GenerateNewScheduledButton(ActionEvent actionEvent) throws IOException {
        SimpleAssigner assigner = new SimpleAssigner();
        getCompetition().getSchedules().add(assigner.creatStartList(getCompetition()));
        loadView("Schedule-view.fxml", actionEvent);
    }
}
