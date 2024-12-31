package controllers;

import models.FilterState;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import services.FilterService;

import java.time.LocalDate;

public class FilterResultsController {
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private ComboBox<String> classificationComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private FilterService filterService;
    private FilterState filterState;

    public void initialize() {
        setupAccessibility();
        initializeControls();
        bindEventHandlers();
    }

    private void setupAccessibility() {
        departmentComboBox.setFocusTraversable(true);
        departmentComboBox.getStyleClass().add("accessible-combo-box");
        departmentComboBox.setAccessibleRole(AccessibleRole.COMBO_BOX);
        departmentComboBox.setAccessibleText("Select department filter");

        classificationComboBox.setFocusTraversable(true);
        classificationComboBox.getStyleClass().add("accessible-combo-box");
        classificationComboBox.setAccessibleRole(AccessibleRole.COMBO_BOX);
        classificationComboBox.setAccessibleText("Select Classification filter");

        startDatePicker.setFocusTraversable(true);
        startDatePicker.setAccessibleRole(AccessibleRole.DATE_PICKER);
        startDatePicker.setAccessibleText("Select start date");

        endDatePicker.setFocusTraversable(true);
        endDatePicker.setAccessibleRole(AccessibleRole.DATE_PICKER);
        endDatePicker.setAccessibleText("Select end date");
    }

    private void initializeControls() {
        setFilterService(new FilterService());
        setFilterState(new FilterState());
        departmentComboBox.getItems().addAll(filterService.getDepartments());
        classificationComboBox.getItems().addAll(filterService.getClassifications());

        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(endDatePicker.getValue()));
            }
        });

        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(startDatePicker.getValue()));
            }
        });
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
        filterState.setStatus(sourceButton.getText());
        updateStatusButtonStyles(sourceButton);
    }

    @FXML
    void handleDepartmentSelect(ActionEvent event) {
        String selectedDepartment = departmentComboBox.getValue();
        filterState.setDepartment(selectedDepartment);
    }

    @FXML
    void handleClassificationSelect(ActionEvent event) {
        String selectedClassification = classificationComboBox.getValue();
        filterState.setClassification(selectedClassification);
    }

    @FXML
    void handleStartDateSelect(ActionEvent event) {
        LocalDate selectedDate = startDatePicker.getValue();
        filterState.setStartDate(selectedDate);
        validateDateRange();
    }

    @FXML
    void handleEndDateSelect(ActionEvent event) {
        LocalDate selectedDate = endDatePicker.getValue();
        filterState.setEndDate(selectedDate);
        validateDateRange();
    }

    @FXML
    void handleReset(ActionEvent event) {
        departmentComboBox.setValue(null);
        classificationComboBox.setValue(null);
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
        filterState.reset();
        resetStatusButtonStyles();
    }

    @FXML
    void handleApply(ActionEvent event) {
        if (validateFilters()) {
            filterService.applyFilters(filterState);
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

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public void setFilterState(FilterState filterState) {
        this.filterState = filterState;
    }
}