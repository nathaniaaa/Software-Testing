package tests.flow.MulaiLari;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestMulaiLari extends BaseTest {

    // ==========================================
    // DAFTAR LOKASI (LOCATORS) - Sesuai Excel
    // ==========================================

    // 1. Menu Bawah: Mulai Lari
    By navMulaiLari = AppiumBy.xpath("//android.widget.TextView[@text='Mulai Lari']");

    // 2. Modal Pilih Jenis Lari
    // Note: Locator di Excel agak aneh ("Outdoor running Outdoor"), mungkin itu gabungan text & content-desc.
    // Kita pakai contains biar aman, atau xpath spesifik.
    By btnOutdoor = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Outdoor')]");

    // 3. Prompt Izin Latar Belakang
    By btnPergiKePengaturan = AppiumBy.xpath("//android.widget.Button[@text='Pergi ke Pengaturan']");

    // 4. Halaman Setting (KHUSUS OPPO/REALME - com.oplus.battery)
    // Sesuai Excel Baris 8 & 9
    By settingPenggunaanBaterai = AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.android.settings:id/recycler_view']/android.widget.LinearLayout[3]");
    
    // Ini locator opsional, kadang langsung masuk ke list, kadang harus klik menu dulu.
    // Sesuaikan dengan yang muncul di layar HP kamu.
    By settingIzinkanLatarBelakang = AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.oplus.battery:id/recycler_view']/android.widget.LinearLayout[2]");
    
    // Tombol Izinkan (Baris 10 Excel)
    By btnIzinkanSystem = AppiumBy.id("android:id/button1");

    // Tombol Back di System (Header Kiri Atas)
    By btnBackSystem = AppiumBy.accessibilityId("Kembali ke atas");


    // ==========================================
    // TEST CASES
    // ==========================================

    @Test(priority = 1)
    public void testBukaHalamanMulaiLari() {
        System.out.println("--- TEST 1: Buka Menu Mulai Lari ---");

        click(navMulaiLari);
        
        // Validasi: Cek apakah tombol Outdoor muncul
        waitForVisibility(btnOutdoor);
        Assert.assertTrue(driver.findElements(btnOutdoor).size() > 0, "Modal Pilihan Lari tidak muncul!");
        
        takeScreenshot("Menu_MulaiLari_Terbuka");
    }

    @Test(priority = 2)
    public void testPilihOutdoorDanSetting() {
        System.out.println("--- TEST 2: Pilih Outdoor & Handle Izin ---");

        // Klik Outdoor
        click(btnOutdoor);

        // Cek apakah muncul pop-up suruh ke pengaturan?
        // Kita pakai try-catch karena kalau izin sudah pernah dikasih, pop-up ini GAK MUNCUL.
        try {
            if (driver.findElements(btnPergiKePengaturan).size() > 0) {
                System.out.println("Pop-up Izin muncul, menuju Pengaturan...");
                click(btnPergiKePengaturan);
                
                // --- MASUK AREA SYSTEM SETTING (DANGER ZONE) ---
                handleOppoBatterySettings();
                
                // Setelah selesai di setting, kita asumsikan user sudah memencet back sampai balik ke app
                // Atau kita handle back button secara manual di fungsi handle
            } else {
                System.out.println("Pop-up Izin tidak muncul (Mungkin sudah diizinkan sebelumnya). Langsung lanjut.");
            }
        } catch (Exception e) {
            System.out.println("Error saat handling permission: " + e.getMessage());
        }

        // Validasi akhir: Harusnya sekarang sudah masuk ke halaman "Persiapan Lari" (Peta/GPS)
        // Tambahkan assertion sesuai halaman tujuan, misal tombol "Mulai" (Start)
        takeScreenshot("Ready_To_Run");
    }

    // --- FUNGSI KHUSUS HANDLING SETTING OPPO ---
    public void handleOppoBatterySettings() {
        try {
            Thread.sleep(2000); // Tunggu app setting loading
            
            // Logika di sini sangat bergantung pada apa yang tampil di layar HP mu saat ini.
            // Berdasarkan Excel, sepertinya harus klik "Izinkan Aktivitas Latar Belakang"
            
            // Coba klik opsi yang dilist di Excel
            // Hati-hati: Locator "LinearLayout[3]" itu index, rawan berubah kalau urutan menu beda.
            // Lebih aman cari by Text kalau ada, misal "Izinkan"
            
            System.out.println("Mencoba klik setting background...");
            
            // Cek apakah ada tombol "Izinkan" (Dialog System) langsung?
            if (driver.findElements(btnIzinkanSystem).size() > 0) {
                 click(btnIzinkanSystem);
            } else {
                // Kalau masuk ke menu list dulu
                // click(settingIzinkanLatarBelakang); // Sesuaikan alur real-nya
            }

            System.out.println("Selesai setting, mencoba kembali ke aplikasi...");
            
            // Tekan Back 2-3 kali sampai balik ke aplikasi kita
            // Kita pakai loop safety
            for(int i=0; i<3; i++) {
                // Cek apakah kita sudah balik ke app (misal cek apakah tombol Outdoor/Navigasi ada?)
                if (driver.findElements(navMulaiLari).size() > 0 || driver.findElements(btnOutdoor).size() > 0) {
                    break;
                }
                
                // Kalau masih di setting, klik back
                if (driver.findElements(btnBackSystem).size() > 0) {
                    click(btnBackSystem);
                } else {
                    driver.navigate().back(); // Back bawaan Android
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println("Gagal di menu setting: " + e.getMessage());
        }
    }
}