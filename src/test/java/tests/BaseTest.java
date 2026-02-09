package tests;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.ArrayList; 
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import tests.utils.TestListener;       
import com.aventstack.extentreports.MediaEntityBuilder; 

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
import tests.helper.CaptureHelper;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; 
    protected CaptureHelper capture;

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
                .setUdid("R9CW1010R2P")
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

        capture = new CaptureHelper(driver);
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
            String evidence = capture.getScreenshotWithHighlight(element);

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
            String errorEvidence = capture.getScreenshotBase64();
            getScreenshotList().add(errorEvidence); // Add error photo to list
            
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("Gagal: " + stepDetail,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(errorEvidence).build());
            }
            throw e; 
        }
    }

    public void logInfo(String message) {
        try {
            // 1. Take Screenshot
            String screenshot = capture.getScreenshotBase64();

            // 2. Add to Excel List
            if (getScreenshotList() != null) {
                getScreenshotList().add(screenshot);
            }

            // 3. Add to HTML Report (Extent)
            if (TestListener.getTest() != null) {
                TestListener.getTest().info(message, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
            }
        } catch (Exception e) {
            System.out.println("Log Info Failed: " + e.getMessage());
        }
    }

    public void logPass(String message) {
        try {
            // 1. Take Screenshot
            String screenshot = capture.getScreenshotBase64();

            // 2. Add to Excel List
            if (getScreenshotList() != null) {
                getScreenshotList().add(screenshot);
            }

            // 3. Add to HTML Report (Extent)
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass(message, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
            }
        } catch (Exception e) {
            System.out.println("Log Pass Failed: " + e.getMessage());
        }
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

    public String getScreenshotBase64() {
        return capture.getScreenshotBase64();
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

    public CaptureHelper getCapture() {
        return capture;
    }
}