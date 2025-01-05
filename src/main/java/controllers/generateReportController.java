package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import models.Document;
import reports.ExcelReportGenerator;
import reports.PDFReportGenerator;
import reports.ReportGenerator;
import application.ContentSwitcher;
import services.FilterService;
import services.SearchService;
import utils.LocalizationUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class generateReportController {
    private final SearchService searchService = SearchService.getInstance();
    private final FilterService filterService = FilterService.getInstance();
    @FXML
    private Button closeButton;
    @FXML
    private Label dateRangeLabel;
    @FXML
    private Label departmentsLabel;
    @FXML
    private Label classificationsLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ToggleButton pdfButton;
    @FXML
    private ToggleButton excelButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button generateButton;

    private String formatDate(String originalDate) {
        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // Parse the original date string into LocalDate
        LocalDate date = LocalDate.parse(originalDate, originalFormatter);

        // Convert the LocalDate to the new format
        String formattedDate = date.format(targetFormatter);
        return formattedDate;
    }
    @FXML
    private void initialize() {
        if (filterService.getStartDate() != null && filterService.getEndDate()!=null) {
            String start = formatDate(String.valueOf(filterService.getStartDate()));
            String end = formatDate(String.valueOf(filterService.getEndDate()));
            dateRangeLabel.setText(start + " - " + end);
        }
        if (filterService.getDepartment() != null) {
            departmentsLabel.setText(filterService.getDepartment());
        }
        if (filterService.getClassification() != null) {
            classificationsLabel.setText(filterService.getClassification());
        }
        if (filterService.getStatus() != null) {
            statusLabel.setText(filterService.getStatus());
        }
    }

    @FXML
    public void handleClose(ActionEvent event) {
        ContentSwitcher.getStage(event).getScene().getWindow().hide();
    }
    @FXML
    public void handleCancel(ActionEvent event) {
        handleClose(event);
    }

    @FXML
    public void handleGenerate(ActionEvent event) {
        // retrieve filtered documents
        ObservableList<Document> documents = searchService.getDocuments();
        // map the document into column:value paris
        List<LinkedHashMap<String, Object>> documentsMap = new ArrayList<>();
        for (Document document: documents) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put(LocalizationUtil.getString("label.department"), document.getDepartment());
            map.put(LocalizationUtil.getString("label.documentId"), document.getId());
            map.put(LocalizationUtil.getString("label.description"), document.getDescription());
            map.put(LocalizationUtil.getString("label.createdDate"), document.getCreatedDate());
            map.put(LocalizationUtil.getString("label.deadlineDate"), document.getDeadline());
            map.put(LocalizationUtil.getString("label.status"), document.getStatus());
            documentsMap.add(map);
        }
        ReportGenerator reportGenerator;
        if (pdfButton.isSelected()) {
            reportGenerator = new PDFReportGenerator();
        }
        else {
            reportGenerator = new ExcelReportGenerator();
        }

        reportGenerator.generate(documentsMap);
        handleClose(event);
    }
}
