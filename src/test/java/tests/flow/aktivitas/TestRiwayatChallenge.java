package tests.flow.aktivitas;

import tests.BaseTest;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestRiwayatChallenge extends BaseTest {

    // Daftar Lokasi
    // Ikon Aktivitas (Bottom Navigation)
    By navAktivitas = AppiumBy.xpath("//android.widget.Button[@text=\"Aktivitas Aktivitas\"]");

    // Tab Challenge (Header Atas)
    By tabChallenge = AppiumBy.accessibilityId("Leaderboard Riwayat Challenge");

    // Card Challenge 
    By cardChallengePertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Detail Page Elements
    // Button Deskripsi dan Leaderboard
    By btnDeskripsi = AppiumBy.xpath("//android.widget.TextView[@text='Deskripsi'] | //android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By btnLeaderboard = AppiumBy.xpath("//android.widget.TextView[@text='Leaderboard'] | //android.view.View[contains(@resource-id, 'trigger-leaderboard')]");

    // Tombol Back
    By btnBack = AppiumBy.className("android.widget.Button");

    // Test Cases
    @Test(priority = 1)
    public void testNavigasiKeListChallenge() {
        System.out.println("TEST 1: Navigasi ke List Riwayat Challenge");

        // Klik ikon Aktivitas di Bottom Navigation
        System.out.println("Klik ikon Aktivitas");
        driver.findElement(navAktivitas).click();
        try { Thread.sleep(1000); } catch (Exception e) {}

        // Klik Riwayat Challenge di header
        System.out.println("Klik Riwayat Challenge");
        driver.findElement(tabChallenge).click();

        // Pastikan card muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(cardChallengePertama));
        boolean isCardVisible = driver.findElements(cardChallengePertama).size() > 0;
        Assert.assertTrue(isCardVisible, "Gagal: List Riwayat Challenge tidak tampil!");

        TestListener.getTest().pass("Berhasil Masuk ke Tab Riwayat Challenge, berikut Halaman List Riwayat Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testInteraksiDetailRiwayatChallenge() {
        System.out.println("TEST 2: Interaksi pada Halaman Detail Riwayat Challenge");

        try { Thread.sleep(2000); } catch (Exception e) {}

        // Masuk ke card
        System.out.println("Masuk ke Riwayat Challenge Pertama");
        driver.findElement(cardChallengePertama).click();

        // Validasi masuk detail (cek tombol Deskripsi)
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnDeskripsi));
        
        // Default halaman di Deskripsi -> scroll dulu
        System.out.println("Halaman Deskripsi terbuka, mencoba Scroll ke bawah");
        try {
            // Scroll ke bawah untuk baca deskripsi
            actions.swipeVertical(0.7, 0.3); 
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("Gagal scroll: " + e.getMessage());
        }

        // Pindah ke Leaderboard
        System.out.println("Klik Button Leaderboard");
        driver.findElement(btnLeaderboard).click();
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        // Validasi: pastikan tombol deskripsi masih ada (tidak crash)
        Assert.assertTrue(driver.findElements(btnDeskripsi).size() > 0, "Tab Leaderboard gagal dimuat");

        // Balik lagi ke Deskripsi
        System.out.println("Klik button ke Deskripsi");
        driver.findElement(btnDeskripsi).click();
        
        TestListener.getTest().pass("Test interaksi pada halaman detail Riwayat Challenge berhasil.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 3)
    public void testKembaliKeList() {
        System.out.println("TEST 3: Kembali ke List Riwayat Challenge");

        // Tekan Back
        System.out.println("Tekan tombol Back");
        driver.findElement(btnBack).click();

        // Validasi: Harus balik ke halaman list (card muncul lagi)
        try { Thread.sleep(1000); } catch (Exception e) {}
        boolean isListVisible = driver.findElements(cardChallengePertama).size() > 0;
        
        Assert.assertTrue(isListVisible, "Gagal kembali ke List Riwayat Challenge!");
        
        System.out.println("Siklus Test Selesai.");
        TestListener.getTest().pass("Berhasil kembali ke List Riwayat Challenge. Siklus Test Selesai.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
}