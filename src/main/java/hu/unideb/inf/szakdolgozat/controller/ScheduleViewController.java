package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.model.dao.CompetitionDAO;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import hu.unideb.inf.szakdolgozat.model.dto.Competitor;
import hu.unideb.inf.szakdolgozat.model.dto.Relay;
import hu.unideb.inf.szakdolgozat.model.dto.Schedule;
import hu.unideb.inf.szakdolgozat.model.dto.view.ScheduledCompetitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.jdbi.v3.core.Handle;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ScheduleViewController extends AbstractController {
    @FXML
    public javafx.scene.control.TreeTableView<ScheduledCompetitor> TreeTableView;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> relayNumber;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> startTime;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> endTime;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> fireingPalce;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> name;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> club;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> birthYear;
    @FXML
    public TreeTableColumn<ScheduledCompetitor, String> eventType;

    @FXML
    public void back(ActionEvent actionEvent) throws IOException {
        super.loadView("addCompetitorView.fxml", actionEvent);
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        getHandle().useTransaction(h -> {
            CompetitionDAO dao = h.attach(CompetitionDAO.class);

            if (getCompetition().getId() == null) {
                getCompetition().setId(dao.saveCompetition(getCompetition()));
            } else {
                dao.updateCompetition(getCompetition());
            }
        });
    }

    @Override
    public void init(Competition competition) {
        setCompetition(competition);
        initTableColum();
        TreeTableView.setRoot(getScheduleTree(competition.getSchedules()));
    }

    private void initTableColum() {
        relayNumber.setCellValueFactory(new TreeItemPropertyValueFactory<>("relayNumber"));
        startTime.setCellValueFactory(new TreeItemPropertyValueFactory<>("startTime"));
        endTime.setCellValueFactory(new TreeItemPropertyValueFactory<>("endTime"));
        fireingPalce.setCellValueFactory(new TreeItemPropertyValueFactory<>("fireingPalce"));
        name.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        club.setCellValueFactory(new TreeItemPropertyValueFactory<>("club"));
        birthYear.setCellValueFactory(new TreeItemPropertyValueFactory<>("birthYear"));
        eventType.setCellValueFactory(new TreeItemPropertyValueFactory<>("eventType"));
    }

    private TreeItem<ScheduledCompetitor> getScheduleTree(List<Schedule> schedules) {

        TreeItem<ScheduledCompetitor> root = new TreeItem<>(ScheduledCompetitor.getRootSchedule());

        root.getChildren().addAll(getScheduleTrees(schedules));


        return root;
    }

    private List<TreeItem<ScheduledCompetitor>> getScheduleTrees(List<Schedule> schedules) {

        List<TreeItem<ScheduledCompetitor>> treeItems = new LinkedList<>();

        for (int i = 0; i < schedules.size(); i++) {
            TreeItem<ScheduledCompetitor> scheduleTreeItem = new TreeItem<>(ScheduledCompetitor.getScheduleRoot(i));
            scheduleTreeItem.getChildren().addAll(getRelayTrees(schedules.get(i).getRelays()));
            treeItems.add(scheduleTreeItem);
        }

        return treeItems;
    }

    private List<TreeItem<ScheduledCompetitor>> getRelayTrees(List<Relay> relays) {

        List<TreeItem<ScheduledCompetitor>> treeItems = new LinkedList<>();

        for (int i = 0; i < relays.size(); i++) {
            TreeItem<ScheduledCompetitor> relayTreeItem = new TreeItem<>(ScheduledCompetitor.getRelayRoot(relays.get(i)));
            relayTreeItem.getChildren().addAll(getCompetitors(relays.get(i).getCompetitors()));
            treeItems.add(relayTreeItem);
        }

        return treeItems;
    }

    private List<TreeItem<ScheduledCompetitor>> getCompetitors(List<Competitor> competitors) {
        List<TreeItem<ScheduledCompetitor>> treeItems = new LinkedList<>();

        for (int i = 0; i < competitors.size(); i++) {
            TreeItem<ScheduledCompetitor> relayTreeItem = new TreeItem<>(ScheduledCompetitor.getCompetitor(i, competitors.get(i)));
            treeItems.add(relayTreeItem);
        }

        return treeItems;
    }
}


