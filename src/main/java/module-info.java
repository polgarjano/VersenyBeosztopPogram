module hu.unideb.inf.szakdolgozat {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.junit.jupiter.api;



    opens hu.unideb.inf.szakdolgozat to javafx.fxml;
    exports hu.unideb.inf.szakdolgozat;
    exports hu.unideb.inf.szakdolgozat.controller;
    exports hu.unideb.inf.szakdolgozat.model.dto;
    exports hu.unideb.inf.szakdolgozat.model.dto.view;
    opens hu.unideb.inf.szakdolgozat.controller to javafx.fxml;
    exports hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner;
}