module hu.unideb.inf.szakdolgozat {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.unideb.inf.szakdolgozat to javafx.fxml;
    exports hu.unideb.inf.szakdolgozat;
}