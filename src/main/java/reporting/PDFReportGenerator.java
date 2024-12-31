package reporting;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PDFReportGenerator extends ReportGenerator {
    @Override
    public boolean generate(List<LinkedHashMap<String, Object>> documents) {
        if (documents.isEmpty()) {
            throw new IllegalArgumentException("documents list is empty!");
        }
        File file = askForFileLocation(List.of("pdf"));
        if (file ==  null) {
            return false;
        }
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(file.getAbsolutePath());
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document PDFDocument = new Document(pdf);
        String[] columns = documents.get(0).keySet().toArray(new String[0]);

        // create table with the required number of columns
        Table table = new Table(new float[columns.length]);
        table.setWidthPercent(100); // Set table width to 100% of the page

        // Add table header (first row)
        for (String column: columns) {
            table.addHeaderCell(createStyledCell(column, true));
        }

        int i = 0;
        while (i < documents.size()) {
            Map<String, Object> document = documents.get(i);
            for (String column: columns) {
                String value = String.valueOf(document.get(column));
                table.addCell(createStyledCell(value, false));
            }
            i++;
        }

        // Add the table to the PDFDocument
        PDFDocument.add(table);
        PDFDocument.close();
        //System.out.println("PDF Report Generated: " + file.getAbsolutePath());
        return true;
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
