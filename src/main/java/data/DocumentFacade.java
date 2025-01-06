package data;

import models.Document;
import services.FieldsServices;
import services.FilterService;
import utils.ErrorHandler;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentFacade {

    private static DocumentFacade instance;
    private DBFacade dbFacade;
    private FilterService filterService = FilterService.getInstance();

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
            statement.setString(7, document.getUploaderId());
            statement.setDate(8, Date.valueOf(document.getCreatedDate()));
            statement.setDate(9, document.getUpdatedDate() != null ? Date.valueOf(document.getUpdatedDate()) : null);
            statement.setDate(10, document.getDeadline() != null ? Date.valueOf(document.getDeadline()) : null);
            statement.setString(11, document.getFilePath() != null ? document.getFilePath() : null);
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
            statement.setDate(6, document.getUpdatedDate() != null ? Date.valueOf(document.getUpdatedDate()) : null);
            statement.setDate(7, document.getDeadline() != null ? Date.valueOf(document.getDeadline()) : null);
            statement.setString(8, document.getFilePath() != null ? document.getFilePath() : null);
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

    public String buildQueryWithFilters(List<Object> parameters, String keyword, int offset, int limit) {
        // Start the base query
        StringBuilder query = new StringBuilder(
                "SELECT * FROM documents WHERE (id LIKE ? OR title LIKE ? OR description LIKE ?)"
        );

        // Add keyword-related parameters
        parameters.add("%" + keyword + "%"); // for partial matching in ID
        parameters.add("%" + keyword + "%"); // for partial matching in title
        parameters.add("%" + keyword + "%"); // for partial matching in description

        // Dynamically append filters based on non-null filter values
        if (filterService.getDepartment() != null) {
            query.append(" AND department = ?");
            parameters.add(filterService.getDepartment());
        }
        if (filterService.getClassification() != null) {
            query.append(" AND classification = ?");
            parameters.add(filterService.getClassification());
        }
        if (filterService.getStartDate() != null) {
            query.append(" AND createdDate >= ?");
            parameters.add(filterService.getStartDate());
        }
        if (filterService.getEndDate() != null) {
            query.append(" AND createdDate <= ?");
            parameters.add(filterService.getEndDate());
        }
        if (filterService.getStatus() != null) {
            query.append(" AND status = ?");
            parameters.add(filterService.getStatus());
        }

        // Add sorting and pagination (SQL Server syntax)
        query.append(" ORDER BY id OFFSET ? ROWS");
        parameters.add(offset);

        if (limit > 0) {
            query.append(" FETCH NEXT ? ROWS ONLY");
            parameters.add(limit);
        }

        return query.toString();
    }

    // Method to search documents with lazy loading (pagination)
    public List<Document> searchDocuments(String keyword, int offset, int limit) {
        List<Object> parameters = new ArrayList<>();
        String query = buildQueryWithFilters(parameters, keyword, offset, limit);
        List<Document> documents = new ArrayList<>();

        try (Connection connection = dbFacade.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters dynamically
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                documents.add(mapResultSetToDocument(resultSet));
            }
        } catch (SQLException e) {
            ErrorHandler.handle(e, "An error occurred while searching documents.");
        }

        return documents;
    }

    // Helper method to map a ResultSet to a Document object
    private Document mapResultSetToDocument(ResultSet resultSet) throws SQLException {
        return new Document.Builder(
                resultSet.getString("status"),
                resultSet.getString("uploaderId"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("department"),
                resultSet.getString("classification"))
                .id(resultSet.getString("id"))
                .deadline(resultSet.getDate("deadline") != null ? resultSet.getDate("deadline").toLocalDate() : null)
                .createdDate(resultSet.getDate("createdDate").toLocalDate())
                .updatedDate(resultSet.getDate("updatedDate") != null ? resultSet.getDate("updatedDate").toLocalDate() : null)
                .filePath(resultSet.getString("filePath") != null ? resultSet.getString("filePath") : null)
                .build();
    }
}
