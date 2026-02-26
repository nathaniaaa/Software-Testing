package tests.creation.negative;

import java.time.Year;

import javax.swing.plaf.TreeUI;

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
        profilePage.navigateToEditProfile();
        
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

        boolean isOriginalNamePresent = profilePage.areElementsDisplayed(originalName);

        if (!isOriginalNamePresent) {
            String errorMsg = "FAILED: The original name '" + originalName + "' is NOT visible on the dashboard anymore!";
            logFail(errorMsg);   // 1. Take Screenshot & Log Red
            Assert.fail(errorMsg); // 2. Stop the Test
        }

        // Check 2: Is Temp Name There? (It shouldn't be)
        boolean isTempNamePresent = profilePage.areElementsDisplayed(tempName);
        if (isTempNamePresent) {
            String errorMsg = "FAILED: The temporary name '" + tempName + "' IS visible! The app saved it!";
            logFail(errorMsg);   // 1. Take Screenshot & Log Red
            Assert.fail(errorMsg); // 2. Stop the Test
        }

        // 5. SUCCESS
        TestListener.getTest().log(Status.PASS, "SUCCESS: Original name '" + originalName + "' is still displayed correctly.");
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
        profilePage.navigateToEditProfile();

        // logInfo("Starting Test: Check Save Button State with Empty Fields");

        // 2. CLEAR FIELDS (Trigger Validation)
         TestListener.getTest().log(Status.INFO, "Action: Clearing all mandatory fields...");
        profilePage.fillInputAndReadBack("Nama", "", false);
        profilePage.fillInputAndReadBack("Tinggi Badan", "", false);
        profilePage.fillInputAndReadBack("Berat Badan", "", false);

        // Hide keyboard to ensure we see the button and errors
        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 4. VERIFY ERROR MESSAGES (Dynamic Validation)
        boolean isScreenValid = profilePage.areElementsDisplayed(
            "Nama harus memiliki minimal 3 karakter", 
            "Tinggi badan harus berupa angka",
            "Berat badan harus berupa angka"
        );

        // 3. VERIFY BUTTON STATE (Must be Disabled)
        boolean isEnabled = profilePage.isSaveButtonEnabled(true);

        if (!isEnabled) {
            // SUCCESS: Button is grayed out/disabled
            TestListener.getTest().log(Status.PASS, "SUCCESS: Tombol Simpan DISABLE (Tidak bisa ditekan) sesuai ekspektasi.");
        } else {
            // FAILURE: Button is active
            String msg = "FAILED: Tombol Simpan MASIH AKTIF (Enabled) padahal field kosong!";
            logFail(msg);
            
            // Try to click it anyway to see if it blocks submission
            profilePage.tapButtonByTextOrId("Simpan", "Simpan");
        }


        if (isScreenValid) {
            TestListener.getTest().log(Status.PASS, "SUCCESS: All expected error messages are displayed for empty fields.");
        } else {
            String msg = "FAILED: Expected error messages for empty fields are NOT displayed!";
            TestListener.getTest().log(Status.WARNING, msg);
        }
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
        profilePage.navigateToEditProfile();

        // 3. INPUT NAMA DUPLIKAT
        TestListener.getTest().log(Status.INFO, "Action: Menginput nama yang sudah terdaftar: '" + existingUsername + "'");
        profilePage.fillInputAndReadBack("Nama", existingUsername);

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 5. VERIFIKASI PESAN ERROR
        boolean isErrorMessageDisplayed = profilePage.areElementsDisplayed(expectedErrorText);
        TestListener.getTest().log(Status.INFO, "Is Error Message Displayed: " + isErrorMessageDisplayed);

        // 4. KLIK SIMPAN
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled(true);
        TestListener.getTest().log(Status.INFO, "Is Save Button Enabled: " + isSaveEnabled);
        
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
        profilePage.navigateToEditProfile();

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
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled(true);

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
    @Test(priority = 5, description = "Pengguna memasukkan nilai tinggi badan dan berat badan tidak wajar")
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
        profilePage.navigateToEditProfile();

        // 3. INPUT DATA TIDAK WAJAR
        TestListener.getTest().log(Status.INFO, "Action: Menginput Tinggi '" + unreasonableHeight + "' dan Berat '" + unreasonableWeight + "'");
        
        profilePage.fillInputAndReadBack("Tinggi Badan", unreasonableHeight, false);
        profilePage.fillInputAndReadBack("Berat Badan", unreasonableWeight, false);

        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 4. VERIFIKASI: TOMBOL & ERROR
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled(true);
        boolean isErrorMsgDisplayed = profilePage.areElementsDisplayed("Tinggi badan harus antara 50 cm dan 300 cm", "Berat badan harus antara 20 kg dan 500 kg");
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

// --- TEST: AGE RESTRICTION (7 - 100 Years) ---
    @Test(priority = 6, description = "Pengguna memasukkan tanggal lahir dengan tahun yang tidak valid")
    @TestInfo(
        testType = "Negative Case", 
        group = "Profile", 
        expected = "Pengguna tidak bisa memasukkan tanggal lahir dengan tahun yang tidak valid, karna tahun lahir sudah diset minimal kelahiran 7 sampai 100 tahun kebelakang"

    )
    public void testAgeRestrictionInDatePicker() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Age Restriction (7-80 Years)");

        // 1. KALKULASI TAHUN DINAMIS
        int currentYear = java.time.Year.now().getValue();
        int minAge = 7;
        int maxAge = 100;

        // Hitung tahun yang DILARANG (Invalid)
        String tooYoungYear = String.valueOf(currentYear - (minAge - 1)); // Terlalu muda (misal: 6 tahun)
        String minYear = String.valueOf(currentYear - minAge); // Tepat batas bawah (7 tahun)
        String tooOldYear   = String.valueOf(currentYear - (maxAge + 1)); // Terlalu tua (misal: 81 tahun)
        String maxYear = String.valueOf(currentYear - maxAge); // Tepat batas atas
        
        TestListener.getTest().log(Status.INFO, "Testing Restricted Years: " + tooYoungYear + " (Terlalu Muda) & " + tooOldYear + " (Terlalu Tua)");

        // 2. NAVIGASI
        profilePage.navigateToEditProfile(false);

        // 3. CEK TAHUN TERLALU MUDA (Expected: Ditolak/Return False)
        TestListener.getTest().log(Status.INFO, "Action: Mencoba memilih tahun terlarang: " + tooYoungYear);
        boolean isTooYoungSelected = profilePage.attemptToSelectYearOnly(tooYoungYear, true);

        if (!isTooYoungSelected) {
            TestListener.getTest().log(Status.PASS, "SUCCESS: Sistem menolak/menyembunyikan tahun " + tooYoungYear + " (Validasi Minimal 7 tahun OK).");
            profilePage.navigateToEditProfile(false);
            profilePage.attemptToSelectYearOnly(minYear, false);
        } else {
            String failMsg = "FAILED: Sistem MENGIZINKAN pemilihan tahun " + tooYoungYear + "!";
            TestListener.getTest().log(Status.FAIL, failMsg);
            Assert.fail(failMsg);
        }
        
        profilePage.navigateToEditProfile();

        // 4. CEK TAHUN TERLALU TUA (Expected: Ditolak/Return False)
        TestListener.getTest().log(Status.INFO, "Action: Mencoba memilih tahun terlarang: " + tooOldYear);
        boolean isTooOldSelected = profilePage.attemptToSelectYearOnly(tooOldYear, false);

        if (!isTooOldSelected) {
            TestListener.getTest().log(Status.PASS, "SUCCESS: Sistem menolak/menyembunyikan tahun " + tooOldYear + " (Validasi Maksimal 100 tahun OK).");
            profilePage.navigateToEditProfile(false);
            profilePage.attemptToSelectYearOnly(maxYear, false);
        } else {
            String failMsg = "FAILED: Sistem MENGIZINKAN pemilihan tahun " + tooOldYear + "!";
            TestListener.getTest().log(Status.FAIL, failMsg);
            Assert.fail(failMsg);
        }
    }
}



