package tests;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("RRCTA02QJAR") 
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.android.settings") // Trigger pancingan
                .setAppActivity(".Settings")
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // masukKeMenuAyoLari();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- TEST: TEST KONEKSI & SCROLL ---
    @Test
    public void testConnectionAndScroll() {
        System.out.println("--- TESTING KONEKSI & SCROLL ---");

        // 1. Wait for Settings List to Load
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.className("androidx.recyclerview.widget.RecyclerView")
            ));
        } catch (Exception e) {
            System.out.println("Warning: List menu belum muncul penuh.");
        }

        // 2. Manual Scroll Test (Swipe Up)
        System.out.println("Mencoba Scroll ke bawah (Manual)...");
        scrollScreen();
        try { Thread.sleep(1000); } catch (Exception e) {} 
        
        scrollScreen();
        try { Thread.sleep(1000); } catch (Exception e) {}

        // 3. SMART SCROLL TEST (Cari Teks)
        // Kita cari menu yang biasanya ada di paling bawah Settings.
        // Ganti teks "About" dengan "Tentang" jika HP kamu Bahasa Indonesia.
        String textToFind = "Tentang"; 
        
        System.out.println("Mencoba Smart Scroll mencari: " + textToFind);
        scrollToText(textToFind);

        System.out.println("Semua Test Selesai!");
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    public void masukKeMenuAyoLari() {
        System.out.println("Memulai Navigasi ke AyoLari");
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {}

        try {
            System.out.println("Mencari Menu AyoLari");
            // XPath index ke-5 sesuai request sebelumnya
            String xpathAyoLari = "(//android.widget.FrameLayout[@resource-id='com.telkomsel.telkomselcm:id/cvDigitalService'])[5]";
            
            WebElement btnAyoLari = wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpathAyoLari))
            );
            btnAyoLari.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("Mulai Lari")
            ));
            System.out.println("Berhasil masuk AyoLari.");

        } catch (Exception e) {
            System.out.println("Gagal masuk AyoLari: " + e.getMessage());
            Assert.fail("Gagal navigasi.");
        }
    }
    
    // REUSABLE SCROLL METHODS
    /**
     * METHOD 1: MANUAL SWIPE
     * Melakukan swipe layar dari bawah ke atas (seperti scroll TikTok/IG).
     * Cocok untuk sekedar geser layar.
     */
    public void scrollScreen() {
        System.out.println("Melakukan Scroll Layar...");
        Dimension size = driver.manage().window().getSize();
        
        // Titik tengah X
        int centerX = size.width / 2;
        
        // Titik mulai Y (Bawah layar, 80%)
        int startY = (int) (size.height * 0.8);
        
        // Titik akhir Y (Atas layar, 20%)
        int endY = (int) (size.height * 0.2);

        // W3C Action Sequence (Standar baru Appium)
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);

        sequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), centerX, endY));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));
    }

    /**
     * METHOD 2: SMART SCROLL TO TEXT
     * Mencari teks spesifik dan otomatis scroll sampai ketemu.
     * Cocok untuk cari tombol 'Simpan' atau 'Logout' yang ada di bawah.
     * @param visibleText Teks yang muncul di layar (Case Sensitive)
     */
    public void scrollToText(String visibleText) {
        System.out.println("Mencari teks: " + visibleText);
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                ".scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\").instance(0))"
            ));
            System.out.println("Teks ditemukan!");
        } catch (Exception e) {
            System.out.println("Gagal scroll ke teks: " + visibleText);
            Assert.fail("Elemen dengan teks '" + visibleText + "' tidak ketemu setelah scroll.");
        }
    }
}