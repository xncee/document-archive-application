package controllers;

import data.DBFacade;
import javafx.scene.Node;
import javafx.scene.control.*;
import models.Document;
import models.login.Login;
import services.DocumentServices;
import services.FieldsServices;
import utils.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import utils.Messages;

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
    private Button cancelButton;
    @FXML
    private Button saveButton;

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

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
    }
    public void setFieldValid(Node field) {
        FieldsServices.setFieldValid(field);
    }
    public void setFieldInvalid(Node field, String errorMessage) {
        FieldsServices.setRedBorder(field);
        FieldsServices.addErrorMessage(errorLabel, errorMessage);
    }
    private boolean validateForm() {
        errorLabel.setText("");
        boolean valid = true;
        // documentId is optional
        if (title.getText().isBlank()) {
            setFieldInvalid(title, "Title: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(title);
        }
        if (description.getText().isBlank()) {
            setFieldInvalid(description, "Description: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(description);
        }
        if (departmentComboBox.getValue() == null) {
            setFieldInvalid(departmentComboBox, "Department: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(departmentComboBox);
        }
        if (classificationComboBox.getValue() == null) {
            setFieldInvalid(classificationComboBox, "Classification: "+Messages.THIS_FIELD_IS_REQUIRED.getMessage());
            valid = false;
        } else {
            setFieldValid(classificationComboBox);
        }

        return valid;
    }
    @FXML
    public void handleSave(ActionEvent event) throws IOException {
        if (!validateForm()) {
            // display some error message.
            return;
        }

        int uploaderId = Login.getInstance().getId();
        Document.Builder documentBuilder = new Document.Builder(
                "Pending",
                uploaderId, title.getText(),
                description.getText(),
                departmentComboBox.getValue(),
                classificationComboBox.getValue());

        // created date is current date by default.
        // updated date is initially null.
        String filePath = "C:\\Users\\xncee\\OneDrive\\Desktop\\testReport.pdf";
        documentBuilder.id(documentId.getText()).deadline(deadline.getValue()).filePath(filePath);

        Document document = documentBuilder.build();
        if (!DocumentServices.saveDocument(document)) {
            errorLabel.setText("Upload failed.");
            return;
        }
        else {
            System.out.println("Document added "+document.getId());
            ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
        }
    }
}
