package application;

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
import utils.FXMLCache;
import services.UserPreffrences;
import utils.LocalizationUtil;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application  {
    private static final String[] PAGES = {"/view/signin-view.fxml", "/view/signup-view.fxml"};
    private static final ContentSwitcher contentSwitcher = ContentSwitcher.getInstance();
    private final Stage primaryStage;

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
    private void preloadPages() {
        for (String page: PAGES) {
            FXMLCache.preloadFXML(page);
        }
    }
    private void start() throws IOException {
        // setting application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png")));
        primaryStage.getIcons().add(icon);
        // Set user preferred language
        LocalizationUtil.setLocale(new Locale(UserPreffrences.getLanguage()));

        // Loading the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-layout-view.fxml"));
        BorderPane root = loader.load();
        // setting mainContainer to ContentSwitcher
        contentSwitcher.setMainContainer(root);

        // preloading static pages
        new Thread(this::preloadPages).start();

        // Loading the content
        contentSwitcher.switchContent("/view/signin-view.fxml"); // this page was already preloaded

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void setup_database() {
        try {
            DBFacade dbFacade = DBFacade.getInstance(Dotenv.load().get("DATABASE_URL"));
            if (!dbFacade.isConnected()) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Failed to connect to database");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to database!");
                alert.showAndWait();
                throw new RuntimeException("database connection failed.");
            }
        }
        catch (Exception e) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Failed to connect to database", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database connection failed! \n"+e.getMessage());
            alert.showAndWait();
            throw new RuntimeException("Database connection failed!");
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
