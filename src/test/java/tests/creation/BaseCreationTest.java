package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tests.BaseTest;

public class BaseCreationTest extends BaseTest {

    // --- COMMON LOCATORS ---
    protected final By BTN_NEXT_GENERIC = io.appium.java_client.AppiumBy.xpath("//*[contains(@text, 'Selanjutnya') or contains(@text, 'Lanjut')]");
    protected final By TXT_SUCCESS_GENERIC = io.appium.java_client.AppiumBy.xpath("//*[contains(@text, 'Berhasil')]");

    // --- REUSABLE ACTIONS (ORIGINAL - DO NOT CHANGE) ---

    /**
     * Robust Submit Action.
     * 1. Waits for the button to be visible.
     * 2. Checks if enabled.
     * 3. Taps the specific X,Y coordinates provided.
     */
    protected void clickSubmitRobust(By locator, int x, int y) {
        try {
            System.out.println("   -> Attempting Robust Submit on element...");
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            
            if (btn.isEnabled()) {
                System.out.println("   -> Element enabled. Tapping coordinates: " + x + ", " + y);
                actions.tapByCoordinates(x, y);
            } else {
                System.out.println("   -> Button is disabled/not ready.");
            }
        } catch (Exception e) {
            System.out.println("   -> Robust submit interaction failed: " + e.getMessage());
        }
    }

    /**
     * Overload: Calculates center coordinates automatically.
     */
    protected void clickSubmitRobust(By locator) {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            if (btn.isEnabled()) {
                int centerX = btn.getLocation().getX() + (btn.getSize().getWidth() / 2);
                int centerY = btn.getLocation().getY() + (btn.getSize().getHeight() / 2);
                System.out.println("   -> Tapping center calculated: " + centerX + ", " + centerY);
                actions.tapByCoordinates(centerX, centerY);
            }
        } catch (Exception e) {
            System.out.println("   -> Robust submit (auto-center) failed: " + e.getMessage());
        }
    }

    protected void fillInputField(By locator, String text) {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            input.click();
            input.clear();
            input.sendKeys(text);
        } catch (Exception e) {
            System.out.println("   -> Failed to fill input: " + locator);
        }
    }

    protected void handleSuccessModal() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_SUCCESS_GENERIC));
            wait.until(ExpectedConditions.elementToBeClickable(BTN_NEXT_GENERIC)).click();
        } catch (Exception e) {
            System.out.println("   -> Success modal auto-dismissed or not found.");
        }
    }

    /**
     * Taps a point based on percentage of screen width/height.
     */
    protected void tapByPercentage(double xPct, double yPct) {
        try {
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            int pointX = (int) (size.getWidth() * xPct);
            int pointY = (int) (size.getHeight() * yPct);
            System.out.println("   -> Tapping by % (" + xPct + ", " + yPct + ") => Coordinates: " + pointX + ", " + pointY);
            actions.tapByCoordinates(pointX, pointY);
        } catch (Exception e) {
            System.out.println("   -> Failed to tap by percentage: " + e.getMessage());
        }
    }

    // --- NEW METHODS FOR CHALLENGE TEST (Added safely) ---

    /**
     * Finds text on screen, gets its position, and taps the center pixel.
     * Bypasses 'clickable=false' or layout blocking issues.
     */
    protected void tapByTextPosition(String visibleText) {
        try {
            System.out.println("   -> [Sniper] Searching for text: '" + visibleText + "'...");
            By locator = io.appium.java_client.AppiumBy.xpath("//*[contains(@text, '" + visibleText + "') or contains(@content-desc, '" + visibleText + "')]");
            
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            int centerX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
            int centerY = element.getLocation().getY() + (element.getSize().getHeight() / 2);
            
            System.out.println("   -> Found at [" + centerX + "," + centerY + "]. Tapping...");
            actions.tapByCoordinates(centerX, centerY);
        } catch (Exception e) {
            System.out.println("   -> Failed to tap text '" + visibleText + "': " + e.getMessage());
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
            actions.tapByCoordinates(centerX, centerY);
        } catch (Exception e) {
            System.out.println("   -> Failed to tap element center: " + e.getMessage());
            throw new RuntimeException("Tap failed"); // Throw so fallback can catch it
        }
    }

}