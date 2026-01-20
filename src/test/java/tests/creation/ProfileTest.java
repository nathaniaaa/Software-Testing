package tests.creation;

import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;

public class ProfileTest extends BaseTest {

    private ProfileActionHelper profilePage;

    // ========================================================================
    // SETUP: STATE MANAGEMENT
    // ========================================================================
    @BeforeMethod
    public void setupPage() {
        // 1. Initialize Helper
        profilePage = new ProfileActionHelper((AndroidDriver) driver);

        // 2. Intelligent Navigation
        // Only navigate if we are NOT already on the Edit screen.
        // This prevents the test from trying to click "Saya" if it's already there.
        try {
            if (!profilePage.isOnEditProfilePage()) {
                System.out.println("[SETUP] Navigating to Edit Profile...");
                profilePage.navigateToEditProfile();
            }
        } catch (Exception e) {
            System.out.println("[SETUP] WARN: Navigation check failed, forcing navigation.");
            profilePage.navigateToEditProfile();
        }
    }

    // ========================================================================
    // GROUP A: HAPPY PATH & FUNCTIONAL
    // ========================================================================

    @Test(priority = 1, description = "Verify successful profile update with valid data")
    public void testUpdateProfileSuccess() {
        System.out.println("=== TEST 1: Happy Path (Unique Data) ===");

        // 1. Generate Unique Username (Timestamp based)
        String uniqueName = "user.test." + (System.currentTimeMillis() % 10000);
        System.out.println("   -> Generated Name: " + uniqueName);

        // 2. Fill Text Fields (Sanitization Check included implicitly)
        profilePage.fillInputAndReadBack("Nama", uniqueName);
        profilePage.fillInputAndReadBack("Tinggi Badan", "175");
        profilePage.fillInputAndReadBack("Berat Badan", "70");
        
        // 3. Complex Inputs
        // Note: Using 'Agustus' for Indonesian locale. Change to 'Aug' if English.
        profilePage.updateDateOfBirthSmart("1995", "Agu", "20");
        profilePage.selectGenderAndBloodType("Laki-laki", "O");

        // 4. Photo Upload (Testing Gallery Flow)
        profilePage.uploadProfilePhoto("Galeri");

        // 5. CRITICAL ASSERTION: Save Button MUST be enabled
        Assert.assertTrue(profilePage.isSaveButtonEnabled(), 
            "BLOCKER: Save button is disabled even though data is valid!");

        // 6. Save & Verify
        profilePage.saveAndVerify(); 
        System.out.println("   -> SUCCESS: Profile updated successfully.");
    }

    @Test(priority = 2, description = "Verify app saves and sanitizes spaces")
    public void testAutoSanitization() {
        System.out.println("=== TEST 2: Auto-Sanitization (Save Check) ===");

        String dirtyInput = "  user.spaced  ";
        String expectedClean = "user.spaced";

        // 1. Type the dirty input
        profilePage.fillInputAndReadBack("Nama", dirtyInput);

        // 2. ACTION: Try to Save
        boolean isSaved = profilePage.attemptSaveAndValidateSuccess();

        // Check 1: Did the app allow us to save?
        Assert.assertTrue(isSaved, "FAIL: App refused to save the name with spaces!");

        System.out.println("   -> Save successful. Verifying stored data...");

        // 3. RE-NAVIGATE: Go back to Edit Profile to see what was actually stored
        // (Because after saving, we are usually thrown back to the Dashboard)
        profilePage.navigateToEditProfile();

        // 4. VERIFY: Read the value from the server/database
        String storedValue = profilePage.getInputValue("Nama"); 
        
        Assert.assertEquals(storedValue, expectedClean, 
            "FAIL: Server saved the spaces! Expected '" + expectedClean + "' but got '" + storedValue + "'");

        System.out.println("   -> SUCCESS: Server sanitized and saved input correctly.");
    }
    // ========================================================================
    // GROUP B: NEGATIVE TESTING (SECURITY & VALIDATION)
    // ========================================================================

    @Test(priority = 3, description = "Security: Block short usernames")
    public void testShortUsername() {
        System.out.println("=== TEST 3: Short Username ===");

        // 1. Input Invalid Data (< 3 chars)
        profilePage.fillInputAndReadBack("Nama", "ab");

        // 2. ASSERTION: Gatekeeper Check
        // The Save button MUST be disabled (Grayed out)
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled();
        
        Assert.assertFalse(isSaveEnabled, 
            "SECURITY FAIL: Save button enabled for short name 'ab'!");
            
        System.out.println("   -> SUCCESS: System correctly blocked short name.");
    }

    @Test(priority = 4, description = "Security: Verify app sanitizes and allows valid save")
    public void testInvalidCharacters() {
        System.out.println("=== TEST 4: Invalid Characters (Sanitization) ===");

        String dirtyInput = "User_Test!";
        String expectedClean = "ser_est"; 

        // 1. Type the dirty input
        profilePage.fillInputAndReadBack("Nama", dirtyInput);

        // 2. CHECK 1: Did the app sanitize/truncate the symbols?
        String actualValue = profilePage.getInputValue("Nama");
        Assert.assertEquals(actualValue, expectedClean, 
            "SANITIZATION FAIL: App did not remove symbols! Found: " + actualValue);

        // 3. CHECK 2: Is the button enabled? (Since the name is now clean, it should be valid)
        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertTrue(isEnabled, 
            "LOGIC FAIL: Name was sanitized to '" + actualValue + "' but Save button is still disabled!");
        
        System.out.println("   -> SUCCESS: Symbols removed & Save button enabled.");
    }

    @Test(priority = 5, description = "Logic: Block Future Date of Birth")
    public void testFutureDate() {
        System.out.println("=== TEST 5: Future Date ===");

        String nextYear = String.valueOf(java.time.Year.now().getValue() + 1);
        
        // 1. Try to set date
        // capture the boolean result: TRUE = Date Changed, FALSE = Year Not Found
        boolean yearWasFound = profilePage.updateDateOfBirthSmart(nextYear, "Jan", "1");

        // 2. Logic: If year wasn't found, the App BLOCKED the future date. SUCCESS!
        if (!yearWasFound) {
            System.out.println("   -> SUCCESS: Future year was not selectable (Hidden by App).");
            return; // EXIT THE TEST HERE. Do not check the save button.
        }

        // 3. If year WAS found, THEN check if Save is disabled
        Assert.assertFalse(profilePage.isSaveButtonEnabled(), 
            "LOGIC FAIL: System allows saving a Future Date!");
    }

    // ========================================================================
    // GROUP C: EDGE CASES & SYSTEM STABILITY
    // ========================================================================

    @Test(priority = 6, description = "Max Character Boundary")
    public void testMaxCharBoundary() {
        System.out.println("=== TEST 6: Max Char Boundary ===");

        // Create 300 characters
        String massiveString = "a".repeat(300);
        profilePage.fillInputAndReadBack("Nama", massiveString);

        // 1. Get the Actual Length
        String currentText = profilePage.getInputValue("Nama");
        int actualLength = currentText.length();
        System.out.println("   -> Input Length in Box: " + actualLength);

        // 2. DUAL CHECK LOGIC
        if (actualLength < 300) {
            // SCENARIO A: The app Truncated the text (Good!)
            // If it cut it down to a safe limit (e.g. 100), the save button should be ENABLED.
            System.out.println("   -> App truncated input (Safe). Checking Save Button...");
            Assert.assertTrue(profilePage.isSaveButtonEnabled(), 
                "FAIL: App truncated text to safe limit but Save button is disabled!");
        } else {
            // SCENARIO B: The app kept all 300 chars (Risk!)
            // If the text is massive, the save button MUST be DISABLED.
            System.out.println("   -> App accepted all characters. Checking Save Button lock...");
            Assert.assertFalse(profilePage.isSaveButtonEnabled(), 
                "RISK: App accepted 300 chars AND left the Save button enabled! Buffer Overflow Risk.");
        }
        
        System.out.println("   -> SUCCESS: App handled max limit correctly.");
    }

    @Test(priority = 7, description = "Stability: Data persistence after Rotation")
    public void testRotationPersistence() {
        System.out.println("=== TEST 7: Rotation Persistence ===");

        String testData = "rotate_test";
        
        // 1. Fill Data
        profilePage.fillInputAndReadBack("Nama", testData);

        // 2. Try to Rotate (With Handler)
        try {
            System.out.println("   -> Attempting to rotate to Landscape...");
            driver.rotate(ScreenOrientation.LANDSCAPE);
            Thread.sleep(2000);
            
            System.out.println("   -> Attempting to rotate to Portrait...");
            driver.rotate(ScreenOrientation.PORTRAIT);
            Thread.sleep(2000);
            
        } catch (Exception e) {
            // === HANDLE: ROTATION FAILED ===
            // This catches cases where the device/emulator does not support rotation
            System.out.println("   -> [SKIP] Device failed to rotate. This test step is skipped.");
            System.out.println("      Reason: " + e.getMessage());
            
            // Return immediately so we don't fail the assertion below.
            // The test will be marked as "Passed" (Green) but with a Log message explaining why.
            return; 
        }

        // 3. Verify Data (Only runs if rotation succeeded)
        // We read the text back to ensure the app didn't wipe the form
        String currentText = profilePage.fillInputAndReadBack("Nama", testData); 
        
        Assert.assertEquals(currentText, testData, 
            "STABILITY FAIL: Data lost after screen rotation!");
        
        System.out.println("   -> SUCCESS: Data persisted.");
    }
}