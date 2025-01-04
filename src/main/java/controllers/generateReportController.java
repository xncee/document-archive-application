package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import reports.ExcelReportGenerator;
import reports.PDFReportGenerator;
import reports.ReportGenerator;
import application.ContentSwitcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class generateReportController {
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
        // set text for filter labels
        // load filtered documents

        String start = formatDate("2024-05-10");
        String end = formatDate("2024-12-31");
        dateRangeLabel.setText(start+" - "+end);
        String[] departments = {"Dept1", "Dept2", "Dept3"};
        departmentsLabel.setText(String.join(", ", departments));
        String[] classifications = {"private", "public"};
        classificationsLabel.setText(String.join(", ", classifications));
        String status = "Pending";
        statusLabel.setText(status);
    }

    @FXML
    public void handleClose(ActionEvent event) {
        ContentSwitcher.closeWindow(event);
    }
    @FXML
    public void handleCancel(ActionEvent event) {
        handleClose(event);
    }

    // temp
    private static LinkedHashMap<String, Object> createDocument(String documentId, String title, String classification, LocalDate createdDate) {
        LinkedHashMap<String, Object> document = new LinkedHashMap<>();
        document.put("documentId", documentId);
        document.put("title", title);
        document.put("Classification", classification);
        document.put("created date", createdDate);
        return document;
    }
    private List<LinkedHashMap<String, Object>> generateSampleData() {
        List<LinkedHashMap<String, Object>> documents = new ArrayList<>();
        documents.add(createDocument("DOC-1234", "Software Engineering", "public", LocalDate.now()));
        documents.add(createDocument("DOC-4545", "Computer Science", "public", LocalDate.now().minusMonths(7)));
        documents.add(createDocument("DOC-4533", "Artificial Intelligence", "private", LocalDate.now().minusMonths(3)));
        documents.add(createDocument("DOC-5678", "Data Structures", "private", LocalDate.now().minusYears(1)));
        documents.add(createDocument("DOC-7890", "Machine Learning", "public", LocalDate.now().minusMonths(5)));
        documents.add(createDocument("DOC-2468", "Database Management", "restricted", LocalDate.now().minusWeeks(2)));
        documents.add(createDocument("DOC-1357", "Cloud Computing", "public", LocalDate.now().minusDays(10)));
        documents.add(createDocument("DOC-9999", "Cyber Security", "private", LocalDate.now().minusYears(2)));
        documents.add(createDocument("DOC-3333", "Web Development", "public", LocalDate.now().minusMonths(6)));
        documents.add(createDocument("DOC-7777", "Big Data Analytics", "restricted", LocalDate.now().minusYears(1).plusMonths(3)));

        return documents;
    }
    @FXML
    public void handleGenerate(ActionEvent event) {
        // report generation logic here
        // retrieve filtered documents
        List<LinkedHashMap<String, Object>> documentsMap = generateSampleData();


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
