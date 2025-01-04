package services;

import data.DBFacade;
import io.github.cdimascio.dotenv.Dotenv;

public class Test {
    public static void main(String[] args) {
        String dbUrl = Dotenv.load().get("DATABASE_URL");
        DBFacade f = DBFacade.getInstance(dbUrl);
//        List<Map<String, String>> x = f.test("users", "alice.johnson5@example.com", true, "username");
//        System.out.println(x.isEmpty());
    }
}
