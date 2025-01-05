package models.login;

import data.UserFacade;
import exceptions.DatabaseOperationException;
import io.github.cdimascio.dotenv.Dotenv;
import data.DBFacade;
import models.User;
import utils.ErrorHandler;

import java.sql.SQLException;
import java.util.Map;

public class Login {
    private static final UserFacade userFacade = UserFacade.getInstance();
    public static Login instance = null;
    public User user;

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
            boolean valid = userFacade.authUser(username, password);

            if (valid) {
                this.user = userFacade.fetchUser(username);
                return true; // Successfully signed in
            }
        } catch (Exception e) {
            ErrorHandler.handle(e, "An error occurred during sign-in");
        }

        return false; // Sign-in failed
    }

    public boolean signUp(String username, String email, String fullname, String password) {
        User user = new User(username, fullname, email,password);
        try {
            if (!userFacade.addUser(user))
                return false;
            this.user = userFacade.fetchUser(username);
        } catch (SQLException e) {
            // Handle or log the exception here
            ErrorHandler.handle(e, "An error occurred while fetching user information.");
            return false;
        }

        return true;
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }
}
