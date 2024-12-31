package control;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Document;

import java.io.IOException;
import java.time.LocalDate;

public class DashboardController {

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
    private TableColumn<Document, String> statusColumn;

    @FXML
    private TableColumn<Document, LocalDate> createdDateColumn;

    @FXML
    private TableColumn<?, ?> actionsColumn;

    private ObservableList<Document> documents;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        documentTable.getStylesheets().add(getClass().getResource("/styles/table-style.css").toExternalForm());
        setupTableColumns();
        loadDocuments();

        // Apply custom cell factory to the status column to add CSS classes for status badges
        statusColumn.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Document, String>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);
                    if (empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        if (status != null) {
                            switch (status) {
                                case "Pending":
                                    getStyleClass().add("status-badge");
                                    getStyleClass().add("pending");
                                    break;
                                case "Approved":
                                    getStyleClass().add("status-badge");
                                    getStyleClass().add("approved");
                                    break;
                                case "Rejected":
                                    getStyleClass().add("status-badge");
                                    getStyleClass().add("rejected");
                                    break;
                                default:
                                    getStyleClass().add("status-badge");
                                    break;
                            }
                        }
                    }
                }
            };
        });
    }

    @FXML
    private void handleFilter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/results-filter-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage modularStage = new Stage();
        modularStage.initStyle(StageStyle.TRANSPARENT);
        modularStage.initModality(Modality.WINDOW_MODAL);
        modularStage.initOwner(stage);
        Scene scene = new Scene(root);
        modularStage.setScene(scene);
        modularStage.showAndWait();
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) {

    }

    @FXML
    private void handleAddDocument(ActionEvent event) throws IOException {
        ContentSwitcher.switchContent(event, "/view/document-upload-view.fxml");
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        // Implementation for search functionality
    }

    private void setupTableColumns() {
        // Link columns to Document properties using PropertyValueFactory
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        // Set preferred column widths for better alignment
//        documentIdColumn.setPrefWidth(100);
//        titleColumn.setPrefWidth(200);
//        departmentColumn.setPrefWidth(150);
//        statusColumn.setPrefWidth(100);
//        createdDateColumn.setPrefWidth(150);
//        actionsColumn.setPrefWidth(50);

        // Set custom cell alignment for each column
        documentIdColumn.setCellFactory(column -> {
            TableCell<Document, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setStyle("-fx-alignment: CENTER_LEFT;");
            return cell;
        });

        statusColumn.setCellFactory(column -> {
            TableCell<Document, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setStyle("-fx-alignment: CENTER;");
            return cell;
        });

        // Apply auto resize for columns
        documentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadDocuments() {
        // Create sample data for the TableView
//        documents = FXCollections.observableArrayList(
//                new Document("Project Plan", "Description of project plan", "Engineering",
//                        "Confidential", "/path/to/document1.pdf", null,
//                        "Pending", LocalDate.now(), null),
//                new Document("Budget Report", "Description of budget report", "Finance",
//                        "Public", "/path/to/document2.pdf", null,
//                        "Approved", LocalDate.of(2023, 12, 1), null),
//                new Document("Marketing Strategy", "Description of marketing strategy", "Marketing",
//                        "Internal", "/path/to/document3.pdf", null,
//                        "Rejected", LocalDate.of(2023, 11, 15), null)
//        );

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
