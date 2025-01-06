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
    private static final DBFacade dbFacade = DBFacade.getInstance();
    private static final FilterService filterService = FilterService.getInstance();
    private DashboardController dashboardController; // DI (Dependency Injection)
    private String status;

    @FXML private Button completedButton;
    @FXML private Button pendingButton;
    @FXML private Button lateButton;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private ComboBox<String> classificationComboBox;
    @FXML private CheckBox currentDateCheckbox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void initialize() {
        initializeFields();
        initializeControls();
        bindEventHandlers();
    }

    private void initializeFields() {
        this.status = filterService.getStatus();
        switch (status) {
            case "completed" -> updateStatusButtonStyles(completedButton);
            case "pending" -> updateStatusButtonStyles(pendingButton);
            case "late" -> updateStatusButtonStyles(lateButton);
            case null, default -> this.status = null;
        }

        departmentComboBox.setValue(filterService.getDepartment());
        classificationComboBox.setValue(filterService.getClassification());
        startDatePicker.setValue(filterService.getStartDate());
        endDatePicker.setValue(filterService.getEndDate());
    }

    private void initializeControls() {
        for (Map<String, Object> department : dbFacade.getDepartments()) {
            departmentComboBox.getItems().add((String) department.get("name"));
        }
        for (Map<String, Object> classification : dbFacade.getClassifications()) {
            classificationComboBox.getItems().add((String) classification.get("name"));
        }
    }

    private void bindEventHandlers() {
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateDateRange());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateDateRange());
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

        // Check if the button is already selected
        if (sourceButton.getStyleClass().contains("selected")) {
            resetStatusButtonStyles();  // Reset styles if it's already selected
            status = null;  // Deselect status
        } else {
            updateStatusButtonStyles(sourceButton);  // Update styles to mark as selected
            status = sourceButton.getAccessibleText() != null ? sourceButton.getAccessibleText().toLowerCase() : null;
        }
    }

    @FXML
    void handleDepartmentSelect(ActionEvent event) {
        departmentComboBox.setValue(departmentComboBox.getValue());
    }

    @FXML
    void handleClassificationSelect(ActionEvent event) {
        // Don't update FilterState immediately, wait for Apply button
    }

    @FXML
    void handleStartDateSelect(ActionEvent event) {
        // Don't update FilterState immediately, wait for Apply button
    }

    @FXML
    void handleEndDateSelect(ActionEvent event) {
        // Don't update FilterState immediately, wait for Apply button
    }

    @FXML
    void handleCurrentDate(ActionEvent event) {
        if (currentDateCheckbox.isSelected()) {
            startDatePicker.setValue(LocalDate.now());
            startDatePicker.setDisable(true);
        }
        else {
            startDatePicker.setDisable(false);
        }
    }

    @FXML
    void handleReset(ActionEvent event) {
        departmentComboBox.setValue(null);
        classificationComboBox.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        filterService.reset();
        resetStatusButtonStyles();
    }

    private void updateFilterState() {
        String selectedDepartment = departmentComboBox.getValue();
        String selectedClassification = classificationComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        filterService.setStatus(status);
        filterService.setDepartment(selectedDepartment);
        filterService.setClassification(selectedClassification);
        filterService.setStartDate(startDate);
        filterService.setEndDate(endDate);
    }

    @FXML
    void handleApply(ActionEvent event) {
        //if (validateFilters()) {}
        // Update the filter state only when Apply is pressed.
        updateFilterState();

        if (dashboardController != null) {
            dashboardController.handleSearch(null); // Pass filters to dashboard controller
        } else {
            System.err.println("DashboardController is not set in FilterResultsController.");
        }
        closeFilterPanel();
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
        completedButton.getParent().getChildrenUnmodifiable().forEach(node -> {
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