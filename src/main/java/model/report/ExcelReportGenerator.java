package model.report;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExcelReportGenerator implements ReportGenerator {
    public static void main(String[] args) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        // Create a CellStyle for borders
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

        // Create header row with borders
        Row header = sheet.createRow(0);
        String[] columns = {"Department", "Transaction ID", "Subject", "Created Date", "Updated Date", "Status"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fetch data from the model.database
        try (Connection conn = DriverManager.getConnection(Dotenv.load().get("DATABASE_URL"));
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE created_date BETWEEN ? AND ?")) {

            stmt.setDate(1, java.sql.Date.valueOf("2023-12-01"));
            stmt.setDate(2, java.sql.Date.valueOf("2025-12-25"));
            ResultSet rs = stmt.executeQuery();

            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                Cell departmentCell = row.createCell(0);
                departmentCell.setCellValue(rs.getString("department"));
                departmentCell.setCellStyle(borderedStyle);

                Cell transactionIdCell = row.createCell(1);
                transactionIdCell.setCellValue(rs.getString("transaction_id"));
                transactionIdCell.setCellStyle(borderedStyle);

                Cell subjectCell = row.createCell(2);
                subjectCell.setCellValue(rs.getString("subject"));
                subjectCell.setCellStyle(borderedStyle);

                Cell createdDateCell = row.createCell(3);
                createdDateCell.setCellValue(rs.getString("created_date"));
                createdDateCell.setCellStyle(borderedStyle);

                Cell updatedDateCell = row.createCell(4);
                updatedDateCell.setCellValue(rs.getString("updated_date"));
                updatedDateCell.setCellStyle(borderedStyle);

                Cell statusCell = row.createCell(5);
                statusCell.setCellValue(rs.getString("status"));
                statusCell.setCellStyle(borderedStyle);


            }
        }

        // Auto-size columns for better readability
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream("Transaction_Report.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Excel Report Generated: Transaction_Report.xlsx");
    }
}
