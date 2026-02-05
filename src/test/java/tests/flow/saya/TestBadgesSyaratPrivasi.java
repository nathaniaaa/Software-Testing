package tests.flow.saya;

import tests.BaseTest;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder; 

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestBadgesSyaratPrivasi extends BaseTest {
    // Daftar Lokasi
    // Ikon Saya (Bottom Navigation)
    By navSaya = AppiumBy.xpath("//android.widget.Button[@text=\"Saya Saya\"]");

    // 'Lihat Semua' di Badges
    By textLihatSemuaBadges = AppiumBy.xpath("//android.widget.TextView[@text=\"Lihat Semua\"]");

    // Tombol back di halaman Badges
    By btnBackBadges = AppiumBy.xpath("//android.widget.Button");

    // Tombol back di halaman Syarat & Privasi
    By btnBackSyaratPrivasi = AppiumBy.xpath("//android.widget.Button");

    // Card Syarat & Privasi
    By cardSyaratPrivasi = AppiumBy.xpath("//android.view.View[@content-desc=\"icon information Syarat & Privasi Lihat syarat dan privasi pengguna.\"]");

    // Link Syarat dan Ketentuan
    By linkSyaratKetentuan = AppiumBy.xpath("//android.widget.TextView[@text=\"Syarat dan ketentuan\"]");
    // Back Syarat dan Ketentuan
    By btnBackSyaratKetentuan = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[3]/android.view.View[1]/android.widget.Button");

    // Link Kebijakan Privasi
    By linkKebijakanPrivasi = AppiumBy.xpath("//android.view.View[@content-desc=\"Kebijakan Privasi\"]");
    // Ikon search untuk validasi halaman Kebijakan Privasi
    By searchKebijakanPrivasi = AppiumBy.xpath("//android.widget.Button[@resource-id=\"headerSearchMobile\"]");

    // Test Cases
    @Test(priority = 1)
    public void testBadges(){
        System.out.println("Test 1: Navigasi ke Halaman Badges dari Profil");

        // Klik Saya di Bottom Navigation
        System.out.println("Klik Saya (Bottom Navigation)");
        driver.findElement(navSaya).click();
        waitTime();

        Assert.assertTrue(driver.findElement(textLihatSemuaBadges).isDisplayed(), "Gagal masuk ke halaman Profil (Saya).");
        
        TestListener.getTest().pass("Berhasil masuk ke halaman Profil.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Lihat Semua di Badges
        System.out.println("Klik 'Lihat Semua' di Badges");
        driver.findElement(textLihatSemuaBadges).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackBadges));
        System.out.println("Validasi: Button Back muncul.");
        
        Assert.assertTrue(driver.findElement(btnBackBadges).isDisplayed(), "Gagal masuk ke halaman Badges.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Badges.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Kembali ke Profil
        System.out.println("Kembali ke Profil dari Badges");
        driver.findElement(btnBackBadges).click();
        waitTime();

        Assert.assertTrue(driver.findElement(textLihatSemuaBadges).isDisplayed(), "Gagal kembali ke Profil dari Badges.");
        
        TestListener.getTest().pass("Berhasil kembali ke halaman Profil.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testSyaratPrivasi(){
        System.out.println("Test 2: Navigasi ke Halaman Syarat & Privasi dari Profil");

        // Scroll ke bawah ke Card Syarat & Privasi
        System.out.println("Scroll Vertical Halaman Profil");
        actions.swipeVertical(0.8, 0.3);
        waitTime();

        TestListener.getTest().pass("Berhasil scroll ke bawah cari menu Syarat & Privasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Card Syarat & Privasi
        System.out.println("Klik Card Syarat & Privasi");
        driver.findElement(cardSyaratPrivasi).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratPrivasi));
        System.out.println("Validasi: Button Back muncul di halaman Syarat & Privasi.");

        Assert.assertTrue(driver.findElement(linkSyaratKetentuan).isDisplayed(), "Gagal masuk ke menu Syarat & Privasi.");

        TestListener.getTest().pass("Berhasil masuk ke halaman Syarat & Privasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Link Syarat dan Ketentuan
        System.out.println("Klik Link Syarat dan Ketentuan");
        driver.findElement(linkSyaratKetentuan).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratKetentuan));
        System.out.println("Validasi: Halaman Syarat dan Ketentuan muncul.");

        Assert.assertTrue(driver.findElement(btnBackSyaratKetentuan).isDisplayed(), "Gagal masuk ke detail Syarat & Ketentuan.");

        TestListener.getTest().pass("Berhasil masuk ke detail Syarat & Ketentuan.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Kembali dari Syarat dan Ketentuan
        System.out.println("Kembali dari Syarat dan Ketentuan");
        driver.findElement(btnBackSyaratKetentuan).click();
        waitTime();

        Assert.assertTrue(driver.findElement(linkKebijakanPrivasi).isDisplayed(), "Gagal kembali ke menu Syarat & Privasi dari detail Syarat.");

        TestListener.getTest().pass("Berhasil kembali ke menu Syarat & Privasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Klik Link Kebijakan Privasi
        System.out.println("Klik Link Kebijakan Privasi");
        driver.findElement(linkKebijakanPrivasi).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchKebijakanPrivasi));
        System.out.println("Validasi: Halaman Kebijakan Privasi muncul.");

        Assert.assertTrue(driver.findElement(searchKebijakanPrivasi).isDisplayed(), "Gagal masuk ke detail Kebijakan Privasi.");

        TestListener.getTest().pass("Berhasil masuk ke detail Kebijakan Privasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Kembali dari Kebijakan Privasi
        System.out.println("Kembali dari Kebijakan Privasi");
        driver.navigate().back();
        waitTime();

        Assert.assertTrue(driver.findElement(linkSyaratKetentuan).isDisplayed(), "Gagal kembali ke menu Syarat & Privasi dari detail Kebijakan.");

        TestListener.getTest().pass("Berhasil kembali ke menu Syarat & Privasi.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Kembali ke Profil dari Syarat & Privasi
        System.out.println("Kembali ke Profil dari Syarat & Privasi");
        driver.findElement(btnBackSyaratPrivasi).click();
        waitTime();

        Assert.assertTrue(driver.findElement(cardSyaratPrivasi).isDisplayed(), "Gagal kembali ke halaman Profil utama.");

        TestListener.getTest().pass("Berhasil kembali ke halaman Profil utama.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // Helper 
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}