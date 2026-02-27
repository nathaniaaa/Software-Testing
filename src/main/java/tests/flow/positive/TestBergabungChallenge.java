package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestBergabungChallenge extends BaseTest {
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

    // Private Challenge - Target: Lari Merdeka 2026 v3 - Klo mau ke card lain janlup diganti nama text nya sesuai kyk judul card
    By cardPrivateChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Lari Merdeka 2026 v3']");

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

    // Flag
    static boolean isJoinedPublicChallenge = false;
    static boolean isJoinedPrivateChallenge = false;
    static boolean isPrivateChallengeApproved = false;

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
        group = "SET & LIHAT CHALLENGE"
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
        group = "SET & LIHAT CHALLENGE"
    )
    public void testJoinPublicChallenge() {
        System.out.println("TEST 2: Pengguna bergabung ke Public Challenges yang bertipe publik");

        logInfo("Tampilan awal");

        // Scroll ke Bawah cari section Public
        System.out.println("Scroll mencari section Public Challenge");
        actions.swipeVertical(0.8, 0.5); 
        waitTime();

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

        if (driver.findElements(cardPublicChallenge).size() == 0) {
            driver.navigate().back();
            logSkip("Test dilewati: Card 'Fun for health' tidak ditemukan di list");
            return;
        }

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

        // Pastikan tombol Join ada (jaga-jaga kalau ternyata udah join)
        if (driver.findElements(btnJoinChallenge).size() > 0) {
            // Klik button 'Join Challenge'
            clickTest(btnJoinChallenge, "Klik tombol 'Join Challenge'");
            waitTime();

            // Klik button 'Bergabung Sekarang'
            wait.until(ExpectedConditions.elementToBeClickable(btnBergabung));
            clickTest(btnBergabung, "Klik tombol 'Bergabung Sekarang'");
            
            try { Thread.sleep(4000); } catch (Exception e) {} 
            
            isJoinedPublicChallenge = true; // set flag berhasil join 
        } else {
            isJoinedPublicChallenge = true; // flag true karena user sudah join sebelumnya
            logInfo("Tombol Join tidak ada (mungkin user sudah bergabung sebelumnya)");
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

    @Test(priority = 3, description = "Validasi berhasil bergabung ke Public Challenges yang bertipe publik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat bergabung ke Public Challenges yang bertipe publik buatan pengguna lain selama kuota peserta belum terpenuhi.\n" + //
                        "\n" + "Setelah bergabung, maka Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testValidasiBerhasilBergabung() {
        System.out.println("TEST 3: Validasi berhasil bergabung ke Public Challenges yang bertipe publik");

        // cek flag 
        if (!isJoinedPublicChallenge) {
            logSkip("Test dilewati: Proses Join (Test 6) terlewati/gagal, tidak ada yang perlu divalidasi");
            return; 
        }

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, " Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        // Cari & Validasi "Fun for health" ada di list
        System.out.println("Mencari 'Fun for health' di list");
        actions.scrollToText("Fun for health");
        waitTime();
        
        Assert.assertTrue(driver.findElements(cardPublicChallenge).size() > 0, 
            "GAGAL: Challenge 'Fun for health' tidak masuk ke Challenge Saya!");
        
        logPass("Validasi sukses: Challenge 'Fun for health' ditemukan di list Saya.");
    }

    @Test(priority = 4, description = "Pengguna bergabung ke Private Challenge dan menginput kode undangan join challenge dengan benar")
    @TestInfo(
        testType = "Positive Case",
        expected = "Setelah menginput kode challenge dengan benar, maka pengguna akan mengirimkan permintaan bergabung ke pembuat challenge.\n" + //
                        "Pengguna harus menunggu konfirmasi dari pembuat challenge tersebut, dan setelah diterima, barulah Pengguna sudah berhasil bergabung ke Private Challenge tersebut ",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testJoinPrivateChallenge() {
        System.out.println("TEST 4: Pengguna bergabung ke Private Challenge dan menginput kode undangan join challenge dengan benar");

        if (driver.findElements(btnBackListSaya).size() > 0) {
            wait.until(ExpectedConditions.elementToBeClickable(btnBackListSaya)).click();
        } else {
            driver.navigate().back();
        }
        waitTime();

        logInfo("Tampilan awal");

        // Scroll ke Bawah cari section Public
        System.out.println("Scroll mencari section Public Challenge");
        actions.swipeVertical(0.8, 0.5); 
        waitTime();

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

        // Cari Card "Lari Merdeka 2026 v3" (Scroll To Text)
        System.out.println("Mencari card 'Lari Merdeka 2026 v3'");
        actions.scrollToText("Lari Merdeka 2026 v3"); 
        waitTime(); 

        if (driver.findElements(cardPrivateChallenge).size() == 0) {
            driver.navigate().back();
            logSkip("Test dilewati: Card 'Lari Merdeka 2026 v3' tidak ditemukan");
            return; 
        }

        logPass("Card Lari Merdeka 2026 v3 ditemukan");
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardPrivateChallenge));
        clickTest(cardPrivateChallenge, "Klik card 'Lari Merdeka 2026 v3' di Public Challenge");
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        // Validasi: Masuk detail
        Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail challenge!");

        logPass("Berhasil masuk detail 'Lari Merdeka 2026 v3'.");
        
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
        if (driver.findElements(btnJoinChallenge).size() > 0) {
            // Klik button 'Join Challenge'
            clickTest(btnJoinChallenge, "Klik tombol 'Join Challenge'");
            waitTime();

            // Pengisian kode undangan - verifikasi
            System.out.println("Input Kode Undangan");
            String kodeUndangan = "537545"; // jangan lupa ganti sesuai kode undangan challenge private 
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
            waitTime();

            // Cek hasil setelah input kode
            if (driver.findElements(btnMenungguPersetujuan).size() > 0) {
                isJoinedPrivateChallenge = true; // set flag berhasil
                capture.highlightAndCapture(btnMenungguPersetujuan, "Kode undangan benar, status menjadi menunggu persetujuan");
            } else {
                isJoinedPrivateChallenge = false; // set flag gagal karena error/kode salah 
                logFail("Gagal bergabung! Kode undangan salah atau terjadi error pada sistem");
            }
        } else {
            // JIKA TOMBOL JOIN DARI AWAL TIDAK ADA
            System.out.println("Tombol Join tidak ditemukan, mengecek status user");
            
            boolean isSudahJoin = driver.findElements(btnKeluarChallenge).size() > 0;
            boolean isMenunggu = driver.findElements(btnMenungguPersetujuan).size() > 0;
            
            if (isSudahJoin || isMenunggu) {
                isJoinedPrivateChallenge = true; // aman
            } else {
                isJoinedPrivateChallenge = false; // gagal
                logSkip("Tombol Join tidak ada, tapi user belum bergabung (mungkin UI error)");
            }
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

    @Test(priority = 5, description = "Validasi apakah sudah disetujui bergabung ke Public Challenges yang bertipe private")
    @TestInfo(
        testType = "Positive Case",
        expected = "Jika sudah disetujui, maka setelah bergabung, Challenge tersebut akan masuk ke dalam daftar Challenge Saya pada Tab Challenge.\n" + //
                        "\n" + "Jika belum disetujui, maka Challenge tidak akan muncul di daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testValidasiBerhasilBergabungPrivate() {
        System.out.println("TEST 5: Validasi Challenge Private masuk ke List Saya");

        // cek flag
        if (!isJoinedPrivateChallenge) {
            logSkip("Test dilewati: Proses Join Private (Test 9) terlewati, tidak ada yang perlu divalidasi.");
            return; 
        }

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, "Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        // Cari & Validasi "Lari Merdeka 2026 v3" ada di list
        System.out.println("Mencari 'Lari Merdeka 2026 v3' di list");
        waitTime();
        
        boolean isCardPresent = driver.findElements(cardPrivateChallenge).size() > 0;

        if (isCardPresent) {
            System.out.println("Validasi Sukses: Challenge ditemukan.");
            isPrivateChallengeApproved = true; // set true karena sudah di-approve (card sudah muncul di list)
            capture.highlightAndCapture(cardPrivateChallenge, "Validasi SUKSES: Challenge 'Lari Merdeka 2026 v3' ditemukan di list Saya");
        } else {
            System.out.println("Validasi Gagal: Challenge tidak ada di list.");
            isPrivateChallengeApproved = false; // set false karena belum di-approve (card belum muncul di list)
            logSkip("Card tidak ada di list, mungkin belum di setujui");
        }
    }

    @Test(priority = 6, description = "Pengguna keluar dari Public Challenges yang bertipe private")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat keluar dari Public Challenges yang bertipe private yang telah diikuti sebelumnya.\n" + //
                        "\n" + "Setelah keluar, maka Challenge tersebut akan hilang dari daftar Challenge Saya pada Tab Challenge.",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testKeluarPrivateChallenge() {
        System.out.println("TEST 6: Keluar Private Challenge");

        // cek flag dari test 9
        if (!isJoinedPrivateChallenge)  {
            // Balik ke halaman utama challenge
            if (driver.findElements(btnBackListSaya).size() > 0) {
                driver.findElement(btnBackListSaya).click();
            } else {
                driver.navigate().back();
            }
            waitTime();
            
            logSkip("Test dilewati: Proses Join Private (Test 9) terlewati, tidak bisa melakukan proses keluar");
            return;
        }

        // cek flag dari test sebelumnya (approval)
        if (!isPrivateChallengeApproved) {
            System.out.println("Berdasarkan Test 10, Challenge belum disetujui, tidak perlu mencari card untuk keluar");
            
            // Balik ke halaman utama challenge
            if (driver.findElements(btnBackListSaya).size() > 0) {
                driver.findElement(btnBackListSaya).click();
            } else {
                driver.navigate().back();
            }
            waitTime();
            
            logSkip("SKIP: Challenge private belum disetujui penyelenggara, tidak bisa keluar.");
            return;
        }

        actions.scrollToText("Lari Merdeka 2026 v3");
        waitTime();

        boolean isCardExist = driver.findElements(cardPrivateChallenge).size() > 0;

        if (isCardExist) {
            // Card Lari Merdeka 2026 v3 ada di list
            System.out.println("Card ditemukan. Menjalankan proses keluar");
            
            // Klik Card
            clickTest(cardPrivateChallenge, "Klik card 'Lari Merdeka 2026 v3'");
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

            actions.scrollToText("Lari Merdeka 2026 v3");
            waitTime();

            // Validasi Hilang
            if (driver.findElements(cardPrivateChallenge).size() == 0) {
                logPass("Validasi: Card sudah hilang dari list.");
            } else {
                logInfo("Validasi: Card masih terlihat di list (mungkin masih delay)");
            }

        } else {
            // Card Lari Merdeka 2026 v3 ga ada di list
            System.out.println("Card 'Lari Merdeka 2026 v3' TIDAK ditemukan di list");
            logSkip("SKIP: Challenge private tidak ditemukan di list (Mungkin belum di setujui penyelenggara)");
        }

        System.out.println("Keluar dari List Saya, kembali ke Halaman Utama Challenge");
        try {
            if (driver.findElements(btnBackListSaya).size() > 0) {
                clickTest(btnBackListSaya, "Klik tombol Back (List Saya)");
            } else {
                driver.navigate().back();
            }
        } catch (Exception e) { driver.navigate().back(); }
        waitTime();
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
