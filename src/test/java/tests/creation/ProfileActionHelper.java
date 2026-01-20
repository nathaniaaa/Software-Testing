package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ProfileActionHelper extends CreationActionHelper {

    // ========================================================================
    // 1. LOCATORS
    // ========================================================================
    private final By TAB_SAYA = AppiumBy.accessibilityId("Saya\nTab 5 of 5");
    private final By BTN_EDIT_PROFIL = AppiumBy.accessibilityId("Duration"); // Based on your code
    private final By BTN_SUCCESS_OKE = AppiumBy.xpath("//*[contains(@text, 'Oke')]");
    private final By BTN_SAVE = AppiumBy.xpath("//*[contains(@text, 'Simpan')]");
    // Specific Locator for the Date Input field specifically in Profile// The "trigger" is the box displaying the date, sibling to the Label
    private final By INPUT_DATE_DISPLAY = AppiumBy.xpath("//*[contains(@text, 'Tanggal Lahir')]/following-sibling::android.widget.Button");
    // ========================================================================
    // 2. CONSTRUCTOR
    // ========================================================================
    public ProfileActionHelper(AndroidDriver driver) {
        super(driver); // Inherits all the "Robust Submit" and "Sniper" tools
    }

    // ========================================================================
    // 3. PAGE ACTIONS
    // ========================================================================

    public void navigateToEditProfile() {
        System.out.println("Step: Navigate to Edit Page...");
        try {
            // Try standard navigation
            wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_SAYA)).click();
        } catch (Exception e) {
            // Fallback
            tapByTextPosition("Saya");
            // tapByExactText("Saya");
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_EDIT_PROFIL)).click();
        } catch (Exception e) {
            // Coordinate fallback from your original test
            tapAtScreenRatio(0.56, 0.20);
        }
    }

    public void uploadProfilePhoto(String source) {
        System.out.println("Step: Uploading Photo via " + source + "...");
        try {
            scrollToText("Upload Foto Profil");
            tapButtonByTextOrId("Upload Foto Profil", "Upload Foto Profil");
            
            // Tap "Galeri" or "Kamera" based on input
            tapButtonByTextOrId(source, source);

            if (source.equalsIgnoreCase("Galeri")) {
                Thread.sleep(3000);
                tapAtScreenRatio(0.50, 0.63); // Select Photo
                Thread.sleep(2000);
                tapAtScreenRatio(0.83, 0.91); // Crop/Done
            } else {
                // Camera Logic (Generic Coords)
                Thread.sleep(2000);
                tapAtScreenRatio(0.5, 0.85); // Shutter
                Thread.sleep(2000);
                tapAtScreenRatio(0.5, 0.90); // OK
            }

            // Handle optional popup
            try {
                Thread.sleep(1500);
                if (driver.findElements(AppiumBy.xpath("//*[contains(@text, 'Galeri')]")).size() > 0) {
                    tapAtScreenRatio(0.5, 0.2); // Dismiss
                }
            } catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("WARN: Photo upload failed.");
        }
    }
    public void selectGenderAndBloodType(String gender, String bloodType) {
        scrollToText(gender);
        tapButtonByTextOrId(gender, gender);
        
        scrollToText("Golongan Darah");
        tapByExactText(bloodType);
        // tapButtonByTextOrId(bloodType, bloodType);
    }

    public void saveAndVerify() {
        System.out.println("Step: Finishing Form...");
        scrollToText("Simpan");
        
        try { driver.hideKeyboard(); } catch (Exception ignored) {}
        
        tapButtonByTextOrId("Simpan", "Simpan");

        // Handle Success Modal
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_SUCCESS_OKE));
            tapButtonByTextOrId("Oke", "Oke");
            System.out.println("SUCCESS: Profile Updated.");
        } catch (Exception e) {
            System.out.println("WARN: Success popup didn't appear or timed out.");
        }
        
        // Ensure we are back on the main screen
        wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_EDIT_PROFIL));
    }

    // ... [Previous Navigation Methods: navigateToEditProfile, etc.] ...

    /**
     * SMART DATE PICKER STRATEGY
     * 1. Reads the current date displayed on the form (e.g., "15 Aug 1998").
     * 2. Opens the picker.
     * 3. Uses "1998" to find the Year Button and "Aug" to find the Month Button.
     * 4. Selects the new date.
     */
    public boolean updateDateOfBirthSmart(String targetYear, String targetMonth, String targetDay) {
        System.out.println("Step: Smart Date Update...");

        // 1. READ & PARSE
        String[] currentDateParts = getCurrentDateFromUi();
        String currentDay = currentDateParts[0]; // e.g., "15" or "05"
        String currentYear = currentDateParts[2];   
        String currentMonthName = getMonthNameFromNumber(currentDateParts[1]); 

        System.out.println("   -> Current UI Date: " + currentDay + " " + currentMonthName + " " + currentYear);

        // 2. OPEN PICKER
        try {
            driver.findElement(INPUT_DATE_DISPLAY).click();
        } catch (Exception e) {
            clickByLabelOffset("Tanggal Lahir");
        }
        
        // Wait for Picker
        try {
            waitForElement(AppiumBy.xpath("//*[contains(@text, '" + currentYear + "')]"));
        } catch (Exception e) {
            System.out.println("WARN: Date picker did not open!");
            return false;
        }

        // 3. CHANGE YEAR
        if (!currentYear.equals(targetYear)) {
            System.out.println("   -> Attempting to switch year to: " + targetYear);
            try {
                tapByExactText(currentYear); 
                scrollToText(targetYear, 5); 
                driver.findElement(AppiumBy.xpath("//*[contains(@text, '" + targetYear + "')]")).click();
            } catch (Exception e) {
                System.out.println("   -> [SUCCESS] Year '" + targetYear + "' was NOT found.");
                System.out.println("   -> Closing picker safely...");
                driver.navigate().back(); 
                return false; // Stop execution here if year isn't found
            }
        }

        // 4. CHANGE MONTH & DAY
        try {
            // A. Handle Month
            if (!currentMonthName.startsWith(targetMonth.substring(0, 3))) {
                tapByExactText(currentMonthName);
                scrollToText(targetMonth);
                tapByExactText(targetMonth);
            }

            // B. Handle Day (Logic Added Here)
            boolean needToClickDay = true;
            try {
                // Parse to int to handle "05" vs "5" comparison
                int curDayInt = Integer.parseInt(currentDay);
                int tgtDayInt = Integer.parseInt(targetDay);
                
                if (curDayInt == tgtDayInt) {
                    needToClickDay = false;
                }
            } catch (NumberFormatException e) {
                // Fallback: If parsing fails, do a simple string comparison
                if (currentDay.equalsIgnoreCase(targetDay)) {
                    needToClickDay = false;
                }
            }

            if (needToClickDay) {
                System.out.println("   -> Selecting Day: " + targetDay);
                tapButtonByTextOrId(targetDay, targetDay);
            } else {
                System.out.println("   -> Day '" + targetDay + "' is already selected. Skipping click.");
            }

            // 5. CONFIRM
            if (isElementPresent(AppiumBy.id("android:id/button1"), 2)) {
                 driver.findElement(AppiumBy.id("android:id/button1")).click();
            } else {
                 driver.findElement(INPUT_DATE_DISPLAY).click();
            }

        } catch (Exception e) {
            System.out.println("WARN: Failed during Month/Day selection. Closing picker.");
            driver.navigate().back();
            return false;
        }

        return true;
    }

    /**
     * Helper: Maps "08" -> "Aug", "1" -> "Jan"
     */
    private String getMonthNameFromNumber(String monthRaw) {
        // If it's already text (e.g., "Aug"), just return it
        if (monthRaw.length() > 2) return monthRaw;

        try {
            int monthNum = Integer.parseInt(monthRaw);
            String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};
            if (monthNum >= 1 && monthNum <= 12) {
                return months[monthNum - 1];
            }
        } catch (NumberFormatException e) {
            // Not a number, assume it's already text
        }
        return monthRaw;
    }

    /**
     * Helper: Safely parses "15/08/1998" into ["15", "08", "1998"]
     */
    private String[] getCurrentDateFromUi() {
        String dateText = "";
        try {
            WebElement dateEl = driver.findElement(INPUT_DATE_DISPLAY);
            dateText = dateEl.getText(); 
            if (dateText == null || dateText.isEmpty()) dateText = dateEl.getAttribute("content-desc");
        } catch (Exception e) {}

        if (dateText == null || dateText.length() < 5) return new String[]{"1", "01", "2000"};

        // Split by NON-DIGITS (Handles /, -, space, etc.)
        // "15/08/1998" -> ["15", "08", "1998"]
        String[] parts = dateText.split("[^0-9a-zA-Z]+");

        if (parts.length < 3) return new String[]{"1", "01", "2000"};

        return parts;
    }

    public boolean isOnEditProfilePage() {
    // Check for a unique element on the Edit Page (e.g., the "Edit Profil" title or Name field)
    // Using a short timeout (1-2 seconds) so it doesn't wait long if not found
        return isElementPresent(AppiumBy.xpath("//*[contains(@text, 'Edit Profil')]"), 2);
    }

    public boolean isSaveButtonEnabled() {
        try {
            scrollToText("Simpan");
            WebElement saveBtn = driver.findElement(BTN_SAVE);
            return saveBtn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tries to save the form and checks if the Success Modal appears.
     * Returns TRUE if saved successfully, FALSE if blocked.
     */
    public boolean attemptSaveAndValidateSuccess() {
        System.out.println("   -> Attempting to Click Save and verify System response...");
        
        scrollToText("Simpan");
        try { driver.hideKeyboard(); } catch (Exception ignored) {}

        // 1. Check if button is enabled first (Fast Fail)
        if (!isSaveButtonEnabled()) {
            System.out.println("   -> Save button is disabled. Blocked by UI.");
            return false;
        }

        // 2. Click Save
        try {
            driver.findElement(BTN_SAVE).click();
        } catch (Exception e) {
            tapButtonByTextOrId("Simpan", "Simpan");
        }

        // 3. Wait for Success Modal (The "Proof" of acceptance)
        try {
            // Wait up to 3 seconds for the Success/Oke popup
            wait.withTimeout(java.time.Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(BTN_SUCCESS_OKE));
            
            // If found, close it to return to dashboard
            tapButtonByTextOrId("Oke", "Oke");
            return true; 
        } catch (Exception e) {
            System.out.println("   -> Success popup did not appear. Save failed/blocked.");
            return false;
        }
    }
}