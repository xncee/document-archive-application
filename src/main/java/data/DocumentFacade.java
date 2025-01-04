package data;

import models.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentFacade {

    private static DocumentFacade instance;
    private DBFacade dbFacade;

    // Private constructor to prevent instantiation
    private DocumentFacade() {
        this.dbFacade = DBFacade.getInstance(); // Get the singleton instance of DBFacade
    }

    // Method to get the singleton instance of DocumentFacade
    public static synchronized DocumentFacade getInstance() {
        if (instance == null) {
            instance = new DocumentFacade();
        }
        return instance;
    }

    // Method to create a new document
    public boolean addDocument(Document document) throws SQLException {
        String query = "INSERT INTO documents (id, title, description, department, classification, status, uploaderId, createdDate, updatedDate, deadline, filePath) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, document.getId());
            statement.setString(2, document.getTitle());
            statement.setString(3, document.getDescription());
            statement.setString(4, document.getDepartment());
            statement.setString(5, document.getClassification());
            statement.setString(6, document.getStatus());
            statement.setInt(7, document.getUploaderId());
            statement.setDate(8, Date.valueOf(document.getCreatedDate()));
            statement.setDate(9, Date.valueOf(document.getUpdatedDate()));
            statement.setDate(10, document.getDeadline() != null ? Date.valueOf(document.getDeadline()) : null);
            statement.setString(11, document.getFilePath());
            return statement.executeUpdate() > 0;
        }
    }

    // Method to retrieve a document by ID
    public Document getDocumentById(String documentId) throws SQLException {
        String query = "SELECT * FROM documents WHERE id = ?";
        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, documentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToDocument(resultSet);
            }
        }
        return null;
    }

    // Method to update a document
    public boolean updateDocument(Document document) throws SQLException {
        String query = "UPDATE documents SET title = ?, description = ?, department = ?, classification = ?, status = ?, updatedDate = ?, deadline = ?, filePath = ? WHERE id = ?";
        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, document.getTitle());
            statement.setString(2, document.getDescription());
            statement.setString(3, document.getDepartment());
            statement.setString(4, document.getClassification());
            statement.setString(5, document.getStatus());
            statement.setDate(6, Date.valueOf(document.getUpdatedDate()));
            statement.setDate(7, document.getDeadline() != null ? Date.valueOf(document.getDeadline()) : null);
            statement.setString(8, document.getFilePath());
            statement.setString(9, document.getId());
            return statement.executeUpdate() > 0;
        }
    }

    // Method to delete a document
    public boolean deleteDocument(String documentId) throws SQLException {
        String query = "DELETE FROM documents WHERE id = ?";
        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, documentId);
            return statement.executeUpdate() > 0;
        }
    }

    // Method to search documents with lazy loading (pagination)
    public List<Document> searchDocuments(String keyword, int offset, int limit) throws SQLException {
        String query = "SELECT * FROM documents WHERE title LIKE ? OR description LIKE ? ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Document> documents = new ArrayList<>();

        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            statement.setInt(3, offset);
            statement.setInt(4, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                documents.add(mapResultSetToDocument(resultSet));
            }
        }
        return documents;
    }

    // Helper method to map a ResultSet to a Document object
    private Document mapResultSetToDocument(ResultSet resultSet) throws SQLException {
        return new Document.Builder(
                resultSet.getString("status"),
                resultSet.getInt("uploaderId"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("department"),
                resultSet.getString("classification"))
                .id(resultSet.getString("id"))
                .deadline(resultSet.getDate("deadline") != null ? resultSet.getDate("deadline").toLocalDate() : null)
                .createdDate(resultSet.getDate("createdDate").toLocalDate())
                .updatedDate(resultSet.getDate("updatedDate").toLocalDate())
                .filePath(resultSet.getString("filePath"))
                .build();
    }
}
