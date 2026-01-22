package tests.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProfileActionHelper extends CreationActionHelper {

    // LOCATORS
    private final By TAB_SAYA = AppiumBy.accessibilityId("Saya\nTab 5 of 5");
    private final By BTN_EDIT_PROFIL = AppiumBy.accessibilityId("Duration"); // Based on your code
    private final By BTN_SUCCESS_OKE = AppiumBy.xpath("//*[contains(@text, 'Oke')]");
    private final By BTN_SAVE = AppiumBy.xpath("//*[contains(@text, 'Simpan')]");
    
    // Specific Locator for the Date Input field specifically in Profile// The "trigger" is the box displaying the date, sibling to the Label
    private final By INPUT_DATE_DISPLAY = AppiumBy.xpath("//*[contains(@text, 'Tanggal Lahir')]/following-sibling::android.widget.Button");
    private final By TITLE_EDIT_PROFILE = AppiumBy.xpath("//*[contains(@text, 'Edit Profil')]");
    
    // CONSTRUCTOR
    public ProfileActionHelper(AndroidDriver driver) {
        super(driver); // Inherits all the "Robust Submit" and "Sniper" tools
    }

    // PAGE ACTIONS
    public void navigateToProfileTab() {
        System.out.println("Step: Clicking 'Saya' Tab...");
        try {
            // Try standard navigation
            wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_SAYA)).click();
        } catch (Exception e) {
            // Fallback
            tapByTextPosition("Saya");
        }
        
        // Optional: Wait for the profile page header to load
        waitForLoading(1000); 
    }

    public void enterEditMode() {
        System.out.println("Step: Clicking 'Edit Profil' Button...");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_EDIT_PROFIL)).click();
        } catch (Exception e) {
            // Coordinate fallback
            tapAtScreenRatio(0.56, 0.20);
        }
        
        // Optional: Wait for form to open
        waitForLoading(1000);
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

    public boolean updateDateOfBirth(String targetYear, String targetMonth, String targetDay) {
        System.out.println("Step: Date Update...");

        // 1. READ & PARSE
        String[] currentDateParts = getCurrentDateFromUi();
        String currentDay = currentDateParts[0]; // e.g., "15" or "05"
        String currentYear = currentDateParts[2];   
        String currentMonthName = getMonthNameFromNumber(currentDateParts[1]); 
        String targetMonthName = getMonthNameFromNumber(targetMonth); // e.g., "08" or "8"
        System.out.println("   -> Current UI Date: " + currentDay + " " + currentMonthName + " " + currentYear);

        // 2. OPEN PICKER
        try {
            driver.findElement(INPUT_DATE_DISPLAY).click();
        } catch (Exception e) {
            clickByLabelOffset("Tanggal Lahir");
        }
        
        // Wait for Picker
        try {
            waitForElement(AppiumBy.xpath("//*[contains(@text, '" + currentMonthName + "')]"));
        } catch (Exception e) {
            System.out.println("WARN: Date picker did not open!");
            return false;
        }

        // 3. CHANGE YEAR
        if (!currentYear.equals(targetYear)) {
            System.out.println("   -> Attempting to switch year to: " + targetYear);
            try {
                scrollToText(targetYear, 7); 
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
                
                try {
                    driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiSelector().text(\"" + targetYear + "\")"));
                } finally {
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                }
                tapByExactText(targetYear);   
            } catch (Exception e) {
                System.out.println("   -> [SUCCESS] Year '" + targetYear + "' was NOT found.");
                System.out.println("   -> Closing picker safely...");
                driver.navigate().back(); 
                try {    
                    driver.findElement(INPUT_DATE_DISPLAY).click();
                } catch (Exception er) {
                    clickByLabelOffset("Tanggal Lahir");
                }
                return false; // Stop execution here if year isn't found
            }
        }

        // 4. CHANGE MONTH & DAY
        try {
            // A. Handle Month
            if (!currentMonthName.startsWith(targetMonthName.substring(0, 3))) {
                tapByExactText(currentMonthName);
                scrollToText(targetMonthName);
                tapByExactText(targetMonthName);
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
                try {
                // 1. Convert input strings to integers
                int y = Integer.parseInt(targetYear);
                int m = Integer.parseInt(targetMonth); 
                int d = Integer.parseInt(targetDay);

                // 2. Generate the exact Content-Description
                // Example: "Friday, November 5th, 2010"
                String accessibilityId = generateDateContentDesc(y, m, d);
                System.out.println("   -> Generated Accessibility ID: " + accessibilityId);

                // 3. Click using Accessibility ID (Robust)
                tapByAccessibilityId(accessibilityId);
            } catch (Exception e) {
                System.out.println("   -> [Fallback] Accessibility click failed. Trying Text.");
                // Fallback to old text method if the calculation fails
                tapByExactText(targetDay);
            }
            } else {
                System.out.println("   -> Day '" + targetDay + "' is already selected. Skipping click.");
            }

            // 5. CONFIRM, CLOSE PICKER
            try {    
                driver.findElement(INPUT_DATE_DISPLAY).click();
            } catch (Exception e) {
                clickByLabelOffset("Tanggal Lahir");
            }
            System.out.println("   -> Date selection completed: " + targetDay + " " + targetMonth + " " + targetYear);

        } catch (Exception e) {
            System.out.println("WARN: Failed during Month/Day selection. Closing picker.");
            driver.navigate().back();
            return false;
        }

        return true;
    }

    private String generateDateContentDesc(int year, int month, int day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            
            // 1. Format "Friday, November 5" (Using US Locale for English names)
            // If your app is fully Indonesian (e.g., "Jumat, November..."), change Locale.US to new Locale("id", "ID")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.US);
            String basePart = date.format(formatter);
            
            // 2. Add Suffix (st, nd, rd, th)
            String suffix = getDaySuffix(day);
            
            // 3. Combine: "Friday, November 5" + "th" + ", 2010"
            return basePart + suffix + ", " + year;
        } catch (Exception e) {
            System.out.println("Date Error: " + e.getMessage());
            return "";
        }
    }

    // Helper to get st, nd, rd, th
    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    /**
     * Helper: Maps "08" -> "Aug", "1" -> "Jan"
     */
    private String getMonthNameFromNumber(String monthRaw) {
        // If it's already text (e.g., "Aug"), just return it
        if (monthRaw.length() > 2) return monthRaw;

        try {
            int monthNum = Integer.parseInt(monthRaw);
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
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
        try {
            // Set implicit wait to 0 so we don't wait 10 seconds just to say "No"
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            boolean isDisplayed = driver.findElement(TITLE_EDIT_PROFILE).isDisplayed();
            // Restore implicit wait (assuming default is 10s or 15s)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return isDisplayed;
        } catch (Exception e) {
            // Restore implicit wait here too!
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return false; // Return FALSE instead of crashing
        }
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