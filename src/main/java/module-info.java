module documentArchive {
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires opencv;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires java.prefs;
    requires kernel;
    requires layout;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.zaxxer.hikari;


    opens application to javafx.fxml;
    exports application;
    exports controllers;
    opens controllers to javafx.fxml;
    exports models;
    opens models to javafx.fxml;
    exports utils;
    opens utils to javafx.fxml;
    exports services;
    opens services to javafx.fxml;
    exports validators;
    opens validators to javafx.fxml;
    exports data;
    opens data to javafx.fxml;
}