package tests.flow.aktivitas;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestRiwayatChallenge extends BaseTest {

    // Daftar Lokasi

    // Ikon Aktivitas (Bottom Navigation)
    By iconAktivitas = AppiumBy.xpath("//android.widget.Button[@text=\"Aktivitas Aktivitas\"]");

    // Ikon Riwayat Challenge
    By tabChallenge = AppiumBy.accessibilityId("Leaderboard Riwayat Challenge");

    // LIST ITEM (Di Halaman List Challenge) - membuka yang MAGERUN
    By cardChallengePertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // DETAIL PAGE (Setelah Card diklik)
    // BUTTON DESKRIPSI 
    By btnDeskripsi = AppiumBy.xpath("//android.view.View[@resource-id='radix-_r_1t_-trigger-deskripsi']");
    
    // BUTTON LEADERBOARD
    By btnLeaderboard = AppiumBy.xpath("//android.view.View[@resource-id='radix-_r_1t_-trigger-leaderboard']");

    // IKON BACK
    By btnBack = AppiumBy.className("android.widget.Button");

    // Test Case
    @Test(priority = 1)
    public void testPindahKeTabChallenge() {
        System.out.println("--- TEST 1: Pindah ke Tab Challenge ---");

        // 1. Klik Tab 'Riwayat Challenge'
        // BaseTest sudah mengantar kita ke Dashboard Lari, tapi default-nya di tab Lari.
        // Kita harus pindah tab dulu.
        System.out.println("Klik Tab Riwayat Challenge...");
        click(tabChallenge);

        // 2. Validasi apakah list challenge muncul
        // Kita cek apakah card pertama sudah visible
        waitForVisibility(cardChallengePertama);
        boolean isCardVisible = driver.findElements(cardChallengePertama).size() > 0;
        
        Assert.assertTrue(isCardVisible, "Gagal pindah tab! List Challenge tidak muncul.");
        
        System.out.println("Berhasil pindah ke Tab Challenge.");
        takeScreenshot("Tab_RiwayatChallenge_Success");
    }

    @Test(priority = 2)
    public void testBukaDetailChallenge() {
        System.out.println("--- TEST 2: Buka Detail Challenge ---");

        // 1. Klik salah satu challenge (yg pertama)
        System.out.println("Membuka challenge pertama...");
        click(cardChallengePertama);

        // 2. Validasi elemen di dalam detail (Deskripsi & Leaderboard)
        // Kita pastikan tombol Deskripsi muncul sebagai tanda page sudah load
        waitForVisibility(btnDeskripsi);
        
        boolean isDeskripsiAda = driver.findElements(btnDeskripsi).size() > 0;
        boolean isLeaderboardAda = driver.findElements(btnLeaderboard).size() > 0;

        Assert.assertTrue(isDeskripsiAda, "Tombol Deskripsi tidak ditemukan!");
        Assert.assertTrue(isLeaderboardAda, "Tombol Leaderboard tidak ditemukan!");

        System.out.println("Detail Challenge terbuka dengan benar.");
        takeScreenshot("Detail_Challenge_Success");
    }

    @Test(priority = 3)
    public void testKembaliKeList() {
        System.out.println("--- TEST 3: Kembali ke List Challenge ---");

        // Klik Back
        click(btnBack);

        // Validasi balik ke list (Cek tab challenge lagi atau card lagi)
        waitForVisibility(cardChallengePertama);
        
        System.out.println("Berhasil kembali ke list.");
    }
}