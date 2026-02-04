package tests.flow.challenge.challenges;

import tests.BaseTest;
import tests.utils.TestListener; 
import com.aventstack.extentreports.MediaEntityBuilder; 

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestChallengeSaya extends BaseTest {

    // Daftar Lokasi
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    // Title Challenge Saya (Validasi Halaman)
    By titleChallengeSaya = AppiumBy.xpath("//android.view.View[@content-desc='Challenge Saya'] | //android.widget.TextView[@text='Challenge Saya']");

    // Lihat Semua - Tombol di Challenge Saya
    By btnLihatSemua = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]");

    // Card Spesifik (Pelari FOMO & Yuk Lari Sehat)
    By cardPelariFomo = AppiumBy.xpath("//android.widget.TextView[@text='Pelari tidak fomo']");
    By cardYukLariSehat = AppiumBy.xpath("//android.widget.TextView[@text='YUUUUK LARI SEHAT']");

    // Card Urutan Pertama (Cadangan kalau spesifik ga ada)
    By cardUrutanPertama = AppiumBy.xpath("(//android.widget.TextView)[3]");

    // Detail Page 
    By tabDeskripsi = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[contains(@resource-id, 'trigger-leaderboard')]");
    
    // Tombol Back
    By btnBackDetail = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");
    By btnBackList = AppiumBy.xpath("//android.view.View[@content-desc='joined']"); 

    // Test Cases
    @Test(priority = 1, description = "Test Navigasi ke Menu Challenge & Swipe Carousel")
    public void testNavigasiDanSwipeHorizon() {
        System.out.println("TEST 1: Navigasi & Swipe Carousel");

        // Masuk Menu Challenge
        System.out.println("Klik Menu Challenge");
        driver.findElement(navChallenge).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(titleChallengeSaya));

        // Validasi: Masuk halaman Challenge utama
        Assert.assertTrue(driver.findElements(titleChallengeSaya).size() > 0, "Gagal masuk menu Challenge utama!");
        
        TestListener.getTest().pass("Berhasil masuk menu Challenge utama.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Swipe Horizontal
        System.out.println("Swipe Horizontal di area Challenge Saya");
        try {
            // Swipe ke Kiri
            actions.swipeHorizontal(0.85, 0.15, 0.37); 
            Thread.sleep(2000); // Tunggu animasi selesai baru SS
            
            TestListener.getTest().info("Swipe Card List Challenge Saya ke kiri.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Swipe ke Kanan
            actions.swipeHorizontal(0.15, 0.85, 0.37);
            Thread.sleep(2000); 
            
            TestListener.getTest().info("Swipe Card List Challenge Saya ke kanan.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
        } catch (Exception e) {
            System.out.println("Gagal Swipe: " + e.getMessage());
        }

        // Klik "Lihat Semua"
        System.out.println("Klik 'Lihat Semua'");
        driver.findElement(btnLihatSemua).click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Validasi: Cek apakah list terbuka
        boolean isListOpen = driver.findElements(AppiumBy.className("android.widget.TextView")).size() > 0;
        Assert.assertTrue(isListOpen, "Gagal masuk halaman List Challenge Saya!");
        
        TestListener.getTest().pass("Berhasil masuk halaman List 'Lihat Semua' Challenge Saya.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2, description = "Test Detail Challenge (Pelari FOMO atau Card Pertama)")
    public void testDetailChallengeDynamic() {
        System.out.println("TEST 2: Cek Detail Challenge (Prioritas: Pelari FOMO -> First Card -> Empty)");

        try{ 
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.view.View"))); 
        } catch (Exception e) {}

        // KONDISI 1: Cek apakah Card 'Pelari FOMO' ada?
        if (driver.findElements(cardPelariFomo).size() > 0) {
            System.out.println(">> OPSI 1: Card 'Pelari FOMO' ditemukan.");
            
            // Klik Card
            driver.findElement(cardPelariFomo).click();
            
            // Validasi Masuk Detail
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
            Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail Pelari FOMO.");
            
            TestListener.getTest().pass("Masuk Detail Challenge: Pelari FOMO.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Scroll Deskripsi
            System.out.println("Scroll Deskripsi");
            driver.findElement(tabDeskripsi).click(); 
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            actions.swipeVertical(0.7, 0.4); 
            try { Thread.sleep(1000); } catch (Exception e) {}
            
            TestListener.getTest().info("Scroll Deskripsi ke Bawah.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            actions.swipeVertical(0.4, 0.7);
            try { Thread.sleep(1000); } catch (Exception e) {}

            // Cek Leaderboard 
            System.out.println("Pindah ke Leaderboard");
            driver.findElement(tabLeaderboard).click();
            try { Thread.sleep(3000); } catch (Exception e) {}
            
            TestListener.getTest().info("Pindah ke Tab Leaderboard Pelari FOMO.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            System.out.println("Scroll Leaderboard");
            actions.swipeVertical(0.7, 0.4); 
            try { Thread.sleep(1000); } catch (Exception e) {}
            
            TestListener.getTest().info("Scroll Leaderboard ke Bawah.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            actions.swipeVertical(0.4, 0.7); 
            try { Thread.sleep(1000); } catch (Exception e) {}

            // Back ke List
            System.out.println("Kembali ke List");
            driver.findElement(btnBackDetail).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            TestListener.getTest().pass("Berhasil kembali ke list challenge.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // KONDISI 2: Jika Pelari FOMO TIDAK ADA, ambil Card Urutan PERTAMA 
        } else if (driver.findElements(cardUrutanPertama).size() > 0) {
            System.out.println(">> OPSI 2: Card Spesifik tidak ada. Mengambil Card urutan pertama.");
            
            String namaCardRandom = driver.findElement(cardUrutanPertama).getText();
            System.out.println("Card yang dipilih: " + namaCardRandom);

            driver.findElement(cardUrutanPertama).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
            Assert.assertTrue(driver.findElement(tabDeskripsi).isDisplayed(), "Gagal masuk detail " + namaCardRandom);
            
            TestListener.getTest().pass("Masuk Detail Challenge Alternatif: " + namaCardRandom, 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Scroll Deskripsi
            System.out.println("Scroll Deskripsi");
            driver.findElement(tabDeskripsi).click(); 
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            actions.swipeVertical(0.7, 0.4); 
            try { Thread.sleep(1000); } catch (Exception e) {}

            TestListener.getTest().info("Scroll Deskripsi ke Bawah.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
            actions.swipeVertical(0.4, 0.7); 
            try { Thread.sleep(1000); } catch (Exception e) {}

            // Cek Leaderboard 
            System.out.println("Pindah ke Leaderboard");
            driver.findElement(tabLeaderboard).click();
            try { Thread.sleep(3000); } catch (Exception e) {}
            
            TestListener.getTest().info("Pindah ke Tab Leaderboard " + namaCardRandom, 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            System.out.println("Scroll Leaderboard");
            actions.swipeVertical(0.7, 0.4);
            try { Thread.sleep(1000); } catch (Exception e) {}

            TestListener.getTest().info("Scroll Leaderboard ke Bawah.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
            actions.swipeVertical(0.4, 0.7);
            try { Thread.sleep(1000); } catch (Exception e) {}

            // Back ke List
            System.out.println("Kembali ke List");
            driver.findElement(btnBackDetail).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            TestListener.getTest().pass("Berhasil kembali ke list challenge.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // KONDISI 3: Jika List KOSONG
        } else {
            System.out.println(">> OPSI 3: Tidak ada challenge sama sekali di list.");
            TestListener.getTest().warning("List Challenge Kosong / Belum ada challenge yang tersedia.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        }
    }

    @Test(priority = 3, description = "Test Detail Challenge Spesifik (Yuk Lari Sehat)")
    public void testDetailYukLariSehat() {
        System.out.println("TEST 3: Cek Detail Yuk Lari Sehat (Opsional)");

        // Cek apakah element card Yuk Lari Sehat ada?
        if (driver.findElements(cardYukLariSehat).size() > 0) {
            System.out.println("Card Yuk Lari Sehat ditemukan. Menjalankan tes.");
            driver.findElement(cardYukLariSehat).click();
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabDeskripsi));
            
            TestListener.getTest().pass("Masuk Detail Challenge: Yuk Lari Sehat.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Scroll Deskripsi sedikit
            System.out.println("Scroll Turun Deskripsi");
            actions.swipeVertical(0.7, 0.4);
            try { Thread.sleep(1000); } catch (Exception e) {}

            TestListener.getTest().info("Scroll Deskripsi Yuk Lari Sehat.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Cek Tab Leaderboard sekilas
            System.out.println("Pindah ke Leaderboard (Cek isi saja)");
            driver.findElement(tabLeaderboard).click();
            try { Thread.sleep(3000); } catch (Exception e) {}
            
            TestListener.getTest().info("Pindah ke Tab Leaderboard.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            // Back ke List
            System.out.println("Kembali ke List");
            driver.findElement(btnBackDetail).click();
            try { Thread.sleep(2000); } catch (Exception e) {}

            TestListener.getTest().pass("Berhasil kembali ke list challenge saya.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
        } else {
            System.out.println("SKIP: Card Yuk Lari Sehat tidak ditemukan. Test dilewati (Aman).");
            TestListener.getTest().info("Card Yuk Lari Sehat tidak tersedia, step ini dilewati.");
        }
    }

    @Test(priority = 4, description = "Test Kembali ke Menu Utama")
    public void testKembaliKeMenuUtama() {
        System.out.println("TEST 4: Kembali ke Menu Utama Challenge");

        if (driver.findElements(btnBackList).size() > 0) {
             driver.findElement(btnBackList).click();
        } else {
             System.out.println("Tombol back custom ga ketemu, pakai Back System");
             driver.navigate().back();
        }

        try { Thread.sleep(2000); } catch (Exception e) {}
        
        Assert.assertTrue(driver.findElements(btnLihatSemua).size() > 0, "Gagal kembali ke Menu Utama Challenge!");
        
        TestListener.getTest().pass("Berhasil kembali ke Menu Utama Challenge.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        System.out.println("Selesai.");
    }
}