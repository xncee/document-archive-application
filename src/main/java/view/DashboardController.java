package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    public void switchPage(ActionEvent e) throws IOException {
        System.out.println("Switching to home page...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("transactions.fxml"));
        Parent root = loader.load();

        // pop up stage
        Stage popupStage = new Stage();
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Make the popup window modal (optional)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }
}
