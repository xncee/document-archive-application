package controllers;

import javafx.collections.ObservableList;
import models.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SearchService{
    private static SearchService instance;
    private ObservableList<Document> documents;

    private static int offset;

    private SearchService() {
        instance = this;
    }

    public static SearchService getInstance() {
        if (instance == null) {
            instance = new SearchService();
        }
        return instance;
    }

    private LocalDate convertToLocalDate(Object date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return null; // Return null if date is null or not an instance of java.sql.Date
    }

    public void addDocuments(List<Map<String, Object>> results) {
        for (Map<String, Object> res : results) {
            Document.Builder docBuilder = new Document.Builder(
                    (String) res.get("status"),
                    (Integer) res.get("uploaderId"),
                    (String) res.get("title"),
                    (String) res.get("description"),
                    (String) res.get("department"),
                    (String) res.get("classification"))
                    .id((String) res.get("id"))
                    .deadline(convertToLocalDate(res.get("deadline")))
                    .createdDate(convertToLocalDate(res.get("createdDate")))
                    .updatedDate(convertToLocalDate(res.get("updatedDate")));

            docBuilder.filePath((String) res.get("filePath"));
            Document doc = null;
            try {
                doc = docBuilder.build();
                documents.add(doc);
            } catch (Exception e) {
                System.out.println("Failed to load a document: " + doc);
                System.out.println(e.getMessage());
            }
        }

        // observable list
        // this class will be used to rettrive current filtered documents (generate report, tbale view, ...)

        // load documents in chunks, e.g. 20-30 documents per chunk
        // load more documents when reaching the end of the list

//    public static void search() {
//
//    }
    }
}
