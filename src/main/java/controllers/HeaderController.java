package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;
import models.login.Login;
import application.ContentSwitcher;

import java.io.IOException;

public class HeaderController {
    public VBox header;
    @FXML
    private Button notificationsButton;
    @FXML
    private MenuButton userDropDownButton;
    @FXML
    private Label loggedInUserLabel;
    @FXML
    private Button profileButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        String username = Login.getInstance().getUsername();
        //System.out.println(username);
        loggedInUserLabel.setText("@"+username);
    }

    @FXML
    public void handleNotifications() {

    }

    @FXML
    public void handleProfile() {

    }

    @FXML
    public void handleLogout() {
        Login.getInstance().logout();
        try {
            ContentSwitcher.switchContent("/view/signin-view.fxml");
        } catch (IOException e) {
            System.out.println("Logout Failed: "+e.getMessage());
        }
    }
}
