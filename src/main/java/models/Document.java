package models;

import java.io.File;
import java.time.LocalDate;

public class Document {
    private String id;
    private String uploaderId;
    private String title;
    private String description;
    private String department;
    private String classification;
    private String status;
    private LocalDate deadline;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String filePath;

    // Constructor
    public Document(String status, String uploaderId, String title, String description, String department,
                    String classification, String filePath,
                    String id, LocalDate deadline, LocalDate createdDate, LocalDate updatedDate) {

        // Validate mandatory fields
        if (status == null || uploaderId == null || title == null || description == null || department == null ||
                classification == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }

        // Initialize fields
        this.uploaderId = uploaderId;
        this.id = (id != null) ? id : generateId(); // Generate ID if not provided
        this.title = title;
        this.description = description;
        this.department = department;
        this.classification = classification;
        this.status = status;
        this.deadline = deadline; // deadline can be null
        this.createdDate = (createdDate != null) ? createdDate : LocalDate.now(); // Default created date
        this.updatedDate = (updatedDate != null) ? updatedDate : LocalDate.now(); // Default updated date
        this.filePath = filePath; // filePath can be null if the user didn't upload a file

        // Validate file path (optional)
        if (filePath!= null && !new File(filePath).exists()) {
            throw new IllegalArgumentException("File does not exist at the given file path");
        }
    }

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

    public LocalDate getDeadline() {
        return deadline;
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

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", Classification='" + classification + '\'' +
                ", status='" + status + '\'' +
                ", deadline=" + deadline +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}