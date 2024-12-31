package view;

import utils.ContentSwitcher;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import data.DBFacade;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        DBFacade dbFacade = new DBFacade(Dotenv.load().get("DATABASE_URL"));
        if (!dbFacade.isConnected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to database!");
            alert.showAndWait();
            throw new RuntimeException("database connection failed.");
        }
        //main-layout-view.fxml
        //signin-view.fxml
        //signup-view.fxml
        //dashboard-view.fxml
        //document-upload-view.fxml
        //generate-report-view.fxml
        //document-view.fxml
        //user-profile-view.fxml

        // Loading the main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-layout-view.fxml"));
        BorderPane root = loader.load();
        ContentSwitcher.setMainContainer(root); // setting mainContainer

        // Loading the content
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("signin-view.fxml"));
        // setting content at the center
        root.setCenter(loader1.load());

        // Set the icon to the stage
        Image icon = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
