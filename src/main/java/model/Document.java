package model;

import java.time.LocalDate;

public class Document {
    private final String id;
    private final String title;
    private final String description;
    private final String department;
    private final String classification;
    private final String status;
    private final LocalDate createdDate;
    private final LocalDate updatedDate;
    private final String pdfPath;


    public Document(String title, String description, String department,
                    String classification, String pdfPath,
                    String id, String status, LocalDate createdDate, LocalDate updatedDate) {
        if (title == null || description == null || department == null || classification == null || pdfPath == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.id = id != null ? id : generateId(); // Generate ID if not provided
        this.title = title;
        this.description = description;
        this.department = department;
        this.classification = classification;
        this.status = status;
        this.createdDate = createdDate != null ? createdDate : LocalDate.now(); // Default created date
        this.updatedDate = updatedDate != null ? updatedDate : LocalDate.now(); // Default updated date
        this.pdfPath = pdfPath;
    }

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

    public String getPdfPath() {
        return pdfPath;
    }

    // ID generation logic
    private String generateId() {
        return "DOC-" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", classification='" + classification + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", pdfPath='" + pdfPath + '\'' +
                '}';
    }
}