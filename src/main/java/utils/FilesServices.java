package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FilesServices {
    public static File askForFileLocation(List<String> allowedExtensions) {
        try {
            // Set the Look and Feel to Windows
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String allowed = "";
        if (allowedExtensions != null) {
            Iterator<String> extensions = allowedExtensions.iterator();
            while (extensions.hasNext()) {
                allowed += "*." + extensions.next();
                if (extensions.hasNext()) {
                    allowed += ";";
                }
            }
        }

        JFrame alwaysOnTopFrame = new JFrame();
        alwaysOnTopFrame.setAlwaysOnTop(true);
        alwaysOnTopFrame.setUndecorated(true);
        alwaysOnTopFrame.setType(Window.Type.UTILITY); // Makes it a small utility window
        alwaysOnTopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        alwaysOnTopFrame.setLocationRelativeTo(null); // Centered on the screen
        alwaysOnTopFrame.setVisible(true);

        // Create and configure JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Select a File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (allowedExtensions != null)
            fileChooser.setFileFilter(new FileNameExtensionFilter(allowed, allowedExtensions.toArray(new String[0])));

        // Show the JFileChooser dialog
        File selectedFile = null;
        int result = fileChooser.showOpenDialog(alwaysOnTopFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }

        // Dispose of the invisible frame to avoid leaving it in memory
        alwaysOnTopFrame.dispose();

        return selectedFile;
    }

    public static File askForFileLocation() {
        return askForFileLocation(null);
    }
}
