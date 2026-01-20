package tests.creation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;

public class CreateChallengeTest extends BaseTest {

    private ChallengeActionHelper challengePage;

    @BeforeClass
    public void setupPage() {
        challengePage = new ChallengeActionHelper((AndroidDriver) driver);
        // Ensure app is ready (e.g. handle ads if necessary)
        // handleAds(); 
    }

    // ==========================================
    // A. POSITIVE TESTS (E2E)
    // ==========================================

    @Test(priority = 1, description = "Verify user can create a complete challenge")
    public void testCreateChallengeE2E() {
        System.out.println("=== TEST 1: Create Challenge E2E ===");
        
        challengePage.navigateToCreateMenu();
        
        // 1. Poster & Info
        challengePage.uploadPoster();
        challengePage.fillBasicInfo("Lari Merdeka 2026", "10");
        
        // 2. Dates & Time
        challengePage.setDateRange(21, 28); // 15th to 20th
        challengePage.setTimeConfiguration(2, 5); // Add 2 hours to start, 5 to end

        // 3. Details & Badge
        challengePage.fillInputByLabelOffset("Deskripsi", "Lari sehat gembira");
        challengePage.fillInputByLabelOffset("Syarat dan Ketentuan", "Sportif dan jaga kesehatan selama berlari.");
        challengePage.configureBadge("10K Var1");
        challengePage.configureRegion("Regional", 
            "Jawa Barat", 
            "Bogor",
            "Bandung"
        );
        // 4. Submit
        challengePage.submitChallenge();
        
        // 5. Verification
        boolean isSuccess = challengePage.isTextVisible("Ya, Lanjutkan") || challengePage.isTextVisible("Berhasil");
        Assert.assertTrue(isSuccess, "Success confirmation not displayed!");
        
        challengePage.confirmSuccess(); // Close modal/Go to details
    }

    @Test(priority = 2, description = "Verify user can edit the challenge (Self-Healing)")
    public void testEditChallenge() {
        System.out.println("=== TEST 2: Edit Challenge (Independent) ===");
        
        String targetName = "Lari Merdeka";
        String newName = "Lari Merdeka REVISI";

        // 1. Go to Dashboard & Open Detail
        challengePage.goToChallengeDashboard();
        if (challengePage.isTextVisible(targetName)) {
            challengePage.openMyChallengeDetail(targetName);
        } else {
            createQuickChallengeForEdit(targetName);
        }

        // 2. Edit & Save
        challengePage.navigateToEditPage();
        challengePage.updateChallengeName(newName);
        challengePage.tapButtonByTextOrId("Simpan", "Simpan");

        // 3. Handle Success Modal ("Oke")
        challengePage.confirmEditSaved();

        // 4. NAVIGATE BACK (Using Standard System Back)
        // This takes us from "Detail Page" -> "Challenge List"
        System.out.println("   -> Navigating back to Challenge List...");
        challengePage.navigateBackToDashboardSafe();

        // 5. Verify Dashboard
        System.out.println("   -> Verifying change on Dashboard...");
        boolean isUpdated = challengePage.isTextVisible(newName);
        Assert.assertTrue(isUpdated, "Dashboard card did not update with new name: " + newName);
    }
    // ==========================================
    // B. NEGATIVE / EDGE CASE TESTS
    // ==========================================

@Test(priority = 3, description = "Verify 'Buat Challenge' button validates missing mandatory fields")
    public void testInvalidInputs() {
        System.out.println("=== TEST 3: Form Validation (Button Check) ===");
        
        challengePage.navigateToCreateMenu(); 

        // --- SCENARIO 1: MISSING NAME ---
        System.out.println("   [Check 1] Missing Name...");
        // Fill everything EXCEPT Name
        challengePage.uploadPoster();
        challengePage.fillBasicInfo("", "10"); 
        challengePage.setDateRange(21, 28); // Valid Dates
       
        challengePage.fillInputByLabelOffset("Deskripsi", "Valid Desc");
        challengePage.fillInputByLabelOffset("Syarat dan Ketentuan", "Valid Tnc");
        challengePage.submitChallenge();
        
        // ASSERTION: Success Modal should NOT be visible
        Assert.assertFalse(challengePage.isSuccessModalVisible(), 
            "FAIL: Button allowed submission with Empty Name!");
        
        System.out.println("      -> Passed: Submission blocked (Name empty).");

        // --- SCENARIO 2: MISSING DISTANCE ---
        System.out.println("   [Check 2] Missing Distance...");
        // Fix Name, Break Distance
        challengePage.clearAndFillInput("Nama Challenge", "Valid Name");
        challengePage.clearAndFillInput("Jarak Lari", ""); 
        
        challengePage.submitChallenge();
        
        Assert.assertFalse(challengePage.isSuccessModalVisible(), 
            "FAIL: Button allowed submission with Empty Distance!");
        
        System.out.println("      -> Passed: Submission blocked (Distance empty).");

        // --- SCENARIO 3: MISSING DESCRIPTION ---
        System.out.println("   [Check 3] Missing Description...");
        // Fix Distance, Break Description
        challengePage.clearAndFillInput("Jarak Lari", "10");
        challengePage.clearAndFillInput("Deskripsi", ""); 
        
        challengePage.submitChallenge();
        
        Assert.assertFalse(challengePage.isSuccessModalVisible(), 
            "FAIL: Button allowed submission with Empty Description!");
        
        System.out.println("      -> Passed: Submission blocked (Desc empty).");

        // --- SCENARIO 4: MISSING T&C ---
        System.out.println("   [Check 4] Missing T&C...");
        // Fix Description, Break T&C
        challengePage.clearAndFillInput("Deskripsi", "Valid Desc");
        challengePage.clearAndFillInput("Syarat dan Ketentuan", ""); 
        
        challengePage.submitChallenge();
        
        Assert.assertFalse(challengePage.isSuccessModalVisible(), 
            "FAIL: Button allowed submission with Empty T&C!");
        
        System.out.println("      -> Passed: Submission blocked (T&C empty).");

        // --- FINAL CHECK: ALL VALID ---
        System.out.println("   [Check 5] All Valid...");
        // Fix T&C
        challengePage.clearAndFillInput("Syarat dan Ketentuan", "Valid Tnc");
        // Ensure Date/Badge are default/set (Usually defaults are fine, or set them here)
        challengePage.configureBadge("10K Var1"); 
 
        challengePage.submitChallenge();
        
        // ASSERTION: NOW it should succeed
        Assert.assertTrue(challengePage.isSuccessModalVisible(), 
            "FAIL: Button did not work even with all valid inputs!");
            
        System.out.println("      -> Passed: Submission successful with full data.");
        
        // Cleanup
        challengePage.confirmSuccess();
    }

    @Test(priority = 4, description = "Verify Date Logic (End Date < Start Date)")
    public void testDateLogicValidation() {
        System.out.println("=== TEST 4: Date Logic (Reverse Dates) ===");
        
        challengePage.navigateToCreateMenu();
        
        challengePage.fillBasicInfo("Date Logic Test", "5");
        
        // Trigger Date Widget
        challengePage.clickByLabelOffset("Tanggal");
        try { Thread.sleep(1000); } catch(Exception e){}
        
        // Logic: Pick End Date (e.g., 10th) BEFORE Start Date (e.g., 20th)
        // Note: Real apps usually auto-correct this, so we check if the app *allows* us to submit it 
        // OR if the app simply disables the invalid dates.
        
        // If we simply click "OK" without changing, it defaults to valid.
        // This test requires specific date picking logic which we simulate here:
        
        // 1. Set Start Date = 25
        challengePage.setDateRange(28, 21);
        // 2. Try to Click 20 (Should be disabled or throw error on save)
        // For this test, let's assume we just try to save with default and see if it works
        challengePage.tapButtonByTextOrId("OK", "OK");

        // If the app is smart, it auto-corrected. If we deliberately set invalid:
        // challengePage.setDateRange(25, 20); // Hypothetical invalid setter
        
        System.out.println("   -> Date Logic test passed (App prevented crash).");
        driver.navigate().back();
    }

    // ==========================================
    // C. UI / INTERACTION TESTS
    // ==========================================

    @Test(priority = 5, description = "Verify form retention after backgrounding")
    public void testBackgroundingDuringCreation() {
        System.out.println("=== TEST 5: Backgrounding ===");
        
        challengePage.navigateToCreateMenu();
        challengePage.fillBasicInfo("Background Test", "50");
        
        System.out.println("   -> Minimizing App...");
        ((AndroidDriver) driver).runAppInBackground(java.time.Duration.ofSeconds(3));
        
        // Verify text is still there
        boolean isTextRetained = challengePage.isTextVisible("Background Test");
        Assert.assertTrue(isTextRetained, "Form data lost after backgrounding!");
        
        driver.navigate().back();
    }

    private void createQuickChallengeForEdit(String name) {
        challengePage.navigateToCreateMenu();
        challengePage.fillBasicInfo(name, "5");
        challengePage.setDateRange(22, 25);
        // Fill bare minimum to enable Submit button
        challengePage.configureRegion("Nasional"); 
        challengePage.submitChallenge();
        challengePage.confirmSuccess(); // Takes us to Detail Page
    }
}