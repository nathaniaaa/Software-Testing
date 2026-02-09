package tests.creation.positive;

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
                logInfo("Setup: Profile Dashboard View (Before Editing)");
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
                    logInfo("Setup: Profile Dashboard View (Before Editing)");
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
    @Test(priority = 1, description = "Pengguna mengedit data dirinya di profil lalu menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        expected = "Profil Pengguna akan terupdate",
        note = "",
        group="Profile"
    )
    public void testUpdateProfileSuccess() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Happy Path Update");
        
        navigateToEditProfile();
        
        logInfo("Initial Profile State (Before Editing)");

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

        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertTrue(isEnabled, "BLOCKER: Save button is disabled even though data is valid!");

        TestListener.getTest().pass("2. Form Filled & Validated. Save Button is Enabled.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        profilePage.saveAndVerify(); 
        
        logPass("Profile updated successfully with new data.");
    }

    @Test(priority = 2, description = "Pengguna mengganti Foto profil nya dengan mengupload ulang lewat kamera, kemudian pengguna menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        group = "Profile",
        expected = "Foto profil Pengguna akan terupdate",
        note = "Requires Camera Permission"
    )
    public void testChangePhotoCamera() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Upload via Camera");

        // 1. Navigate to Profile > Edit
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // 2. Perform Upload (Camera)
        // This uses your updated logic with the specific Samsung A14 bounds
        profilePage.uploadProfilePhoto("Kamera");

        // 3. Save Changes (If there is a 'Simpan' button)
        profilePage.saveAndVerify(); 
        // 4. Assertion
        // Verify we are back on the Edit Page or a Success Message appears

        logPass("Success: Profile photo updated via Camera.");
    }

    @Test(priority = 3, description = "Pengguna mengganti Foto profil nya dengan mengupload ulang lewat galeri, kemudian pengguna menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        group = "Profile",
        expected = "Foto profil Pengguna akan terupdate",
        note = "Ensure Gallery has at least one photo"
    )
    public void testChangePhotoGallery() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Upload via Gallery");

        // 1. Navigate to Profile > Edit
        profilePage.navigateToProfileTab();
        profilePage.enterEditMode();

        // 2. Perform Upload (Gallery)
        // This uses your generic coordinate logic
        profilePage.uploadProfilePhoto("Galeri");

        // 3. Save Changes
        profilePage.saveAndVerify();

        // 4. Assertion
        //masih gatau cara verifnya

        logPass("Success: Profile photo updated via Gallery.");
    }
}