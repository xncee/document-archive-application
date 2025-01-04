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
import utils.UserPreffrences;
import utils.LocalizationUtil;
import java.io.IOException;
import java.util.Locale;

public class Application  {
    private static final String[] PAGES = {"/view/signin-view.fxml", "/view/signup-view.fxml", "/view/document-upload-view.fxml", "/view/generate-report-view.fxml"};
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
    private void preloadPages() {
        for (String page: PAGES) {
            try {
                FXMLCache.preloadFXML(page);
            } catch (IOException e) {
                System.out.println("Failed to load: "+page);
                e.printStackTrace();
            }
        }
    }
    private void start() throws IOException {
        // setting application icon
        Image icon = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        primaryStage.getIcons().add(icon);
        // Set user preferred language
        LocalizationUtil.setLocale(new Locale(UserPreffrences.getLanguage()));

        // Loading the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-layout-view.fxml"));
        BorderPane root = loader.load();
        // setting mainContainer to ContentSwitcher
        ContentSwitcher.setMainContainer(root);
        // If arabic, switch direction
        if ("ar".equals(UserPreffrences.getLanguage())) {
            ContentSwitcher.switchDirection("right-to-left", "right");
        }

        // preloading static pages
        new Thread(this::preloadPages).start();

        // Loading the content
        ContentSwitcher.switchContent("/view/signin-view.fxml", true); // this page was already preloaded

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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Database connection failed! \n"+e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
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
