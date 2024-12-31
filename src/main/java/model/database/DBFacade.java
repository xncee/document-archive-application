package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class DBFacade {
    private static DBFacade instance;

    private final String USERS_TABLE = "users";
    private static DBManager dbManager = null;

    public DBFacade(String dbUrl) {
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
            synchronized (DBFacade.class) {
                if (instance == null) {
                    instance = new DBFacade(dbUrl);
                }
            }
        }

        return instance;
    }
    public static DBFacade getInstance() {
        return getInstance(null);
    }

    public boolean isConnected() {
        return dbManager.isConnected();
    }

    public boolean addUser(String username, String email, String password, String name) throws SQLException {
        Map<String, Object> map = Map.of(
                "username", username,
                "email", email,
                "password", password,
                "fullname", name,
                "isAdmin", 0
        );
        return dbManager.insert("users", map) > 0;
    }

    public boolean authUser(String username, String password) throws SQLException {
        return dbManager.checkUser(username, password);
    }

    public List<Map<String, String>> searchUsers(Object value, boolean match, String... columns) {
        return dbManager.search(USERS_TABLE, value, match, columns);
    }
    public boolean removeUser(String id) {
        return dbManager.delete(USERS_TABLE, id);
    }

    public boolean addActivity(String documentId, int userId, String description, LocalDateTime datetime) throws SQLException {
        Map<String, Object> map = Map.of(
                "documentId", documentId,
                "userId", userId,
                "description", description,
                "datetime", datetime
        );
        return dbManager.insert("ActivityLog", map) > 0;

    }
    public int getStatusCount(String status) {
        return 0;
    }
}
