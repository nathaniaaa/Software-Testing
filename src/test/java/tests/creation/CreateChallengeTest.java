package tests.creation;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class CreateChallengeTest extends BaseCreationTest {

    // --- Locators ---
    private final By BTN_ADD_YELLOW = AppiumBy.accessibilityId("create-run");
    private final By BTN_BADGE_PLACEHOLDER = AppiumBy.className("android.widget.ImageView");
    private final By SELECT_BADGE_OPTION = AppiumBy.xpath("(//android.view.View/android.view.View/android.view.View/android.widget.ImageView)[1]"); 
    private final By BTN_PILIH_BADGE = AppiumBy.accessibilityId("Pilih");
    private final By OPTION_PESERTA_20 = AppiumBy.accessibilityId("20");
    private final By OPTION_REGIONAL = AppiumBy.accessibilityId("Regional");
    private final By OPTION_PAPUABD = AppiumBy.accessibilityId("Papua Barat Daya");
    private final By BTN_BUAT_CHALLENGE = AppiumBy.accessibilityId("Buat Challenge");

    @Test
    public void testCreateChallengeFull() {
        navigateToCreateMenu();
        System.out.println("=== START: Create Challenge (Sniper Mode) ===");

        // ============================================================
        System.out.println("Step: Uploading Poster...");
        try {

            // 2. Click "Upload Poster" (Opens Gallery directly)
            clickButtonByTextOrId("Upload Poster", "Upload Poster");

            // 3. Wait for Gallery to Open
            Thread.sleep(3000); 

            // 4. Tap Ratio for Photo (Samsung A14: 0.50, 0.63)
            // Taps the middle of the screen to pick an image
            System.out.println("   -> Selecting photo from gallery...");
            actions.tapAtScreenRatio(0.50, 0.63);
            
            Thread.sleep(2000); 

            // 5. Tap Ratio for Done/Selesai (Samsung A14: 0.83, 0.91)
            // Taps the bottom-right confirmation button
            System.out.println("   -> Confirming selection...");
            actions.tapAtScreenRatio(0.83, 0.91);

        } catch (Exception e) {
            System.out.println("WARN: Poster upload failed (Optional step). Continuing...");
        }
        
        // 1. FILL BASIC INFO
        System.out.println("Step 1: Filling Basic Info...");
        fillInputByLabelOffset("Nama Challenge", "Challenge Lari 2026");
        fillInputByLabelOffset("Jarak Lari", "10"); 
        driver.hideKeyboard();

        // 2. SET DATE (Using Sniper Click)
        System.out.println("Step 2: Setting Date...");
        
        // This finds "Pilih Tanggal", looks down 120px, and taps.
        clickByLabelOffset("Tanggal");

        // ... (Date selection logic) ...
        // 3. SET DATE (Dynamic Label Targeting)
        System.out.println("Step 3: Setting Date Range...");
        
        try {
            Thread.sleep(1000); // Wait for widget to open

            // --- CALCULATE EXPECTED LABELS ---
            // Start Date is Today (e.g., "Jan 14, 2026")
            String startLabel = getDefaultDateText(0); 
            
            // End Date is Today + 7 Days (e.g., "Jan 21, 2026")
            String endLabel = getDefaultDateText(7); 

            System.out.println("   -> Looking for tabs: '" + startLabel + "' and '" + endLabel + "'");


            // --- A. MODIFY START DATE ---
            // 1. Click the Start Tab by its TEXT
            // This is safer than index because we know exactly what it says
            clickButtonByTextOrId(startLabel, startLabel);
            
            // 2. Perform selection logic (e.g., Change year/day)
            // (Example: Change day to 15)
            clickButtonByTextOrId("15", "15");

            //everyime program set the startdate, the end date will be change into the new startdate+7 days
            // --- B. MODIFY END DATE ---
            // 1. Click the End Tab by its TEXT
            // clickButtonByTextOrId(endLabel, endLabel);
            
            // 2. Perform selection logic
            // (Example: Change day to 25)
            clickButtonByTextOrId("25", "25");


            // Save
            clickButtonByTextOrId("OK", "OK");

        } catch (Exception e) {
            System.out.println("WARN: Dynamic date selection failed: " + e.getMessage());
            // Fallback: Click Save anyway
            try { 
                clickButtonByTextOrId("OK", "OK"); } catch (Exception ex) {}
        }

        // 3. SET TIME (Using Sniper Click)
        System.out.println("Step 3: Setting Time...");
        
        // This finds "Jam (Opsional)" or "Mulai - selesai", looks down, and taps.
        clickByLabelOffset("Jam (Opsional)");
        try {
            Thread.sleep(1000); // Wait for widget

            // A. JAM MULAI
            // Example: Add 2 Hours (Increase), Add 10 Minutes (Increase)
            // ID used: "Increase hour", "Increase minute"
            setTimeWidget("Jam Mulai", 2, 10);

            // B. JAM SELESAI
            // Example: Add 5 Hours (Increase), Subtract 5 Minutes (Decrease)
            // ID used: "Increase hour", "Decrease minute"
            setTimeWidget("Jam Selesai", 5, -5);

            // Submit
             clickButtonByTextOrId("Confirm", "Confirm");

        } catch (Exception e) {
            System.out.println("WARN: Time selection failed.");
             clickButtonByTextOrId("Confirm", "Confirm");
        }

        // 4. FILL DETAILS
        System.out.println("Step 4: Filling Details...");
        actions.scrollToText("Deskripsi"); 
        fillInputByLabelOffset("Deskripsi", "Lari sehat gembira");
        
        actions.scrollToText("Syarat dan Ketentuan");
        fillInputByLabelOffset("Syarat dan Ketentuan", "Wajib sportif");
        // driver.hideKeyboard();

        // 5. BADGE & SETTINGS
        System.out.println("Step 5: Settings...");
        clickButtonByTextOrId("Badge Pemenang",  "Badge Pemenang"); 

        // SELECT THE BADGE (New Robust Logic)
        // Uses ID "10K Var1". Falls back to ratio if ID fails.
        selectBadgeRobust("10K Var1");
        
        // Using Sniper Click for Dropdowns too!
        // wait.until(ExpectedConditions.elementToBeClickable(BTN_BADGE_PLACEHOLDER)).click();
        // wait.until(ExpectedConditions.elementToBeClickable(SELECT_BADGE_OPTION)).click();
        // wait.until(ExpectedConditions.elementToBeClickable(BTN_PILIH_BADGE)).click();

        actions.scrollToText("Jumlah Peserta"); 
        
        // Click the dropdown box below "Jumlah Peserta"
        clickByLabelOffset("Jumlah Peserta"); 
        clickButtonByTextOrId("20", "20");

        clickButtonByTextOrId("Nasional", "Nasional");
        selectAreaRegionalAceh();

        // 6. SUBMIT
        System.out.println("Step 6: Submitting...");
        actions.scrollToText("Buat Challenge");
        clickSubmitRobust(BTN_BUAT_CHALLENGE);

        System.out.println("=== END: Test Success ===");
    }
    // --- HELPER METHODS ---
    private void navigateToCreateMenu() {
        if (actions.isElementPresent(BTN_ADD_YELLOW, 2)) {
             driver.findElement(BTN_ADD_YELLOW).click();
             return;
        }
        try {
            By tabLocator = AppiumBy.androidUIAutomator("new UiSelector().descriptionMatches(\"^Challenge(\\n.*|$)\")");
            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(tabLocator));
            int centerX = tab.getLocation().getX() + (tab.getSize().getWidth() / 2);
            int centerY = tab.getLocation().getY() + (tab.getSize().getHeight() / 2);
            actions.tapByCoordinates(centerX, centerY);
        } catch (Exception e) {
            tapByPercentage(0.65, 0.93);
        }
        try {
            Thread.sleep(1500);
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_ADD_YELLOW)).click();
        } catch (Exception e) {
            tapByPercentage(0.85, 0.25);
        }
    }

    private void selectAreaRegionalAceh() {
        try {
            actions.scrollToText("Atur Area Challenge");
            clickButtonByTextOrId("Regional", "Regional"); 
            wait.until(ExpectedConditions.visibilityOfElementLocated(OPTION_REGIONAL)).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(OPTION_PAPUABD)).click();
        } catch (Exception e) {
            System.out.println("   -> Area selection failed: " + e.getMessage());
            System.out.println("   -> Trying fallback to click Papua Barat Daya via text...");
            clickButtonByTextOrId("Papua Barat Daya", "Papua Barat Daya");
        }
    }

    /**
     * ACCESSIBILITY TIME SETTER:
     * Uses the exact IDs provided: "Increase hour", "Decrease minute", etc.
     * * @param tabName      "Jam Mulai" or "Jam Selesai"
     * @param hourClicks   Positive (+) to Increase, Negative (-) to Decrease
     * @param minuteClicks Positive (+) to Increase, Negative (-) to Decrease
     */
    public void setTimeWidget(String tabName, int hourClicks, int minuteClicks) {
        try {
            System.out.println("   -> [Time Widget] Switching to tab: " + tabName);
            
            // 1. Switch Tab (e.g., "Jam Mulai")
            clickButtonByTextOrId(tabName, tabName);
            Thread.sleep(500); // Animation wait

            // 2. Adjust Hours
            adjustTimeById("hour", hourClicks);

            // 3. Adjust Minutes
            adjustTimeById("minute", minuteClicks);

        } catch (Exception e) {
            System.out.println("WARN: Failed to set time for " + tabName + ": " + e.getMessage());
        }
    }

    /**
     * Helper to loop clicks on the specific arrow ID.
     * @param unit "hour" or "minute"
     * @param clicks Number of clicks (Positive = Increase, Negative = Decrease)
     */
    private void adjustTimeById(String unit, int clicks) {
        if (clicks == 0) return;

        // Determine Action: "Increase" or "Decrease"
        String action = (clicks > 0) ? "Increase" : "Decrease";
        
        // Construct ID: e.g. "Increase hour" or "Decrease minute"
        String accessibilityId = action + " " + unit;

        System.out.println("   -> Clicking '" + accessibilityId + "' " + Math.abs(clicks) + " times.");

        for (int i = 0; i < Math.abs(clicks); i++) {
            try {
                driver.findElement(AppiumBy.accessibilityId(accessibilityId)).click();
                Thread.sleep(100); // Short pause to ensure app registers the tap
            } catch (Exception e) {
                System.out.println("      -> Failed to click arrow: " + accessibilityId);
                break; // Stop loop if button missing/disabled
            }
        }
    }

    /**
     * BADGE PICKER: Tries Accessibility ID, falls back to Ratio.
     * @param badgeId The Accessibility ID of the specific badge (e.g., "10K Var1")
     */
    public void selectBadgeRobust(String badgeId) {
        System.out.println("   -> [Badge Picker] Selecting badge: " + badgeId);

        // --- STEP 1: CLICK THE BADGE ICON ---
        try {
            // Try 1: Specific Accessibility ID
            // Using a short wait because if it's not there, we want to fallback fast
            new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(badgeId)))
                .click();
            System.out.println("      -> Clicked badge via ID.");
            
        } catch (Exception e) {
            System.out.println("      -> Badge ID failed. Using Fallback Ratio (Top-Left Item).");
            
            // Try 2: Fallback Ratio for the 1st Badge in the grid
            // Based on image_d3b5ac.png, the first item is roughly at X=15%, Y=40%
            tapByPercentage(0.15, 0.40);
        }

        // --- STEP 2: CLICK "PILIH" BUTTON ---
        try {
            Thread.sleep(500); // Wait for selection highlight
            
            // Try 1: Click by Text/Desc "Pilih"
            // WebElement pilihBtn = new WebDriverWait(driver, Duration.ofSeconds(2))
            //     .until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Pilih")));
            // pilihBtn.click();
            // System.out.println("      -> Clicked 'Pilih' via ID.");
            clickButtonByTextOrId("Pilih", "Pilih");

        } catch (Exception e) {
            System.out.println("      -> 'Pilih' ID failed. Using Fallback Ratio from Bounds.");
            
            // Try 2: Fallback Ratio calculated from your bounds [67,2066][1012,2207]
            // Center X = ~540, Center Y = ~2135
            // On a standard FHD+ screen, that is roughly 0.50 (X) and 0.89 (Y)
            tapByPercentage(0.50, 0.89);
            // clickButtonByTextOrId("Pilih", "Pilih");
        }
    }
}