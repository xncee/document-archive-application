package controllers;

import data.DBFacade;
import data.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import models.Document;
import utils.ContentSwitcher;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardController {
    private DBFacade dbFacade = DBFacade.getInstance();

    @FXML
    private Label totalDocuments;
    @FXML
    private Label recentUploads;
    @FXML
    private Label pendingReview;
    @FXML
    private Button addDocumentButton;
    @FXML
    private Button generateReportButton;
    @FXML
    private Button filterButton;
    @FXML
    private TableView<Document> documentTable;
    @FXML
    private TableColumn<Document, String> documentIdColumn;
    @FXML
    private TableColumn<Document, String> titleColumn;
    @FXML
    private TableColumn<Document, String> departmentColumn;
    @FXML
    private TableColumn<Document, String> classificationColumn;
    @FXML
    private TableColumn<Document, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Document, String> statusColumn;

    @FXML
    private TableColumn<Document, LocalDate> createdDateColumn;

    private ObservableList<Document> documents;

    @FXML
    private void initialize() {
        setupStatsCards();
        setupTableColumns();
    }

    @FXML
    private void handleFilter(ActionEvent event) throws IOException {
        ContentSwitcher.popUpWindow(event, "/view/results-filter-view.fxml");
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) throws IOException {
        ContentSwitcher.popUpWindow(event, "/view/generate-report-view.fxml");
    }

    @FXML
    private void handleAddDocument(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/document-upload-view.fxml");
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        // Implementation for search functionality
    }

    private void setupStatsCards() {
        int total = dbFacade.getCount("documents");
        int recent = dbFacade.getDataByDateRange("documents", "createdDate", LocalDate.now().minusDays(1), LocalDate.now()).size();
        int pending = dbFacade.search(Tables.DOCUMENTS.getTableName(), "pending", true, "status").size();
        totalDocuments.setText(String.valueOf(total));
        recentUploads.setText(String.valueOf(recent));
        pendingReview.setText(String.valueOf(pending));
    }
    private void setupTableColumns() {
        // Link columns to Document properties using PropertyValueFactory
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        // Apply auto resize for columns
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadDocuments();
    }

    private LocalDate convertToLocalDate(Object date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return null; // Return null if date is null or not an instance of java.sql.Date
    }
    private void loadDocuments() {
        documents = FXCollections.observableArrayList();
        List<Map<String, Object>> docs = dbFacade.getLimitedRows(Tables.DOCUMENTS.getTableName(), 100);
        // when reaching end of the list: SELECT * FROM table_name WHERE id > ? ORDER BY id ASC;
        for (Map<String, Object> x: docs) {
            Document.Builder docBuilder = new Document.Builder(
                    (String) x.get("status"),
                    (Integer) x.get("uploaderId"),
                    (String) x.get("title"),
                    (String) x.get("description"),
                    (String) x.get("department"),
                    (String) x.get("classification"))
                    .id((String) x.get("id"))
                    .deadline(convertToLocalDate(x.get("deadline")))
                    .createdDate(convertToLocalDate(x.get("createdDate")))
                    .updatedDate(convertToLocalDate(x.get("updatedDate")));
            //IllegalArgumentException
//            try {
//                docBuilder.filePath((String) x.get("filePath"));
//            }
//            catch (IllegalArgumentException e) {
//                // pass
//            }
            Document doc = docBuilder.build();
            documents.add(doc);
        }
        // Set the items to the TableView
        documentTable.setItems(documents);
    }

    @FXML
    private void handleHelpLink(ActionEvent event) {
        // Implementation for help link
    }

    @FXML
    private void handlePrivacyLink(ActionEvent event) {
        // Implementation for privacy link
    }

    @FXML
    private void handleTermsLink(ActionEvent event) {
        // Implementation for terms link
    }
}
