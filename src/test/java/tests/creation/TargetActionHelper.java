package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class TargetActionHelper extends CreationActionHelper {

    // --- LOCATORS (Specific to Target Only) ---
    private final By BTN_ADD_TARGET = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View[1]/android.widget.TextView[4]");
    private final By BTN_RESET_TARGET = AppiumBy.xpath("//android.widget.TextView[@text=\"Reset Target\"]");
    private final By TARGET_PROGRESS_TEXT = AppiumBy.xpath("//*[contains(@text, 'km') and contains(@text, 'dari')]");

    // Modal Specifics
    public final By TITLE_MODAL = AppiumBy.xpath("//android.widget.TextView[@text=\"Atur Target Pribadi\"]");
    private final By FIELD_INPUT_KM = AppiumBy.className("android.widget.EditText");
    private final By RADIO_MINGGUAN = AppiumBy.xpath("//*[contains(@text, 'Mingguan')]");
    private final By RADIO_HARIAN = AppiumBy.xpath("//*[contains(@text, 'Harian')]");
    
    // We override the generic submit button with the specific Target one
   private final By BTN_SUBMIT_TARGET = AppiumBy.xpath("//android.widget.Button[@text=\"Atur Target\"]");
   private final By BTN_SELANJUTNYA = AppiumBy.xpath("//android.widget.Button[@text=\"Selanjutnya\"]");
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
            tap(BTN_ADD_TARGET, "Click Add Target Button");    
            wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_MODAL));
        } catch (Exception e) {
            System.out.println("   -> [Target] Open failed. Using Fallback Ratio...");
            tapAtScreenRatio(0.83, 0.21); // Inherited from ActionHelper
        }
    }

    public boolean isModalVisible() {
        try {
            return driver.findElement(TITLE_MODAL).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void fillForm(String value) {
        // Reuse 'fillInputField' from CreationActionHelper!
        fillInputField(FIELD_INPUT_KM, value);

        try { Thread.sleep(1000); } catch (Exception e) {}

        // Specific logic for Radio button
        try {
            tap(RADIO_MINGGUAN, "Select Option: Mingguan",  false);
            capture.highlightAndCapture(RADIO_MINGGUAN, "Highlight Option: Mingguan");
        } catch (Exception e) {
            System.out.println("   -> Tap failed, use default option...");
            capture.highlightAndCapture(RADIO_HARIAN, "Highlight Option: Mingguan");    
        }
    }

    public void submitForm() {
        try {
            tap(BTN_SUBMIT_TARGET, "Click Submit Target");
        } catch (Exception e) {
            System.out.println("   -> Tap failed, trying robust submit...");
            tapAtScreenRatio(0.5, 0.88);  
        }
    }

    public void handleSuccessModal() {
        try {
            // 2. Click the 'Next' Button
            // This takes ANOTHER screenshot with a BLUE box around the button, then clicks
            tap(BTN_SELANJUTNYA, "Click Selanjutnya on Success Modal");
        } catch (Exception e) {
            System.out.println("   -> Tap failed, trying robust submit...");
            tapAtScreenRatio(0.5, 0.88); 
        }
    }

    public boolean isTargetProgressVisible() {
        boolean hasText = !driver.findElements(TARGET_PROGRESS_TEXT).isEmpty();
        boolean hasBtn = !driver.findElements(BTN_RESET_TARGET).isEmpty();

        return hasText || hasBtn;
    }

    public void cleanUpExistingTarget() {
        System.out.println("   -> [Target] Checking cleanup...");
        try {
            boolean isVisible = isTargetProgressVisible();
            if (!isVisible) return; 

            System.out.println("   -> Target found. Cleaning up...");
            
            // 1. Click Reset (Trash Icon)
            try {
                tap(BTN_RESET_TARGET, "Click Reset Target Button");
                Thread.sleep(2000);
            } catch (Exception ignored) {
                tapButtonByTextOrId("Reset Target", "Reset Target");
            }

            // 2. Click Confirm 'Ya'
            try {
                tap(BTN_YA_RESET, "Click Confirm 'Ya'");
                waitForLoading(2500);
            } catch (Exception e) {
                System.out.println("   -> Warning: 'Ya' popup missed.");
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