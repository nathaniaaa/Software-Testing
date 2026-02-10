package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestBeranda extends BaseTest {
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
    
    // Card Challenge Saya - dari beranda 
    By cardChallengeSayaBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[1]/android.view.View[1]");

    // Card Public Challenge - dari beranda
    By cardPublicChallengeBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]/android.view.View[1]");

    // Card Riwayat Lari - dari beranda
    By cardRiwayatBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]");

    // Test Cases
    @Test(priority = 1, description = "Pengguna Melihat Detail Total Lari Harian dari Beranda")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat Melihat Detail Total Lari Harian dari Beranda",
        note = "",
        group = "Beranda"
    ) 
    public void testTotalLariHarian() {
        System.out.println("Test 1: Pengguna dapat Melihat Detail Total Lari Harian dari Beranda");
        waitTime();

        logInfo("Tampilan awal Beranda");
        Assert.assertTrue(driver.findElement(navBeranda).isDisplayed(), "Gagal memuat halaman Beranda di awal test.");
        
        capture.highlightRectangleByRatio(0.05, 0.43, 0.90, 0.18, "Validasi Tampilan Total Lari Harian");
    }

    @Test(priority = 2, description = "Pengguna menekan tombol \"lihat semua\" pada Challenge yang diikuti")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan semua challenge yang telah diikuti maupun yang telah dibuat oleh Pengguna.",
        note = "",
        group = "Beranda"
    ) 
    public void testLihatSemuaChallengeYangDiikuti() {
        System.out.println("Test 2: Pengguna menekan tombol \\\"lihat semua\\\" pada Challenge yang diikuti");
        waitTime();

        capture.highlightRectangleByRatio(0.05, 0.25, 0.90, 0.15, "Bagian Challenge yang Diikuti (Challenge Saya)");

        // Klik lihat semua yg diikuti (Challenge Saya)
        System.out.println("Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        clickTest(textLihatSemuaChallengeYangDiikuti, "Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        
        // Tunggu 
        try { 
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.view.View"))); 
        } catch (Exception e) {}

        logPass("Berhasil klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).");

        waitTime();
        clickBack();
        
        logPass("Berhasil kembali dari 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya).");
    }

    @Test(priority = 3, description = "Challenges")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna bisa menampilkan halaman Challenge dengan menekan \"lihat semua\" pada Challenges (Public Challenges).",
        note = "",
        group = "Beranda"
    ) 
    public void challenges() {
        System.out.println("Test 3: Menampilkan detail daftar challenge yang mencakup Challenge Saya, Challenge Reward Januari, Exclusive Challenges, dan Public Challenge. \n" + //
                        "\n" + //
                        "Saat pengguna menekan 'Lihat Semua', aplikasi akan membuka tab Challenge untuk menampilkan daftar lengkapnya");
        waitTime();

        capture.highlightRectangleByRatio(0.05, 0.61, 0.90, 0.20, "Bagian Challenges (Public Challenges)");

        // Klik lihat semua yg punya challenges (Public Challenge)
        System.out.println("Klik 'Lihat Semua' (Public Challenge)");
        clickTest(textLihatSemuaChallenges, "Klik 'Lihat Semua' (Public Challenge)");
        waitTime();

        logPass("Berhasil klik 'Lihat Semua' Challenges (Public Challenge),");

        // Klik Bottom Navigation yang Beranda (keluar dari challenge)
        System.out.println(" Klik Bottom Nav 'Beranda'");
        clickTest(navBeranda, "Klik Bottom Nav 'Beranda'");
        waitTime();

        Assert.assertTrue(driver.findElement(navBeranda).isSelected() || driver.findElement(textLihatSemuaChallenges).isDisplayed(), "Gagal kembali ke Beranda via Bottom Nav.");

        logPass("Berhasil kembali ke Beranda.");
    }

    @Test(priority = 4, description = "Exclusive Challenges")
    @TestInfo(
        testType = "Positive Case",
        expected = "Challenge spesial Ayolari yang dibuat saat event-event lari tertentu, Pengguna dapat bergabung dalam Exclusive Challenges jika telah memenuhi syarat dan ketentuan yang berlaku yang dibuat oleh admin pembuat Exclusive Challenges tersebut",
        note = "",
        group = "Beranda"
    ) 
    public void exclusiveChallenges() {
        System.out.println("Test 4: Challenge spesial Ayolari yang dibuat saat event-event lari tertentu, Pengguna dapat bergabung dalam Exclusive Challenges jika telah memenuhi syarat dan ketentuan yang berlaku yang dibuat oleh admin pembuat Exclusive Challenges tersebut");
        waitTime();

        capture.highlightRectangleByRatio(0.05, 0.61, 0.90, 0.22, "Bagian Challenges (Public Challenges)");
    }

    @Test(priority = 5, description = "Public Challenges")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini menampilkan daftar Public Challenge yang sedang berlangsung. Saat pengguna memilih salah satu challenge, detail lengkap challenge tersebut akan ditampilkan",
        note = "",
        group = "Beranda"
    ) 
    public void publicChallenges() {
        logInfo("Tampilan awal");

        // Scroll horizontal di Challenges (Public Challenge)
        System.out.println("Scroll Horizontal 'Public Challenges'");
        actions.swipeHorizontal(0.9, 0.1, 0.7);
        waitTime();

        actions.swipeHorizontal(0.1, 0.9, 0.7);
        waitTime();

        // Klik card Public Challenges (card di Beranda)
        System.out.println(" Klik Salah Satu Card 'Public Challenges' di Beranda");
        
        // LOGIC: Cek apakah Card Pertama (Index 2) Ada?
        if (driver.findElements(cardPublicChallengeBeranda).size() > 0) {
            // Kondisi: Ada Card di List
            System.out.println("Card ditemukan di List. Klik Card Pertama.");
            clickTest(cardPublicChallengeBeranda, getScreenshotBase64());
            waitTime();

            // Back ke Beranda
            System.out.println("Back ke Beranda");
            clickBack(); // Back dari Detail
            waitTime();
            
            logPass("Berhasil kembali ke Beranda");
            
        } else {
            // Kondisi: List Kosong
            System.out.println("List Kosong / Card tidak ditemukan.");
            logInfo("List Public Challenges kosong");
        }
        waitTime();
    }
    
    @Test(priority = 6, description = "Event Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Fitur ini menampilkan berbagai banner Event Lari. Ketika pengguna menekan salah satu banner, akan ditampilkan informasi lengkap mengenai Event Lari tersebut",
        note = "",
        group = "Beranda"
    ) 
    public void eventLari() {
        System.out.println("Test 6: Fitur ini menampilkan berbagai banner Event Lari. Ketika pengguna menekan salah satu banner, akan ditampilkan informasi lengkap mengenai Event Lari tersebut");
        waitTime();

        actions.swipeVertical(0.8, 0.2);

        capture.highlightRectangleByRatio(0.05, 0.23, 0.90, 0.11, "Bagian Challenges (Public Challenges)");
    }

    @Test(priority = 7, description = "Riwayat Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Menampilkan daftar Riwayat Lari terakhir milik pengguna. Di halaman beranda dibatasi hanya 4 Riwayat Lari paling terbaru saja termasuk sync lari menggunakan smartwatch.\n" + //
                        "\n" + //
                        "Jika dibuka Lihat Semua, maka akan tampil daftar Riwayat Lari lengkap milik pengguna dan pindah ke tab Aktivitas",
        note = "",
        group = "Beranda"
    ) 
    public void riwayatLari() {
        System.out.println("Test 7: Menampilkan daftar Riwayat Lari terakhir milik pengguna. Di halaman beranda dibatasi hanya 4 Riwayat Lari paling terbaru saja termasuk sync lari menggunakan smartwatch.\n" + //
                        "\n" + //
                        "Jika dibuka Lihat Semua, maka akan tampil daftar Riwayat Lari lengkap milik pengguna dan pindah ke tab Aktivitas");
        waitTime();

        capture.highlightRectangleByRatio(0.05, 0.32, 0.90, 0.22, "Bagian Challenges (Public Challenges)");
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
