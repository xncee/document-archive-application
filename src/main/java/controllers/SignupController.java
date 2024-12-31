package controllers;

import utils.ContentSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import services.FieldsServices;
import utils.ErrorHandler;
import validators.FieldsValidator;
import models.login.Login;
import utils.Messages;
import services.SignUpServices;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField fullnameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;
    @FXML private Label errorLabel;

    private void setFieldInvalid(TextField field, String errorMessage) {
        FieldsServices.setRedBorder(field);
        FieldsServices.addErrorMessage(errorLabel, errorMessage);
    }

    private void setFieldValid(TextField field) {
        FieldsServices.setFieldValid(field);
    }
    private boolean validateForm() throws SQLException {
        errorLabel.setText("");
        boolean valid = true;

        // Entry Checking
        if (!termsCheckbox.isSelected()) {
            //showError("Please accept the terms.");
            valid = false;
        }

        // Username validation
        if (usernameField.getText().isBlank()) {
            setFieldInvalid(usernameField, null);
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
        if (emailField.getText().isBlank()) {
            setFieldInvalid(emailField, null);
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
        if (fullnameField.getText().isBlank()) {
            setFieldInvalid(fullnameField, null);
            valid = false;
        }
        else {
            setFieldValid(fullnameField);
        }

        // Password validation
        if (passwordField.getText().isBlank()) {
            setFieldInvalid(passwordField, null);
            valid = false;
        } else if (!FieldsValidator.validatePassword(passwordField.getText())) {
            setFieldInvalid(passwordField, Messages.PASSWORD_REQUIREMENTS_NOT_MET.getMessage());
            valid = false;
        } else {
            setFieldValid(passwordField);
        }

        // Confirm Password validation
        if (confirmPasswordField.getText().isBlank()) {
            setFieldInvalid(confirmPasswordField, null);
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
    public void handleSignup(ActionEvent event) {
        try {
            if (!validateForm()) {
                showError("Please fill in all fields correctly and accept the terms.");
                return;
            }
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while validating signup-form.");
        }

        Login login = new Login();
        if (!login.signUp(usernameField.getText().strip(), emailField.getText().strip(), fullnameField.getText().strip(), passwordField.getText())) {
            errorLabel.setText("Login Failed.");
            return;
        }
        System.out.println("Signed up "+usernameField.getText());
        try {
            ContentSwitcher.switchContent(event, "/view/dashboard-view.fxml");
        } catch (IOException e) {
            ErrorHandler.handle(e, "An error occurred while switching pages signup-page -> dashboard-page.");
        }
    }

    @FXML
    public void handleSignin(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/signin-view.fxml");
    }
}