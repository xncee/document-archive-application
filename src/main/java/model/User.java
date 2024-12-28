package model;

import java.util.UUID;

public class User {
    private final String userId;
    private String username;
    private String fullname;
    private String email;
    private String password;

    // Constructor for required fields
    public User(String username, String fullname, String email, String password) {
        if (username == null || fullname == null || email == null || password == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.userId = generateUserId();
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    // Constructor with an existing userId
    public User(String userId, String username, String fullname, String email, String password) {
        if (username == null || fullname == null || email == null || password == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.userId = userId != null ? userId : generateUserId();
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters (optional, depending on requirements)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Generate a unique user ID
    private String generateUserId() {
        return "USER-" + UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='********'" +
                '}';
    }
}
