package tests;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod; // Bisa diganti @AfterClass kalau mau run 1 sesi
import org.testng.annotations.BeforeMethod; // Bisa diganti @BeforeClass kalau mau run 1 sesi

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; // Objek ini yang bakal dipakai di semua test

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("2ab55c03") // Sesuaikan Device ID
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.android.settings") // Pancingan awal
                .setAppActivity(".Settings")
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Inisialisasi ActionHelper agar bisa dipakai oleh class anak (TestRincianLari)
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
        System.out.println("Navigasi ke AyoLari...");
        try {
            // Buka App MyTelkomsel
            driver.activateApp("com.telkomsel.telkomselcm");

            // Tunggu sebentar, loading homepage stabil (kadang ada iklan/banner)
            try { Thread.sleep(5000); } catch (Exception e) {}

            // Tutup Iklan/Pop-up kalau ada
            try {
                driver.findElement(AppiumBy.id("com.telkomsel.telkomselcm:id/btSecondTypeFirstSecondary")).click();
            } catch (Exception e) {}
            
            // Cari menu Ayo Lari (Index ke-5)
            String xpathAyoLari = "(//android.widget.FrameLayout[@resource-id='com.telkomsel.telkomselcm:id/cvDigitalService'])[5]";
            
            WebElement btnAyoLari = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpathAyoLari)));
            btnAyoLari.click();
            
            // Validasi sukses masuk
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Mulai Lari")));
            System.out.println("Sukses masuk dashboard Lari.");
            
        } catch (Exception e) {
            System.out.println("Navigasi Auto Gagal (Mungkin sudah di halaman?): " + e.getMessage());
        }
    }
}