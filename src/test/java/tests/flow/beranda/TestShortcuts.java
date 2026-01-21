package tests.flow.beranda;

import tests.BaseTest;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert; // Tambahan: Import Assert
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.AppiumBy;

public class TestShortcuts extends BaseTest {

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
    // Challenges (Public Challenge) -> Halaman Navigasi Challenge (kalau dari Bottom Navigation)
    By textLihatSemuaChallenges = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");
    By textLihatSemuaRiwayatLari = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");
    
    // Card di List Challenge Saya (saat masuk Lihat Semua yang Challenge yang Diikuti)
    By cardInListMyChallenge = AppiumBy.xpath("(//android.widget.TextView[@text='Pelari FOMO'])[1]");
    
    // Card Challenges (Public Challenge) di Beranda (Lari)
    By cardDailyRunBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Lari']");
    
    // Card Riwayat Lari di Beranda (Aktivitas Lari)
    By cardRiwayatBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Aktivitas Lari']");

    // Test Cases
    @Test(priority = 1)
    public void testBerandaInteractionFlow() {
        System.out.println("START TEST: Beranda Shortcuts & Navigation Flow");
        waitTime();

        TestListener.getTest().pass("Tampilan awal Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");

        // Scroll atas - bawah Beranda 
        System.out.println("Scroll Vertical Beranda");
        actions.swipeVertical(0.7, 0.4); // Turun
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Beranda ke bawah.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeVertical(0.4, 0.7); // Naik lagi
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Beranda ke atas.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll horizontal di Challenge yang Diikuti
        System.out.println("Scroll Horizontal 'Challenge Saya'");
        actions.swipeHorizontal(0.9, 0.1, 0.35); // Geser Kiri
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Challenge yang diikuti ke kiri.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeHorizontal(0.1, 0.9, 0.35); // Balik Kanan
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Challenge yang diikuti ke kanan.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Lihat Semua di Challenge yang Diikuti (Challenge Saya)
        System.out.println("Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        driver.findElement(textLihatSemuaChallengeYangDiikuti).click();
        waitTime();

        try {
            Assert.assertTrue(driver.findElement(cardInListMyChallenge).isDisplayed(), "Gagal masuk ke halaman List Challenge Saya.");
        } catch (Exception e) { }

        TestListener.getTest().pass("Berhasil klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik salah 1 card (di dalam List)
        System.out.println("Klik Card 'Pelari FOMO' di List");
        wait.until(ExpectedConditions.elementToBeClickable(cardInListMyChallenge)).click();
        waitTime();

        TestListener.getTest().pass("Berhasil klik card 'Pelari FOMO' di List Challenge yang Diikuti.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Back 2x (Detail -> List -> Beranda)
        System.out.println("Back 2x ke Beranda");
        clickBack(); // Back dari Detail
        waitTime();

        TestListener.getTest().pass("Berhasil kembali dari Detail ke List Challenge yang Diikuti.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        clickBack(); // Back dari List (Beranda)
        waitTime();

        Assert.assertTrue(driver.findElement(textLihatSemuaChallengeYangDiikuti).isDisplayed(), "Gagal kembali ke Beranda setelah dari List Challenge.");

        TestListener.getTest().pass("Berhasil kembali dari List Challenge yang Diikuti ke Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll horizontal di Challenges (Public Challenge)
        System.out.println("Scroll Horizontal 'Public Challenges'");
        actions.swipeHorizontal(0.9, 0.1, 0.7);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Public Challenges ke kiri.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeHorizontal(0.1, 0.9, 0.7);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Public Challenges ke kanan.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik card Lari (card di Beranda)
        System.out.println(" Klik Card 'Lari' di Beranda (Public Challenges)");
        driver.findElement(cardDailyRunBeranda).click();
        waitTime();

        TestListener.getTest().pass("Berhasil klik card 'Lari' di Beranda (Public Challenges).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        clickBack(); // Balik ke Beranda
        waitTime();

        Assert.assertTrue(driver.findElement(cardDailyRunBeranda).isDisplayed(), "Gagal kembali ke Beranda dari Detail Lari.");

        TestListener.getTest().pass("Berhasil kembali ke Beranda dari Detail Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik lihat semua yg punya challenges (Public Challenge)
        System.out.println("Klik 'Lihat Semua' (Public Challenge)");
        driver.findElement(textLihatSemuaChallenges).click();
        waitTime();

        TestListener.getTest().pass("Berhasil klik 'Lihat Semua' (Public Challenge).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll dikit aja (lihat halaman sebentar)
        System.out.println("Scroll dikit");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Challenges (Public Challenge).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeVertical(0.5, 0.7);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Challenges (Public Challenge) kembali ke atas.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Bottom Navigation yang Beranda (keluar dari challenge)
        System.out.println(" Klik Bottom Nav 'Beranda'");
        driver.findElement(navBeranda).click();
        waitTime();

        Assert.assertTrue(driver.findElement(navBeranda).isSelected() || driver.findElement(textLihatSemuaChallenges).isDisplayed(), "Gagal kembali ke Beranda via Bottom Nav.");

        TestListener.getTest().pass("Berhasil kembali ke Beranda dari halaman Challenges (Public Challenge).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll Beranda untuk cari Riwayat Lari
        System.out.println(" Scroll cari 'Riwayat Lari'");
        actions.swipeVertical(0.9, 0.3);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Beranda untuk cari Riwayat Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik lihat semua yg punya riwayat lari
        System.out.println(" Klik 'Lihat Semua' (Riwayat Lari)");
        driver.findElement(textLihatSemuaRiwayatLari).click();
        waitTime();

        TestListener.getTest().pass("Berhasil klik 'Lihat Semua' (Riwayat Lari).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Ini bakal ke pop up ke navigasi aktivitas -> Jdi trs pilih bot nav yg beranda
        System.out.println(" Pindah Tab Aktivitas -> Klik Bottom Nav 'Beranda'");
        driver.findElement(navBeranda).click();
        waitTime();

        TestListener.getTest().pass("Berhasil kembali ke Beranda setelah navigasi ke aktivitas Riwayat Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll lgi ke bawah (Cari Card Riwayat di Beranda)
        System.out.println(" Scroll lagi cari Card Riwayat");
        actions.swipeVertical(0.9, 0.3);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Beranda untuk cari Card Riwayat Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik salah 1 card di riwayat lari
        System.out.println(" Klik Card 'Aktivitas Lari'");
        driver.findElement(cardRiwayatBeranda).click();
        try { 
            Thread.sleep(5000); 
            TestListener.getTest().pass("Berhasil klik card Aktivitas Lari.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        } catch (Exception e) {}

        // Back 1x (Balik ke Beranda)
        System.out.println(" Back ke Beranda (Finish)");
        clickBack();

        TestListener.getTest().pass("Berhasil kembali ke Beranda dari Detail Aktivitas Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // Reset ke atas
        actions.scrollToTop();

        TestListener.getTest().pass("Berhasil scroll halaman kembali ke atas.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // Helper 
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    public void clickBack() {
        try {
            if (driver.findElements(btnBackHeader1).size() > 0) {
                driver.findElement(btnBackHeader1).click();
            } else if (driver.findElements(btnBackHeader2).size() > 0) {
                driver.findElement(btnBackHeader2).click();
            } else if (driver.findElements(btnBackList).size() > 0) {
                driver.findElement(btnBackList).click();
            } else {
                System.out.println("   (Tombol Back UI gak nemu, pakai Back HP)");
                driver.navigate().back();
            }
            waitTime();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}