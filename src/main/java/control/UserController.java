package control;

import database.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;
import model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserController {
    private final DBFacade userDBFacade;

    public UserController() {
        userDBFacade = new DBFacade(Dotenv.load().get("DATABASE_URL"));
    }

    // Method to handle user actions, e.g., adding a user
    public void addUser(String fullName, String email, String password) {
        // Create a User object
        User newUser = new User.Builder()
                .setFullName(fullName)
                .setEmail(email)
                .setPassword(password)
                .setBirthDate(LocalDateTime.now().toLocalDate())
                .build();

        // Call the facade to handle the addition of the user
        try {
            userDBFacade.addUser(newUser);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Other controller methods can go here (e.g., updateUser, removeUser, etc.)
}