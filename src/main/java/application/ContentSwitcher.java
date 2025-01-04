package application;

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
import utils.FXMLCache;
import utils.LocalizationUtil;

import java.io.IOException;
import java.util.Stack;

public class ContentSwitcher {
    private static BorderPane mainContainer = null;
    private static String currentFile;
    private static Stack<Parent> pages = new Stack<>();

    // Method to switch content dynamically using preloaded pages
    @FXML
    public static void switchContent(String fxmlFile, boolean forceReload) throws IOException {
        currentFile = fxmlFile;
        // Check if the FXML file is already preloaded
        Parent content = FXMLCache.getFXML(fxmlFile);
        if (content == null || forceReload) {
            // If not preloaded, load it and add it to the cache
            FXMLCache.preloadFXML(fxmlFile);
            content = FXMLCache.getFXML(fxmlFile);
        }

        // Set the content in the center of the BorderPane (main container)
        pages.push(content);
        mainContainer.setCenter(content);
    }

    @FXML
    public static void switchContent(String fxmlFile) throws IOException {
        switchContent(fxmlFile, false);
    }

    @FXML
    public static void offload() {
        pages.pop();
    }

    @FXML
    public static void reloadPage() throws IOException {
        Parent content = FXMLCache.preloadFXML(currentFile);
        mainContainer.setCenter(content);
    }

    @FXML
    public static void switchToPreviousPage() {
        Parent current = null;
        if (!pages.isEmpty())
            current = pages.pop();
        try {
            mainContainer.setCenter(pages.peek());
        }
        catch (Exception e) {
            pages.push(current); // if switching failed, push the page back into the stack.
            System.out.println("Failed to switch to previous page because this page has no previous pages.");
            //e.printStackTrace();
        }
    }

    @FXML
    public static Stage getStage(Event event) {
        return (Stage) ((Node) (event.getSource())).getScene().getWindow();
    }

    @FXML
    public static void popUpWindow(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(ContentSwitcher.class.getResource(fxmlFile), LocalizationUtil.getResourceBundle());
            Parent root = loader.load();

            if (root == null) {
                System.out.println("FXML root is null. Check the file path or FXML file.");
                return;
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage modularStage = new Stage();
            modularStage.initStyle(StageStyle.UNIFIED);
            modularStage.setResizable(false);
            modularStage.initModality(Modality.WINDOW_MODAL);
            modularStage.initOwner(stage);
            Scene scene = new Scene(root);
            modularStage.setScene(scene);
            modularStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + fxmlFile);
            e.printStackTrace();
        }
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
