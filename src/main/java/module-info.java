module zarqa.governmentproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires opencv;
    requires tess4j;
    requires kernel;
    requires layout;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens view to javafx.fxml;
    exports view;
}