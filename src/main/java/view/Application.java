package view;

import data.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ContentSwitcher;

import java.io.IOException;

public class Application  {
    private Stage primaryStage;

    public Application(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setup_database();
        setup_screen();
        try {
            start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() throws IOException {
        // Loading the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-layout-view.fxml"));
        BorderPane root = loader.load();
        // Loading the content
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("signin-view.fxml"));
        // setting content at the center
        root.setCenter(loader1.load());
        // setting mainContainer to ContentSwitcher
        ContentSwitcher.setMainContainer(root);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void setup_database() {
        try {
            DBFacade dbFacade = DBFacade.getInstance(Dotenv.load().get("DATABASE_URL"));
            if (!dbFacade.isConnected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to database!");
                alert.showAndWait();
                throw new RuntimeException("database connection failed.");
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database connection string wasn't found!");
            alert.showAndWait();
            throw new RuntimeException("database connection string wasn't found.");
        }
    }
    private void setup_screen() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the stage's minimum width and height so it doesn't exceed the screen size
        primaryStage.setMinWidth(300);  // Minimum width for the application window
        primaryStage.setMinHeight(200); // Minimum height for the application window

        // Set the initial size of the window to fit within the screen
        primaryStage.setWidth(screenBounds.getWidth()*0.8);
        primaryStage.setHeight(screenBounds.getHeight()*0.8);
    }
}
