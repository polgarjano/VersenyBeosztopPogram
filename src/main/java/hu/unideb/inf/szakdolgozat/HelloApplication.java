package hu.unideb.inf.szakdolgozat;

import hu.unideb.inf.szakdolgozat.controller.AbstractController;
import hu.unideb.inf.szakdolgozat.model.dao.CompetitionDAO;
import hu.unideb.inf.szakdolgozat.model.dao.EventTypeDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;


import java.io.IOException;

public class HelloApplication extends Application {
    Handle handle;

    @Override
    public void start(Stage stage) throws IOException {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());
        handle = jdbi.open();
        EventTypeDAO dao = handle.attach(EventTypeDAO.class);
        CompetitionDAO CompetitionDao =handle.attach(CompetitionDAO.class);
        dao.createTable();
        CompetitionDao.createTable();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startView.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        AbstractController controller = fxmlLoader.<AbstractController>getController();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        controller.setHandle(handle);
        controller.init(null);
    }

    @Override
    public void stop() {
        handle.close();
    }

    public static void main(String[] args) {
        launch();
    }
}