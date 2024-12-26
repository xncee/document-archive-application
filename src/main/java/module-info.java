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
}