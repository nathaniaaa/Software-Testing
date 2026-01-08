package tests.flow;

import tests.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestRincianLari extends BaseTest {

    // ========================================================
    // 1. DAFTAR LOCATORS
    // ========================================================
    
    By btnLihatSemua = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[2]");
    By cardAktivitasPertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // Element Peta
    By btnZoomIn = AppiumBy.accessibilityId("Zoom in"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    
    // Element Grafik
    By textSumbuY = AppiumBy.xpath("//android.widget.TextView[@text='150 m']"); 

    // ========================================================
    // 2. TEST CASES
    // ========================================================

    @Test(priority = 1)
    public void testMasukKeRincianLari() {
        System.out.println("--- TEST 1: Navigasi ke Rincian Lari ---");

        // Gunakan 'actions' untuk scroll
        // actions.scrollToText("Lihat Semua"); 
        
        System.out.println("Klik tombol Lihat Semua...");
        click(btnLihatSemua);

        System.out.println("Memilih aktivitas lari pertama...");
        click(cardAktivitasPertama);

        waitForVisibility(titlePage);
        String judul = getText(titlePage);
        Assert.assertEquals(judul, "Rincian Lari", "Judul halaman tidak sesuai!");
        
        System.out.println("Berhasil masuk ke halaman: " + judul);
        takeScreenshot("MasukRincianLari_Success");
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("--- TEST 2: Validasi Elemen Statistik ---");
        
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik dasar terlihat aman.");
    }

    @Test(priority = 3)
    public void testInteraksiPeta() {
        System.out.println("--- TEST 3: Interaksi Peta (Map) ---");

        // REVISI: Panggil via object 'actions'
        actions.scrollToText("Peta");

        // Opsi 1: Pakai Button Zoom bawaan aplikasi
        System.out.println("Mencoba Zoom In (Tombol)...");
        click(btnZoomIn);
        try { Thread.sleep(1000); } catch (Exception e) {} 

        // Opsi 2: Pakai Gesture Pinch (Opsional, dari ActionHelper)
        // System.out.println("Mencoba Pinch Zoom In...");
        // actions.zoomIn(); 

        System.out.println("Mencoba Zoom Out...");
        click(btnZoomOut);
        
        takeScreenshot("BuktiInteraksiPeta");
        System.out.println("Interaksi Peta selesai.");
    }

    @Test(priority = 4)
    public void testScrapingGrafikKetinggian() {
        System.out.println("--- TEST 4: Interaksi Grafik Ketinggian ---");

        // REVISI: Panggil via object 'actions'
        actions.scrollToText("Ketinggian");

        WebElement grafikContainer = null;
        try {
            grafikContainer = driver.findElement(textSumbuY).findElement(By.xpath(".."));
        } catch (Exception e) {
            Assert.fail("Gagal menemukan container grafik via anchor '150 m'");
        }

        int startX = grafikContainer.getLocation().getX();
        int endX = startX + grafikContainer.getSize().getWidth();
        int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
        
        int safeStartX = startX + (int)(grafikContainer.getSize().getWidth() * 0.2); 
        
        System.out.println("Mulai melakukan Tap Scanning pada grafik...");
        
        int[] tapPoints = {safeStartX, (startX+endX)/2, endX - 50};
        
        for (int pointX : tapPoints) {
            // REVISI: Panggil via object 'actions'
            actions.tapByCoordinates(pointX, centerY);
            
            try { Thread.sleep(500); } catch (Exception e) {} 
            
            List<WebElement> texts = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ' m')]"));
            
            for (WebElement el : texts) {
                String txt = el.getText();
                if (!txt.equals("150 m") && !txt.equals("300 m") && !txt.equals("450 m") && !txt.equals("600 m") 
                    && !txt.contains("Perolehan") && !txt.contains("Maksimal")) {
                    System.out.println("Titik X=" + pointX + " | Data: " + txt);
                }
            }
        }
        takeScreenshot("HasilScrapingGrafik");
    }
    
    @Test(priority = 5)
    public void testKembaliKeMenu() {
        System.out.println("--- TEST 5: Kembali ke Halaman Sebelumnya ---");
        
        click(btnBack);
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Seharusnya sudah keluar dari halaman detail!");
        
        System.out.println("Berhasil kembali.");
    }

    // ========================================================
    // HELPERS LOKAL (Karena kamu menghapusnya dari BaseTest)
    // ========================================================
    // Sebaiknya fungsi ini dimasukkan lagi ke BaseTest biar rapi,
    // tapi kalau mau quick fix, taruh di sini dulu gapapa.
    
    public void click(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public String getText(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void takeScreenshot(String fileName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File destFile = new File("screenshots/" + fileName + "_" + timestamp + ".png");
            FileUtils.copyFile(srcFile, destFile);
        } catch (Exception e) {
            System.out.println("Gagal screenshot: " + e.getMessage());
        }
    }
}