package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
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

    public static void setMainContainer(BorderPane mainContainer) {
        ContentSwitcher.mainContainer = mainContainer;
    }
}
