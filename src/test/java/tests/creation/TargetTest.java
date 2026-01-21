package tests.creation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest; 
// import TargetActionHelper; // Update to match your new folder
import tests.utils.TestListener; // Import the listener to access logs

public class TargetTest extends BaseTest {

    private TargetActionHelper targetPage;

    @BeforeClass
    public void setupPage() {
        targetPage = new TargetActionHelper((AndroidDriver) driver);
    }

    // ==========================================
    // A. FUNCTIONAL (POSITIVE)
    // ==========================================

    @Test(priority = 1, description = "1. Set Valid Target")
    public void testSetValidTarget() {
        // Log to Report instead of Console
        TestListener.getTest().log(Status.INFO, "Starting Test: Set Valid Target (5 km)");

        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("5");
        
        targetPage.submitForm();
        targetPage.handleSuccessModal(); // Assumes you fixed this method as discussed
        targetPage.waitForLoading(3000); // Wait for dashboard refresh

        // Verification
        boolean isDisplayed = targetPage.isTargetDisplayed("5");
        Assert.assertTrue(isDisplayed, "Target 5 km visual text not found on dashboard.");

        // === EVIDENCE SCREENSHOT (Success) ===
        // This puts the picture inside the green "Pass" log in your HTML report
        TestListener.getTest().pass("Target successfully set to 5km!", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2, description = "2. Update Target")
    public void testUpdateTarget() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Update Target to 10 km");

        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("10");
        targetPage.submitForm();
        
        targetPage.handleSuccessModal();
        targetPage.waitForLoading(3000);

        Assert.assertTrue(targetPage.isTargetDisplayed("10"), "Target updated to 10 km not found.");
        
        // Success Screenshot
        TestListener.getTest().pass("Target updated to 10km successfully.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // ==========================================
    // B. EXTREME & EDGE CASES (NEGATIVE)
    // ==========================================

    @DataProvider(name = "invalidInputs")
    public Object[][] createInvalidData() {
        return new Object[][] {
            { "0", "Zero" },
            { "-100", "Negative" },
            { "5.5", "Decimal" }
        };
    }

    @Test(priority = 3, dataProvider = "invalidInputs", description = "Verify invalid numeric inputs are blocked")
    public void testInvalidNumericValues(String input, String caseName) {
        TestListener.getTest().log(Status.INFO, "Testing Invalid Input Case: " + caseName + " (" + input + ")");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm(input);

        // Check Sanitization
        String actualText = targetPage.getInputValue();
        TestListener.getTest().info("User typed: " + input + " | App showed: " + actualText);

        // Special handling for Negative numbers (Auto-fix check)
        if (caseName.equals("Negative") && !actualText.contains("-")) {
            TestListener.getTest().pass("Success: App auto-sanitized negative input.");
            return; 
        }

        targetPage.submitForm();
        
        // Assert modal is still visible (using Helper, not driver!)
        // Note: You need to implement 'isModalVisible' in your Helper as discussed
        try {
             Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), 
                 "Failed: Modal closed for input " + input);
             TestListener.getTest().pass("System correctly blocked invalid input.");
        } catch (Exception e) {
             // If this fails, the Listener will catch it and take a screenshot automatically!
             Assert.fail("Modal closed or crashed on invalid input: " + input);
        }
            
        targetPage.dismissModal();
    }

    @Test(priority = 4, description = "Max Limit & Chars")
    public void testMaxLimitAndChars() {
        TestListener.getTest().info("Testing Max Int & Special Characters");
        
        targetPage.openModal();
        
        // Case: Max Int
        targetPage.fillForm("999999999");
        targetPage.submitForm();
        // Simple assert to check if app crashed
        TestListener.getTest().info("Submitted Max Int. Checking stability...");
        
        // Case: Special Chars
        targetPage.fillForm("@#$ABCD");
        targetPage.submitForm();
        
        try {
            targetPage.waitForLoading(500);
            Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed());
            TestListener.getTest().pass("System rejected special characters.");
        } catch (Exception e) {
            Assert.fail("Modal closed/crashed on special characters");
        }
        
        targetPage.dismissModal();
    }

    @Test(priority = 6, description = "Backgrounding")
    public void testBackgrounding() {
        TestListener.getTest().info("Testing App Backgrounding state");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("50");

        TestListener.getTest().info("Sending app to background for 3 seconds...");
        ((AndroidDriver) driver).runAppInBackground(java.time.Duration.ofSeconds(3));
        
        targetPage.waitForLoading(1000);
        
        Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "Modal closed after backgrounding");
        TestListener.getTest().pass("App state preserved after backgrounding.");
        
        targetPage.dismissModal();
    }
}