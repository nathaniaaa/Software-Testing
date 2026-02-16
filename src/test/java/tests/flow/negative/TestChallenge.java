package tests.flow.negative;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestChallenge extends BaseTest {

    // Daftar Lokasi 
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Lihat Semua - Tombol di Challenge Saya
    By btnLihatSemuaSaya = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]");
    
    // Lihat Semua - Tombol di Public Challenge
    By btnLihatSemuaPublic = AppiumBy.xpath("(//android.view.View[@content-desc='Lihat Semua'])[2]");

    //  Public Challenge - Target: Fun for health - Klo mau ke card lain janlup diganti nama text nya sesuai kyk judul card
    By cardPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Fun for health']");

    // Private Challenge - Target: Lari Merdeka 2026 - Klo mau ke card lain janlup diganti nama text nya sesuai kyk judul card
    By cardPrivateChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Lari Merdeka 2026']");

    // Detail Page
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");
    
    // Tombol Aksi (Sticky Button)
    // Flow Join: Join Challenge -> Bergabung Sekarang
    By btnJoinChallenge = AppiumBy.xpath("//android.widget.Button[@text='Join Challenge']");
    By btnBergabung = AppiumBy.xpath("//android.widget.Button[@text='Bergabung Sekarang']");
    By btnVerifikasi = AppiumBy.xpath("//android.widget.Button[@text='Verifikasi']");
    By btnMenungguPersetujuan = AppiumBy.xpath("//android.widget.Button[@text='Menunggu Persetujuan']");
    
    // Flow Leave: Keluar Challenge -> Ya, Lanjutkan
    By btnKeluarChallenge = AppiumBy.xpath("//android.widget.Button[@text='Keluar Challenge']");
    By btnKonfirmasiKeluar = AppiumBy.xpath("//android.widget.Button[@text='Ya, Lanjutkan']");

    // Tombol Back
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    By btnBackListPublic = AppiumBy.xpath("//android.view.View[@content-desc='challenges']");
    By btnBackListSaya = AppiumBy.xpath("//android.view.View[@content-desc='joined']");

    // **** Bagian Reward Bulan ****
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
    @Test(priority = 9, description = "Pengguna bergabung ke Public Challenges yang bertipe private")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe private buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testJoinPrivateChallenge() {
        System.out.println("TEST 9: Pengguna bergabung ke Public Challenges yang bertipe private");

        wait.until(ExpectedConditions.elementToBeClickable(btnBackListSaya)).click();;

        logInfo("Tampilan awal");

        // Scroll ke Bawah cari section Public
        System.out.println("Scroll mencari section Public Challenge");
        actions.swipeVertical(0.8, 0.5); 
        waitTime();

        logPass("Section Public Challenge ditemukan");

        // Klik "Lihat Semua" punya Public
        System.out.println("Klik Lihat Semua (Public)");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaPublic));
            clickTest(btnLihatSemuaPublic, "Klik tombol Lihat Semua di Public Challenge");
        } catch (Exception e) {
            By alternatifLihatSemuaPublic = AppiumBy.xpath("(//android.view.View[@content-desc='Lihat Semua'])[last()]");
            clickTest(alternatifLihatSemuaPublic, "Klik tombol Lihat Semua di Public Challenge - Alternatif");
        }
        waitTime();

        logPass("Berhasil masuk ke list card Public Challenge");

        // Cari Card "Lari Merdeka 2026" (Scroll To Text)
        System.out.println("Mencari card 'Lari Merdeka 2026'");
        actions.scrollToText("Lari Merdeka 2026"); 
        waitTime(); 

        logPass("Card Lari Merdeka 2026 ditemukan");
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardPrivateChallenge));
        clickTest(cardPrivateChallenge, "Klik card 'Lari Merdeka 2026' di Public Challenge");
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        // Validasi: Masuk detail
        Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail challenge!");

        logPass("Berhasil masuk detail 'Lari Merdeka 2026'.");
        
        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        logPass("Scroll ke bawah di Tab Deskripsi.");

        actions.swipeVertical(0.5, 0.7);
        waitTime();

        // Cek Tab Leaderboard
        System.out.println("Cek Leaderboard");
        clickTest(tabLeaderboard, "Klik Tab Leaderboard");
        waitTime();

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        logPass("Scroll ke bawah di Leaderboard.");

        // JOIN CHALLENGE (2 Langkah)
        // Klik 'Join Challenge'
        System.out.println("Klik tombol 'Join Challenge'");
        clickTest(btnJoinChallenge, "Klik tombol 'Join Challenge'");
        waitTime();

        // Pengisian kode undangan - verifikasi
        System.out.println("Input Kode Undangan");
        String kodeUndangan = "840494"; // jangan lupa ganti sesuai kode undangan challenge private 
        // locator Kotak Pertama (Index 1)
        By inputDigitPertama = AppiumBy.xpath("//android.app.AlertDialog[contains(@resource-id, 'radix')]/android.view.View/android.view.View/android.widget.TextView[1]");
        
        // Tunggu & Klik Kotak Pertama
        wait.until(ExpectedConditions.elementToBeClickable(inputDigitPertama));
        clickTest(inputDigitPertama, "Klik Kotak Input Pertama");
        
        // Loop Kirim Angka 
        char[] digits = kodeUndangan.toCharArray();
        for (char digit : digits) {
            String strDigit = String.valueOf(digit);
            
            // Kirim angka via keyboard action
            actions.pressKeyboard(strDigit);
            
            System.out.println("   -> Ketik angka: " + strDigit);
            try { Thread.sleep(500); } catch (Exception e) {}
        }
        
        logPass("Berhasil input kode: " + kodeUndangan);

        // Klik Tombol Verifikasi
        wait.until(ExpectedConditions.elementToBeClickable(btnVerifikasi));
        clickTest(btnVerifikasi, "Klik tombol 'Verifikasi'");
        
        // Tunggu loading verifikasi
        waitTime();

        if (driver.findElements(btnMenungguPersetujuan).size() > 0) {
            System.out.println("Kode undangan benar, lanjut ke proses bergabung.");
            logPass("Kode undangan benar, menunggu persetujuan");
        } else {
            System.out.println("Kode undangan salah atau terjadi masalah!");
            logInfo("Kode undangan salah, tidak dapat bergabung");
        }

        // Back 2x
        System.out.println("Back ke List Public");
        wait.until(ExpectedConditions.elementToBeClickable(btnBackDetail));
        clickTest(btnBackDetail, "Klik tombol Back");
        waitTime();
        
        System.out.println("Back ke Halaman Challenge");
        if (driver.findElements(btnBackListPublic).size() > 0) {
            clickTest(btnBackListPublic, "Klik tombol Back");
        } else {
            driver.navigate().back();
            logInfo("Tombol back tidak ditemukan, menggunakan back UI Handphone");
        }
        waitTime();

        logPass("Berhasil kembali ke halaman Challenge");
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
