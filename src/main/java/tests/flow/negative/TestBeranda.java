package tests.flow.negative;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;

public class TestBeranda extends BaseTest {
    // Daftar Lokasi
    // Ikon Beranda (Bottom Navigation)
    By navBeranda = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");

    // Link 'Lihat Semua' 
    // Challenge yang Diikuti -> Challenge Saya
    By textLihatSemuaChallengeYangDiikuti = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[1]");
    
    // // Card Challenge Saya - dari beranda 
    // // By cardChallengeSayaBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[1]/android.view.View[1]");
    // By cardChallengeSayaBeranda = AppiumBy.xpath("//android.view.View[preceding-sibling::android.widget.TextView[@text='Challenge yang Diikuti'] and following-sibling::android.widget.TextView[@text='Total Lari Harian']]");
    // // Card Public Challenge - dari beranda
    // By cardPublicChallengeBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]/android.view.View[1]");
    
    // // Card Riwayat Lari - dari beranda
    // By cardRiwayatBeranda = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.view.View[2]");

    // Card Challenge Saya - dari beranda (Sandwich XPath: Di antara judul dan Total Lari Harian)
    By cardChallengeSayaBeranda = AppiumBy.xpath("//android.view.View[preceding-sibling::android.widget.TextView[@text='Challenge yang Diikuti'] and following-sibling::android.widget.TextView[@text='Total Lari Harian']]");
    
    // Card Public Challenge - dari beranda (Wadah horizontal scroll-nya)
    By cardPublicChallengeBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Public Challenges']/following-sibling::android.view.View[1]");
    
    // Card Riwayat Lari - dari beranda (Card riwayat pertama setelah teks Riwayat Lari)
    By cardRiwayatBeranda = AppiumBy.xpath("//android.widget.TextView[@text='Riwayat Lari']/following-sibling::android.view.View[1]");

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
    @Test(priority = 1, description = "Pengguna tidak mengikuti challenges apapun")
    @TestInfo(
        testType = "Negative Case",
        expected = "Jika Pengguna tidak mengikuti challenges apapun maka daftar challenges yang diikuti akan kosong dihalaman beranda",
        note = "",
        group = "Beranda"
    )
    public void testEmptyStateChallengeSaya() {
        System.out.println("TEST 1: Pengguna tidak mengikuti challenges apapun");

        actions.swipeVertical(0.5, 0.9); // pastikan scroll ke atas dulu
        waitTime();

        // Wait dulu sampai halaman benar2 sudah muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(textLihatSemuaChallengeYangDiikuti));
        waitTime();

        // Cek, ada card atau engga 
        boolean isKosong = driver.findElements(textBelumAdaChallenge).size() > 0;

        // Capture & highlight
        capture.highlightAndCapture(headerChallengeSaya, "Bagian Challenge yang Diikuti (Challenge Saya)");

        if (isKosong) {
            // Skenario benar -> ga ada card, kosong 
            System.out.println("Card tidak ditemukan. Empty State tervalidasi.");
            capture.highlightAndCapture(textBelumAdaChallenge, "Validasi: Empty State Challenge Saya");
            logPass("Tidak ada Challenge yang Diikuti (Empty State).");
        } else {
            // Skenario skip -> ada card
            System.out.println("Card ditemukan. Gagal memvalidasi empty state.");
            logSkip("Test dilewati: Akun sudah memiliki challenge yang diikuti (tidak kosong).");
        }
    }

    @Test(priority = 2, description = "Tidak ada event")
    @TestInfo(
        testType = "Negative Case",
        expected = "Jika tidak ada event yang berlangsung, maka space Banner event akan dikecilkan dihalaman beranda",
        note = "",
        group = "Beranda"
    )
    public void testEmptyStateEventLari() {
        System.out.println("TEST 2: Tidak ada event");
        waitTime();

        // scroll
        actions.swipeVertical(0.6, 0.2);
        waitTime();

        // Cek, ada card atau engga 
        boolean isEventKosong = driver.findElements(textBelumAdaEvent).size() > 0;

        // Capture & highlight area kosong
        capture.highlightAndCapture(headerEventLari, "Bagian Event");

        if (isEventKosong) {
            // Skenario benar -> ga ada card, kosong 
            System.out.println("Text 'Tidak ada event' ditemukan. Empty State tervalidasi.");
            capture.highlightAndCapture(textBelumAdaEvent, "Validasi: Empty State Event");
            logPass("Tidak ada Event (Empty State).");
            
        } else {
            // Skenario skip -> ada card
            System.out.println("Card ditemukan. Gagal memvalidasi empty state.");
            logSkip("Test dilewati: ada event yang berlangsung (tidak kosong).");
        }
    }

    // JANGAN LUPA DI SESUAIKAN LAGII

    // Tebakan Struktur (Path) card "Exclusive Challenges" -> jangan lupa diganti klo ada card nya
    By cardExclusiveStruktur = AppiumBy.xpath("//android.widget.TextView[@text='Exclusive Challenges']/following-sibling::android.view.View[1]");
    
    // Tebakan Teks (Nama) -> cari teks apapun yang mengandung kata "Challenge Exclusive"
    By cardExclusiveText = AppiumBy.xpath("//*[contains(@text, 'Challenge Exclusive')]");

    // Header section
    By headerExclusive = AppiumBy.xpath("//android.widget.TextView[@text='Exclusive Challenges']");

    @Test(priority = 3, description = "Tidak ada Exclusive Challenges yang berlangsung")
    @TestInfo(
        testType = "Negative Case",
        expected = "Jika tidak ada Exclusive Challenges yang berlangsung, maka halaman card Exclusive Challenges akan kosong dihalaman beranda",
        note = "",
        group = "Beranda"
    )
    public void testEmptyStateExclusiveChallenge() {
        System.out.println("TEST 3: Tidak ada Exclusive Challenges yang berlangsung");
        waitTime();

        // cek elemen 
        boolean isHeaderAda = driver.findElements(headerExclusive).size() > 0;
        boolean isKetemuPakeStruktur = driver.findElements(cardExclusiveStruktur).size() > 0;
        boolean isKetemuPakeTeks = driver.findElements(cardExclusiveText).size() > 0;

        // Karena Exclusive ga ada, cari header utama 'Challenges' untuk di-highlight sebagai bukti
        capture.highlightAndCapture(headerPublicChallenge, "Bagian Challenges (Public Challenges)");

        // Klo 2-2 nya ga ada (ga nemu), berarti memang kosong
        if (!isHeaderAda && !isKetemuPakeStruktur && !isKetemuPakeTeks) {
            // Skenaario benar -> ga ada exclusive challenge (empty state - pass) 
            System.out.println("Header dan Card Exclusive tidak ditemukan. Empty State tervalidasi.");
            
            logPass("Validasi sukses: Section Exclusive Challenges tidak muncul karena tidak ada yang berlangsung.");
        } else {
            // Skenario skip -> ada exclusive challenge (bukan empty state - skip)
            System.out.println("Elemen Exclusive terdeteksi. Batal memvalidasi empty state");
            
            // Ambil bukti screenshot mana elemen yang ke-detect 
            if (isKetemuPakeTeks) {
                capture.highlightAndCapture(cardExclusiveText, "Bukti: Card Exclusive ternyata ada");
            } else if (isKetemuPakeStruktur) {
                capture.highlightAndCapture(cardExclusiveStruktur, "Bukti: Card Exclusive ternyata ada");
            } else {
                capture.highlightAndCapture(headerExclusive, "Bukti: Header Exclusive ternyata ada");
            }
            logSkip("Test dilewati: Saat ini sedang ada Exclusive Challenges yang berlangsung, tidak bisa memvalidasi tampilan kosong.");
        }
    }

    @Test(priority = 4, description = "Tidak ada Riwayat Lari")
    @TestInfo(
        testType = "Negative Case",
        expected = "Jika tidak ada Riwayat Lari, maka space Riwayat Lari akan kosong di halaman beranda",
        note = "",
        group = "Beranda"
    )
    public void testEmptyStateRiwayatLari() {
        System.out.println("TEST 4: Tidak ada Riwayat Lari");

        // Scroll
        if (driver.findElements(textBelumAdaRiwayatLari).size() == 0) {
            actions.swipeVertical(0.9, 0.2);
        }
        waitTime();

        // Cek, ada card atau engga 
        boolean isKosong = driver.findElements(textBelumAdaRiwayatLari).size() > 0;

        // Capture & highlight
        capture.highlightAndCapture(headerRiwayatLari, "Bagian Riwayat Lari");

        if (isKosong) {
            // Skenario benar -> ga ada card, kosong 
            System.out.println("Card tidak ditemukan. Empty State tervalidasi.");
            capture.highlightAndCapture(textBelumAdaRiwayatLari, "Validasi: Empty State Riwayat Lari");
            logPass("Tidak ada Riwayat Lari (Empty State).");
            
        } else {
            // Skenario skip -> ada card
            System.out.println("Card ditemukan. Gagal memvalidasi empty state.");
            logSkip("Test dilewati: Akun sudah memiliki Riwayat Lari (tidak kosong).");
        }
        actions.swipeVertical(0.2, 0.9);
    }

    public void waitTime() {
        try { Thread.sleep(3000); } catch (Exception e) {}
    }
}
