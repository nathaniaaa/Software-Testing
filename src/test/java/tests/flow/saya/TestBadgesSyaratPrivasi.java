package tests.flow.saya;

import tests.BaseTest;
import org.openqa.selenium.By;
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

        // Klik Lihat Semua di Badges
        System.out.println("Klik 'Lihat Semua' di Badges");
        driver.findElement(textLihatSemuaBadges).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackBadges));
        System.out.println("Validasi: Button Back muncul.");
        takeScreenshot("Saya_Badges_Show");

        // Kembali ke Profil
        System.out.println("Kembali ke Profil dari Badges");
        driver.findElement(btnBackBadges).click();
        waitTime();
    }

    @Test(priority = 2)
    public void testSyaratPrivasi(){
        System.out.println("Test 2: Navigasi ke Halaman Syarat & Privasi dari Profil");

        // Scroll ke bawah ke Card Syarat & Privasi
        System.out.println("Scroll Vertical Halaman Profil");
        actions.swipeVertical(0.7, 0.4);
        waitTime();

        // Klik Card Syarat & Privasi
        System.out.println("Klik Card Syarat & Privasi");
        driver.findElement(cardSyaratPrivasi).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratPrivasi));
        System.out.println("Validasi: Button Back muncul di halaman Syarat & Privasi.");
        takeScreenshot("Saya_SyaratPrivasi_Show");

        // Klik Link Syarat dan Ketentuan
        System.out.println("Klik Link Syarat dan Ketentuan");
        driver.findElement(linkSyaratKetentuan).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratKetentuan));
        System.out.println("Validasi: Halaman Syarat dan Ketentuan muncul.");
        takeScreenshot("Saya_SyaratKetentuan_Show");

        // // Scroll
        // actions.swipeVertical(0.9, 0.1);
        // waitTime();

        // Kembali dari Syarat dan Ketentuan
        System.out.println("Kembali dari Syarat dan Ketentuan");
        driver.findElement(btnBackSyaratKetentuan).click();
        waitTime();

        // Klik Link Kebijakan Privasi
        System.out.println("Klik Link Kebijakan Privasi");
        driver.findElement(linkKebijakanPrivasi).click();
        waitTime();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchKebijakanPrivasi));
        System.out.println("Validasi: Halaman Kebijakan Privasi muncul.");
        takeScreenshot("Saya_KebijakanPrivasi_Show");

        // // Scroll
        // actions.swipeVertical(0.9, 0.1);
        // waitTime();

        // Kembali dari Kebijakan Privasi
        System.out.println("Kembali dari Kebijakan Privasi");
        driver.navigate().back();
        waitTime();

        // Kembali ke Profil dari Syarat & Privasi
        System.out.println("Kembali ke Profil dari Syarat & Privasi");
        driver.findElement(btnBackSyaratPrivasi).click();
        waitTime();
    }


    // Helper 
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
