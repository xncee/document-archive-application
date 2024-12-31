package models;

import java.time.LocalDate;

public class Update {
    private final String userId;
    private final String documentId;
    private String title;
    private String text;
    private final LocalDate createdDate;

    // Constructor for required fields with created_date set to current date
    public Update(String userId, String documentId, String title, String text) {
        if (userId == null || documentId == null || title == null || text == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.userId = userId;
        this.documentId = documentId;
        this.title = title;
        this.text = text;
        this.createdDate = LocalDate.now(); // Default to the current date
    }

    // Constructor with explicit createdDate
    public Update(String userId, String documentId, String title, String text, LocalDate createdDate) {
        if (userId == null || documentId == null || title == null || text == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        this.userId = userId;
        this.documentId = documentId;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate != null ? createdDate : LocalDate.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // Setters for updatable fields
    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Update{" +
                "userId='" + userId + '\'' +
                ", documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}