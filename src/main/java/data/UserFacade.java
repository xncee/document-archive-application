package data;

import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFacade {

    private static UserFacade instance; // Singleton instance
    private DBFacade dbFacade;

    // Private constructor to prevent direct instantiation
    private UserFacade() {
        dbFacade = DBFacade.getInstance();  // Access the Singleton instance of DBFacade
    }

    // Public method to get the Singleton instance
    public static UserFacade getInstance() {
        if (instance == null) {
            synchronized (UserFacade.class) {  // Ensure thread-safety
                if (instance == null) {
                    instance = new UserFacade();
                }
            }
        }
        return instance;
    }

    // Authenticate User
    public boolean authUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (PreparedStatement statement = dbFacade.getConnection().prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);  // Using username or email for login

            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String storedHash = res.getString("password");
                    try {
                        return Hashing.match(storedHash, password);
                    } catch (Exception e) {
                        e.printStackTrace(); // Consider logging the error properly
                    }
                }
            }
        }
        return false;
    }

    // Fetch User by Username
    public User fetchUser(String username) throws SQLException {
        String sql = "SELECT userId, username, email, fullname FROM users WHERE username = ?";
        try (PreparedStatement stmt = dbFacade.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null; // Return null if user not found
    }

    // Fetch Users with Pagination (Offset and Limit)
    public List<User> getUsersWithPagination(int offset, int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        // SQL Server query with pagination using OFFSET and FETCH NEXT
        String query = "SELECT * FROM users ORDER BY username OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement stmt = dbFacade.getConnection().prepareStatement(query)) {
            stmt.setInt(1, offset);  // Skip the number of rows specified by offset
            stmt.setInt(2, limit);   // Limit the number of rows fetched

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error properly
            throw e; // Rethrow so the caller can handle the exception
        }
        return users;
    }

    // Count Total Users (for Pagination Calculation)
    public int getTotalUserCount() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM users";
        try (Statement stmt = dbFacade.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error properly
            throw e; // Rethrow so the caller can handle the exception
        }
        return 0; // Default to 0 if error occurs
    }

    // Map ResultSet to User Object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("username"),
                rs.getString("fullname"),
                rs.getString("email")
        );
    }

    // Add New User
    public boolean addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, fullname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbFacade.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            return stmt.executeUpdate() > 0; // Returns true if a new user is added
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error properly
            throw e; // Rethrow so the caller can handle the exception
        }
    }

    // Update User Details
    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, fullname = ?, email = ?, password = ? WHERE userId = ?";
        try (PreparedStatement stmt = dbFacade.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getUserId());
            return stmt.executeUpdate() > 0; // Returns true if the user is updated
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error properly
            throw e; // Rethrow so the caller can handle the exception
        }
    }

    // Delete User by userId
    public boolean deleteUser(String userId) throws SQLException {
        String query = "DELETE FROM users WHERE userId = ?";
        try (PreparedStatement stmt = dbFacade.getConnection().prepareStatement(query)) {
            stmt.setString(1, userId);
            return stmt.executeUpdate() > 0; // Returns true if the user is deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error properly
            throw e; // Rethrow so the caller can handle the exception
        }
    }
}
