package controllers;

import data.DBFacade;
import services.FilterService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.Map;

public class FilterResultsController {
    private DBFacade dbFacade =  DBFacade.getInstance();
    private FilterService filterService = FilterService.getInstance();
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private ComboBox<String> classificationComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    public void initialize() {
        initializeControls();
        bindEventHandlers();
    }

    private void initializeControls() {
        for (Map<String, Object> department: dbFacade.getDepartments()) {
            departmentComboBox.getItems().add((String) department.get("name"));
        }
        for (Map<String, Object> classification: dbFacade.getClassifications()) {
            classificationComboBox.getItems().add((String) classification.get("name"));
        }

        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

    }

    private void bindEventHandlers() {
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) ->
                validateDateRange());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) ->
                validateDateRange());
    }

    @FXML
    void handleClose(MouseEvent event) {
        closeFilterPanel();
    }

    @FXML
    void handleCloseKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
            closeFilterPanel();
        }
    }

    @FXML
    void handleStatusSelect(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        filterService.setStatus(sourceButton.getText());
        updateStatusButtonStyles(sourceButton);
    }

    @FXML
    void handleDepartmentSelect(ActionEvent event) {
        String selectedDepartment = departmentComboBox.getValue();
        filterService.setDepartment(selectedDepartment);
        departmentComboBox.setValue(selectedDepartment);
    }

    @FXML
    void handleClassificationSelect(ActionEvent event) {
        String selectedClassification = classificationComboBox.getValue();
        filterService.setClassification(selectedClassification);
        classificationComboBox.setPromptText(selectedClassification);
    }

    @FXML
    void handleStartDateSelect(ActionEvent event) {
        LocalDate selectedDate = startDatePicker.getValue();
        filterService.setStartDate(selectedDate);
        validateDateRange();
    }

    @FXML
    void handleEndDateSelect(ActionEvent event) {
        LocalDate selectedDate = endDatePicker.getValue();
        filterService.setEndDate(selectedDate);
        validateDateRange();
    }

    @FXML
    void handleReset(ActionEvent event) {
        departmentComboBox.setValue(null);
        classificationComboBox.setValue(null);
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
        filterService.reset();
        resetStatusButtonStyles();
    }

    @FXML
    void handleApply(ActionEvent event) {
        if (validateFilters()) {
            //
            closeFilterPanel();
        }
    }

    private void validateDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            startDatePicker.setStyle("-fx-border-color: red;");
            endDatePicker.setStyle("-fx-border-color: red;");
        } else {
            startDatePicker.setStyle("");
            endDatePicker.setStyle("");
        }
    }

    private boolean validateFilters() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showError("Please select both start and end dates");
            return false;
        }

        if (startDate.isAfter(endDate)) {
            showError("Start date must be before end date");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setAccessibleText(message);
        alert.showAndWait();
    }

    private void updateStatusButtonStyles(Button selectedButton) {
        selectedButton.getParent().getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.getStyleClass().remove("selected");
            }
        });
        selectedButton.getStyleClass().add("selected");
    }

    private void resetStatusButtonStyles() {
        startDatePicker.getParent().getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.getStyleClass().remove("selected");
            }
        });
    }

    private void closeFilterPanel() {
        startDatePicker.getScene().getWindow().hide();
    }
}