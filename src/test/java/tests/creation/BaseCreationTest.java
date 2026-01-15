package tests.creation;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait; // Important for "Jan", "Feb" (English) vs "Jan", "Peb" (Indonesian)

import io.appium.java_client.AppiumBy;
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

    // public void clickButtonByTextOrId(String text, String id) {
    //     try {
    //         wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(id)));
    //         driver.findElement(AppiumBy.accessibilityId(id)).click();
    //         System.out.println("   -> Clicked ID: " + id);
    //     } catch (Exception e) {
    //         try {
    //             String xpath = String.format("//*[@text='%s']", text);
    //             driver.findElement(AppiumBy.xpath(xpath)).click();
    //             System.out.println("   -> Clicked Text: " + text);
    //         } catch (Exception ex) {
    //             System.out.println("   -> Using Text Sniper for: " + text);
    //             tapByTextPosition(text);
    //         }
    //     }
    // }
    // Add this import if missing

    public void clickButtonByTextOrId(String text, String accId) {
        actions.scrollToText(text);
        // 1. Try Accessibility ID with a FAST timeout (e.g., 3 seconds)
        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            fastWait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(accId)));
            driver.findElement(AppiumBy.accessibilityId(accId)).click();
            System.out.println("   -> Clicked via Access. ID: " + accId);
            return; // Success! Exit method.
        } catch (Exception e) {
            System.out.println("   -> Access. ID '" + accId + "' not found/clickable (waited 3s). Trying Text...");
        }

        // 2. Try Text (Use main wait here because if this fails, we really want to wait)
        try {
            // [Pro Tip] Use 'contains' for text to handle extra spaces or newlines
            String xpath = String.format("//*[contains(@text, '%s')]", text);
            wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpath)));
            driver.findElement(AppiumBy.xpath(xpath)).click();
            System.out.println("   -> Clicked via Text: " + text);
        } catch (Exception e) {
            // 3. Last Resort: Sniper Tap
            System.out.println("   -> Standard clicks failed. Engaging Sniper for: " + text);
            tapByTextPosition(text);
        }
    }

    /**
     * Calculates the default date string displayed in the app.
     * Format matches the UI: "MMM d, yyyy" (e.g., "Jan 14, 2026")
     * * @param daysToAdd 0 for Today, 7 for Next Week
     * @return The formatted date string to look for.
     */
    protected String getDefaultDateText(int daysToAdd) {
        LocalDate date = LocalDate.now().plusDays(daysToAdd);
        
        // Pattern "MMM d, yyyy" produces "Jan 14, 2026"
        // Use Locale.US for "Jan", "Feb" (or Locale("id", "ID") if app is Indonesian "Jan", "Peb")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);
        return date.format(formatter);
    }

    // /**
    //  * TIME SPINNER ADJUSTER
    //  * Uses the "Jam" and "Menit" labels as anchors to find the Up/Down arrows.
    //  * @param clicks  Number of times to tap. (+ for UP arrow, - for DOWN arrow)
    //  * @param anchorLabel The text label below the spinner ("Jam" or "Menit")
    //  */
    // public void adjustSpinner(String anchorLabel, int clicks) {
    //     if (clicks == 0) return;

    //     System.out.println("   -> [Time] Adjusting '" + anchorLabel + "' by " + clicks + " clicks.");

    //     // COORDINATE CALIBRATION (Based on Screenshot image_d1d8f2.png)
    //     // The label "Jam" is at the bottom.
    //     // Down Arrow is just above "Jam" (~120px up).
    //     // Up Arrow is above the number (~350px up).
    //     int xOffset = 0;
    //     int yOffsetUp = -350;   // Tap high for UP
    //     int yOffsetDown = -120; // Tap low for DOWN

    //     int targetY = (clicks > 0) ? yOffsetUp : yOffsetDown;
    //     int count = Math.abs(clicks);

    //     for (int i = 0; i < count; i++) {
    //         tapRelativeToLabel(anchorLabel, xOffset, targetY);
    //         // Small pause is CRITICAL for spinners to register taps
    //         try { Thread.sleep(200); } catch (Exception e) {} 
    //     }
    // }

    // /**
    //  * SNIPER RELATIVE: Finds text and taps a specific distance away from it.
    //  * Useful for hitting buttons that lack IDs but are positioned near text 
    //  * (like Up/Down arrows near "Jam" or Next Arrow near "2026").
    //  * * @param labelText  The text to find (e.g., "Jam", "Menit", "2026")
    //  * @param xOffset    Pixels to move Horizontal (+Right, -Left)
    //  * @param yOffset    Pixels to move Vertical (+Down, -Up)
    //  */
    // public void tapRelativeToLabel(String labelText, int xOffset, int yOffset) {
    //     try {
    //         // 1. Find the Label (Try both text and content-desc)
    //         WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
    //             AppiumBy.xpath("//*[contains(@text, '" + labelText + "') or contains(@content-desc, '" + labelText + "')]")
    //         ));

    //         // 2. Get Center of Label
    //         int startX = label.getLocation().getX() + (label.getSize().getWidth() / 2);
    //         int startY = label.getLocation().getY() + (label.getSize().getHeight() / 2);

    //         // 3. Apply Offsets to find the Target
    //         int targetX = startX + xOffset;
    //         int targetY = startY + yOffset;

    //         System.out.println("   -> [Sniper Relative] Anchor: '" + labelText + "'. Tapping at [" + targetX + ", " + targetY + "]");

    //         // 4. Tap the calculated target
    //         actions.tapByCoordinates(targetX, targetY);
            
    //         // Short pause to allow UI to react (crucial for repeated taps like Spinners)
    //         Thread.sleep(300);

    //     } catch (Exception e) {
    //         System.out.println("   -> Relative tap failed for '" + labelText + "': " + e.getMessage());
    //     }
    // }

    // /**
    //  * HIGH LEVEL HELPER: Sets a specific Time Tab.
    //  * @param tabName "Jam Mulai" or "Jam Selesai"
    //  * @param hourClicks How many hours to add/subtract
    //  * @param minuteClicks How many minutes to add/subtract
    //  */
    // public void setTimeWidget(String tabName, int hourClicks, int minuteClicks) {
    //     try {
    //         // 1. Switch Tab
    //         System.out.println("   -> Switching to Time Tab: " + tabName);
    //         clickButtonByTextOrId(tabName, tabName);
    //         Thread.sleep(500); // Wait for tab switch animation

    //         // 2. Adjust Hours (Anchor: "Jam")
    //         adjustSpinner("Jam", hourClicks);

    //         // 3. Adjust Minutes (Anchor: "Menit")
    //         adjustSpinner("Menit", minuteClicks);

    //     } catch (Exception e) {
    //         System.out.println("WARN: Failed to set time for " + tabName);
    //     }
    // }

    /**
     * NUCLEAR OPTION: Sniper Offset
     * Finds the Label, calculates the coordinates just below it (where the box is),
     * taps that empty space, and types blindly.
     */
    public void fillInputByLabelOffset(String labelText, String valueToType) {
        try {
            System.out.println("   -> [Sniper Offset] Targeting input below label: '" + labelText + "'");

            // 1. Find the Label (Use partial text match to be safe)
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + labelText + "') or contains(@content-desc, '" + labelText + "')]")
            ));

            // 2. Calculate Coordinates
            int labelX = label.getLocation().getX();
            int labelY = label.getLocation().getY();
            int labelHeight = label.getSize().getHeight();
            int labelWidth = label.getSize().getWidth();

            // Target X: Center of the label
            int targetX = labelX + (labelWidth / 2);
            
            // Target Y: Bottom of label + 120 pixels (adjust based on screen density, usually safe for inputs)
            // shows the box is directly below the label
            int targetY = labelY + labelHeight + 120; 

            System.out.println("   -> Label found at [" + labelY + "]. Tapping offset at [" + targetY + "]");

            // 3. Tap the "Empty Space" below the label
            actions.tapByCoordinates(targetX, targetY);

            // 4. Type Blindly (Active Element)
            // Give it a moment to focus
            Thread.sleep(500);
            
            org.openqa.selenium.interactions.Actions blindType = new org.openqa.selenium.interactions.Actions(driver);
            blindType.sendKeys(valueToType).perform();
            
            // Hide keyboard to prevent blocking next scroll
            try { driver.hideKeyboard(); } catch (Exception e) {}
            
            System.out.println("   -> Typed value: " + valueToType);

        } catch (Exception e) {
            System.out.println("   -> Sniper Offset failed for '" + labelText + "': " + e.getMessage());
        }
    }

    /**
     * SNIPER CLICK (OFFSET ONLY):
     * Finds the Label Text, calculates the coordinate BELOW it, and taps there.
     * STRICTLY VISUAL: Does NOT use Accessibility IDs or Element IDs.
     * Useful for opening Dropdowns, Date Pickers, or Time Pickers.
     */
    public void clickByLabelOffset(String labelText) {
        try {
            System.out.println("   -> [Sniper Click] Targeting box below label: '" + labelText + "'");

            // 1. Find the Label (Use partial text match to be safe)
            WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + labelText + "') or contains(@content-desc, '" + labelText + "')]")
            ));

            // 2. Calculate Coordinates
            int labelX = label.getLocation().getX();
            int labelY = label.getLocation().getY();
            int labelHeight = label.getSize().getHeight();
            int labelWidth = label.getSize().getWidth();

            // Target X: Center of the label
            int targetX = labelX + (labelWidth / 2);
            
            // Target Y: Bottom of label + 120 pixels (Standard offset to hit the box below)
            int targetY = labelY + labelHeight + 120; 

            System.out.println("   -> Label found at Y[" + labelY + "]. Tapping offset at Y[" + targetY + "]");

            // 3. Tap the "Empty Space" below the label
            actions.tapByCoordinates(targetX, targetY);
            
            // Small pause to let the UI react (e.g., Popup opening)
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("   -> Sniper Click failed for '" + labelText + "': " + e.getMessage());
        }
    }
    /**
     * Robust Input Filler: Targets the field by its Placeholder text.
     * Strategy 1: Find EditText with exact/partial text match.
     * Strategy 2 (Sniper): Tap the text coordinates, then type blindly (Active Element).
     */
    public void fillInputByPlaceholder(String placeholderText, String valueToType) {
        // 1. Try Standard Appium Interaction
        try {
            // Look for any element (EditText or View) containing the placeholder
            String xpath = String.format("//*[contains(@text, '%s') or @text='%s']", placeholderText, placeholderText);
            
            // Wait briefly
            WebElement input = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(xpath)));
            
            input.click();
            // Clear can be tricky with placeholders, sometimes it clears the placeholder itself in WebViews, 
            // but usually safe to just click and type if it's empty.
            try { input.clear(); } catch (Exception e) {} 
            input.sendKeys(valueToType);
            
            System.out.println("   -> Filled input via placeholder: '" + placeholderText + "'");
            try { driver.hideKeyboard(); } catch (Exception e) {}
            return; // Success

        } catch (Exception e) {
            System.out.println("   -> Standard placeholder fill failed. Engaging Sniper Type for: " + placeholderText);
        }

        // 2. Sniper Fallback (Tap Coordinates -> Type into Active Element)
        try {
            // A. Find the text location (even if it's not an "EditText")
            tapByTextPosition(placeholderText);
            
            // B. Wait a tiny bit for focus
            Thread.sleep(500);
            
            // C. Type into the "Currently Focused" element using Actions
            System.out.println("   -> Typing blindly into focused element...");
            org.openqa.selenium.interactions.Actions blindType = new org.openqa.selenium.interactions.Actions(driver);
            blindType.sendKeys(valueToType).perform();
            
            try { driver.hideKeyboard(); } catch (Exception e) {}

        } catch (Exception e) {
            System.out.println("   -> Sniper Type failed: " + e.getMessage());
        }
    }

}