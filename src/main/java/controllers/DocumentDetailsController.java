package controllers;

import application.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DocumentDetailsController {

    @FXML
    public void handleBackAction(ActionEvent event) {

        ContentSwitcher.switchToPreviousPage();
    }
    @FXML
    public void handleShareAction(ActionEvent event) {

    }
    @FXML
    public void handleAddFileAction(ActionEvent event) {

    }
    @FXML
    public void handleCommentSubmit(ActionEvent event) {

    }
}
