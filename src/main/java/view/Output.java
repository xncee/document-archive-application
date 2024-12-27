package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Output {
    @FXML
    private TextArea outputTextArea;

    public void setOutputText(String text) {
        outputTextArea.setText(text);
    }
}