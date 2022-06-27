package hu.unideb.inf.szakdolgozat.controller;

import hu.unideb.inf.szakdolgozat.HelloApplication;
import hu.unideb.inf.szakdolgozat.model.dto.Competition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {
    private Competition competition;

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }


    public void loadView(String viewName, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(viewName));
        Parent root = fxmlLoader.load();
        AbstractController controller = fxmlLoader.<AbstractController>getController();
        controller.init(competition);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    abstract public void init(Competition competition);
}
