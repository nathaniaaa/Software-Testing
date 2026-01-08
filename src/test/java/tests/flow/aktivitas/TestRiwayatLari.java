package tests.flow.aktivitas;

import tests.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import java.util.List;

public class TestRiwayatLari extends BaseTest {

    // Daftar Lokasi

    // Tombol Lihat Semua di bagian Riwayat Lari pada HomePage
    By btnLihatSemua = AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[2]");
    // Kartu Aktivitas Lari Pertama di List Riwayat Lari
    By cardAktivitasPertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Halaman Detail Setelah klik Kartu Aktivitas
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    // Ikon Back di Halaman Detail
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 
    
    // Element Peta
    // Button Zoom In bawaan peta
    By btnZoomIn = AppiumBy.accessibilityId("Zoom in"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    // Button i bawaan peta
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text=\"Toggle attribution\"]");
    // Button Arah Mata Angin bawaan peta
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // Element Grafik
    By textSumbuY = AppiumBy.xpath("//android.widget.TextView[@text='150 m']"); 

    // Test Case
    @Test(priority = 1)
    public void testMasukKeRiwayatLari() {
        System.out.println("TEST 1: Navigasi ke Riwayat Lari");

        // Masuk ke Riwayat Lari melalui HomePage
        // Gunakan actions untuk scroll hingga menemukan teks "Riwayat Lari"
        actions.scrollToText("Riwayat Lari"); 
        
        System.out.println("Klik tombol Lihat Semua");
        click(btnLihatSemua);

        System.out.println("Memilih aktivitas lari pertama");
        click(cardAktivitasPertama);

        waitForVisibility(titlePage);
        String judul = getText(titlePage);
        Assert.assertEquals(judul, "Riwayat Lari", "Judul halaman tidak sesuai!");
        
        System.out.println("Berhasil masuk ke halaman: " + judul);
        takeScreenshot("MasukRiwayatLari_Success");
    }

    @Test(priority = 2)
    public void testCekStatistikUI() {
        System.out.println("TEST 2: Validasi Elemen Statistik");
        
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik dasar terlihat aman.");
    }

    @Test(priority = 3)
    public void testInteraksiPeta() {
        System.out.println("TEST 3: Interaksi Peta (Map)");

        // Scroll ke bagian Peta
        actions.scrollToText("Peta");

        // Menggunakan tombol zoom in bawaan peta
        System.out.println("Mencoba Zoom In (Tombol)...");
        click(btnZoomIn);
        try { Thread.sleep(1000); } catch (Exception e) {} 

        // Menggunakan Gesture Pinch dari ActionHelper
        System.out.println("Mencoba Zoom Out");
        click(btnZoomOut);
        
        takeScreenshot("BuktiInteraksiPeta");
        System.out.println("Interaksi Peta selesai.");
    }

    @Test(priority = 4)
    public void testScrapingGrafikKetinggian() {
        System.out.println("TEST 4: Interaksi Grafik Ketinggian");

        // Scroll ke bagian Grafik Ketinggian
        actions.scrollToText("Maksimal Ketinggian");

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
        
        System.out.println("Mulai melakukan Tap Scanning pada grafik");
        
        int[] tapPoints = {safeStartX, (startX+endX)/2, endX - 50};
        
        for (int pointX : tapPoints) {
            // Menggunakan tapByCoordinates dari ActionHelper
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
        System.out.println("TEST 5: Kembali ke Halaman Sebelumnya");
        
        click(btnBack);
        
        boolean isStillInDetail = driver.findElements(titlePage).size() > 0;
        Assert.assertFalse(isStillInDetail, "Seharusnya sudah keluar dari halaman detail!");
        
        System.out.println("Berhasil kembali.");
    }
}