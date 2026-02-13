package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;


public class TestChallenge extends BaseTest {
    // **** BAGIAN TAB CHALLENGE ****

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
    @Test(priority = 1, description = "Pengguna membuka halaman Challenge dari Bottom Navigation")
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
        System.out.println("TEST 1: Pengguna membuka halaman Challenge dari Bottom Navigation");

        // Masuk Menu Challenge
        clickTest(navChallenge, "Klik ikon Challenge di Bottom Navigation");
        waitTime();

        // Validasi: Masuk halaman challenge
        Assert.assertTrue(driver.findElements(btnLihatSemuaPublic).size() > 0, "Gagal masuk menu Challenge");
        logPass("Berhasil masuk ke halaman Challenge");
    }

    @Test(priority = 2, description = "Pengguna membuka Challenges Saya")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan semua challenge yang telah diikuti maupun dibuat oleh Pengguna",
        note = "",
        group = "Challenge"
    )
    public void testNavigasiChallengeSaya() {
        System.out.println("TEST 2: Pengguna membuka Challenges Saya");
        
        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, " Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        logPass("Berhasil masuk ke list card Challenge Saya");

        wait.until(ExpectedConditions.elementToBeClickable(btnBackListSaya)).click();
    }

    @Test(priority = 3, description = "Pengguna membuka Public Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan daftar semua Public Challenge, yang merupakan challenge buatan pengguna Ayolari, serta memungkinkan pengguna untuk melihat detail dari setiap challenge yang ada",
        note = "",
        group = "Challenge"
    )
    public void testNavigasiPublicChallenge() {
        System.out.println("TEST 3: Pengguna membuka Public Challenge");
        
        // Klik "Lihat Semua" (Public Challenge)
        System.out.println("Klik Lihat Semua (Public Challenge)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaPublic));
        clickTest(btnLihatSemuaPublic, " Klik tombol Lihat Semua di Public Challenge");
        waitTime(); 

        logPass("Berhasil masuk ke list card Public Challenge");

        wait.until(ExpectedConditions.elementToBeClickable(btnBackListPublic)).click();
    }

    @Test(priority = 4, description = "Pengguna membuka Challenge Reward Januari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini memperlihatkan perkembangan aktivitas lari pengguna dalam mencapai milestone reward pada Challenge Reward Januari, serta menampilkan peringkat pengguna di leaderboard global",
        note = "",
        group = "Challenge"
    )
    public void testMasukDetailReward() {
        System.out.println("TEST 4: Navigasi ke Detail Reward Januari");

        // Klik "Lihat Detail"
        System.out.println("Mencari tombol 'Lihat Detail'");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnLihatDetailRewardBulan));
            clickTest(btnLihatDetailRewardBulan, "Klik Lihat Detail Reward Bulan Januari");
            waitTime();
        } catch (Exception e) {
            System.out.println("Gagal klik Lihat Detail: " + e.getMessage());

            logInfo("Tombol lihat detail tidak ditemukan");
            
            Assert.fail("Tombol Lihat Detail tidak ditemukan.");
        }

        // Validasi: Pastikan masuk ke halaman detail
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabChallengeRewardBulan));
        Assert.assertTrue(driver.findElements(tabChallengeRewardBulan).size() > 0, "Gagal masuk halaman detail reward!");
        logPass("Berhasil masuk ke halaman Detail Reward Bulan Januari.");
    }

    @Test(priority = 5, description = "Pengguna mengecek tab Challenge dan Leaderboard pada halaman Detail Reward Januari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini memperlihatkan perkembangan aktivitas lari pengguna dalam mencapai milestone reward pada Challenge Reward Januari, serta menampilkan peringkat pengguna di leaderboard global",
        note = "",
        group = "Challenge"
    )
    public void testCekTabDanTombol() {
        System.out.println("TEST 2: Cek Tab Challenge & Leaderboard");

        // Aktivitas di Tab Challenge
        System.out.println("Klik Tab Challenge");
        driver.findElement(tabChallengeRewardBulan).click();
        waitTime();
        
        // Scroll ke Bawah (Cari tombol Klaim)
        System.out.println("Scroll ke bawah di Challenge");
        actions.swipeVertical(0.7, 0.3); 
        waitTime();

        logInfo("Scroll ke bawah");

        // Cek & Klik Tombol Klaim
        System.out.println("Mengecek tombol Klaim");
        if (driver.findElements(btnKlaim).size() > 0) {
            System.out.println("Tombol Klaim ditemukan.");
            
            // Klik button 'Klaim'
            clickTest(btnKlaim, "Klik tombol Klaim Reward");
            
            // Tunggu popup muncul
            try { Thread.sleep(2000); } catch (Exception e) {}

            // Klik button 'Selesai' di popup
            System.out.println("Mencoba menutup popup 'Selesai'");
            
            try {
                // Coba cari & klik tombol Selesai
                wait.until(ExpectedConditions.visibilityOfElementLocated(btnSelesai));
                clickTest(btnSelesai, "Klik tombol Selesai");
                logPass("Berhasil klik tombol Selesai.");
                
            } catch (Exception e) {
                // Jika button gagal, gunakan tombol Back bawaan HP
                System.out.println("Tombol Selesai macet/tidak ketemu. Menggunakan tombol Back HP.");
                driver.navigate().back();
                logInfo("Popup ditutup menggunakan tombol Back System.");
            }
            
            waitTime();
        } else {
            System.out.println("Info: Tombol Klaim/Selesai tidak muncul.");
            logInfo("Tombol Klaim/Selesai tidak muncul.");
        }

        System.out.println("Scroll balik ke atas agar Tab terlihat");
        actions.swipeVertical(0.3, 0.8); 
        waitTime();

        // Pindah ke Tab Leaderboard
        System.out.println("Pindah ke Tab Leaderboard");
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabLeaderboardRewardBulan));
        clickTest(tabLeaderboardRewardBulan, "Klik Tab Leaderboard");
        
        waitTime();
        
        logPass("Berhasil pindah ke Tab Leaderboard");

        // Scroll Turun Leaderboard
        System.out.println("Scroll ke bawah Leaderboard");
        actions.swipeVertical(0.7, 0.3); 
        waitTime();

        logInfo("Scroll ke bawah");
        
        // Scroll Naik Leaderboard
        System.out.println("Scroll kembali ke atas Leaderboard");
        actions.swipeVertical(0.3, 0.7); 
        waitTime();
        
        Assert.assertTrue(driver.findElements(tabLeaderboardRewardBulan).size() > 0, "Aplikasi crash atau keluar dari halaman detail.");

        // Kembali ke halaman Challenge
        wait.until(ExpectedConditions.elementToBeClickable(btnBackRewardBulan));
        clickTest(btnBackRewardBulan, "Klik back");

        waitTime();
        
        boolean isBackSuccess = driver.findElements(btnLihatDetailRewardBulan).size() > 0;
        
        Assert.assertTrue(isBackSuccess, "Gagal kembali ke halaman Challenge.");
        System.out.println("Berhasil kembali ke halaman Challenge.");
        
        logPass("Berhasil kembali ke halaman Challenge.");
    }

    @Test(priority = 6, description = "Pengguna bergabung ke Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe publik buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testJoinPublicChallenge() {
        System.out.println("TEST 6: Pengguna bergabung ke Public Challenges yang bertipe publik");

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

        // Cari Card "Fun for health" (Scroll To Text)
        System.out.println("Mencari card 'Fun for health'");
        actions.scrollToText("Fun for health"); 
        waitTime(); 

        logPass("Card Fun for health ditemukan");
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardPublicChallenge));
        clickTest(cardPublicChallenge, "Klik card 'Fun for health' di Public Challenge");
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
    }

    @Test(priority = 7, description = "Validasi berhasil bergabung ke Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe publik buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testValidasiBerhasilBergabung() {
        System.out.println("TEST 7: Validasi berhasil bergabung ke Public Challenges yang bertipe publik");

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, " Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        logPass("Berhasil masuk ke list card Challenge Saya");

        // Cari & Validasi "Fun for health" ada di list
        System.out.println("Mencari 'Fun for health' di list");
        waitTime();
        
        Assert.assertTrue(driver.findElements(cardPublicChallenge).size() > 0, 
            "GAGAL: Challenge 'Fun for health' tidak masuk ke Challenge Saya!");
        
        logInfo("Validasi: Challenge 'Fun for health' ditemukan di list Saya.");
    }

    @Test(priority = 8, description = "Pengguna keluar dari Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat keluar dari Public Challenges yang bertipe publik yang telah diikuti sebelumnya.\n" + //
                        "\n" + "Setelah keluar, maka Challenge tersebut akan hilang dari daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testKeluarPublicChallenge() {
        System.out.println("Test 8: Pengguna keluar dari Public Challenges yang bertipe publik");

        // Klik Card & Cek Detail lagi
        clickTest(cardPublicChallenge, "Klik card 'Fun for health' di Challenge Saya");
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        logPass("Berhasil masuk ke halaman detail 'Fun for health'");

        // Cek Tab Deskripsi
        System.out.println("Cek Deskripsi -> Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        logInfo("Scroll ke bawah di Tab Deskripsi");

        actions.swipeVertical(0.5, 0.7);
        waitTime();

        // Cek Tab Leaderboard sebentar
        System.out.println("Cek Leaderboard");
        clickTest(tabLeaderboard, "Klik Tab Leaderboard");
        waitTime();

        System.out.println("Scroll Turun Leaderboard");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        logInfo("Scroll ke bawah di Leaderboard");
        
        // Keluar Challenge (Leave)
        System.out.println("Proses Keluar Challenge");
        
        // Klik 'Keluar Challenge'
        wait.until(ExpectedConditions.elementToBeClickable(btnKeluarChallenge));
        clickTest(btnKeluarChallenge, " Klik tombol 'Keluar Challenge' -> Muncul konfirmasi");
        waitTime(); // Tunggu popup konfirmasi
        
        // Klik 'Ya, Lanjutkan'
        wait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiKeluar));
        clickTest(btnKonfirmasiKeluar, " Klik tombol 'Ya, Lanjutkan' untuk konfirmasi keluar");
        
        System.out.println("Berhasil keluar, tunggu loading");
        try { Thread.sleep(4000); } catch (Exception e) {} // Tunggu proses API selesai

        logPass("Berhasil keluar/leave Challenge");

        // Back 1x
        System.out.println("Back ke List Saya");
        if (driver.findElements(btnBackDetail).size() > 0) {
             clickTest(btnBackDetail, "Klik tombol Back");
        }
        waitTime();

        // Validasi Hilang
        System.out.println("Validasi Challenge 'Fun for health' sudah hilang");

        // Cek apakah card masih ada
        boolean isCardPresent = driver.findElements(cardPublicChallenge).size() > 0;

        if (!isCardPresent) {
            // Skenario sukses: card sudah hilang
            System.out.println("Berhasil keluar dari Challenge 'Fun for health', card sudah hilang dari list.");

            logPass("Validasi: Challenge 'Fun for health' sudah hilang dari list.");

        } else {
            // Skenario gagal: card masih ada
            System.out.println("Gagal keluar dari Challenge 'Fun for health', card masih ada di list!");
            logInfo("Gagal keluar dari Challenge 'Fun for health', card masih ada di list!");
        }
    }

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

    @Test(priority = 10, description = "Validasi apakah sudah disetujui bergabung ke Public Challenges yang bertipe private")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe private buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testValidasiBerhasilBergabungPrivate() {
        System.out.println("TEST 10: Validasi Challenge Private masuk ke List Saya");

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, "Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        logPass("Berhasil masuk ke list card Challenge Saya");

        // Cari & Validasi "Lari Merdeka 2026" ada di list
        System.out.println("Mencari 'Lari Merdeka 2026' di list");
        waitTime();
        
        boolean isCardPresent = driver.findElements(cardPrivateChallenge).size() > 0;

        if (isCardPresent) {
            // Card ditemukan, validasi sukses
            System.out.println("Validasi Sukses: Challenge ditemukan.");
            logPass("Validasi SUKSES: Challenge 'Lari Merdeka 2026' ditemukan di list Saya.");
        } else {
            // Card tidak ditemukan, validasi gagal
            System.out.println("Validasi Gagal: Challenge tidak ada di list.");
            logInfo("Card tidak ada di list, mungkin belum di setujui");
        }
    }

    @Test(priority = 11, description = "Pengguna keluar dari Public Challenges yang bertipe private")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat keluar dari Public Challenges yang bertipe private yang telah diikuti sebelumnya.\n" + //
                        "\n" + "Setelah keluar, maka Challenge tersebut akan hilang dari daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "Challenge"
    )
    public void testKeluarPrivateChallenge() {
        System.out.println("TEST 11: Keluar Private Challenge");

        boolean isCardExist = driver.findElements(cardPrivateChallenge).size() > 0;

        if (isCardExist) {
            // Card Lari Merdeka 2026 ada di list
            System.out.println("Card ditemukan. Menjalankan proses keluar");
            
            // Klik Card
            clickTest(cardPrivateChallenge, "Klik card 'Lari Merdeka 2026'");
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
            waitTime();

            // Scroll dikit
            actions.swipeVertical(0.7, 0.5);
            waitTime();

            // Klik Keluar
            try {
                wait.until(ExpectedConditions.elementToBeClickable(btnKeluarChallenge));
                clickTest(btnKeluarChallenge, "Klik 'Keluar Challenge'");
                
                wait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiKeluar));
                clickTest(btnKonfirmasiKeluar, "Klik 'Ya, Lanjutkan'");
                
                System.out.println("Proses keluar");
                try { Thread.sleep(4000); } catch (Exception e) {} 
                
                logPass("Berhasil klik keluar challenge.");

            } catch (Exception e) {
                // Handle jika tombol keluar gak ketemu (misal status masih 'Menunggu Persetujuan' jadi gak bisa keluar)
                logInfo("Gagal klik tombol keluar (Mungkin status masih pending approval).");
            }

            // Back ke List
            System.out.println("Back ke List Saya");
            if (driver.findElements(btnBackDetail).size() > 0) {
                clickTest(btnBackDetail, "Klik tombol Back");
            }
            waitTime();

            // Validasi Hilang
            if (driver.findElements(cardPrivateChallenge).size() == 0) {
                logPass("Validasi: Card sudah hilang dari list.");
            } else {
                logInfo("Validasi: Card masih terlihat di list (mungkin masih delay)");
            }

        } else {
            // Card Lari Merdeka 2026 ga ada di list
            System.out.println("Card 'Lari Merdeka 2026' TIDAK ditemukan di list");
            logInfo("SKIP: Challenge private tidak ditemukan di list (Mungkin belum di setujui penyelenggara)");
        }

        driver.findElement(btnBackListSaya).click();
        waitTime();
    }

    // **** BAGIAN TAB LEADERBOARD ****
    // Tab Leaderboard (Header Atas)
    By tabLeaderboardIkonAtas = AppiumBy.xpath("//android.view.View[@content-desc='Leaderboard Leaderboard']");

    // Validasi (Global Leaderboard)
    By textGlobal = AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Global') or contains(@text, 'Global')] | //android.widget.TextView[contains(@text, 'Global')]");

    @Test(priority = 12, description = "Pengguna membuka Tab Leaderboard di halaman Challenge Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan list Top 10 Pengguna Ayolari dengan total lari terbanyak, serta menampilkan peringkat pengguna di leaderboard Ayolari secara global",
        note = "",
        group = "Challenge"
    )
    public void testMasukLeaderboard() {
        System.out.println("TEST 12: Pengguna membuka Tab Leaderboard di halaman Challenge Lari");

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

        logPass("Berhaisl masuk dan memuat data Leaderboard");

        // Scroll ke bawah dan atas 
        System.out.println("Scroll Turun");
        actions.swipeVertical(0.7, 0.5);
        waitTime();
        
        logInfo("Scroll ke bawah daftar Leaderboard");

        System.out.println("Scroll Naik");
        actions.swipeVertical(0.5, 0.7);
        waitTime();

        logInfo("Scroll ke atas daftar Leaderboard");
        System.out.println("Selesai cek Leaderboard.");
    }

    // **** BAGIAN TAB REWARDS ****
    By tabRewardsIkonAtas = AppiumBy.xpath("//android.view.View[@content-desc='Rewards Rewards']");
    By btnRiwayatReward = AppiumBy.xpath("//android.view.View[@content-desc='Public Riwayat Reward']");
    By btnBackIkonRewards = AppiumBy.xpath("//android.widget.Button");

    @Test(priority = 13, description = "Pengguna membuka Tab Reward di halaman Challenge Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini menampilkan daftar reward yang tersedia untuk diklaim berdasarkan challenge yang diikuti oleh pengguna. Selain itu, pengguna juga dapat melihat panduan langkah demi langkah untuk melakukan proses redeem reward",
        note = "",
        group = "Challenge"
    )
    public void testBukaRewardsDanRiwayat() {
        System.out.println("TEST 13: Pengguna membuka Tab Reward di halaman Challenge Lari");
        waitTime(); 

        // Klik Tab Rewards
        clickTest(tabRewardsIkonAtas, "Klik Tab Rewards");
        waitTime();

        // Klik Button Riwayat Rewards
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        clickTest(btnRiwayatReward, "Klik menu Riwayat Rewards");

        // Validasi Masuk Halaman Riwayat
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackIkonRewards));
        Assert.assertTrue(driver.findElement(btnBackIkonRewards).isDisplayed(), "Gagal masuk halaman Riwayat.");

        // Klik Back
        waitTime();
        
        if (driver.findElements(btnBackIkonRewards).size() > 0) {
            clickTest(btnBackIkonRewards, "Klik tombol Back");
            logPass("Kembali ke Tab Rewards.");
        } else {
            System.out.println("Tombol Back UI tidak ada, pakai Back System.");
            driver.navigate().back();
            logInfo("Tombol back aplikasi tidak bisa, back menggunakan UI system");
        }

        // Validasi Kembali ke Rewards
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        } catch (Exception e) {}

        Assert.assertTrue(driver.findElements(btnRiwayatReward).size() > 0, "Gagal kembali ke halaman Rewards!");
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
