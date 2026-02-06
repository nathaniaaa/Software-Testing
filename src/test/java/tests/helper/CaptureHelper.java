package tests.helper;

import io.appium.java_client.android.AndroidDriver;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Duration;

import com.aventstack.extentreports.MediaEntityBuilder; 
import tests.BaseTest;        

public class CaptureHelper {
    protected  AndroidDriver driver; 
    protected  WebDriverWait wait; 

    public CaptureHelper(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getScreenshotBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Helper to draw a RED BOX around the element on a screenshot.
     */
    public String getScreenshotWithHighlight(WebElement element) {
        try {
            Rectangle elementRect = element.getRect();
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(srcFile);

            Graphics2D g = image.createGraphics();
            
            // Calculate scale in case of high-DPI screens
            double screenWidth = (double) driver.manage().window().getSize().getWidth();
            double imgWidth = (double) image.getWidth();
            double scaleFactor = imgWidth / screenWidth;

            int x = (int) (elementRect.getX() * scaleFactor);
            int y = (int) (elementRect.getY() * scaleFactor);
            int w = (int) (elementRect.getWidth() * scaleFactor);
            int h = (int) (elementRect.getHeight() * scaleFactor);

            // Draw Red Border
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(8)); 
            g.drawRect(x, y, w, h);
            
            // Draw Semi-transparent Red Fill
            g.setColor(new Color(255, 0, 0, 40)); 
            g.fillRect(x, y, w, h);
            
            g.dispose();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            return java.util.Base64.getEncoder().encodeToString(bos.toByteArray());

        } catch (Exception e) {
            // Fallback to normal screenshot if image processing fails
            return getScreenshotBase64();
        }
    }

    public String getScreenshotWithCoordinateHighlight(int targetX, int targetY) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(srcFile);

            Graphics2D g = image.createGraphics();

            // Calculate scale (for high-DPI devices)
            double screenWidth = (double) driver.manage().window().getSize().getWidth();
            double imgWidth = (double) image.getWidth();
            double scaleFactor = imgWidth / screenWidth;

            // Scale the target coordinates
            int x = (int) (targetX * scaleFactor);
            int y = (int) (targetY * scaleFactor);
            int radius = 30; // Size of the marker

            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(5));

            // Draw Circle
            g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

            // Draw Crosshair (Optional: makes it look cooler)
            g.drawLine(x - radius - 10, y, x + radius + 10, y); // Horizontal
            g.drawLine(x, y - radius - 10, x, y + radius + 10); // Vertical
            
            // Draw semi-transparent fill
            g.setColor(new Color(255, 0, 0, 50));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            g.dispose();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            return java.util.Base64.getEncoder().encodeToString(bos.toByteArray());

        } catch (Exception e) {
            return getScreenshotBase64();
        }
    }

    // Mark kotak highlight (no klik)
    public void highlightAndCapture(By locator, String stepDetail) {
        try {
            // 1. Cari elemennya (tunggu sampai terlihat)
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // 2. Ambil screenshot dengan kotak merah (menggunakan fungsi yang sudah kamu buat)
            String evidence = getScreenshotWithHighlight(element);

            // 3. Masukkan ke list evidence (agar otomatis ditarik oleh TestListener ke Excel)
            if (BaseTest.getScreenshotList() != null) {
                BaseTest.getScreenshotList().add(evidence);
            }

            // 4. Log ke HTML Report (Extent Report)
            if (TestListener.getTest() != null) {
                TestListener.getTest().info("Highlight: " + stepDetail, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(evidence).build());
            }
            
            System.out.println("[HIGHLIGHT] " + stepDetail);

        } catch (Exception e) {
            System.err.println("Gagal highlight elemen: " + e.getMessage());
            // Jika gagal highlight, ambil screenshot biasa sebagai backup
            if (BaseTest.getScreenshotList() != null) {
                BaseTest.getScreenshotList().add(getScreenshotBase64());
            }
        }
    }

}
