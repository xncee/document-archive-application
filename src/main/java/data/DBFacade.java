package data;

import models.Department;
import models.Document;
import utils.ErrorHandler;

import javax.print.Doc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> searchUsers(Object value, boolean match, String... columns) {
        try {
            return dbManager.search(USERS_TABLE, value, match, columns);
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while adding data to the database.");
        }
        return null;
    }

    public Map<String, Object> getUserByUsername(String username) throws SQLException {
        return dbManager.getUserByUsername(username);
    }

    public Map<String, Object> authUser(String username, String password) {
        try {
            return dbManager.checkUser(username, password);
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while validating login details.");
        }
        return null;
    }

    public boolean addUser(String username, String email, String password, String name) {
        Map<String, Object> map = Map.of(
                "username", username,
                "email", email,
                "password", password,
                "fullname", name,
                "isAdmin", 0
        );
        try {
            return dbManager.insert("users", map) > 0;
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while adding data to the database.");
        }
        return false;
    }

    public boolean removeUser(String id) {
        return dbManager.delete(USERS_TABLE, id);
    }

    public boolean addDocument(Document document) {
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
            return dbManager.insert("documents", map) > 0;
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while adding document to the database.");
        }
        return false;
    }

    public boolean addActivity(String documentId, int userId, String description, LocalDateTime datetime) {
        Map<String, Object> map = Map.of(
                "documentId", documentId,
                "userId", userId,
                "description", description,
                "datetime", datetime
        );
        try {
            return dbManager.insert("ActivityLog", map) > 0;
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while adding data to the database.");
        }
        return false;
    }

    public List<Map<String, Object>> getDepartments() {
        try {
            return dbManager.getFromTable("departments", "name");
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while fetching data from database.");
        }
        return null;
    }

    public List<Map<String, Object>> getClassifications() {
        try {
            return dbManager.getFromTable("classifications", "name");
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while fetching data from database.");
        }
        return null;
    }
    public int getStatusCount(String status) {
        return 0;
    }
}
