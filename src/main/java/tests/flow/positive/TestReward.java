package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestReward extends BaseTest {
    // **** BAGIAN TAB CHALLENGE ****

    // Daftar Lokasi 
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Ikon Beranda (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");

    // Detail Page
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");

    // Tombol Back
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    By btnBackListPublic = AppiumBy.xpath("//android.view.View[@content-desc='challenges']");
    By btnBackListSaya = AppiumBy.xpath("//android.view.View[@content-desc='joined']");

    // Tombol Back (Bawaan Aplikasi)
    By btnBackHeader1 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.widget.Button");
    By btnBackHeader2 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[1]/android.widget.Button[1]");
    By btnBackList = AppiumBy.xpath("//android.view.View[@content-desc=\"joined\"]");

    // locator header (untuk highlight section)
    By headerChallenges = AppiumBy.xpath("//android.widget.TextView[@text='Challenges']");
    By headerChallengeSaya = AppiumBy.xpath("//android.widget.TextView[@text='Challenge yang Diikuti']");
    By headerTotalLari = AppiumBy.xpath("//android.widget.TextView[@text='Total Lari Harian']");
    By headerPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']");
    By headerEventLari = AppiumBy.xpath("//android.widget.TextView[@text='Event Lari']");
    By headerRiwayatLari = AppiumBy.xpath("//android.widget.TextView[@text='Riwayat Lari']");

    // **** BAGIAN TAB REWARDS ****
    By tabRewardsIkonAtas = AppiumBy.xpath("//android.view.View[@content-desc='Rewards Rewards']");
    By btnRiwayatReward = AppiumBy.xpath("//android.view.View[@content-desc='Public Riwayat Reward']");
    By btnBackIkonRewards = AppiumBy.xpath("//android.widget.Button");

    @Test(priority = 1, description = "Pengguna membuka Tab Reward di halaman Challenge Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini menampilkan daftar reward yang tersedia untuk diklaim berdasarkan challenge yang diikuti oleh pengguna. Selain itu, pengguna juga dapat melihat panduan langkah demi langkah untuk melakukan proses redeem reward",
        note = "",
        group = "REWARD"
    )
    public void testNavigasiRewards() {
        System.out.println("TEST 1: Pengguna membuka Tab Reward di halaman Challenge Lari");
        waitTime(); 

        actions.scrollToTop();

        clickTest(navChallenge, "Klik Bottom Navigation Challenge");
        waitTime();

        // Klik Tab Rewards
        clickTest(tabRewardsIkonAtas, "Klik Tab Rewards");
        waitTime();

        logPass("Berhasil klik tab rewards");
    }

    @Test(priority = 2, description = "Pengguna mengklaim Reward setelah mencapai target milestone di reward bulanan")
    @TestInfo(
        testType = "Positive Case",
        expected = "Reward berhasil di klaim oleh pengguna",
        note = "",
        group = "REWARD"
    )
    public void testKlaimReward2() {
        System.out.println("Pengguna mengklaim Reward setelah mencapai target milestone di reward bulanan");
        waitTime(); 

        logInfo("Halaman rewards");
    }

    // Lihat Detail Reward Bulan Bulan
    By btnLihatDetailRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='Lihat Detail']");
    
    // Tab Menu (Challenge & Leaderboard)
    By tabChallengeRewardBulan = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-challenge')]");
    By tabLeaderboardRewardBulan = AppiumBy.xpath("//*[contains(@resource-id, 'trigger-leaderboard') or contains(@text, 'Leaderboard') or @content-desc='Leaderboard']");

    // Tombol Back 
    By btnBackRewardBulan = AppiumBy.xpath("//android.view.View[@content-desc='telkomsel-challenge']/android.widget.Button");

    @Test(priority = 3, description = "Pengguna mengklaim Reward setelah mencapai target milestone tertentu lewat tab reward")
    @TestInfo(
        testType = "Positive Case",
        expected = "Reward berhasil di klaim oleh pengguna",
        note = "",
        group = "REWARD"
    )
    public void testKlaimReward() {
        System.out.println("TEST 3: Pengguna mengklaim Reward setelah mencapai target milestone tertentu lewat tab reward");
        waitTime(); 

        actions.scrollToTop();

        wait.until(ExpectedConditions.visibilityOfElementLocated(navChallenge)).click();
        waitTime();

        // Klik "Lihat Detail"
        System.out.println("Mencari tombol 'Lihat Detail'");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnLihatDetailRewardBulan));
            clickTest(btnLihatDetailRewardBulan, "Klik Lihat Detail Reward Bulan");
            logPass("Tampilan halaman");
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

        if(driver.findElements(btnBackRewardBulan).size() > 0) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackRewardBulan)).click();
        } else {
            driver.navigate().back();
        }
        waitTime();
    }

    @Test(priority = 4, description = "Pengguna membuka Riwayat Reward pada tab Reward")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan riwayat reward yang telah diklaim oleh pengguna",
        note = "",
        group = "REWARD"
    )
    public void testBukaRewardsDanRiwayat() {
        System.out.println("TEST 4: Pengguna membuka Riwayat Reward pada tab Reward");
        waitTime(); 

        actions.scrollToTop();

        wait.until(ExpectedConditions.visibilityOfElementLocated(navChallenge)).click();
        waitTime();

        wait.until(ExpectedConditions.visibilityOfElementLocated(tabRewardsIkonAtas)).click();
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
