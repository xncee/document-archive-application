package services;

import data.DBFacade;

import java.sql.SQLException;

public class SignUpServices {
    private static final DBFacade dbFacade = DBFacade.getInstance();

    public static boolean isUsernameAvailable(String username) {
        return dbFacade.searchUsers(username, true, "username").isEmpty();
    }

    public static boolean isEmailAvailable(String email) {
        return dbFacade.searchUsers(email, true, "email").isEmpty();
    }
}
