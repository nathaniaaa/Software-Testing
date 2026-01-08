package tests;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; 

    // LOCATORS
    // [FIX 1] Gunakan tipe 'By' dan XPath Text agar lebih akurat
    private final By AD_CLOSE_BUTTON_TEXT = AppiumBy.xpath("//android.widget.Button[@text='Nanti Saja']");
    private final By AD_CLOSE_BUTTON_ID = AppiumBy.id("com.telkomsel.telkomselcm:id/btSecondTypeFirstSecondary");
    private final By MALL_TAB_ID = AppiumBy.accessibilityId("Mall");

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("2ab55c03") 
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.telkomsel.telkomselcm") 
                // [FIX PENTING] Wildcard "*" artinya tunggu activity APAPUN. 
                // Ini solusi error "SplashActivityRevamp never started".
                .setAppWaitActivity("*") 
                .setAppWaitDuration(Duration.ofMillis(30000)) // Tunggu max 30 detik buat app buka
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new ActionHelper(driver);

        masukKeMenuAyoLari();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void masukKeMenuAyoLari() {
        System.out.println("Navigasi ke AyoLari via Mall...");
        
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {}

        // 1. HANDLE ADS (Upgrade Logic)
        handlePotentialAds();

        // 2. TAP "MALL" TAB
        System.out.println("Klik Menu Mall...");
        try {
            // Kita tunggu agak sabar buat Tab Mall
            WebElement mallTab = wait.until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
            mallTab.click();
        } catch (Exception e) {
            System.out.println("Gagal klik Mall Tab. Iklan mungkin menghalangi atau Tab belum load.");
        }
        
        try { Thread.sleep(3000); } catch (Exception e) {}

        // 3. MANUAL SCROLL LOOP TO FIND "LARI"
        System.out.println("Mencari menu 'Lari' dengan scroll manual...");
        boolean menuFound = false;
        int maxScrolls = 3; 

        for (int i = 0; i < maxScrolls; i++) {
            try {
                WebElement lariIcon = driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"Lari\")"
                ));
                
                System.out.println("Menu 'Lari' ketemu! Klik...");
                lariIcon.click();
                menuFound = true;
                break; 
                
            } catch (Exception e) {
                System.out.println("Menu belum terlihat, scroll ke bawah (" + (i + 1) + "/" + maxScrolls + ")...");
                actions.scrollVertical(); 
                try { Thread.sleep(1500); } catch (Exception ex) {} 
            }
        }

        if (!menuFound) {
            System.out.println("PERINGATAN: Menu 'Lari' tidak ditemukan.");
        }

        // 4. CLICK "AYO MULAI LARI"
        System.out.println("Klik tombol Ayo Mulai Lari...");
        try {
            WebElement btnMulai = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.accessibilityId("Ayo Mulai Lari")
            ));
            btnMulai.click();
        } catch (Exception e) {
            System.out.println("Button Ayo Mulai tidak muncul (Mungkin sudah di dashboard).");
        }

        // 5. DISMISS NOTIFICATION
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        tapCenterScreen(); 

        System.out.println("Sukses masuk dashboard Lari.");
    }

    // [FIX LOGIC IKLAN] Lebih Cerdas & Sabar
    public void handlePotentialAds() {
        System.out.println("Checking for Ads...");
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Prioritas 1: Cari TEKS "Nanti Saja" (Paling akurat)
            WebElement textBtn = shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_TEXT));
            textBtn.click();
            System.out.println("Iklan ditutup via Teks.");
        } catch (Exception e1) {
            // Prioritas 2: Kalau teks gak ketemu, cari ID-nya
            try {
                WebElement idBtn = shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_ID));
                idBtn.click();
                System.out.println("Iklan ditutup via ID.");
            } catch (Exception e2) {
                System.out.println("Aman, tidak ada iklan.");
            }
        }
    }

    public void tapCenterScreen() {
        Dimension size = driver.manage().window().getSize();
        actions.tapByCoordinates(size.width / 2, size.height / 2);
    }

    // GLOBAL HELPERS
    public void click(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public String getText(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void takeScreenshot(String fileName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destFile = new File("screenshots/" + fileName + "_" + timestamp + ".png");
            FileUtils.copyFile(srcFile, destFile);
        } catch (Exception e) {
            System.out.println("Gagal screenshot: " + e.getMessage());
        }
    }
}