package models;

import java.io.File;
import java.time.LocalDate;

public class Document {
    private final String id;
    private final String uploaderId;
    private final String title;
    private final String description;
    private final String department;
    private final String classification;
    private final String status;
    private final LocalDate deadline;
    private final LocalDate createdDate;
    private final LocalDate updatedDate;
    private final String filePath;

    // Private constructor to ensure objects are created through the builder
    private Document(Builder builder) {
        // Validate mandatory fields
        if (
                builder.title == null || builder.title.isBlank() ||
                builder.description == null || builder.description.isBlank() ||
                builder.department == null || builder.department.isBlank() ||
                builder.classification == null || builder.classification.isBlank() ||
                builder.status == null ||
                builder.uploaderId == null) {
            throw new IllegalArgumentException("Required fields cannot be null or blank");
        }

        this.uploaderId = builder.uploaderId;
        this.id = (builder.id != null) ? builder.id : generateId(); // Generate ID if not provided
        this.title = builder.title;
        this.description = builder.description;
        this.department = builder.department;
        this.classification = builder.classification;
        this.status = builder.status;
        this.deadline = builder.deadline; // deadline can be null
        this.createdDate = (builder.createdDate != null) ? builder.createdDate : LocalDate.now(); // Default created date
        this.updatedDate = builder.updatedDate; // updatedDate can be null initially
        this.filePath = builder.filePath; // filePath can be null if the user didn't upload a file
    }

    // Getters for fields
    public String getId() {
        return id;
    }

    public String getUploaderId() {
        return uploaderId;
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
                ", uploaderId=" + uploaderId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", classification='" + classification + '\'' +
                ", status='" + status + '\'' +
                ", deadline=" + deadline +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    // Builder Class
    public static class Builder {
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

        // Required fields constructor
        public Builder(String status, String uploaderId, String title, String description, String department, String classification) {
            this.status = status;
            this.uploaderId = uploaderId;
            this.title = title;
            this.description = description;
            this.department = department;
            this.classification = classification;
        }

        // Optional fields setters
        public Builder id(String id) {
            if (!id.isBlank())
                this.id = id;
            return this;
        }

        public Builder deadline(LocalDate deadline) {
            this.deadline = deadline;
            return this;
        }

        public Builder createdDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder updatedDate(LocalDate updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        // Build method to create a Document instance
        public Document build() {
            return new Document(this);
        }
    }
}