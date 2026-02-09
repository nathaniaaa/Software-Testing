package tests.creation.positive;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;
import tests.creation.TargetActionHelper;
import tests.utils.TestInfo;
import tests.utils.TestListener; 

public class TargetTest extends BaseTest {

    private TargetActionHelper targetPage;

    @BeforeClass
    public void setupPage() {
        targetPage = new TargetActionHelper((AndroidDriver) driver);
    }


    // A. FUNCTIONAL (POSITIVE)
    @Test(priority = 1, description = "Pengguna mengatur Target Pribadi")
    @TestInfo(
        testType = "Positive Case",
        expected = "Target jarak lari dapat diatur per hari, minggu, bulan, atau tahun sesuai kebutuhan Pengguna",
        note = "",
        group="Beranda"
    )
    public void testSetValidTarget() {
        // Log to Report instead of Console
        TestListener.getTest().log(Status.INFO, "Starting Test: Set Valid Target (5 km)");

        logInfo("Initial Dashboard State");
        
        // targetPage.cleanUpExistingTarget();
        targetPage.openModal();

        targetPage.fillForm("5");
        TestListener.getTest().info("Form filled with value: 5",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        targetPage.submitForm();
        targetPage.handleSuccessModal(); // Assumes you fixed this method as discussed
        targetPage.waitForLoading(3000); // Wait for dashboard refresh

        // Verification
        boolean isDisplayed = targetPage.isTargetDisplayed("5");
        Assert.assertTrue(isDisplayed, "Target 5 km visual text not found on dashboard.");

        logPass("Target successfully set to 5 km.");
    }

    @Test(priority = 2, description = "Pengguna mereset Target Pribadi")
    @TestInfo(
        testType = "Positive Case",
        expected = "Saat pengguna menekan tombol \n" + //
                        "'Reset Target', target pribadi sebelumnya akan terhapus, dan pengguna dapat membuat target lari baru sesuai kebutuhan",
        note = "",
        group="Beranda"
    )
    public void testResetTarget() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Update Target to 10 km");

        logInfo("Dashboard state before reset target");
        
        targetPage.cleanUpExistingTarget();

        boolean isTargetStillThere = targetPage.isTargetProgressVisible();        

        Assert.assertFalse(isTargetStillThere, "FAILED: Target reset failed. Target text or reset button is still visible.");
        
        // Success Screenshot
        logPass("Target reset successfully to set new target.");
    }
}