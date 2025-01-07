package data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exceptions.DatabaseConnectionException;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DBManager {
    private static volatile DBManager instance;
    private static HikariDataSource dataSource;

    public DBManager(String dbUrl) throws IllegalArgumentException, DatabaseConnectionException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            throw new IllegalArgumentException("Database URL cannot be null or empty");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setMaximumPoolSize(10); // Set the max pool size as needed
        config.setMinimumIdle(5); // Set the minimum idle connections

        dataSource = new HikariDataSource(config);
    }

    public static DBManager getInstance(String dbUrl) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(dbUrl);
                }
            }
        }
        return instance;
    }

    // Public method to get the instance without specifying the URL
    public static DBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DBManager instance is not initialized. Call getInstance() first.");
        }
        return instance;
    }

    // Get a connection from the pool
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not initialized.");
        }
        return dataSource.getConnection();
    }

    public boolean isConnected() throws SQLException {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        }
    }

    public Map<String, Object> mapData(ResultSet rs, ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object columnValue = rs.getObject(i);
            row.put(columnName, columnValue);
        }
        return row;
    }

    public List<Map<String, Object>> getLimited(String table, int limit) throws SQLException {
        String query = String.format("SELECT TOP %d * FROM %s", limit, table);

        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                results.add(mapData(rs, metaData));
            }
        }
        return results;
    }

    public List<Map<String, Object>> getValuesAfterPK(String table, String pk, int value) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE ? > ? ORDER BY id ASC", table);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, pk);
            stmt.setInt(2, value);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData));
                }
            }
        }

        return results;
    }

    public int getCount(String table) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + table;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new SQLException("Failed to retrieve count from table: " + table);
        }
    }

    public List<Map<String, Object>> getDataByDateRange(String table, String dateColumn, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        String query = "SELECT * FROM " + table + " WHERE " + dateColumn + " BETWEEN ? AND ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData));
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> search(String table, Object value, String orderBy, boolean match, int offset, String... columns) throws SQLException {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" LIKE ?");
            if (i < columns.length - 1) {
                query.append(" OR ");
            }
        }

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            String searchValue = value.toString();
            if (!match) {
                searchValue = "%" + searchValue + "%";
            }

            for (int i = 0; i < columns.length; i++) {
                stmt.setObject(i + 1, searchValue);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData));
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> search(String table, Object value, String orderBy, int offset, String... columns) throws SQLException {
        return search(table, value, orderBy, false, offset, columns);
    }

    public Object insert(String table, Map<String, Object> colValMap) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
            columns.append(entry.getKey()).append(",");
            placeholders.append("?").append(",");
        }
        columns.setLength(columns.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
                stmt.setObject(index++, entry.getValue());
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getObject(1);
                } else {
                    throw new SQLException("Insertion failed, no generated key obtained.");
                }
            }
        }
    }

    public boolean update(String table, String column, String value, String pkColumn, String pkValue) throws SQLException {
        String query = "UPDATE " + table + " SET " + column + " = ? WHERE " + pkColumn + " = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, value);
            stmt.setString(2, pkValue);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean delete(String table, String column, Object value) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, value);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Map<String, Object>> getFromTable(String table, String... columns) throws SQLException {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(",");
        }
        query.setLength(query.length() - 1);
        query.append(" FROM ").append(table);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query.toString());
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                results.add(mapData(rs, metaData));
            }
        }

        return results;
    }
}
