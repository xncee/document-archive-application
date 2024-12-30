package model;

public enum LoginError {
    INVALID_CREDENTIALS("Invalid username or password. Please try again.");
    // Add other error messages if needed

    private final String message;

    LoginError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}