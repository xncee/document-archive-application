package reports;

import java.time.LocalDate;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        List<LinkedHashMap<String, Object>> documents = new ArrayList<>();
        documents.add(createDocument("DOC-1234", "Software Engineering", "public", LocalDate.now()));
        documents.add(createDocument("DOC-4545", "Computer Science", "public", LocalDate.now().minusMonths(7)));
        documents.add(createDocument("DOC-4533", "Artificial Intelligence", "private", LocalDate.now().minusMonths(3)));
        documents.add(createDocument("DOC-5678", "Data Structures", "private", LocalDate.now().minusYears(1)));
        documents.add(createDocument("DOC-7890", "Machine Learning", "public", LocalDate.now().minusMonths(5)));
        documents.add(createDocument("DOC-2468", "Database Management", "restricted", LocalDate.now().minusWeeks(2)));
        documents.add(createDocument("DOC-1357", "Cloud Computing", "public", LocalDate.now().minusDays(10)));
        documents.add(createDocument("DOC-9999", "Cyber Security", "private", LocalDate.now().minusYears(2)));
        documents.add(createDocument("DOC-3333", "Web Development", "public", LocalDate.now().minusMonths(6)));
        documents.add(createDocument("DOC-7777", "Big Data Analytics", "restricted", LocalDate.now().minusYears(1).plusMonths(3)));

        // Assuming ExcelReportGenerator is defined elsewhere
        ReportGenerator generator = new PDFReportGenerator();
        generator.generate(documents);
    }

    private static LinkedHashMap<String, Object> createDocument(String documentId, String title, String classification, LocalDate createdDate) {
        LinkedHashMap<String, Object> document = new LinkedHashMap<>();
        document.put("documentId", documentId);
        document.put("title", title);
        document.put("Classification", classification);
        document.put("created date", createdDate);
        return document;
    }
}
