package tests.flow.beranda;

import tests.BaseTest;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestBottomNav extends BaseTest {

    // Daftar Lokasi (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");
    By navAktivitas = AppiumBy.xpath("//android.widget.Button[@text=\"Aktivitas Aktivitas\"]");
    By navMulaiLari = AppiumBy.xpath("//android.widget.TextView[@text='Mulai Lari']");
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    By navSaya = AppiumBy.xpath("//android.widget.Button[@text=\"Saya Saya\"]");

    // Daftar Lokasi Validasi Halaman  
    // BERANDA (Teks Headline)
    By headerBeranda = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.TextView[1]");

    // AKTIVITAS ("Aktifitas Kamu")
    By headerAktivitas = AppiumBy.xpath("//android.widget.TextView[@text='Aktifitas Kamu']");

    // CHALLENGE (Header "Challenge Lari")
    By headerChallenge = AppiumBy.xpath("//android.view.View[@content-desc=\"Challenges Challenges\"]");

    // SAYA ("Profil Kamu")
    By headerSaya = AppiumBy.xpath("//android.widget.TextView[@text='Profil Kamu']");
    
    // MODAL MULAI LARI (Tombol Outdoor)
    By btnOutdoor = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Outdoor')]");


    // Test Cases
    @Test(priority = 1)
    public void testPindahSemuaMenu() {

        System.out.println("TEST 1: Cek Navigasi Menu Bawah");

        TestListener.getTest().pass("Tampilan awal ada di Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // KE AKTIVITAS
        System.out.println("Klik Aktivitas");
        driver.findElement(navAktivitas).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(headerAktivitas));
        System.out.println("Validasi: Header 'Aktifitas Kamu' muncul.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Aktivitas.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // KE CHALLENGE
        System.out.println("Klik Challenge");

        try { Thread.sleep(2000); } catch (Exception e) {}

        driver.findElement(navChallenge).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerChallenge));
        System.out.println("Validasi: Header Challenge muncul.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // KE SAYA (PROFIL)
        System.out.println("Klik Saya");

        try { Thread.sleep(2000); } catch (Exception e) {}

        driver.findElement(navSaya).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerSaya));
        System.out.println("Validasi: Header 'Profil Kamu' muncul.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Profil Kamu (Saya).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // BALIK KE BERANDA
        System.out.println("Klik Beranda");

        try { Thread.sleep(2000); } catch (Exception e) {}

        driver.findElement(navBeranda).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerBeranda));
        System.out.println("Validasi: Balik ke Beranda (Headline muncul).");
        
        TestListener.getTest().pass("Berhasil kembali ke halaman Beranda.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testBukaTutupModalMulaiLari() {
        System.out.println("TEST 2: Cek Modal Mulai Lari & Tutup");
        
        try { Thread.sleep(2000); } catch (Exception e) {}

        driver.findElement(navMulaiLari).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnOutdoor));
        Assert.assertTrue(driver.findElements(btnOutdoor).size() > 0, "Modal gagal terbuka!");

        TestListener.getTest().pass("Modal 'Mulai Lari' berhasil terbuka (Muncul opsi Outdoor).", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // TUTUP MODAL
        System.out.println("Menutup modal");
        try { Thread.sleep(1000); } catch (Exception e) {}
        
        // Action Helper tap di luar modal
        actions.tapAtScreenRatio(0.5, 0.2);
        
        try { Thread.sleep(1000); } catch (Exception e) {}
        Assert.assertTrue(driver.findElements(btnOutdoor).size() == 0, "Modal gagal tertutup!");

        TestListener.getTest().pass("Modal 'Mulai Lari' berhasil tertutup.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
}