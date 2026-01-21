package tests.flow.aktivitas;

import tests.BaseTest;
import tests.utils.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.AppiumBy;

public class TestRiwayatLari extends BaseTest {

    // Daftar Lokasi
    // Ikon Aktivitas (Bottom Navigation)
    By navAktivitas = AppiumBy.xpath("//android.widget.TextView[@text='Aktivitas'] | //android.widget.Button[contains(@text, 'Aktivitas')]");
    
    // Header (Judul)
    By headerAktivitas = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Aktifitas Kamu')]");
    
    // Card Riwayat Lari -> Card Pertama
    By firstActivityCard = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Halaman Detail
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // Tombol Unduh & Edit
    By btnUnduh = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View/android.view.View[1]/android.widget.Button[2]");
    By btnEditAktivitas = AppiumBy.xpath("(//android.widget.Image[@text='svg%3e'])[3]");
    
    // Modal Edit Nama Aktivitas
    By inputNamaAktivitas = AppiumBy.className("android.widget.EditText");
    By btnSimpan = AppiumBy.xpath("//android.widget.Button[@text='Simpan']");
    
    // Judul Modal (Buat dipencet biar keyboard nutup)
    By titleModalEdit = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Ubah Nama')]");

    // Element Peta
    By mapAreaLocator = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View[1]/android.view.View[1]");
    By btnZoomIn = AppiumBy.xpath("//android.widget.Button[@content-desc=\"Zoom in\"]"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text='Toggle attribution']"); 
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // Element Grafik 
    By grafikAreaLocator = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.view.View");


    // Test Cases
    @Test(priority = 1)
    public void testMasukKeRiwayatLariViaAktivitas() {
        System.out.println("TEST 1: Navigasi via Tab Aktivitas");
        
        // Klik ikon Aktivitas di Bottom Navigation
        driver.findElement(navAktivitas).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerAktivitas));
        
        try { Thread.sleep(3000); } catch (Exception e) {}
        
        if(driver.findElements(firstActivityCard).size() > 0) {
            System.out.println("Aktivitas ditemukan. Klik card pertama");
            driver.findElement(firstActivityCard).click();
            
            System.out.println("Memuat halaman detail");
            try { Thread.sleep(4000); } catch (Exception e) {}
        } else {
            System.out.println("ERROR: Tidak ada aktivitas lari di list!");
            TestListener.getTest().pass("Gagal Mencari List Aktivitas Lari.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build()); 
            Assert.fail("Gagal: Tidak ada aktivitas lari di list!");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(titlePage));
        String judul = driver.findElement(titlePage).getText();
        Assert.assertEquals(judul, "Rincian Lari", "Salah halaman!");
        TestListener.getTest().pass("Berhasil masuk ke halaman Rincian Lari.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("TEST 2: Validasi Elemen Statistik");
        
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik aman.");
    }
    
    @Test(priority = 3)
    public void testFiturTambahanHeader() {
        System.out.println("TEST 3: Cek Tombol Unduh & Edit");
        
        // Cek Tombol Unduh
        if (driver.findElements(btnUnduh).size() > 0) {
            System.out.println("Tombol Unduh ada. Klik");
            driver.findElement(btnUnduh).click();
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            System.out.println("Menutup dialog unduh (Tap Outside)");
            actions.tapAtScreenRatio(0.5, 0.15); 
            
            try { Thread.sleep(1000); } catch (Exception e) {}
        } else {
            System.out.println("SKIP: Tombol Unduh tidak ditemukan.");
        }
        
        // Cek Tombol Edit & Isi Nama
        if (driver.findElements(btnEditAktivitas).size() > 0) {
            System.out.println("Tombol Edit ada. Klik");
            driver.findElement(btnEditAktivitas).click();
            
            System.out.println("Menunggu modal edit muncul");
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputNamaAktivitas));
            
            // Isi Nama Baru
            WebElement input = driver.findElement(inputNamaAktivitas);
            input.click(); 
            try { Thread.sleep(500); } catch (Exception e) {}
            
            input.clear(); 
            input.sendKeys("TEST DLU OKIW"); // Nama baru
            try { Thread.sleep(2000); } catch (Exception e) {}
            System.out.println("Ketik nama baru selesai.");
            
            try {
                if(driver.findElements(titleModalEdit).size() > 0) {
                    System.out.println("Klik judul modal (biar keyboard nutup)");
                    driver.findElement(titleModalEdit).click();
                }
            } catch (Exception e) {}
            
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            // Klik Simpan
            if (driver.findElements(btnSimpan).size() > 0) {
                System.out.println("Klik tombol Simpan");
                wait.until(ExpectedConditions.elementToBeClickable(btnSimpan)).click();

                TestListener.getTest().pass("Berhasil edit nama aktivitas.", 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
                
                System.out.println("Menunggu proses simpan");
                try {
                    // Tunggu modal hilang 
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(inputNamaAktivitas));
                    System.out.println("Sukses: Modal Edit sudah tertutup.");
                } catch (Exception e) {
                    System.out.println("Warning: Modal masih nyangkut, paksa tap luar");
                    actions.tapAtScreenRatio(0.5, 0.15);
                }
            }
            
        } else {
            System.out.println("SKIP: Tombol Edit tidak ditemukan.");
        }
        
        Assert.assertTrue(driver.findElements(titlePage).size() > 0, "ERROR: Keluar dari halaman detail lari!");
    }

    @Test(priority = 4)
    public void testInteraksiPeta() {
        System.out.println("TEST 4: Interaksi Peta (Map)");

        System.out.println("Scrolling down to Map");
        actions.swipeVertical(0.4, 0.15); 
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Validasi Peta
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapAreaLocator));
        Assert.assertTrue(driver.findElements(mapAreaLocator).size() > 0, "ERROR: Peta tidak muncul!");
        System.out.println("Peta terdeteksi.");

        // Zoom In
        if(driver.findElements(btnZoomIn).size() > 0) {
            System.out.println("Klik Zoom In");
            driver.findElement(btnZoomIn).click();
            try { Thread.sleep(2000); } catch (Exception e) {} 
        }

        // Zoom Out
        if(driver.findElements(btnZoomOut).size() > 0) {
            System.out.println("Klik Zoom Out");
            driver.findElement(btnZoomOut).click();
            try { Thread.sleep(2000); } catch (Exception e) {} 
        }
        
        // Info Map
        if(driver.findElements(btnInfoMap).size() > 0) {
            System.out.println("Klik Info Map");
            driver.findElement(btnInfoMap).click();
            try { Thread.sleep(2000); } catch (Exception e) {} 
            driver.findElement(btnInfoMap).click();
            try { Thread.sleep(1000); } catch (Exception e) {} 
        }

        // Panning Map
        System.out.println("Simulasi Geser Peta (Panning)");
        int screenWidth = driver.manage().window().getSize().width;
        int screenHeight = driver.manage().window().getSize().height;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        
        actions.dragMap(centerX, centerY, centerX + 200, centerY + 200);
        try { Thread.sleep(1500); } catch (Exception e) {}

        // Rotate
        System.out.println("Mencoba Memutar Peta");
        try {
            WebElement mapArea = driver.findElement(mapAreaLocator);
            actions.rotateMap(mapArea); 
            try { Thread.sleep(2000); } catch (Exception e) {} 
        } catch (Exception e) {}

        // Compass
        try {
            if(driver.findElements(btnCompass).size() > 0) {
                System.out.println("Klik Kompas");
                driver.findElement(btnCompass).click();
                try { Thread.sleep(2000); } catch (Exception e) {} 
            }
        } catch (Exception e) {}
        
        TestListener.getTest().pass("Interaksi Peta berhasil.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 5)
    public void testScrapingGrafikKetinggian() {
        System.out.println("TEST 5: Interaksi Grafik Ketinggian");

        if (driver.findElements(grafikAreaLocator).size() > 0) {
             System.out.println("Grafik ditemukan!");
             WebElement grafikContainer = driver.findElement(grafikAreaLocator);

             int startX = grafikContainer.getLocation().getX();
             int totalWidth = grafikContainer.getSize().getWidth();
             int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
             //  int endX = startX + totalWidth;

             // Tap tap Grafik
             System.out.println("Tap data grafik");
             int point1 = startX + (int)(totalWidth * 0.20); 
             int point2 = startX + (int)(totalWidth * 0.50); 
             int point3 = startX + (int)(totalWidth * 0.80); 
             
             int[] tapPoints = {point1, point2, point3};
             
             for (int pointX : tapPoints) {
                 actions.tapByCoordinates(pointX, centerY);
                 try { Thread.sleep(1500); } catch (Exception e) {} 
             }

             // DRAG HORIZONTAL
             System.out.println("Drag horizontal di grafik (Scrubbing)");
             
             int dragStartX = startX + (int)(totalWidth * 0.15); 
             int dragEndX = startX + (int)(totalWidth * 0.85); 
             
             actions.dragMap(dragStartX, centerY, dragEndX, centerY);
             
             try { Thread.sleep(1500); } catch (Exception e) {} 
             
             TestListener.getTest().pass("Interaksi Grafik Ketinggian berhasil.", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        } else {
             System.out.println("SKIP: Grafik tidak ditemukan.");
        }
    }
    
    @Test(priority = 6)
    public void testKembaliKeMenu() {
        System.out.println("TEST 6: Kembali ke List Riwayat");
        
        System.out.println("Menunggu sebelum kembali");
        try { Thread.sleep(2000); } catch (Exception e) {}

        driver.findElement(btnBack).click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Gagal kembali: Masih di halaman detail!");
        Assert.assertTrue(driver.findElements(headerAktivitas).size() > 0, "Gagal kembali ke Tab Aktivitas!");

        System.out.println("Berhasil kembali ke Aktivitas.");
        TestListener.getTest().pass("Berhasil kembali ke Halaman Aktivitas. Siklus Test Selesai.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
}