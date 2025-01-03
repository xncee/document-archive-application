package services;

import data.DBFacade;
import data.Tables;

public class SignUpServices {
    private static final DBFacade dbFacade = DBFacade.getInstance();

    public static boolean isUsernameAvailable(String username) {
        return dbFacade.search(Tables.USERS.getTableName(), username, true, "username").isEmpty();
    }

    public static boolean isEmailAvailable(String email) {
        return dbFacade.search(Tables.USERS.getTableName(), email, true, "email").isEmpty();
    }
}
