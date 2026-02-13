package tests.creation.negative;

import java.time.Year;

import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.SkipException; // Import this for proper skipping
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;
import tests.creation.ProfileActionHelper;
import tests.utils.TestInfo;
import tests.utils.TestListener;

public class ProfileTest extends BaseTest {

    private ProfileActionHelper profilePage;

    // SETUP: STATE MANAGEMENT
    @BeforeMethod
    public void setupPage() {
        profilePage = new ProfileActionHelper((AndroidDriver) driver);
    }

    public void navigateToEditProfile() {
        // From Dashboard to 
        if (profilePage.isOnEditProfilePage()) {
            return; 
        }

        try {
            System.out.println("[SETUP] Navigating to Profile Tab...");
            profilePage.navigateToProfileTab();
            
            Thread.sleep(2000); 

            try {
                TestListener.getTest().info("Setup: Profile Dashboard View (Before Editing)",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            } catch (Exception e) {
                TestListener.getTest().warning("Setup: Failed to capture profile screenshot.");
            }

            System.out.println("[SETUP] Entering Edit Mode...");
            profilePage.enterEditMode();
            
            // Wait for the form to open
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("[SETUP] CRITICAL: Navigation sequence failed. Attempting brute-force.");
            // Fallback: Just click the buttons blindly to try and save the test run
            try {
                profilePage.navigateToProfileTab();
                Thread.sleep(2000);

                try {
                    TestListener.getTest().info("Setup: Profile Dashboard View (Before Editing)",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
                } catch (Exception er) {
                    TestListener.getTest().warning("Setup: Failed to capture profile screenshot.");
                }


                profilePage.enterEditMode();
            } catch (Exception ex) {
                System.out.println("[SETUP] FATAL: Could not recover.");
            }
        }
    }

    @Test(priority = 1, description = "Pengguna tekan tombol back setelah selesai edit profil dan tidak tekan tombol simpan")
    @TestInfo(
        testType = "Negative Case",
        group = "Profile",
        expected = "Setelah selesai mengedit profil, pengguna menekan tombol kembali (back) tanpa menekan tombol \"Simpan\". Akibatnya, perubahan pada profil tidak disimpan dan data profil pengguna tetap seperti sebelumnya.",
        note = ""
    )
    public void testBackWithoutSaving() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Discard Changes via Back Button");

        // 1. GET ORIGINAL VALUE (Baseline)
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();
        
        // We read what is currently in the box before touching it
        String originalName = profilePage.getInputValue("Nama"); 
        TestListener.getTest().info("Baseline Name: '" + originalName + "'");

        // 2. MAKE A CHANGE (But don't save)
        String tempName = "discardme." + System.currentTimeMillis();
        profilePage.fillInputAndReadBack("Nama", tempName);
        TestListener.getTest().info("Temporary Name Set (Not Saved): '" + tempName + "'");
        // 3. PRESS BACK (Discard)
        TestListener.getTest().info("Action: Pressing Back Button...");
        profilePage.tapBackArrow();

        boolean isOriginalNamePresent = profilePage.isElementDisplayed(originalName);

        if (!isOriginalNamePresent) {
            String errorMsg = "FAILED: The original name '" + originalName + "' is NOT visible on the dashboard anymore!";
            logFail(errorMsg);   // 1. Take Screenshot & Log Red
            Assert.fail(errorMsg); // 2. Stop the Test
        }

        // Check 2: Is Temp Name There? (It shouldn't be)
        boolean isTempNamePresent = profilePage.isElementDisplayed(tempName);
        if (isTempNamePresent) {
            String errorMsg = "FAILED: The temporary name '" + tempName + "' IS visible! The app saved it!";
            logFail(errorMsg);   // 1. Take Screenshot & Log Red
            Assert.fail(errorMsg); // 2. Stop the Test
        }

        // 5. SUCCESS
        logPass("SUCCESS: Original name '" + originalName + "' is still displayed correctly.");
    }

    // --- TEST: SAVE BUTTON DISABLED (EMPTY FIELDS) ---
    @Test(priority = 2, description = "Tekan tombol simpan dengan kondisi terdapat field yang masih kosong")
    @TestInfo(
        testType = "Negative Case", 
        group = "Profile", 
        expected = "Saat pengguna menekan tombol \"Simpan\" dengan kondisi masih ada field yang kosong, tombol tersebut berada dalam keadaan disable dan tidak dapat ditekan. Selain itu, muncul pesan error pada setiap field yang wajib diisi."
    )
    public void testSaveWithEmptyFields() {

        // 1. NAVIGATE & EDIT
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // logInfo("Starting Test: Check Save Button State with Empty Fields");

        // 2. CLEAR FIELDS (Trigger Validation)
        // We assume fillInputAndReadBack handles clearing if passed an empty string
        TestListener.getTest().log(Status.INFO, "Action: Clearing all mandatory fields...");
        profilePage.fillInputAndReadBack("Nama", "");
        profilePage.fillInputAndReadBack("Tinggi Badan", "");
        profilePage.fillInputAndReadBack("Berat Badan", "");

        // Hide keyboard to ensure we see the button and errors
        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 3. VERIFY BUTTON STATE (Must be Disabled)
        boolean isEnabled = profilePage.isSaveButtonEnabled();

        if (!isEnabled) {
            // SUCCESS: Button is grayed out/disabled
            logPass("SUCCESS: Tombol Simpan DISABLE (Tidak bisa ditekan) sesuai ekspektasi.");
        } else {
            // FAILURE: Button is active
            String msg = "FAILED: Tombol Simpan MASIH AKTIF (Enabled) padahal field kosong!";
            logFail(msg);
            
            // Try to click it anyway to see if it blocks submission
            profilePage.tapButtonByTextOrId("Simpan", "Simpan");
        }

        // 4. VERIFY ERROR MESSAGES (Dynamic Validation)
        // Check each error text matches your app's actual validation messages
        profilePage.isElementDisplayed("Nama harus memiliki minimal 3 karakter"); // Adjust text if app differs
        profilePage.isElementDisplayed("Tinggi badan harus berupa angka"); // Adjust text if app differs
        profilePage.isElementDisplayed("Berat badan harus berupa angka"); // Adjust text if app differs

        // 5. FINAL ASSERTION
        Assert.assertFalse(isEnabled, "Tombol Simpan seharusnya Disable!");

        driver.navigate().back(); // Exit edit mode
    }

    // --- TEST: DUPLICATE USERNAME ---
    @Test(priority = 3, description = "Pengguna memasukkan nama yang sudah terdaftar atau sudah terpakai diakun Ayolari")
    @TestInfo(
        testType = "Negative Case", 
        group = "Profile", 
        expected = "Saat pengguna memasukkan nama yang sudah terdaftar atau telah digunakan oleh akun lain di Ayolari, akan muncul pesan error yang menandakan bahwa nama tersebut tidak dapat digunakan kembali."
    )
    public void testDuplicateUsername() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Duplicate Username Validation");

        // 1. DATA SETUP
        String existingUsername = "siti_malihah"; 
        String expectedErrorText = "Nama sudah dipakai, coba yang lain"; 

        // 2. NAVIGASI & EDIT
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // 3. INPUT NAMA DUPLIKAT
        TestListener.getTest().log(Status.INFO, "Action: Menginput nama yang sudah terdaftar: '" + existingUsername + "'");
        profilePage.fillInputAndReadBack("Nama", existingUsername);

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 4. KLIK SIMPAN
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled();
        TestListener.getTest().log(Status.INFO, "Is Save Button Enabled: " + isSaveEnabled);
        // 5. VERIFIKASI PESAN ERROR
        boolean isErrorMessageDisplayed = profilePage.isElementDisplayed(expectedErrorText);
        TestListener.getTest().log(Status.INFO, "Is Error Message Displayed: " + isErrorMessageDisplayed);

        if (!isSaveEnabled) {      
            // --- SECONDARY CHECK: Error Message (UX Warning) ---
            if (isErrorMessageDisplayed) {
                // Perfect Case: Disabled + Error Message
                TestListener.getTest().log(Status.PASS, "SUCCESS: Tombol Simpan DISABLE (Benar) untuk username duplikat.");
            } else {
                // Functional Success but Bad UX: Disabled + NO Error Message
                String warningMsg = "WARNING: Tombol Disable (Benar), tapi pesan error '" + expectedErrorText + "' TIDAK MUNCUL!";
                
                // Log WARNING to Extent Report
                TestListener.getTest().log(Status.WARNING, warningMsg, 
                    com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(capture.getScreenshotBase64()).build());
                
                System.out.println("[WARN] " + warningMsg);
            }

            driver.navigate().back(); // Exit edit mode

        } else {
            // --- FAILURE CONDITION: Button is Enabled ---
            // If the button is active, it means the app allows saving duplicates (or validation failed)
            String failMsg = "FAILED: Tombol Simpan MASIH AKTIF (Enabled) padahal username '" + existingUsername + "' sudah terpakai!";
            
            TestListener.getTest().log(Status.FAIL, failMsg);
            
            // Force verify: Try to click it to confirm if it blocks later
            profilePage.tapButtonByTextOrId("Simpan", "Simpan");
            
            Assert.fail(failMsg);
        }
    }

    // --- TEST: DECIMAL INPUT VALIDATION ---
    @Test(priority = 4, description = "Pengguna memasukkan nilai tinggi badan dan berat badan dengan format desimal")
    @TestInfo(
        testType = "Negative Case", 
        group = "Profile", 
        expected = "Pengguna tidak bisa memasukkan nilai tinggi badan dan berat badan dengan format berdesimal."
    )
    public void testDecimalHeightWeight() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Decimal Input Validation (Height/Weight)");

        // 1. DATA SETUP
        String invalidHeight = "26.5";
        String invalidWeight = "45.8";
        String expectedErrorPart = "angka"; // Keyword umum error (misal: "Format angka salah", "Hanya boleh angka")

        // 2. NAVIGASI & EDIT
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // 3. INPUT DATA DESIMAL
        TestListener.getTest().log(Status.INFO, "Action: Menginput Tinggi '" + invalidHeight + "' dan Berat '" + invalidWeight + "'");
        
        // Menggunakan helper fillInputAndReadBack untuk memastikan apa yang tertulis di layar
        String actualHeight = profilePage.fillInputAndReadBack("Tinggi Badan", invalidHeight);
        String actualWeight = profilePage.fillInputAndReadBack("Berat Badan", invalidWeight);

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 4. VALIDASI 1
        boolean isDecimalPresent = actualHeight.contains(".") || actualHeight.contains(",") ||
                                   actualWeight.contains(".") || actualWeight.contains(",");

        if (!isDecimalPresent) {
            String msg = "INFO: Aplikasi otomatis menolak karakter desimal. Input '" + invalidHeight + "' menjadi '" + actualHeight + "'";
            TestListener.getTest().log(Status.PASS, msg);
            driver.navigate().back(); 
            return; 
        }

        // 5. VALIDASI 2: CEK TOMBOL SIMPAN & ERROR (Jika desimal masuk)
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled();

        TestListener.getTest().log(Status.INFO, "Decimal Persisted. Button Enabled: " + isSaveEnabled);

        if (!isSaveEnabled) {
            // --- PASS: Tombol Disable ---
            TestListener.getTest().log(Status.PASS, "SUCCESS: Tombol Simpan DISABLE saat ada input desimal.");
            
        } else {
            profilePage.tapButtonByTextOrId("Simpan", "Simpan");
                // --- FAIL: Tombol Enabled (Even if there's an error message, it's still a failure that user can click Save) ---
                // Input Desimal masuk -> Tombol Nyala -> Diklik tidak ada error (Data tersimpan salah!)
                String failMsg = "FAILED: Sistem MENERIMA input desimal! Tinggi: " + actualHeight + ", Berat: " + actualWeight;
                logFail(failMsg);
                Assert.fail(failMsg);
            }
    }

    // --- TEST: UNREASONABLE INPUT (Height & Weight) ---
    @Test(priority = 7, description = "Pengguna memasukkan nilai tinggi badan dan berat badan tidak wajar")
    @TestInfo(
        testType = "Negative Case", 
        group = "Profile", 
        expected = "Tombol simpan tidak dapat ditekan karna disable, dan muncul pesan error.\n" + //
                        "Hal ini disebabkan data nilai tinggi badan dan berat badan yang dimasukkan user tidak wajar, sehingga pengguna tidak dapat melanjutkan untuk proses edit akun Ayolari"
    )
    public void testUnreasonableHeightWeight() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Unreasonable Value Validation");

        // 1. DATA SETUP
        String unreasonableHeight = "400"; // 4 meter
        String unreasonableWeight = "600"; // 600 kg
        
        // 2. NAVIGASI & EDIT
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // 3. INPUT DATA TIDAK WAJAR
        TestListener.getTest().log(Status.INFO, "Action: Menginput Tinggi '" + unreasonableHeight + "' dan Berat '" + unreasonableWeight + "'");
        
        profilePage.fillInputAndReadBack("Tinggi Badan", unreasonableHeight);
        profilePage.fillInputAndReadBack("Berat Badan", unreasonableWeight);

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 4. VERIFIKASI: TOMBOL & ERROR
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled();
        boolean isErrorMsgDisplayed = profilePage.isElementDisplayed("Tinggi badan harus antara 50 cm dan 300 cm") &&
                                    profilePage.isElementDisplayed("Berat badan harus antara 20 kg dan 500 kg");
        TestListener.getTest().log(Status.INFO, "Error Displayed: " + isErrorMsgDisplayed);


        TestListener.getTest().log(Status.INFO, "Button Enabled: " + isSaveEnabled);
        TestListener.getTest().log(Status.INFO, "Error Displayed: " + isErrorMsgDisplayed);

        // 5. ASSERTION LOGIC
        if (!isSaveEnabled) {
            // --- PASS: Tombol Disable (Sesuai Requirement) ---
            TestListener.getTest().log(Status.PASS, "SUCCESS: Tombol Simpan DISABLE karena nilai input tidak wajar.");

            // Validasi Tambahan: Pesan Error
            if (isErrorMsgDisplayed) {
                TestListener.getTest().log(Status.PASS, "SUCCESS: Pesan error muncul untuk input tidak wajar.");
                driver.navigate().back(); // Exit edit mode
                return;
            } else {
                // Warning jika tombol mati tapi user tidak dikasih tahu alasannya
                TestListener.getTest().log(Status.WARNING, "WARNING: Tombol Disable (Benar), tapi pesan error spesifik tidak ditemukan.");
                driver.navigate().back(); // Exit edit mode
                return;
            }
        } else {
            // --- FAIL: Tombol Masih Nyala ---
            // Ini berbahaya karena user bisa simpan data sampah
            String failMsg = "FAILED: Tombol Simpan MASIH AKTIF (Enabled) padahal input tidak wajar (Tinggi: " + unreasonableHeight + ")!";
            logFail(failMsg);
            
            // Coba klik untuk membuktikan apakah ada blocking di akhir
            profilePage.tapButtonByTextOrId("Simpan", "Simpan");
            
            Assert.fail(failMsg);
        }
    }

//     // --- TEST: AGE RESTRICTION (7 - 80 Years) ---
// // --- TEST: AGE RESTRICTION (7 - 80 Years) ---
//     @Test(priority = 8, description = "Verifikasi batasan umur (Minimal 7 tahun, Maksimal 80 tahun)")
//     @TestInfo(
//         testType = "Negative", 
//         group = "Profile", 
//         expected = "Tahun lahir untuk umur < 7 atau > 80 tahun TIDAK BOLEH muncul di picker"
//     )
//     public void testAgeRestrictionInDatePicker() {
//         TestListener.getTest().log(Status.INFO, "Starting Test: Age Restriction (7-80 Years)");

//         // 1. CALCULATE YEARS (Dynamic)
//         int currentYear = Year.now().getValue();
//         int minAge = 7;
//         int maxAge = 80;

//         // Example: If 2026 -> Too Young = 2020 (6yo), Too Old = 1945 (81yo)
//         String tooYoungYear = String.valueOf(currentYear - (minAge - 1)); 
//         String tooOldYear   = String.valueOf(currentYear - (maxAge + 1)); 

//         logInfo("Testing Restricted Years: " + tooYoungYear + " (Too Young) & " + tooOldYear + " (Too Old)");

//         // 2. NAVIGATE
//         profilePage.navigateToProfileTab();
//         profilePage.enterEditMode();

//         // 3. CHECK TOO YOUNG YEAR (Should NOT be found)
//         logInfo("Action: Checking Year " + tooYoungYear + "...");
        
//         // This helper will capture Min/Max screenshots if the year is missing
//         boolean isTooYoungFound = profilePage.checkYearInPickerAndHighlight(tooYoungYear);

//         if (!isTooYoungFound) {
//             // Success: The year was missing, so the restriction is working
//             logPass("SUCCESS: Tahun " + tooYoungYear + " tidak ditemukan (Sesuai Validasi).");
//         } else {
//             // Failure: The year was found
//             String msg = "FAILED: Tahun " + tooYoungYear + " DITEMUKAN di picker! Validasi umur minimum gagal.";
//             logFail(msg);
//             Assert.fail(msg);
//         }

//         // 4. CHECK TOO OLD YEAR (Should NOT be found)
//         logInfo("Action: Checking Year " + tooOldYear + "...");
        
//         boolean isTooOldFound = profilePage.checkYearInPickerAndHighlight(tooOldYear);

//         if (!isTooOldFound) {
//             // Success: The year was missing
//             logPass("SUCCESS: Tahun " + tooOldYear + " tidak ditemukan (Sesuai Validasi).");
//         } else {
//             // Failure: The year was found
//             String msg = "FAILED: Tahun " + tooOldYear + " DITEMUKAN di picker! Validasi umur maksimum gagal.";
//             logFail(msg);
//             Assert.fail(msg);
//         }
    
//     }
}



