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

    // Ikon Beranda (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");

    // Lihat Semua - Tombol di Challenge Saya
    By btnLihatSemuaSaya = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]");
    
    // Lihat Semua - Tombol di Public Challenge
    By btnLihatSemuaPublic = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");

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

    // **** Bagian Reward Bulan ****
    // Lihat Detail Reward Bulan Bulan
    By btnLihatDetailRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='Lihat Detail']");
    
    // Tab Menu (Challenge & Leaderboard)
    By tabChallengeRewardBulan = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-challenge')]");
    By tabLeaderboardRewardBulan = AppiumBy.xpath("//*[contains(@resource-id, 'trigger-leaderboard') or contains(@text, 'Leaderboard') or @content-desc='Leaderboard']");

    // Tombol Aksi - Reward
    By btnKlaim = AppiumBy.xpath("(//android.widget.Button[@text='Klaim'])[1]");
    By btnSelesai = AppiumBy.xpath("//android.widget.Button[@text='Selesai']");

    // Tombol Back 
    By btnBackRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='telkomsel-challenge']/android.widget.Button");

    // Tombol Back (Bawaan Aplikasi)
    By btnBackHeader1 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.widget.Button");
    By btnBackHeader2 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[1]/android.widget.Button[1]");
    By btnBackList = AppiumBy.xpath("//android.view.View[@content-desc=\"joined\"]");

    // Link 'Lihat Semua' 
    // Challenge yang Diikuti -> Challenge Saya
    By textLihatSemuaChallengeYangDiikuti = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[1]");
    
    // Challenges (Public Challenge) -> Halaman Navigasi Challenge
    By textLihatSemuaChallenges = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");

    By textLihatSemuaRiwayatLari = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");

    // container public challenge
    By wadahPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']/following-sibling::android.view.View[1]");
    
    // Card Public Challenge - dari beranda
    By cardPublicChallengeBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']/following-sibling::android.view.View[1]");

    // Locator untuk Teks Empty State
    By textBelumAdaChallenge = AppiumBy.xpath("//*[contains(@text, 'Belum ada challenge') or contains(@text, 'Tidak ada challenge')]");
    By textBelumAdaEvent = AppiumBy.xpath("//*[contains(@text, 'Belum ada event') or contains(@text, 'Tidak ada event')]");
    By textBelumAdaRiwayatLari = AppiumBy.xpath("//*[contains(@text, 'Belum ada riwayat') or contains(@text, 'Tidak ada riwayat')]");

    // locator header (untuk highlight section)
    By headerChallenges = AppiumBy.xpath("//android.widget.TextView[@text='Challenges']");
    By headerChallengeSaya = AppiumBy.xpath("//android.widget.TextView[@text='Challenge yang Diikuti']");
    By headerTotalLari = AppiumBy.xpath("//android.widget.TextView[@text='Total Lari Harian']");
    By headerPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']");
    By headerEventLari = AppiumBy.xpath("//android.widget.TextView[@text='Event Lari']");
    By headerRiwayatLari = AppiumBy.xpath("//android.widget.TextView[@text='Riwayat Lari']");

    // Test Cases
    @Test(priority = 1, description = "Pengguna membuka Challenges Saya di tab challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan semua challenge yang telah diikuti maupun dibuat oleh Pengguna",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testNavigasiChallengeSaya() {
        System.out.println("TEST 1: Pengguna membuka Challenges Saya di tab challenge");

        actions.scrollToTop(); 

        // Masuk Menu Challenge
        clickTest(navChallenge, "Klik ikon Challenge di Bottom Navigation");
        waitTime();
        
        // Klik "Lihat Semua" (Challenge Saya)
        System.out.println("Klik Lihat Semua (Challenge Saya)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaSaya));
        clickTest(btnLihatSemuaSaya, " Klik tombol Lihat Semua di Challenge Saya");
        waitTime(); 

        logPass("Berhasil masuk ke list card Challenge Saya");

        wait.until(ExpectedConditions.elementToBeClickable(btnBackListSaya)).click();
    }

    @Test(priority = 2, description = "Pengguna membuka Challenges Saya di tab beranda")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan semua challenge yang telah diikuti maupun yang telah dibuat oleh Pengguna",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    ) 
    public void testNavigasiChallengeSayaBeranda() {
        System.out.println("Test 2: Pengguna membuka Challenges Saya di tab beranda");
        waitTime();

        clickTest(navBeranda, "Klik ikon Beranda di Bottom Navigation");
        waitTime();

        if (driver.findElements(textLihatSemuaChallengeYangDiikuti).size() > 0) {
                clickTest(textLihatSemuaChallengeYangDiikuti, "Klik 'Lihat Semua' (Challenge Saya)");
                
                try { wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.view.View"))); } catch (Exception e) {}

                waitTime();
                clickBack();
                logPass("Berhasil kembali ke Beranda dari list Challenge Saya.");
            } else {
                // Jaga-jaga kalau UI-nya ada tapi tombol lihat semuanya ga ngerender
                logSkip("Test dilewati: Challenge ada, tapi Tombol 'Lihat Semua' tidak ditemukan.");
            }
    }

    @Test(priority = 3, description = "Pengguna membuka Challenge Reward Bulan")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini memperlihatkan perkembangan aktivitas lari pengguna dalam mencapai milestone reward pada Challenge Reward Bulan, serta menampilkan peringkat pengguna di leaderboard global",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testMasukDetailReward() {
        System.out.println("TEST 3: Navigasi ke Detail Reward Bulan");

        actions.scrollToTop();
        waitTime();

        clickTest(navChallenge, "Klik ikon Challenge di Bottom Navigation");
        waitTime();

        // Klik "Lihat Detail"
        System.out.println("Mencari tombol 'Lihat Detail'");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnLihatDetailRewardBulan));
            clickTest(btnLihatDetailRewardBulan, "Klik Lihat Detail Reward Bulan");
            waitTime();
        } catch (Exception e) {
            System.out.println("Gagal klik Lihat Detail: " + e.getMessage());

            logInfo("Tombol lihat detail tidak ditemukan");
            
            Assert.fail("Tombol Lihat Detail tidak ditemukan.");
        }

        // Validasi: Pastikan masuk ke halaman detail
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabChallengeRewardBulan));
        Assert.assertTrue(driver.findElements(tabChallengeRewardBulan).size() > 0, "Gagal masuk halaman detail reward!");
        logPass("Berhasil masuk ke halaman Detail Reward Bulan Bulan.");

        // Aktivitas di Tab Reward Bulan
        System.out.println("Klik tab Challenge Reward Bulan");
        clickTest(tabChallengeRewardBulan, "Klik tab Challenge di halaman Detail Reward Bulan Bulan");
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
                System.out.println("Tombol Selesai macet/tidak ketemu, tap di luar modal");
                actions.tapAtScreenRatio(0.5, 0.2);
                logInfo("Popup ditutup menggunakan dengan tapping di luar modal");
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

    @Test(priority = 4, description = "Pengguna membuka Public Challenge di tab challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan daftar semua Public Challenge, yang merupakan challenge buatan pengguna Ayolari, serta memungkinkan pengguna untuk melihat detail dari setiap challenge yang ada",
        note = "",
        group = "SET & LIHAT CHALLENGE"
    )
    public void testNavigasiPublicChallenge() {
        System.out.println("TEST 4: Pengguna membuka Public Challenge di tab challenge");
        
        // Klik "Lihat Semua" (Public Challenge)
        System.out.println("Klik Lihat Semua (Public Challenge)");
        
        wait.until(ExpectedConditions.elementToBeClickable(btnLihatSemuaPublic));
        clickTest(btnLihatSemuaPublic, " Klik tombol Lihat Semua di Public Challenge");
        waitTime(); 

        logPass("Berhasil masuk ke list card Public Challenge");

        if(driver.findElements(btnBackListPublic).size() > 0) {
            wait.until(ExpectedConditions.elementToBeClickable(btnBackListPublic)).click();
        } else {
            driver.navigate().back();
        }
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    public void clickBack() {
        try {
            if (driver.findElements(btnBackHeader1).size() > 0) {
                clickTest(btnBackHeader1, "Klik back");
            } else if (driver.findElements(btnBackHeader2).size() > 0) {
                clickTest(btnBackHeader2, "Klik back");
            } else if (driver.findElements(btnBackList).size() > 0) {
                clickTest(btnBackList, "Klik back");
            } else {
                System.out.println("Tombol Back UI gak nemu, pakai Back HP");
                driver.navigate().back();
            }
            waitTime();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}
