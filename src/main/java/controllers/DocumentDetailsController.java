package controllers;

import application.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DocumentDetailsController {
    private static final ContentSwitcher contentSwitcher = ContentSwitcher.getInstance();

    @FXML
    public void handleBackAction(ActionEvent event) {

        contentSwitcher.switchToPreviousPage();
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
