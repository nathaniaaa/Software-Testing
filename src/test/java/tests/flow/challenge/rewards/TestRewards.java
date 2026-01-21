package tests.flow.challenge.rewards; 

import tests.BaseTest;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder; 

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestRewards extends BaseTest {

    // Daftar Lokasi
    
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Tab Rewards (Header Atas)
    By tabRewards = AppiumBy.xpath("//android.view.View[@content-desc='Rewards Rewards']");

    // Button "Riwayat Rewards" 
    By btnRiwayatReward = AppiumBy.xpath("//android.view.View[@content-desc='Public Riwayat Reward']");

    // Back
    By btnBack = AppiumBy.xpath("//android.widget.Button");

    // Test Cases
    @Test(priority = 1)
    public void testMasukMenuChallenge() {
        System.out.println("TEST 1: Buka Menu Challenge");
        
        sleep(2000);

        // Klik Bottom Nav Challenge
        System.out.println("Klik menu Challenge");
        driver.findElement(navChallenge).click();
        
        // Validasi: Pastikan tombol Rewards muncul sebagai tanda halaman challenge terbuka
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabRewards));
        
        // Assertion: Cek elemen ada
        Assert.assertTrue(driver.findElements(tabRewards).size() > 0, "Gagal masuk halaman Challenge!");
        
        TestListener.getTest().pass("Halaman Challenge Terbuka (Tab Rewards terlihat).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testBukaRewardsDanRiwayat() {
        System.out.println("TEST 2: Buka Rewards -> Riwayat -> Back");

        // Klik Tab Rewards
        sleep(1500); 
        System.out.println("Klik Tab Rewards");
        wait.until(ExpectedConditions.elementToBeClickable(tabRewards)).click();

        TestListener.getTest().pass("Masuk ke Tab Rewards.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Button Riwayat Rewards
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        
        // Assertion: Pastikan tombol Riwayat ada sebelum diklik
        Assert.assertTrue(driver.findElement(btnRiwayatReward).isDisplayed(), "Tombol Riwayat Rewards tidak ditemukan.");

        sleep(1500); 
        System.out.println("Klik Riwayat Rewards");
        driver.findElement(btnRiwayatReward).click();

        // Validasi Halaman Riwayat
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBack));
        System.out.println("Masuk ke halaman Riwayat Rewards.");
        
        // Assertion: Pastikan masuk halaman detail (ada tombol back)
        Assert.assertTrue(driver.findElement(btnBack).isDisplayed(), "Gagal masuk halaman Riwayat Rewards.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Riwayat Rewards.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik BACK
        sleep(2000); 
        System.out.println("Klik Back");
        
        // Cek apakah tombol back ada
        if (driver.findElements(btnBack).size() > 0) {
            driver.findElement(btnBack).click();
        } else {
            // Fallback: pakai bawaan system jika tombol back gak ada
            System.out.println("Tombol Back UI tidak ada, pakai Back System.");
            driver.navigate().back();
        }

        // Validasi: harusnya balik ke halaman Rewards (tombol Riwayat muncul lagi)
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        } catch (Exception e) {}

        Assert.assertTrue(driver.findElements(btnRiwayatReward).size() > 0, "Gagal kembali ke halaman Rewards! Masih di halaman Riwayat.");
        System.out.println("Berhasil kembali.");

        TestListener.getTest().pass("Berhasil kembali ke halaman Rewards.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    public void sleep(int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {}
    }
}