package services;

import data.DBFacade;
import models.Document;
import java.time.LocalDate;
import java.util.List;

public class DocumentServices {
    private static final DBFacade dbFacade = DBFacade.getInstance();
    public static boolean saveDocument(Document document) {
        dbFacade.addDocument(document);
        return true;
    }

    public static void updateDocument(Document document) {

    }

    public static void deleteDocument(Document document) {

    }

    public static List<Document> searchDocuments(String query) {
        return null;
    }

    public static int getTotalDocuments() {
        return 0;
    }

    public long getRecentUploads() {
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//        return documents.stream()
//                .filter(doc -> doc.getCreatedDate().isAfter(oneWeekAgo))
//                .count();
        return 0;
    }

    public long getPendingReviews() {
//        return documents.stream()
//                .filter(doc -> "Under Review".equals(doc.getStatus()))
//                .count();
        return 0;
    }
}