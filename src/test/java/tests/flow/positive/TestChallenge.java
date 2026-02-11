package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder;
import com.example.App;

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
    @Test(priority = 1, description = "Pengguna membuka Tab Challenge pada halaman Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Tab Challenge menampilkan semua jenis Challenge yang tersedia dan sedang berlangsung di Ayolari.\n" + //
                        "\n" + //
                        "Adapun Challenge-challenge yang ditampilkan sbb:\n" + //
                        "1. Challenge Saya => Challenge yang diikuti oleh pengguna\n" + //
                        "2. Challenge Reward Januari => Challenge Bulanan dari Telkomsel yang bisa diikuti oleh semua pengguna Ayolari\n" + //
                        "3. Exclusive Challenges  => Challenge Spesial dari Telkomsel\n" + //
                        "4. Public Challenges  => Challenge buatan para pengguna Ayolari",
        note = "",
        group = "Challenge"
    )
    public void testNavigasiChallenge() {
        System.out.println("TEST 1: Pengguna membuka Tab Challenge pada halaman Challenge");

        // Masuk Menu Challenge
        clickTest(navChallenge, "Klik ikon Challenge di Bottom Navigation");
        waitTime();

        // Validasi: Masuk halaman challenge
        Assert.assertTrue(driver.findElements(btnLihatSemuaPublic).size() > 0, "Gagal masuk menu Challenge");
        logPass("Berhasil masuk ke halaman Challenge");
    }

    @Test(priority = 2, description = "Pengguna bergabung ke Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe publik buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testJoinPublicChallenge() {
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

        // Cari Card "Fun for health" (Scroll To Text)
        System.out.println("Mencari card 'Fun for health'");
        actions.scrollToText("Fun for health"); 
        waitTime(); 

        logPass("Card Fun for health ditemukan");
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardFunHealth)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        // Validasi: Masuk detail
        Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail challenge!");

        logPass("Berhasil masuk detail 'Fun for health'.");
        
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

        logPass("Berhasil klik Join Challenge -> muncul tombol Bergabung Sekarang.");

        // Klik 'Bergabung Sekarang'
        System.out.println("Klik tombol 'Bergabung Sekarang'");
        wait.until(ExpectedConditions.elementToBeClickable(btnBergabung));
        clickTest(btnBergabung, "Klik tombol 'Bergabung Sekarang'");
        
        logPass("Berhasil klik tombol 'Bergabung Sekarang' -> sudah bergabung");
        
        System.out.println("Berhasil Join! Menunggu proses");
        try { Thread.sleep(4000); } catch (Exception e) {} 

        logPass("Berhasil Join Challenge.");
        
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

        // Klik Card & Cek Detail lagi
        driver.findElement(cardFunHealth).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        TestListener.getTest().pass("Berhasil masuk halaman detail 'Fun for health'", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Tab Deskripsi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeVertical(0.5, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Cek Tab Leaderboard sebentar
        System.out.println("Cek Leaderboard");
        driver.findElement(tabLeaderboard).click();
        try { Thread.sleep(2000); } catch (Exception e) {}

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Keluar Challenge (Leave)
        System.out.println("Proses Keluar Challenge");
        
        // Klik 'Keluar Challenge'
        wait.until(ExpectedConditions.elementToBeClickable(btnKeluarChallenge)).click();
        try { Thread.sleep(2000); } catch (Exception e) {} // Tunggu popup konfirmasi

        TestListener.getTest().pass("Berhasil klik 'Keluar Challenge' -> Muncul konfirmasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Klik 'Ya, Lanjutkan'
        wait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiKeluar)).click();
        
        System.out.println("Berhasil keluar, tunggu loading");
        try { Thread.sleep(4000); } catch (Exception e) {} // Tunggu proses API selesai

        TestListener.getTest().pass("Berhasil Keluar/Leave Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Back 1x
        System.out.println("Back ke List Saya");
        if (driver.findElements(btnBackDetail).size() > 0) {
             driver.findElement(btnBackDetail).click();
        }
        waitTime();

        // Validasi Hilang
        System.out.println("Validasi Challenge 'Fun for health' sudah hilang");
        
        boolean isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        if (isCardPresent) {
            System.out.println("Card 'Fun for health' masih terlihat, coba refresh");
            actions.swipeVertical(0.3, 0.8);
            waitTime();
            isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        }

        Assert.assertFalse(isCardPresent, "GAGAL: Challenge 'Fun for health' masih ada di list padahal sudah keluar!");
        System.out.println("SUKSES: Challenge bersih.");
        
        TestListener.getTest().pass("Validasi: Challenge 'Fun for health' sudah hilang dari list.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 3, description = "Pengguna membuka Tab Challenge pada halaman Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Tab Challenge menampilkan semua jenis Challenge yang tersedia dan sedang berlangsung di Ayolari.\n" + //
                        "\n" + //
                        "Adapun Challenge-challenge yang ditampilkan sbb:\n" + //
                        "1. Challenge Saya => Challenge yang diikuti oleh pengguna\n" + //
                        "2. Challenge Reward Januari => Challenge Bulanan dari Telkomsel yang bisa diikuti oleh semua pengguna Ayolari\n" + //
                        "3. Exclusive Challenges  => Challenge Spesial dari Telkomsel\n" + //
                        "4. Public Challenges  => Challenge buatan para pengguna Ayolari",
        note = "",
        group = "Challenge"
    )
    public void testValidasiBerhasilBergabung() {
        System.out.println("TEST 3: Pengguna bergabung ke Public Challenges yang bertipe publik");

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        driver.findElement(btnLihatSemuaSaya).click();
        waitTime(); 

        TestListener.getTest().info("Masuk ke list Challenge Saya.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Cari & Validasi "Fun for health" ada di list
        System.out.println("Mencari 'Fun for health' di list");
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(cardFunHealth).size() > 0, 
            "GAGAL: Challenge 'Fun for health' tidak masuk ke Challenge Saya!");
        
        TestListener.getTest().pass("Validasi: Challenge 'Fun for health' ditemukan di list Saya.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2, description = "Pengguna keluar dari Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe publik buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testOutPublicChallenge() {

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        driver.findElement(btnLihatSemuaSaya).click();
        waitTime(); 

        TestListener.getTest().info("Masuk ke list Challenge Saya.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Cari & Validasi "Fun for health" ada di list
        System.out.println("Mencari 'Fun for health' di list");
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(cardFunHealth).size() > 0, 
            "GAGAL: Challenge 'Fun for health' tidak masuk ke Challenge Saya!");
        
        TestListener.getTest().pass("Validasi: Challenge 'Fun for health' ditemukan di list Saya.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Card & Cek Detail lagi
        driver.findElement(cardFunHealth).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        TestListener.getTest().pass("Berhasil masuk halaman detail 'Fun for health'", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Tab Deskripsi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeVertical(0.5, 0.7);
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Cek Tab Leaderboard sebentar
        System.out.println("Cek Leaderboard");
        driver.findElement(tabLeaderboard).click();
        try { Thread.sleep(2000); } catch (Exception e) {}

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        try { Thread.sleep(2000); } catch (Exception e) {}

        TestListener.getTest().pass("Scroll ke bawah di Leaderboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Keluar Challenge (Leave)
        System.out.println("Proses Keluar Challenge");
        
        // Klik 'Keluar Challenge'
        wait.until(ExpectedConditions.elementToBeClickable(btnKeluarChallenge)).click();
        try { Thread.sleep(2000); } catch (Exception e) {} // Tunggu popup konfirmasi

        TestListener.getTest().pass("Berhasil klik 'Keluar Challenge' -> Muncul konfirmasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Klik 'Ya, Lanjutkan'
        wait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiKeluar)).click();
        
        System.out.println("Berhasil keluar, tunggu loading");
        try { Thread.sleep(4000); } catch (Exception e) {} // Tunggu proses API selesai

        TestListener.getTest().pass("Berhasil Keluar/Leave Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Back 1x
        System.out.println("Back ke List Saya");
        if (driver.findElements(btnBackDetail).size() > 0) {
             driver.findElement(btnBackDetail).click();
        }
        waitTime();

        // Validasi Hilang
        System.out.println("Validasi Challenge 'Fun for health' sudah hilang");
        
        boolean isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        if (isCardPresent) {
            System.out.println("Card 'Fun for health' masih terlihat, coba refresh");
            actions.swipeVertical(0.3, 0.8);
            waitTime();
            isCardPresent = driver.findElements(cardFunHealth).size() > 0;
        }

        Assert.assertFalse(isCardPresent, "GAGAL: Challenge 'Fun for health' masih ada di list padahal sudah keluar!");
        System.out.println("SUKSES: Challenge bersih.");
        
        TestListener.getTest().pass("Validasi: Challenge 'Fun for health' sudah hilang dari list.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
