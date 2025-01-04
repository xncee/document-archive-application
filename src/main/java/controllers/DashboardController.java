package controllers;

import data.DBFacade;
import data.DocumentFacade;
import data.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import models.Document;
import application.ContentSwitcher;
import utils.FXMLCache;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController {
    private final DBFacade dbFacade = DBFacade.getInstance();
    private final DocumentFacade documentFacade = DocumentFacade.getInstance();
    @FXML
    private Label totalDocuments;
    @FXML
    private Label recentUploads;
    @FXML
    private Label pendingReview;
    @FXML
    private TextField searchField;
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


    private final int ROWS_PER_PAGE = 10;
    private ObservableList<Document> documents = FXCollections.emptyObservableList();
    private SearchService searchService = SearchService.getInstance();
    private int offset;
    private boolean more = true;

    @FXML
    private void initialize() {
        try {
            FXMLCache.preloadFXML("/view/user-profile-view.fxml");
        } catch (IOException e) {
            System.out.println("Failed to load user profile.");
            e.printStackTrace();
        }

        setupStatsCards();
        setupTableColumns();
        documentTable.setItems(loadDocuments("" ,offset, ROWS_PER_PAGE)); // All documents
    }

    @FXML
    public void handleDropDown(ActionEvent event) {
        // do something
    }

    @FXML
    private void handleAddDocument(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent("/view/document-upload-view.fxml");
    }

    private void setupStatsCards() {
        int total = dbFacade.getCount("documents");
        int recent = dbFacade.getDataByDateRange("documents", "createdDate", LocalDate.now().minusDays(1), LocalDate.now()).size();
        int pending = dbFacade.search(Tables.DOCUMENTS.getTableName(), "pending", true, 0, "status").size();
        totalDocuments.setText(String.valueOf(total));
        recentUploads.setText(String.valueOf(recent));
        pendingReview.setText(String.valueOf(pending));
    }

    // Table view (view, search, filter, generate report)
    @FXML
    private void handleGenerateReport(ActionEvent event) throws IOException {
        ContentSwitcher.popUpWindow(event, "/view/generate-report-view.fxml");
    }

    @FXML
    private void handleFilter(ActionEvent event) throws IOException {
        ContentSwitcher.popUpWindow(event, "/view/results-filter-view.fxml");
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        offset = 0;
        documents.clear();
        // Ensure TableView items are not null (as this can cause exceptions)
        if (documentTable.getItems() != null) {
            documentTable.setItems(FXCollections.observableArrayList());
        }

        String query = searchField.getText().strip();

        // Create a Task for the search operation (background task, this will prevent the UI from freezing during search operations)
        Task<ObservableList<Document>> searchTask = new Task<>() {
            @Override
            protected ObservableList<Document> call() {
                return loadDocuments(query, offset, ROWS_PER_PAGE);
            }
        };

        // Update the table once the task completes
        searchTask.setOnSucceeded(e -> {
            ObservableList<Document> results = searchTask.getValue();
            if (results != null) {
                documentTable.setItems(results);  // Set results to the table
            } else {
                documentTable.setItems(FXCollections.observableArrayList());  // Set empty list if no results
            }
        });

        // Start the task in the background
        new Thread(searchTask).start();
    }

    @FXML
    private void handleScroll() {
        documentTable.getItems().addAll();
    }

    private void setupTableColumns() {
        // Link columns to Document properties using PropertyValueFactory
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Apply auto resize for columns
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private ObservableList<Document> loadDocuments(String query, int offset, int count) {
        List<Document> results = null;
        try {
            results = documentFacade.searchDocuments(query, offset, count);

        } catch (SQLException e) {
            System.out.println("An error occurred while searching for " + query);
            e.printStackTrace();
        }

        offset += results.size();
        // if results number is equal to the requested amount, then it is possible that there are more unloaded results
        more = results.size() == count;

        return FXCollections.observableArrayList(results);
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
