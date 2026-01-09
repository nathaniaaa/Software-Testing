package tests.flow.aktivitas;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import java.util.List;

public class TestRiwayatLari extends BaseTest {

    // ==========================================
    // DAFTAR LOKASI (LOCATORS)
    // ==========================================

    /**
     * LOGIC BARU:
     * Locator ini mencari teks 'Riwayat Lari', naik ke pembungkusnya (parent), 
     * lalu mencari tombol 'Lihat Semua' di dalam pembungkus yang sama.
     * Ini menjamin kita klik tombol yang SEJAJAR dengan Riwayat Lari.
     */
    By btnLihatSemua = AppiumBy.xpath("//*[@text='Riwayat Lari']/parent::*//*[@text='Lihat Semua']");
    
    // Kartu Aktivitas Lari Pertama (Mengambil index ke-2 karena index 1 biasanya header/spacer)
    By cardAktivitasPertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Halaman Detail
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // Element Peta
    By btnZoomIn = AppiumBy.accessibilityId("Zoom in"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    
    // Tombol Info (i) - Toggle Attribution
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text='Toggle attribution']"); 
    
    // Button Kompas (Biasanya baru muncul setelah peta diputar)
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // Element Grafik (Anchor untuk scrolling grafik)
    By textSumbuY = AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"); 

    // ==========================================
    // TEST CASES
    // ==========================================

    @Test(priority = 1)
    public void testMasukKeRiwayatLari() {
        System.out.println("TEST 1: Navigasi ke Riwayat Lari (Mode Paksa Scroll)");

        // Locator Text Riwayat Lari (hanya buat validasi)
        By textRiwayatLocator = AppiumBy.xpath("//*[@text='Riwayat Lari']");
        
        // Logic: Loop Swipe maksimal 5 kali sampai teks ketemu
        int maxScroll = 5;
        boolean ketemu = false;

        while (maxScroll > 0) {
            // Cek apakah teks 'Riwayat Lari' sudah muncul di layar?
            if (driver.findElements(textRiwayatLocator).size() > 0) {
                ketemu = true;
                break; // Keluar loop kalau sudah ketemu
            }
            
            // Kalau belum ketemu, SWIPE!
            actions.swipeUp();
            maxScroll--;
            
            // Jeda dikit biar rendering selesai
            try { Thread.sleep(1000); } catch (Exception e) {}
        }

        if (!ketemu) {
            Assert.fail("Gagal Scroll: Sudah swipe 5x tapi 'Riwayat Lari' gak muncul juga!");
        }

        System.out.println("Riwayat Lari ditemukan! Mencari tombol Lihat Semua...");

        // Pakai logic XPath sibling/parent yang tadi (ini sudah benar)
        click(btnLihatSemua);

        System.out.println("Memilih aktivitas lari pertama");
        try { Thread.sleep(1500); } catch (Exception e) {}
        click(cardAktivitasPertama);

        waitForVisibility(titlePage);
        String judul = getText(titlePage);
        Assert.assertEquals(judul, "Rincian Lari", "Judul halaman tidak sesuai!");
        
        takeScreenshot("MasukRiwayatLari_Success");
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("TEST 2: Validasi Elemen Statistik");
        
        // Cek apakah ada angka jarak (km) dan durasi (:)
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik dasar terlihat aman.");
    }

    @Test(priority = 3)
    public void testInteraksiPeta() {
        System.out.println("TEST 3: Interaksi Peta (Map)");

        // 1. Scroll ke Peta (Native Scroll lagi biar aman)
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"Peta\"));"
            ));
        } catch (Exception e) {
            System.out.println("Warning: Tidak perlu scroll atau teks 'Peta' tidak ketemu.");
        }

        // 2. Zoom In & Out
        System.out.println("Mencoba Zoom In...");
        click(btnZoomIn);
        try { Thread.sleep(1000); } catch (Exception e) {} 

        System.out.println("Mencoba Zoom Out...");
        click(btnZoomOut);
        try { Thread.sleep(1000); } catch (Exception e) {}

        // 3. Klik Info (Toggle Attribution)
        System.out.println("Mencoba Klik Info Map (i)...");
        try {
            click(btnInfoMap);
            try { Thread.sleep(500); } catch (Exception e) {}
            click(btnInfoMap); // Tutup lagi
        } catch (Exception e) {
            System.out.println("Info Map skip: " + e.getMessage());
        }

        // 4. ROTASI PETA (2 Jari)
        System.out.println("Mencoba Memutar Peta...");
        try {
             // Pastikan actions helper kamu support rotateMap
            WebElement mapArea = driver.findElement(AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]"));
            actions.rotateMap(mapArea); 
            try { Thread.sleep(1500); } catch (Exception e) {} 
        } catch (Exception e) {
            System.out.println("Rotasi gagal/tidak support: " + e.getMessage());
        }

        // 5. Klik Kompas
        System.out.println("Mencoba Klik Kompas...");
        try {
            if(driver.findElements(btnCompass).size() > 0) {
                click(btnCompass);
                System.out.println("Kompas diklik.");
            } else {
                System.out.println("Kompas tidak muncul (mungkin peta belum terputar cukup jauh).");
            }
        } catch (Exception e) {
            System.out.println("Gagal klik Kompas: " + e.getMessage());
        }
        
        takeScreenshot("BuktiInteraksiPeta");
    }

    @Test(priority = 4)
    public void testScrapingGrafikKetinggian() {
        System.out.println("TEST 4: Interaksi Grafik Ketinggian");

        // Scroll cari judul grafik dulu
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"Ketinggian\"));"
            ));
        } catch (Exception e) {}

        WebElement grafikContainer = null;
        try {
            // Cari container grafik berdasarkan salah satu label angka di sumbu Y (misal "150 m" atau "0 m")
            grafikContainer = driver.findElement(textSumbuY).findElement(By.xpath(".."));
        } catch (Exception e) {
            System.out.println("Skip Test Grafik: Container grafik tidak ditemukan.");
            return;
        }

        int startX = grafikContainer.getLocation().getX();
        int endX = startX + grafikContainer.getSize().getWidth();
        int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
        
        // Mulai tap sedikit lebih ke kanan dari sumbu Y (biar gak kena label)
        int safeStartX = startX + (int)(grafikContainer.getSize().getWidth() * 0.15); 
        
        int[] tapPoints = {safeStartX, (safeStartX+endX)/2, endX - 20};
        
        System.out.println("Scanning data grafik...");
        for (int pointX : tapPoints) {
            // Tap pada koordinat spesifik grafik
            actions.tapByCoordinates(pointX, centerY);
            try { Thread.sleep(500); } catch (Exception e) {} 
            
            // Ambil semua teks yang muncul (biasanya tooltip nilai ketinggian)
            List<WebElement> texts = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"));
            
            for (WebElement el : texts) {
                String txt = el.getText();
                // Filter teks sampah (label sumbu Y statis)
                if (!txt.equals("150 m") && !txt.equals("300 m") && !txt.equals("0 m")) {
                    System.out.println("Titik X=" + pointX + " | Nilai: " + txt);
                }
            }
        }
        takeScreenshot("HasilScrapingGrafik");
    }
    
    @Test(priority = 5)
    public void testKembaliKeMenu() {
        System.out.println("TEST 5: Kembali ke Halaman Sebelumnya");
        
        click(btnBack);
        try { Thread.sleep(1000); } catch (Exception e) {}
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Gagal kembali: Masih di halaman detail!");
        
        System.out.println("Berhasil kembali ke list riwayat.");
    }
}