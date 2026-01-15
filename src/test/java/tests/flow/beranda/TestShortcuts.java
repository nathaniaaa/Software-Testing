package tests.flow.beranda;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestShortcuts extends BaseTest {

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
    // Challenges (Public Challenge) -> Halaman Navigasi Challenge (kalau dari Bottom Navigation)
    By textLihatSemuaChallenges = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");
    By textLihatSemuaRiwayatLari = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[2]");
    
    // Card di List Challenge Saya (saat masuk Lihat Semua yang Challenge yang Diikuti)
    By cardInListMyChallenge = AppiumBy.xpath("(//android.widget.TextView[@text='Pelari FOMO'])[1]");
    
    // Card Challenges (Public Challenge) di Beranda (Daily Run)
    By cardDailyRunBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Daily Run']");
    
    // Card Riwayat Lari di Beranda (Aktivitas Lari)
    By cardRiwayatBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Aktivitas Lari']");

    // Test Cases
    @Test(priority = 1)
    public void testBerandaInteractionFlow() {
        System.out.println("START TEST: Beranda Shortcuts & Navigation Flow");
        waitTime();

        // Scroll atas - bawah Beranda 
        System.out.println("STEP 2: Scroll Vertical Beranda");
        actions.swipeVertical(0.7, 0.4); // Turun
        waitTime();
        actions.swipeVertical(0.4, 0.7); // Naik lagi
        waitTime();

        // Scroll horizontal di Challenge yang Diikuti
        System.out.println("STEP 3: Scroll Horizontal 'Challenge Saya'");
        actions.swipeHorizontal(0.9, 0.1, 0.35); // Geser Kiri
        waitTime();
        actions.swipeHorizontal(0.1, 0.9, 0.35); // Balik Kanan
        waitTime();

        // Klik Lihat Semua di Challenge yang Diikuti (Challenge Saya)
        System.out.println("STEP 4: Klik 'Lihat Semua' di Challenge yang Diikuti (Challenge Saya)");
        driver.findElement(textLihatSemuaChallengeYangDiikuti).click();
        waitTime();

        // Klik salah 1 card (di dalam List)
        System.out.println("STEP 5: Klik Card 'Pelari FOMO' di List");
        wait.until(ExpectedConditions.elementToBeClickable(cardInListMyChallenge)).click();
        waitTime();

        // Back 2x (Detail -> List -> Beranda)
        System.out.println("STEP 6: Back 2x ke Beranda");
        clickBack(); // Back dari Detail
        waitTime();
        clickBack(); // Back dari List (Beranda)
        waitTime();

        // Scroll horizontal di Challenges (Public Challenge)
        System.out.println("STEP 7: Scroll Horizontal 'Public Challenges'");
        actions.swipeHorizontal(0.9, 0.1, 0.7);
        waitTime();
        actions.swipeHorizontal(0.1, 0.9, 0.7);
        waitTime();

        // Klik card Daily Run (card di Beranda)
        System.out.println("STEP 7B: Klik Card 'Daily Run' di Beranda (Public Challenges)");
        driver.findElement(cardDailyRunBeranda).click();
        waitTime();

        clickBack(); // Balik ke Beranda
        waitTime();

        // Klik lihat semua yg punya challenges (Public Challenge)
        System.out.println("STEP 8: Klik 'Lihat Semua' (Public Challenge)");
        driver.findElement(textLihatSemuaChallenges).click();
        waitTime();

        // Scroll dikit aja (lihat halaman sebentar)
        System.out.println("STEP 9: Scroll dikit");
        actions.swipeVertical(0.7, 0.5);
        waitTime();

        actions.swipeVertical(0.5, 0.7);
        waitTime();

        // Klik Bottom Navigation yang Beranda (keluar dari challenge)
        System.out.println("STEP 10: Klik Bottom Nav 'Beranda'");
        driver.findElement(navBeranda).click();
        waitTime();

        // Scroll Beranda untuk cari Riwayat Lari
        System.out.println("STEP 13: Scroll cari 'Riwayat Lari'");
        actions.swipeVertical(0.9, 0.3);
        waitTime();

        // Klik lihat semua yg punya riwayat lari
        System.out.println("STEP 14: Klik 'Lihat Semua' (Riwayat Lari)");
        driver.findElement(textLihatSemuaRiwayatLari).click();
        waitTime();

        // Ini bakal ke pop up ke navigasi aktivitas -> Jdi trs pilih bot nav yg beranda
        System.out.println("STEP 15: Pindah Tab Aktivitas -> Klik Bottom Nav 'Beranda'");
        driver.findElement(navBeranda).click();
        waitTime();

        // Scroll lgi ke bawah (Cari Card Riwayat di Beranda)
        System.out.println("STEP 16: Scroll lagi cari Card Riwayat");
        actions.swipeVertical(0.9, 0.3);
        waitTime();

        // Klik salah 1 card di riwayat lari
        System.out.println("STEP 17: Klik Card 'Aktivitas Lari'");
        driver.findElement(cardRiwayatBeranda).click();
        try { Thread.sleep(5000); } catch (Exception e) {}

        // Back 1x (Balik ke Beranda)
        System.out.println("STEP 18: Back ke Beranda (Finish)");
        clickBack();
        
        // Reset ke atas
        actions.scrollToTop();
    }

    // Helper 
    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    public void clickBack() {
        try {
            if (driver.findElements(btnBackHeader1).size() > 0) {
                driver.findElement(btnBackHeader1).click();
            } else if (driver.findElements(btnBackHeader2).size() > 0) {
                driver.findElement(btnBackHeader2).click();
            } else if (driver.findElements(btnBackList).size() > 0) {
                driver.findElement(btnBackList).click();
            } else {
                System.out.println("   (Tombol Back UI gak nemu, pakai Back HP)");
                driver.navigate().back();
            }
            waitTime();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}