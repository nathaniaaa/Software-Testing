package tests.creation.negative;

import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.SkipException; // Import this for proper skipping
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

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
    
    @Test(priority = 1, description = "Verify pressing Back discards unsaved changes")
    @TestInfo(
        testType = "Negative Cases",
        group = "Profile",
        expected = "Changes should be discarded. Name should revert to original value.",
        note = "Uses re-entry strategy to verify data persistence"
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
        String tempName = "DiscardMe_" + System.currentTimeMillis();
        profilePage.fillInputAndReadBack("Nama", tempName);
        
        logInfo("User Typed: '" + tempName + "' (But will not save)");
        
        // 3. PRESS BACK (Discard)
        TestListener.getTest().info("Action: Pressing Back Button...");
        driver.navigate().back();

        // 4. RE-VERIFY (The Critical Step)
        // We go BACK into edit mode. If the back button worked, the text box
        // should still show 'originalName', NOT 'tempName'.
        TestListener.getTest().info("Action: Re-entering Edit Mode to verify discard...");
        profilePage.enterEditMode();

        String currentName = profilePage.getInputValue("Nama");
        TestListener.getTest().info("Value in DB: '" + currentName + "'");

        // 5. ASSERTION
        Assert.assertEquals(currentName, originalName, 
            "FAILED: The app saved the changes even though we pressed Back!");

        Assert.assertNotEquals(currentName, tempName, 
            "FAILED: The temporary name '" + tempName + "' persisted!");

        logPass("SUCCESS: Changes discarded. Name reverted to '" + originalName + "'.");
    }
    

}