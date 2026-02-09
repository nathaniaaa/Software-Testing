package tests.creation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import tests.ActionHelper; 
import tests.helper.CaptureHelper;

public class CreationActionHelper extends ActionHelper{
    
    protected CaptureHelper capture;

    public CreationActionHelper(AndroidDriver driver) {
        super(driver);
        
        this.capture = new CaptureHelper(driver);
    }
    // --- COMMON LOCATORS ---
    private final By BTN_NEXT_GENERIC = AppiumBy.xpath("//*[contains(@text, 'Selanjutnya') or contains(@text, 'Ya, Lanjutkan') or contains(@text, 'Oke')]");
    private final By TXT_SUCCESS_GENERIC = AppiumBy.xpath("//*[contains(@text, 'Berhasil') or contains(@text, 'Apakah ingin melanjutkan?')]");
    protected String inputXpathTemplate = "//*[@text='%s']/following-sibling::android.widget.EditText";

    // --- BUSINESS LOGIC & STRATEGIES ---

    /**
     * Robust Submit Action.
     * Checks if enabled. If standard click fails, falls back to ActionHelper coordinates.
     */
    public void clickSubmitRobust(By locator) {
        try {
            System.out.println("   -> Attempting Robust Submit...");
            
            // 1. Find the element once
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // 2. Check logic
            if (btn.isEnabled()) {
                System.out.println("   -> Element enabled.");
                
                tapElementCenter(btn); 
                
            } else {
                System.out.println("   -> Button is disabled/not ready.");
            }
        } catch (Exception e) {
            System.out.println("   -> Robust submit failed: " + e.getMessage());
        }
    }

    /**
     * Overload: Allows Robust Submit using Screen Ratios (Percentages)
     * Example: clickSubmitRobust(BTN, 0.5, 0.88);
     */
    public void clickSubmitRobust(By locator, double xRatio, double yRatio) {
        try {
            System.out.println("   -> Attempting Robust Submit (Ratio)...");
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            
            if (btn.isEnabled()) {
                System.out.println("   -> Element enabled. Tapping ratio: " + xRatio + ", " + yRatio);
                // Call the method that handles ratios!
                tapAtScreenRatio(xRatio, yRatio);
            }
        } catch (Exception e) {
            System.out.println("   -> Robust submit (ratio) failed.");
        }
    }

    public void fillInputField(By locator, String text) {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            input.click();
            input.clear();
            input.sendKeys(text);
            capture.highlightAndCapture(locator, "Filling field with: " + text);
            // try { driver.hideKeyboard(); } catch (Exception ignored) {}
        } catch (Exception e) {
            System.out.println("   -> Failed to fill input: " + locator);
        }
    }

    public void handleSuccessModal() {
        try {
            // 2. Click the 'Next' Button
            // This takes ANOTHER screenshot with a BLUE box around the button, then clicks
            tap(BTN_NEXT_GENERIC, "Click Next on Success Modal");
        } catch (Exception e) {
            System.out.println("   -> Success modal auto-dismissed or not found.");
        }
    }

    // --- SNIPER LOGIC (Strategies for finding tricky elements) ---

    public void tapButtonByTextOrId(String text, String accId) {
        try {
            // Custom Fast Wait (3s)
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement el = fastWait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(accId)));
            
            tap(el, "Click via Access. ID: " + accId); 
            return;
        } catch (Exception ignored) {
            System.out.println("ID not found, trying text...");
        }

        try {
            tap(AppiumBy.xpath("//*[contains(@text, '" + text + "')]"), "Click via Text: " + text);
        } catch (Exception e) {
            tapByTextPosition(text);
        }
    }

    public void tapByAccessibilityId(String accId) {
        try {
            System.out.println("   -> [Strict] Attempting Accessibility ID: " + accId);
            wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(accId))).click();
            System.out.println("   -> Clicked successfully.");
        } catch (Exception e) {
            System.out.println("   -> Standard click failed. Engaging Center Tap fallback...");
            tapElementCenter(AppiumBy.accessibilityId(accId));
        }
    }

    /**
     * Calculates center of an element by locator and taps it blindly.
     * Used for icon-only buttons (like FABs) where text search won't work.
     */
    protected void tapElementCenter(By locator) {
        try {
            System.out.println("   -> [Sniper] Calculating center for locator...");
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            int centerX = btn.getLocation().getX() + (btn.getSize().getWidth() / 2);
            int centerY = btn.getLocation().getY() + (btn.getSize().getHeight() / 2);
            
            System.out.println("   -> Tapping coordinates: " + centerX + ", " + centerY);
            tapByCoordinates(centerX, centerY);
        } catch (Exception e) {
            System.out.println("   -> Failed to tap element center: " + e.getMessage());
            throw new RuntimeException("Tap failed"); // Throw so fallback can catch it
        }
    }

    public void clickByLabelOffset(String labelText) {
        try {
            System.out.println("   -> [Sniper Click] Targeting box below label: '" + labelText + "'");
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + labelText + "') or contains(@content-desc, '" + labelText + "')]")
            ));

            // Get screen density factor (rough approximation) or use relative size
            int labelHeight = label.getSize().getHeight();      
            int targetX = label.getLocation().getX() + (label.getSize().getWidth() / 2);
            int targetY = label.getLocation().getY() + labelHeight + (int)(labelHeight * 1.5);
            
            System.out.println("   -> Tapping offset at Y[" + targetY + "]");
            tapByCoordinates(targetX, targetY);
            
            Thread.sleep(1000); // Wait for UI reaction
        } catch (Exception e) {
            System.out.println("   -> Sniper Click failed for '" + labelText + "': " + e.getMessage());
        }
    }

        /**
     * Specific method for Profile Page where inputs are Siblings of the Label
     */
    protected void fillInputByLabelSibling(String labelText, String valueToType) {
        try {
            String xpath = String.format("//*[@text='%s']/following-sibling::android.widget.EditText", labelText);
            WebElement input = driver.findElement(AppiumBy.xpath(xpath));
            input.click();
            input.clear();
            input.sendKeys(valueToType);
            try { driver.hideKeyboard(); } catch (Exception e) {}
        } catch (Exception e) {
            System.out.println("   -> Input '" + labelText + "' not found.");
        }
    }

    public void fillInputByLabelOffset(String labelText, String valueToType) {
        try {
            System.out.println("   -> [Sniper Offset] Targeting input below label: '" + labelText + "'");
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + labelText + "') or contains(@content-desc, '" + labelText + "')]")
            ));

            // Get screen density factor (rough approximation) or use relative size
            int labelHeight = label.getSize().getHeight();      
            int targetX = label.getLocation().getX() + (label.getSize().getWidth() / 2);
            int targetY = label.getLocation().getY() + labelHeight + (int)(labelHeight * 1.5);

            System.out.println("   -> Tapping offset at [" + targetY + "]");
            tapByCoordinates(targetX, targetY);

            Thread.sleep(500); // Focus wait
            
            new org.openqa.selenium.interactions.Actions(driver).sendKeys(valueToType).perform();
            try { driver.hideKeyboard(); } catch (Exception ignored) {}
            
            System.out.println("   -> Typed value: " + valueToType);
        } catch (Exception e) {
            System.out.println("   -> Sniper Offset failed for '" + labelText + "': " + e.getMessage());
        }
    }

    // --- DATA HELPERS ---

    public String getDefaultDateText(int daysToAdd) {
        LocalDate date = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);
        return date.format(formatter);
    }

    public String fillInputAndReadBack(String label, String valueToType) {
        try {
            String xpath = String.format(inputXpathTemplate, label);
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(xpath)));
            
            input.click();
            input.clear();
            input.sendKeys(valueToType);
            
            // Critical: Hide keyboard so the App triggers its "onBlur" or "Formatting" logic
            try { driver.hideKeyboard(); } catch (Exception ignored) {}
            Thread.sleep(1000); // Give app 1s to auto-sanitize
            
            return input.getText(); // Return what is ACTUALLY in the box
        } catch (Exception e) {
            System.out.println("Failed to interact with input: " + label);
            return "";
        }
    }

    /**
     * READS the value of an input field without modifying it.
     * Use this for verifying saved data.
     */
    public String getInputValue(String label) {
        try {
            // Re-use your existing xpath template
            String xpath = String.format("//*[@text='%s']/following-sibling::android.widget.EditText", label);
            
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(xpath)));
            
            // JUST return text. Do not Click. Do not Clear.
            return input.getText(); 
        } catch (Exception e) {
            System.out.println("WARN: Could not read value for label: " + label);
            return "";
        }
    }



}