package tests.flow.challenge.leaderboard;

import tests.BaseTest;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder; 

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestLeaderboard extends BaseTest {

    // Daftar Lokasi
    
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Tab Leaderboard (Header Atas)
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[@content-desc='Leaderboard Leaderboard']");

    // Validasi (Global Leaderboard)
    By textGlobal = AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Global') or contains(@text, 'Global')] | //android.widget.TextView[contains(@text, 'Global')]");

    // Test Cases
    @Test(priority = 1)
    public void testMasukMenuChallenge() {
        System.out.println("TEST 1: Buka Tab Challenge");
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Klik Challenge di Bottom Navigation
        driver.findElement(navChallenge).click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Validasi: pastikan tombol Leaderboard di atas muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabLeaderboard));
        
        Assert.assertTrue(driver.findElements(tabLeaderboard).size() > 0, "Menu Challenge gagal terbuka!");
        
        TestListener.getTest().pass("Berhasil masuk ke halaman Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testMasukLeaderboard() {
        System.out.println("TEST 2: Klik Ikon Leaderboard");

        // Klik ikon piala (Leaderboard)
        wait.until(ExpectedConditions.elementToBeClickable(tabLeaderboard)).click();
        
        // Jeda loading data leaderboard 
        System.out.println("Menunggu data leaderboard");
        try { Thread.sleep(4000); } catch (Exception e) {} 
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(textGlobal));
        } catch (Exception e) {}

        Assert.assertTrue(driver.findElements(textGlobal).size() > 0, "Data Leaderboard (Global) tidak muncul!");

        TestListener.getTest().pass("Berhasil masuk dan memuat data Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 3)
    public void testScrollLeaderboard() {
        System.out.println("TEST 3: Scroll Daftar Leaderboard");

        // Scroll ke bawah dan atas 
        System.out.println("Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Screenshot saat di bawah
        TestListener.getTest().info("Scroll ke bawah daftar Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        System.out.println("Scroll Naik");
        actions.swipeVertical(0.5, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(tabLeaderboard).size() > 0, "Gagal scroll atau aplikasi close.");

        TestListener.getTest().pass("Berhasil scroll daftar Leaderboard naik dan turun.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        System.out.println("Selesai cek Leaderboard.");
    }
}