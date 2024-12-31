package data;

import java.sql.*;
import java.util.*;

public class DBManager {
    private final Connection connection;

    DBManager(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    public boolean isConnected() {
        try {
            return connection != null && connection.isValid(2); // Timeout in seconds
        }
        catch (SQLException e) {
            return false;
        }
    }

    public List<Map<String, String>> search(String table, Object value, boolean match, String... columns) {
        List<Map<String, String>> results = new ArrayList<>();

        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        StringBuilder query = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" LIKE ?");
            if (i < columns.length - 1) {
                query.append(" OR ");
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            String v = (String) value;

            if (!match)
                v = "%"+v+"%";
            for (int i = 0; i < columns.length; i++) {
                stmt.setObject(i + 1, v);
            }

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Process each row
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String columnValue = rs.getString(i);
                        row.put(columnName, columnValue);
                    }
                    results.add(row);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing search query: " + e.getMessage(), e);
        }

        return results;
    }
    public List<Map<String, String>> search(String table, Object value, String... columns) {
        return search(table, value, false, columns);
    }

    public boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);
            statement.setString(3, password);
            try (ResultSet res = statement.executeQuery()) {
                return res.next();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int insert(String table, Map<String, Object> colValMap) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
            columns.append(entry.getKey()).append(",");
            values.append("'").append(entry.getValue()).append("',");
        }

        // Remove trailing commas
        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(query);
    }

    private String getPK(String table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, table);

        return primaryKeys.getString("COLUMN_NAME");
    }

    public boolean update(String table, String column, String value) {
        return true;
    }

    public boolean delete(String table, String pk) {
        return true;
    }
}
