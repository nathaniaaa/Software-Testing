package tests.flow.challenge.challenges;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestPublicChallenge extends BaseTest {

    // Daftar Lokasi
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Lihat Semua - Tombol di Challenge Saya
    By btnLihatSemuaSaya = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]");
    
    // Lihat Semua - Tombol di Public Challenge
    By btnLihatSemuaPublic = AppiumBy.xpath("(//android.view.View[@content-desc='Lihat Semua'])[2]");

    // Card Challenge - Target: Fun for health
    By cardFunHealth = AppiumBy.xpath("//android.widget.TextView[@text='Fun for health']");

    // Detail Page
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");
    
    // Tombol Aksi (Sticky Button)
    // Flow Join: Join Challenge -> Bergabung Sekarang
    By btnJoinChallenge = AppiumBy.xpath("//android.widget.Button[@text='Join Challenge']");
    By btnBergabung = AppiumBy.xpath("//android.widget.Button[@text='Bergabung Sekarang']");
    
    // Flow Leave: Keluar Challenge -> Ya, Lanjutkan
    By btnKeluarChallenge = AppiumBy.xpath("//android.widget.Button[@text='Keluar Challenge']");
    By btnKonfirmasiKeluar = AppiumBy.xpath("//android.widget.Button[@text='Ya, Lanjutkan']");

    // Tombol Back
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    By btnBackListPublic = AppiumBy.xpath("//android.view.View[@content-desc='challenges']");
    By btnBackListSaya = AppiumBy.xpath("//android.view.View[@content-desc='joined']");

        // Test Cases
    @Test(priority = 1)
    public void testJoinPublicChallenge() {
        System.out.println("TEST 1: Join 'Fun for health' dari Public");

        // Masuk Menu Challenge
        driver.findElement(navChallenge).click();
        try { Thread.sleep(3000); } catch (Exception e) {} 

        // Scroll ke Bawah cari section Public
        System.out.println("Scroll mencari section Public Challenge");
        actions.swipeVertical(0.8, 0.5); 
        try { Thread.sleep(2000); } catch (Exception e) {} 

        // Klik "Lihat Semua" punya Public
        System.out.println("Klik Lihat Semua (Public)");
        try {
            driver.findElement(btnLihatSemuaPublic).click();
        } catch (Exception e) {
            driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[last()]")).click();
        }
        try { Thread.sleep(3000); } catch (Exception e) {} 

        // Cari Card "Fun for health" (Scroll To Text)
        System.out.println("Mencari card 'Fun for health'");
        actions.scrollToText("Fun for health"); 
        try { Thread.sleep(3000); } catch (Exception e) {} 
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardFunHealth)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        try { Thread.sleep(3000); } catch (Exception e) {}

        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        actions.swipeVertical(0.5, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Cek Tab Leaderboard
        System.out.println("Cek Leaderboard");
        driver.findElement(tabLeaderboard).click();
        try { Thread.sleep(2000); } catch (Exception e) {}

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // JOIN CHALLENGE (2 Langkah)
        // Klik 'Join Challenge'
        System.out.println("Klik tombol 'Join Challenge'");
        wait.until(ExpectedConditions.elementToBeClickable(btnJoinChallenge)).click();
        try { Thread.sleep(2000); } catch (Exception e) {} 

        // Klik 'Bergabung Sekarang'
        System.out.println("Klik tombol 'Bergabung Sekarang'");
        wait.until(ExpectedConditions.elementToBeClickable(btnBergabung)).click();
        
        System.out.println("Berhasil Join! Menunggu proses");
        try { Thread.sleep(4000); } catch (Exception e) {} 

        // Back 2x
        System.out.println("Back ke List Public");
        wait.until(ExpectedConditions.elementToBeClickable(btnBackDetail)).click();
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        System.out.println("Back ke Halaman Challenge");
        if (driver.findElements(btnBackListPublic).size() > 0) {
            driver.findElement(btnBackListPublic).click();
        } else {
            driver.navigate().back();
        }
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    @Test(priority = 2)
    public void testCekMyChallengeAndLeave() {
        System.out.println("TEST 2: Validasi di 'Challenge Saya' & Leave");

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        driver.findElement(btnLihatSemuaSaya).click();
        try { Thread.sleep(3000); } catch (Exception e) {} 

        // Cari & Validasi "Fun for health" ada di list
        System.out.println("Mencari 'Fun for health' di list");
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(cardFunHealth).size() > 0, 
            "GAGAL: Challenge 'Fun for health' tidak masuk ke Challenge Saya!");
        
        // Klik Card & Cek Detail lagi
        driver.findElement(cardFunHealth).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        try { Thread.sleep(3000); } catch (Exception e) {}

        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        actions.swipeVertical(0.5, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Cek Tab Leaderboard sebentar
        System.out.println("Cek Leaderboard");
        driver.findElement(tabLeaderboard).click();
        try { Thread.sleep(2000); } catch (Exception e) {}

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Keluar Challenge (Leave)
        System.out.println("Proses Keluar Challenge");
        
        // Klik 'Keluar Challenge'
        wait.until(ExpectedConditions.elementToBeClickable(btnKeluarChallenge)).click();
        try { Thread.sleep(2000); } catch (Exception e) {} // Tunggu popup konfirmasi
        
        // Klik 'Ya, Lanjutkan'
        wait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiKeluar)).click();
        
        System.out.println("Berhasil keluar, tunggu loading");
        try { Thread.sleep(4000); } catch (Exception e) {} // Tunggu proses API selesai

        // Back 1x
        System.out.println("Back ke List Saya");
        if (driver.findElements(btnBackDetail).size() > 0) {
             driver.findElement(btnBackDetail).click();
        }
        try { Thread.sleep(3000); } catch (Exception e) {}

        // Validasi Hilang
        System.out.println("Validasi Challenge sudah hilang");
        
        boolean isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        if (isCardPresent) {
            System.out.println("Card masih terlihat, coba refresh");
            actions.swipeVertical(0.3, 0.8);
            try { Thread.sleep(3000); } catch (Exception e) {}
            isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        }

        Assert.assertFalse(isCardPresent, "GAGAL: Challenge masih ada di list padahal sudah keluar!");
        System.out.println("SUKSES: Challenge bersih.");
    }
}