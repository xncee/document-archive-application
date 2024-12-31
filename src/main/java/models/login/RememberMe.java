package models.login;

import java.util.prefs.Preferences;

public class RememberMe {

    private static final String USERNAME_KEY="username";
    private static final String PASSWORD_KEY="password";

    //after the user checks "Remember Me" ChickBox
    public static void saveCredentials(String username,String password) {
        Preferences pref = Preferences.userRoot().node(RememberMe.class.getName());
        pref.put(USERNAME_KEY,username);
        pref.put(PASSWORD_KEY,password);
    }
    // when the app starts
    public static String[] getCredentials() {
        Preferences pref = Preferences.userRoot().node(RememberMe.class.getName());
        String username = pref.get(USERNAME_KEY, null);
        String password = pref.get(PASSWORD_KEY, null);
        return new String[] {username, password};
    }

    // when the user logs out
    public static void clearCredentials() {
        Preferences pref = Preferences.userRoot().node(RememberMe.class.getName());
        pref.remove(USERNAME_KEY);
        pref.remove(PASSWORD_KEY);
    }
}
