package tests.creation;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ChallengeActionHelper extends CreationActionHelper {

    // --- LOCATORS ---
    private final By BTN_ADD_YELLOW = AppiumBy.accessibilityId("create-run");
    private final By BTN_BUAT_CHALLENGE = AppiumBy.accessibilityId("Buat Challenge");
    private final By INPUT_NAME = AppiumBy.xpath("//*[contains(@text, 'Nama Challenge')]/following-sibling::android.widget.EditText");
    
    // --- CONSTRUCTOR ---
    public ChallengeActionHelper(AndroidDriver driver) {
        super(driver);
    }

    // ==========================================
    // A. NAVIGATION & SETUP
    // ==========================================

    public void navigateToCreateMenu() {
        System.out.println("   -> Navigating to Challenge Creation...");
        
        // 1. Check if we are already on the "Create" button screen
        if (isElementPresent(BTN_ADD_YELLOW, 2)) {
            tapElementCenter(BTN_ADD_YELLOW);
            return;
        }

        // 2. If not, click "Challenge" Tab first
        try {
            By tabLocator = AppiumBy.androidUIAutomator("new UiSelector().descriptionMatches(\"^Challenge(\\n.*|$)\")");
            tapElementCenter(tabLocator);
        } catch (Exception e) {
            tapAtScreenRatio(0.65, 0.93); // Fallback: Tab Location
        }

        // 3. Click Yellow Button
        try {
            Thread.sleep(1500);
            tapElementCenter(BTN_ADD_YELLOW);
        } catch (Exception e) {
            tapAtScreenRatio(0.85, 0.25); // Fallback: Button Location
        }
    }

    public void navigateToEditPage() {
        System.out.println("   -> Navigating to Edit Page...");
        try {
            // Try finding the small "edit" icon (usually a pencil or gear)
            WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[contains(@content-desc, 'Edit') or @text='icon']")
            ));
            editBtn.click();
        } catch (Exception e) {
            // Fallback: Top-Right Corner (Samsung A14 Standard)
            tapAtScreenRatio(0.925, 0.061);
        }
    }

    // ==========================================
    // C. NEW NAVIGATION FLOW (Edit via Dashboard)
    // ==========================================

    /**
     * 1. Go to the main Challenge Tab (Dashboard)
     */
    public void goToChallengeDashboard() {
        System.out.println("   -> [Nav] Switching to Challenge Tab...");
        try {
            // Try to find the "Challenge" icon in bottom bar
            By tabLocator = AppiumBy.androidUIAutomator("new UiSelector().descriptionMatches(\"^Challenge(\\n.*|$)\")");
            tapElementCenter(tabLocator);
        } catch (Exception e) {
            // Fallback: Tap ratio for the Challenge Tab (approx 65% width, 93% height)
            tapAtScreenRatio(0.65, 0.93);
        }
        
        // Wait for the "Challenge Saya" text to appear so we know it loaded
        try {
            WebDriverWait dashboardWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            dashboardWait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Challenge Saya')]")),
                ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Public challenges')]"))
            ));
        } catch (Exception e) {
            System.out.println("   -> Warning: Dashboard slow to load.");
        }
    }

    /**
     * 2. Find and Click a Challenge Card under "Challenge Saya"
     * @param partialName The name of the challenge (e.g., "Lari Merdeka")
     */
    public void openMyChallengeDetail(String partialName) {
        System.out.println("   -> [Action] Opening Detail for: " + partialName);
        
        // Strategy A: Find the specific text (Best if you know the name)
        try {
            // Scroll just in case it's off-screen
            scrollToText("Challenge Saya"); 
            
            // Use existing helper to tap text containing the name
            tapButtonByTextOrId(partialName, partialName);
            return;
        } catch (Exception e) {
            System.out.println("      -> Specific card not found. Trying fallback...");
        }

        // Strategy B: Click the first card below "Challenge Saya" (Sniper Mode)
        // If the name is truncated (e.g. "Lari Merd...") this catches it.
        try {
            System.out.println("      -> Clicking the first card under 'Challenge Saya'...");
            
            // 1. Find the header
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, 'Challenge Saya')]")
            ));

            // 2. Calculate a click point roughly 300 pixels (or 15% screen height) below the header
            int startX = header.getLocation().getX() + 100; // Slightly indented
            int startY = header.getLocation().getY() + header.getSize().getHeight() + 250; 
            
            tapByCoordinates(startX, startY);
            
        } catch (Exception e) {
            System.out.println("WARN: Failed to click challenge card.");
        }
    }

    /**
     * Safely navigates back to the Dashboard by pressing Back repeatedly 
     * until the "Challenge Saya" header is found.
     * Handles cases where the app has "phantom" history stacks.
     */
    public void navigateBackToDashboardSafe() {
        System.out.println("   -> [Nav] Navigating back to Dashboard (Safe Mode)...");
        
        int maxAttempts = 5; // Prevent infinite loops
        int attempt = 0;

        while (attempt < maxAttempts) {
            // 1. Check if we are already on the Dashboard
            if (isTextVisible("Challenge Saya") || isTextVisible("zChallenge Lari")) {
                System.out.println("      -> Arrived at Dashboard.");
                return; // Success! Stop pressing back.
            }

            // 2. If not, press Back
            System.out.println("      -> Not on Dashboard yet. Pressing Back (" + (attempt + 1) + ")...");
            driver.navigate().back();
            
            // 3. Wait a moment for page transition
            try { Thread.sleep(1500); } catch (Exception e) {}
            
            attempt++;
        }
        
        System.out.println("WARN: Could not return to Dashboard after " + maxAttempts + " attempts.");
    }

    // ==========================================
    // B. FORM FILLING ACTIONS
    // ==========================================

    public void uploadPoster() {
        System.out.println("   -> [Action] Uploading Poster...");
        try {
            tapButtonByTextOrId("Upload Poster", "Upload Poster");
            Thread.sleep(2500); // Wait for Gallery Open
            
            // Tap Center to pick photo
            tapAtScreenRatio(0.50, 0.63);
            Thread.sleep(1500);
            
            // Tap "Done" (Bottom Right)
            tapAtScreenRatio(0.83, 0.91);
        } catch (Exception e) {
            System.out.println("WARN: Poster upload skipped (Optional).");
        }
    }

    // ... inside ChallengeActionHelper class ...

    /**
     * Specific method for Editing that ensures the previous text is cleared.
     * Uses the 'INPUT_NAME' locator defined at the top of this class.
     */
    public void updateChallengeName(String newName) {
        System.out.println("   -> [Action] Updating Name to: " + newName);
        try {
            // 1. Find the input using the robust XPath (Sibling of Label)
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_NAME));
            
            // 2. Click to focus
            input.click();
            
            // 3. CLEAR the text (The Critical Step)
            input.clear(); 
            
            // 4. Type the new name
            input.sendKeys(newName);
            
            // 5. Hide keyboard
            try { driver.hideKeyboard(); } catch (Exception ignored) {}
            
        } catch (Exception e) {
            System.out.println("WARN: Failed to clear/update name via locator. Trying fallback...");
            // Fallback: Use the Offset method but try to select all + delete manually
            fillInputByLabelOffset("Nama Challenge", newName);
        }
    }

    /**
     * Handles the "Challenge Berhasil Disimpan" modal after editing.
     * Clicks the Red "Oke" button.
     */
    public void confirmEditSaved() {
        System.out.println("   -> [Action] Confirming Edit Success...");
        try {
            // 1. Wait for the success text
            WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            modalWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Challenge Berhasil Disimpan')]")),
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Berhasil')]"))
            ));

            // 2. Click "Oke"
            tapButtonByTextOrId("Oke", "Oke");
            
            // 3. Wait for modal to disappear (Details page loads)
            Thread.sleep(1500);
            
        } catch (Exception e) {
            System.out.println("WARN: Edit Success Modal not found or auto-dismissed.");
        }
    }

    public void fillBasicInfo(String name, String distance) {
        System.out.println("   -> [Action] Filling Name: " + name + ", Dist: " + distance);
        fillInputByLabelOffset("Nama Challenge", name);
        fillInputByLabelOffset("Jarak Lari", distance);
        try { driver.hideKeyboard(); } catch (Exception ignored) {}
    }

    public void setDateRange(int startDay, int endDay) {
        System.out.println("   -> [Action] Setting Dates: " + startDay + " - " + endDay);
        clickByLabelOffset("Tanggal"); // Opens Date Widget
        
        try {
            Thread.sleep(1000);
            // 1. Pick Start Day
            scrollToExactText(String.valueOf(startDay));
            tapButtonByTextOrId(String.valueOf(startDay), String.valueOf(startDay));
            
            // 2. Pick End Day (Simplified: Just clicking OK implies single day or auto range)
            // If you need to switch tabs, add tapButtonByTextOrId("Selesai", "Selesai") here.
            scrollToExactText("OK");
            tapButtonByTextOrId("OK", "OK");
        } catch (Exception e) {
            System.out.println("WARN: Date selection failed. Attempting force close.");
            try { tapButtonByTextOrId("OK", "OK"); } catch (Exception ex) {}
        }
    }

    public void setTimeConfiguration(int startHourClicks, int endHourClicks) {
        System.out.println("   -> [Action] Setting Time...");
        clickByLabelOffset("Jam (Opsional)");
        try {
            Thread.sleep(1000);
            // Use accessibility IDs to click Up/Down arrows
            adjustTimeById("hour", startHourClicks); // Adjust Start
            
            tapButtonByTextOrId("Jam Selesai", "Jam Selesai"); // Switch Tab
            adjustTimeById("hour", endHourClicks);   // Adjust End
            
            // Confirm
            try { tapByAccessibilityId("Confirm"); } 
            catch (Exception e) { tapAtScreenRatio(0.858, 0.627); } // Ratio Fallback
            
        } catch (Exception e) {
             System.out.println("WARN: Time set failed.");
             tapButtonByTextOrId("Confirm", "Confirm");
        }
    }

    public void configureBadge(String badgeId) {
        System.out.println("   -> [Action] Configuring Badge & Region...");
        
        // 1. Badge
        scrollToText("Badge Pemenang");
        tapButtonByTextOrId("Badge Pemenang", "Badge Pemenang");
        selectBadgeRobust(badgeId);
    }

    /**
     * Generic method to configure Region.
     * Logic:
     * 1. If type is "Nasional", it selects it and stops.
     * 2. If type is "Regional", it selects it, then loops through all drillDowns locations.
     * * @param regionType  "Nasional" or "Regional"
     * @param drillDowns  (Optional) Comma-separated list of locations (Province, City, District)
     */
    public void configureRegion(String regionType, String... drillDowns) {
        System.out.println("   -> [Action] Configuring Region: " + regionType);

        try {
            // 1. Select the Main Scope (Nasional / Regional)
            // We scroll to it just in case, then tap.
            scrollToExactText("Nasional");
            tapButtonByTextOrId("Nasional", "Nasional");
            
            // Wait for UI to react
            Thread.sleep(1000); 

            // 2. Logic Check
            if (regionType.equalsIgnoreCase("Nasional")) {
                System.out.println("      -> Selected Nasional. Stopping drill-down.");
                return; // STOP HERE
            }

            // 3. If Regional, Loop through the drill-downs
            if (drillDowns != null && drillDowns.length > 0) {
                System.out.println("      -> Drilling down " + drillDowns.length + " locations...");
                tapButtonByTextOrId("Regional", "Regional"); 
                scrollToExactText("Aceh (NAD)");
                tapButtonByTextOrId("Aceh (NAD)", "Aceh (NAD)"); // Example to open first level
                   
                for (String location : drillDowns) {
                    try {
                        System.out.println("         -> Selecting: " + location);
                        
                        // A. Scroll to find the text (Critical for long lists like Provinces)
                        scrollToExactText(location);
                        
                        // B. Tap the location
                        tapButtonByTextOrId(location, location);
                        
                        // C. Wait for the next list to load (API delay)
                        Thread.sleep(1500); 
                        
                    } catch (Exception e) {
                        System.err.println("WARN: Failed to select '" + location + "'. List might not have loaded.");
                        // Optional: Break loop if one fails? 
                        // break; 
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("WARN: Region configuration failed: " + e.getMessage());
        }
    }

    public void submitChallenge() {
        System.out.println("   -> [Action] Submitting Challenge...");
        scrollToExactText("Buat Challenge");
        try {
            tapButtonByTextOrId("Buat Challenge", "Buat Challenge");
        } catch (Exception e) {
            tapAtScreenRatio(0.50, 0.888); // Fallback Button Position
        }
    }

    public void confirmSuccess() {
        System.out.println("   -> [Action] Handling Success Modal...");
        try {
            if (isTextVisible("Ya, Lanjutkan")) {
                tapButtonByTextOrId("Ya, Lanjutkan", "Ya, Lanjutkan");
            }
            if (isTextVisible("Cek Challenge")) {
                tapButtonByTextOrId("Cek Challenge", "Cek Challenge");
            }
        } catch (Exception e) {}
    }

    // --- HELPERS ---
    
    private void adjustTimeById(String unit, int clicks) {
        if (clicks == 0) return;
        String action = (clicks > 0) ? "Increase" : "Decrease";
        String id = action + " " + unit;
        for (int i = 0; i < Math.abs(clicks); i++) {
            try { driver.findElement(AppiumBy.accessibilityId(id)).click(); } catch (Exception e) { break; }
        }
    }

    private void selectBadgeRobust(String badgeId) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(badgeId))).click();
        } catch (Exception e) {
            tapAtScreenRatio(0.15, 0.40); // Click first badge
        }
        try { Thread.sleep(500); tapByExactText("Pilih"); } catch (Exception e) { tapAtScreenRatio(0.50, 0.887); }
    }

    /**
     * Checks if a text exists on the screen (visible).
     * Used for assertions and verification.
     */
    public boolean isTextVisible(String text) {
        try {
            // Use a short wait so the test doesn't hang if text is missing
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            
            // Search for any element containing the text
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + text + "')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ... inside ChallengeActionHelper ...

    /**
     * Clears an input field located by its label, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
/**
     * Scrolls to the label, clears the input field below it, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
/**
     * Scrolls to the label, clears the input field below it, then types new text.
     * Essential for Negative Testing (clearing a valid field to make it invalid).
     */
    public void clearAndFillInput(String label, String text) {
        System.out.println("   -> [Action] Clearing & Filling '" + label + "' with: '" + text + "'");
        try {
            // 1. SCROLL TO THE LABEL FIRST (The Fix)
            scrollToText(label);
            
            // 2. Find the label
            WebElement labelEl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//*[contains(@text, '" + label + "')]")
            ));
            
            // 3. Find the input box (Assuming standard vertical layout: Label -> Input)
            // Use pointer offset to tap the center of the input box
            int targetX = labelEl.getLocation().getX() + (labelEl.getSize().getWidth() / 2);
            int targetY = labelEl.getLocation().getY() + labelEl.getSize().getHeight() + 100; // Offset down
            
            tapByCoordinates(targetX, targetY);
            Thread.sleep(500);

            // 4. Clear Text (Select All + Del)
            // This is safer than .clear() for custom UI fields
            org.openqa.selenium.interactions.Actions act = new org.openqa.selenium.interactions.Actions(driver);
            act.keyDown(org.openqa.selenium.Keys.CONTROL).sendKeys("a").keyUp(org.openqa.selenium.Keys.CONTROL).perform();
            act.sendKeys(org.openqa.selenium.Keys.BACK_SPACE).perform();
            
            // 5. Type new text (if not empty)
            if (!text.isEmpty()) {
                act.sendKeys(text).perform();
            }
            
            try { driver.hideKeyboard(); } catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("WARN: Failed to clear/fill " + label);
        }
    }
    /**
     * Checks if the Success Modal ("Ya, Lanjutkan" or "Berhasil") is visible.
     * Used to verify if the button worked or was blocked.
     */
    public boolean isSuccessModalVisible() {
        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return fastWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Ya, Lanjutkan')]")),
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath("//*[contains(@text, 'Berhasil')]"))
            ));
        } catch (Exception e) {
            return false;
        }
    }
}