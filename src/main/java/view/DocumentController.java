package view;

import database.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class DocumentController {

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

    public DocumentController() {
        String dbUrl = Dotenv.load().get("DATABASE_URL");
        this.dbFacade = new DBFacade(dbUrl);
    }

    @FXML
    public void initialize() {

    }

    private void loadDocuments() {

    }

}
