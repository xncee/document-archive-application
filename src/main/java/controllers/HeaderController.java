package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.login.Login;

import java.awt.event.ActionEvent;

public class HeaderController {
    public VBox header;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button userInfoButton;
    @FXML
    private Label loggedInUserLabel;

    @FXML
    public void initialize() {
        prepareLanguages();
        String username = Login.getInstance().getUsername();
        //System.out.println(username);
        loggedInUserLabel.setText("@"+username);
    }

    private void prepareLanguages() {
        languageComboBox.getItems().addAll("ِArabic", "English");
        languageComboBox.setValue("English"); // replace with preferred language
    }

    @FXML
    public void handleLanguage() {
        System.out.println("Language switched to "+languageComboBox.getValue());
    }

    @FXML
    public void handleNotifications() {

    }

    @FXML
    public void handleUserInfo() {

    }
}
