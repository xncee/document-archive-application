package model;

import io.github.cdimascio.dotenv.Dotenv;
import model.database.DBFacade;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Login {
    private final static DBFacade dbFacade = new DBFacade(Dotenv.load().get("DATABASE_URL"));

    public static boolean signIn(String username, String password) {
        try {
            return dbFacade.authUser(username, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean signUp(String username, String email, String fullName, String password) {
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


    public static boolean isUsernameAvailable(String username) {
        List<Map<String, String>> userData = dbFacade.searchUsers(username, true, "username");
        return userData.isEmpty();
    }
}
