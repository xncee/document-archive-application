package model;

import javafx.scene.control.TextField;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsValidator {
    private static boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Split the name into first and last name
        String[] nameParts = name.trim().split("\\s+");

        return nameParts.length == 2;
    }

    public static boolean validateUsername(String username) {
        if (username == null || username.length() < 4) {
            return false;
        }

        // Regex pattern for only letters (upper or lower case), numbers, and underscores
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean validateEmail(String email) {
        if (isFieldEmpty(email)) return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        if (isFieldEmpty(password)) return false;

        String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validateFilePath(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
}
