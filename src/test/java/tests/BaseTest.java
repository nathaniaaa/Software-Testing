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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; 

    // --- LOCATORS ---
    // Iklan & Navigasi Awal
    private final By AD_CLOSE_BUTTON_TEXT = AppiumBy.xpath("//android.widget.Button[@text='Nanti Saja']");
    private final By AD_CLOSE_BUTTON_ID = AppiumBy.id("com.telkomsel.telkomselcm:id/btSecondTypeFirstSecondary");
    private final By MALL_TAB_ID = AppiumBy.accessibilityId("Mall");
    
    // LOCATOR BARU: Bottom Navigation Beranda (Ayo Lari)
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
                .setNoReset(true) // PENTING: NoReset true agar session tidak mati
                .setNewCommandTimeout(Duration.ofSeconds(60)); 

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new ActionHelper(driver);

        // --- SMART NAVIGATION ---
        // Cek posisi dulu sebelum navigasi penuh
        ensureOnAyoLariDashboard();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- LOGIC BARU: Cek & Reset ke Beranda ---
    // --- LOGIC BARU: FORCE RESTART APP ---
    public void ensureOnAyoLariDashboard() {
        System.out.println("--- PREPARING TEST ENV ---");
        
        try {
            String appPackage = "com.telkomsel.telkomselcm";
            
            // 1. Matikan Aplikasi (Force Stop)
            // Ini membersihkan semua popup, sisa scroll, atau page yang nyangkut
            System.out.println("Merestart aplikasi agar fresh...");
            driver.terminateApp(appPackage);
            
            // Jeda dikit biar napas
            try { Thread.sleep(2000); } catch (Exception e) {}

            // 2. Buka Lagi
            driver.activateApp(appPackage);
            System.out.println("Aplikasi dibuka kembali.");

            // 3. Tunggu Loading Awal (Biasanya ada splash screen / iklan)
            try { Thread.sleep(5000); } catch (Exception e) {}

            // 4. Handle Iklan (Wajib cek lagi karena abis restart biasanya muncul iklan)
            handlePotentialAds();

            // 5. Cek Posisi: Apakah langsung di Dashboard Ayo Lari?
            // Biasanya kalau abis restart, dia balik ke Home Utama Telkomsel (Mall)
            if (isElementVisible(navBeranda)) {
                System.out.println("State Aman: Langsung masuk fitur Ayo Lari.");
            } else {
                // Kalau tidak ada tombol 'Beranda', berarti dia terlempar ke Home Utama
                // Jalankan navigasi masuk lagi
                System.out.println("Posisi di Home Utama. Masuk ke Ayo Lari...");
                masukKeMenuAyoLari();
            }

        } catch (Exception e) {
            System.out.println("Gagal restart app: " + e.getMessage());
        }
    }

    // Helper untuk cek elemen tanpa bikin error
    public boolean isElementVisible(By locator) {
        try {
            // Cek cepat (3 detik)
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- LOGIC LAMA (FULL NAVIGATION) ---
    public void masukKeMenuAyoLari() {
        try { 
            System.out.println("Menunggu aplikasi stabil...");
            Thread.sleep(5000); 
        } catch (InterruptedException e) {}
        
        System.out.println("Navigasi ke AyoLari via Mall...");
        
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {}

        // 1. HANDLE ADS
        handlePotentialAds();

        // 2. TAP "MALL" TAB
        System.out.println("Klik Menu Mall...");
        try { Thread.sleep(3000); } catch (Exception e) {} 

        try {
            WebElement mallTab = wait.until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
            mallTab.click();
            System.out.println("  -> Berhasil klik tab Mall.");
        } catch (Exception e) {
            System.out.println("  -> Gagal klik Mall Tab (Mungkin tertutup iklan/overlay). Force Tap...");
            actions.tapAtScreenRatio(0.5, 0.9);
        }
        
        try { Thread.sleep(3000); } catch (Exception e) {}

        // 3. MANUAL SCROLL LOOP TO FIND "LARI"
        System.out.println("Mencari menu 'Lari' dengan scroll manual...");
        boolean menuFound = false;
        int maxScrolls = 5; 

        for (int i = 0; i < maxScrolls; i++) {
            try {
                WebElement lariIcon = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Lari']"));
                
                System.out.println("Menu 'Lari' ketemu! Klik...");
                lariIcon.click();
                menuFound = true;
                break; 
                
            } catch (Exception e) {
                System.out.println("Menu belum terlihat, scroll ke bawah (" + (i + 1) + "/" + maxScrolls + ")...");
                actions.scrollVertical(); 
                try { Thread.sleep(2000); } catch (Exception ex) {} 
            }
        }

        if (!menuFound) {
            System.out.println("PERINGATAN: Menu 'Lari' tidak ditemukan. Cek screenshot.");
            takeScreenshot("LariNotFound");
        }

        // 4. CLICK "AYO MULAI LARI"
        System.out.println("Klik tombol Ayo Mulai Lari...");
        try {
            Thread.sleep(2000);
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

    public void handlePotentialAds() {
        System.out.println("Checking for Ads...");
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // --- TYPE A: Close Button ---
        try {
            WebElement textBtn = shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_TEXT));
            textBtn.click();
            System.out.println("  -> Iklan Type A ditutup (via Teks).");
        } catch (Exception e1) {
            try {
                WebElement idBtn = shortWait.until(ExpectedConditions.presenceOfElementLocated(AD_CLOSE_BUTTON_ID));
                idBtn.click();
                System.out.println("  -> Iklan Type A ditutup (via ID).");
            } catch (Exception e2) {
                System.out.println("  -> Tidak ada iklan Type A.");
            }
        }

        // --- TYPE B: Iklan Overlay (Tap Outside) ---
        System.out.println("Menunggu sejenak sebelum cek Iklan Type B (Overlay)...");
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
            
            System.out.println("  -> Aman: Layar tidak terblokir (Mall Tab clickable).");
            
        } catch (Exception e) {
             System.out.println("  -> LAYAR TERBLOKIR! Mencoba tap outside (Type B)...");
            actions.tapAtScreenRatio(0.5, 0.15); 
            try { Thread.sleep(1500); } catch (InterruptedException ex) {}
        }
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
        } catch (Exception e) {
            System.out.println("Gagal screenshot: " + e.getMessage());
        }
    }

    public String getScreenshotBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
}