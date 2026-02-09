package tests.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ExcelReportManager {

    private static Workbook workbook;
    
    // --- STATE MANAGEMENT PER SHEET ---
    private static Map<String, Sheet> sheets = new HashMap<>();
    private static Map<String, Integer> rowIndices = new HashMap<>();
    
    // 🔥 NEW: Track the last group name separately for EACH sheet
    private static Map<String, String> lastGroupNames = new HashMap<>();

    // Styling
    private static CellStyle headerStyle, groupHeaderStyle, passStyle, failStyle, textStyle;

    // --- SETUP ---
    public static synchronized void setupExcel() {
        if (workbook != null) return;
        workbook = new XSSFWorkbook();
        createStyles();
    }

    // --- SHEET MANAGEMENT ---
    private static synchronized Sheet getOrCreateSheet(String sheetName) {
        if (workbook == null) setupExcel();

        if (!sheets.containsKey(sheetName)) {
            Sheet sheet = workbook.createSheet(sheetName);
            sheets.put(sheetName, sheet);
            rowIndices.put(sheetName, 0); 
            lastGroupNames.put(sheetName, ""); // Initialize group tracker
            
            createHeader(sheet);
        }
        return sheets.get(sheetName);
    }

    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(rowIndices.get(sheet.getSheetName()));
        headerRow.setHeightInPoints(40);

        String[] headers = {"No", "Test Case", "Expected", "Actual", "Note", "Status", "Screenshots"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        sheet.setColumnWidth(0, 1500); 
        sheet.setColumnWidth(1, 6000); 
        sheet.setColumnWidth(2, 6000); 
        sheet.setColumnWidth(3, 6000); 
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 3000);

        int currentIdx = rowIndices.get(sheet.getSheetName());
        rowIndices.put(sheet.getSheetName(), currentIdx + 1);
    }

    // --- LOGGING ---
    public static synchronized void logToExcel(String sheetName, String group, String testName, String expected, String actual, String note, List<String> screenshots, String status) {
        Sheet sheet = getOrCreateSheet(sheetName);
        int rowIndex = rowIndices.get(sheetName);

        // 🔥 CHECK GROUP CHANGE (Per Sheet)
        String lastGroup = lastGroupNames.get(sheetName);
        if (group != null && !group.isEmpty() && !group.equalsIgnoreCase(lastGroup)) {
            
            // 1. Create Group Header Row
            Row groupRow = sheet.createRow(rowIndex++);
            groupRow.setHeightInPoints(25);

            Cell groupCell = groupRow.createCell(0);
            groupCell.setCellValue(group.toUpperCase());
            groupCell.setCellStyle(groupHeaderStyle);

            // 2. Merge Cells (A to G)
            sheet.addMergedRegion(new CellRangeAddress(
                groupRow.getRowNum(), groupRow.getRowNum(), 0, 6
            ));

            // 3. Update trackers
            lastGroupNames.put(sheetName, group);
            rowIndices.put(sheetName, rowIndex); // Update index because we added a row
        }
        
        // Re-fetch index in case it changed due to grouping
        rowIndex = rowIndices.get(sheetName);

        // --- CREATE TEST ROW ---
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(220f); 

        CellStyle statusStyle = status.equalsIgnoreCase("PASS") ? passStyle : failStyle;

        createCell(row, 0, String.valueOf(rowIndex), textStyle); // Fixed: Removed "-1" to keep logic simple
        createCell(row, 1, testName, textStyle);
        createCell(row, 2, expected, textStyle);
        createCell(row, 3, actual, textStyle);
        createCell(row, 4, note, textStyle);
        createCell(row, 5, status, statusStyle);

        if (screenshots != null) {
            for (int i = 0; i < screenshots.size(); i++) {
                insertImageToCell(sheet, screenshots.get(i), rowIndex, 6 + i);
            }
        }

        rowIndices.put(sheetName, rowIndex + 1);
    }

    // --- IMAGE HANDLING ---
    private static void insertImageToCell(Sheet sheet, String base64Image, int rowNum, int colNum) {
        try {
            if (base64Image == null || base64Image.isEmpty()) return;

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage = ImageIO.read(bis);
            if (bImage == null) return;
            bis.close();

            double rowHeightPx = 220f * 1.3333;
            double imgHeight = bImage.getHeight();
            double scale = rowHeightPx / imgHeight;

            double newImgWidthPx = bImage.getWidth() * scale;
            int columnWidthUnits = (int) (newImgWidthPx * 36.56) + 200;
            if (sheet.getColumnWidth(colNum) < columnWidthUnits) {
                sheet.setColumnWidth(colNum, columnWidthUnits);
            }

            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(1.0); 
            pict.resize(scale);

        } catch (Exception e) {
            System.err.println("Image Error: " + e.getMessage());
        }
    }

    // --- SAVING ---
    public static synchronized void saveExcel() {
        if (workbook == null) return;

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String folderPath = System.getProperty("user.dir") + "/test-output/";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String filePath = folderPath + "Report_" + timestamp + ".xlsx";

            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            
            // Reset state
            workbook = null;
            sheets.clear();
            rowIndices.clear();
            lastGroupNames.clear(); // Clear groups too!
            
            System.out.println("Report Saved: " + filePath);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- STYLES ---
    private static void createStyles() {
        headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(headerStyle);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // 🔥 RESTORED GROUP HEADER STYLE
        groupHeaderStyle = workbook.createCellStyle();
        groupHeaderStyle.cloneStyleFrom(headerStyle);
        groupHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        groupHeaderStyle.setAlignment(HorizontalAlignment.LEFT);

        textStyle = workbook.createCellStyle();
        textStyle.setWrapText(true);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(textStyle);

        passStyle = workbook.createCellStyle();
        passStyle.cloneStyleFrom(textStyle);
        passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        failStyle = workbook.createCellStyle();
        failStyle.cloneStyleFrom(textStyle);
        failStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private static void createCell(Row row, int col, String val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val != null ? val : "-");
        c.setCellStyle(style);
    }
}