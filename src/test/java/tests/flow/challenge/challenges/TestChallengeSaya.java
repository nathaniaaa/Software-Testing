package tests.flow.challenge.challenges;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestChallengeSaya extends BaseTest {

    // ==========================================
    // DAFTAR LOKASI (LOCATORS)
    // Sesuai Screenshot Excel
    // ==========================================

    // 1. Navigasi Awal
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    By titleChallengeSaya = AppiumBy.xpath("//android.view.View[@content-desc='Challenge Saya'] | //android.widget.TextView[@text='Challenge Saya']");

    // 2. Tombol Lihat Semua
    // Index 1 karena biasanya ini yang paling atas (milik Challenge Saya)
    By btnLihatSemua = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]");

    // 3. List Card (Di Halaman Lihat Semua)
    // Box 1 (Pelari FOMO)
    By cardPelariFomo = AppiumBy.xpath("//android.widget.TextView[@text=\"Pelari FOMO\"]");
    // Box 2 (Yuk Lari Sehat)
    By cardYukLariSehat = AppiumBy.xpath("//android.widget.TextView[@text=\"YUUUUK LARI SEHAT\"]");

    // 4. Detail Page Elements (Pelari FOMO & Yuk Lari Sehat)
    // Tab Deskripsi & Leaderboard (Pakai contains resource-id biar aman)
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");
    
    // Tombol Back di Detail Challenge
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    
    // Tombol Back di Halaman List "Lihat Semua" (Biasanya content-desc 'joined')
    By btnBackList = AppiumBy.xpath("//android.view.View[@content-desc='joined']"); 


    // ==========================================
    // TEST CASES
    // ==========================================

    @Test(priority = 1)
    public void testNavigasiDanSwipeHorizon() {
        System.out.println("--- TEST 1: Navigasi & Swipe Carousel ---");

        // 1. Masuk Menu Challenge
        System.out.println("Klik Menu Challenge...");
        driver.findElement(navChallenge).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleChallengeSaya));

        // 2. SWIPE HORIZONTAL
        System.out.println("Swipe Horizontal di area Challenge Saya...");
        try {
            // TIPS TUNING ANGKA:
            // Parameter: (Start X, End X, Posisi Y)
            
            // Coba 1: Swipe di ketinggian 0.25 (Agak atas, tempat biasanya card berada)
            // Dari Kanan (0.85) tarik ke Kiri (0.15)
            actions.swipeHorizontal(0.85, 0.15, 0.37); 
            Thread.sleep(4000);
            
            // Coba 2: Kalau belum kegeser, mungkin card-nya agak bawahan dikit?
            // Coba swipe di ketinggian 0.35
            // actions.swipeHorizontal(0.85, 0.15, 0.35);

            // Balikin lagi (Swipe Kiri ke Kanan) biar card awal muncul lagi
            actions.swipeHorizontal(0.15, 0.85, 0.37);
            Thread.sleep(4000);
            
        } catch (Exception e) {
            System.out.println("Gagal Swipe: " + e.getMessage());
        }

        // 3. Klik "Lihat Semua"
        System.out.println("Klik 'Lihat Semua'...");
        driver.findElement(btnLihatSemua).click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Validasi: Cek teks Pelari FOMO ada di layar (belum diklik)
        By textPelariFomo = AppiumBy.xpath("//android.widget.TextView[@text='Pelari FOMO']");
        Assert.assertTrue(driver.findElements(textPelariFomo).size() > 0, "Gagal masuk halaman List Challenge Saya!");
    }

    @Test(priority = 2)
    public void testDetailPelariFomo() {
        System.out.println("--- TEST 2: Cek Detail Pelari FOMO (Scroll Leaderboard) ---");

        // 1. Pilih Card 1
        System.out.println("Mencari Card Pelari FOMO...");
        // Wait agar card benar-benar siap diklik (handle loading/animasi list)
        wait.until(ExpectedConditions.elementToBeClickable(cardPelariFomo));
        
        System.out.println("Klik Card Pelari FOMO...");
        driver.findElement(cardPelariFomo).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        
        // 2. Cek Tab Deskripsi & Scroll
        System.out.println("Cek Tab Deskripsi...");
        driver.findElement(tabDeskripsi).click(); 
        try { Thread.sleep(3000); } catch (Exception e) {}

        System.out.println("Scroll Turun Deskripsi...");
        actions.swipeVertical(0.7, 0.4);
        try { Thread.sleep(2000); } catch (Exception e) {}

        System.out.println("Scroll Naik Deskripsi...");
        actions.swipeVertical(0.4, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // 3. Pindah Leaderboard & Scroll
        System.out.println("Pindah ke Leaderboard...");
        driver.findElement(tabLeaderboard).click();
        try { Thread.sleep(3000); } catch (Exception e) {} 
        
        System.out.println("Scroll Turun Leaderboard...");
        actions.swipeVertical(0.7, 0.4);
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        System.out.println("Scroll Naik Leaderboard...");
        actions.swipeVertical(0.4, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // 4. Back ke List
        System.out.println("Kembali ke List...");
        driver.findElement(btnBackDetail).click();
        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    @Test(priority = 3)
    public void testDetailYukLariSehat() {
        System.out.println("--- TEST 3: Cek Detail Yuk Lari Sehat (No Scroll) ---");

        if (driver.findElements(cardYukLariSehat).size() > 0) {
            System.out.println("Klik Card Yuk Lari Sehat...");
            driver.findElement(cardYukLariSehat).click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));

            System.out.println("Scroll Turun Deskripsi...");
            actions.swipeVertical(0.7, 0.4);
            try { Thread.sleep(2000); } catch (Exception e) {}

            System.out.println("Pindah ke Leaderboard (Cek isi saja)...");
            driver.findElement(tabLeaderboard).click();
            try { Thread.sleep(3000); } catch (Exception e) {}
            
            System.out.println("Leaderboard terlihat (Skip scrolling).");

            System.out.println("Kembali ke List...");
            driver.findElement(btnBackDetail).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
            
        } else {
            System.out.println("SKIP: Card Yuk Lari Sehat tidak ditemukan.");
        }
    }

    @Test(priority = 4)
    public void testKembaliKeMenuUtama() {
        System.out.println("--- TEST 4: Kembali ke Menu Utama Challenge ---");

        if (driver.findElements(btnBackList).size() > 0) {
             driver.findElement(btnBackList).click();
        } else {
             System.out.println("Tombol back custom ga ketemu, pakai Back System...");
             driver.navigate().back();
        }

        try { Thread.sleep(2000); } catch (Exception e) {}
        Assert.assertTrue(driver.findElements(btnLihatSemua).size() > 0, "Gagal kembali ke Menu Utama Challenge!");
        System.out.println("Selesai.");
    }
}