package control.document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entities.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentService {
    private final List<Document> documents = new ArrayList<>();

    public DocumentService() {
        initializeSampleData();
    }

    private void initializeSampleData() {
//        documents.add(new Document("Annual Report 2024", "Finance", "Approved", "kjk", "/documents/annual-report-2024.pdf", null, null, null, null));
//        documents.add(new Document("Employee Handbook", "HR", "Under Review","class1" , "/documents/employee-handbook.pdf", null, null, null, null));
//        documents.add(new Document("Project Proposal", "Sales", "Draft", "class2", "/documents/project-proposal.pdf", null, null, null, null));
//        documents.add(new Document("Security Policy", "IT", "Approved", "class3", "/documents/security-policy.pdf", null, null, null, null));
//        documents.add(new Document("Marketing Plan", "Marketing", "Under Review", "class4", "/documents/marketing-plan.pdf", null, null, null, null));
    }

    public ObservableList<Document> getAllDocuments() {
        return FXCollections.observableArrayList(documents);
    }

    public void saveDocument(Document document) {
        documents.add(document);
    }

    public void updateDocument(Document document) {
        documents.removeIf(doc -> doc.getTitle().equals(document.getTitle()));
        documents.add(document);
    }

    public void deleteDocument(Document document) {
        documents.remove(document);
    }

    public List<Document> searchDocuments(String query) {
        return documents.stream()
                .filter(doc -> doc.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        doc.getDepartment().toLowerCase().contains(query.toLowerCase()) ||
                        doc.getStatus().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public int getTotalDocuments() {
        return documents.size();
    }

    public long getRecentUploads() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        return documents.stream()
                .filter(doc -> doc.getCreatedDate().isAfter(oneWeekAgo))
                .count();
    }

    public long getPendingReviews() {
        return documents.stream()
                .filter(doc -> "Under Review".equals(doc.getStatus()))
                .count();
    }
}