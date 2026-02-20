package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestSyaratPrivasi extends BaseTest {
    // Daftar Lokasi
    // Ikon Saya (Bottom Navigation)
    By navSaya = AppiumBy.xpath("//android.widget.Button[@text=\"Saya Saya\"]");

    // 'Lihat Semua' di Badges
    By textLihatSemuaBadges = AppiumBy.xpath("//android.widget.TextView[@text=\"Lihat Semua\"]");

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
    @Test(priority = 1, description = "Navigasi ke Halaman Syarat & Privasi dari Profil")
    @TestInfo(
        testType = "Positive Case",
        expected = "Halaman Profile terbuka, user bisa masuk ke halaman Syarat & Privasi.",
        note = "",
        group = "Syarat & Privasi"
    ) 
    public void testSyaratPrivasi(){
        System.out.println("Test 1: Navigasi ke Halaman Syarat & Privasi dari Profil");

        // Klik Saya di Bottom Navigation
        System.out.println("Klik Saya (Bottom Navigation)");
        clickTest(navSaya, "Klik Saya (Bottom Navigation)");
        waitTime();

        Assert.assertTrue(driver.findElement(textLihatSemuaBadges).isDisplayed(), "Gagal masuk ke halaman Profil (Saya).");
        logPass("Berhasil masuk ke halaman Profil.");

        // Scroll ke bawah ke Card Syarat & Privasi
        System.out.println("Scroll Vertical Halaman Profil");
        actions.swipeVertical(0.8, 0.3);
        waitTime();

        logPass("Berhasil scroll ke bawah cari menu Syarat & Privasi.");

        // Klik Card Syarat & Privasi
        System.out.println("Klik Card Syarat & Privasi");
        clickTest(cardSyaratPrivasi, "Klik Card Syarat & Privasi");
        waitTime();

        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratPrivasi));
        System.out.println("Validasi: Button Back muncul di halaman Syarat & Privasi.");

        Assert.assertTrue(driver.findElement(linkSyaratKetentuan).isDisplayed(), "Gagal masuk ke menu Syarat & Privasi.");

        logPass("Berhasil masuk ke halaman Syarat & Privasi.");
    }

    @Test(priority = 2, description = "Link Syarat dan Ketentuan")
    @TestInfo(
        testType = "Positive Case",
        expected = "User bisa membuka detail Syarat dan Ketentuan.",
        note = "",
        group = "Syarat & Privasi"
    )
    public void testSyaratdanKetentuan(){
        System.out.println("Test 2: Link Syarat dan Ketentuan");

        // Klik Link Syarat dan Ketentuan
        System.out.println("Klik Link Syarat dan Ketentuan");
        clickTest(linkSyaratKetentuan, "Klik Link Syarat dan Ketentuan");
        waitTime();

        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackSyaratKetentuan));
        System.out.println("Validasi: Halaman Syarat dan Ketentuan muncul.");

        Assert.assertTrue(driver.findElement(btnBackSyaratKetentuan).isDisplayed(), "Gagal masuk ke detail Syarat & Ketentuan.");

        logPass("Berhasil masuk ke detail Syarat & Ketentuan.");

        actions.scrollAndCapture(13, 0.8, 0.2, "Baca Syarat & Ketentuan");
        
        System.out.println("Balik ke atas");
        By textvalidasi1 = AppiumBy.xpath("//android.widget.TextView[@text='Selamat datang Syarat & Ketentuan']");
        
        actions.scrollToTopCustom(textvalidasi1, 15, 0.2, 0.85); 
    }

    @Test(priority = 3, description = "Navigasi Kembali dari Syarat dan Ketentuan")
    @TestInfo(
        testType = "Positive Case",
        expected = "User bisa kembali dari halaman detail Syarat dan Ketentuan.",
        note = "",
        group = "Syarat & Privasi"
    )
    public void testNavigasiKembaliSyaratdanKetentuan(){
        System.out.println("Test 3: Navigasi Kembali dari Syarat dan Ketentuan");

        // Kembali dari Syarat dan Ketentuan
        System.out.println("Kembali dari Syarat dan Ketentuan");

        try {
            clickTest(btnBackSyaratKetentuan, "Klik back Syarat dan Ketentuan");
        } catch (Exception e) {
            System.out.println("Tombol UI tidak ditemukan, menggunakan back bawaan HP.");
            driver.navigate().back();
        }
        waitTime();

        Assert.assertTrue(driver.findElement(linkKebijakanPrivasi).isDisplayed(), "Gagal kembali ke menu Syarat & Privasi dari detail Syarat.");

        logPass("Berhasil kembali ke menu Syarat & Privasi.");
    }

    @Test(priority = 4, description = "Link Kebijakan Privasi")
    @TestInfo(
        testType = "Positive Case",
        expected = "User bisa membuka detail Kebijakan Privasi.",
        note = "",
        group = "Syarat & Privasi"
    )
    public void testKebijakanPrivasi(){
        System.out.println("Test 4: Link Kebijakan Privasi");

        // Klik Link Kebijakan Privasi
        System.out.println("Klik Link Kebijakan Privasi");
        clickTest(linkKebijakanPrivasi, "Klik Link Kebijakan Privasi");
        waitTime();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchKebijakanPrivasi));
        System.out.println("Validasi: Halaman Kebijakan Privasi muncul.");

        Assert.assertTrue(driver.findElement(searchKebijakanPrivasi).isDisplayed(), "Gagal masuk ke detail Kebijakan Privasi.");

        logPass("Berhasil masuk ke detail Kebijakan Privasi.");

        actions.scrollAndCapture(13, 0.8, 0.2, "Baca Kebijakan Privasi");

        System.out.println("Balik ke atas");
        By textvalidasi1 = AppiumBy.xpath("//android.widget.TextView[@text='Kebijakan Privasi']");
        
        actions.scrollToTopCustom(textvalidasi1, 8, 0.2, 0.85); 
    }

    @Test(priority = 5, description = "Navigasi Kembali dari Kebijakan Privasi")
    @TestInfo(
        testType = "Positive Case",
        expected = "User bisa kembali dari halaman detail Kebijakan Privasi.",
        note = "",
        group = "Syarat & Privasi"
    )
    public void testNavigasiKembaliKebijakanPrivasi(){
        System.out.println("Test 5: Navigasi Kembali dari Kebijakan Privasi");

        // Kembali dari Kebijakan Privasi
        System.out.println("Kembali dari Kebijakan Privasi");
        driver.navigate().back();
        waitTime();

        Assert.assertTrue(driver.findElement(linkSyaratKetentuan).isDisplayed(), "Gagal kembali ke menu Syarat & Privasi dari detail Kebijakan.");

        logPass("Berhasil kembali ke menu Syarat & Privasi.");
    }

    @Test(priority = 6, description = "Navigasi Kembali dari Syarat & Privasi")
    @TestInfo(
        testType = "Positive Case",
        expected = "User bisa kembali dari halaman Syarat & Privasi.",
        note = "",
        group = "Syarat & Privasi"
    )
    public void testNavigasiKembalikeProfile(){
        System.out.println("Test 6: Navigasi Kembali dari Syarat & Privasi");

        // Kembali ke Profil dari Syarat & Privasi
        System.out.println("Kembali ke Profil dari Syarat & Privasi");

        try {
            clickTest(btnBackSyaratPrivasi, "Klik back di Halaman Syarat & Privasi");
        } catch (Exception e) {
            System.out.println("Tombol UI tidak ditemukan, menggunakan back bawaan HP.");
            driver.navigate().back();
        }
        waitTime();

        Assert.assertTrue(driver.findElement(cardSyaratPrivasi).isDisplayed(), "Gagal kembali ke halaman Profil utama.");

        logPass("Berhasil kembali ke halaman Profil utama.");
    }

    // Helper 
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    
}
