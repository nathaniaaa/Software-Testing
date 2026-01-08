package tests;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.Dimension;
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
    // Perhatikan: ID elemen tetap menggunakan 'com.telkomsel.mytelkomsel' (sesuai Inspector)
    private final AppiumBy AD_CLOSE_BUTTON = (AppiumBy) AppiumBy.id("com.telkomsel.telkomselcm:id/btSecondTypeFirstSecondary");
    private final AppiumBy MALL_TAB_ID = (AppiumBy) AppiumBy.accessibilityId("Mall");

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("RRCTA02QJAR") 
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                // --- PERBAIKAN DI SINI ---
                // Gunakan package asli aplikasi (yang terinstall di HP)
                .setAppPackage("com.telkomsel.telkomselcm") 
                // Hapus setAppActivity agar Appium mencari default launcher sendiri
                // .setAppActivity(...) <--- DIHAPUS
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
        
        // Pastikan app aktif
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {}

        // 1. HANDLE ADS
        handlePotentialAds();

        // 2. TAP "MALL" TAB
        System.out.println("Klik Menu Mall...");
        try {
            WebElement mallTab = wait.until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
            mallTab.click();
        } catch (Exception e) {
            System.out.println("Gagal klik Mall Tab. Coba tap koordinat manual...");
            // actions.tapByCoordinates(540, 2200); 
        }
        
        // Tunggu Mall loading
        try { Thread.sleep(3000); } catch (Exception e) {}

        // 3. MANUAL SCROLL LOOP TO FIND "LARI"
        System.out.println("Mencari menu 'Lari' dengan scroll manual...");
        boolean menuFound = false;
        int maxScrolls = 3; 

        for (int i = 0; i < maxScrolls; i++) {
            try {
                // Strict text match untuk menghindari salah klik banner
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
                try { Thread.sleep(1500); } catch (Exception ex) {} // Jeda agak lama biar UI stabil
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
            System.out.println("Button not found (Maybe already on dashboard).");
        }

        // 5. DISMISS NOTIFICATION
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        tapCenterScreen(); 

        System.out.println("Sukses masuk dashboard Lari.");
    }

    public void handlePotentialAds() {
        System.out.println("Checking for Ads...");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement closeBtn = shortWait.until(ExpectedConditions.visibilityOfElementLocated(AD_CLOSE_BUTTON));
            closeBtn.click();
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(AD_CLOSE_BUTTON));
        } catch (Exception e) {}

        // Cek apakah layar tertutup (Mall tab tidak bisa diklik)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(MALL_TAB_ID));
        } catch (Exception e) {
             System.out.println("Ad detected (Type B). Tapping outside...");
             actions.tapByCoordinates(540, 150);
             try { Thread.sleep(1500); } catch (InterruptedException ex) {}
        }
    }

    public void tapCenterScreen() {
        Dimension size = driver.manage().window().getSize();
        actions.tapByCoordinates(size.width / 2, size.height / 2);
    }
}