package models.login;

import exceptions.DatabaseOperationException;
import io.github.cdimascio.dotenv.Dotenv;
import data.DBFacade;
import utils.ErrorHandler;

import java.sql.SQLException;
import java.util.Map;

public class Login {
    public static Login instance = null;
    private int id;
    private String username;
    private String email;
    private String fullname;

    private final static DBFacade dbFacade = DBFacade.getInstance(Dotenv.load().get("DATABASE_URL"));
    public Login() {
        instance = this;
    }

    public static Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }
    public boolean signIn(String username, String password) {
        try {
            // Attempt to authenticate the user (null is returned if authentication failed)
            boolean valid = dbFacade.authUser(username, password);

            if (valid) {
                Map<String, Object> userInfo = dbFacade.fetchUser(username);
                // Set user fields from the retrieved information
                this.id = userInfo.get("userId") instanceof Integer
                        ? (Integer) userInfo.get("userId")
                        : Integer.parseInt(String.valueOf(userInfo.get("userId")));

                this.username = username.toLowerCase(); // Standardize username
                this.email = String.valueOf(userInfo.get("email"));
                this.fullname = String.valueOf(userInfo.get("fullname"));

                return true; // Successfully signed in
            }
        } catch (Exception e) {
            ErrorHandler.handle(e, "An error occurred during sign-in");
        }

        return false; // Sign-in failed
    }

    public boolean signUp(String username, String email, String fullname, String password) {
        try {
            if (!dbFacade.addUser(username, email, password, fullname)) {
                return false;
            }
        } catch (DatabaseOperationException e) {
            // Log the error or provide more details
            return false;
        }

        Map<String, Object> userInfo = null;
        try {
            userInfo = dbFacade.fetchUser(username);
        } catch (SQLException e) {
            // Handle or log the exception here
            ErrorHandler.handle(e, "An error occurred while fetching user information.");
            return false;
        }

        if (userInfo != null) {
            this.id = (int) userInfo.get("userId");
            this.username = username.toLowerCase();
            this.email = email.toLowerCase();
            this.fullname = fullname;
        }
        return true;
    }

    public void logout() {
        this.id = 0;
        this.username = null;
        this.email = null;
        this.fullname = null;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }
}
