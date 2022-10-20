package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner.MiAssigner;
import hu.unideb.inf.szakdolgozat.model.dao.EventTypeDAO;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
//import hu.unideb.inf.szakdolgozat.model.dto.Constraint;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import hu.unideb.inf.szakdolgozat.model.validator.*;
import hu.unideb.inf.szakdolgozat.model.dto.record.Constraint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public TableColumn<Competitor, String> eventTypeTableColum;
    @FXML
    public TableColumn<Competitor, String> timeConstrainTableColum;

    @FXML
    public TableView<Competitor> table;


    @FXML
    public Label fromTimeExceptionLabel;
    @FXML
    public DatePicker fromTimeDate;
    @FXML
    public TextField fromTimeHour;
    @FXML
    public TextField fromTimeMinute;
    @FXML
    public Label untilTimeExceptionLabel;
    @FXML
    public DatePicker untilTimeDate;
    @FXML
    public TextField untilTimeHour;
    @FXML
    public TextField untilTimeMinute;
    @FXML
    public CheckBox isConstrained;
    @FXML
    public ComboBox<Competitor> selectCompetitorComboBox;
    @FXML
    public Button addCompetitorButton;

    public Competitor currentCompetitor;

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        loadEventTypes();
        initLabels();
        initCompetitor();
        initEventComboBox();
        initTimeConstrains();

    }




    private void initCompetitor() {

        nameTableColum.setCellValueFactory(new PropertyValueFactory<>("name"));
        clubTableColum.setCellValueFactory(new PropertyValueFactory<>("club"));
        birthYearTableColum.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        eventTypeTableColum.setCellValueFactory(new PropertyValueFactory<>("eventTypeName"));
        timeConstrainTableColum.setCellValueFactory(new PropertyValueFactory<>("constrainInString"));

        ObservableList<Competitor> observableList = FXCollections.observableList(getCompetition().getCompetitors());
        table.setItems(observableList);
        selectCompetitorComboBox.setItems(observableList);
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


    private void initTimeConstrains() {
        isConstrained.setSelected(false);
        fromTimeDate.setValue(getCompetition().getTimeOfBeginning().toLocalDate());
        fromTimeHour.setText(Integer.toString(getCompetition().getTimeOfBeginning().getHour()));
        fromTimeMinute.setText(Integer.toString(getCompetition().getTimeOfBeginning().getMinute()));

        untilTimeDate.setValue(getCompetition().getTimeOfBeginning().toLocalDate());
        untilTimeHour.setText(Integer.toString(getCompetition().getTimeOfBeginning().getHour()));
        untilTimeMinute.setText(Integer.toString(getCompetition().getTimeOfBeginning().getMinute()));
    }

    private void loadEventTypes() {
            System.out.println("loadEventType");
            EventTypeDAO dao = getHandle().attach(EventTypeDAO.class);
            getCompetition().setEventTypes(dao.getEventTypeList());


    }

    private void resetExceptionLabels() {
        nameExceptionLabel.setText("");
        clubExceptionLabel.setText("");
        birthDayExceptionLabel.setText("");
        eventExceptionLabel.setText("");
        addExceptionLabel.setText("");
    }

    @FXML
    public void addCompetitor(ActionEvent actionEvent) {
        resetExceptionLabels();
        if (validate()) {
            Competitor competitor = new Competitor(nameTextField.getText(),
                    Integer.parseInt(birthYearTextField.getText()),
                    clubTextField.getText(),
                    eventTypeComboBox.getValue(),
                    isConstrained.isSelected(),
                    new Constraint(LocalDateTime.of(fromTimeDate.getValue(),
                            LocalTime.of(Integer.parseInt(fromTimeHour.getText()), Integer.parseInt(fromTimeMinute.getText()))),
                            LocalDateTime.of(untilTimeDate.getValue(),
                                    LocalTime.of(Integer.parseInt(untilTimeHour.getText()), Integer.parseInt(untilTimeMinute.getText()))))
            );

            if (!getCompetition().addCompetitor(competitor)) {
                addExceptionLabel.setText("Competitor already in the list");

                LocalTime.of(Integer.parseInt(fromTimeHour.getText()), Integer.parseInt(fromTimeMinute.getText()));
            }
        }
        table.refresh();
        ObservableList<Competitor> observableList = FXCollections.observableList(getCompetition().getCompetitors());
        selectCompetitorComboBox.setItems(observableList);
        System.out.println(getCompetition());
    }

    @FXML
    public void loadNewEventView(ActionEvent actionEvent) throws IOException {
        super.loadView("AddNewEventType.fxml", actionEvent);
    }

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        super.loadView("competiton-view.fxml", actionEvent);
    }

    private boolean validate() {
        AbstractValidator<String> notEmptyString = new StringNotEmptyValidator();

        AbstractValidator<String> birthYearValidator = new StringNotEmptyValidator();
        birthYearValidator.add(new StringToIntegerValidator());
        birthYearValidator.add(new MaxIntegerValueValidator(LocalDate.now().getYear()));

        AbstractValidator<Object> eventTypeValidator = new NotNullValidator();

        AbstractValidator<Object> dateValidator = new NotNullValidator();

        AbstractValidator<String> hourValidator = new StringNotEmptyValidator();
        hourValidator.add(new StringToIntegerValidator());
        hourValidator.add(new HourValidator());

        AbstractValidator<String> minuteValidator = new StringNotEmptyValidator();
        minuteValidator.add(new StringToIntegerValidator());
        minuteValidator.add(new MinuteValidator());


        return (notEmptyString.execute(nameTextField.getText(), nameExceptionLabel)
                & notEmptyString.execute(clubTextField.getText(), clubExceptionLabel)
                & birthYearValidator.execute(birthYearTextField.getText(), birthDayExceptionLabel)
                & eventTypeValidator.execute(eventTypeComboBox.getValue(), eventExceptionLabel)
                & dateValidator.execute(fromTimeDate.getValue(), fromTimeExceptionLabel)
                & hourValidator.execute(fromTimeHour.getText(), fromTimeExceptionLabel)
                & minuteValidator.execute(fromTimeMinute.getText(), fromTimeExceptionLabel)
                & dateValidator.execute(untilTimeDate.getValue(), untilTimeExceptionLabel)
                & hourValidator.execute(untilTimeHour.getText(), untilTimeExceptionLabel)
                & minuteValidator.execute(untilTimeMinute.getText(), untilTimeExceptionLabel));
    }

    @FXML
    public void deleteCompetitor(ActionEvent actionEvent) {
        getCompetition().getCompetitors().remove(currentCompetitor);
        cancel(actionEvent);
        table.refresh();
    }

    @FXML
    public void nextButton(ActionEvent actionEvent) throws IOException {
        loadView("Schedule-view.fxml", actionEvent);
    }

    @FXML
    public void generateNewScheduledButton(ActionEvent actionEvent) throws IOException {
        // SimpleAssigner assigner = new SimpleAssigner();
        MiAssigner assigner = new MiAssigner(getCompetition());
        getCompetition().getSchedules().add(assigner.creatStartList());
        loadView("Schedule-view.fxml", actionEvent);
    }

    public void selectShooter(ActionEvent actionEvent) {

        currentCompetitor = selectCompetitorComboBox.getValue();
        if (currentCompetitor != null) {
            addCompetitorButton.setText("Update");
            addCompetitorButton.setOnAction(this::updateCompetitor);
            nameTextField.setText(currentCompetitor.getName());
            clubTextField.setText(currentCompetitor.getClub());
            birthYearTextField.setText(currentCompetitor.getBirthYear().toString());
            isConstrained.setSelected(currentCompetitor.isConstrained());

            fromTimeDate.setValue(currentCompetitor.getConstrain().availableFromThatTime().toLocalDate());
            fromTimeHour.setText(Integer.toString(currentCompetitor.getConstrain().availableFromThatTime().getHour()));
            fromTimeMinute.setText(Integer.toString(currentCompetitor.getConstrain().availableFromThatTime().getMinute()));

            untilTimeDate.setValue(currentCompetitor.getConstrain().availableUntilThisTime().toLocalDate());
            untilTimeHour.setText(Integer.toString(currentCompetitor.getConstrain().availableUntilThisTime().getHour()));
            untilTimeMinute.setText(Integer.toString(currentCompetitor.getConstrain().availableUntilThisTime().getMinute()));

            eventTypeComboBox.getSelectionModel().select(currentCompetitor.getEventType());
        }
    }

    @FXML
    private void updateCompetitor(ActionEvent actionEvent) {
        if(validate()) {
            currentCompetitor.setName(nameTextField.getText());
            currentCompetitor.setBirthYear(Integer.parseInt(birthYearTextField.getText()));
            currentCompetitor.setClub(clubTextField.getText());
            currentCompetitor.setEventType(eventTypeComboBox.getValue());
            currentCompetitor.setConstrained(isConstrained.isSelected());
            currentCompetitor.setConstrain(new Constraint(LocalDateTime.of(fromTimeDate.getValue(),
                    LocalTime.of(Integer.parseInt(fromTimeHour.getText()), Integer.parseInt(fromTimeMinute.getText()))),
                    LocalDateTime.of(untilTimeDate.getValue(),
                            LocalTime.of(Integer.parseInt(untilTimeHour.getText()), Integer.parseInt(untilTimeMinute.getText())))));
            cancel(actionEvent);
            table.refresh();
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        addCompetitorButton.setText("Add");
        addCompetitorButton.setOnAction(this::addCompetitor);
        currentCompetitor = null;

        nameTextField.setText("");
        clubTextField.setText("");
        birthYearTextField.setText("");
        initTimeConstrains();
        eventTypeComboBox.getSelectionModel().clearSelection();
        selectCompetitorComboBox.getSelectionModel().clearSelection();

    }
}
