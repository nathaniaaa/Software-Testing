package tests.flow.aktivitas;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestRiwayatChallenge extends BaseTest {

    // Daftar Lokasi
    // Ikon Aktivitas (Bottom Navigation)
    By iconAktivitas = AppiumBy.xpath("//android.widget.Button[contains(@content-desc, 'Aktivitas') or contains(@text, 'Aktivitas')]");

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

        // STEP 1: Klik Menu Aktivitas di Bottom Navigation
        System.out.println("1. Klik Ikon Aktivitas");
        click(iconAktivitas);
        try { Thread.sleep(1000); } catch (Exception e) {}

        // STEP 2: Klik Tab Challenge di Header
        System.out.println("2. Klik Tab Riwayat Challenge");
        click(tabChallenge);

        // Pastikan Card muncul
        waitForVisibility(cardChallengePertama);
        boolean isCardVisible = driver.findElements(cardChallengePertama).size() > 0;
        Assert.assertTrue(isCardVisible, "Gagal: List Riwayat Challenge tidak tampil!");
        
        System.out.println("Berhasil masuk ke tab Riwayat Challenge.");
        takeScreenshot("ListRiwayatChallenge_Show");
    }

    @Test(priority = 2)
    public void testInteraksiDetailRiwayatChallenge() {
        System.out.println("TEST 2: Interaksi Pada Halaman Detail Riwayat Challenge");

        try { Thread.sleep(2000); } catch (Exception e) {}

        // STEP 3: Masuk ke Card
        System.out.println("3. Masuk ke Riwayat Challenge Pertama");
        click(cardChallengePertama);

        // Validasi masuk detail (Cek tombol Deskripsi)
        waitForVisibility(btnDeskripsi);
        
        // STEP 4: Default Halaman di Deskripsi -> Scroll Dulu
        System.out.println("4. Halaman Deskripsi terbuka, mencoba Scroll ke bawah");
        try {
            // Scroll ke bawah untuk baca deskripsi
            actions.scrollVertical(); 
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("Gagal scroll: " + e.getMessage());
        }

        // STEP 5: Pindah ke Leaderboard
        System.out.println("5. Klik Tab Leaderboard");
        click(btnLeaderboard);
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        // Validasi simpel: Pastikan tombol deskripsi masih ada (tidak crash)
        Assert.assertTrue(driver.findElements(btnDeskripsi).size() > 0, "Tab Leaderboard gagal dimuat");

        // STEP 6: Balik lagi ke Deskripsi
        System.out.println("6. Klik Balik ke Tab Deskripsi");
        click(btnDeskripsi);
        
        takeScreenshot("DetailChallenge_Interaction");
    }

    @Test(priority = 3)
    public void testKembaliKeList() {
        System.out.println("TEST 3: Kembali ke List Riwayat Challenge");

        // STEP 7: Tekan Back
        System.out.println("7. Tekan tombol Back");
        click(btnBack);

        // Validasi: Harus balik ke halaman list (Card muncul lagi)
        try { Thread.sleep(1000); } catch (Exception e) {}
        boolean isListVisible = driver.findElements(cardChallengePertama).size() > 0;
        
        Assert.assertTrue(isListVisible, "Gagal kembali ke List Riwayat Challenge!");
        
        System.out.println("Siklus Test Selesai.");
    }
}