package tests.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;      
import java.awt.image.BufferedImage;

public class ExcelReportManager {

    private static Workbook workbook;
    private static Sheet sheet;
    private static int rowIndex = 0;
    
    // --- CONFIGURATION ---
    // 220 points height (Approx 293 pixels)
    private static final float ROW_HEIGHT_POINTS = 220f; 

    public static void setupExcel() {
        if (workbook != null) return;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("QA Report");

        // Header Styling
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(headerStyle);
        
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.setHeightInPoints(40); 

        // Header Columns
        String[] headers = {"No", "Test Case Name", "Expected Result", "Actual Result", "Status", "Screenshots ->"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    public static void logToExcel(String testName, String expected, String actual, List<String> screenshots, String status) {
        if (workbook == null) setupExcel();

        Row row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(ROW_HEIGHT_POINTS); // Force Row Height

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        
        CellStyle statusStyle = workbook.createCellStyle();
        statusStyle.cloneStyleFrom(style);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(status.equalsIgnoreCase("PASS") ? IndexedColors.GREEN.getIndex() : IndexedColors.RED.getIndex());
        statusStyle.setFont(font);
        statusStyle.setAlignment(HorizontalAlignment.CENTER);

        // --- FILL TEXT COLUMNS ---
        createCell(row, 0, String.valueOf(rowIndex - 1), style);
        createCell(row, 1, testName, style);
        createCell(row, 2, expected, style);
        createCell(row, 3, actual, style);
        createCell(row, 4, status, statusStyle); 

        // --- INSERT IMAGES (STARTING AT COLUMN 5) ---
        if (screenshots != null) {
            for (int i = 0; i < screenshots.size(); i++) {
                // Insert at Col 5, 6, 7...
                insertImageToCell(screenshots.get(i), row.getRowNum(), 5 + i);
            }
        }
    }

    private static void insertImageToCell(String base64Image, int rowNum, int colNum) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            
            // 1. GET TRUE IMAGE SIZE (Java Native)
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bImage = ImageIO.read(bis);
            if (bImage == null) return;
            bis.close(); 

            double imgWidth = bImage.getWidth();
            double imgHeight = bImage.getHeight();

            // 2. CALCULATE EXACT SCALE
            // Convert Row Height Points to Pixels (Points * 1.3333)
            double rowHeightPx = ROW_HEIGHT_POINTS * 1.3333;
            double scale = rowHeightPx / imgHeight;

            // 3. PRE-CALCULATE COLUMN WIDTH & SET IT FIRST
            // This is the FIX. We prepare the column size BEFORE adding the image.
            double newImgWidthPx = imgWidth * scale;
            
            // Excel Width Unit = 1/256th char width. (Approx 36.56 units per pixel)
            // We add padding (+ 200) so the image has a small border
            int columnWidthUnits = (int) (newImgWidthPx * 36.56) + 200; 
            
            // Expand the column if needed
            if (sheet.getColumnWidth(colNum) < columnWidthUnits) {
                sheet.setColumnWidth(colNum, columnWidthUnits); 
            }

            // 4. ADD PICTURE
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            
            // Anchor Top-Left
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            
            // Critical: Don't resize automatically when we manipulate columns later
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);

            Picture pict = drawing.createPicture(anchor, pictureIdx);
            
            // 5. RESIZE IMAGE
            // Now that the column is the right size, resize() will work accurately.
            // Reset to 1.0 first to clear any default scaling assumptions
            pict.resize(1.0); 
            pict.resize(scale);

        } catch (Exception e) {
            System.err.println("Img Error: " + e.getMessage());
        }
    }

    private static void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "-");
        cell.setCellStyle(style);
    }
    
    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    public static void saveExcel() {
        try {
            // Set widths for text columns
            sheet.setColumnWidth(0, 1500); // No
            sheet.setColumnWidth(1, 6000); // Test Case
            sheet.setColumnWidth(2, 6000); // Expected
            sheet.setColumnWidth(3, 6000); // Actual
            sheet.setColumnWidth(4, 3000); // Status
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = System.getProperty("user.dir") + "/test-output/Final_Report_" + timestamp + ".xlsx";
            
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Report Saved: " + filePath);
        } catch (IOException e) { e.printStackTrace(); }
    }
}