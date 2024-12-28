package control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DashboardController {

    @FXML
    private HBox titleBar;

    @FXML
    private Button closeButton;

    @FXML
    private Button extendButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button notificationsButton;

    @FXML
    private Button userInfoButton;

    @FXML
    private Button filterButton;

    @FXML
    private TableView<?> documentTable;

    @FXML
    private TableColumn<?, ?> documentColumn;

    @FXML
    private TableColumn<?, ?> departmentColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> actionsColumn;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        setupTableColumns();
        loadDocuments();

        // Add drag functionality for the title bar
        titleBar.setOnMousePressed(this::handleMousePressed);
        titleBar.setOnMouseDragged(this::handleMouseDragged);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleExtend(ActionEvent event) {
        Stage stage = (Stage) extendButton.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
        stage.setFullScreenExitHint(""); // Optional: Hides fullscreen exit hint
    }

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleNotifications(ActionEvent event) {
        // handle notifications
    }
    @FXML
    private void handleUserInfo(ActionEvent event) {
        // handle user info logic
    }

    @FXML
    private void handleFilter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/results-filter-view.fxml"));
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
    private void handleGenerateReport(ActionEvent event) {
        // Implementation for generating a report
    }

    @FXML
    private void handleAddDocument(ActionEvent event) {
        // Implementation for adding a document
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        // Implementation for search functionality
    }

    @FXML
    private void handleHelpLink(ActionEvent event) {
        // Implementation for help link
    }

    @FXML
    private void handlePrivacyLink(ActionEvent event) {
        // Implementation for privacy link
    }

    @FXML
    private void handleTermsLink(ActionEvent event) {
        // Implementation for terms link
    }

    private void setupTableColumns() {
        // Implementation for setting up table columns
    }

    private void loadDocuments() {
        // Implementation for loading documents
    }

    private void handleMousePressed(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        if (!stage.isFullScreen()) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        }
    }
}
