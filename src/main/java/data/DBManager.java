package data;

import java.sql.*;
import java.util.*;

public class DBManager {
    private final Connection connection;

    DBManager(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    public boolean isConnected() throws SQLException {
        return connection != null && connection.isValid(2); // Timeout in seconds
    }

    public Map<String, Object> mapData(ResultSet rs, ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            // Dynamically handle different data types (use Object data type)
            Object columnValue = rs.getObject(i);
            row.put(columnName, columnValue);
        }
        return row;
    }

    public List<Map<String, Object>> search(String table, Object value, boolean match, String... columns) throws SQLException {
        // Ensure that at least one column is specified
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        List<Map<String, Object>> results = new ArrayList<>();

        // Build the dynamic SQL query using StringBuilder
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]).append(" LIKE ?");
            if (i < columns.length - 1) {
                query.append(" OR ");
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            String searchValue = value.toString();  // Convert the value to string

            // Adjust the value based on the 'match' flag
            if (!match) {
                searchValue = "%" + searchValue + "%";  // Use LIKE for partial match
            }

            // Set the parameters for the prepared statement
            for (int i = 0; i < columns.length; i++) {
                stmt.setObject(i + 1, searchValue);  // Set the value for each column
            }

            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData));  // Add the row data to the result list
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> search(String table, Object value, String... columns) throws SQLException {
        return search(table, value, false, columns); // Call the original search with 'false' for partial matching
    }

    public Map<String, Object> checkUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);  // Using username or email for login
            statement.setString(3, password);  // Hashed password comparison

            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    ResultSetMetaData metaData = res.getMetaData();
                    return mapData(res, metaData);  // If user exists, map data to Map
                }
                return null;  // Return null if no user is found
            }
        }
    }

    public int insert(String table, Map<String, Object> colValMap) throws SQLException {
        // Build the column names and placeholders for values
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
            columns.append(entry.getKey()).append(",");
            placeholders.append("?").append(",");  // Use placeholders for PreparedStatement
        }

        // Remove trailing commas
        columns.setLength(columns.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        // Prepare the SQL query with placeholders
        String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ")";

        // Use PreparedStatement to prevent SQL injection
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;

            // Bind the values to the placeholders in the prepared statement
            for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
                stmt.setObject(index++, entry.getValue());
            }

            // Execute the insert statement
            return stmt.executeUpdate();
        }
    }

    private String getPK(String table) throws SQLException {
        // A table can have multiple columns as PK.
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, table);

        StringBuilder pkColumns = new StringBuilder();

        // Iterate through all primary key columns (if any)
        while (primaryKeys.next()) {
            String pkColumn = primaryKeys.getString("COLUMN_NAME");
            if (!pkColumns.isEmpty()) {
                pkColumns.append(", ");
            }
            pkColumns.append(pkColumn);
        }

        // Close the resources
        primaryKeys.close();

        // If no primary key found, return null
        if (pkColumns.isEmpty()) {
            return null;
        }

        return pkColumns.toString();
    }


    public boolean update(String table, String column, String value) {
        return true;
    }

    public boolean delete(String table, String pk) {
        return true;
    }

    public List<Map<String, Object>> getFromTable(String table, String... columns) throws SQLException {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        List<Map<String, Object>> results = new ArrayList<>();

        // Constructing the query
        StringBuilder query = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ").append(table);

        // Execute the query
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query.toString())) {
            ResultSetMetaData metaData = rs.getMetaData();
            // Process each row
            while (rs.next()) {
                results.add(mapData(rs, metaData));
            }
        } catch (SQLException e) {
            throw new SQLException("An error occurred while fetching data from table: " + table, e);
        }

        return results;
    }

    public Map<String, Object> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT userId, username, email, fullname FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    return mapData(rs, metaData);
                }
            }
        }
        return null;
    }
}
