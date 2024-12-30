package control.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField fullnameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox termsCheckbox;

    private boolean validateForm() {
        return !usernameField.getText().isEmpty() &&
                !emailField.getText().isEmpty() &&
                !fullnameField.getText().isEmpty() &&
                !passwordField.getText().isEmpty() &&
                passwordField.getText().equals(confirmPasswordField.getText()) &&
                termsCheckbox.isSelected();
    }

    @FXML
    protected void handleSignup(ActionEvent event) {
        if (validateForm()) {
            // Implement signup logic

        }
        else {
            //
        }
    }

    @FXML
    public void handleSignin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signin-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}