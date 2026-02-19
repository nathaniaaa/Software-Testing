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

    // Card pertama di challenge saya (halaman challenge) - utk validasi apakah ada challenge yang diikuti / tdk
    By cardPertamaChallengeSaya = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[3]/android.view.View[1]");

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
    
    static boolean isKeluarChallengeSukses = false;

    // Test Cases
    @Test(priority = 1, description = "Pengguna bergabung ke challenge private dengan menginput kode undangan yang salah")
    @TestInfo(
        testType = "Negative Case",
        expected = "Pengguna mencoba bergabung ke challenge private dengan memasukkan kode undangan yang salah. Akibatnya, pengguna tidak dapat bergabung dan akan muncul pesan error sebagai penanda kesalahan",
        note = "",
        group = "Challenge"
    )
    public void testJoinPrivateChallenge() {
        System.out.println("TEST 1: Pengguna bergabung ke challenge private dengan menginput kode undangan yang salah");

        // Klik bottom navigation Challenge
        wait.until(ExpectedConditions.elementToBeClickable(navChallenge)).click();;

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

        if (driver.findElements(cardPrivateChallenge).size() == 0) {
            driver.navigate().back();
            logSkip("Test dilewati: Card 'Lari Merdeka 2026' tidak ditemukan di list.");
        }

        logPass("Card Lari Merdeka 2026 ditemukan");
        
        // Klik Card
        wait.until(ExpectedConditions.elementToBeClickable(cardPrivateChallenge));
        clickTest(cardPrivateChallenge, "Klik card 'Lari Merdeka 2026' di Public Challenge");
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        // Validasi: Masuk detail
        Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail challenge!");

        logPass("Berhasil masuk detail 'Lari Merdeka 2026'.");
        
        // JOIN CHALLENGE 
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

        // Buat penanda bug
        boolean isBugFound = false; 
        String bugMessage = "";

        // Cek apakah tombol sukses (menunggu persetujuan) muncul
        boolean isSuccessButtonMuncul = driver.findElements(btnMenungguPersetujuan).size() > 0 || driver.findElements(btnBergabung).size() > 0;

        if (!isSuccessButtonMuncul) {
            // Skenario benar -> kode yg di input salah + ditolak
            System.out.println("Kode undangan salah, tidak bisa bergabung");
            logPass("Kode undangan salah, tidak bisa bergabung");
        } else {
            // Skenario bug: kode yg di input salah tpi diterima
            System.out.println("Kodenya salah kok berhasil?");
            
            // Tandai bahwa ada bug, catat di HTML, tapi jangan stop script
            isBugFound = true;
            bugMessage = "Sistem menerima kode undangan yang salah, tetapi muncul button 'Menunggu Persetujuan'";
            logFail(bugMessage); 
        }

        // Tutup modal input kode undangan
        actions.tapAtScreenRatio(0.5, 0.2); 
        waitTime();

        // Back 2x
        System.out.println("Back ke List Public");
        try {
            if (driver.findElements(btnBackDetail).size() > 0) clickTest(btnBackDetail, "Klik tombol Back Detail");
        } catch (Exception e) { driver.navigate().back(); }
        waitTime();
        
        System.out.println("Back ke Halaman Challenge");
        if (driver.findElements(btnBackListPublic).size() > 0) {
            clickTest(btnBackListPublic, "Klik tombol Back");
        } else {
            driver.navigate().back();
            logInfo("Tombol back tidak ditemukan, menggunakan back UI Handphone");
        }
        waitTime();

        if (isBugFound) {
            Assert.fail(bugMessage); 
        }
    }

    @Test(priority = 2, description = "Pengguna keluar dari Challenge Lari yang telah diikuti")
    @TestInfo(
        testType = "Negative Case",
        expected = "Selama Challenge Lari masih berlangsung, pengguna dapat memilih untuk keluar dari challenge yang telah diikuti.",
        note = "",
        group = "Challenge"
    )
    public void testKeluarPublicChallenge() {
        System.out.println("Test 2: Pengguna keluar dari Challenge Lari yang telah diikuti");

        // Cek apakah user ada mengikuti challenge? kalau engga, skip
        if (driver.findElements(cardPertamaChallengeSaya).size() == 0) {
            logSkip("Test dilewati: User tidak mengikuti challenge apapun (List kosong).");
        }

        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        waitTime();
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, " Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        logPass("Berhasil masuk ke list card Challenge Saya");

        // cari card
        System.out.println("Mencari card 'Fun for health' di daftar Challenge Saya");
        actions.scrollToText("Fun for health"); 
        waitTime();

        // Cek apakah card "Fun for health" ada di list Challenge Saya, kalau engga skip (biar ga crash)
        if (driver.findElements(cardPublicChallenge).size() == 0) {
            driver.navigate().back();
            logSkip("Test dilewati: Challenge 'Fun for health' tidak ditemukan di daftar Challenge Saya.");
        }

        // Klik Card
        clickTest(cardPublicChallenge, "Klik card 'Fun for health' di Challenge Saya");
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
        waitTime();

        logPass("Berhasil masuk ke halaman detail 'Fun for health'");

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
        actions.scrollToText("Fun for health"); // Coba cari 
        waitTime();

        // Cek apakah card masih ada
        boolean isCardPresent = driver.findElements(cardPublicChallenge).size() > 0;

        if (!isCardPresent) {
            // Skenario sukses: card sudah hilang
            System.out.println("Berhasil keluar dari Challenge 'Fun for health', card sudah hilang dari list.");
            isKeluarChallengeSukses = true;

            logPass("Validasi: Challenge 'Fun for health' sudah hilang dari list.");
        } else {
            // Skenario gagal: card masih ada
            System.out.println("Gagal keluar dari Challenge 'Fun for health', card masih ada di list!");
            logInfo("Gagal keluar dari Challenge 'Fun for health', card masih ada di list!");
        }
    }

    @Test(priority = 3, description = "Pengguna kembali join ke Challenge Lari setelah sebelumnya telah keluar dari challenge tersebut")
    @TestInfo(
        testType = "Negative Case",
        expected = "Setelah keluar dari Challenge Lari, pengguna masih dapat bergabung kembali selama masih ada kuota challenge tersebut masih tersedia",
        note = "",
        group = "Challenge"
    )
    public void testJoinPublicChallenge() {
        System.out.println("TEST 3: Pengguna kembali join ke Challenge Lari setelah sebelumnya telah keluar dari challenge tersebut");

        // Kalau test 2 gagal -> test 3 di skip karena kondisi belum keluar challenge nya belum terpenuhi, jadi ga bisa join ulang
        if (!isKeluarChallengeSukses) {
            logSkip("Test dilewati: Karena Gagal atau Skip pada proses Keluar Challenge (Test 2), maka tidak bisa melakukan re-join.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(btnBackListSaya)).click();

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

        if (driver.findElements(cardPublicChallenge).size() == 0) {
            driver.navigate().back();
            logSkip("Test dilewati: Challenge 'Fun for health' tidak ditemukan di daftar Public Challenge");
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
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
