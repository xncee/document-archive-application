package control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDate;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;

public class DocumentUploadController {
    @FXML private TextField documentIdField;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> departmentCombo;
    @FXML private ComboBox<String> classificationCombo;
    @FXML private DatePicker deadlinePicker;
    @FXML private VBox uploadZone;

    private File selectedFile;
    private static final double MAX_FILE_SIZE_MB = 10.0;

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupValidation();
        setupAccessibility();
        setupDragAndDrop();
    }

    private void setupComboBoxes() {
        departmentCombo.getItems().addAll(
                "Human Resources",
                "Finance",
                "Information Technology",
                "Marketing",
                "Operations"
        );

        classificationCombo.getItems().addAll(
                "Public",
                "Internal",
                "Confidential",
                "Restricted"
        );

        departmentCombo.setVisibleRowCount(5);
        classificationCombo.setVisibleRowCount(4);
    }

    private void setupValidation() {
        titleField.textProperty().addListener((obs, old, newValue) -> {
            if (newValue != null && newValue.length() > 100) {
                titleField.setText(old);
            }
        });

        descriptionArea.textProperty().addListener((obs, old, newValue) -> {
            if (newValue != null && newValue.length() > 500) {
                descriptionArea.setText(old);
            }
        });

        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void setupAccessibility() {
        documentIdField.setFocusTraversable(false);

        titleField.setAccessibleRoleDescription("Required field");
        descriptionArea.setAccessibleRoleDescription("Required field");
        departmentCombo.setAccessibleRoleDescription("Required field");
        classificationCombo.setAccessibleRoleDescription("Required field");
        deadlinePicker.setAccessibleRoleDescription("Optional field");
        uploadZone.setAccessibleText("Click or press Enter to select a PDF file for upload");
        uploadZone.setFocusTraversable(true);
    }

    private void setupDragAndDrop() {
        uploadZone.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        uploadZone.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasFiles()) {
                File file = event.getDragboard().getFiles().get(0);
                if (validateFile(file)) {
                    selectedFile = file;
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    private void handleUploadKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
            openFileChooser();
        }
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF Document");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.model.pdf")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null && validateFile(file)) {
            selectedFile = file;
        }
    }

    private boolean validateFile(File file) {
        if (!file.getName().toLowerCase().endsWith(".model.pdf")) {
            showErrorAlert("Invalid File Type", "Please select a PDF file.");
            return false;
        }

        double fileSizeMB = file.length() / (1024.0 * 1024.0);
        if (fileSizeMB > MAX_FILE_SIZE_MB) {
            showErrorAlert("File Too Large",
                    String.format("Maximum file size is %.0fMB. Selected file is %.1fMB.",
                            MAX_FILE_SIZE_MB, fileSizeMB));
            return false;
        }

        return true;
    }

    @FXML
    private void handleSave() {
        if (!validateForm()) {
            return;
        }

        if (selectedFile == null) {
            showErrorAlert("Missing File", "Please select a PDF file to upload.");
            return;
        }

        try {
//            DocumentUploadService.getInstance().uploadDocument(
//                    titleField.getText(),
//                    descriptionArea.getText(),
//                    departmentCombo.getValue(),
//                    classificationCombo.getValue(),
//                    deadlinePicker.getValue(),
//                    selectedFile
//            );

            showSuccessAlert();
            clearForm();
        } catch (Exception e) {
            showErrorAlert("Upload Error",
                    "An error occurred while uploading the document. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errors.append("Title is required\n");
        }

        if (descriptionArea.getText() == null || descriptionArea.getText().trim().isEmpty()) {
            errors.append("Description is required\n");
        }

        if (departmentCombo.getValue() == null) {
            errors.append("Department is required\n");
        }

        if (classificationCombo.getValue() == null) {
            errors.append("Classification is required\n");
        }

        if (errors.length() > 0) {
            showErrorAlert("Validation Error", errors.toString());
            return false;
        }

        return true;
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Document saved successfully!");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        departmentCombo.setValue(null);
        classificationCombo.setValue(null);
        deadlinePicker.setValue(null);
        selectedFile = null;
    }
}