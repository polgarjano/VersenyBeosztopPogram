module hu.unideb.inf.szakdolgozat {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens hu.unideb.inf.szakdolgozat to javafx.fxml;
    exports hu.unideb.inf.szakdolgozat;
    exports hu.unideb.inf.szakdolgozat.controller;
    opens hu.unideb.inf.szakdolgozat.controller to javafx.fxml;
}