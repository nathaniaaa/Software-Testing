package tests.creation.negative;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.Assert;
import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;
import tests.creation.ChallengeActionHelper;
import tests.utils.TestInfo;
import tests.utils.TestListener;
import tests.utils.data.ChallengeData;


public class ChallengeTest extends BaseTest {

    private ChallengeActionHelper challengePage;

    @BeforeClass
    public void setupPage() {
        challengePage = new ChallengeActionHelper((AndroidDriver) driver);
        // Ensure app is ready (e.g. handle ads if necessary)
        // handleAds(); 
    }

    @Test(priority = 1, description = "Verifikasi pesan error muncul jika banner challenge > 5MB")
    public void testCreateChallengeBannerExceeds5MB() {
        System.out.println("=== TEST 12: Upload Banner Challenge > 5MB ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu();
        
        // 2. Klik Area Upload Foto Awal (Sesuaikan dengan locator tombol + upload Anda)
        // challengePage.tap(challengePage.BTN_UPLOAD_AREA, "Klik area Upload Banner");

        // 3. Jalankan Alur UI Baru
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengunggah gambar berukuran > 5MB via UI Koleksi...");
        }
        challengePage.uploadPoster(3);

        // 4. Verifikasi Pesan Error Muncul
        boolean isErrorVisible = challengePage.isImageSizeErrorDisplayed();

        if (isErrorVisible) {
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS (Negative Case): Pesan error maksimal 5MB berhasil ditampilkan.");
            }
        } else {
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("FAILURE: Sistem tidak menampilkan pesan error saat gambar > 5MB diunggah.");
            }
        }

        // Hard Assert
        Assert.assertTrue(isErrorVisible, "Gagal memvalidasi penolakan gambar dengan ukuran > 5MB!");

        // 5. Cleanup
        challengePage.navigateBackToDashboardSafe();
    }

    @Test(priority = 2, description = "Verify Challenge can be created using the default badge")
    @TestInfo(expected = "Challenge created successfully with the default badge", group = "Challenge")
    public void testCreateChallengeDefaultBadge() {
        System.out.println("=== TEST 10: Create Challenge Default Badge ===");

        // 1. Navigate
        challengePage.navigateToCreateMenu();
        
        // 2. Prepare Data (Badge parameter is empty)
        String uniqueName = "Lari Default " + (System.currentTimeMillis() % 10000);
        ChallengeData testData = new ChallengeData(
            uniqueName, 
            "5", 
            "Lari santai dengan badge bawaan sistem", 
            "Jaga kesehatan dan selalu terhidrasi.", 
            "",   
            true  // Private Mode
        );

        // 3. ACTION: Fill the entire form in one line
        challengePage.fillCreateChallengeForm(testData);

        // 5. Submit & Confirm
        boolean isCreated = challengePage.submitAndConfirm();

        // 6. Assertions
        if (isCreated) {
            TestListener.getTest().pass("SUCCESS: Challenge '" + uniqueName + "' created with default badge.");
            
            // Validate we are on the Detail Page
            Assert.assertTrue(challengePage.isTextVisible(uniqueName), "Detail page title mismatch");
        } else {
            TestListener.getTest().fail("FAILURE: Submit flow failed for default badge.");
        }

        Assert.assertTrue(isCreated, "Challenge Creation Failed!");

        challengePage.scrollAndCapture(1, 0.8, 0.2, "Screeshot setelah submit Challenge dengan default badge");
        challengePage.highlightBadgeResult(uniqueName);
        // 7. Cleanup
        challengePage.navigateBackToDashboardSafe();
    }

    @Test(priority = 3, description = "Verifikasi tombol Buat Challenge disabled jika field wajib kosong")
    @TestInfo(
        expected = "Tombol submit dalam kondisi disable dan proses pembuatan tidak dapat dilanjutkan", 
        group = "Challenge - Negative Cases",
        testType = "Negative Cases"
    )
    public void testSubmitButtonDisabledOnEmptyFields() {
        System.out.println("=== TEST 11: Tombol Submit Disabled (Field Wajib Kosong) ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu();
        
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("1. Berada di form Buat Challenge");
        }

        // 2. Siapkan Data Tidak Lengkap (Field Wajib Kosong)
        // Sengaja mengosongkan Name dan Distance
        ChallengeData incompleteData = new ChallengeData(
            "", 
            "", 
            "Deskripsi tetap diisi untuk test", 
            "Syarat juga diisi", 
            "10K Var1", 
            true
        );

        // 3. Isi form dengan data tidak lengkap
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengisi form tetapi mengosongkan Nama dan Jarak...");
        }
        challengePage.fillCreateChallengeForm(incompleteData);

        // 4. Verifikasi Status Tombol
        // Scroll ke bawah agar tombol Simpan / Buat Challenge terlihat di layar
        challengePage.scrollToText("Simpan"); // atau "Buat Challenge" sesuai teks di aplikasi Anda
        
        boolean isButtonActive = challengePage.isSubmitButtonEnabled();

        // 5. Verifikasi dan Assert Report
        // Karena ini Negative Test, ekspektasi kita adalah isButtonActive bernilai FALSE
        if (!isButtonActive) {
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS (Negative Case): Tombol Submit terdeteksi DISABLED karena field wajib kosong.");
            }
        } else {
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("FAILURE: Tombol Submit tetap ENABLED meskipun field wajib kosong! (Bug Potensial)");
            }
        }

        // Hard Assert: Test lulus jika tombol TIDAK aktif (false)
        Assert.assertFalse(isButtonActive, "Gagal! Tombol submit masih bisa diklik padahal data wajib kosong!");

        // 6. Cleanup
        challengePage.navigateBackToDashboardSafe();
    }

    @Test(priority = 4, description = "Verifikasi pesan error muncul jika rentang waktu Challenge tidak valid")
    @TestInfo(
        expected = "Sistem menolak input dan menampilkan pesan error penanda kesalahan rentang waktu", 
        group = "Challenge - Negative Cases",
        testType = "Negative Cases"
    )
    public void testCreateChallengeInvalidTimeRange() {
        System.out.println("=== TEST 6: Rentang Waktu Tidak Valid ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu();
        
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("1. Berada di form Buat Challenge");
        }

        // 3. SKENARIO ERROR: Masukkan rentang waktu terbalik
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengisi Tanggal/Waktu Selesai lebih awal dari Waktu Mulai...");
        }

        challengePage.scrollToText("Jam (Opsional)");
        challengePage.setTimeConfiguration(5, 2); 

         boolean isErrorVisible = challengePage.isTimeErrorDisplayed();

        if (isErrorVisible) {
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS (Negative Case): Pesan error rentang waktu berhasil ditampilkan sesuai ekspektasi.");
            }
        } else {
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("FAILURE: Sistem tidak menampilkan pesan error saat rentang waktu tidak valid.");
            }
        }

        // Hard Assert: Test ini LULUS (Passed) jika error BENAR-BENAR MUNCUL
        Assert.assertTrue(isErrorVisible, "Gagal memvalidasi error rentang waktu tidak valid!");

        // 6. Cleanup
        challengePage.navigateBackToDashboardSafe();
        
    }

    @Test(priority = 7, description = "Verifikasi admin dapat mengeluarkan (Kick Out) peserta dari challenge")
    public void testKickOutPeserta() {
        System.out.println("=== TEST 7: Kick Out Peserta Challenge ===");

        try {
            // (Opsional) Sesuaikan nama challenge target Anda
            challengePage.navigateToDetailChallenge("Lari Private");
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi awal jumlah peserta sebelum di-kick out");
            
            // Jalankan alur Kick Out
            challengePage.kickOutParticipant("Lari Private");
            
            driver.navigate().back(); // Kembali ke halaman Detail Challenge
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private");
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah peserta di-kick out");
            
            if (TestListener.getTest() != null) {
                logPass("SUCCESS: Alur Kick Out peserta berjalan lancar.");
            }
        } catch (Exception e) {
            if (TestListener.getTest() != null) logFail("FAILURE: Terjadi kesalahan saat memproses Kick Out peserta.");
            Assert.fail("Alur Kick Out Peserta gagal!");
        }

        challengePage.navigateBackToDashboardSafe();
    }

    @Test(priority = 8, description = "Verifikasi admin dapat menolak peserta secara satuan dan massal")
    public void testRejectPeserta() {
        System.out.println("=== TEST 7: Tolak Peserta Challenge ===");

        try {
            // (Opsional) Ganti nama challenge jika Anda menggunakan challenge test yang berbeda
            challengePage.navigateToDetailChallenge("Lari Private");
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi awal jumlah peserta sebelum ditolak");
            
            // Jalankan alur tolak 1 peserta
            challengePage.rejectOneParticipant("Lari Private");
            
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menolak 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private");
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menolak 1 peserta");
            
            challengePage.rejectAllParticipants("Lari Private");
            
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menolak semua peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private");
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menolak semua peserta");
            
            if (TestListener.getTest() != null) {
                logPass("SUCCESS: Alur penolakan peserta berjalan lancar (Satuan & Massal).");
            }
        } catch (Exception e) {
            if (TestListener.getTest() != null) logFail("FAILURE: Terjadi kesalahan saat memproses penolakan peserta.");
            Assert.fail("Alur Reject Peserta gagal!");
        }

        challengePage.navigateBackToDashboardSafe();
    }
}