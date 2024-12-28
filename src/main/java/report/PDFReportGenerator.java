package report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PDFReportGenerator implements ReportGenerator{
    public static void main(String[] args) throws Exception {
        String fileName = "Transaction_Report.pdf";

        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Create a table with 4 columns
        Table table = new Table(new float[]{2, 3, 2, 3});
        table.setWidthPercent(100); // Set table width to 100% of the page

        // Add table header (first row)
        table.addHeaderCell(createStyledCell("Transaction ID", true));
        table.addHeaderCell(createStyledCell("Subject", true));
        table.addHeaderCell(createStyledCell("Status", true));
        table.addHeaderCell(createStyledCell("Date", true));

        try (Connection conn = DriverManager.getConnection(Dotenv.load().get("DATABASE_URL"));
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE created_date BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf("2023-12-01"));
            stmt.setDate(2, java.sql.Date.valueOf("2025-12-25"));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.addCell(createStyledCell(rs.getString("transaction_id"), false));
                table.addCell(createStyledCell(rs.getString("subject"), false));
                table.addCell(createStyledCell(rs.getString("status"), false));
                table.addCell(createStyledCell(rs.getString("created_date"), false));
            }
        }

        // Add the table to the document
        document.add(table);
        document.close();
        System.out.println("PDF Report Generated: " + fileName);
    }

    private static Cell createStyledCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(content);
        if (isHeader) {
            cell.setBold();
        }
        cell.setPadding(5);
        return cell;
    }
}
