package control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Login;

public class HeaderController {
    public VBox header;

    @FXML
    private Button notificationsButton;

    @FXML
    private Button userInfoButton;

    @FXML
    private Label loggedInUserLabel;

    @FXML
    public void initialize() {
        String username = Login.getInstance().getUsername();
        //System.out.println(username);
        loggedInUserLabel.setText("@"+username);
    }

    @FXML
    private void handleNotifications() {

    }

    @FXML
    private void handleUserInfo() {

    }
}
