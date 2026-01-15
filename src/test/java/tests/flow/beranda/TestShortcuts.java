package tests.flow.beranda;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestShortcuts extends BaseTest {

    // 1. BOTTOM NAVIGATION (Untuk Step 8 & 13)
    // Locator tombol Beranda di menu bawah
    By navBeranda = AppiumBy.xpath("//android.view.View[@content-desc='Beranda'] | //android.widget.TextView[@text='Beranda']");

    // 2. HEADER BACK (Untuk Step 6, 10, 16)
    By btnBackHeader = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.widget.Button");

    // 3. ITEMS & LINKS
    // Link 'Lihat Semua' (Index 1=MyChall, 2=Public, 3=Riwayat - asumsi urutan muncul)
    By textLihatSemua = AppiumBy.xpath("//android.widget.TextView[@text='Lihat Semua']");
    
    // Card di List Challenge Saya (Saat masuk Lihat Semua)
    By cardInListMyChallenge = AppiumBy.xpath("(//android.widget.TextView[@text='Pelari FOMO'])[1]");
    
    // Card Public di Home (Fun Run)
    By cardFunRunHome = AppiumBy.xpath("//android.widget.TextView[@text='Fun Run']");
    
    // Card Riwayat di Home (Aktivitas Lari)
    By cardHistoryHome = AppiumBy.xpath("//android.widget.TextView[@text='Aktivitas Lari']");


    // ==========================================
    // TEST CASES (URUT DARI ATAS KE BAWAH)
    // ==========================================

    @Test(priority = 1)
    public void testHomeInteractionFlow() {
        System.out.println("--- START TEST: Home Shortcuts & Navigation Flow ---");
        
        // 1) Begitu app kebuka dia udh di home (Reset posisi ke atas)
        actions.scrollToTop();
        santai();

        // 2) Scroll ke bawah 0,7 - 0,4 dan sebaliknya buat nunjukin aja (Demo Scroll)
        System.out.println("STEP 2: Demo Scroll Vertical Home...");
        actions.swipeVertical(0.7, 0.4); // Turun
        santai();
        actions.swipeVertical(0.4, 0.7); // Naik lagi
        santai();

        // 3) Mainan scroll horizontal punya di Challenge yang Diikuti
        System.out.println("STEP 3: Scroll Horizontal 'Challenge Saya'...");
        // Area ini ada di bagian atas, kita swipe di ketinggian Y=0.35
        actions.swipeHorizontal(0.9, 0.1, 0.35); // Geser Kiri
        santai();
        actions.swipeHorizontal(0.1, 0.9, 0.35); // Balik Kanan
        santai();

        // 4) Klik lihat semua yg Challenge Saya
        System.out.println("STEP 4: Klik 'Lihat Semua' (Challenge Saya)...");
        driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[1]")).click();
        santai();

        // 5) Klik salah 1 card (Di dalam List)
        System.out.println("STEP 5: Klik Card 'Pelari FOMO' di List...");
        wait.until(ExpectedConditions.elementToBeClickable(cardInListMyChallenge)).click();
        santai();

        // 6) Balik 2x (Detail -> List -> Home)
        System.out.println("STEP 6: Back 2x ke Home...");
        clickBack(); // Back dari Detail
        clickBack(); // Back dari List (Home)
        
        // --- SELESAI SECTION ATAS ---

        // 7) Skrg mainan scroll horizontal nya di Challenges (Public)
        System.out.println("STEP 7: Cari & Scroll Horizontal 'Public Challenges'...");
        // Scroll dulu biar section nya kelihatan
        actions.scrollToText("Fun Run"); 
        
        // Swipe horizontal di area tengah layar (Y=0.6 kira-kira)
        actions.swipeHorizontal(0.9, 0.1, 0.6);
        santai();
        actions.swipeHorizontal(0.1, 0.9, 0.6);
        santai();

        // 8) Klik lihat semua yg punya challenges (Public Chall)
        System.out.println("STEP 8: Klik 'Lihat Semua' (Public Challenge)...");
        // Cari tombol lihat semua yang visible (biasanya index ke-2 setelah scroll)
        try {
            driver.findElement(textLihatSemua).click();
        } catch (Exception e) {
            // Backup locator
             driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[2]")).click();
        }
        santai();

        // 9) Abis tu scroll dikit aja (Navigasi di halaman challenges)
        System.out.println("STEP 9: Scroll dikit di halaman List Public...");
        actions.swipeVertical(0.7, 0.5);
        santai();

        // 10) Klik bot nav yg beranda (Cara keluar dari list)
        System.out.println("STEP 10: Klik Bottom Nav 'Beranda'...");
        driver.findElement(navBeranda).click();
        santai();

        // 11) Klik salah 1 card di challenges (Di Home)
        System.out.println("STEP 11: Klik Card 'Fun Run' di Home...");
        // Pastikan card terlihat (Scroll text lagi)
        actions.scrollToText("Fun Run");
        driver.findElement(cardFunRunHome).click();
        santai();

        // 12) Back 1x (Balik ke Beranda)
        System.out.println("STEP 12: Back ke Beranda...");
        clickBack();

        // --- SELESAI SECTION TENGAH ---

        // 13) Scroll beranda nya (Cari Riwayat)
        System.out.println("STEP 13: Scroll cari 'Riwayat Lari'...");
        actions.scrollToText("Riwayat Lari");
        santai();

        // 14) Pilih lihat semua yg punya riwayat lari
        System.out.println("STEP 14: Klik 'Lihat Semua' (Riwayat Lari)...");
        try {
            driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='Lihat Semua'])[last()]")).click();
        } catch (Exception e) {
            driver.findElement(textLihatSemua).click();
        }
        santai();

        // 15) Ini bakal ke pop up ke navigasi aktivitas -> Jdi trs pilih bot nav yg beranda
        System.out.println("STEP 15: Pindah Tab Aktivitas -> Klik Bottom Nav 'Beranda'...");
        driver.findElement(navBeranda).click();
        santai();

        // 16) Scroll lgi ke bawah (Cari Card Riwayat di Home)
        System.out.println("STEP 16: Scroll lagi cari Card Riwayat...");
        actions.scrollToText("Aktivitas Lari");
        santai();

        // 17) Pilih salah 1 card di riwayat lari
        System.out.println("STEP 17: Klik Card 'Aktivitas Lari'...");
        driver.findElement(cardHistoryHome).click();
        santai();

        // 18) Back 1x (Balik ke Beranda)
        System.out.println("STEP 18: Back ke Beranda (Finish)...");
        clickBack();
        
        // Reset ke atas
        actions.scrollToTop();
    }

    // ==========================================
    // HELPER FUNCTIONS
    // ==========================================

    public void santai() {
        try { Thread.sleep(2500); } catch (Exception e) {}
    }

    public void clickBack() {
        try {
            if (driver.findElements(btnBackHeader).size() > 0) {
                driver.findElement(btnBackHeader).click();
            } else {
                System.out.println("   (Tombol Back UI gak nemu, pakai Back HP)");
                driver.navigate().back();
            }
            santai();
        } catch (Exception e) {
            driver.navigate().back();
        }
    }
}