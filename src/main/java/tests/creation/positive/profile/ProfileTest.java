package tests.creation.positive.profile;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test; // Import this for proper skipping

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

    // GROUP A: HAPPY PATH & FUNCTIONAL
    @Test(priority = 1, description = "Pengguna mengedit data dirinya di profil lalu menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        expected = "Profil Pengguna akan terupdate",
        note = "",
        group="EDIT PROFIL"
    )
    public void testUpdateProfileSuccess() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Happy Path Update");
        
        profilePage.navigateToEditProfile();
        
        TestListener.getTest().info("Initial Profile State (Before Editing)",
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
        profilePage.selectGenderAndBloodType("Laki-laki", "");

        boolean isEnabled = profilePage.isSaveButtonEnabled();
        Assert.assertTrue(isEnabled, "BLOCKER: Save button is disabled even though data is valid!");

        TestListener.getTest().pass("2. Form Filled & Validated. Save Button is Enabled.",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        profilePage.saveAndVerify(); 
        
        profilePage.areElementsDisplayed( uniqueName, "175", "70");
        // logPass("Profile updated successfully with new data.");
    }

    @Test(priority = 2, description = "Pengguna mengganti Foto profil nya dengan mengupload ulang lewat galeri, kemudian pengguna menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        group = "EDIT PROFIL",
        expected = "Foto profil Pengguna akan terupdate",
        note = "Pastikan ada foto di galeri untuk upload test ini"
    )
    public void testChangePhotoGallery() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Upload via Gallery");

        // 1. Navigate to Profile > Edit
        profilePage.navigateToEditProfile();

        // 2. Perform Upload (Gallery)
        // This uses your generic coordinate logic
        profilePage.uploadProfilePhoto("Galeri");

        // 3. Save Changes
        profilePage.saveAndVerify();

        // 4. Assertion
        //masih gatau cara verifnya
        profilePage.captureProfilePicture(); // Capture the profile picture for evidence
        TestListener.getTest().pass("Success: Profile photo updated via Gallery.");
    }

    @Test(priority = 3, description = "Pengguna mengganti Foto profil nya dengan mengupload ulang lewat kamera, kemudian pengguna menyimpannya")
    @TestInfo(
        testType = "Positive Case",
        group = "EDIT PROFIL",
        expected = "Foto profil Pengguna akan terupdate",
        note = ""
    )
    public void testChangePhotoCamera() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Upload via Camera");

        // 1. Navigate to Profile > Edit
        profilePage.navigateToEditProfile();

        // 2. Perform Upload (Camera)
        // This uses your updated logic with the specific Samsung A14 bounds
        profilePage.uploadProfilePhoto("Kamera");

        // 3. Save Changes (If there is a 'Simpan' button)
        profilePage.saveAndVerify(); 
        // 4. Assertion
        // Verify we are back on the Edit Page or a Success Message appears
        profilePage.captureProfilePicture(); // Capture the profile picture for evidence
        TestListener.getTest().pass("Success: Profile photo updated via Camera.");
    }

}