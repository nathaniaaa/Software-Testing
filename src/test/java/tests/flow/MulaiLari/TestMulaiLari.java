package tests.flow.MulaiLari;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class TestMulaiLari extends BaseTest {

    // Daftar Lokasi 

    // Ikon Mulai Lari (Bottom Navigation)
    By navMulaiLari = AppiumBy.xpath("//android.widget.TextView[@text='Mulai Lari']");
    // Tombol Outdoor pada Modal Pilih Lokasi Lari
    By btnOutdoor = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Outdoor')]");

    // Halaman Perizinan Baterai 
    By btnPergiKePengaturan = AppiumBy.xpath("//android.widget.Button[@text='Pergi ke Pengaturan']");

    // Setting OPPO Locators (Perizinan Baterai)
    By menuPenggunaanBaterai = AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.android.settings:id/recycler_view']/android.widget.LinearLayout[3]");
    By toggleLatarBelakang = AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.oplus.battery:id/recycler_view']/android.widget.LinearLayout[2]");
    By btnIzinkanSystem = AppiumBy.id("android:id/button1");
    By btnBackSettingOppo = AppiumBy.id("com.oplus.battery:id/coui_toolbar_back_view");

    // Switch Toggle di Halaman Setting untuk Lokasi
    By toggleLokasiSwitch = AppiumBy.id("android:id/switch_widget");
    // Tombol Back di Halaman Lokasi Setting
    By btnBackLocationSettings = AppiumBy.id("com.android.settings:id/coui_toolbar_back_view");

    // Halaman Lari Sedang Berjalan
    By btnStop = AppiumBy.id("com.telkomsel.telkomselcm:id/toggle_button_stop");
    By btnFinish = AppiumBy.id("com.telkomsel.telkomselcm:id/btn_finish");
    
    By btnBackFromResult = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View/android.view.View[1]/android.widget.Button[1]"); 


    // Test Cases
    @Test(priority = 1)
    public void testBukaHalamanMulaiLari() {
        System.out.println("TEST 1: Klik ikon Mulai Lari (Bottom Navigation)");
        
        driver.findElement(navMulaiLari).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnOutdoor));
        Assert.assertTrue(driver.findElements(btnOutdoor).size() > 0, "Bottom Navigation Mulai Lari gagal dibuka!");
    }

    @Test(priority = 2)
    public void testPilihOutdoorDanAutoStart() {
        System.out.println("TEST 2: Pilih Outdoor & Auto Start");

        try { Thread.sleep(1000); } catch (Exception e) {}
        wait.until(ExpectedConditions.elementToBeClickable(btnOutdoor)).click();

        //  Setting Perizinan Baterai jika diminta
        // Tungu pop-up perizinan muncul (maks 5 detik)
        try {
            WebDriverWait waitIzin = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitIzin.until(ExpectedConditions.visibilityOfElementLocated(btnPergiKePengaturan));
            
            // JIKA MUNCUL -> pergi ke setting baterai
            System.out.println("Button Perizinan Muncul -> Masuk ke Setting Baterai");
            try { Thread.sleep(1000); } catch (Exception e) {}
            driver.findElement(btnPergiKePengaturan).click();
            
            handleOppoSettings(); // Atur perizinan di setting lalu kembali 
            
            System.out.println("Perizinan Baterai diatur. Kembali ke App dan mulai lagi");
            try { Thread.sleep(2000); } catch (Exception e) {}
            
            // RE-OPEN MENU JIKA BALIK KE HOME
            // Validasi (cek) apakah tombol Outdoor hilang? Kalau hilang, berarti di Home
            if (driver.findElements(btnOutdoor).size() == 0) {
                System.out.println("Posisi di Home. Buka menu 'Mulai Lari' lagi");
                
                // Klik Mulai Lari di Bottom Navigation
                if (driver.findElements(navMulaiLari).size() > 0) {
                    driver.findElement(navMulaiLari).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(btnOutdoor)); // Tunggu modal muncul lagi

                    try { Thread.sleep(1000); } catch (Exception e) {}
                }
            }

            // Klik Outdoor lagi (setelah urusan baterai kelar)
            System.out.println("Klik Outdoor lagi untuk mulai");
            driver.findElement(btnOutdoor).click();

        } catch (Exception e) {
            // JIKA TIDAK MUNCUL -> Aman, lanjut cek lokasi
            System.out.println("Izin Baterai aman/sudah ada");
        }

        // Setting Lokasi (GPS) jika diminta
        // Kadang setelah klik outdoor, dilempar ke setting Lokasi jika GPS mati
        try {
            // Tunggu sebentar apakah masuk ke page Setting Lokasi? (Cek Toggle Switch)
            WebDriverWait waitLokasi = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitLokasi.until(ExpectedConditions.visibilityOfElementLocated(toggleLokasiSwitch));
            
            System.out.println("Dialihkan ke Pengaturan Lokasi -> nyalakan toggle");
            
            // Cek status toggle, kalau mati (false) baru diklik
            WebElement switchElement = driver.findElement(toggleLokasiSwitch);

            // Get attribute 'checked' (nilai: true/false)
            String checked = switchElement.getAttribute("checked");
            
            if (checked.equals("false")) {
                switchElement.click();
                System.out.println("Toggle Lokasi dinyalakan");
                try { Thread.sleep(1500); } catch (Exception e) {}
            } else {
                 System.out.println("Toggle Lokasi ternyata sudah nyala");
            }

            // Klik Back untuk kembali ke App (dan auto start)
            if (driver.findElements(btnBackLocationSettings).size() > 0) {
                 driver.findElement(btnBackLocationSettings).click();
            } else {
                 driver.navigate().back();
            }
            System.out.println("Kembali ke App dari Setting Lokasi");

        } catch (Exception e) {
            System.out.println("Tidak dialihkan ke pengaturan lokasi (Lokasi Aman)");
        }

        // Menunggu lari dimulai (tombol STOP muncul)
        System.out.println("Menunggu lari dimulai (Waiting for Stop button)");
        WebDriverWait waitLari = new WebDriverWait(driver, Duration.ofSeconds(60));
        
        try {
            waitLari.until(ExpectedConditions.visibilityOfElementLocated(btnStop));
            System.out.println("Tombol STOP sudah muncul, berarti Lari sudah dimulai.");
        } catch (Exception e) {
            takeScreenshot("Gagal_Start_Lari");
            Assert.fail("Gagal Start Lari: Tombol STOP tidak muncul dalam 60 detik!");
        }
        takeScreenshot("Lari_Sedang_Berjalan");
    }

    @Test(priority = 3)
    public void testStopDanFinishLari() {
        System.out.println("TEST 3: Lari -> Stop -> Finish");
        
        System.out.println("Simulasi Lari 10 detik");
        try { Thread.sleep(10000); } catch (Exception e) {}

        System.out.println("Berhenti lari");

        // Klik STOP
        wait.until(ExpectedConditions.elementToBeClickable(btnStop)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnFinish));

        // Klik FINISH
        driver.findElement(btnFinish).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(btnBackFromResult));
        
        System.out.println("Selesai!");
        try { Thread.sleep(2000); } catch (Exception e) {}

        takeScreenshot("Hasil_Lari_Selesai");
        driver.findElement(btnBackFromResult).click();
    }

    // Handle setting OPPO untuk perizinan baterai
    public void handleOppoSettings() {
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Klik menu "Penggunaan baterai"
        if (driver.findElements(menuPenggunaanBaterai).size() > 0) {
            driver.findElement(menuPenggunaanBaterai).click();
            try { Thread.sleep(1500); } catch (Exception e) {}
            
            // Klik toggle
            if (driver.findElements(toggleLatarBelakang).size() > 0) {
                driver.findElement(toggleLatarBelakang).click();
                
                // Konfirmasi popup -> pilih "izinkan"
                try { Thread.sleep(1000); } catch (Exception e) {}
                if (driver.findElements(btnIzinkanSystem).size() > 0) {
                     driver.findElement(btnIzinkanSystem).click();
                }
            }
            
            // Back 2x (balik ke Home)
            driver.navigate().back(); 
            try { Thread.sleep(1000); } catch (Exception e) {}
            driver.navigate().back(); 
            
        } else {
            System.out.println("Menu Penggunaan Baterai tidak ditemukan, kembali saja.");
            driver.navigate().back(); 
        }
    }
}