package data;

import java.sql.*;
import java.time.LocalDate;
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
    public List<Map<String, Object>> getLimited(String table, int limit) throws SQLException {
        String query = String.format("SELECT TOP %d * FROM %s", limit, table);

        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                results.add(mapData(rs, metaData)); // Assuming `mapData` converts the ResultSet row to a Map
            }
        }

        return results;
    }

    public List<Map<String, Object>> getValuesAfterPK(String table, String pk, int value) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE ? > ? ORDER BY id ASC", table);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pk);
            stmt.setInt(2, value);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData)); // Map the row data to a map
                }
            }
        }

        return results;
    }

    public int getCount(String table) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + table;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1); // Retrieve the count from the first column
            }
            throw new SQLException("Failed to retrieve count from table: " + table);
        }
    }

    public List<Map<String, Object>> getDataByDateRange(String table, String dateColumn, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        String query = "SELECT * FROM " + table + " WHERE " + dateColumn + " BETWEEN ? AND ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate)); // Convert LocalDate to java.sql.Date
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    results.add(mapData(rs, metaData)); // Map each row to a Map object
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> search(String table, Object value, String orderBy, boolean match, String... columns) throws SQLException {
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

    public List<Map<String, Object>> search(String table, Object value, String orderBy, String... columns) throws SQLException {
        return search(table, value, orderBy, false, columns); // Call the original search with 'false' for partial matching
    }

    public boolean verifyUserPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);  // Using username or email for login

            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String storedHash = res.getString("password");
                    try {
                        return Hashing.match(storedHash, password);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
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

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            for (Map.Entry<String, Object> entry : colValMap.entrySet()) {
                stmt.setObject(index++, entry.getValue());
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getObject(1);  // Return the generated key as an Object
                } else {
                    throw new SQLException("Insertion failed, no generated key obtained.");
                }
            }
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


    public boolean update(String table, String column, String value, String pkColumn, String pkValue) throws SQLException {
        // Build the update query dynamically
        String query = "UPDATE " + table + " SET " + column + " = ? WHERE " + pkColumn + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set the parameters for the update statement
            stmt.setString(1, value);  // Set the new value for the column
            stmt.setString(2, pkValue);

            // Execute the update and check the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean delete(String table, String column, Object value) throws SQLException {
        String sql = "DELETE FROM " + table + " WHERE " + column + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, value);
            return stmt.executeUpdate() > 0;  // Return true if the deletion was successful
        }
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

    public Map<String, Object> fetchUser(String username) throws SQLException {
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
