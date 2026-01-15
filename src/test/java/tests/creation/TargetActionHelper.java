package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class TargetActionHelper extends CreationActionHelper {

    // --- LOCATORS (Specific to Target Only) ---
    private final By BTN_ADD_TARGET = AppiumBy.xpath("//*[contains(@text, 'Target Pribadi')]/parent::*//android.widget.ImageView");
    private final By BTN_RESET_TARGET = AppiumBy.accessibilityId("Reset Target");
    private final By TARGET_PROGRESS_TEXT = AppiumBy.xpath("//*[contains(@text, 'km') and contains(@text, 'dari')]");

    // Modal Specifics
    public final By TITLE_MODAL = AppiumBy.xpath("//*[@text='Atur Target Pribadi']");
    private final By FIELD_INPUT_KM = AppiumBy.className("android.widget.EditText");
    private final By RADIO_MINGGUAN = AppiumBy.xpath("//*[contains(@text, 'Mingguan')]");
    
    // We override the generic submit button with the specific Target one
    private final By BTN_SUBMIT_TARGET = AppiumBy.xpath("//*[contains(@text, 'Atur Target')]");
    private final By BTN_YA_RESET = AppiumBy.xpath("//*[contains(@text, 'Ya') or contains(@text, 'Reset')]");

    // --- CONSTRUCTOR ---
    public TargetActionHelper(AndroidDriver driver) {
        super(driver); // Passes driver up to CreationActionHelper -> ActionHelper
    }

    // --- ACTIONS ---
        public void clearFieldAndBack(String value) {
        try {
            // 1. Click and Type
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM)); // Make sure FIELD_INPUT_KM is defined
            input.click();
            input.sendKeys(value);
            
            Thread.sleep(500);
            
            // 2. Press Backspace (Delete)
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DEL));
            
            // 3. Hide Keyboard (Safety)
            // try { ((AndroidDriver) driver).hideKeyboard(); } catch (Exception ignored) {}
            
        } catch (Exception e) {
            System.out.println("   -> Failed to clear field and back: " + e.getMessage());
        }
    }

    public void openModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(BTN_ADD_TARGET)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_MODAL));
        } catch (Exception e) {
            System.out.println("   -> [Target] Open failed. Using Fallback Ratio...");
            tapAtScreenRatio(0.83, 0.21); // Inherited from ActionHelper
        }
    }

    public void fillForm(String value) {
        // Reuse 'fillInputField' from CreationActionHelper!
        fillInputField(FIELD_INPUT_KM, value);

        try { Thread.sleep(1000); } catch (Exception e) {}

        // Specific logic for Radio button
        try {
            driver.findElement(RADIO_MINGGUAN).click();
        } catch (Exception e) {
            tapElementCenter(RADIO_MINGGUAN); // Inherited from CreationActionHelper
        }
    }

    public void submitForm() {
        // Reuse 'clickSubmitRobust' from CreationActionHelper!
        clickSubmitRobust(BTN_SUBMIT_TARGET, 0.5, 0.88);
         
    }

    public void cleanUpExistingTarget() {
        System.out.println("   -> [Target] Checking cleanup...");
        try {
            boolean hasText = !driver.findElements(TARGET_PROGRESS_TEXT).isEmpty();
            boolean hasBtn = !driver.findElements(BTN_RESET_TARGET).isEmpty();

            if (!hasText && !hasBtn) return; // Clean
            System.out.println("   -> Target found. Cleaning up...");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(BTN_YA_RESET)).click();
                Thread.sleep(2000);
            } catch (Exception ignored) {
                System.out.println("   -> Standard click failed. Using Sniper Fallback...");
                // Uses the robust helper method from ActionHelper
                tapButtonByTextOrId("Reset Target", "Reset Target");
            }

            // Handle 'Ya' / Confirm Popup
            try {
                WebElement btnYa = wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_YA_RESET));
                btnYa.click();
                
                System.out.println("   -> Confirming Reset. Waiting for refresh...");
                waitForLoading(2500); // Wait for dashboard to update
            } catch (Exception e) {
                System.out.println("   -> Warning: 'Ya' popup didn't appear or was missed.");
            }

        } catch (Exception e) {
            System.out.println("   -> Error during cleanup: " + e.getMessage());
        }
        
    }

    public void dismissModal() {
        tapAtScreenRatio(0.83, 0.21);
    }
    
    // --- VERIFICATION ---
    public boolean isTargetDisplayed(String value) {
         try {
            By locator = AppiumBy.xpath("//*[contains(@text, 'dari " + value + "') or contains(@content-desc, 'dari " + value + "')]");
            return driver.findElement(locator).isDisplayed();
         } catch (Exception e) {
             return false;
         }
    }
    
    public String getInputValue() {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
            return input.getText();
        } catch (Exception e) {
            return "";
        }
    }
}