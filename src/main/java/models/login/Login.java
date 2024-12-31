package models.login;

import io.github.cdimascio.dotenv.Dotenv;
import data.DBFacade;

import java.sql.SQLException;

public class Login {
    public static Login instance = null;
    private String username;
    private String email;
    private String fullname;

    private final static DBFacade dbFacade = new DBFacade(Dotenv.load().get("DATABASE_URL"));
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
            boolean loggedIn = dbFacade.authUser(username, password);
            if (loggedIn) {
                this.username = username.toLowerCase();
            }
            return loggedIn;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean signUp(String username, String email, String fullname, String password) {
        try {
            boolean created = dbFacade.addUser(username, email, password, fullname);
            if (created) {
                this.username = username.toLowerCase();
                this.email = email.toLowerCase();
                this.fullname = fullname;
            }
            return created;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
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
