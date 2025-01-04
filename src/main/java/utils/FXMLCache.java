package utils;

import application.ContentSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FXMLCache {
    private static final Map<String, Parent> fxmlCache = new HashMap<>();

    public static void preloadFXML(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(ContentSwitcher.class.getResource(fxmlFile), LocalizationUtil.getResourceBundle());
        Parent content = loader.load();
        fxmlCache.put(fxmlFile, content);
        System.out.println("Page loaded: "+fxmlFile);
    }

    public static Parent getFXML(String fxmlFile) {
        return fxmlCache.getOrDefault(fxmlFile, null);
    }

    public static void clearCache() {
        fxmlCache.clear();
    }
}