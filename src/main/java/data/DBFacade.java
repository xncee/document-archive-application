package data;

import exceptions.DatabaseOperationException;
import utils.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DBFacade {
    private static DBFacade instance;
    private static DBManager dbManager;

    public DBFacade(String dbUrl) {
        if (dbManager == null) {
            dbManager = DBManager.getInstance(dbUrl);
            System.out.println("Connected to database.");
        }
        instance = this;
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
        return dbManager != null && dbManager.isConnected();
    }

    public Connection getConnection() throws SQLException {
        return dbManager.getConnection();
    }
    public static DBManager getDbManager() {
        return dbManager;
    }

    public List<Map<String, Object>> search(String table, Object value, boolean match, int offset, String... columns) {
        try {
            return dbManager.search(table, value, null, match, offset, columns);
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

    public List<Map<String, Object>> getDepartments() {
        try {
            return dbManager.getFromTable("departments", "name");
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while retrieving departments from database.");
            return null;
        }
    }

    public List<Map<String, Object>> getClassifications() {
        try {
            return dbManager.getFromTable("classifications", "name");
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while retrieving classifications from database.");
            return null;
        }
    }
    public int getStatusCount(String status) {
        try {
            return dbManager.search("documents", status, null, true, 0, "status").size();
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while getting "+status+" status count from database.");
            return -1;
        }
    }
}
