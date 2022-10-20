module hu.unideb.inf.szakdolgozat {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.junit.jupiter.api;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires java.sql;

    opens hu.unideb.inf.szakdolgozat to javafx.fxml;
    exports hu.unideb.inf.szakdolgozat;
    exports hu.unideb.inf.szakdolgozat.controller;
    exports hu.unideb.inf.szakdolgozat.model.dto;
    exports hu.unideb.inf.szakdolgozat.model.dao;
    exports hu.unideb.inf.szakdolgozat.model.dto.view;
    opens hu.unideb.inf.szakdolgozat.controller to javafx.fxml;
    opens hu.unideb.inf.szakdolgozat.model.dao to org.jdbi.v3.sqlobject;
    exports hu.unideb.inf.szakdolgozat.model.assigner.MiAssigner;
}