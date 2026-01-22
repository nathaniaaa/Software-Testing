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

    // A. FUNCTIONAL (POSITIVE)
    @Test(priority = 1, description = "Set Valid Target")
    public void testSetValidTarget() {
        // Log to Report instead of Console
        TestListener.getTest().log(Status.INFO, "Starting Test: Set Valid Target (5 km)");

        TestListener.getTest().info("Initial Dashboard State",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        targetPage.cleanUpExistingTarget();
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

        // Take the final Success Screenshot
        TestListener.getTest().pass("Target successfully set to 5 km!", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    @Test(priority = 2, description = "Update Target")
    public void testUpdateTarget() {
        TestListener.getTest().log(Status.INFO, "Starting Test: Update Target to 10 km");
        TestListener.getTest().info("Dashboard state before update", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        targetPage.cleanUpExistingTarget();
        targetPage.openModal();

        targetPage.fillForm("10");
        TestListener.getTest().info("Form filled with new value: 10", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        targetPage.submitForm();
        targetPage.handleSuccessModal();
        targetPage.waitForLoading(3000);

        Assert.assertTrue(targetPage.isTargetDisplayed("10"), "Target updated to 10 km not found.");
        
        // Success Screenshot
        TestListener.getTest().pass("Target updated to 10km successfully.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    }

    // B. EXTREME & EDGE CASES (NEGATIVE)
    @DataProvider(name = "invalidInputs")
    public Object[][] createInvalidData() {
        return new Object[][] {
            { "0", "Zero" },
            { "-100", "Negative" },
            { "5.5", "Decimal" },
            { "999999999", "Max Integer Limit" },
            { "@#$ABCD", "Special Characters" }
        };
    }

    @Test(priority = 3, dataProvider = "invalidInputs", description = "Verify system handles various invalid inputs")
    public void testInvalidNumericValues(String input, String caseName) {
        TestListener.getTest().log(Status.INFO, "Testing Case: " + caseName + " (" + input + ")");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();

        TestListener.getTest().info("Initial Modal State",
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        targetPage.fillForm(input);
        TestListener.getTest().info("Attempted input: " + input,
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        // 1. Check Sanitization (Page Logic)
        String actualText = targetPage.getInputValue();
        TestListener.getTest().info("User typed: " + input + " | App showed: " + actualText);

        // Logic for NEGATIVE numbers
        if (caseName.contains("Negative") && !actualText.contains("-")) {
            TestListener.getTest().pass("Success: App auto-sanitized negative input.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            targetPage.dismissModal();
            return; 
        }

        // Logic for DECIMALS
        if (caseName.contains("Decimal")) {
            if (!actualText.contains(".")) {
                TestListener.getTest().pass("Success: App auto-sanitized decimal input.",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
                targetPage.dismissModal();
                return;
            } else {
                TestListener.getTest().info("'.' detected. Proceeding to check submit block...");
            }
        }

        // Logic for SPECIAL CHARS (Optional: Check if app removed them instantly)
        if (caseName.contains("Special") && actualText.isEmpty()) {
             TestListener.getTest().pass("Success: App prevented typing special characters.",
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            targetPage.dismissModal();
             return;
        }

        // --- SUBMIT CHECK ---
        // If the bad input is still there (like 999999999), try to submit.
        // The system MUST keep the modal open to count as a "Pass".
        targetPage.submitForm();
        targetPage.waitForLoading(1000);

        if (targetPage.isModalVisible()) {
             TestListener.getTest().pass("System correctly blocked input: " + caseName,
                MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        } else {
             // If modal closed, it means the app accepted "999999999" or Crashed
             Assert.fail("Critical Fail: Modal closed or App Crashed on input: " + input);
        }
            
        targetPage.dismissModal();
    }

    @Test(priority = 4, description = "Empty Field Submission")
    public void testEmptyField() {
        TestListener.getTest().info("Testing Empty Field Submission");
        targetPage.openModal();

        // Ensure field is cleared
        targetPage.clearFieldAndBack("1"); // Use your smart clear method
        TestListener.getTest().info("Field cleared (Empty State)", 
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        targetPage.submitForm();
        targetPage.waitForLoading(1000);
        
        Assert.assertTrue(targetPage.isModalVisible(), "Modal closed on Empty Input!");
        TestListener.getTest().pass("System correctly rejected empty input.", 
             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        targetPage.dismissModal();
    }

    @Test(priority = 5, description = "Backgrounding")
    public void testBackgrounding() {
        TestListener.getTest().info("Testing App Backgrounding state");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();

        // Snapshot of the empty modal
        TestListener.getTest().info("Initial Modal State", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
        
        targetPage.fillForm("50");
        TestListener.getTest().info("State before backgrounding (Input: 50)", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

        TestListener.getTest().info("Sending app to background for 3 seconds...");
        ((AndroidDriver) driver).runAppInBackground(java.time.Duration.ofSeconds(3));
        
        targetPage.waitForLoading(1000);
        
        Assert.assertTrue(targetPage.isModalVisible(), "Modal closed after backgrounding");
        TestListener.getTest().pass("App state preserved (Modal visible) after backgrounding.", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
            
        targetPage.dismissModal();
    }
}