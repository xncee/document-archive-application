package model.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBFacade {
    private final String USERS_TABLE = "users";
    private final DBManager dbManager;

    public DBFacade(String dbUrl) {
        try {
            dbManager = new DBManager(dbUrl);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean addUser(String username, String email, String password, String name) throws SQLException {
        Map<String, Object> map = Map.of(
                "username", username,
                "email", email,
                "password", password,
                "name", name
        );
        return dbManager.insert("users", map) > 0;
    }
    public boolean authUser(String username, String password) throws SQLException {
        return dbManager.checkUser(username, password);
    }

    public List<Map<String, String>> searchUsers(Object value, boolean match, String... columns) {
        return dbManager.search(USERS_TABLE, value, match, columns);
    }
    public boolean removeUser(String id) {
        return dbManager.delete(USERS_TABLE, id);
    }
}
