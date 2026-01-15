package tests.creation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest; // Using the BaseTest you provided

public class TargetTest extends BaseTest {

    // We use TargetActionHelper, which now inherits from CreationActionHelper
    private TargetActionHelper targetPage;

    @BeforeClass
    public void setupPage() {
        // 1. Initialize Helper with the driver from BaseTest
        targetPage = new TargetActionHelper((AndroidDriver) driver);

        // 2. Ensure we are on the correct screen (Ayo Lari Dashboard)
        // This method comes from your BaseTest
        // masukKeMenuAyoLari(); 
    }

    // ==========================================
    // A. FUNCTIONAL (POSITIVE)
    // ==========================================

    @Test(priority = 1, description = "1. Set Valid Target")
    public void testSetValidTarget() {
        System.out.println("=== TEST 1: Set Valid Target (5 km) ===");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("5");
        
        // Inherited method from CreationActionHelper
        targetPage.submitForm(); 
        
        // Wait for API response
        targetPage.waitForLoading(2000); 
        
        // Inherited method from CreationActionHelper
        targetPage.handleSuccessModal(); 
        
        // Wait for Dashboard Refresh
        targetPage.waitForLoading(3000); 
        
        Assert.assertTrue(targetPage.isTargetDisplayed("5"), "Target 5 km visual text not found on dashboard.");
        System.out.println("SUCCESS: Target 5 km is verified and visible!");
    }

    @Test(priority = 2, description = "2. Update Target")
    public void testUpdateTarget() {
        System.out.println("=== TEST 2: Update Target (10 km) ===");

        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("10");
        targetPage.submitForm();

        targetPage.waitForLoading(2000);
        targetPage.handleSuccessModal();
        targetPage.waitForLoading(3000);

        Assert.assertTrue(targetPage.isTargetDisplayed("10"), "Target updated to 10 km not found.");
        System.out.println("SUCCESS: Target 10 km is verified and visible!");
    }

// ==========================================
    // B. EXTREME & EDGE CASES (NEGATIVE)
    // ==========================================

    @Test(priority = 3, description = "3, 4, 5. Invalid Numeric Values")
    public void testInvalidNumericValues() {
        System.out.println("=== TEST 3: Invalid Numeric Values ===");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();

        // --- Case: Zero ("0") ---
        System.out.println("   [Check] Testing '0'...");
        targetPage.fillForm("0");
        
        // Try to submit
        targetPage.submitForm();
        targetPage.waitForLoading(500);
        
        // Assert: Modal should still be open
        Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "Modal closed on '0' input!");

        // --- Case: Negative ("-100") ---
        System.out.println("   [Check] Testing '-100'...");
        
        // 1. Clear & Fill
        targetPage.fillForm("-100");
        
        // 2. READ what actually got typed (Sanitization Check)
        String actualText = targetPage.getInputValue(); 
        System.out.println("      -> We typed '-100', App wrote: '" + actualText + "'");

        // 3. Logic: Did the app fix it automatically?
        if (!actualText.contains("-")) {
            System.out.println("      -> SUCCESS: App auto-sanitized negative input.");
        } else {
            // If '-' is present, button must be disabled or submit must fail
            System.out.println("      -> '-' detected. Checking submit block...");
            targetPage.submitForm();
            targetPage.waitForLoading(500);
            Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "Modal closed on negative input!");
        }

        // --- Case: Decimal ("5.5") ---
        System.out.println("   [Check] Testing '5.5'...");
        
        // 1. Clear & Fill
        targetPage.fillForm("5.5");
        
        // 2. READ what actually got typed
        actualText = targetPage.getInputValue();
        System.out.println("      -> We typed '5.5', App wrote: '" + actualText + "'");

        // 3. Logic
        if (!actualText.contains(".")) {
            System.out.println("      -> SUCCESS: App auto-sanitized decimal input.");
        } else {
            System.out.println("      -> '.' detected. Checking submit block...");
            targetPage.submitForm();
            targetPage.waitForLoading(500);
            Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "Modal closed on decimal input!");
        }

        targetPage.dismissModal();
    }

    @Test(priority = 4, description = "6, 7. Max Limit & Chars")
    public void testMaxLimitAndChars() {
        System.out.println("=== TEST 4: Max Limit & Chars ===");
        
        // Ensure modal is open (in case previous test failed to dismiss)
        // if (driver.findElements(targetPage.TITLE_MODAL).isEmpty()) targetPage.openModal();
        targetPage.openModal();
        // --- Case: Max Int ---
        System.out.println("   [Check] Testing Max Int...");
        targetPage.fillForm("999999999");
        
        // Hide keyboard by clicking title (Stability)
        // try { driver.findElement(targetPage.TITLE_MODAL).click(); } catch (Exception e) {}
        
        targetPage.submitForm();
        targetPage.waitForLoading(1000);
        
        Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "App Crashed or Modal closed on Max Int");

        // --- Case: Special Characters ---
        System.out.println("   [Check] Testing Special Chars...");
        targetPage.fillForm("@#$ABCD");
        
        try { driver.findElement(targetPage.TIWTLE_MODAL).click(); } catch (Exception e) {}
        
        targetPage.submitForm();
        
        try {
            targetPage.waitForLoading(500);
            Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem menerima karakter spesial.");
            System.out.println("      -> Special Char check passed (Rejected).");
        } catch (Exception e) {
             Assert.fail("Modal tertutup/Crash saat input karakter aneh.");
        }
        
        targetPage.dismissModal();
    }

    @Test(priority = 5, description = "8. Empty Field")
    public void testEmptyField() {
        System.out.println("=== TEST 5: Empty Field ===");
        
        // if (driver.findElements(targetPage.TITLE_MODAL).isEmpty()) targetPage.openModal();
        targetPage.openModal();
        // --- Smart Clear (Type '1' then Delete) ---
        // This ensures the field is truly "touched" and empty
        targetPage.clearFieldAndBack("1"); 
        
        System.out.println("   [Check] Clicking Submit on Empty Field...");
        
        targetPage.submitForm();
        
        try {
            targetPage.waitForLoading(1000); 
            Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem mengizinkan target kosong.");
            System.out.println("      -> Empty check passed (System rejected empty submission).");
        } catch (Exception e) {
            Assert.fail("Modal tertutup saat field kosong (Bug Critical).");
        }

        targetPage.dismissModal();
    }

    // ==========================================
    // C. UI / INTERACTION
    // ==========================================

    @Test(priority = 6, description = "10. Backgrounding")
    public void testBackgrounding() {
        System.out.println("=== TEST 7: Backgrounding ===");
        
        targetPage.cleanUpExistingTarget();
        targetPage.openModal();
        targetPage.fillForm("50");

        System.out.println("   -> Going to background...");
        // This command minimizes the app for 3 seconds then brings it back
        ((AndroidDriver) driver).runAppInBackground(java.time.Duration.ofSeconds(3));
        
        targetPage.waitForLoading(1000);
        
        Assert.assertTrue(driver.findElement(targetPage.TITLE_MODAL).isDisplayed(), "Modal closed after backgrounding");
        targetPage.dismissModal();
    }
}