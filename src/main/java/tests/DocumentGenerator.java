package tests;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DocumentGenerator {

    public static void main(String[] args) {
        StringBuilder sqlStatement = new StringBuilder("INSERT INTO documents (id, uploaderId, title, description, department, classification, status, deadline, createdDate, updatedDate, filePath) VALUES ");

        Random rand = new Random();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] statuses = {"pending", "completed", "late"};

        for (int i = 0; i < 40; i++) {
            String id = "doc_" + (i + 1);  // Example id format
            int uploaderId = rand.nextInt(100) + 1; // Random uploaderId between 1 and 100
            String title = "Document " + (i + 1);
            String description = "Description of document " + (i + 1);
            String department = "Department " + (rand.nextInt(5) + 1);  // Random department between 1 and 5
            String classification = "Class " + (rand.nextInt(3) + 1); // Random classification
            String status = statuses[rand.nextInt(3)]; // Random status from pending, completed, late
            LocalDate deadline = LocalDate.now().plusDays(rand.nextInt(30)); // Random deadline within 30 days
            LocalDate createdDate = LocalDate.now().minusDays(rand.nextInt(60)); // Random created date within 60 days ago
            LocalDate updatedDate = createdDate.plusDays(rand.nextInt(30)); // Updated date after createdDate
            String filePath = "/path/to/file" + (i + 1);

            sqlStatement.append(String.format("('%s', %d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    id, uploaderId, title, description, department, classification, status,
                    deadline.format(dateFormatter), createdDate.format(dateFormatter),
                    updatedDate.format(dateFormatter), filePath));

            if (i < 39) {
                sqlStatement.append(", "); // Add a comma after each record except the last one
            }
        }

        // Output the final SQL statement
        System.out.println(sqlStatement.toString());
    }
}
