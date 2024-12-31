package services;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FieldsServices {
    public static void setRedBorder(Node element) {
        element.setStyle("-fx-border-color: red");
    }
    private static void resetBorderColor(Node element) {
        element.setStyle("-fx-border-color: #d4d4d4");
    }
    public static void setFieldValid(Node element) {
        resetBorderColor(element);
    }
    // Method to set the field as invalid and display an error message
    public static void addErrorMessage(Label errorLabel, String errorMessage) {
        // Only update the error label if the error message is not null or empty
        if (errorLabel != null && errorMessage != null && !errorMessage.isEmpty()) {
            // append the new error message
            String old = errorLabel.getText();
            errorLabel.setText(old + "* " + errorMessage + "\n");
            errorLabel.setVisible(true);  // Make sure the error label is visible
        }
    }
}