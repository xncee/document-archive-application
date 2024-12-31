package utils;

import javafx.scene.control.Alert;

public class ErrorHandler {
    public static void handle(Exception e, String message) {
        System.out.println(message+": "+e.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
        throw new RuntimeException(e);
    }
}
