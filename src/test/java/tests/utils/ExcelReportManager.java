package tests.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.codec.binary.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelReportManager {

    private static Workbook workbook;
    private static Sheet sheet;
    private static int rowIndex = 0;
    private static String filePath;

    // --- SETUP AWAL: Bikin File & Header ---
    public static void setupExcel() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("QA Automation Report");

        // 1. Bikin Style Header (Warna Cyan/Aqua, Bold, Border)
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        setBorders(headerStyle);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 2. Bikin Baris Header
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.setHeight((short) 600); // Agak tinggi dikit headernya

        String[] headers = {"No", "Test Case Name", "Expected Result", "Actual Result", "Screenshot", "Note", "Status"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // --- FUNGSI TULIS DATA PER BARIS ---
    public static void logToExcel(String testName, String expected, String actual, String base64Image, String note, String status) {
        if (workbook == null) setupExcel();

        Row row = sheet.createRow(rowIndex++);
        // Tinggi baris diset cukup besar (3000 unit) agar gambar muat
        row.setHeight((short) 3000); 

        // Style untuk sel biasa (Wrap text, Border, Tengah)
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(cellStyle);

        // Style khusus Status (Merah/Hijau)
        CellStyle statusStyle = workbook.createCellStyle();
        statusStyle.cloneStyleFrom(cellStyle);
        Font statusFont = workbook.createFont();
        statusFont.setBold(true);
        if (status.equalsIgnoreCase("FAIL")) {
            statusFont.setColor(IndexedColors.RED.getIndex());
        } else {
            statusFont.setColor(IndexedColors.GREEN.getIndex());
        }
        statusStyle.setFont(statusFont);
        statusStyle.setAlignment(HorizontalAlignment.CENTER);

        // Isi Data ke Kolom
        createCell(row, 0, String.valueOf(rowIndex - 1), cellStyle); // No
        createCell(row, 1, testName, cellStyle);                     // Test Case
        createCell(row, 2, expected, cellStyle);                     // Expected
        createCell(row, 3, actual, cellStyle);                       // Actual
        
        // Kolom 4: Screenshot (Logic khusus)
        Cell imgCell = row.createCell(4);
        imgCell.setCellStyle(cellStyle);
        if (base64Image != null && !base64Image.isEmpty()) {
            insertImageToCell(base64Image, row.getRowNum(), 4);
        } else {
            imgCell.setCellValue("No Image");
        }

        createCell(row, 5, note, cellStyle);                         // Note
        createCell(row, 6, status, statusStyle);                     // Status
    }

    // --- HELPER: Bikin Sel Cepat ---
    private static void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "-");
        cell.setCellStyle(style);
    }

    // --- HELPER: Border Kotak ---
    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    // --- LOGIC TEMPEL GAMBAR ---
    private static void insertImageToCell(String base64Image, int rowNum, int colNum) {
        try {
            byte[] imageBytes = Base64.decodeBase64(base64Image);
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            
            // Koordinat Gambar (Pas di tengah sel)
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            anchor.setCol2(colNum + 1);
            anchor.setRow2(rowNum + 1);
            // Padding (Jarak tepi gambar ke garis)
            anchor.setDx1(50); anchor.setDy1(50); 
            anchor.setDx2(-50); anchor.setDy2(-50);

            drawing.createPicture(anchor, pictureIdx);
        } catch (Exception e) {
            System.out.println("Gagal insert gambar: " + e.getMessage());
        }
    }

    // --- SIMPAN FILE ---
    public static void saveExcel() {
        try {
            // Atur Lebar Kolom (Satuan 1/256th character width)
            sheet.setColumnWidth(0, 1500);  // No
            sheet.setColumnWidth(1, 8000);  // Test Case
            sheet.setColumnWidth(2, 9000);  // Expected
            sheet.setColumnWidth(3, 9000);  // Actual
            sheet.setColumnWidth(4, 8000);  // Screenshot
            sheet.setColumnWidth(5, 5000);  // Note
            sheet.setColumnWidth(6, 3000);  // Status

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            filePath = System.getProperty("user.dir") + "/test-output/Manual_QA_Format_" + timestamp + ".xlsx";
            
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            
            System.out.println("\n[EXCEL] Laporan berhasil dibuat: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}