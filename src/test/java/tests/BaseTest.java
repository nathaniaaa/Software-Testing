package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.URI;
import java.time.Duration;

public class BaseTest {

    // Variable Global (bisa dipakai semua subclass file)
    protected AndroidDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws Exception {
        // Setup Koneksi
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("2ab55c03") //UDID HP TESTING
                .setDeviceName("HP TESTING")
                .setAdbExecTimeout(Duration.ofSeconds(60))

                // NOTE NOTE
                // Di pancing untuk buka Settings dulu biar koneksi aman di Oppo
                .setAppPackage("com.android.settings")
                .setAppActivity(".Settings")
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        // Inisialisasi Wait (maksimal 20 detik)
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Navigasi ke AyoLari 
        masukKeMenuAyoLari();
    }

    @AfterMethod
    public void tearDown() {
        // Matikan driver setelah test selesai 
        if (driver != null) {
            driver.quit();
        }
    }

    // FUNGSI NAVIGASI 
    public void masukKeMenuAyoLari() {
        System.out.println(" Memulai Navigasi ke AyoLari");
        
        // Buka MyTelkomsel
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
        } catch (Exception e) {
            // Kalau app belum kebuka, bakal error dikit tapi lanjut
        }

        try {
            // Cari & Klik Menu 'AyoLari' (Logic Index ke-5)
            System.out.println("Mencari Menu AyoLari");
            
            String xpathAyoLari = "(//android.widget.FrameLayout[@resource-id='com.telkomsel.telkomselcm:id/cvDigitalService'])[5]";
            
            WebElement btnAyoLari = wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpathAyoLari))
            );
            btnAyoLari.click();
            System.out.println("Tombol Menu diklik");

            // Validasi: sudah masuk? (Cek tombol 'Mulai Lari')
            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("Mulai Lari")
            ));
            System.out.println("Berhasil: Sudah berada di dalam halaman AyoLari.");

        } catch (Exception e) {
            System.out.println("Gagal: Tidak bisa masuk ke AyoLari");
            System.out.println("Error: " + e.getMessage());
            Assert.fail("Gagal masuk ke menu AyoLari, cek kode/koneksi/xpath");
        }
    }
}