package tests.creation;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import tests.helper.CaptureHelper;
import tests.utils.TestListener;
import tests.utils.data.ChallengeData;

public class ChallengeActionHelper extends CreationActionHelper {

    protected CaptureHelper capture;
        // --- CONSTRUCTOR ---
    public ChallengeActionHelper(AndroidDriver driver) {
        super(driver);
        this.capture = new CaptureHelper(driver);
    }

    // --- LOCATORS ---
    private final By BTN_BERANDA_TAB = AppiumBy.xpath("//android.widget.Button[@text=\"Beranda Beranda\"]");
    private final By BTN_CHALLENGE_TAB = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");
    private final By BTN_TO_CREATE_MENU = AppiumBy.xpath("//android.view.View[@content-desc=\"create-run\"]");
    private final By BTN_SEE_ALL_CHALLENGES = AppiumBy.xpath("(//android.widget.TextView[@text=\"Lihat Semua\"])[1]");
    private final By BTN_EDIT_CHALLENGE = AppiumBy.xpath("//android.widget.Button[@text=\"icon\"]");
    private final By BTN_TO_DETAIL_CHALLENGE = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[2]/android.view.View[1]/android.view.View");
    private final By BTN_KELOLA_CHALLENGE = AppiumBy.xpath("//android.widget.Button[@text=\"Kelola Challenge\"]");
    private final By BTN_SHARE_CHALLENGE = AppiumBy.xpath("//android.widget.Button[@text=\"Bagikan Challenge\"]");
    private final By BTN_SHARE_WHATSAPP = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.android.intentresolver:id/text1\" and @text=\"WhatsApp\"]");
    private final By BTN_WHATSAPP_SEND = AppiumBy.xpath("//android.widget.ImageButton[@content-desc=\"Kirim\"]"); 
    private final By BTN_TO_TAB_PERSETUJUAN = AppiumBy.xpath("//android.view.View[@resource-id=\"radix-_r_c_-trigger-persetujuan\"]");
    private final By BTN_TO_ACCEPT_ONE = AppiumBy.xpath("//android.view.View[@resource-id=\"radix-_r_i_-content-persetujuan\"]/android.widget.TextView[4]");
    private final By BTN_TO_ACCEPT_ALL = AppiumBy.xpath("(//android.widget.Button[@text=\"Semua\"])[2]");
    private final By BTN_TO_REJECT_ONE = AppiumBy.xpath("//android.view.View[@resource-id=\"radix-_r_i_-content-persetujuan\"]/android.widget.TextView[3]");
    private final By BTN_TO_REJECT_ALL = AppiumBy.xpath("(//android.widget.Button[@text=\"Semua\"])[1]");
    private final By BTN_KICK_OUT = AppiumBy.xpath("//android.widget.Button[@text=\"Kick Out\"]");
    private final By BTN_CONFIRM_LANJUTKAN = AppiumBy.xpath("//android.widget.Button[@text=\"Ya, Lanjutkan\"]");
    private final By BTN_TO_TAB_PESERTA = AppiumBy.xpath("//android.view.View[@resource-id=\"radix-_r_c_-trigger-peserta\"]"); 
    private final By BTN_BUAT_CHALLENGE = AppiumBy.xpath("//android.widget.Button[@text=\"Buat Challenge\"]");
    private final By INPUT_NAME = AppiumBy.xpath("//*[contains(@text, 'Nama Challenge')]/following-sibling::android.widget.EditText");
    private final By CONFIRM_DATE_BTN = AppiumBy.xpath("//android.widget.Button[@text=\"OK\"]");
    private final By CANCEL_DATE_BTN = AppiumBy.xpath("//android.widget.Button[@text=\"Cancel\"]");
    private final By FIELD_NAMA = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.widget.EditText[1]");
    private final By FIELD_JARAK = AppiumBy.xpath("//android.widget.EditText[@text=\"Masukan jarak\"]");
    private final By FIELD_DATES = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.widget.EditText[3]");
    private final By FIELD_TIME = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[5]/android.view.View[2]");
    private final By FIELD_DESCRIPTION = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.widget.EditText[4]");
    private final By FIELD_TERMS = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.widget.EditText[2]");
    private final By FIELD_BADGE = AppiumBy.xpath("//android.widget.TextView[@text='Badge Pemenang']/following-sibling::android.view.View");private final By FIELD_JML_PESERTA = AppiumBy.xpath("//android.view.View[@text=\"10\"]");
    private final By TOGGLE_PRIVATE = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.widget.TextView[3]");
    private final By CONFIRM_BADGE = AppiumBy.xpath("//android.widget.Button[@text=\"Pilih\"]");
    private final By FIELD_POSTER = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"dropzone-file\"]");
    private final By PICTURE_ONE = AppiumBy.xpath("//android.view.View[@content-desc=\"Petak media\"]/android.view.View/android.view.View[4]/android.view.View[2]/android.view.View[1]/android.view.View");
    private final By PICTURE_TWO = AppiumBy.xpath("//android.view.View[@content-desc=\"Petak media\"]/android.view.View/android.view.View[7]/android.view.View[2]/android.view.View");
    private final By BTN_CONFIRM_POSTER = AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[6]/android.view.View/android.view.View[3]/android.widget.Button");
    private final By BTN_SUBMIT_CHALLENGE = AppiumBy.xpath("//android.widget.Button[@text=\"Buat Challenge\"]");
    private final By BTN_KOLEKSI = AppiumBy.xpath("//android.widget.TextView[@text=\"Koleksi\"]");
    private final By BTN_THIS_DEVICE = AppiumBy.xpath("//android.widget.TextView[@text=\"Dari perangkat ini\"]");
    private final By BTN_DOWNLOAD_FOLDER = AppiumBy.xpath("//android.view.View[@content-desc=\"Download\"]");
    private final By PICTURE_DOWNLOAD = AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.view.View[1]/android.view.View[2]/android.view.View");



    private final By TEXT_CHALLENGE_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Challenge Lari\"]");
    private final By TEXT_BERANDA_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Challenge yang Diikuti\"]");
    private final By ADD_CHALLENGE_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Virtual Run\"]");
    private final By SEE_ALL_CHALLENGES_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Daftar Challenge yang kamu buat atau ikuti.\"]");
    private final By DETAIL_CHALLENGE_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Rincian Challenge\"]");
    private final By EDIT_CHALLENGE_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Edit Challenge\"]");
    private final By KELOLA_CHALLENGE_PAGE = AppiumBy.xpath("//android.widget.TextView[@text=\"Ajak Temanmu Bergabung dan Ikuti Challenge Bersama!\"]");
    private final By ERROR_INVALID_TIME = AppiumBy.xpath("//android.widget.TextView[@text=\"Format waktu tidak valid\"]");
    private final By ERROR_IMAGE_SIZE = AppiumBy.xpath("//android.widget.TextView[@text=\"Ukuran file maksimal 5MB\"]");
    
    // ==========================================
    // A. NAVIGATION & SETUP
    // ==========================================

    public boolean isChallengeDashboard() {
        return isElementPresent(TEXT_CHALLENGE_PAGE, 3);
    }

    public boolean isAddChallengeMenu() {
        return isElementPresent(ADD_CHALLENGE_PAGE, 3);
    }

    public boolean isSeeAllChallengesPage() {
        return isElementPresent(SEE_ALL_CHALLENGES_PAGE, 3);
    }

    public boolean isEditChallengePage() {
        return isElementPresent(EDIT_CHALLENGE_PAGE, 3);
    }

    public boolean isDetailChallengePage() {
        return isElementPresent(DETAIL_CHALLENGE_PAGE, 3);
    }

    public boolean isKelolaChallengePage() {
        return isElementPresent(KELOLA_CHALLENGE_PAGE, 3);
    }

    public void navigateToCreateMenu(boolean screenshot) {
        System.out.println("   -> [Nav] Navigating to Create Challenge Menu...");

        // ALREADY THERE
        if (isAddChallengeMenu()) {
            System.out.println("   -> Already on Creation Menu. Skipping navigation.");
            return;
        }

        // B. ON DASHBOARD?
        if (!isChallengeDashboard()) {
            System.out.println("   -> Not on Dashboard. Switching Tab...");
            navigateToChallengeDashboard(screenshot);
        }

        // C. CLICK ADD BUTTON
        System.out.println("   -> Clicking Add Button...");
        try {
            // Check if the floating button exists before clicking
            if (isElementPresent(BTN_TO_CREATE_MENU, 3)) {
                tap(BTN_TO_CREATE_MENU, "Click (+) Button", screenshot);
            } else {
                throw new Exception("Button locator failed");
            }
        } catch (Exception e) {
            System.out.println("   -> (+) Button not found via Locator. Using Coordinate Fallback (0.85, 0.25).");
            tapAtScreenRatio(0.85, 0.25, screenshot); // Keeping your requested coordinates
        }

        // Final Check
        if (!isAddChallengeMenu()) {
            System.out.println("   -> WARN: Navigation might have failed. Input field not detected.");
        }
    }

    public void navigateToDetailChallenge(String partialName, boolean screenshot) {
        System.out.println("   -> [Nav] Navigating to Edit Challenge Page...");

        if (isDetailChallengePage()) {
            System.out.println("   -> On Detail Page. Proceeding to click Edit...");
            scrollToTop();
            return;
        }

        if (!isSeeAllChallengesPage()) {
            if (!isChallengeDashboard()) {
                System.out.println("   -> Not on Dashboard. Navigating there first...");
                navigateToChallengeDashboard(screenshot); 
            }
            
            System.out.println("   -> Clicking 'Lihat Semua' (See All)...");
            try {
                tap(BTN_SEE_ALL_CHALLENGES, "Tap 'Lihat Semua' Challenges", screenshot);
                Thread.sleep(1500); // Brief wait for transition
            } catch (Exception e) {
                System.out.println("WARN: Failed to click 'Lihat Semua'.");
            }
        }

        // 4. ON "SEE ALL" PAGE: Click a challenge card
        System.out.println("   -> Clicking a Challenge Card to view details...");
        try {
            scrollToText(partialName); 
            
            tapButtonByTextOrId(partialName, partialName, screenshot);
            
            // tap(BTN_TO_DETAIL_CHALLENGE, "Tap Challenge Card");
            Thread.sleep(1500); // Brief wait for Detail Page to load
        } catch (Exception e) {
            System.out.println("WARN: Failed to click Challenge Card. Using Fallback.");
            tapAtScreenRatio(0.50, 0.253, screenshot); // Fallback: Tap center of where the card should be
        }
        
        // Final Validation Check
        if (!isDetailChallengePage()) {
            System.out.println("   -> WARN: Navigation may have failed. See All Challenges page not detected.");
        }
    }

    public void navigateToEditChallenge(boolean screenshot) {
        System.out.println("   -> Navigating to Edit Page...");
        try {
            tap(BTN_EDIT_CHALLENGE, "Click Edit Button", screenshot);
        } catch (Exception e) {
            System.out.println("   -> Standard edit button not found. Using fallback coordinates.");
            tapAtScreenRatio(0.925, 0.061, screenshot);
        }
    }
    
    public void navigateToSeeAllChallengesPage(boolean screenshot) {
        System.out.println("   -> Navigating to See All Challenges Page...");
        try {
            tap(BTN_SEE_ALL_CHALLENGES, "Tap See All Challenges Button", screenshot);
        } catch (Exception e) {
            System.out.println("   -> See All Challenges button not found.");
        }
    }

    public void navigateToChallengeDashboard(boolean screenshot) {
        try {
            tap(BTN_CHALLENGE_TAB, "Tap Challenge Tab", screenshot);
        } catch (Exception e) {
            tapAtScreenRatio(0.65, 0.93, screenshot); // Fallback for Tab
        }
        
        // Wait for dashboard to load
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(TEXT_CHALLENGE_PAGE));
        } catch (Exception e) {
            System.out.println("   -> Dashboard load check timed out (Non-fatal)");
        }

        scrollToTop();
    }

        public void navigateToBeranda(boolean screenshot) {
        try {
            tap(BTN_BERANDA_TAB, "Tap Beranda Tab", screenshot);
        } catch (Exception e) {
            tapAtScreenRatio(0.100, 0.9567, screenshot); // Fallback for Tab
        }
        
        // Wait for dashboard to load
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(TEXT_BERANDA_PAGE));
        } catch (Exception e) {
            System.out.println("   -> Dashboard load check timed out (Non-fatal)");
        }

        scrollToTop();
    }

    public void navigateToKelolaChallenge(String ChallengeName, boolean screenshot) {
        System.out.println("   -> [Nav] Navigating to Kelola Challenge Page...");

        // 1. ALREADY THERE? (Menggunakan locator baru Anda)
        if (isKelolaChallengePage()) {
            System.out.println("   -> Already on Kelola Challenge Page. Skipping navigation.");
            return;
        }

        // 2. ON DETAIL PAGE?
        if (isDetailChallengePage()) {
            System.out.println("   -> On Detail Page. Proceeding to click Kelola...");
            tap(BTN_KELOLA_CHALLENGE, "Tap Kelola Challenge Button", screenshot);
            
            // Validasi setelah klik
            if (!isKelolaChallengePage()) {
                System.out.println("   -> WARN: Navigation may have failed. Kelola page not detected.");
            }
            return;
        }

        //  NEED TO START FROM DASHBOARD?
        if (!isSeeAllChallengesPage()) {
            navigateToDetailChallenge(ChallengeName, screenshot);
        }

        // 5. FINALLY: Click Kelola Challenge
        tap(BTN_KELOLA_CHALLENGE, "Tap Kelola Challenge Button", screenshot);
        
        // 6. FINAL VALIDATION (Menggunakan locator baru Anda)
        if (!isKelolaChallengePage()) {
            System.out.println("   -> WARN: Navigation may have failed. Kelola page not detected.");
        }
    }

    /**
     * Safely navigates back to the Dashboard by pressing Back repeatedly 
     * until the "Challenge Saya" header is found.
     * Handles cases where the app has "phantom" history stacks.
     */
    public void navigateBackToDashboardSafe() {
        System.out.println("   -> [Nav] Navigating back to Dashboard (Safe Mode)...");
        
        int maxAttempts = 7; // Prevent infinite loops
        int attempt = 0;

        while (attempt < maxAttempts) {
            // 1. Check if we are already on the Dashboard
            if (isTextVisible("Challenge Saya") && isTextVisible("Challenge Lari")) {
                System.out.println("      -> Arrived at Dashboard.");
                scrollToTop();
                return; // Success! Stop pressing back.
            }

            // 2. If not, press Back
            System.out.println("      -> Not on Dashboard yet. Pressing Back (" + (attempt + 1) + ")...");
            driver.navigate().back();
            
            // 3. Wait a moment for page transition
            try { Thread.sleep(1500); } catch (Exception e) {}
            
            attempt++;
        }

        
        System.out.println("WARN: Could not return to Dashboard after " + maxAttempts + " attempts.");
    }

    public boolean isTimeErrorDisplayed() {
        try {
            // Tunggu maksimal 3 detik untuk pesan error muncul
            boolean isDisplayed = isElementPresent(ERROR_INVALID_TIME, 3);
            if (isDisplayed) {
                capture.highlightAndCapture(ERROR_INVALID_TIME, "Pesan error waktu tidak valid muncul");
            }
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    public void kickOutParticipant(String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai alur Kick Out Peserta...");

        // 1. Pastikan berada di halaman Kelola Challenge
        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Berpindah ke Tab Peserta
        System.out.println("   -> Berpindah ke Tab Peserta...");
        try {
            tap(BTN_TO_TAB_PESERTA, "Switch ke Tab Peserta");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Tab Peserta.");
            tapAtScreenRatio(0.723, 0.417);
        }
        try { Thread.sleep(2000); } catch (Exception ignored) {} // Tunggu data me-render

        // 3. Kick Out Peserta
        System.out.println("   -> Mengeluarkan (Kick Out) 1 Peserta...");
        try {
            tap(BTN_KICK_OUT, "Tap tombol Kick Out");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Kick Out.");
            tapAtScreenRatio(0.925, 0.507);
        }

        // 4. Konfirmasi Modal 'Ya, Lanjutkan'
        System.out.println("   -> Konfirmasi 'Ya, Lanjutkan'...");
        try {
            Thread.sleep(1000); 
            if (isElementPresent(BTN_CONFIRM_LANJUTKAN, 3)) {
                tap(BTN_CONFIRM_LANJUTKAN, "Konfirmasi 'Ya, Lanjutkan'");
            } else {
                throw new Exception("Tombol Lanjutkan tidak terlihat");
            }
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator konfirmasi. Mencari berdasarkan teks...");
            tapButtonByTextOrId("Ya, Lanjutkan", "Ya, Lanjutkan");
        }

        // 5. Validasi Tab Peserta & Capture
        System.out.println("   -> Memvalidasi tampilan akhir Tab Peserta...");
        try { Thread.sleep(2000); } catch (Exception ignored) {} // Tunggu proses loading selesai
        
    }

    public void acceptOneParticipant(String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai alur Accept 1 Peserta...");

        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Pastikan berada di Tab Persetujuan (Berjaga-jaga jika sebelumnya di tab lain)
        System.out.println("   -> Memastikan berada di Tab Persetujuan...");
        try {
            tap(BTN_TO_TAB_PERSETUJUAN, "Switch ke Tab Persetujuan");
        } catch (Exception e) {
            tapAtScreenRatio(0.276, 0.417);
        }
        try { Thread.sleep(1500); } catch (Exception ignored) {}

        // 3. Terima 1 Peserta
        System.out.println("   -> Menerima 1 Peserta...");
        try {
            tap(BTN_TO_ACCEPT_ONE, "Tap tombol Accept (Centang) untuk 1 peserta");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Accept One.");
            tapAtScreenRatio(0.925, 0.569); 
        }

        // 4. Validasi ke Tab Peserta & Capture
        System.out.println("   -> Berpindah ke Tab Peserta untuk validasi...");
        try {
            tap(BTN_TO_TAB_PESERTA, "Switch ke Tab Peserta");
        } catch (Exception e) {
            tapAtScreenRatio(0.723, 0.417);
        }
        
        
    }

    public void acceptAllParticipants(String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai alur Accept SEMUA Peserta...");

        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Pastikan berada di Tab Persetujuan (Sangat penting jika fungsi ini dipanggil setelah acceptOnePeserta)
        System.out.println("   -> Kembali/Memastikan berada di Tab Persetujuan...");
        try {
            tap(BTN_TO_TAB_PERSETUJUAN, "Switch ke Tab Persetujuan");
        } catch (Exception e) {
            tapAtScreenRatio(0.276, 0.417);
        }
        try { Thread.sleep(1500); } catch (Exception ignored) {}

        // 3. Terima SEMUA Peserta
        System.out.println("   -> Menerima SEMUA Peserta...");
        try {
            tap(BTN_TO_ACCEPT_ALL, "Tap tombol Accept Semua");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Accept All.");
            tapAtScreenRatio(0.841, 0.492);
        }

        // 4. Konfirmasi Modal 'Ya, Lanjutkan'
        System.out.println("   -> Konfirmasi 'Ya, Lanjutkan'...");
        try {
            Thread.sleep(1000); 
            if (isElementPresent(BTN_CONFIRM_LANJUTKAN, 3)) {
                tap(BTN_CONFIRM_LANJUTKAN, "Konfirmasi 'Ya, Lanjutkan'");
            } else {
                throw new Exception("Tombol Lanjutkan tidak terlihat");
            }
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator konfirmasi. Mencari berdasarkan teks...");
            tapButtonByTextOrId("Ya, Lanjutkan", "Ya, Lanjutkan");
        }

        // 5. Validasi ke Tab Peserta & Capture
        System.out.println("   -> Berpindah ke Tab Peserta untuk validasi akhir...");
        try {
            Thread.sleep(2000); // Tunggu proses loading massal selesai
            tap(BTN_TO_TAB_PESERTA, "Switch ke Tab Peserta");
        } catch (Exception e) {
            tapAtScreenRatio(0.723, 0.417);
        }
        
        try { Thread.sleep(2000); } catch (Exception ignored) {} 
         
    }

    // ==========================================
    // ACTION: KELOLA PESERTA (REJECT ONE)
    // ==========================================

    public void rejectOneParticipant(String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai alur Reject 1 Peserta...");

        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Pastikan berada di Tab Persetujuan
        System.out.println("   -> Memastikan berada di Tab Persetujuan...");
        try {
            tap(BTN_TO_TAB_PERSETUJUAN, "Switch ke Tab Persetujuan");
        } catch (Exception e) {
            tapAtScreenRatio(0.276, 0.417);
        }
        try { Thread.sleep(1500); } catch (Exception ignored) {}

        // 3. Tolak 1 Peserta
        System.out.println("   -> Menolak 1 Peserta...");
        try {
            tap(BTN_TO_REJECT_ONE, "Tap tombol Reject (Silang) untuk 1 peserta");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Reject One.");
            tapAtScreenRatio(0.818, 0.506); 
        }

        // 4. Validasi Tab Peserta & Capture
        System.out.println("   -> Berpindah ke Tab Peserta untuk validasi...");
        try {
            tap(BTN_TO_TAB_PESERTA, "Switch ke Tab Peserta");
        } catch (Exception e) {
            tapAtScreenRatio(0.723, 0.417);
        }
        
        try { Thread.sleep(2000); } catch (Exception ignored) {} 
        
    }


    // ==========================================
    // ACTION: KELOLA PESERTA (REJECT ALL)
    // ==========================================

    public void rejectAllParticipants(String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai alur Reject SEMUA Peserta...");
        
        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Pastikan berada di Tab Persetujuan
        System.out.println("   -> Kembali/Memastikan berada di Tab Persetujuan...");
        try {
            tap(BTN_TO_TAB_PERSETUJUAN, "Switch ke Tab Persetujuan");
        } catch (Exception e) {
            tapAtScreenRatio(0.276, 0.417);
        }
        try { Thread.sleep(1500); } catch (Exception ignored) {}

        // 3. Tolak SEMUA Peserta
        System.out.println("   -> Menolak SEMUA Peserta...");
        try {
            tap(BTN_TO_REJECT_ALL, "Tap tombol Tolak Semua");
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator. Using corrected Ratio untuk Reject All.");
            tapAtScreenRatio(0.564, 0.429);
        }

        // 4. Konfirmasi Modal 'Ya, Lanjutkan'
        System.out.println("   -> Konfirmasi 'Ya, Lanjutkan'...");
        try {
            Thread.sleep(1000); 
            if (isElementPresent(BTN_CONFIRM_LANJUTKAN, 3)) {
                tap(BTN_CONFIRM_LANJUTKAN, "Konfirmasi 'Ya, Lanjutkan'");
            } else {
                throw new Exception("Tombol Lanjutkan tidak terlihat");
            }
        } catch (Exception e) {
            System.out.println("WARN: Gagal via locator konfirmasi. Mencari berdasarkan teks...");
            tapButtonByTextOrId("Ya, Lanjutkan", "Ya, Lanjutkan");
        }

        // 5. Validasi Tab Peserta & Capture
        System.out.println("   -> Berpindah ke Tab Peserta untuk validasi akhir...");
        try {
            Thread.sleep(2000); // Tunggu proses loading massal selesai
            tap(BTN_TO_TAB_PESERTA, "Switch ke Tab Peserta");
        } catch (Exception e) {
            tapAtScreenRatio(0.723, 0.417);
        }
        
        try { Thread.sleep(2000); } catch (Exception ignored) {} 
    }

    public void shareChallengeToWhatsApp(String contactName, String challengeName, boolean screenshot) {
        System.out.println("   -> [Action] Memulai proses Share Challenge ke WhatsApp...");

        // 1. Pastikan berada di halaman Kelola Challenge
        navigateToDetailChallenge(challengeName, screenshot);
        navigateToKelolaChallenge(challengeName, true);

        // 2. Klik tombol "Bagikan Challenge"
        System.out.println("   -> [Share] Mengklik tombol Bagikan Challenge...");
        try {
            tap(BTN_SHARE_CHALLENGE, "Tap 'Bagikan Challenge'");
        } catch (Exception e) {
            System.out.println("WARN: Gagal mengklik tombol Bagikan Challenge.");
            return; // Berhenti jika tombol awal tidak bisa diklik
        }

        // 3. Tunggu Android Bottom Sheet (Intent Resolver) muncul dan klik WhatsApp
        System.out.println("   -> [Share] Memilih WhatsApp dari System Share Sheet...");
        try {
            // Transisi ke UI Sistem Android kadang memakan waktu
            Thread.sleep(2000); 
            
            if (isElementPresent(BTN_SHARE_WHATSAPP, 5)) {
                tap(BTN_SHARE_WHATSAPP, "Pilih Aplikasi WhatsApp");
            } else {
                throw new Exception("Logo WhatsApp tidak ditemukan di Share Sheet");
            }
        } catch (Exception e) {
            System.out.println("WARN: Gagal menemukan/mengklik WhatsApp di Share Sheet. Mungkin UI Sistem Android berbeda.");
            // Opsi Fallback: Anda bisa menggunakan swipe/scroll jika WhatsApp tersembunyi
            return;
        }

        // 4. Tunggu WhatsApp terbuka dan pilih Kontak "Anda"
        System.out.println("   -> [WhatsApp] Mencari kontak tujuan...");
        try {
            // Transisi membuka aplikasi eksternal (WhatsApp) biasanya memakan waktu lebih lama
            Thread.sleep(3000); 

            scrollToText(contactName);

            tapButtonByTextOrId(contactName, contactName);

        } catch (Exception e) {
            System.out.println("WARN: Gagal menemukan kontak di WhatsApp.");
            return;
        }

        System.out.println("   -> [WhatsApp] Mengirim pesan...");
        try {
            Thread.sleep(1000);
            tap(BTN_WHATSAPP_SEND, "Klik Tombol Kirim WhatsApp");
            
            // Tunggu pesan terkirim
            Thread.sleep(1500); 
            
        } catch (Exception e) {
            System.out.println("WARN: Gagal menekan tombol Send di WhatsApp.");
        }
        
        System.out.println("   -> [SUCCESS] Alur Share ke WhatsApp selesai.");
    }

    public void uploadPoster() {
        uploadPoster(1, false); 
    }

    public void uploadPoster(int photoIndex, boolean screenshot) {
        System.out.println("   -> [Action] Uploading Poster (Choice: " + photoIndex + ")...");

        // --- STEP 1: OPEN GALLERY ---
        try {
            tap(FIELD_POSTER, "Tap Upload Poster Area", screenshot);
        } catch (Exception e) {
            System.out.println("   -> 'Upload Poster' field not found via locator. Using Text Fallback.");
            try {
                tapButtonByTextOrId("Upload Poster", "Upload Poster", screenshot);
            } catch (Exception ex) {
                System.out.println("WARN: Could not open gallery. Skipping upload.");
                return;
            }
        }

        // Wait for Gallery Animation
        try { Thread.sleep(2500); } catch (Exception ignored) {}

        // --- STEP 2: SELECT IMAGE BASED ON CHOICE ---
        try {
            if (photoIndex == 2 ) {
                // Try Photo 2
                if (isElementPresent(PICTURE_TWO, 3)) {
                    tap(PICTURE_TWO, "Select Gallery Image (2)", screenshot);
                } else {
                    tapAtScreenRatio(0.50, 0.477, screenshot); // Fallback: Tap Center of Gallery
                }
            } else if (photoIndex == 1) {
                // Default to Photo 1
                if (isElementPresent(PICTURE_ONE, 3)) {
                    tap(PICTURE_ONE, "Select Gallery Image (1)", screenshot);
                } else {
                    tapAtScreenRatio(0.166, 0.477, screenshot);
                }
            } else if (photoIndex == 3) {
                selectImageFromKoleksi();
            }
        } catch (Exception e) {
            System.out.println("   -> Specific photo locator failed. Using Ratio Fallback.");
            // Fallback: Tap Center Screen (Generic selection)
            tapAtScreenRatio(0.50, 0.63, screenshot);
        }

        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // --- STEP 3: CONFIRM SELECTION ---
        try {
            if (isElementPresent(BTN_CONFIRM_POSTER, 3)) {
                tap(BTN_CONFIRM_POSTER, "Tap 'Done' Button", screenshot);
            } else {
                throw new Exception("Confirm button not found");
            }
        } catch (Exception e) {
            System.out.println("   -> Confirm button locator failed. Using Ratio Fallback.");
            tapAtScreenRatio(0.83, 0.91, screenshot); // Bottom Right
        }
    }

    public void selectImageFromKoleksi() {
        System.out.println("   -> [Action] Memulai alur pemilihan gambar dari UI Koleksi...");

        try {
            // 1. Klik 'Koleksi'
            System.out.println("   -> Tap menu 'Koleksi'...");
            if (isElementPresent(BTN_KOLEKSI, 3)) {
                tap(BTN_KOLEKSI, "Pilih opsi Koleksi");
            } else {
                throw new Exception("Menu Koleksi tidak ditemukan");
            }
            Thread.sleep(1500);

            // 2. Klik 'Dari perangkat ini'
            System.out.println("   -> Tap 'Dari perangkat ini'...");
            if (isElementPresent(BTN_THIS_DEVICE, 3)) {
                tap(BTN_THIS_DEVICE, "Pilih Dari perangkat ini");
            } else {
                throw new Exception("Menu Dari perangkat ini tidak ditemukan");
            }
            Thread.sleep(1500);

            // 3. Klik Folder 'Download'
            System.out.println("   -> Masuk ke folder 'Download'...");
            if (isElementPresent(BTN_DOWNLOAD_FOLDER, 5)) {
                tap(BTN_DOWNLOAD_FOLDER, "Buka folder Download");
            } else {
                // Fallback scroll jika folder tertutup di bawah
                scrollToText("Download");
                tap(BTN_DOWNLOAD_FOLDER, "Buka folder Download");
            }
            Thread.sleep(2000); // Tunggu isi folder me-render

            // 4. Klik Gambar Target
            System.out.println("   -> Memilih gambar target...");
            try {
                tap(PICTURE_DOWNLOAD, "Pilih gambar dari grid");
            } catch (Exception e) {
                System.out.println("WARN: XPath gambar terlalu panjang/gagal ditemukan. Menggunakan Fallback Ratio.");
                // Hitungan dari bounds: [0,963][358,1321]
                tapAtScreenRatio(0.166, 0.504);
            }
            
            System.out.println("   -> [SUCCESS] Gambar berhasil diklik dari UI!");
            Thread.sleep(2000); // Tunggu sampai modal upload tertutup dan gambar termuat di form Buat Challenge

        } catch (Exception e) {
            System.out.println("WARN: Alur UI pemilihan gambar terhenti: " + e.getMessage());
        }
    }

    public void fillCreateChallengeForm(ChallengeData data, boolean screenshot) {
        System.out.println("   -> [Action] Filling Challenge Form...");

        // 1. Upload Poster (using your smart logic)
        uploadPoster(1, screenshot);
        
        scrollToExactText("Nama Challenge");
        try {
            fillInputField(FIELD_NAMA, data.name, screenshot);
        } catch (Exception e) {
            fillInputByLabelOffset("Nama Challenge", data.name, screenshot); // Fallback if locator fails
        }

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        scrollToExactText("Jarak Lari (km)");
        try {
            fillInputField(FIELD_JARAK, data.distance, screenshot);    
        } catch (Exception e) {
            fillInputByLabelOffset("Jarak Lari (km)", data.distance, screenshot); 
        }
        
        try { driver.hideKeyboard(); } catch (Exception ignored) {}
        
        // 3. Date & Time (Complex logic kept inside helper)
        scrollToExactText("Tanggal");
        setDateRange(26, 28, screenshot); 
        scrollToText("Jam (Opsional)");
        setTimeConfiguration(2, 5, screenshot);

        // 4. Description & Terms
        scrollToExactText("Deskripsi");
        // try {
        //     fillInputField(FIELD_DESCRIPTION, data.description, screenshot);
        // } catch (Exception e) {
            fillInputByLabelOffset("Deskripsi", data.description, screenshot);
        // }

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        scrollToExactText("Syarat dan Ketentuan");
        // try {
        //     fillInputField(FIELD_TERMS, data.terms, screenshot);
        // } catch (Exception e) {
            fillInputByLabelOffset("Syarat dan Ketentuan", data.terms, screenshot);
        // }
        
        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 5. Badge & Region
        // We keep these specific calls because they involve complex UI interactions
        if (data.badge != null && !data.badge.isEmpty()){
            configureBadge(data.badge, screenshot);
        }
        else{
            System.out.println("   -> No badge specified in test data. Skipping badge configuration.");
            scrollToText("Badge Pemenang");
            
            try { Thread.sleep(1000); } catch (Exception ignored) {}

            // Highlight area container badge tersebut
            capture.highlightAndCapture(FIELD_BADGE, "Mengecek penggunaan Badge Default");

        }

        scrollToExactText("Visibilitas Challenge");
        // 6. Private Mode
        if (data.isPrivate) {
            setPrivateMode(true, screenshot);
        } else{
            setPrivateMode(false, screenshot);
        }
        
        scrollToExactText("Atur Area Challenge");
        configureRegion(screenshot, "Regional", "Jawa Barat", "Bogor", "Bandung");

        if (!screenshot) {
            scrollToExactText("Buat Challenge Lari!");
            scrollAndCapture(2, 0.946, 0.066, "Final view of filled form before submission");
        }

    }
    
    public void updateChallengeName(String newName) {
        System.out.println("   -> [Action] Updating Name to: " + newName);
        try {
            fillInputField(FIELD_NAMA, newName);
            
            // 5. Hide keyboard
            try { driver.hideKeyboard(); } catch (Exception ignored) {}
            
        } catch (Exception e) {
            System.out.println("WARN: Failed to clear/update name via locator. Trying fallback...");
            // Fallback: Use the Offset method but try to select all + delete manually
            fillInputByLabelOffset("Nama Challenge", newName);
        }
    }
    
    public void setPrivateMode(boolean enable, boolean screenshot) {
        String labelText = "Atur Sebagai Mode Private";
        System.out.println("   -> [Action] Setting Private Mode to: " + enable);

        try {
            scrollToText(labelText);
            Thread.sleep(500); 
            if (enable) {
                tap(TOGGLE_PRIVATE, "Toggle 'Mode Private'", screenshot);
            }
            
            capture.highlightAndCapture(TOGGLE_PRIVATE, "Interacted with Private Mode toggle");
        } catch (Exception e) {
            System.out.println("   -> Specific toggle locator failed. Trying dynamic fallback...");
            
            try {
                By fallbackToggle = AppiumBy.xpath("//*[contains(@text, '" + labelText + "')]/following-sibling::*[1]");
                if (enable) {
                    tap(fallbackToggle, "Toggle 'Mode Private' (Fallback)", screenshot);
                }
                
                capture.highlightAndCapture(fallbackToggle, "Interacted with Private Mode toggle using fallback locator");
            } catch (Exception ex) {
                System.out.println("WARN: Completely failed to interact with Private Mode toggle.");
            }
        }
    }

    /**
     * Handles the "Challenge Berhasil Disimpan" modal after editing.
     * Clicks the Red "Oke" button.
     */
    public boolean confirmEditSaved() {
        System.out.println("   -> [Action] Confirming Edit Success...");
        try {
            // 1. Tunggu teks Berhasil muncul
            // By successMsgLocator = AppiumBy.xpath("//*[contains(@text, 'Challenge Berhasil Disimpan')]");
            // capture.highlightAndCapture(successMsgLocator, "Success Modal Displayed");

            // 2. Klik Oke
            tapButtonByTextOrId("Oke", "Oke");
            
            // 3. Tunggu transisi kembali ke halaman Detail
            Thread.sleep(1500);
            
            return true;

        } catch (Exception e) {
            System.out.println("WARN: Edit Success Modal not found or auto-dismissed.");
            return false; 
        }
    }

    public void setDateRange(int startDay, int endDay, boolean screenshot) {
        System.out.println("   -> [Action] Setting Dates: " + startDay + " - " + endDay);
        clickByLabelOffset("Tanggal", screenshot); // Opens Date Widget
        
        try {
            Thread.sleep(1000);
            // 1. Pick Start Day
            scrollToExactText(String.valueOf(startDay));
            tapButtonByTextOrId(String.valueOf(startDay), String.valueOf(startDay), screenshot);
            
            // 2. Pick End Day (Simplified: Just clicking OK implies single day or auto range)
            // If you need to switch tabs, add tapButtonByTextOrId("Selesai", "Selesai") here.
            scrollToExactText("OK");
            // tapButtonByTextOrId("OK", "OK");
            tap(CONFIRM_DATE_BTN, "Confirm Date Selection", screenshot);
        } catch (Exception e) {
            System.out.println("WARN: Date selection failed. Attempting force close.");
            try { tapButtonByTextOrId("OK", "OK", screenshot); } catch (Exception ex) {}
        }
    }

    public void setTimeConfiguration(int startHourClicks, int endHourClicks, boolean screenshot) {
        System.out.println("   -> [Action] Setting Time...");
        clickByLabelOffset("Jam (Opsional)", screenshot);
        try {
            Thread.sleep(1000);
            // Use accessibility IDs to click Up/Down arrows
            adjustTimeById("hour", startHourClicks, screenshot); // Adjust Start
            
            tapButtonByTextOrId("Jam Selesai", "Jam Selesai", screenshot); // Switch Tab
            adjustTimeById("hour", endHourClicks, screenshot);   // Adjust End
            
            // Confirm
            try { tapByAccessibilityId("Confirm", screenshot); } 
            catch (Exception e) { tapAtScreenRatio(0.858, 0.627, screenshot); } // Ratio Fallback
            
        } catch (Exception e) {
             System.out.println("WARN: Time set failed.");
             tapButtonByTextOrId("Confirm", "Confirm", screenshot);
        }
    }

    public void configureBadge(String badgeId, boolean screenshot) {
        System.out.println("   -> [Action] Configuring Badge & Region...");
        
        // 1. Badge
        scrollToText("Badge Pemenang");
        tapButtonByTextOrId("Badge Pemenang", "Badge Pemenang", screenshot);
        selectBadgeRobust(badgeId, screenshot);
    }

    /**
     * Generic method to configure Region.
     * Logic:
     * 1. If type is "Nasional", it selects it and stops.
     * 2. If type is "Regional", it selects it, then loops through all drillDowns locations.
     * * @param regionType  "Nasional" or "Regional"
     * @param drillDowns  (Optional) Comma-separated list of locations (Province, City, District)
     */
    public void configureRegion(boolean screenshot, String regionType, String... drillDowns) {
        System.out.println("   -> [Action] Configuring Region: " + regionType);

        try {
            // 1. Select the Main Scope (Nasional / Regional)
            // We scroll to it just in case, then tap.
            scrollToExactText("Nasional");
            tapButtonByTextOrId("Nasional", "Nasional", screenshot);
            
            // Wait for UI to react
            Thread.sleep(1000); 

            // 2. Logic Check
            if (regionType.equalsIgnoreCase("Nasional")) {
                System.out.println("      -> Selected Nasional. Stopping drill-down.");
                return; // STOP HERE
            }

            // 3. If Regional, Loop through the drill-downs
            if (drillDowns != null && drillDowns.length > 0) {
                System.out.println("      -> Drilling down " + drillDowns.length + " locations...");
                tapButtonByTextOrId("Regional", "Regional", screenshot); 
                scrollToExactText("Aceh (NAD)");
                tapButtonByTextOrId("Aceh (NAD)", "Aceh (NAD)", screenshot); // Example to open first level
                   
                for (String location : drillDowns) {
                    try {
                        System.out.println("         -> Selecting: " + location);
                        
                        // A. Scroll to find the text (Critical for long lists like Provinces)
                        scrollToExactText(location);
                        
                        // B. Tap the location
                        tapButtonByTextOrId(location, location, screenshot);
                        
                        // C. Wait for the next list to load (API delay)
                        Thread.sleep(1500); 
                        
                    } catch (Exception e) {
                        System.err.println("WARN: Failed to select '" + location + "'. List might not have loaded.");
                        // Optional: Break loop if one fails? 
                        // break; 
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("WARN: Region configuration failed: " + e.getMessage());
        }
    }

    public boolean submitAndConfirm() {
        System.out.println("   -> [Action] Submitting and Confirming (Sequential Flow)...");
        
        // --- STEP 1: CLICK SUBMIT ---
        try { ((AndroidDriver) driver).hideKeyboard(); } catch (Exception e) {}
        scrollToExactText("Buat Challenge");

        try {
            System.out.println("      -> Attempting Submit click...");
            tapButtonByTextOrId("Buat Challenge", "Buat Challenge");
        } catch (Exception e) {
            try {
                tapAtScreenRatio(0.50, 0.90); // Fallback
            } catch (Exception ex) {
                System.err.println("      -> [FATAL] Submit button click failed!");
                return false;
            }
        }

        // --- STEP 2: HANDLE "YA, LANJUTKAN" (The Success Modal) ---
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        try {
            System.out.println("      -> Waiting for 'Ya, Lanjutkan'...");
            
            // Wait for the first popup
            wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, 'Ya, Lanjutkan')]")));

            // Click it
            tapButtonByTextOrId("Ya, Lanjutkan", "Ya, Lanjutkan");
            System.out.println("      -> Clicked 'Ya, Lanjutkan'.");

        } catch (TimeoutException e) {
            System.err.println("      -> [Fail] 'Ya, Lanjutkan' never appeared!");
            return false; // Chain broken
        } catch (Exception e) {
            return false;
        }

        // --- STEP 3: HANDLE "CEK CHALLENGE" (The Navigation Button) ---
        // This appears AFTER clicking 'Ya, Lanjutkan'
        try {
            System.out.println("      -> Waiting for 'Cek Challenge'...");

            // Wait for the second button (Wait up to 10s as this might be a page load)
            WebDriverWait navWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            navWait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, 'Cek Challenge')]")));

            // Click it
            tapButtonByTextOrId("Cek Challenge", "Cek Challenge");
            System.out.println("      -> Clicked 'Cek Challenge'. Flow Complete.");
            
            return true; // SUCCESS: We made it to the end

        } catch (TimeoutException e) {
            System.err.println("      -> [Fail] Clicked 'Ya, Lanjutkan' but 'Cek Challenge' did not appear!");
            return false; // Chain broken
        } catch (Exception e) {
            return false;
        }
    }
    
    private void adjustTimeById(String unit, int clicks, boolean screenshot) {
        if (clicks == 0) return;
        String action = (clicks > 0) ? "Increase" : "Decrease";
        String id = action + " " + unit;
        int count = Math.abs(clicks);

        By buttonLocator = AppiumBy.accessibilityId(id);

        try {
            if (screenshot) {
                capture.highlightAndCapture(buttonLocator, "Tap '" + id + "' " + count + " times");
            }

        for (int i = 0; i < count; i++) {
                try {
                    // We use standard Selenium click here for speed
                    driver.findElement(buttonLocator).click();
                    
                    // Optional: Small sleep if the UI animation is slow
                    // Thread.sleep(100); 
                } catch (Exception e) {
                    System.out.println("   -> Click loop interrupted at index " + i);
                    break; 
                }
            }
        } catch (Exception e) {
            System.out.println("   -> Failed to adjust time for " + id);
        }
    }

    private void selectBadgeRobust(String badgeId, boolean screenshot) {
        try {
            tap(AppiumBy.accessibilityId(badgeId), "Select Badge: " + badgeId, screenshot);
        } catch (Exception e) {
            System.out.println("   -> Badge ID '" + badgeId + "' not found. Using coordinate fallback.");
            
            // Fallback: Click the first badge position
            tapAtScreenRatio(0.15, 0.40, screenshot);
        }
        try { 
            Thread.sleep(500); // Wait for selection animation
            tapByExactText("Pilih", screenshot);
            // tapButtonByTextOrId("Pilih", "Confirm Selection");
            
        } catch (Exception e) {
            System.out.println("   -> 'Pilih' text not found. Using coordinate fallback.");
            
            // Fallback: Click the "Select" button area
            tapAtScreenRatio(0.50, 0.887, screenshot); 
        }
    }

    /**
     * Checks if a text exists on the screen (visible).
     * Used for assertions and verification.
     */
    public boolean isTextVisible(String text) {
        try {
            // Use a short wait so the test doesn't hang if text is missing
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            
            // Search for any element containing the text
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + text + "')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ... inside ChallengeActionHelper ...

    /**
     * Clears an input field located by its label, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
/**
     * Scrolls to the label, clears the input field below it, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
/**
     * Scrolls to the label, clears the input field below it, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
    public void clearAndFillInput(String label, String text) {
        System.out.println("   -> [Action] Clearing & Filling '" + label + "' with: '" + text + "'");
        try {
            // 1. SCROLL TO THE LABEL FIRST (The Fix)
            scrollToText(label);
            
            // 2. Find the label
            WebElement labelEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + label + "')]")
            ));
            
            // 3. Find the input box (Assuming standard vertical layout: Label -> Input)
            // Use pointer offset to tap the center of the input box
            int targetX = labelEl.getLocation().getX() + (labelEl.getSize().getWidth() / 2);
            int targetY = labelEl.getLocation().getY() + labelEl.getSize().getHeight() + 100; // Offset down
            
            tapByCoordinates(targetX, targetY);
            Thread.sleep(500);

            // 4. Clear Text (Select All + Del)
            // This is safer than .clear() for custom UI fields
            org.openqa.selenium.interactions.Actions act = new org.openqa.selenium.interactions.Actions(driver);
            act.keyDown(org.openqa.selenium.Keys.CONTROL).sendKeys("a").keyUp(org.openqa.selenium.Keys.CONTROL).perform();
            act.sendKeys(org.openqa.selenium.Keys.BACK_SPACE).perform();
            
            // 5. Type new text (if not empty)
            if (!text.isEmpty()) {
                act.sendKeys(text).perform();
            }
            
            try { driver.hideKeyboard(); } catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("WARN: Failed to clear/fill " + label);
        }
    }
    /**
     * Checks if the Success Modal ("Ya, Lanjutkan" or "Berhasil") is visible.
     * Used to verify if the button worked or was blocked.
     */
    public boolean isSuccessModalVisible() {
        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return fastWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Ya, Lanjutkan')]")),
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Berhasil')]"))
            ));
        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================
    // ACTION: HIGHLIGHT DYNAMIC BADGE RESULT
    // ==========================================

    /**
     * Memvalidasi dan melakukan highlight pada gambar badge berdasarkan nama challenge.
     * @param challengeName Nama challenge yang sedang dites (misal: "Lari Default 3954")
     */
    public void highlightBadgeResult(String challengeName) {
        System.out.println("   -> [Action] Memvalidasi dan Highlight Badge untuk: " + challengeName);
        
        // 1. Buat XPath dinamis dengan memasukkan nama challenge ke dalamnya
        
        String dynamicXpath = String.format("//android.widget.Image[@content-desc='%s' and @text=\"Finisher_var1_1730275975\"]", challengeName);
        By DYNAMIC_BADGE_LOCATOR = AppiumBy.xpath(dynamicXpath);
        
        try {
            if (isElementPresent(DYNAMIC_BADGE_LOCATOR, 5)) {
                capture.highlightAndCapture(DYNAMIC_BADGE_LOCATOR, "Validasi Badge Target: " + challengeName);
                System.out.println("   -> [SUCCESS] Badge berhasil ditemukan dan di-highlight!");
            } else {
                throw new Exception("Elemen badge tidak ditemukan di layar.");
            }
            
        } catch (Exception e) {
            System.out.println("WARN: Gagal menemukan/highlight badge dengan nama: " + challengeName);
            System.out.println("Detail Error: " + e.getMessage());
            
            // Fallback screenshot jika gagal highlight
            if (TestListener.getTest() != null) {
                try {
                    TestListener.getTest().info("Fallback Capture: Area Badge (Tidak Ditemukan)",
                        com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(capture.getScreenshotBase64()).build());
                } catch (Exception ignored) {}
            }
        }
    }

    public boolean isSubmitButtonEnabled() {
        System.out.println("   -> Mengecek status tombol Submit...");
        try {
            // Perbaikan di baris ini: Gunakan pemanggilan variabel 'driver' langsung
            WebElement submitBtn = driver.findElement(BTN_SUBMIT_CHALLENGE);
            
            boolean isEnabled = submitBtn.isEnabled(); // Membaca atribut 'enabled'
            
            capture.highlightAndCapture(BTN_SUBMIT_CHALLENGE, "Pengecekan Status Tombol (Enabled: " + isEnabled + ")");
            
            return isEnabled;
        } catch (Exception e) {
            System.out.println("WARN: Tombol submit tidak ditemukan di layar.");
            return false;
        }
    }

    public boolean isImageSizeErrorDisplayed() {
        System.out.println("   -> Mengecek kemunculan error ukuran gambar...");
        try {
            // Tunggu maksimal 5 detik karena proses upload/validasi kadang butuh waktu
            boolean isDisplayed = isElementPresent(ERROR_IMAGE_SIZE, 5);
            if (isDisplayed) {
                capture.highlightAndCapture(ERROR_IMAGE_SIZE, "Pesan error ukuran gambar > 5MB muncul");
            }
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }
}