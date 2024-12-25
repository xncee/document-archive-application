package view;

import database.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> transactionIdColumn;

    @FXML
    private TableColumn<Transaction, String> subjectColumn;

    @FXML
    private TableColumn<Transaction, String> departmentColumn;

    @FXML
    private TableColumn<Transaction, String> statusColumn;

    @FXML
    private TableColumn<Transaction, String> createdDateColumn;

    private final DBFacade dbFacade;
    private ObservableList<Transaction> transactions;

    public TransactionsController() {
        String dbUrl = Dotenv.load().get("DATABASE_URL");
        this.dbFacade = new DBFacade(dbUrl);
    }

    @FXML
    public void initialize() {
        // Configure columns
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("created_date"));

        // Load data
        transactions = FXCollections.observableArrayList();
        loadTransactions();
        transactionsTable.setItems(transactions);
    }

    private void loadTransactions() {
        // Updated formatter to handle optional fractional seconds

        List<Map<String, String>> resultList = dbFacade.searchTransaction("%",
                "transaction_id", "subject", "description", "department", "classification", "status", "created_date", "updated_date", "pdf_path");

        for (Map<String, String> row : resultList) {
            try {
                Transaction transaction = new Transaction.Builder(
                        Integer.parseInt(row.get("transactionId")),
                        row.get("subject"),
                        row.get("description")
                )
                        .department(row.get("department"))
                        .classification(row.get("classification"))
                        .status(row.get("status"))
//                        .createdDate(LocalDateTime.parse(row.get("created_date")))
//                        .updatedDate(LocalDateTime.parse(row.get("updated_date")))
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .pdfPath(row.get("pdf_path"))
                        .build();

                transactions.add(transaction);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + e.getMessage());
                // Optionally log or skip the row
            }
        }
    }

}
