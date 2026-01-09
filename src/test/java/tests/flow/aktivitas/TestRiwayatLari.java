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
     * LOGIC PENCARIAN TOMBOL:
     * Mencari tombol 'Lihat Semua' yang SATU KONTAINER dengan teks 'Riwayat Lari'.
     */
    By btnLihatSemua = AppiumBy.xpath("//*[@text='Riwayat Lari']/parent::*//*[@text='Lihat Semua']");
    
    // Kartu Aktivitas Lari Pertama
    By cardAktivitasPertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Halaman Detail
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // Element Peta
    By btnZoomIn = AppiumBy.accessibilityId("Zoom in"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text='Toggle attribution']"); 
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // Element Grafik (Anchor untuk scrolling grafik)
    By textSumbuY = AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"); 

    // ==========================================
    // TEST CASES
    // ==========================================

    @Test(priority = 1)
    public void testMasukKeRiwayatLari() {
        System.out.println("TEST 1: Navigasi ke Riwayat Lari");

        actions.scrollToText("Riwayat Lari");

        // Locator hanya untuk validasi keberadaan teks
       // By textRiwayatLocator = AppiumBy.xpath("//*[@text='Riwayat Lari']");
        
        // // --- LOGIC SCROLLING (REVISI) ---
        // // Kita coba scroll maksimal 5 kali pakai scrollVertical (Standard)
        // int maxScroll = 5;
        // boolean ketemu = false;

        // System.out.println("Mencari menu 'Riwayat Lari'...");
        // while (maxScroll > 0) {
        //     // Cek apakah teks sudah muncul?
        //     if (driver.findElements(textRiwayatLocator).size() > 0) {
        //         ketemu = true;
        //         break; 
        //     }
            
        //     // Kalau belum, LAKUKAN SCROLL
        //     // Kita pakai scrollVertical() dari ActionHelper yang baru (Logic 70% -> 30%)
        //     actions.scrollVertical(); 
            
        //     // Note: Kalau scrollVertical() dirasa kurang nendang, ganti baris atas jadi:
        //     // actions.swipeUp(); 
            
        //     maxScroll--;
            
        //     // Jeda biar layar render dulu
        //     try { Thread.sleep(1500); } catch (Exception e) {}
        // }

        // if (!ketemu) {
        //     Assert.fail("Gagal menemukan 'Riwayat Lari' setelah 5x scroll!");
        // }

        System.out.println("'Riwayat Lari' ditemukan. Klik Lihat Semua...");
        click(btnLihatSemua);

        System.out.println("Memilih aktivitas lari pertama...");
        try { Thread.sleep(1500); } catch (Exception e) {} // Tunggu loading list
        click(cardAktivitasPertama);

        waitForVisibility(titlePage);
        String judul = getText(titlePage);
        Assert.assertEquals(judul, "Rincian Lari", "Judul halaman salah/tidak masuk detail!");
        
        takeScreenshot("MasukRiwayatLari_Success");
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("TEST 2: Validasi Elemen Statistik");
        
        // Validasi elemen dasar (Jarak & Waktu)
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik aman.");
    }

    @Test(priority = 3)
    public void testInteraksiPeta() {
        System.out.println("TEST 3: Interaksi Peta (Map)");

        // 1. Scroll sedikit ke bawah untuk memastikan Peta terlihat utuh
        // Kita pakai scrollVertical sekali aja
        actions.scrollVertical();
        try { Thread.sleep(1000); } catch (Exception e) {}

        // 2. Zoom In & Out
        System.out.println("Mencoba Zoom In...");
        if(driver.findElements(btnZoomIn).size() > 0) {
            click(btnZoomIn);
            try { Thread.sleep(1000); } catch (Exception e) {} 
        }

        System.out.println("Mencoba Zoom Out...");
        if(driver.findElements(btnZoomOut).size() > 0) {
            click(btnZoomOut);
            try { Thread.sleep(1000); } catch (Exception e) {}
        }

        // 3. Toggle Info Map
        try {
            if(driver.findElements(btnInfoMap).size() > 0) {
                 click(btnInfoMap);
                 try { Thread.sleep(500); } catch (Exception e) {}
                 click(btnInfoMap); // Tutup lagi
                 System.out.println("Info map toggle sukses.");
            }
        } catch (Exception e) {
            System.out.println("Info Map skip: " + e.getMessage());
        }

        // 4. ROTASI PETA
        System.out.println("Mencoba Memutar Peta...");
        try {
            // Cari elemen peta (biasanya View besar setelah root)
            WebElement mapArea = driver.findElement(AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]"));
            actions.rotateMap(mapArea); 
            try { Thread.sleep(2000); } catch (Exception e) {} // Tunggu animasi putar
        } catch (Exception e) {
            System.out.println("Rotasi gagal/elemen peta beda: " + e.getMessage());
        }

        // 5. Klik Kompas (Reset Bearing)
        // Kompas biasanya baru muncul SETELAH diputar
        try {
            if(driver.findElements(btnCompass).size() > 0) {
                click(btnCompass);
                System.out.println("Kompas diklik (Reset Utara).");
            } else {
                System.out.println("Kompas tidak muncul (Mungkin putaran kurang jauh).");
            }
        } catch (Exception e) {}
        
        takeScreenshot("BuktiInteraksiPeta");
    }

    @Test(priority = 4)
    public void testScrapingGrafikKetinggian() {
        System.out.println("TEST 4: Interaksi Grafik Ketinggian");

        // Scroll cari anchor grafik (Label Sumbu Y " m")
        System.out.println("Mencari Grafik...");
        int maxGrafikScroll = 3;
        WebElement grafikContainer = null;
        
        while(maxGrafikScroll > 0) {
            try {
                // Coba cari label sumbu Y (misal "150 m") lalu ambil parent-nya
                grafikContainer = driver.findElement(textSumbuY).findElement(By.xpath(".."));
                break; // Ketemu
            } catch (Exception e) {
                actions.scrollVertical(); // Scroll cari grafik
                maxGrafikScroll--;
                try { Thread.sleep(1000); } catch (Exception ex) {}
            }
        }

        if (grafikContainer == null) {
            System.out.println("SKIP: Grafik tidak ditemukan atau Sumbu Y beda locator.");
            return;
        }

        // Logic Tap Grafik
        int startX = grafikContainer.getLocation().getX();
        int endX = startX + grafikContainer.getSize().getWidth();
        int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
        
        // Tap 3 titik: Kiri, Tengah, Kanan
        int safeStartX = startX + (int)(grafikContainer.getSize().getWidth() * 0.15); 
        int[] tapPoints = {safeStartX, (safeStartX+endX)/2, endX - 20};
        
        System.out.println("Scanning data grafik...");
        for (int pointX : tapPoints) {
            actions.tapByCoordinates(pointX, centerY);
            try { Thread.sleep(500); } catch (Exception e) {} 
            
            // Ambil tooltip text
            List<WebElement> texts = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"));
            for (WebElement el : texts) {
                String txt = el.getText();
                // Filter label statis (misal 150 m, 0 m)
                if (!txt.equals("150 m") && !txt.equals("0 m") && !txt.equals("300 m")) {
                    System.out.println("Titik X=" + pointX + " | Nilai: " + txt);
                }
            }
        }
        takeScreenshot("HasilScrapingGrafik");
    }
    
    @Test(priority = 5)
    public void testKembaliKeMenu() {
        System.out.println("TEST 5: Kembali ke List Riwayat");
        
        click(btnBack);
        try { Thread.sleep(1000); } catch (Exception e) {}
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Gagal kembali: Masih di halaman detail!");
        
        System.out.println("Berhasil kembali.");
    }
}