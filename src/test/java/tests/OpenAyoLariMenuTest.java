package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.net.URI;
import java.time.Duration;

public class OpenAyoLariMenuTest {

    @Test
    public void openAyoLariMenu() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("2ab55c03") // UDID Oppo 
                .setDeviceName("Oppo Reno8 T 5G")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.android.settings") 
                .setAppActivity(".Settings")
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        AndroidDriver driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        // 1. Buka Aplikasi MyTelkomsel 
        System.out.println("Membuka MyTelkomsel...");
        driver.activateApp("com.telkomsel.telkomselcm");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // 2. KLIK MENU 'AYO LARI'
            // Masalah: ID-nya pasaran (dipakai rame-rame).
            // Solusi: Pakai XPath Index ke-5 sesuai info Inspector
            
            System.out.println("Mencari menu Ayo Lari (Index ke-5)...");

            String xpathAyoLari = "(//android.widget.FrameLayout[@resource-id='com.telkomsel.telkomselcm:id/cvDigitalService'])[5]";
            
            WebElement btnAyoLari = wait.until(
                ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpathAyoLari))
            );
            
            btnAyoLari.click();
            System.out.println("KLIK BERHASIL! Sedang masuk ke Ayo Lari...");

            // 3. tunggu sampai halaman AyoLari (WebView) muncul
            // tunggu sampai muncul tombol 'Mulai Lari' yang tadi kita bahas
            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.accessibilityId("Mulai Lari")
            ));
            System.out.println("Sudah masuk di dalam Ayo Lari!");

        } catch (Exception e) {
            System.out.println("Gagal klik. Cek apakah urutan menunya berubah?");
            e.printStackTrace();
        }

        Thread.sleep(5000);
        // driver.quit();
    }
}