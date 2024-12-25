package database;

import model.Transaction;
import model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFacade {
    private final String TRANSACTIONS_TABLE = "transactions";
    private final String USERS_TABLE = "users";
    private final DBManager dbManager;

    public DBFacade(String dbUrl) {
        try {
            dbManager = new DBManager(dbUrl);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, String>> searchTransaction(Object value, String... columns) {
        return dbManager.search(TRANSACTIONS_TABLE, value, columns);
    }
    public boolean addTransaction(Transaction transaction) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        //map.put("transaction_id", transaction.getTransactionId());
        map.put("subject", transaction.getSubject());
        map.put("description", transaction.getDescription());
        map.put("department", transaction.getDepartment());
        map.put("classification", transaction.getClassification());
        map.put("status", transaction.getStatus());
        map.put("created_date", transaction.getCreatedDate());
        map.put("updated_date", transaction.getUpdatedDate());
        map.put("pdf_path", transaction.getPdfPath());

        return dbManager.insert(TRANSACTIONS_TABLE, map) > 0;
    }
    public boolean removeTransaction(String id) {
        return dbManager.delete(TRANSACTIONS_TABLE, id);
    }
    public boolean updateTransactionStatus(String id, String status) {
        return dbManager.update(TRANSACTIONS_TABLE, "status", status);
    }

    public List<Map<String, String>> searchUsers(Object value, String... columns) {
        return dbManager.search(USERS_TABLE, value, columns);
    }
    public boolean addUser(User user) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        //map.put("user_id", user.getUserId());
        map.put("name", user.getFullName());
        map.put("email", user.getEmail());
        map.put("password", user.getPassword());
        map.put("date_of_birth", user.getBirthDate());
        map.put("gender", user.getGender());
        map.put("phone_number", user.getPhoneNumber());
        map.put("address", user.getAddress());
        map.put("profile_picture", user.getProfileImage());

        return dbManager.insert("users", map) > 0;
    }
    public boolean removeUser(String id) {
        return dbManager.delete(USERS_TABLE, id);
    }
}
