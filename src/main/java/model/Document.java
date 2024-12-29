package model;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Document {
    private final String id;
    private final String uploaderId;  // Conventionally, use camel case
    private final String title;
    private final String description;
    private final String department;
    private final String classification;
    private final String status;
    private final LocalDate createdDate;
    private final LocalDate updatedDate;
    private final String filePath;

    // Constructor
    public Document(String uploaderId, String title, String description, String department,
                    String classification, String filePath,
                    String id, String status, LocalDate createdDate, LocalDate updatedDate) {

        // Validate mandatory fields
        if (uploaderId == null || title == null || description == null || department == null ||
                classification == null || filePath == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }

        // Initialize fields
        this.uploaderId = uploaderId;
        this.id = (id != null) ? id : generateId(); // Generate ID if not provided
        this.title = title;
        this.description = description;
        this.department = department;
        this.classification = classification;
        this.status = status; // status can be null, if not provided
        this.createdDate = (createdDate != null) ? createdDate : LocalDate.now(); // Default created date
        this.updatedDate = (updatedDate != null) ? updatedDate : LocalDate.now(); // Default updated date
        this.filePath = filePath;

        // Validate file path (optional)
        if (!new File(filePath).exists()) {
            throw new IllegalArgumentException("File does not exist at the given file path");
        }
    }

    // Getter for uploaderId
    public String getUploaderId() {
        return uploaderId;
    }

    // Getters for other fields
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getClassification() {
        return classification;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    // Generate ID if not provided
    private String generateId() {
        return "DOC-" + System.currentTimeMillis();
    }

    // Custom toString method with date formatting
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        return "Document{" +
                "id='" + id + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", classification='" + classification + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate.format(formatter) +
                ", updatedDate=" + updatedDate.format(formatter) +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}