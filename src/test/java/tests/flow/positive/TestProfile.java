package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestProfile extends BaseTest {

    // Daftar Lokasi
    By navSaya = AppiumBy.xpath("//android.widget.Button[@text=\"Saya Saya\"]");

    // Validasi halaman - Total Jarak
    By labelTotalJarak = AppiumBy.xpath("//android.widget.TextView[@text='Total Jarak']");

    // Test Cases
    @Test(priority = 1, description = "Total Jarak, Durasi Lari, dan Total Aktivitas Lari di halaman Profil Kamu")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan Total Jarak, Durasi Lari, dan Total Aktivitas Lari pengguna",
        note = "",
        group = "Profile"
    ) 
    public void testStatistikProfil() {
        System.out.println("Test 1: Total Jarak, Durasi Lari, dan Total Aktivitas Lari di halaman Profil Kamu");

        // Klik 'Saya' di bottom navigation
        wait.until(ExpectedConditions.elementToBeClickable(navSaya));
        clickTest(navSaya, "Klik Button Navigation");
        
        boolean isStatsVisible = false;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(labelTotalJarak));
            isStatsVisible = true;
        } catch (Exception e) {
            isStatsVisible = false;
        }

        if (isStatsVisible) {
            // Aksi jika elemen ditemukan
            System.out.println("Statistik Profil ditemukan");
            
            capture.highlightRectangleByRatio(0.05, 0.27, 0.90, 0.15, 
                "Validasi Tampilan Statistik (Jarak, Durasi, Aktivitas)");
            
        } else {
            // Elemen tidak ditemukan - Gagal
            System.err.println("Gagal: Statistik Profil tidak muncul!");
            
            // Log info screenshot biasa tanpa highlight
            logInfo("Statistik tidak ditemukan di layar.");
            Assert.fail("Elemen Statistik Profil tidak ditemukan.");
        }
    }
}
