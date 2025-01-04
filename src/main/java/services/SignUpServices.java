package services;

import data.DBFacade;
import data.Tables;

public class SignUpServices {
    private static final DBFacade dbFacade = DBFacade.getInstance();

    public static boolean isUsernameAvailable(String username) {
        return dbFacade.search(Tables.USERS.getTableName(), username, true, 0, "username").isEmpty();
    }

    public static boolean isEmailAvailable(String email) {
        return dbFacade.search(Tables.USERS.getTableName(), email, true, 0, "email").isEmpty();
    }
}
