package view;

import control.ContentSwitcher;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Login;
import model.database.DBFacade;
import model.database.DBManager;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        DBFacade dbFacade = new DBFacade(Dotenv.load().get("DATABASE_URL"));
        if (!dbFacade.isConnected()) {
            throw new RuntimeException("database connection failed.");
        }
        //main-layout-view.fxml
        //document-upload-view.fxml
        //dashboard-view.fxml
        //signin-view.fxml

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
