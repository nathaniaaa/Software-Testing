package tests.creation.negative.challenge;

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
    }

    @Test(priority = 1, description = "Buat Challenge Lari - Upload Banner challenge dengan file diatas 5MB")
    @TestInfo(
        expected = "Saat membuat Challenge Lari, jika banner yang diunggah berukuran lebih dari 5MB, proses upload gagal dan muncul pesan error sebagai penanda kesalahan", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Negative Case",
        note = ""
    )
    public void testCreateChallengeBannerExceeds5MB() {
        System.out.println("=== TEST 12: Upload Banner Challenge > 5MB ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu(false);
        
        // 2. Klik Area Upload Foto Awal (Sesuaikan dengan locator tombol + upload Anda)
        // challengePage.tap(challengePage.BTN_UPLOAD_AREA, "Klik area Upload Banner");

        // 3. Jalankan Alur UI Baru
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengunggah gambar berukuran > 5MB via UI Koleksi...");
        }
        challengePage.uploadPoster(3, true);

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

    @Test(priority = 2, description = "Buat Challenge Lari - Tekan tombol (+) kemudian buat challenge lari dengan kondisi ada field informasi challenge yang kosong")
    @TestInfo(
        expected = "Saat membuat Challenge Lari dengan menekan tombol (+), jika terdapat field informasi wajib yang masih kosong, maka tombol \"Buat Challenge\" akan dalam kondisi disable dan proses pembuatan challenge tidak dapat dilanjutkan.", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Negative Case",
        note = ""
    )
    public void testSubmitButtonDisabledOnEmptyFields() {
        System.out.println("=== TEST 2: Tombol Submit Disabled (Field Wajib Kosong) ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu(false);
        
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("1. Berada di form Buat Challenge");
        }

        // 2. Siapkan Data Tidak Lengkap (Field Wajib Kosong)
        // Sengaja mengosongkan Name dan Distance
        ChallengeData incompleteData = new ChallengeData(
            1,
            "", 
            "", 
            "Deskripsi tetap diisi untuk test", 
            "", 
            "10K Var1", 
            true
        );

        // 3. Isi form dengan data tidak lengkap
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengisi form tetapi mengosongkan Nama dan Jarak...");
        }
        challengePage.fillCreateChallengeForm(incompleteData, false);

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

    @Test(priority = 3, description = "Buat Challenge Lari - Pengguna mengatur rentang waktu yang tidak valid")
    @TestInfo(
        expected = "Saat membuat Challenge Lari, jika rentang waktu yang dipilih tidak valid atau salah, maka akan ada pesan error sebagai penanda kesalahan", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Negative Case",
        note = ""
    )
    public void testCreateChallengeInvalidTimeRange() {
        System.out.println("=== TEST 3: Rentang Waktu Tidak Valid ===");

        // 1. Navigasi ke halaman Buat Challenge
        challengePage.navigateToCreateMenu(false);
        
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("1. Berada di form Buat Challenge");
        }

        // 3. SKENARIO ERROR: Masukkan rentang waktu terbalik
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Action: Mengisi Tanggal/Waktu Selesai lebih awal dari Waktu Mulai...");
        }

        challengePage.scrollToText("Jam (Opsional)");
        challengePage.setTimeConfiguration(5, 2, true); 

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

    @Test(priority = 4, description = "Buat Challenge Lari - Tidak memilih badge untuk challenge lari atau badge tetap dalam keadaan kosong")
    @TestInfo(
        expected = "Challenge akan tetap bisa dibuat dengan menggunakan badge default", 
        group = "SET & LIHAT CHALLENGE", 
        testType = "Negative Case",
        note = ""
    )
    public void testCreateChallengeDefaultBadge() {
        System.out.println("=== TEST 4: Create Challenge Default Badge ===");

        // 1. Navigate
        challengePage.navigateToCreateMenu(false);
        
        // 2. Prepare Data (Badge parameter is empty)
        String uniqueName = "Lari Default " + (System.currentTimeMillis() % 10000);
        ChallengeData testData = new ChallengeData(
            1,
            uniqueName, 
            "5", 
            "Lari santai dengan badge bawaan sistem", 
            "Jaga kesehatan dan selalu terhidrasi.", 
            "",   
            true  // Private Mode
        );

        // 3. ACTION: Fill the entire form in one line
        challengePage.fillCreateChallengeForm(testData, false);

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

    @Test(priority = 5, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge menolak permintaan bergabung peserta baru ke challengenya")
    @TestInfo(
        expected = "Peserta yang ditolak oleh admin challenge akan menghilang dari daftar persetujuan kelola challenge, dan tidak akan masuk ke daftar peserta dan tidak bisa mengikuti challenge yang diadakan oleh admin", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Negative Case",
        note = ""
    )
    public void testRejectPeserta() {
        System.out.println("=== TEST 5: Tolak Peserta Challenge ===");

        try {
            // (Opsional) Ganti nama challenge jika Anda menggunakan challenge test yang berbeda
            challengePage.navigateToDetailChallenge("Lari Private Reject", false);

            // Jalankan alur tolak 1 peserta
            challengePage.rejectOneParticipant("Lari Private Reject", true);
            
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menolak 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Reject", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menolak 1 peserta");
            
            challengePage.rejectAllParticipants("Lari Private Reject", true);
            
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menolak semua peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Reject", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menolak semua peserta");
            
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS: Alur penolakan peserta berjalan lancar (Satuan & Massal).");
            }
        } catch (Exception e) {
            if (TestListener.getTest() != null) logFail("FAILURE: Terjadi kesalahan saat memproses penolakan peserta.");
            Assert.fail("Alur Reject Peserta gagal!");
        }

        challengePage.navigateBackToDashboardSafe();
    }

    @Test(priority = 6, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge mengeluarkan peserta yang ada di challengenya")
    @TestInfo(
        expected = "Peserta yang dikeluarkan oleh admin akan menghilang dari daftar peserta di challenge tersebut, dan sudah tidak bisa mengikuti challenge yang diadakan oleh admin", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Negative Case",
        note = ""
    )
    public void testKickOutPeserta() {
        System.out.println("=== TEST 6: Kick Out Peserta Challenge ===");

        try {
            // (Opsional) Sesuaikan nama challenge target Anda
            challengePage.navigateToDetailChallenge("Lari Private Accept", false);

            // Jalankan alur Kick Out
            challengePage.kickOutParticipant("Lari Private Accept", true);
            
            driver.navigate().back(); // Kembali ke halaman Detail Challenge
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah peserta di-kick out");
            
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS: Alur Kick Out peserta berjalan lancar.");
            }
        } catch (Exception e) {
            if (TestListener.getTest() != null) TestListener.getTest().fail("FAILURE: Terjadi kesalahan saat memproses Kick Out peserta.");
            Assert.fail("Alur Kick Out Peserta gagal!");
        }

        challengePage.navigateBackToDashboardSafe();
    }

}