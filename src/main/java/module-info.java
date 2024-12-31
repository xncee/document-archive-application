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


    opens view to javafx.fxml;
    exports view;
    exports control;
    opens control to javafx.fxml;
    exports model;
    opens model to javafx.fxml;
    exports control.document;
    opens control.document to javafx.fxml;
    exports control.filter;
    opens control.filter to javafx.fxml;
    exports control.login;
    opens control.login to javafx.fxml;
    exports model.entities;
    opens model.entities to javafx.fxml;
    exports model.login;
    opens model.login to javafx.fxml;
}