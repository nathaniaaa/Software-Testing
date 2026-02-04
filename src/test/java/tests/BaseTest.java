package tests;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.ArrayList; 
import java.util.List;

// --- IMPORT IMAGE PROCESSING ---
import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import org.openqa.selenium.Rectangle; 
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

// --- IMPORT EXCEL & REPORTING ---
import tests.utils.ExcelReportManager; 
import tests.utils.TestListener;       
import com.aventstack.extentreports.MediaEntityBuilder; 
import org.testng.Reporter; // Untuk mengambil deskripsi dari @Test
// ---------------------------------------------

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import tests.ActionHelper;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; 

    private static ThreadLocal<List<String>> screenshots = ThreadLocal.withInitial(ArrayList::new);

    // --- LOCATORS ---
    private final By AD_CLOSE_BUTTON_TEXT = AppiumBy.xpath("//android.widget.Button[@text='Nanti Saja']");
    private final By AD_CLOSE_BUTTON_ID = AppiumBy.id("com.telkomsel.telkomselcm:id/btSecondTypeFirstSecondary");
    private final By MALL_TAB_ID = AppiumBy.accessibilityId("Mall");
    private final By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");

    @BeforeClass
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("2ab55c03")
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.telkomsel.telkomselcm") 
                .setAppWaitActivity("*") 
                .setAppWaitDuration(Duration.ofMillis(30000)) 
                .setAutoGrantPermissions(true)
                .setNoReset(true) 
                .setDisableWindowAnimation(true)
                .setNewCommandTimeout(Duration.ofSeconds(60)); 

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new ActionHelper(driver);

        ensureOnAyoLariDashboard();
    }

    @BeforeMethod
    public void clearEvidence() {
        getScreenshotList().clear();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static List<String> getScreenshotList() {
        return screenshots.get();
    }

    // =======================================================
    // 🔥 UPDATED METHOD: HANDLES LIST OF SCREENSHOTS
    // =======================================================
    public void clickTest(By locator, String stepDetail) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

            // 1. Capture Screenshot
            String evidence = getScreenshotWithHighlight(element);

            // 2. Add to List (DO NOT LOG TO EXCEL YET)
            getScreenshotList().add(evidence);

            // 3. Log to HTML Report (Optional, for detailed steps)
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass(stepDetail, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(evidence).build());
            }

            element.click();
            System.out.println("[SUCCESS] " + stepDetail);

        } catch (Exception e) {
            String errorEvidence = getScreenshotBase64();
            getScreenshotList().add(errorEvidence); // Add error photo to list
            
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("Gagal: " + stepDetail,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(errorEvidence).build());
            }
            throw e; 
        }
    }

    // =======================================================
    // IMAGE PROCESSING (HIGHLIGHT BOX)
    // =======================================================
    public String getScreenshotWithHighlight(WebElement element) {
        try {
            Rectangle elementRect = element.getRect();
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(srcFile);

            Graphics2D g = image.createGraphics();
            
            double screenWidth = (double) driver.manage().window().getSize().getWidth();
            double imgWidth = (double) image.getWidth();
            double scaleFactor = imgWidth / screenWidth;

            int x = (int) (elementRect.getX() * scaleFactor);
            int y = (int) (elementRect.getY() * scaleFactor);
            int w = (int) (elementRect.getWidth() * scaleFactor);
            int h = (int) (elementRect.getHeight() * scaleFactor);

            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(8)); 
            g.drawRect(x, y, w, h);
            g.setColor(new Color(255, 0, 0, 40)); 
            g.fillRect(x, y, w, h);
            g.dispose();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            return java.util.Base64.getEncoder().encodeToString(bos.toByteArray());

        } catch (Exception e) {
            return getScreenshotBase64(); 
        }
    }

    public String getScreenshotBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    // =======================================================
    // NAVIGATION & UTILS
    // =======================================================
    public void ensureOnAyoLariDashboard() {
        System.out.println("--- PREPARING TEST ENV (SMART MODE) ---");
        try {
            if (isElementVisible(navBeranda)) {
                System.out.println("Tombol Beranda terlihat. Reset posisi...");
                driver.findElement(navBeranda).click();
                Thread.sleep(2000);
                return;
            }
            for (int i = 0; i < 3; i++) {
                driver.navigate().back();
                Thread.sleep(1500);
                if (isElementVisible(navBeranda)) {
                    driver.findElement(navBeranda).click();
                    Thread.sleep(2000);
                    return;
                }
            }
            forceRestartApp();
        } catch (Exception e) {
            forceRestartApp();
        }
    }

    public void forceRestartApp() {
        try {
            String appPackage = "com.telkomsel.telkomselcm";
            driver.terminateApp(appPackage);
            Thread.sleep(1000);
            driver.activateApp(appPackage);
            Thread.sleep(5000); 
            handlePotentialAds();
            if (!isElementVisible(navBeranda)) {
                masukKeMenuAyoLari();
            }
        } catch (Exception e) {}
    }

    public void masukKeMenuAyoLari() {
        try { 
            Thread.sleep(5000); 
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {}

        handlePotentialAds();

        try {
            Thread.sleep(3000);
            WebElement mallTab = wait.until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
            mallTab.click();
        } catch (Exception e) {
            actions.tapAtScreenRatio(0.5, 0.9);
        }
        
        try { Thread.sleep(3000); } catch (Exception e) {}
        boolean menuFound = false;
        for (int i = 0; i < 5; i++) {
            try {
                WebElement lariIcon = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Lari']"));
                lariIcon.click();
                menuFound = true;
                break; 
            } catch (Exception e) {
                actions.scrollVertical(); 
                try { Thread.sleep(2000); } catch (Exception ex) {} 
            }
        }
        if (!menuFound) { takeScreenshot("LariNotFound"); }

        try {
            Thread.sleep(2000);
            WebElement btnMulai = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Ayo Mulai Lari")));
            btnMulai.click();
        } catch (Exception e) {}

        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        // tapCenterScreen(); 
    }

    public void handlePotentialAds() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_TEXT)).click();
        } catch (Exception e1) {
            try {
                shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_ID)).click();
            } catch (Exception e2) {}
        }
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
        } catch (Exception e) { actions.tapAtScreenRatio(0.5, 0.15); }
    }

    public boolean isElementVisible(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) { return false; }
    }

    public void tapCenterScreen() {
        Dimension size = driver.manage().window().getSize();
        actions.tapByCoordinates(size.width / 2, size.height / 2);
    }

    public void takeScreenshot(String fileName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destFile = new File("screenshots/" + fileName + "_" + timestamp + ".png");
            FileUtils.copyFile(srcFile, destFile);
        } catch (Exception e) {}
    }
}