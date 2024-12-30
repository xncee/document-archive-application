package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ContentSwitcher {
    private BorderPane mainContainer;

    public ContentSwitcher(BorderPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    // Method to switch content dynamically
    @FXML
    public void switchContent(ActionEvent event, String fxmlFile) throws IOException {
        // Load the new content from the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent newContent = loader.load();

        // Set the new content in the BorderPane (content area)
        mainContainer.setCenter(newContent);  // Use setCenter for BorderPane
    }
}
