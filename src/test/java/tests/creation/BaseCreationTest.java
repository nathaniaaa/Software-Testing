package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tests.BaseTest;

public class BaseCreationTest extends BaseTest {

    // --- COMMON LOCATORS ---
    protected final By BTN_NEXT_GENERIC = io.appium.java_client.AppiumBy.xpath("//*[contains(@text, 'Selanjutnya') or contains(@text, 'Lanjut')]");
    protected final By TXT_SUCCESS_GENERIC = io.appium.java_client.AppiumBy.xpath("//*[contains(@text, 'Berhasil')]");

    // --- REUSABLE ACTIONS ---

    /**
     * Robust Submit Action.
     * 1. Waits for the button to be visible.
     * 2. Checks if enabled.
     * 3. Taps the specific X,Y coordinates provided (to bypass overlap/interception issues).
     */
    protected void clickSubmitRobust(By locator, int x, int y) {
        try {
            System.out.println("   -> Attempting Robust Submit on element...");
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            
            if (btn.isEnabled()) {
                System.out.println("   -> Element enabled. Tapping coordinates: " + x + ", " + y);
                // Use the ActionHelper inherited from BaseTest
                actions.tapByCoordinates(x, y);
            } else {
                System.out.println("   -> Button is disabled/not ready.");
            }
        } catch (Exception e) {
            System.out.println("   -> Robust submit interaction failed: " + e.getMessage());
        }
    }

    /**
     * Overload: Calculates center coordinates automatically if you don't want to hardcode X/Y.
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
            // try { driver.hideKeyboard(); } catch (Exception e) {}
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
     * @param xPct Percentage of width (0.0 to 1.0) Example: 0.92 for 92%
     * @param yPct Percentage of height (0.0 to 1.0) Example: 0.50 for 50%
     */
    protected void tapByPercentage(double xPct, double yPct) {
        try {
            // Get current screen dimensions
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            
            // Calculate coordinates
            int pointX = (int) (size.getWidth() * xPct);
            int pointY = (int) (size.getHeight() * yPct);
            
            System.out.println("   -> Tapping by % (" + xPct + ", " + yPct + ") => Coordinates: " + pointX + ", " + pointY);
            
            // Perform Tap
            actions.tapByCoordinates(pointX, pointY);
            
        } catch (Exception e) {
            System.out.println("   -> Failed to tap by percentage: " + e.getMessage());
        }
    }
}