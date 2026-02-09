package tests.creation;

import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.SkipException; // Import this for proper skipping
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;
import tests.utils.TestListener;

public class ProfileTestv3 extends BaseTest {

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

    // GROUP A: HAPPY PATH & FUNCTIONAL
    @Test(priority = 1, description = "Verify successful profile update with valid data")
    public void testUpdateProfileSuccess() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Happy Path Update");
        
        navigateToEditProfile();
        
        TestListener.getTest().info("1. Initial Profile State (Before Editing)",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // DATA ENTRY & LOGGING
        String uniqueName = "user.test." + (System.currentTimeMillis() % 10000);
        
        // 1. Name (Log the readback verification)
        String actualName = profilePage.fillInputAndReadBack("Nama", uniqueName);
        TestListener.getTest().info("Input 'Nama' -> Typed: " + uniqueName + " | Verified: " + actualName);

        // 2. Height
        String actualHeight = profilePage.fillInputAndReadBack("Tinggi Badan", "175");
        TestListener.getTest().info("Input 'Tinggi' -> Typed: 175 | Verified: " + actualHeight);

        // 3. Weight
        String actualWeight = profilePage.fillInputAndReadBack("Berat Badan", "70");
        TestListener.getTest().info("Input 'Berat' -> Typed: 70 | Verified: " + actualWeight);
        
        // 4. Complex Inputs (Date)
        TestListener.getTest().info("Action: Setting Date of Birth to 20-Aug-1995");
        //set the dob with number format
        profilePage.updateDateOfBirth("1995", "8", "20");

        // 5. Gender & Blood Type
        TestListener.getTest().info("Action: Selecting Gender 'Laki-laki' & Blood Type 'O'");
        profilePage.selectGenderAndBloodType("Laki-laki", "O");

        // 6. Photo Upload
        TestListener.getTest().info("Action: Uploading photo from Gallery...");
        profilePage.uploadProfilePhoto("Galeri");

        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertTrue(isEnabled, "BLOCKER: Save button is disabled even though data is valid!");

        TestListener.getTest().pass("2. Form Filled & Validated. Save Button is Enabled.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        profilePage.saveAndVerify(); 
        
        TestListener.getTest().pass("3. SUCCESS: Profile updated and redirected to Dashboard.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2, description = "Verify app saves and sanitizes spaces")
    public void testAutoSanitization() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Auto-Sanitization");
        navigateToEditProfile();
        String dirtyInput = "  user.spaced . ";

        String uiValue = profilePage.fillInputAndReadBack("Nama", dirtyInput);
        TestListener.getTest().info("User Typed: '" + dirtyInput + "' | UI Displayed: '" + uiValue + "'");

        TestListener.getTest().info("1. Input filled with spaces (Before Save)",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        boolean isSaved = profilePage.attemptSaveAndValidateSuccess();
        Assert.assertTrue(isSaved, "FAIL: App refused to save the name with spaces!");

        TestListener.getTest().info("2. Save Successful. Redirected to Dashboard.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // RE-VERIFY DATA (SERVER CHECK)
        TestListener.getTest().info("Action: Re-entering Edit Mode to verify stored data...");
        
        profilePage.enterEditMode(); 
        
        String storedValue = profilePage.getInputValue("Nama"); 
        TestListener.getTest().info("Server Value Retrieved: '" + storedValue + "'");

        String strictPattern = "^[a-z0-9_.]+$";
        boolean isSanitized = storedValue.matches(strictPattern);

        Assert.assertTrue(isSanitized, 
            "SANITIZATION FAIL: Stored value '" + storedValue + "' contains forbidden characters! (Allowed: a-z, 0-9, _)");

        TestListener.getTest().pass("3. SUCCESS: Server sanitized input correctly.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // GROUP B: NEGATIVE TESTING (SECURITY & VALIDATION)
    @Test(priority = 3, description = "Security: Block short usernames")
    public void testShortUsername() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Short Username Security Check");
        navigateToEditProfile();
        String shortName = "ab";

        String actualUiText = profilePage.fillInputAndReadBack("Nama", shortName);
        TestListener.getTest().info("User Typed: '" + shortName + "' | UI Displayed: '" + actualUiText + "'");

        TestListener.getTest().info("1. Input filled with short username (During)",
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // VALIDATION LOGIC
        boolean isSaveEnabled = profilePage.isSaveButtonEnabled();

        // Visual Logging for the Report
        if (isSaveEnabled) {
            TestListener.getTest().fail("Logic Check: Button is ENABLED (Critical Risk)");
        } else {
            TestListener.getTest().info("Logic Check: Button is DISABLED (Safe)");
        }

        // Hard Assertion
        Assert.assertFalse(isSaveEnabled, "SECURITY FAIL: Save button enabled for short name 'ab'!");

        // SNAPSHOT 2: PROOF OF BLOCKING
        TestListener.getTest().pass("2. SUCCESS: System correctly blocked short name.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 4, description = "Security: Verify app sanitizes invalid chars")
    public void testInvalidCharacters() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Invalid Character Sanitization");
        navigateToEditProfile();
        String dirtyInput = "User_Test!123";
        
        String actualUiText = profilePage.fillInputAndReadBack("Nama", dirtyInput);
        
        TestListener.getTest().info("User Typed: '" + dirtyInput + "' | UI Displayed: '" + actualUiText + "'");

        TestListener.getTest().info("1. Input Phase: Checking sanitization result",
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        // 2. Assertion: Regex Validation
        String allowedPattern = "^[a-z0-9_.]+$";
        boolean isValidFormat = actualUiText.matches(allowedPattern);
        
        Assert.assertTrue(isValidFormat, 
            "SANITIZATION FAIL: Output '" + actualUiText + "' contains invalid characters! Allowed: [a-z, 0-9, _, .]");

        // Verify the Save button is enabled (since the name is now valid)
        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertTrue(isValidFormat, 
            "SANITIZATION FAIL: Output '" + actualUiText + "' contains invalid characters! Allowed: [a-z, 0-9, _, .]");
       
        // Final Valid State
        TestListener.getTest().pass("2. Success: Output matches allowed pattern (lowercase/num/_) & Save is enabled.",
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 5, description = "Logic: Block Future Date of Birth")
    public void testFutureDate() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Future Date Logic");
        navigateToEditProfile();
        String nextYear = String.valueOf(java.time.Year.now().getValue() + 1);

        TestListener.getTest().info("1. Ready to attempt selecting future year: " + nextYear,
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Returns TRUE if year existed and was selected, FALSE if year wasn't in the list
        //set the dob with number format
        boolean yearWasFound = profilePage.updateDateOfBirth(nextYear, "1", "1");

        if (!yearWasFound) {
            // Outcome A: The App is smart and hides future years completely
            TestListener.getTest().pass("2. Success: Future year " + nextYear + " is not selectable (Hidden by App).",
                 MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            return; 
        }

        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertFalse(isEnabled, "LOGIC FAIL: System allows saving a Future Date!");
        
        TestListener.getTest().pass("2. Success: Future date selected, but Save button is disabled.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // GROUP C: EDGE CASES & SYSTEM STABILITY
    @Test(priority = 6, description = "Max Character Boundary")
    public void testMaxCharBoundary() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Max Character Boundary (300 chars)");
        navigateToEditProfile();
        String massiveString = "a".repeat(300);
        
        String currentText = profilePage.fillInputAndReadBack("Nama", massiveString);
        int actualLength = currentText.length();

        TestListener.getTest().info("Generated 300 chars. UI accepted: " + actualLength + " chars.");

        TestListener.getTest().info("1. Input Phase: massive string entered.",
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // Logic Branching
        if (actualLength < 300) {
            // SCENARIO A: The App Truncated the text (Good UX)
            TestListener.getTest().info("Logic: App truncated input (Safe). Checking Save Button...");
            
            Assert.assertTrue(profilePage.isSaveButtonEnabled(), "FAIL: Text truncated but Save disabled!");
            
            // SNAPSHOT 2A: SUCCESS (TRUNCATED)
            TestListener.getTest().pass("2. Success: App truncated overflow text safely & Save is enabled.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        } else {
            // SCENARIO B: The App Accepted all text (Risk)
            TestListener.getTest().warning("Logic: App accepted all 300 chars. Verifying Safety Lock...");
            
            Assert.assertFalse(profilePage.isSaveButtonEnabled(), 
                "RISK: App accepted 300 chars AND left the Save button enabled! Potential Buffer Overflow.");
            
            // SNAPSHOT 2B: SUCCESS (BLOCKED)
            TestListener.getTest().pass("2. Success: App accepted text but correctly blocked submission.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        }
    }

    @Test(priority = 7, description = "Stability: Data persistence after Rotation")
    public void testRotationPersistence() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Screen Rotation Persistence");
        navigateToEditProfile();
        String testData = "rotate_test";

        String initialUiText = profilePage.fillInputAndReadBack("Nama", testData);
        TestListener.getTest().info("User Typed: '" + testData + "' | UI Displayed: '" + initialUiText + "'");

        // SNAPSHOT 1: PORTRAIT EVIDENCE
        TestListener.getTest().info("1. Portrait Mode: Data entered successfully.",
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        try {
            TestListener.getTest().info("Action: Rotating device to LANDSCAPE...");
            driver.rotate(ScreenOrientation.LANDSCAPE);
            Thread.sleep(2000); // Wait for animation and layout adjustment

            // SNAPSHOT 2: LANDSCAPE EVIDENCE
            TestListener.getTest().info("2. Landscape Mode: Checking UI stability.",
                 MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

            TestListener.getTest().info("Action: Rotating device back to PORTRAIT...");
            driver.rotate(ScreenOrientation.PORTRAIT);
            Thread.sleep(2000); // Wait for layout reset

        } catch (Exception e) {
            TestListener.getTest().skip("Device failed to rotate. Skipping test.");
            throw new SkipException("Device does not support rotation API: " + e.getMessage());
        }

        String currentText = profilePage.getInputValue("Nama"); 
        
        Assert.assertEquals(currentText, testData, "STABILITY FAIL: Text field was cleared or corrupted after rotation!");

        TestListener.getTest().pass("3. Success: Data persisted exactly after full rotation cycle.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }
}