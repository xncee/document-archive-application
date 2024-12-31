package control.login;

import control.ContentSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import model.FieldsValidator;
import model.login.Login;
import model.Messages;
import model.login.SignUpServices;

import java.io.IOException;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField fullnameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;
    @FXML private Label errorLabel;

    private void setFieldInvalid(TextField field, String errorMessage) {
        // Set the field border color to red to indicate error
        field.setStyle("-fx-border-color: red");

        // Only update the error label if the error message is not null or empty
        if (errorMessage != null && !errorMessage.isEmpty()) {
            // Prepend the error message with asterisk (*) and append the new error message
            String old = errorLabel.getText();
            errorLabel.setText(old+"* " + errorMessage+"\n");
            errorLabel.setVisible(true);  // Make sure the error label is visible
        }
    }


    private void setFieldValid(TextField field) {
        field.setStyle("-fx-border-color: #d4d4d4");
    }
    private boolean validateForm() {
        errorLabel.setText("");
        boolean valid = true;

        // Entry Checking
        if (!termsCheckbox.isSelected()) {
            //showError("Please accept the terms.");
            valid = false;
        }

        // Username validation
        if (usernameField.getText().isEmpty()) {
            setFieldInvalid(usernameField, "");
            valid = false;
        } else if (!FieldsValidator.validateUsername(usernameField.getText())) {
            setFieldInvalid(usernameField, Messages.USERNAME_REQUIREMENTS_NOT_MET.getMessage());
            valid = false;
        } else if (!SignUpServices.isUsernameAvailable(usernameField.getText())) {
            setFieldInvalid(usernameField, Messages.USERNAME_TAKEN.getMessage());
            valid = false;
        } else {
            setFieldValid(usernameField);
        }

        // Email validation
        if (emailField.getText().isEmpty()) {
            setFieldInvalid(emailField, "");
            valid = false;
        } else if (!FieldsValidator.validateEmail(emailField.getText())) {
            setFieldInvalid(emailField, Messages.EMAIL_REQUIREMENTS_NOT_MET.getMessage());
            valid = false;
        } else if (!SignUpServices.isEmailAvailable(emailField.getText())) {
            setFieldInvalid(emailField, Messages.EMAIL_ALREADY_REGISTERED.getMessage());
            valid = false;
        } else {
            setFieldValid(emailField);
        }

        // Fullname validation
        if (fullnameField.getText().isEmpty()) {
            setFieldInvalid(fullnameField, "");
            valid = false;
        }
        else {
            setFieldValid(fullnameField);
        }

        // Password validation
        if (passwordField.getText().isEmpty()) {
            setFieldInvalid(passwordField, "");
            valid = false;
        } else if (!FieldsValidator.validatePassword(passwordField.getText())) {
            setFieldInvalid(passwordField, Messages.PASSWORD_REQUIREMENTS_NOT_MET.getMessage());
            valid = false;
        } else {
            setFieldValid(passwordField);
        }

        // Confirm Password validation
        if (confirmPasswordField.getText().isEmpty()) {
            setFieldInvalid(confirmPasswordField, "");
            valid = false;
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            confirmPasswordField.setText("");
            setFieldInvalid(confirmPasswordField, Messages.PASSWORD_CONFIRMATION_MISMATCH.getMessage());
            valid = false;
        } else {
            setFieldValid(confirmPasswordField);
        }

        return valid;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
    @FXML
    public void handleSignup(ActionEvent event) throws IOException {
        if (!validateForm()) {
            showError("Please fill in all fields correctly and accept the terms.");
            return;
        }


        Login login = new Login();
        if (!login.signUp(usernameField.getText(), emailField.getText(), fullnameField.getText(), passwordField.getText())) {
            errorLabel.setText("Login Failed.");
            return;
        }
        System.out.println("Signed up "+usernameField.getText());
        ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
    }

    @FXML
    public void handleSignin(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/signin-view.fxml");
    }
}