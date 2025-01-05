package controllers;

import data.DBFacade;
import data.DocumentFacade;
import data.Tables;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.scene.input.ScrollEvent;
import models.Document;
import application.ContentSwitcher;
import services.ExpandViewService;
import services.SearchService;
import utils.ErrorHandler;
import utils.FXMLCache;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class DashboardController {
    private final DBFacade dbFacade = DBFacade.getInstance();
    private final DocumentFacade documentFacade = DocumentFacade.getInstance();
    private final SearchService searchService = SearchService.getInstance();
    private final ExpandViewService expandViewService = ExpandViewService.getInstance();

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
    private ScrollPane scrollPane;
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
    @FXML
    private Label currentResultsLabel;
    @FXML
    private Label totalResultsLabel;
    @FXML
    private Label resultsMessageLabel;

    private static final Set<KeyCode> IGNORED_KEYS = Set.of(
            //KeyCode.BACK_SPACE,
            KeyCode.ESCAPE,
            KeyCode.TAB,
            KeyCode.DELETE,
            KeyCode.HOME,
            KeyCode.END,
            KeyCode.PAGE_UP,
            KeyCode.PAGE_DOWN
    );
    private final int ROWS_PER_PAGE = 20;
    private ObservableList<Document> documents;
    private int offset = 0;
    private boolean has_more = true;
    private boolean isLoading = false;

    @FXML
    private void initialize() {
        FXMLCache.preloadFXML("/view/user-profile-view.fxml");

        setupStatsCards();
        setupTableColumns();
        documents = loadDocuments("" ,offset, ROWS_PER_PAGE);
        documentTable.setItems(documents); // All documents
        currentResultsLabel.setText(String.valueOf(documents.size()));
        totalResultsLabel.setText(String.valueOf(dbFacade.getCount(Tables.DOCUMENTS.getTableName())));

        // DI (Dependency Injection) -> this is used to provide global access to current search results.
        searchService.setDocuments(documents);
    }

    @FXML
    private void handleAddDocument(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent("/view/document-upload-view.fxml");
    }

    private void setupStatsCards() {
        int total = dbFacade.getCount("documents");
        int recent = dbFacade.getDataByDateRange(Tables.DOCUMENTS.getTableName(), "createdDate", LocalDate.now().minusDays(1), LocalDate.now()).size();
        int pending = dbFacade.search(Tables.DOCUMENTS.getTableName(), "pending", true, 0, "status").size();
        totalDocuments.setText(String.valueOf(total));
        recentUploads.setText(String.valueOf(recent));
        pendingReview.setText(String.valueOf(pending));
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) throws IOException {
        // load all results and prepare for report generation
        String keyword = searchField.getText() != null ? searchField.getText() : "";
        ObservableList<Document> results = loadDocuments(keyword, offset, -1);
        documents.addAll(results);
        ContentSwitcher.popUpWindow(event, "/view/generate-report-view.fxml");
    }

    @FXML
    private void handleFilter(ActionEvent event) throws IOException {
        // Preload and inject dashboard controller object into filterResultsController.
        FXMLCache.preloadFXML("/view/results-filter-view.fxml");

        // Get the controller instance from the cache
        FilterResultsController filterResultsController = (FilterResultsController) FXMLCache.getController("/view/results-filter-view.fxml");

        if (filterResultsController != null) {
            // Inject the DashboardController into FilterResultsController
            filterResultsController.setDashboardController(this);
        } else {
            // Handle the case where the controller is not found in the cache (although it should be)
            System.err.println("Error: Controller for FilterResultsView not found in cache.");
        }

        // Show the popup window
        ContentSwitcher.popUpWindow(event, "/view/results-filter-view.fxml");
    }

    @FXML
    void handleSearch(KeyEvent event) {
        // Ignore all other keys
        if (event != null && (event.getCode().isArrowKey()
                || event.getCode().isFunctionKey()
                || event.isAltDown()
                || event.isControlDown()
                || event.isShiftDown()
                || event.isShortcutDown()
                || event.isMetaDown()
                || IGNORED_KEYS.contains(event.getCode()))) {
            event.consume();  // Prevent further processing of the key event
            return;
        }

        resultsMessageLabel.setText("");
        totalResultsLabel.setText("unknown"); // replace with total results for applied search filters
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
            if (results.isEmpty() && offset == 0) {
                Platform.runLater(() -> resultsMessageLabel.setText("No results found."));
            }
            else {
                Platform.runLater(() -> {
                    documents.addAll(results);
                    documentTable.setItems(documents);
                    currentResultsLabel.setText(String.valueOf(documents.size()));
                });
            }
        });

        // Start the task in the background
        new Thread(searchTask).start();
    }

    @FXML
    private void handleScroll(ScrollEvent event) {
        if (!has_more || isLoading) return;

        if (scrollPane.getVvalue() >= 0.8) {
            isLoading = true; // Set loading flag
            ObservableList<Document> results = loadDocuments(searchField.getText(), offset, ROWS_PER_PAGE);
            documentTable.getItems().addAll(results);
            isLoading = false; // Reset loading flag
            currentResultsLabel.setText(String.valueOf(documents.size()));
        }
    }

    private void setupTableColumns() {
        // Link columns to Document properties using PropertyValueFactory
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        classificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        // Make rows clickable:
        documentTable.setRowFactory(doc -> {
            TableRow<Document> row = new TableRow<>();
            row.setFocusTraversable(false);
            row.setCursor(Cursor.HAND);
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Document document = row.getItem();  // Get the item (Document) of the clicked row
                    expandViewService.setDocument(document);
                    try {
                        ContentSwitcher.switchContent("/view/document-details-view.fxml");
                    } catch (Exception e) {
                        ErrorHandler.handle(e, "Failed to open document: "+document.getId());
                    }
                }
            });
            return row;  // Return the row to the row factory
        });
        // Apply auto resize for columns
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private ObservableList<Document> loadDocuments(String query, int offset, int count) {
        List<Document> results = documentFacade.searchDocuments(query, offset, count);

        if (results == null)
            return FXCollections.emptyObservableList();

        this.offset += results.size();
        has_more = results.size() == count; // if results number is equal to the requested amount, then it is possible that there are has_more unloaded results

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
