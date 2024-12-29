package model.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelReportGenerator extends ReportGenerator {
    @Override
    public boolean generate(List<Map<String, Object>> documents) {
        if (documents.isEmpty()) {
            throw new IllegalArgumentException("documents list is empty!");
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Documents Report - "+ LocalDate.now());

        CellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);

        // Create a bold font for the header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.cloneStyleFrom(borderedStyle);
        headerStyle.setFont(headerFont);

        // Get document columns
        String[] columns = documents.get(0).keySet().toArray(new String[0]);
        // Create header row with borders
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle); //
        }

        // inserting each document in the table
        int currentRow = 1; // start at row 1 as row 0 was used as header row.
        int docIndex = 0; // current
        while (docIndex < documents.size()) {
            Map<String, Object> document = documents.get(docIndex);
            Row row = sheet.createRow(currentRow);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = row.createCell(col);
                cell.setCellStyle(borderedStyle);
                String value = String.valueOf(document.get(columns[col]));
                cell.setCellValue(value);
            }

            currentRow++;
            docIndex++;
        }

        // Auto-size columns for better readability
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = askForFileLocation(List.of("xlsx", "xls"));
        if (file == null) {
            return false;
        }
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            workbook.close();
            //System.out.println("Excel Report Generated: "+file.getAbsolutePath());
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
