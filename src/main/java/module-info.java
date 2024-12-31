module zarqa.governmentproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires opencv;
    requires java.desktop;
    requires com.google.protobuf;
    requires org.apache.poi.ooxml;
    requires kernel;
    requires layout;
    requires java.prefs;


    opens view to javafx.fxml;
    exports view;
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