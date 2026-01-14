package tests.flow.challenge.rewards; 

import tests.BaseTest;
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
        Assert.assertTrue(driver.findElements(tabRewards).size() > 0, "Gagal masuk halaman Challenge!");
        
        takeScreenshot("Halaman_Challenge_Terbuka");
    }

    @Test(priority = 2)
    public void testBukaRewardsDanRiwayat() {
        System.out.println("TEST 2: Buka Rewards -> Riwayat -> Back");

        // Klik Tab Rewards
        sleep(1500); 
        System.out.println("Klik Tab Rewards");
        wait.until(ExpectedConditions.elementToBeClickable(tabRewards)).click();

        // Klik Button Riwayat Rewards
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        
        sleep(1500); 
        System.out.println("Klik Riwayat Rewards");
        driver.findElement(btnRiwayatReward).click();

        // Validasi Halaman Riwayat
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBack));
        System.out.println("Masuk ke halaman Riwayat Rewards.");
        takeScreenshot("Halaman_Riwayat_Rewards");

        // Klik BACK
        sleep(2000); 
        System.out.println("Klik Back");
        
        if (driver.findElements(btnBack).size() > 0) {
            driver.findElement(btnBack);
        } else {
            // Fallback: pakai bawaan system jika tombol back gak ada
            System.out.println("Tombol Back UI tidak ada, pakai Back System.");
            driver.navigate().back();
        }

        // Validasi akhir: harusnya balik ke halaman Rewards (tombol Riwayat muncul lagi)
        sleep(3000);
        Assert.assertTrue(driver.findElements(btnRiwayatReward).size() > 0, "Gagal kembali ke halaman Rewards!");
        System.out.println("Berhasil kembali.");
    }

    public void sleep(int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {}
    }
}