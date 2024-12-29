package model;

import model.database.DBFacade;

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
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean signUp(String username, String email, String fullName, String password) {
        try {
            if (!isUsernameAvailable(username)) {
                return false;
            }

            return dbFacade.addUser(username, email, password, fullName);

        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }


    public boolean isUsernameAvailable(String username) {
        List<Map<String, String>> userData = dbFacade.searchUsers(username, true, "username");
        return userData.isEmpty();
    }
}
