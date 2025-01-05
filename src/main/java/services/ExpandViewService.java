package services;

import models.Document;

public class ExpandViewService {
    private static ExpandViewService instance;
    private Document document;

    public static ExpandViewService getInstance() {
        if (instance == null) {
            instance = new ExpandViewService();
        }

        return instance;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
