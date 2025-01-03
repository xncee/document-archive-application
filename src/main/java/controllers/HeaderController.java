package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.login.Login;
import utils.ContentSwitcher;
import utils.LocalizationUtil;
import utils.UserPreffrences;

import java.io.IOException;
import java.util.Locale;

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
        languageComboBox.getItems().addAll("Arabic", "English");
        languageComboBox.setValue(UserPreffrences.getLanguage()); // replace with preferred language
    }

    @FXML
    public void handleLanguage() throws IOException {
        String selectedLanguage = languageComboBox.getValue().toLowerCase();
        if ("english".equals(selectedLanguage)) {
            LocalizationUtil.setLocale(new Locale("en"));
            ContentSwitcher.switchDirection("left-to-right", "right");
            UserPreffrences.setLanguage("en");
        }
        else if ("arabic".equals(selectedLanguage)) {
            LocalizationUtil.setLocale(new Locale("ar"));
            ContentSwitcher.switchDirection("right-to-left", "left");
            UserPreffrences.setLanguage("ar");
        }
        else {
            LocalizationUtil.setLocale(new Locale("en"));
        }

        System.out.println("Language switched to "+languageComboBox.getValue());
    }

    @FXML
    public void handleNotifications() {

    }

    @FXML
    public void handleUserInfo() {

    }
}
