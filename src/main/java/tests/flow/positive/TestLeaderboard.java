package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestLeaderboard extends BaseTest {
    // **** BAGIAN TAB CHALLENGE ****

    // Daftar Lokasi 
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 

    // Ikon Aktivitas (Bottom Navigation)
    By navAktivitas = AppiumBy.xpath("//android.widget.Button[@text=\"Aktivitas Aktivitas\"]");

    // Detail Page
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");
    
    // Tombol Back
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    By btnBackListPublic = AppiumBy.xpath("//android.view.View[@content-desc='challenges']");
    By btnBackListSaya = AppiumBy.xpath("//android.view.View[@content-desc='joined']");

    // **** BAGIAN TAB LEADERBOARD ****
    // Tab Leaderboard (Header Atas)
    By tabLeaderboardIkonAtas = AppiumBy.xpath("//android.view.View[@content-desc='Leaderboard Leaderboard']");

    // Validasi (Global Leaderboard)
    By textGlobal = AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Global') or contains(@text, 'Global')] | //android.widget.TextView[contains(@text, 'Global')]");

    @Test(priority = 1, description = "Pengguna membuka Tab Leaderboard di halaman Challenge Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan list Top 10 Pengguna Ayolari dengan total lari terbanyak, serta menampilkan peringkat pengguna di leaderboard Ayolari secara global",
        note = "",
        group = "LEADERBOARD"
    )
    public void testMasukLeaderboard() {
        System.out.println("TEST 1: Pengguna membuka Tab Leaderboard di halaman Challenge Lari");

        // Masuk Menu Challenge
        driver.findElement(navChallenge).click();
        waitTime();

        actions.scrollToTop();

        // Klik ikon piala (Leaderboard)
        wait.until(ExpectedConditions.elementToBeClickable(tabLeaderboardIkonAtas));
        clickTest(tabLeaderboardIkonAtas, "Klik ikon Leaderboard di header atas");
        waitTime();
        
        // Jeda loading data leaderboard 
        System.out.println("Menunggu data leaderboard");
        try { Thread.sleep(4000); } catch (Exception e) {} 
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(textGlobal));
        } catch (Exception e) {}

        Assert.assertTrue(driver.findElements(textGlobal).size() > 0, "Data Leaderboard (Global) tidak muncul!");

        logPass("Berhasil masuk dan memuat data Leaderboard");
    }

    // Daftar Lokasi - Riwayat Challenge
    // Tab Challenge (Header Atas)
    By tabRiwayatChallenge = AppiumBy.accessibilityId("Leaderboard Riwayat Challenge");

    // Card Challenge 
    By cardChallengePertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Detail Page Elements
    // Button Deskripsi dan Leaderboard
    By btnDeskripsi = AppiumBy.xpath("//android.widget.TextView[@text='Deskripsi'] | //android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By btnLeaderboard = AppiumBy.xpath("//android.widget.TextView[@text='Leaderboard'] | //android.view.View[contains(@resource-id, 'trigger-leaderboard')]");

    By textEmptyState = AppiumBy.xpath("//*[contains(@text, 'Belum ada') or contains(@text, 'Tidak ada') or contains(@text, 'belum ada')]");

    static boolean isRiwayatChallengeAda = false;

    @Test(priority = 2, description = "Interaksi pada Halaman Detail Riwayat Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat melihat detail riwayat challenge dengan mengklik salah satu card riwayat challenge, lalu pengguna dapat berinteraksi dengan konten yang ada di dalamnya seperti melihat deskripsi challenge, melihat leaderboard, dan kembali ke list riwayat challenge",
        note = "",
        group = "LEADERBOARD"
    )
    public void testInteraksiDetailRiwayatChallenge() {
        System.out.println("TEST 2: Interaksi pada Halaman Detail Riwayat Challenge");

        actions.scrollToTop();
        waitTime();

        clickTest(navAktivitas, "Klik ikon Aktivitas di Bottom Navigation");
        waitTime();

        System.out.println("Klik Riwayat Challenge");
        clickTest(tabRiwayatChallenge, "Klik Riwayat Challenge");
        waitTime();

        boolean isKosong = driver.findElements(textEmptyState).size() > 0;

        // Cek apakah ada card riwayat challenge?
        if (isKosong) {
            System.out.println("SKIP: Teks Empty State terdeteksi!");
            isRiwayatChallengeAda = false; // Set flag
            logPass("Berhasil navigasi ke Tab Riwayat Challenge (Tampilan Empty State / Belum ada riwayat)");
        } else {
            isRiwayatChallengeAda = true; // Set flag
            logPass("Berhasil masuk dan List Riwayat Challenge tampil.");
        }

        waitTime();

        // Pastikan ada card riwayat challenge, kalau engga skip
        if (!isRiwayatChallengeAda) {
            logSkip("Test dilewati: Tidak ada riwayat challenge untuk dibuka");
            return;
        }

        // Masuk ke card
        System.out.println("Masuk ke Riwayat Challenge Pertama");
        clickTest(cardChallengePertama, "Klik Card Riwayat Challenge Pertama");

        // Validasi masuk detail (tombol Deskripsi)
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnDeskripsi));
        
        // Pindah ke Leaderboard
        System.out.println("Klik Button Leaderboard");
        clickTest(btnLeaderboard, "Klik Button Leaderboard");
        waitTime();
        
        // Validasi: pastikan tombol deskripsi masih ada (tidak crash)
        Assert.assertTrue(driver.findElements(btnDeskripsi).size() > 0, "Tab Leaderboard gagal dimuat");
        logPass("Berhasil klik Tab Leaderboard");

        // Pastikan benar-benar ada di dalam Detail Riwayat Challenge 
        boolean isDiDetailChallenge = driver.findElements(btnDeskripsi).size() > 0;

        if (isDiDetailChallenge) {
            driver.findElement(btnBack).click();
            waitTime();
        } else {
            logSkip("Test dilewati: Posisi sudah berada di luar halaman detail.");
        }
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
