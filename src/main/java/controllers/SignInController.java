package controllers;

import services.UserPreffrences;
import services.FieldsServices;
import application.ContentSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.login.Login;
import utils.Messages;

import java.io.IOException;

public class SignInController {
    private static final ContentSwitcher contentSwitcher = ContentSwitcher.getInstance();

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    private Button signInButton;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    public void initialize(){
        loadRememberMe();
    }

    private void loadRememberMe() {
        String[] credentials = UserPreffrences.getCredentials();
        String savedUsername = credentials[0];
        String savedPassword = credentials[1];

        if (savedUsername != null && savedPassword != null) {
            usernameField.setText(savedUsername);
            passwordField.setText(savedPassword);
            rememberMeCheckbox.setSelected(true);
        }
    }

    private boolean validateForm() {
        boolean valid  = true;
        if (usernameField.getText().isBlank()) {
            FieldsServices.setRedBorder(usernameField);
            valid = false;
        }
        else {
            FieldsServices.setFieldValid(usernameField);
        }
        if (passwordField.getText().isBlank()) {
            FieldsServices.setRedBorder(passwordField);
            valid = false;
        } else {
            FieldsServices.setFieldValid(passwordField);
        }

        return valid;
    }

    @FXML
    public void handleSignIn(ActionEvent event) throws IOException {
        //System.out.println("Signing in...");
        if (!validateForm()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in the required fields.");
            alert.showAndWait();
            return;
        }

        if (rememberMeCheckbox != null && rememberMeCheckbox.isSelected())
            UserPreffrences.saveCredentials(usernameField.getText(),passwordField.getText());
        else
            UserPreffrences.clear();

        Login login = new Login();
        boolean loggedIn = login.signIn(usernameField.getText(), passwordField.getText());
        if (!loggedIn) {
            Alert alert = new Alert(Alert.AlertType.ERROR, Messages.INVALID_LOGIN_DETAILS.getMessage());
            alert.showAndWait();
            return;
        }
        System.out.println("Logged In as "+usernameField.getText());
        System.out.println("Switching to dashboard...");
        contentSwitcher.switchContent("/view/dashboard-view.fxml");
    }
    @FXML
    public void handleSignup(ActionEvent event) throws IOException {
        contentSwitcher.switchContent("/view/signup-view.fxml");
    }

    @FXML
    public void handleForgotPassword(ActionEvent event) {

    }
}
