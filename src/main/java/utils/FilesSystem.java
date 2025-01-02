package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

public class FilesSystem {
    private static final String COPY_DIRECTORY = "/";
    public static String copyFile(String source) {
        Path sourceFile = Paths.get(source);
        Path destinationDirectory = Paths.get(COPY_DIRECTORY);

        // Ensure the destination directory exists; create it if it doesn't
        try {
            Files.createDirectories(destinationDirectory); // Creates the directory if it doesn't exist
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating destination directory: "+COPY_DIRECTORY);
            return null;
        }

        // Create the destination file path by appending the source file name to the destination directory
        Path destinationFile = destinationDirectory.resolve(sourceFile.getFileName());

        // Check if the file already exists and modify its name if necessary
        if (Files.exists(destinationFile)) {
            destinationFile = getUniqueFileName(destinationFile);
        }

        // Copy the file to the destination directory
        try {
            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied: "+sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred while copying the file: "+sourceFile);
            return null;
        }

        return destinationFile.toFile().getAbsolutePath();
    }

    // Method to generate a unique file name by appending a suffix
    private static Path getUniqueFileName(Path file) {
        String fileName = file.getFileName().toString();
        String nameWithoutExtension = "";
        String extension = "";

        // Split the file name and extension if it exists
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            nameWithoutExtension = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        } else {
            nameWithoutExtension = fileName;
        }

        // Generate a unique name by appending an index to the name
        int counter = 1;
        Path newFile = file.getParent().resolve(nameWithoutExtension + "_" + counter + extension);
        while (Files.exists(newFile)) {
            counter++;
            newFile = file.getParent().resolve(nameWithoutExtension + "_" + counter + extension);
        }

        return newFile;
    }

}
