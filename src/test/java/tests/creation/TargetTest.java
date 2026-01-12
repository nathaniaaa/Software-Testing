package tests.creation;

import java.time.Duration;

import org.openqa.selenium.By; 
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import tests.BaseTest;

public class TargetTest extends BaseTest {

    // --- LOCATORS ---
    private final By BTN_ADD_TARGET = AppiumBy.xpath("//*[contains(@text, 'Target Pribadi')]/parent::*//android.widget.ImageView");
    private final By BTN_RESET_TARGET = AppiumBy.xpath("//*[contains(@content-desc, 'Reset') or contains(@text, 'km')]");
    
    // Modal & Form
    private final By TITLE_MODAL = AppiumBy.xpath("//*[@text='Atur Target Pribadi']");
    private final By BTN_CLOSE = AppiumBy.xpath("//android.widget.ImageView[contains(@resource-id, 'close') or contains(@resource-id, 'ivClose')]");
    private final By FIELD_INPUT_KM = AppiumBy.className("android.widget.EditText");
    private final By RADIO_HARIAN = AppiumBy.xpath("//*[contains(@text, 'Harian')]");
    private final By RADIO_MINGGUAN = AppiumBy.xpath("//*[contains(@text, 'Mingguan')]");
    private final By BTN_SUBMIT = AppiumBy.xpath("//*[contains(@text, 'Atur Target')]");
    
    // Success & Popups
    private final By BTN_NEXT = AppiumBy.xpath("//*[contains(@text, 'Selanjutnya')]");
    private final By TXT_SUCCESS = AppiumBy.xpath("//*[contains(@text, 'Berhasil')]");
    
    // Locator Popup Konfirmasi (Ya/Tidak)
    private final By POPUP_CONFIRM = AppiumBy.xpath("//*[contains(@text, 'Yakin') or contains(@text, 'Konfirmasi')]");
    private final By BTN_YA_RESET = AppiumBy.xpath("//*[contains(@text, 'Ya') or contains(@text, 'Reset')]");


    // ==========================================
    // A. FUNCTIONAL (POSITIVE)
    // ==========================================

    @Test(priority = 1, description = "1. Set Valid Target")
    public void testSetValidTarget() {
        System.out.println("=== TEST 1: Set Valid Target (5 km) ===");
        cleanUpExistingTarget(); 

        openModal();
        fillForm("5");
        clickSubmitRobust();
        
        handleSuccessModal();
        
        try {
            Thread.sleep(1000);
            String dashboardText = driver.findElement(BTN_RESET_TARGET).getText(); 
            Assert.assertTrue(dashboardText.contains("5"), "Dashboard tidak update ke 5 km");
        } catch (Exception e) {
            Assert.fail("Target 5 km tidak muncul di dashboard.");
        }
    }

    @Test(priority = 2, description = "2. Update Target (Condition: 0 Progress vs Progress)")
    public void testUpdateTarget() {
        System.out.println("=== TEST 2: Update Target (10 km) ===");

        // 1. Klik Reset (Target Lama)
        try {
            wait.until(ExpectedConditions.elementToBeClickable(BTN_RESET_TARGET)).click();
            System.out.println("   -> Clicked Reset Button.");
        } catch (Exception e) {
            System.out.println("   -> Reset button not found (Mungkin sudah bersih).");
        }

        // 2. CONDITIONAL POPUP HANDLING (Logic yang Diperbaiki)
        // Kita gunakan WebDriverWait khusus untuk popup, bukan implicit wait
        try {
            System.out.println("   -> Checking for Popup...");
            // Tunggu maksimal 3 detik untuk tombol "Ya" / "Reset"
            WebElement btnYa = new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(BTN_YA_RESET));
            
            // Jika baris di atas tidak error, berarti Popup Muncul
            btnYa.click();
            System.out.println("   -> Popup Detected: Clicked 'Ya'.");
            
        } catch (Exception e) {
            // Jika Timeout (3 detik lewat gak ada popup), berarti Progress = 0 (Reset langsung)
            System.out.println("   -> No Popup detected (Assume 0 Progress/Reset done).");
        }

        // 3. VALIDASI PENTING: Pastikan Reset Selesai
        // Jangan lanjut sebelum tombol (+) benar-benar muncul!
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_ADD_TARGET));
            System.out.println("   -> Target lama hilang, tombol (+) sudah muncul.");
        } catch (Exception e) {
            // Kalau (+) gak muncul juga, kita coba paksa klik Reset lagi (Retry logic)
            // Siapa tau klik pertama tadi gak masuk (miss-click)
            System.out.println("   -> Retry: Klik reset lagi karena (+) belum muncul...");
            actions.tapByCoordinates(500, 500); // Tap tengah widget target (sesuaikan koordinat widget reset kamu)
            try { 
                 Thread.sleep(1000);
                 driver.findElement(BTN_YA_RESET).click(); 
            } catch(Exception ex){}
        }

        // 4. Create New Target (Value 10)
        openModal(); 
        
        fillForm("10");
        clickSubmitRobust();
        handleSuccessModal();

        // 5. Verify Update
        try {
            Thread.sleep(1000);
            String dashboardText = driver.findElement(BTN_RESET_TARGET).getText();
            Assert.assertTrue(dashboardText.contains("10"), "Dashboard tidak update ke 10 km");
            System.out.println("   -> Success: Target updated to 10km.");
        } catch (Exception e) { Assert.fail("Target 10 km gagal update."); }
    }

    // ==========================================
    // B. EXTREME & EDGE CASES (NEGATIVE)
    // ==========================================

    @Test(priority = 3, description = "3, 4, 5. Invalid Numeric Values")
    public void testInvalidNumericValues() {
        System.out.println("=== TEST 3: Invalid Numeric Values ===");
        
        cleanUpExistingTarget();
        openModal();

        WebElement submitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_SUBMIT));

        // Case 3: Zero ("0")
        fillForm("0");
        if (submitBtn.isEnabled()) {
            clickSubmitRobust();
            Assert.assertTrue(driver.findElements(TITLE_MODAL).size() > 0, "Seharusnya tidak bisa submit 0!");
        }

        // Case 4: Negative ("-100")
        fillForm("-100");
        if(driver.findElement(FIELD_INPUT_KM).getText().contains("-")) {
             Assert.assertFalse(submitBtn.isEnabled(), "Submit harus mati jika negatif.");
        }

        // Case 5: Decimal ("5.5")
        fillForm("5.5");
        if(driver.findElement(FIELD_INPUT_KM).getText().contains(".")) {
             Assert.assertFalse(submitBtn.isEnabled(), "Submit harus mati jika desimal.");
        }
        
        driver.findElement(BTN_CLOSE).click();
    }

@Test(priority = 4, description = "6, 7. Max Limit & Chars")
    public void testMaxLimitAndChars() {
        System.out.println("=== TEST 4: Max Limit & Chars ===");
        
        // Pastikan modal terbuka
        if (driver.findElements(TITLE_MODAL).isEmpty()) openModal();

        // --- Case 6: Max Int ("999999999") ---
        System.out.println("   [Check] Testing Max Int...");
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
        input.click();
        input.clear();
        input.sendKeys("999999999");
        
        // Hide Keyboard
        try { driver.findElement(TITLE_MODAL).click(); } catch (Exception e) {}
        
        // Action: Coba Submit
        clickSubmitRobust();
        
        // Validasi: App tidak boleh crash & Modal harus tetap terbuka (atau tertutup jika success, tergantung requirement)
        // Asumsi: Angka sebesar ini mungkin diterima atau ditolak. Yang penting app tidak crash.
        Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), "App Crash atau Modal tertutup tidak terduga!");
        System.out.println("   -> Max Int check passed (No Crash).");

        // --- Case 7: Special Characters ("@#$ABCD") ---
        System.out.println("   [Check] Testing Special Chars...");
        input.click();
        input.clear();
        input.sendKeys("@#$ABCD");
        
        try { driver.findElement(TITLE_MODAL).click(); } catch (Exception e) {}
        
        // Action: Paksa Submit
        clickSubmitRobust();
        
        // Validasi: Modal HARUS tetap terbuka (Reject)
        try {
            Thread.sleep(500);
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem menerima karakter spesial.");
            System.out.println("   -> Special Char check passed (Rejected).");
        } catch (Exception e) {
            Assert.fail("Modal tertutup/Crash saat input karakter aneh.");
        }
        
        // Cleanup dilakukan oleh @AfterMethod
    }

    @Test(priority = 5, description = "8. Empty Field")
    public void testEmptyField() {
        System.out.println("=== TEST 5: Empty Field ===");
        
        if (driver.findElements(TITLE_MODAL).isEmpty()) openModal();
        
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
        input.click();
        
        // --- Smart Clear (Seperti sebelumnya) ---
        input.sendKeys("1"); 
        try { Thread.sleep(200); } catch(Exception e){}
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.DEL));
        
        // Focus Shuffle
        try { driver.findElement(TITLE_MODAL).click(); } catch(Exception e) {}
        
        // --- LOGIC BARU: ACTION BASED ---
        System.out.println("   [Check] Clicking Submit on Empty Field...");
        clickSubmitRobust();
        
        // Validasi: Modal HARUS tetap terbuka
        // Jika tertutup, berarti bug (sistem membuat target kosong/0)
        try {
            Thread.sleep(1000);
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed(), 
                "FAIL: Modal tertutup! Sistem mengizinkan target kosong.");
            System.out.println("   -> Empty check passed (System rejected empty submission).");
        } catch (Exception e) {
            Assert.fail("Modal tertutup saat field kosong (Bug Critical).");
        }
        
        // Cleanup dilakukan oleh @AfterMethod
    }
    
    // ==========================================
    // C. UI / INTERACTION
    // ==========================================

    @Test(priority = 6, description = "9. Cancel/Dismiss")
    public void testCancelDismiss() {
        System.out.println("=== TEST 6: Cancel/Dismiss ===");
        
        // Setup clean state
        cleanUpExistingTarget();
        openModal();
        fillForm("5");
        clickSubmitRobust();
        handleSuccessModal();

        // Try to change but Cancel
        try { driver.findElement(BTN_RESET_TARGET).click(); } catch(Exception e) {}
        // Handle popup if exists (Conditional cleanup)
        try { 
             driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
             if(driver.findElements(POPUP_CONFIRM).size() > 0) driver.findElement(BTN_YA_RESET).click();
        } catch(Exception e) {} finally { driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20)); }
        
        openModal();
        fillForm("100"); // Type 100
        driver.findElement(BTN_CLOSE).click(); // Click Close
        
        // Verify dashboard NOT 100
        try {
            Thread.sleep(1000);
            if(driver.findElements(BTN_RESET_TARGET).size() > 0) {
                String text = driver.findElement(BTN_RESET_TARGET).getText();
                Assert.assertFalse(text.contains("100"), "Value 100 tersimpan padahal di-cancel!");
            }
        } catch (Exception e) {}
    }

    @Test(priority = 7, description = "10. Backgrounding")
    public void testBackgrounding() {
        System.out.println("=== TEST 7: Backgrounding ===");
        openModal();
        fillForm("50");

        ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(3));
        
        try {
            Thread.sleep(1000);
            Assert.assertTrue(driver.findElement(TITLE_MODAL).isDisplayed());
        } catch (Exception e) {
            Assert.fail("Modal tertutup setelah backgrounding.");
        }
        driver.findElement(BTN_CLOSE).click();
    }


    // --- HELPERS ---

    public void clickSubmitRobust() {
        try {
            System.out.println("   -> Attempting Robust Submit...");
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_SUBMIT));
            if (btn.isEnabled()) {
                // Hardcoded Coordinate (540, 2120 based on inspector)
                actions.tapByCoordinates(540, 2120);
            }
        } catch (Exception e) { System.out.println("   -> Button interaction failed."); }
    }

    public void fillForm(String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_INPUT_KM));
        input.click();
        input.clear();
        input.sendKeys(value);
        driver.findElement(RADIO_MINGGUAN).click(); // Focus Shuffle
        try { Thread.sleep(500); } catch (Exception e) {}
    }

    public void openModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(BTN_ADD_TARGET)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_MODAL));
        } catch (Exception e) {
            actions.tapByCoordinates(900, 450); // Fallback coordinate for (+) button
        }
    }

    public void cleanUpExistingTarget() {
        System.out.println("   -> Checking for existing target to clean up...");
        try {
            // 1. Cek apakah tombol Reset ada (artinya target aktif)
            // Gunakan findElements agar tidak error/throw exception jika tidak ada
            if (driver.findElements(BTN_RESET_TARGET).size() > 0) {
                System.out.println("   -> Target found. Resetting...");
                driver.findElement(BTN_RESET_TARGET).click();
                
                // 2. Handle Popup Konfirmasi (Jika ada progress)
                try {
                    // Tunggu sebentar siapa tau popup muncul
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
                    if (driver.findElements(BTN_YA_RESET).size() > 0) {
                        driver.findElement(BTN_YA_RESET).click();
                        System.out.println("   -> Confirmed reset (Popup handled).");
                    }
                } catch (Exception e) {
                    // Ignore jika tidak ada popup
                } finally {
                    // Balikin timeout ke normal
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                }

                // 3. WAJIB: Tunggu sampai tombol (+) muncul kembali
                // Ini memastikan dashboard sudah bersih sebelum lanjut test
                wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_ADD_TARGET));
                System.out.println("   -> Dashboard clean. Ready for Test 1.");
            } else {
                System.out.println("   -> No existing target found. Good to go.");
            }
        } catch (Exception e) {
            System.out.println("   -> Warning: Cleanup failed or stuck. Trying to continue...");
        }
    }

    public void handleSuccessModal() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(TXT_SUCCESS));
            driver.findElement(BTN_NEXT).click();
        } catch (Exception e) {}
    }
}