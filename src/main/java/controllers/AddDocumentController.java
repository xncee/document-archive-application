package controllers;

import data.DBFacade;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import models.Document;
import models.login.Login;
import services.DocumentServices;
import services.FieldsServices;
import utils.FilesServices;
import utils.FilesSystem;
import application.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.Messages;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddDocumentController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField documentId;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private ComboBox<String> classificationComboBox;
    @FXML
    private DatePicker deadline;
    @FXML
    private Hyperlink browseFilesLink;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    private String filePath;

    @FXML
    private void initialize() {
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        DBFacade dbFacade = DBFacade.getInstance();

        // Add Departments
        List<Map<String, Object>> departments = dbFacade.getDepartments();
        for (Map<String, Object> dept: departments) {
            departmentComboBox.getItems().add((String) dept.get("name"));
        }

        // Add Classifications
        List<Map<String, Object>> classifications = dbFacade.getClassifications();
        for (Map<String, Object> cls: classifications) {
            classificationComboBox.getItems().add((String) cls.get("name"));
        }
    }

    public void setFieldValid(Node field) {
        FieldsServices.setFieldValid(field);
    }
    public void setFieldInvalid(Node field, String errorMessage) {
        FieldsServices.setRedBorder(field);
        FieldsServices.addErrorMessage(errorLabel, errorMessage);
    }

    private boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    private boolean validateForm() {
        errorLabel.setText("");
        boolean valid = true;
        // documentId is optional

        // Title validation
        if (title.getText().isBlank()) {
            setFieldInvalid(title, "Title: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(title);
        }
        // Description validation
        if (description.getText().isBlank()) {
            setFieldInvalid(description, "Description: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(description);
        }
        // Department validation
        if (departmentComboBox.getValue() == null) {
            setFieldInvalid(departmentComboBox, "Department: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(departmentComboBox);
        }
        // Classification validation
        if (classificationComboBox.getValue() == null) {
            setFieldInvalid(classificationComboBox, "Classification: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(classificationComboBox);
        }
        // FilePath validation
        if (filePath != null && !doesFileExist(filePath)) {
            setFieldInvalid(browseFilesLink, "File: "+Messages.FILE_IS_INVALID);
            valid = false;
        } else {
            setFieldValid(browseFilesLink);
        }
        return valid;
    }

    @FXML
    public void handleBrowseFiles(ActionEvent event) {
        File file = FilesServices.askForFileLocation();
        if (file != null) {
            filePath = file.getAbsolutePath();
            browseFilesLink.setText(file.getName());
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        System.out.println("Operation canceled.");
        System.out.println("Switching to dashboard...");
        ContentSwitcher.switchContent("/view/dashboard-view.fxml");
        clearForm();
    }

    @FXML
    public void handleSave(ActionEvent event) throws IOException {
        if (!validateForm()) {
            // display some error message.
            return;
        }

        // Copy the file to application directory.
        // returns the copied file path or null if failed to copy
        if (filePath != null) {
            String copiedFile = FilesSystem.copyFile(filePath);
            // if copied, update filePath
            if (copiedFile != null) {
                filePath = copiedFile;
            }
        }

        int uploaderId = Login.getInstance().getId();
        Document.Builder documentBuilder = new Document.Builder(
                "Pending",
                uploaderId, title.getText(),
                description.getText(),
                departmentComboBox.getValue(),
                classificationComboBox.getValue());

        // createdDate is current date by default.
        // updatedDate is initially null.
        documentBuilder.id(documentId.getText()).deadline(deadline.getValue()).filePath(filePath);

        Document document = documentBuilder.build();
        if (!DocumentServices.saveDocument(document)) {
            errorLabel.setText("Upload failed.");
            return;
        }
        showSuccessAlert();
        System.out.println("Document added "+document.getId());
        System.out.println("Switching to dashboard...");
        ContentSwitcher.switchContent("/view/dashboard-view.fxml");
        ContentSwitcher.reloadPage();
        clearForm();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Document saved successfully!");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void clearForm() {
        errorLabel.setText("");
        title.clear();
        description.clear();
        departmentComboBox.setValue(null);
        classificationComboBox.setValue(null);
        deadline.setValue(null);
        browseFilesLink.setVisited(false);
        filePath = null;
    }

}
