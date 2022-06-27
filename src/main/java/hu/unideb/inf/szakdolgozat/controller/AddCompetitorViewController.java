package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.EventType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

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
    public Label ClubExceptionLabel;
    @FXML
    public Label birthDayExceptionLabel;
    @FXML
    public Label eventExceptionLabel;

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

    }

    private void initTable() {


        EventType eventType = new EventType("lpi 60", LocalTime.of(0, 10),
                LocalTime.of(0, 15), LocalTime.of(1, 15), 1);
       getCompetition().addCompetitor(new Competitor("kis andras", 2000, "xyClub",eventType,null ));

        nameTableColum.setCellValueFactory(new PropertyValueFactory<>("name"));
        clubTableColum.setCellValueFactory(new PropertyValueFactory<>("club"));
        birthYearTableColum.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        EventTypeTableColum.setCellValueFactory(new PropertyValueFactory<>("eventTypeName"));
        ObservableList<Competitor> observableSet = FXCollections.observableList(getCompetition().getCompetitors());
        table.setItems(observableSet);

    }

    private void initLabels() {
        competitionNameLabel.setText(getCompetition().getName());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        startTimeLabel.setText(getCompetition().getTimeOfBeginning().format(dateFormat));
        numberOfLanesLabel.setText(getCompetition().getNumberOfLanes().toString());
        dateFormat = DateTimeFormatter.ofPattern("HH:mm");
        delayBetweenRelaysLabel.setText(getCompetition().getDelayBetweenRelays().format(dateFormat));

        nameExceptionLabel.setText("");
        ClubExceptionLabel.setText("");
        birthDayExceptionLabel.setText("");
        eventExceptionLabel.setText("");
    }

    public void AddCompetitor(ActionEvent actionEvent) {
        EventType eventType = new EventType("lpi 60", LocalTime.of(0, 10),
                LocalTime.of(0, 15), LocalTime.of(1, 15), 1);
        getCompetition().addCompetitor(new Competitor("kis andras", 22000, "xyClub",eventType,null ));
        table.refresh();
        System.out.println(getCompetition());
    }
}
