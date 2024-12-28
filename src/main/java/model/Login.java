package model;

import database.DBFacade;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Login {
    private final DBFacade dbFacade;

    public Login(String dbUrl) {
        this.dbFacade = new DBFacade(dbUrl);
    }

    public boolean signIn(String username, String password) {
        try {
            return dbFacade.authUser(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean signUp(String username, String email, String fullName, String password) {
        try {
            if (isUsernameAvailable(username)) {
                return false;
            }
            Map<String, Object> userValues = Map.of(
                    "username", username,
                    "email", email,
                    "full_name", fullName,
                    "password", password
            );
            int rowsAffected = dbFacade.getDbManager().insert("users", userValues);
            return rowsAffected > 0;

        } catch (SQLException e) {
//          e.printStackTrace();
            return false;
        }
    }


    public boolean isUsernameAvailable(String username) {
        List<Map<String, String>> userData = dbFacade.searchUsers(username, true, "username");
        return userData.isEmpty();
    }

    public boolean isUserPasswordAvailable(String username, String password) {
        List<Map<String, String>> userData = dbFacade.searchUsers(username, true, "username");
        if (userData.isEmpty()) {
            return false;
        }
        String storedPassword = userData.get(0).get("password");
        return storedPassword.equals(password);
    }
}
