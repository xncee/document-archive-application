package model;

import java.time.LocalDateTime;

public class Transaction {
    // Private data fields
    private final int transactionId;
    private final String subject;
    private final String description;
    private final String department;
    private final String classification;
    private final String status;
    private final LocalDateTime createdDate;
    private final LocalDateTime updatedDate;
    private final String pdfPath;

    // Private constructor to prevent direct instantiation
    private Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.subject = builder.subject;
        this.description = builder.description;
        this.department = builder.department;
        this.classification = builder.classification;
        this.status = builder.status;
        this.createdDate = builder.createdDate;
        this.updatedDate = builder.updatedDate;
        this.pdfPath = builder.pdfPath;
    }

    // Getters (no setters needed because of immutability)
    public int getTransactionId() {
        return transactionId;
    }

    public String getSubject() {
        return subject;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", classification='" + classification + '\'' +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", pdfPath='" + pdfPath + '\'' +
                '}';
    }

    public static class Builder {
        private final int transactionId;
        private final String subject;
        private final String description;
        private String department;
        private String classification;
        private String status;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private String pdfPath;

        // Builder constructor for required fields
        public Builder(int transactionId, String subject, String description) {
            this.transactionId = transactionId;
            this.subject = subject;
            this.description = description;
        }

        // Setters for optional fields
        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder classification(String classification) {
            this.classification = classification;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }

        public Builder pdfPath(String pdfPath) {
            this.pdfPath = pdfPath;
            return this;
        }

        // Build method to return the final Transaction object
        public Transaction build() {
            return new Transaction(this);
        }
    }
}