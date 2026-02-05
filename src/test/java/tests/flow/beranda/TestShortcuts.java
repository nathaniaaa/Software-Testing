package tests.flow.beranda;

import tests.BaseTest;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert; 
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
    @Test(priority = 1, description = "Test Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Challenge yang Diikuti (Challenge Saya)")
    public void testBerandaInteractionFlow() {
        System.out.println("Test 1: Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Challenge yang Diikuti (Challenge Saya)");
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

        // Scroll horizontal di Challenge yang Diikuti - Challenge Saya
        System.out.println("Scroll Horizontal 'Challenge Saya'");
        actions.swipeHorizontal(0.9, 0.1, 0.35); // Geser Kiri
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Challenge yang diikuti ke kiri.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        actions.swipeHorizontal(0.1, 0.9, 0.35); // Balik Kanan
        waitTime();

        TestListener.getTest().pass("Berhasil scroll horizontal card Challenge yang diikuti ke kanan.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik lihat semua yg diikuti (Challenge Saya)
        System.out.println("Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        driver.findElement(textLihatSemuaChallengeYangDiikuti).click();
        
        // Tunggu 
        try { 
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.view.View"))); 
        } catch (Exception e) {}

        TestListener.getTest().pass("Berhasil klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        waitTime();
        clickBack();
        TestListener.getTest().pass("Berhasil kembali dari 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik card Challenge Saya (card di Beranda)
        System.out.println(" Klik Salah Satu Card 'Challenge Saya' di Beranda");
        // LOGIC: Cek apakah Card Pertama (Index 2) Ada?
        if (driver.findElements(cardChallengeSayaBeranda).size() > 0) {
            // == KONDISI: ADA LIST ==
            System.out.println("Card ditemukan di List. Klik Card Pertama.");
            driver.findElement(cardChallengeSayaBeranda).click();
            waitTime();

            TestListener.getTest().pass("Berhasil klik card pertama di List Challenge yang Diikuti.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Back ke Beranda
            System.out.println("Back ke Beranda");
            clickBack(); // Back dari Detail
            waitTime();
            
            TestListener.getTest().pass("Berhasil kembali ke Beranda.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        } else {
            // == KONDISI: LIST KOSONG ==
            System.out.println("List Kosong / Card tidak ditemukan.");
            TestListener.getTest().warning("List Challenge Kosong", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        }
        waitTime();
    }

    @Test(priority = 2, description = "Test Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Public Challenge")
    public void testPublicChallenge() {
        System.out.println("Test 1: Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Public Challenge");
        waitTime();

        TestListener.getTest().pass("Tampilan awal Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");

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

        // Klik card Public Challenges (card di Beranda)
        System.out.println(" Klik Salah Satu Card 'Public Challenges' di Beranda");
        
        // LOGIC: Cek apakah Card Pertama (Index 2) Ada?
        if (driver.findElements(cardPublicChallengeBeranda).size() > 0) {
            // == KONDISI: ADA LIST ==
            System.out.println("Card ditemukan di List. Klik Card Pertama.");
            driver.findElement(cardPublicChallengeBeranda).click();
            waitTime();

            TestListener.getTest().pass("Berhasil klik card pertama di List Public Challenges.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Back ke Beranda
            System.out.println("Back ke Beranda");
            clickBack(); // Back dari Detail
            waitTime();
            
            TestListener.getTest().pass("Berhasil kembali ke Beranda.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        } else {
            // == KONDISI: LIST KOSONG ==
            System.out.println("List Kosong / Card tidak ditemukan.");
            TestListener.getTest().warning("List Public Challenges Kosong", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        }
        waitTime();

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
    }

    @Test(priority = 3, description = "Test Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Riwayat Lari")
    public void testRiwayatLari() {
        System.out.println("Test 1: Interaksi Shortcut dan Navigasi di Halaman Beranda - Khusus Bagian Riwayat Lari");
        waitTime();

        TestListener.getTest().pass("Tampilan awal Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");

        // Scroll Beranda untuk cari Riwayat Lari
        System.out.println(" Scroll cari 'Riwayat Lari'");
        actions.swipeVertical(0.9, 0.1);
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
        System.out.println("Karena Pindah ke Tab Aktivitas -> Maka Klik Bottom Nav 'Beranda' untuk Kembali");
        driver.findElement(navBeranda).click();
        waitTime();

        TestListener.getTest().pass("Berhasil kembali ke Beranda setelah navigasi ke aktivitas Riwayat Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Scroll lgi ke bawah (Cari Card Riwayat di Beranda)
        System.out.println(" Scroll lagi cari Card Riwayat");
        actions.swipeVertical(0.9, 0.1);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll vertikal halaman Beranda untuk cari Card Riwayat Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik salah 1 card di riwayat lari
        System.out.println(" Klik Card 'Aktivitas Lari'");

        // LOGIC: Cek apakah Card Pertama (Index 2) Ada?
        if (driver.findElements(cardRiwayatBeranda).size() > 0) {
            // == KONDISI: ADA LIST ==
            System.out.println("Card ditemukan di List. Klik Card Pertama.");
            driver.findElement(cardRiwayatBeranda).click();
            waitTime();

            TestListener.getTest().pass("Berhasil klik card pertama di List Riwayat Lari.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Back ke Beranda
            System.out.println("Back ke Beranda");
            clickBack(); // Back dari Detail
            waitTime();
            
            TestListener.getTest().pass("Berhasil kembali ke Beranda.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        } else {
            // == KONDISI: LIST KOSONG ==
            System.out.println("List Kosong / Card tidak ditemukan.");
            TestListener.getTest().warning("List Riwayat Lari Kosong", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        }
        waitTime();

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
                System.out.println("Tombol Back UI gak nemu, pakai Back HP");
                driver.navigate().back();
            }
            waitTime();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}