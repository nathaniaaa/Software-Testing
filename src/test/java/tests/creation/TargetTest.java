package tests.creation;

import java.time.Duration;

import org.openqa.selenium.By; 
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class TargetTest extends BaseCreationTest {

    // --- LOCATORS ---
    private final By BTN_ADD_TARGET = AppiumBy.xpath("//*[contains(@text, 'Target Pribadi')]/parent::*//android.widget.ImageView");
    // Updated locator to be more specific to the Dashboard element
    // private final By BTN_RESET_TARGET = AppiumBy.xpath("//*[contains(@content-desc, 'Reset Target') or (contains(@text, 'km') and contains(@text, '/'))]");
    private final By BTN_RESET_TARGET = AppiumBy.accessibilityId("Reset Target");
    private final By TARGET_PROGRESS_TEXT = AppiumBy.xpath("//*[contains(@text, 'km') and contains(@text, 'dari')]");


    // Modal & Form
    private final By TITLE_MODAL = AppiumBy.xpath("//*[@text='Atur Target Pribadi']");
    private final By BTN_CLOSE = AppiumBy.xpath("//*[@text='Atur Target Pribadi']/following-sibling::android.widget.ImageView");
    
    private final By FIELD_INPUT_KM = AppiumBy.className("android.widget.EditText");
    private final By RADIO_MINGGUAN = AppiumBy.xpath("//*[contains(@text, 'Mingguan')]");
    private final By BTN_SUBMIT = AppiumBy.xpath("//*[contains(@text, 'Atur Target')]");
    
    // Popups
    private final By POPUP_CONFIRM = AppiumBy.xpath("//*[contains(@text, 'Yakin') or contains(@text, 'Konfirmasi')]");
    private final By BTN_YA_RESET = AppiumBy.xpath("//*[contains(@text, 'Ya') or contains(@text, 'Reset')]");


    // ==========================================
    // A. FUNCTIONAL (POSITIVE)
    // ==========================================

    @Test(priority = 1, description = "1. Set Valid Target")
    public void testSetValidTarget() {
        System.out.println("=== TEST 1: Set Valid Target (5 km) ===");
        cleanUpExistingTarget(); 

        openModal();
        fillForm("5");
        
        // 1. Submit
        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        
        // 2. Wait for loading (Network request) & Handle Success
        waitForLoading(2000); // Wait for API response
        handleSuccessModal();
        
        // 3. Wait for Dashboard Refresh
        System.out.println("   -> Waiting for dashboard to refresh...");
        waitForLoading(3000); // Give dashboard time to render new number

        // ... inside testSetValidTarget ...

        // 3. Verify Dashboard
        System.out.println("   -> Verifying '5' is displayed on screen...");

        try {
            // Look for ANY text element containing "5" and "km" or just the target number
            // This xpath looks for "5" appearing in text or content-desc anywhere
            By targetTextLocator = AppiumBy.xpath("//*[contains(@text, 'dari 5') or contains(@content-desc, 'dari 5')]");
            
            WebDriverWait verifyWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement targetText = verifyWait.until(ExpectedConditions.visibilityOfElementLocated(targetTextLocator));
            
            System.out.println("   -> Success: Found text '" + targetText.getText() + "' on dashboard.");
            
        } catch (Exception e) {
            // Screenshot for proof
            // actions.takeScreenshot("TargetFailure_5km"); 
            Assert.fail("Target 5 km visual text not found on dashboard.");
        }
        
        // try {
        //     // Verify dashboard updated
        //     String dashboardText = driver.findElement(BTN_RESET_TARGET).getText(); 
        //     Assert.assertTrue(dashboardText.contains("5"), "Dashboard tidak update ke 5 km. Found: " + dashboardText);
        // } catch (Exception e) {
        //     Assert.fail("Target 5 km tidak muncul di dashboard.");
        // }
    }

    @Test(priority = 2, description = "2. Update Target (Condition: 0 Progress vs Progress)")
    public void testUpdateTarget() {
        System.out.println("=== TEST 2: Update Target (10 km) ===");

        // 1. Klik Reset (Target Lama)
        System.out.println("   -> Klik Reset Target...");
        cleanUpExistingTarget();

        // 4. Create New Target (Value 10)
        openModal(); 
        fillForm("10");
        
        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        
        waitForLoading(2000); // Wait for API
        handleSuccessModal();

        // 5. Verify Update
        System.out.println("   -> Waiting for dashboard to update to 10km...");
        waitForLoading(3000); // Dashboard refresh delay

        System.out.println("   -> Verifying '10' is displayed on screen...");

        try {
            // Look for ANY text element containing "5" and "km" or just the target number
            // This xpath looks for "5" appearing in text or content-desc anywhere
            By targetTextLocator = AppiumBy.xpath("//*[contains(@text, 'dari 10') or contains(@content-desc, 'dari 10')]");
            
            WebDriverWait verifyWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement targetText = verifyWait.until(ExpectedConditions.visibilityOfElementLocated(targetTextLocator));
            
            System.out.println("   -> Success: Found text '" + targetText.getText() + "' on dashboard.");
            
        } catch (Exception e) {
            // Screenshot for proof
            // actions.takeScreenshot("TargetFailure_5km"); 
            Assert.fail("Target 10 km visual text not found on dashboard.");
        }
        
    //     try {
    //         String dashboardText = driver.findElement(BTN_RESET_TARGET).getText();
    //         Assert.assertTrue(dashboardText.contains("10"), "Dashboard tidak update ke 10 km");
    //         System.out.println("   -> Success: Target updated to 10km.");
    //     } catch (Exception e) { Assert.fail("Target 10 km gagal update."); }
    }

    // ==========================================
    // B. EXTREME & EDGE CASES (NEGATIVE)
    // ==========================================

@Test(priority = 3, description = "3, 4, 5. Invalid Numeric Values")
    public void testInvalidNumericValues() {
        System.out.println("=== TEST 3: Invalid Numeric Values ===");
        
        cleanUpExistingTarget();
        openModal();

        // --- Case 3: Zero ("0") ---
        // Zero is usually allowed to be typed, but disallowed to submit.
        System.out.println("   [Check] Testing '0'...");
        fillForm("0");
        checkAndForceSubmit("0"); // This expects button to be disabled or modal to stay open

        // --- Case 4: Negative ("-100") ---
        System.out.println("   [Check] Testing '-100'...");
        // 1. Clear previous "0"
        WebElement input = driver.findElement(FIELD_INPUT_KM);
        input.clear();
        
        // 2. Type "-100"
        fillForm("-100");
        
        // 3. READ what actually got typed
        String actualText = input.getText(); // Likely returns "100"
        System.out.println("      -> We typed '-100', App wrote: '" + actualText + "'");

        // 4. LOGIC: If the app stripped the '-', the test PASSES (Safety mechanism works)
        if (!actualText.contains("-")) {
            System.out.println("      -> SUCCESS: App auto-sanitized negative input.");
        } else {
            // If the '-' IS present, THEN we expect the button to be disabled
            checkAndForceSubmit("-100");
        }

        // --- Case 5: Decimal ("5.5") ---
        System.out.println("   [Check] Testing '5.5'...");
        input.click();
        input.clear();
        
        // 2. Type "5.5"
        fillForm("5.5");
        
        // 3. READ what actually got typed
        actualText = input.getText(); // Likely returns "55"
        System.out.println("      -> We typed '5.5', App wrote: '" + actualText + "'");

        // 4. LOGIC: If the app stripped the '.', the test PASSES
        if (!actualText.contains(".")) {
             System.out.println("      -> SUCCESS: App auto-sanitized decimal input.");
        } else {
             // If decimal point exists, button must be disabled
             checkAndForceSubmit("5.5");
        }
        
        dismissModal();
    }
    @Test(priority = 4, description = "6, 7. Max Limit & Chars")
    public void testMaxLimitAndChars() {
        System.out.println("=== TEST 4: Max Limit & Chars ===");
        
        if (driver.findElements(TITLE_MODAL).isEmpty()) openModal();

        // --- Case 6: Max Int ("999999999") ---
        System.out.println("   [Check] Testing Max Int...");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
        input.click();
        input.clear();
        input.sendKeys("999999999");
        
        // Hide keyboard
        try { driver.findElement(TITLE_MODAL).click(); } catch (Exception e) {}
        
        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        waitForLoading(1000); // Check if app crashes immediately
        
        Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), "App Crash atau Modal tertutup!");
        System.out.println("   -> Max Int check passed (No Crash).");

        // --- Case 7: Special Characters ---
        System.out.println("   [Check] Testing Special Chars...");
        input.click();
        input.clear();
        input.sendKeys("@#$ABCD");
        
        try { driver.findElement(TITLE_MODAL).click(); } catch (Exception e) {}
        
        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        
        try {
            Thread.sleep(500);
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem menerima karakter spesial.");
            System.out.println("   -> Special Char check passed (Rejected).");
        } catch (Exception e) {
            Assert.fail("Modal tertutup/Crash saat input karakter aneh.");
        }
        
        dismissModal();
    }

    @Test(priority = 5, description = "8. Empty Field")
    public void testEmptyField() {
        System.out.println("=== TEST 5: Empty Field ===");
        
        if (driver.findElements(TITLE_MODAL).isEmpty()) openModal();
        
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
        input.click();
        
        // --- Smart Clear ---
        input.sendKeys("1"); 
        try { Thread.sleep(200); } catch(Exception e){}
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DEL));
        
        try { driver.findElement(TITLE_MODAL).click(); } catch(Exception e) {}
        
        System.out.println("   [Check] Clicking Submit on Empty Field...");
        
        WebElement btn = driver.findElement(BTN_SUBMIT);
        if(btn.isEnabled()) {
            System.out.println("   [Warning] Submit button is Enabled on Empty Field! (UI Issue)");
        }

        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        
        try {
            waitForLoading(1000); // Wait to see if modal closes
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem mengizinkan target kosong.");
            System.out.println("   -> Empty check passed (System rejected empty submission).");
        } catch (Exception e) {
            Assert.fail("Modal tertutup saat field kosong (Bug Critical).");
        }

        dismissModal();
    }

    // ==========================================
    // C. UI / INTERACTION
    // ==========================================

    @Test(priority = 6, description = "9. Cancel/Dismiss")
    public void testCancelDismiss() {
        System.out.println("=== TEST 6: Cancel/Dismiss ===");
        
        cleanUpExistingTarget();
        openModal();
        fillForm("5");
        
        clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
        handleSuccessModal();

        // Cancel Reset
        try { driver.findElement(BTN_RESET_TARGET).click(); } catch(Exception e) {}
        try { 
             waitForLoading(1000);
             if(driver.findElements(POPUP_CONFIRM).size() > 0) driver.findElement(BTN_YA_RESET).click();
        } catch(Exception e) {} 
        
        waitForLoading(2000); // Wait for reset to finish

        cleanUpExistingTarget();

        openModal();
        fillForm("100"); 

        dismissModal(); 

        // Verify dashboard NOT 100
        try {
            Thread.sleep(1000);
            if(driver.findElements(BTN_RESET_TARGET).size() > 0) {
                String text = driver.findElement(BTN_RESET_TARGET).getText();
                Assert.assertFalse(text.contains("100"), "Value 100 tersimpan padahal di-cancel!");
            }
        } catch (Exception e) {}
    }

    @Test(priority = 7, description = "10. Backgrounding")
    public void testBackgrounding() {
        System.out.println("=== TEST 7: Backgrounding ===");
        
        cleanUpExistingTarget();
        if (driver.findElements(TITLE_MODAL).isEmpty()) openModal();
        fillForm("50");

        System.out.println("   -> Going to background...");
        ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(3));
        waitForLoading(1000); // Wait for app to resume UI
        
        try {
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed());
        } catch (Exception e) {
            Assert.fail("Modal tertutup setelah backgrounding.");
        }

        dismissModal();
    }


    // --- HELPERS (TARGET SPECIFIC) ---

    // Simple Helper for Stability Pauses
    public void waitForLoading(int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {}
    }

    public void fillForm(String value) {
        fillInputField(FIELD_INPUT_KM, value);

        // Wait for keyboard to fully retract (Crucial for small screens)
        waitForLoading(2000);

        try {
            driver.findElement(RADIO_MINGGUAN).click(); 
        } catch (Exception e) {
            System.out.println("   -> Radio click retry...");
            driver.findElement(RADIO_MINGGUAN).click(); 
        }
        waitForLoading(500);
    }

    private void checkAndForceSubmit(String testCaseLabel) {
        try {
            WebElement btn = driver.findElement(BTN_SUBMIT);
            if (btn.isEnabled()) {
                System.out.println("   [Warning] Button ENABLED for " + testCaseLabel);
            } else {
                System.out.println("   [Info] Button DISABLED for " + testCaseLabel);
            }

            clickSubmitRobust(BTN_SUBMIT, 0.5, 0.88);
            
            // Wait briefly to ensure no crash happened immediately
            waitForLoading(500);
            Assert.assertTrue(driver.findElements(TITLE_MODAL).size() > 0, "Seharusnya tidak bisa submit: " + testCaseLabel);
            
        } catch (Exception e) {
            System.out.println("   -> Error checking submit: " + e.getMessage());
        }
    }

    private void dismissModal() {
        try {
            System.out.println("   -> Closing modal via Percentage Tap...");
            // tapByPercentage(0.92, 0.45); 
            tapByPercentage(0.83, 0.21);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(TITLE_MODAL));
        } catch (Exception e) {
            System.out.println("   -> Warning: Could not close modal.");
        }
    }

    public void openModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(BTN_ADD_TARGET)).click();
            // Wait for modal animation to finish
            wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_MODAL));
            waitForLoading(500); // Extra safety for animation
        } catch (Exception e) {
            System.out.println("   -> Standard click failed. Using Fallback Ratio Tap...");
            tapByPercentage(0.83, 0.21); 
            
            // Wait for modal after tap
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_MODAL));
            } catch (Exception ex) {
                System.out.println("   -> Failed to open modal even with fallback.");
            }
            // actions.tapByCoordinates(900, 450); 
        }
    }

    // public void cleanUpExistingTarget() {
    //     System.out.println("   -> Checking for existing target...");

    //     try {
    //         // 1. POSITIVE CHECK: Wait up to 3 seconds for the RESET button to appear.
    //         // If the app is loading, this will wait until it finishes.
    //         // If the dashboard is clean, this will time out (fail) after 3s.
    //         WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            
    //         // We look for the RESET target, not the invisibility of Add
    //         WebElement resetButton = shortWait.until(ExpectedConditions.visibilityOfElementLocated(BTN_RESET_TARGET));

    //         // 2. If line above passes, Target DEFINITELY exists.
    //         System.out.println("   -> Found Reset Button. Cleaning up...");
            
    //         try {
    //             resetButton.click();
    //         } catch (Exception clickErr) {
    //             // Fallback for Samsung A14
    //             System.out.println("   -> Click failed. Using Ratio Tap...");
    //             tapByPercentage(0.79, 0.27); // Corrected Y-Ratio for Target
    //         }

    //         // 3. Handle Popup
    //         try {
    //             WebElement btnYa = wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_YA_RESET));
    //             btnYa.click();
    //             System.out.println("   -> Reset Confirmed. Waiting for refresh...");
    //             waitForLoading(3000); 
    //         } catch (Exception e) {
    //             System.out.println("   -> Popup missed or there is no popup.");
    //         }

    //     } catch (Exception e) {
    //         // 4. If the wait times out, it means Reset Button never appeared.
    //         // This confirms the dashboard is clean.
    //         System.out.println("   -> No target found (Clean Dashboard).");
    //     }
    // }

    public void cleanUpExistingTarget() {
        System.out.println("   -> Checking for existing target to cleanup...");

        try {
            boolean textFound =
                driver.findElements(TARGET_PROGRESS_TEXT).size() > 0;

            boolean buttonFound =
                driver.findElements(BTN_RESET_TARGET).size() > 0;

            // OR LOGIC (THIS IS WHAT YOU ASKED)
            if (!textFound && !buttonFound) {
                throw new Exception("No target indicators found");
            }

            System.out.println("   -> Found Reset Target button. Clicking...");

            try {
                wait.until(ExpectedConditions.elementToBeClickable(BTN_RESET_TARGET)).click();
            } catch (Exception clickErr) {
                System.out.println("   -> Standard click failed. Using Ratio Tap (0.79, 0.27)...");
                // tapByPercentage(0.79, 0.27);
                tapByTextPosition("Reset Target");
            }

            // POPUP HANDLING — UNCHANGED
            try {
                WebElement btnYa =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_YA_RESET));
                btnYa.click();

                System.out.println("   -> Confirming Reset. Waiting for refresh...");
                waitForLoading(2500);
            } catch (Exception e) {
                System.out.println("   -> Warning: 'Ya' popup didn't appear or was missed.");
            }

        } catch (Exception e) {
            System.out.println("   -> No existing target found (Dashboard is clean).");
        }
    }


    protected void clickSubmitRobust(By locator, double xRatio, double yRatio) {
         try {
            System.out.println("   -> Attempting Robust Submit (Ratio: " + yRatio + ")...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            tapByPercentage(xRatio, yRatio);
        } catch (Exception e) {
            System.out.println("   -> Robust submit interaction failed: " + e.getMessage());
        }
    }
}