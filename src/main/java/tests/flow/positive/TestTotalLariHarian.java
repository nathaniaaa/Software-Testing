package tests.flow.positive;

import tests.BaseTest;
import tests.creation.TargetActionHelper;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestTotalLariHarian extends BaseTest {
    private TargetActionHelper targetPage;

    // Variabel untuk menyimpan Y Offset jika Target Progress muncul
    private double yOffset = 0.00;

    @BeforeClass
    public void setupPage() {
        targetPage = new TargetActionHelper((AndroidDriver) driver);
    }

    // Daftar Lokasi
    // Ikon Beranda (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");
    
    // Tombol Back (Bawaan Aplikasi)
    By btnBackHeader1 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.widget.Button");
    By btnBackHeader2 = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[1]/android.widget.Button[1]");
    By btnBackList = AppiumBy.xpath("//android.view.View[@content-desc=\"joined\"]");

    // Link 'Lihat Semua' 
    // Challenge yang Diikuti -> Challenge Saya
    By textLihatSemuaChallengeYangDiikuti = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[1]");
    
    // Challenges (Public Challenge) -> Halaman Navigasi Challenge
    By textLihatSemuaChallenges = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");

    By textLihatSemuaRiwayatLari = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");

    // container public challenge
    By wadahPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']/following-sibling::android.view.View[1]");
    
    // Card Public Challenge - dari beranda
    By cardPublicChallengeBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']/following-sibling::android.view.View[1]");

    // Locator untuk Teks Empty State
    By textBelumAdaChallenge = AppiumBy.xpath("//*[contains(@text, 'Belum ada challenge') or contains(@text, 'Tidak ada challenge')]");
    By textBelumAdaEvent = AppiumBy.xpath("//*[contains(@text, 'Belum ada event') or contains(@text, 'Tidak ada event')]");
    By textBelumAdaRiwayatLari = AppiumBy.xpath("//*[contains(@text, 'Belum ada riwayat') or contains(@text, 'Tidak ada riwayat')]");

    // locator header (untuk highlight section)
    By headerChallengeSaya = AppiumBy.xpath("//android.widget.TextView[@text='Challenge yang Diikuti']");
    By headerTotalLari = AppiumBy.xpath("//android.widget.TextView[@text='Total Lari Harian']");
    By headerPublicChallenge = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']");
    By headerEventLari = AppiumBy.xpath("//android.widget.TextView[@text='Event Lari']");
    By headerRiwayatLari = AppiumBy.xpath("//android.widget.TextView[@text='Riwayat Lari']");

    // Test Cases
    @Test(priority = 1, description = "Total lari harian")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini menampilkan rekap total jarak tempuh, total waktu lari, serta rata-rata kalori yang tercatat pada hari ini",
        note = "",
        group = "TOTAL LARI HARIAN"
    ) 
    public void testTotalLariHarian() {
        System.out.println("Test 1: Total lari harian");
        waitTime();

        // Cek apakah Target Progress muncul, kalau muncul adjust Y Offset untuk highlight supaya pas
        if (targetPage.isTargetProgressVisible()) {
            yOffset = 0.04; // Sesuaikan angka, misal jadinya kurang pas (bisa 0.02, 0.03, dll)
            System.out.println("INFO: Target Progress terlihat! UI terdorong ke bawah. Y Offset = " + yOffset);
        } else {
            yOffset = 0.0;
            System.out.println("INFO: Target Progress tidak ada. Y Offset normal (0.0)");
        }

        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");

        capture.highlightAndCapture(headerTotalLari, "Validasi Tampilan Total Lari Harian");
    }

    @Test(priority = 2, description = "Riwayat Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan daftar Riwayat Lari terakhir milik pengguna. Di halaman beranda dibatasi hanya 4 Riwayat Lari paling terbaru saja termasuk sync lari menggunakan smartwatch. Jika dibuka Lihat Semua, maka akan tampil daftar Riwayat Lari lengkap milik pengguna dan pindah ke tab Aktivitas",
        note = "",
        group = "TOTAL LARI HARIAN"
    ) 
    public void riwayatLari() {
        System.out.println("Test 2: Riwayat Lari");
        waitTime();

        actions.swipeVertical(0.8, 0.2);

        capture.highlightAndCapture(headerRiwayatLari, "Bagian Riwayat Lari");

        boolean isAkunKosong = driver.findElements(textBelumAdaRiwayatLari).size() > 0;

        if (isAkunKosong) {
            //skenario 1 -> akun kosong
            System.out.println("Kondisi: User belum memiliki Riwayat Lari apapun");
            
            capture.highlightAndCapture(textBelumAdaRiwayatLari, "Validasi: Teks Empty State Riwayat Lari");
        } else {
            // skenario 2 -> ada riwayat lari 
            System.out.println("Kondisi: User memiliki riwayat lari.");
        }
        actions.swipeVertical(0.2, 0.8);
    }

    // Helper
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    public void clickBack() {
        try {
            if (driver.findElements(btnBackHeader1).size() > 0) {
                clickTest(btnBackHeader1, "Klik back");
            } else if (driver.findElements(btnBackHeader2).size() > 0) {
                clickTest(btnBackHeader2, "Klik back");
            } else if (driver.findElements(btnBackList).size() > 0) {
                clickTest(btnBackList, "Klik back");
            } else {
                System.out.println("Tombol Back UI gak nemu, pakai Back HP");
                driver.navigate().back();
            }
            waitTime();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}
