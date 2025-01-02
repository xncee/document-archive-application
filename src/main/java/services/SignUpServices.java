package services;

import data.DBFacade;
import data.TABLES;

import java.sql.SQLException;

public class SignUpServices {
    private static final DBFacade dbFacade = DBFacade.getInstance();

    public static boolean isUsernameAvailable(String username) {
        return dbFacade.search(TABLES.USERS.getTableName(), username, true, "username").isEmpty();
    }

    public static boolean isEmailAvailable(String email) {
        return dbFacade.search(TABLES.USERS.getTableName(), email, true, "email").isEmpty();
    }
}
