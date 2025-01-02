package utils;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ContentSwitcher {
    private static BorderPane mainContainer = null;

    // Method to switch content dynamically
    @FXML
    public static void switchContent(ActionEvent event, String fxmlFile) throws IOException {
        // Load the new content from the FXML file
        FXMLLoader loader = new FXMLLoader(ContentSwitcher.class.getResource(fxmlFile));
        Parent newContent = loader.load();

        // Set the new content in the BorderPane (content area)
        mainContainer.setCenter(newContent);  // Use setCenter for BorderPane
    }

    @FXML
    public static Stage getStage(Event event) {
        return (Stage) ((Node) (event.getSource())).getScene().getWindow();
    }
    @FXML
    public static void popUpWindow(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(ContentSwitcher.class.getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage modularStage = new Stage();
        modularStage.initStyle(StageStyle.TRANSPARENT);
        modularStage.initModality(Modality.WINDOW_MODAL);
        modularStage.initOwner(stage);
        Scene scene = new Scene(root);
        modularStage.setScene(scene);
        modularStage.showAndWait();
    }

    @FXML
    public static void setDirectionLTR() {
        mainContainer.setStyle("-fx-direction: ltr;");
    }

    @FXML
    public static void setDirectionRTL() {
        mainContainer.setStyle("-fx-direction: rtl;");
    }

    @FXML
    public static void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void setMainContainer(BorderPane mainContainer) {
        ContentSwitcher.mainContainer = mainContainer;
    }
}
