package control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField fullnameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;

    @FXML
    protected void handleSignup(ActionEvent event) {
        if (validateForm()) {
            // Implement signup logic
        }
    }

    @FXML
    protected void handleSignin(ActionEvent event) {
        // Implement signin navigation
    }

    private boolean validateForm() {
        return !usernameField.getText().isEmpty() &&
                !emailField.getText().isEmpty() &&
                !fullnameField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                passwordField.getText().equals(confirmPasswordField.getText()) &&
                termsCheckbox.isSelected();
    }
}