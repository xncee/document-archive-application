package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);

        Image icon = new Image(getClass().getResourceAsStream("/icons/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        //HBox titleBar = (HBox) root.lookup("#titleBar");
        //DragUtil.makeDraggable(stage, titleBar);

        // Set the icon to the stage
        stage.getIcons().add(icon);
        stage.show();
    }
}
