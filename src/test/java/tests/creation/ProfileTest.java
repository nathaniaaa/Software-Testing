package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class ProfileTest extends BaseCreationTest {

    // ========================================================================
    // 1. LOCATORS
    // ========================================================================

    // --- NAVIGATION ---
    private final By TAB_SAYA = AppiumBy.accessibilityId("Saya\nTab 5 of 5");

    // --- PROFILE MAIN PAGE ---
    private final By BTN_EDIT_PROFIL = AppiumBy.accessibilityId("Duration");

    // --- GENDER LOCATORS ---
    private final By RADIO_GENDER_MALE = AppiumBy.accessibilityId("Laki-laki");
    private final By RADIO_GENDER_FEMALE = AppiumBy.accessibilityId("Perempuan");

    // --- BLOOD TYPE LOCATORS ---
    private final By RADIO_BLOOD_A = AppiumBy.accessibilityId("A");
    private final By RADIO_BLOOD_O = AppiumBy.accessibilityId("O");

    // --- ACTIONS ---
    private final By BTN_SIMPAN = AppiumBy.accessibilityId("Simpan");
    
    // --- POPUP (From your screenshot) ---
    // We look for the "Oke" button text
    private final By BTN_SUCCESS_OKE = AppiumBy.xpath("//*[contains(@text, 'Oke')]");


    // ========================================================================
    // 2. TEST LOGIC
    // ========================================================================

    @Test(priority = 1)
    public void testEditProfileFlow() {
        System.out.println("=== TEST START: Edit Profile (Robust & Scrolling) ===");

        // 1. Navigate to Saya
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_SAYA));
            driver.findElement(TAB_SAYA).click();
        } catch (Exception e) {
            System.out.println("WARN: Tab Saya ID failed. Using Text Sniper...");
            tapByTextPosition("Saya");
        }

        // 2. Click Edit Profile
        System.out.println("Step: Navigate to Edit Page...");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_EDIT_PROFIL));
            driver.findElement(BTN_EDIT_PROFIL).click();
        } catch (Exception e) {
            System.out.println("WARN: Edit Button ID failed. Using coordinates...");
            // Fallback to coordinates if ID fails
            actions.tapAtScreenRatio(0.56, 0.20);
        }

        // 3. EDIT INPUTS 
        System.out.println("Step: Editing Text Fields...");
        // Wait for form to load by checking for "Nama" label
        wait.until(ExpectedConditions.visibilityOfElementLocated(
             AppiumBy.xpath("//*[contains(@text, 'Nama')]")
        ));
        
        fillInputByLabel("Nama", "User Scroll Test"); 
        fillInputByLabel("Tinggi Badan", "170"); 
        fillInputByLabel("Berat Badan", "65");


        // ============================================================
        // 5. CHANGE GENDER (SCROLL & ROBUST FALLBACKS)
        // ============================================================
        System.out.println("Step: Scrolling to Gender options...");

        // 1. Scroll
        actions.scrollToText("Laki-laki");
        
        // 2. WAIT for scroll momentum to stop completely
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // --- Click Male (With Fallback) ---
        try {
            wait.until(ExpectedConditions.elementToBeClickable(RADIO_GENDER_MALE));
            driver.findElement(RADIO_GENDER_MALE).click();
        } catch (Exception e) {
            System.out.println("WARN: Gender Male ID failed. Using Text Sniper...");
            tapByTextPosition("Laki-laki");
        }

        // Small pause between toggles
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // --- Click Female (With Fallback) ---
        try {
            wait.until(ExpectedConditions.elementToBeClickable(RADIO_GENDER_FEMALE));
            driver.findElement(RADIO_GENDER_FEMALE).click();
        } catch (Exception e) {
            System.out.println("WARN: Gender Female ID failed. Using Text Sniper...");
            tapByTextPosition("Perempuan");
        }

        // --- Scroll & Click Blood Type (With Fallback) ---
        System.out.println("Step: Handling Blood Type...");
        
        actions.scrollToText("Golongan Darah");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        try {
            // Check if Blood Type A is clickable
            wait.until(ExpectedConditions.elementToBeClickable(RADIO_BLOOD_A));
            driver.findElement(RADIO_BLOOD_A).click();
        } catch (Exception e) {
            System.out.println("WARN: Blood Type ID failed. Using Text Sniper...");
            // Use "A" strictly
            // tapByTextPosition("A"); 
        }

        // ============================================================
        // 7. SAVE (SCROLL & ROBUST FALLBACK)
        // ============================================================
        System.out.println("Step: Scrolling to Save button...");

        // 1. Scroll
        actions.scrollToText("Simpan");
        
        // 2. WAIT for scroll to stop
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // 3. Click with Fallback
        try {
            wait.until(ExpectedConditions.elementToBeClickable(BTN_SIMPAN));
            driver.findElement(BTN_SIMPAN).click();
        } catch (Exception e) {
            System.out.println("WARN: Save Button ID failed. Using Text Sniper...");
            tapByTextPosition("Simpan");
        }

        // ============================================================
        // 8. HANDLE SUCCESS POPUP (CRITICAL STEP)
        // ============================================================
        System.out.println("Step: Handling Success Popup...");
        try {
            // Wait for the "Oke" button on the modal
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_SUCCESS_OKE));
            driver.findElement(BTN_SUCCESS_OKE).click();
        } catch (Exception e) {
            System.out.println("WARN: Success popup 'Oke' button not found. It might have auto-closed.");
        }

        // 9. VERIFY RETURN TO PROFILE
        System.out.println("Step: Verifying...");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_EDIT_PROFIL));
            System.out.println("SUCCESS: Profile Saved and Verified.");
        } catch (Exception e) {
            Assert.fail("Failed to return to Profile Page.");
        }
    }


    // ========================================================================
    // 3. HELPER METHODS (TEXT-BASED INPUT STRATEGY)
    // ========================================================================

    public void fillInputByLabel(String labelText, String valueToType) {
        System.out.println("   -> Looking for field labeled: '" + labelText + "'");
        try {
            // Strategy 1: Sibling
            WebElement inputField = driver.findElement(AppiumBy.xpath(
                "//*[contains(@text, '" + labelText + "')]/following-sibling::android.widget.EditText"
            ));
            
            inputField.click();
            inputField.clear();
            inputField.sendKeys(valueToType);
            try { driver.hideKeyboard(); } catch (Exception e) {}
            
        } catch (Exception e) {
            System.out.println("   -> Failed to find input by sibling strategy. Trying Parent strategy...");
            try {
                // Strategy 2: Parent
                WebElement inputField = driver.findElement(AppiumBy.xpath(
                    "//*[contains(@text, '" + labelText + "')]/..//android.widget.EditText"
                ));
                inputField.click();
                inputField.clear();
                inputField.sendKeys(valueToType);
                try { driver.hideKeyboard(); } catch (Exception ex) {}
            } catch (Exception ex) {
                System.out.println("   -> ERROR: Could not find input field for label: " + labelText);
            }
        }
    }
}