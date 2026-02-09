package tests.flow.beranda;

import tests.BaseTest;
import tests.utils.TestInfo;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestBeranda extends BaseTest {
    // Daftar Lokasi
    // Ikon Beranda (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");
    
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
    
    // Card Challenge Saya - dari beranda 
    By cardChallengeSayaBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[1]/android.view.View[1]");

    // Card Public Challenge - dari beranda
    By cardPublicChallengeBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]/android.view.View[1]");

    // Card Riwayat Lari - dari beranda
    By cardRiwayatBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]");

    // Test Cases
    @Test(priority = 1, description = "Pengguna Melihat Detail Total Lari Harian dari Beranda")
    @TestInfo(
        expected = "Pengguna dapat Melihat Detail Total Lari Harian dari Beranda",
        note = "Pastikan user sudah login sebelumnya.",
        group = "Beranda"
    ) 
    public void testTotalLariHarian() {
        System.out.println("Test 1: Pengguna dapat Melihat Detail Total Lari Harian dari Beranda");
        waitTime();

        logInfo("Tampilan awal Beranda");
        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");
        
        capture.highlightRectangleByRatio(0.05, 0.40, 0.90, 0.20, "Validasi Tampilan Total Lari Harian");
    }

    @Test(priority = 2, description = "Pengguna menekan tombol \"lihat semua\" pada Challenge yang diikuti")
    @TestInfo(
        expected = "Menampilkan semua challenge yang telah diikuti maupun yang telah dibuat oleh Pengguna.",
        note = "Pastikan user sudah login sebelumnya.",
        group = "Beranda"
    ) 
    public void testLihatSemuaChallengeYangDiikuti() {
        System.out.println("Test 2: Pengguna menekan tombol \\\"lihat semua\\\" pada Challenge yang diikuti");
        waitTime();

        // Klik lihat semua yg diikuti (Challenge Saya)
        System.out.println("Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        clickTest(textLihatSemuaChallengeYangDiikuti, "Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        
        // Tunggu 
        try { 
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.view.View"))); 
        } catch (Exception e) {}

        logPass("Berhasil klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).");

        waitTime();
        clickBack();
        
        logPass("Berhasil kembali dari 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).");
    }

    @Test(priority = 3, description = "Challenges")
    @TestInfo(
        expected = "Pengguna bisa menampilkan halaman Challenge dengan menekan \"lihat semua\" pada Challenges (Public Challenges).",
        note = "Pastikan user sudah login sebelumnya.",
        group = "Beranda"
    ) 
    public void testes() {
        System.out.println("Test 3: Pengguna bisa menampilkan halaman Challenge dengan menekan \"lihat semua\" pada Challenges (Public Challenges).");
        waitTime();

        // Klik lihat semua yg punya challenges (Public Challenge)
        System.out.println("Klik 'Lihat Semua' (Public Challenge)");
        clickTest(textLihatSemuaChallenges, "Klik 'Lihat Semua' (Public Challenge)");
        waitTime();

        logPass("Berhasil klik 'Lihat Semua' Challenges (Public Challenge),");

        // Klik Bottom Navigation yang Beranda (keluar dari challenge)
        System.out.println(" Klik Bottom Nav 'Beranda'");
        clickTest(navBeranda, "Klik Bottom Nav 'Beranda'");
        waitTime();

        Assert.assertTrue(driver.findElement(navBeranda).isSelected() || driver.findElement(textLihatSemuaChallenges).isDisplayed(), "Gagal kembali ke Beranda via Bottom Nav.");

        logPass("Berhasil kembali ke Beranda.");
    }

    // Helper
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
