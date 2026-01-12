package tests.flow.aktivitas;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestRiwayatLari extends BaseTest {

    // ==========================================
    // DAFTAR LOKASI (LOCATORS)
    // ==========================================

    // 1. Navigation (Bottom Nav)
    By navAktivitas = AppiumBy.xpath("//android.widget.TextView[@text='Aktivitas'] | //android.widget.Button[contains(@text, 'Aktivitas')]");

    // 2. Tab Aktivitas Page
    By headerAktivitas = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Aktifitas Kamu')]");
    
    // 3. Card Riwayat Lari
    By firstActivityCard = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // 4. Halaman Detail
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // 5. Element Peta
    By mapAreaLocator = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");
    
    By btnZoomIn = AppiumBy.accessibilityId("Zoom in"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text='Toggle attribution']"); 
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // 6. Element Grafik (Anchor Sumbu Y)
    By textSumbuY = AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"); 


    // ==========================================
    // TEST CASES
    // ==========================================

    @Test(priority = 1)
    public void testMasukKeRiwayatLariViaAktivitas() {
        System.out.println("--- TEST 1: Navigasi via Tab Aktivitas ---");
        
        driver.findElement(navAktivitas).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerAktivitas));
        
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        if(driver.findElements(firstActivityCard).size() > 0) {
            System.out.println("Aktivitas ditemukan. Klik card pertama...");
            driver.findElement(firstActivityCard).click();
            
            System.out.println("Memuat halaman detail...");
            try { Thread.sleep(4000); } catch (Exception e) {}
        } else {
            System.out.println("ERROR: Tidak ada aktivitas lari di list!");
            takeScreenshot("Gagal_Cari_Aktivitas"); 
            Assert.fail("Gagal: Tidak ada aktivitas lari di list!");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(titlePage));
        String judul = driver.findElement(titlePage).getText();
        Assert.assertEquals(judul, "Rincian Lari", "Salah halaman!");
        takeScreenshot("Masuk_Rincian_Lari");
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("--- TEST 2: Validasi Elemen Statistik ---");
        
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik aman.");
    }

    @Test(priority = 3)
    public void testInteraksiPeta() {
        System.out.println("--- TEST 3: Interaksi Peta (Map) ---");

        // --- STEP PENTING: SCROLL DULU KE BAWAH ---
        // Kita scroll dari bagian atas layar biar peta gak kegeser
        System.out.println("Scrolling down to Map...");
        actions.swipeVertical(0.4, 0.15); // Ini swipe layar ke atas (konten turun)
        try { Thread.sleep(1500); } catch (Exception e) {}

        // 1. VALIDASI PETA MUNCUL
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapAreaLocator));
        Assert.assertTrue(driver.findElements(mapAreaLocator).size() > 0, "ERROR: Peta tidak muncul!");
        System.out.println("Peta terdeteksi.");

        // 2. ZOOM BUTTONS
        if(driver.findElements(btnZoomIn).size() > 0) {
            driver.findElement(btnZoomIn).click();
            try { Thread.sleep(1000); } catch (Exception e) {} 
        }
        if(driver.findElements(btnZoomOut).size() > 0) {
            driver.findElement(btnZoomOut).click();
            try { Thread.sleep(1000); } catch (Exception e) {}
        }

        // 3. MAP GESTURES (PANNING/DRAG)
        System.out.println("Simulasi Geser Peta (Panning)...");
        // Drag dari tengah ke kanan bawah
        int screenWidth = driver.manage().window().getSize().width;
        int screenHeight = driver.manage().window().getSize().height;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        
        // Geser peta sedikit
        actions.dragMap(centerX, centerY, centerX + 200, centerY + 200);
        try { Thread.sleep(1500); } catch (Exception e) {}

        // 4. ROTASI PETA
        System.out.println("Mencoba Memutar Peta...");
        try {
            WebElement mapArea = driver.findElement(mapAreaLocator);
            actions.rotateMap(mapArea); 
            try { Thread.sleep(2000); } catch (Exception e) {} 
        } catch (Exception e) {
            System.out.println("Rotasi skip: " + e.getMessage());
        }

        // 5. KLIK KOMPAS (Reset)
        try {
            if(driver.findElements(btnCompass).size() > 0) {
                driver.findElement(btnCompass).click();
                System.out.println("Kompas diklik.");
            }
        } catch (Exception e) {}
        
        takeScreenshot("Bukti_Interaksi_Peta");
    }

    @Test(priority = 4)
    public void testScrapingGrafikKetinggian() {
        System.out.println("--- TEST 4: Interaksi Grafik Ketinggian ---");

        System.out.println("Scroll mencari grafik...");
        // Scroll lagi jaga-jaga kalau grafik masih di bawah
        actions.swipeUp(); 
        
        int maxScroll = 2;
        WebElement grafikContainer = null;
        
        while(maxScroll > 0) {
            try {
                grafikContainer = driver.findElement(textSumbuY).findElement(By.xpath(".."));
                break; 
            } catch (Exception e) {
                actions.swipeUp(); // Scroll lagi
                maxScroll--;
                try { Thread.sleep(1500); } catch (Exception ex) {}
            }
        }

        if (grafikContainer == null) {
            System.out.println("SKIP: Grafik tidak ditemukan.");
            return;
        }

        // Logic Tap Grafik
        int startX = grafikContainer.getLocation().getX();
        int endX = startX + grafikContainer.getSize().getWidth();
        int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
        
        int safeStartX = startX + (int)(grafikContainer.getSize().getWidth() * 0.15); 
        int[] tapPoints = {safeStartX, (safeStartX+endX)/2, endX - 20};
        
        System.out.println("Scanning data grafik...");
        for (int pointX : tapPoints) {
            actions.tapByCoordinates(pointX, centerY);
            try { Thread.sleep(500); } catch (Exception e) {} 
        }
        takeScreenshot("Hasil_Scraping_Grafik");
    }
    
    @Test(priority = 5)
    public void testKembaliKeMenu() {
        System.out.println("--- TEST 5: Kembali ke List Riwayat ---");
        
        driver.findElement(btnBack).click();
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Gagal kembali: Masih di halaman detail!");
        Assert.assertTrue(driver.findElements(headerAktivitas).size() > 0, "Gagal kembali ke Tab Aktivitas!");

        System.out.println("Berhasil kembali ke menu utama.");
    }
}