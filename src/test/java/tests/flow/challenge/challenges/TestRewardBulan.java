package tests.flow.challenge.challenges;

import tests.BaseTest;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder; 

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestRewardBulan extends BaseTest {

    // Daftar Lokasi
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Lihat Detail Reward Bulan Januari
    By btnLihatDetailRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='Lihat Detail']");
    
    // Tab Menu (Challenge & Leaderboard)
    By tabChallengeRewardBulan = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-challenge')]");
    By tabLeaderboardRewardBulan = AppiumBy.xpath("//*[contains(@resource-id, 'trigger-leaderboard') or contains(@text, 'Leaderboard') or @content-desc='Leaderboard']");

    // Tombol Aksi - Reward
    By btnKlaim = AppiumBy.xpath("(//android.widget.Button[@text='Klaim'])[1]");
    By btnSelesai = AppiumBy.xpath("//android.widget.Button[@text='Selesai']");

    // Tombol Back 
    By btnBackRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='telkomsel-challenge']/android.widget.Button");

    // Test Cases
    @Test(priority = 1)
    public void testMasukDetailReward() {
        System.out.println("TEST 1: Navigasi ke Detail Reward Januari");

        // Klik Challenge (Bottom Nav)
        System.out.println("Klik Menu Challenge (Bottom Nav)");
        driver.findElement(navChallenge).click();

        TestListener.getTest().pass("Berhasil masuk ke halaman Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Klik "Lihat Detail"
        System.out.println("Mencari tombol 'Lihat Detail'");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnLihatDetailRewardBulan));
            driver.findElement(btnLihatDetailRewardBulan).click();
            System.out.println("Berhasil klik Lihat Detail.");
        } catch (Exception e) {
            System.out.println("Gagal klik Lihat Detail: " + e.getMessage());
            
            TestListener.getTest().fail("Tombol Lihat Detail tidak ditemukan.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
            Assert.fail("Tombol Lihat Detail tidak ditemukan.");
        }

        // Validasi: Pastikan masuk ke halaman detail
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabChallengeRewardBulan));
        
        Assert.assertTrue(driver.findElements(tabChallengeRewardBulan).size() > 0, "Gagal masuk halaman detail reward!");
        
        TestListener.getTest().pass("Berhasil masuk ke halaman Detail Reward Bulan Januari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testCekTabDanTombol() {
        System.out.println("TEST 2: Cek Tab Challenge & Leaderboard");

        // Aktivitas di Tab Challenge
        System.out.println("Klik Tab Challenge");
        driver.findElement(tabChallengeRewardBulan).click();
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Scroll ke Bawah (Cari tombol Klaim)
        System.out.println("Scroll ke bawah di Challenge");
        actions.swipeVertical(0.7, 0.3); 
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Halaman Reward Januari (Tab Challenge).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Cek & Klik Tombol Klaim
        System.out.println("Mengecek tombol Klaim");
        if (driver.findElements(btnKlaim).size() > 0) {
            System.out.println("Tombol KLAIM ditemukan. Klik");
            driver.findElement(btnKlaim).click();
            
            try { Thread.sleep(3000); } catch (Exception e) {}
            
            TestListener.getTest().info("Klik tombol Klaim.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Tap area aman 
            System.out.println("Tutup popup klaim (Tap Outside)");
            actions.tapAtScreenRatio(0.5, 0.2);
            try { Thread.sleep(1000); } catch (Exception e) {}

            TestListener.getTest().pass("Berhasil close popup klaim.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
        } else if (driver.findElements(btnSelesai).size() > 0) {
            System.out.println("Status: Reward sudah diklaim.");
            driver.findElement(btnSelesai).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
        } else {
            System.out.println("Info: Tombol Klaim/Selesai tidak muncul.");
        }

        System.out.println("Scroll balik ke atas agar Tab terlihat");
        actions.swipeVertical(0.3, 0.8); // Gerakan jari dari atas ke bawah (Scroll Naik)
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Pindah ke Tab Leaderboard
        System.out.println("Pindah ke Tab Leaderboard");
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabLeaderboardRewardBulan));
            driver.findElement(tabLeaderboardRewardBulan).click();
        } catch (Exception e) {
            System.out.println("Gagal klik Leaderboard via ID, coba force click Text");
            driver.findElement(By.xpath("//*[contains(@text, 'Leaderboard')]")).click();
        }
        
        try { Thread.sleep(3000); } catch (Exception e) {} 
        
        TestListener.getTest().pass("Berhasil pindah ke Tab Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll Turun Leaderboard
        System.out.println("Scroll ke bawah Leaderboard");
        actions.swipeVertical(0.7, 0.3); 
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Scroll Naik Leaderboard
        System.out.println("Scroll kembali ke atas Leaderboard");
        actions.swipeVertical(0.3, 0.7); 
        try { Thread.sleep(1500); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(tabLeaderboardRewardBulan).size() > 0, "Aplikasi crash atau keluar dari halaman detail.");
        
        TestListener.getTest().pass("Scroll kembali ke atas di Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 3)
    public void testKembaliKeList() {
        System.out.println("TEST 3: Kembali (Back Button)");

        wait.until(ExpectedConditions.elementToBeClickable(btnBackRewardBulan));
        driver.findElement(btnBackRewardBulan).click();

        try { Thread.sleep(2000); } catch (Exception e) {}
        
        boolean isBackSuccess = driver.findElements(btnLihatDetailRewardBulan).size() > 0;
        
        Assert.assertTrue(isBackSuccess, "Gagal kembali ke halaman List Rewards!");
        System.out.println("Berhasil kembali ke list.");
        
        TestListener.getTest().pass("Berhasil kembali ke halaman List Rewards utama.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
}