package tests.flow.challenge.rewards;

import tests.BaseTest;
import tests.utils.TestInfo; 
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestRewards extends BaseTest {

    // --- DAFTAR LOKASI (LOCATORS) ---
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    By tabRewardsIkonAtas = AppiumBy.xpath("//android.view.View[@content-desc='Rewards Rewards']");
    By btnRiwayatReward = AppiumBy.xpath("//android.view.View[@content-desc='Public Riwayat Reward']");
    By btnBackIkonRewards = AppiumBy.xpath("//android.widget.Button");

    // --- TEST CASES ---

    @Test(priority = 1, description = "Navigasi ke Menu Challenge")
    @TestInfo(
        expected = "Halaman Challenge terbuka, Tab 'Rewards' terlihat.",
        note = "Pastikan user sudah login sebelumnya."
    )
    public void testMasukMenuChallenge() {
        System.out.println("TEST 1: Buka Menu Challenge");
        sleep(2000);

        // 1. Klik Bottom Nav Challenge (Otomatis SS + Excel)
        clickTest(navChallenge, "Klik menu Challenge di Bottom Nav");
        
        // 2. Validasi
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabRewardsIkonAtas));
        Assert.assertTrue(driver.findElements(tabRewardsIkonAtas).size() > 0, "Gagal masuk halaman Challenge!");
    }

    @Test(priority = 2, description = "Flow Buka Tab Rewards & Riwayat")
    @TestInfo(
        expected = "User bisa membuka Tab Rewards, masuk ke Riwayat, dan kembali lagi dengan tombol Back.",
        note = "Cek kestabilan transisi halaman."
    )
    public void testBukaRewardsDanRiwayat() {
        System.out.println("TEST 2: Buka Rewards -> Riwayat -> Back");
        sleep(1500); 

        // 1. Klik Tab Rewards
        clickTest(tabRewardsIkonAtas, "Klik Tab Rewards");

        logInfo("Masuk ke Tab Rewards.");
        // 2. Klik Button Riwayat Rewards
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        clickTest(btnRiwayatReward, "Klik menu Riwayat Rewards");

        // Validasi Masuk Halaman Riwayat
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackIkonRewards));
        Assert.assertTrue(driver.findElement(btnBackIkonRewards).isDisplayed(), "Gagal masuk halaman Riwayat.");

        // 3. Klik Back
        sleep(2000); 
        
        if (driver.findElements(btnBackIkonRewards).size() > 0) {
            clickTest(btnBackIkonRewards, "Klik tombol Back");
            logPass("Kembali ke Tab Rewards.");
        } else {
            System.out.println("Tombol Back UI tidak ada, pakai Back System.");
            driver.navigate().back();
        }



        // Validasi Kembali ke Rewards
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnRiwayatReward));
        } catch (Exception e) {}

        Assert.assertTrue(driver.findElements(btnRiwayatReward).size() > 0, "Gagal kembali ke halaman Rewards!");
    }

    public void sleep(int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {}
    }
}