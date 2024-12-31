package services;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FieldsServices {

    // Method to set the field as valid

    public static void setRedBorder(Node element) {
        element.setStyle("-fx-border-color: red");
    }
    public static void resetBorderColor(Node element) {
        element.setStyle("-fx-border-color: #d4d4d4");
    }
    public static void setFieldValid(Node element) {
        resetBorderColor(element);
    }
    // Method to set the field as invalid and display an error message
    public static void setFieldInvalid(TextField field, Label errorLabel, String errorMessage) {
        // Set the field border color to red to indicate error
        setRedBorder(field);
        // Only update the error label if the error message is not null or empty
        if (errorLabel != null && errorMessage != null && !errorMessage.isEmpty()) {
            // append the new error message
            String old = errorLabel.getText();
            errorLabel.setText(old + "* " + errorMessage + "\n");
            errorLabel.setVisible(true);  // Make sure the error label is visible
        }
    }
    public static void setFieldInvalid(TextField field) {
        setFieldInvalid(field, null, null);
    }
}