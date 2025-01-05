package services;

import javafx.collections.ObservableList;
import models.Document;

public class SearchService {
    private static SearchService instance;
    private ObservableList<Document> documents;

    public static SearchService getInstance() {
        if (instance == null) {
            instance = new SearchService();
        }

        return instance;
    }

    public ObservableList<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(ObservableList<Document> documents) {
        this.documents = documents;
    }
}
