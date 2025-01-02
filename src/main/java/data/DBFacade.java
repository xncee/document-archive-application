package data;

import exceptions.DatabaseOperationException;
import models.Document;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DBFacade {
    private static DBFacade instance;

    private final String USERS_TABLE = "users";
    private static DBManager dbManager = null;

    private DBFacade(String dbUrl) {
        // if (dbManager != null) return;

        try {
            dbManager = new DBManager(dbUrl);
            instance = this;
            System.out.println("Connected to database.");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBFacade getInstance(String dbUrl) {
        if (instance == null) {
            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new IllegalArgumentException("Database URL cannot be null or empty.");
            }
            instance = new DBFacade(dbUrl);
        }

        return instance;
    }
    public static DBFacade getInstance() {
        return getInstance(null);
    }

    public boolean isConnected() throws SQLException {
        return dbManager.isConnected();
    }

    public List<Map<String, Object>> search(String table, Object value, boolean match, String... columns) {
        try {
            return dbManager.search(table, value, null, match, columns);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while searching users in database.", e);
        }
    }

    public int getCount(String table) throws DatabaseOperationException {
        try {
            return dbManager.getCount(table); // Delegate the call to DBManager
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while retrieving the count from table: " + table, e);
        }
    }

    public List<Map<String, Object>> getDataByDateRange(String table, String dateColumn, LocalDate startDate, LocalDate endDate) throws DatabaseOperationException {
        try {
            return dbManager.getDataByDateRange(table, dateColumn, startDate, endDate);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while fetching data from " + table + " within the date range.", e);
        }
    }

    public List<Map<String, Object>> getLimitedRows(String table, int limit) {
        try {
            return dbManager.getLimited(table, limit);
        } catch (SQLException e) {
            System.out.println(e);
            return Collections.emptyList(); // Return an empty list in case of failure
        }
    }

    public List<Map<String, Object>> getValuesAfterPK(String table, String pk, int value) {
        try {
            return dbManager.getValuesAfterPK(table, pk, value);
        } catch (SQLException e) {
            //throw new DatabaseOperationException("An error occurred while fetching data from "+table+" in method getValuesAfterPK.", e);
            return Collections.emptyList(); // Return an empty list in case of failure
        }
    }

    public Map<String, Object> getUserByUsername(String username) throws SQLException {
        return dbManager.getUserByUsername(username);
    }

    public Map<String, Object> authUser(String username, String password) {
        try {
            return dbManager.checkUser(username, password);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while authenticating user in authUser method.", e);
        }
    }

    public int addUser(String username, String email, String password, String name) throws DatabaseOperationException {
        Map<String, Object> map = Map.of(
                "username", username,
                "email", email,
                "password", password,
                "fullname", name,
                "isAdmin", 0
        );
        try {
            return (int) dbManager.insert("users", map);  // returns auto-generated userId.
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while adding user to database.", e);
        }
    }

    public boolean removeUser(String id) throws DatabaseOperationException {
        try {
            return dbManager.delete(USERS_TABLE, "id", id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while removing user from database.", e);
        }
    }

    public String addDocument(Document document) throws DatabaseOperationException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", document.getId());
        map.put("uploaderId", document.getUploaderId());
        map.put("title", document.getTitle());
        map.put("description", document.getDescription());
        map.put("department", document.getDepartment());
        map.put("classification", document.getClassification());
        map.put("status", document.getStatus());
        map.put("deadline", document.getDeadline());
        map.put("createdDate", document.getCreatedDate());
        map.put("updatedDate", document.getUpdatedDate());
        map.put("filePath", document.getFilePath());
        try {
            return (String) dbManager.insert("documents", map); // returns the generated id.
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while adding document to database.", e);
        }
    }

    public List<Map<String, Object>> getTopDocuments(int n) throws DatabaseOperationException {
        try {
            return dbManager.getLimited("documents", n);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while fetching "+n+" documents from database.", e);
        }
    }

    public void addActivity(String documentId, int userId, String description, LocalDateTime datetime) throws DatabaseOperationException {
        Map<String, Object> map = Map.of(
                "documentId", documentId,
                "userId", userId,
                "description", description,
                "datetime", datetime
        );
        try {
            dbManager.insert("ActivityLog", map);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while logging activity to database.", e);
        }
    }
    private Object addEntity(String table, Map<String, Object> map) throws DatabaseOperationException {
        try {
            return dbManager.insert(table, map);
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while adding entity to database.", e);
        }
    }
    public List<Map<String, Object>> getDepartments() {
        try {
            return dbManager.getFromTable("departments", "name");
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while retrieving documents from database.", e);
        }
    }

    public List<Map<String, Object>> getClassifications() throws DatabaseOperationException {
        try {
            return dbManager.getFromTable("classifications", "name");
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while getting classifications from database.", e);
        }
    }
    public int getStatusCount(String status) throws DatabaseOperationException {
        try {
            return dbManager.search("documents", status, null, true, "status").size();
        } catch (SQLException e) {
            throw new DatabaseOperationException("An error occurred while getting "+status+" status count from database.", e);
        }
    }
}
