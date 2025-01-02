package view;

import data.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.UserPreffrences;
import utils.ContentSwitcher;
import utils.LocalizationUtil;

import java.io.IOException;
import java.util.Locale;

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
        // setting application icon
        Image icon = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        primaryStage.getIcons().add(icon);
        // Set user preferred language
        LocalizationUtil.setLocale(new Locale(UserPreffrences.getLanguage()));
        // Loading the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-layout-view.fxml"));
        BorderPane root = loader.load();
        // setting mainContainer to ContentSwitcher
        ContentSwitcher.setMainContainer(root);
        // Loading the content
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("signin-view.fxml"), LocalizationUtil.getResourceBundle());
        // setting content at the center
        root.setCenter(loader1.load());

        // If arabic, switch direction
        if ("ar".equals(UserPreffrences.getLanguage())) {
            ContentSwitcher.setDirectionRTL();
        }
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

        primaryStage.setMinWidth(300);  // Minimum width for the application window
        primaryStage.setMinHeight(200); // Minimum height for the application window
        primaryStage.setMaximized(true);
        // Set the initial size of the window to fit within the screen
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
    }
}
