package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {
    @FXML
    Button signInButton;
    @FXML
    Hyperlink signUpLink;

    @FXML
    public void handleSignIn(ActionEvent event) throws IOException {
        System.out.println("Signing in...");
    }
    @FXML
    public void handleSignup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signup-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleForgotPassword(ActionEvent event) throws IOException {

    }
}
// {{}, 9, {}}
//package your.package.name;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//        import javafx.scene.layout.Pane;
//
//public class SignInController {
//
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private PasswordField passwordField;
//
//    @FXML
//    private Button signInButton;
//
//    @FXML
//    private Hyperlink signUpLink;
//
//    @FXML
//    private Label errorLabel;
//
//    @FXML
//    private Pane mainPane;
//
//    // Handle Sign In action
//    @FXML
//    public void handleSignIn(ActionEvent event) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        if (validateInput(username, password)) {
//            boolean success = authenticateUser(username, password);
//            if (success) {
//                errorLabel.setText("Sign In successful!");
//                // Navigate to the main application page or dashboard
//                navigateToMainPage();
//            } else {
//                errorLabel.setText("Invalid username or password.");
//            }
//        } else {
//            errorLabel.setText("Please fill all fields.");
//        }
//    }
//
//    // Navigate to the Sign Up page
//    @FXML
//    public void handleSignUpLink(ActionEvent event) {
//        navigateToSignUpPage();
//    }
//
//    // Validate input fields
//    private boolean validateInput(String username, String password) {
//        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
//    }
//
//    // Mock authentication (replace with backend call)
//    private boolean authenticateUser(String username, String password) {
//        return "user".equals(username) && "password".equals(password);
//    }
//
//    // Mock navigation to main application page
//    private void navigateToMainPage() {
//        System.out.println("Navigating to main application page...");
//    }
//
//    // Mock navigation to Sign Up page
//    private void navigateToSignUpPage() {
//        System.out.println("Navigating to Sign Up page...");
//    }
//}
//
