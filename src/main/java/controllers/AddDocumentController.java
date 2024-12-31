package controllers;

import services.FieldsServices;
import utils.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.Messages;

import java.io.IOException;

public class AddDocumentController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField documentId;
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private ComboBox<String> department;
    @FXML
    private ComboBox<String> classification;

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        // add departments & classifications to combo box
        setDepartments();
        setClassifications();

    }

    private void setDepartments() {
        department.getItems().addAll("Option 1", "Option 2", "Option 3", "Option 4");

        // Set a default value (optional)
        department.setValue("Option 1");
    }
    private void setClassifications() {

    }
    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
    }
    public void setFieldValid(TextField field) {
        FieldsServices.setFieldValid(field);
    }
    public void setFieldInvalid(TextField field, String errorMessage) {
        FieldsServices.setFieldInvalid(field, errorLabel, errorMessage);
    }
    private boolean validateForm() {
        boolean valid = true;
        // validation logic here
        // documentId is optional
        if (title.getText().isBlank()) {
            setFieldInvalid(title, "Title: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(title);
        }
        if (description.getText().isBlank()) {
            setFieldInvalid(title, "Title: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(description);
        }
        if (department.getValue() == null) {
            FieldsServices.setRedBorder(department);
        } else {
            FieldsServices.resetBorderColor(department);
        }
        if (classification.getValue() == null) {
            FieldsServices.setRedBorder(classification);
        } else {
            FieldsServices.resetBorderColor(classification);
        }

        return valid;
    }
    @FXML
    public void handleSave(ActionEvent event) throws IOException {
        // add document logic here
        if (!validateForm()) {
            // display some error message.
            return;
        }

        ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
    }
}
