package tests.creation.positive;

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

    // @Test(priority = 1, description = "Pengguna membuka halaman Challenge dan membuat Private Challenge")
    // @TestInfo(
    //     expected = "Pengguna dapat membuka formulir untuk membuat Private Challenge. Setelah seluruh informasi diisi dengan benar dan lengkap, sistem akan memproses dan membuat Private Challenge tersebut atas nama pengguna.\n" + //
    //                     "\n" + //
    //                     "Untuk membuat challenge sebagai private, bisa dengan mengaktifkan Toggle : Atur Sebagai Mode Private", 
    //     group = "Challenge", 
    //     testType = "Positive Case"
    // )
    // public void testCreateChallengePrivate() {
    //     System.out.println("=== TEST 1: Create Challenge Private ===");

    //     // 1. Navigate
    //     challengePage.navigateToCreateMenu(true);
        
    //     // 2. Prepare Data
    //     String uniqueName = "Lari Privat " + (System.currentTimeMillis() % 10000);
    //     String description = "Tantangan lari privat 10K khusus komunitas. Uji daya tahan Anda, selesaikan total jarak 10 kilometer sebelum waktu habis, dan raih badge eksklusif di garis finish. Tetap konsisten dan jaga kebugaran tubuh Anda!";
    //     String terms = "1. Peserta wajib menyelesaikan total jarak 10km.\n" +
    //                             "2. Aktivitas lari dapat dicicil maupun diselesaikan sekaligus.\n" +
    //                             "3. Bukti aktivitas wajib terekam oleh GPS aplikasi.\n" +
    //                             "4. Dilarang keras melakukan kecurangan (menggunakan kendaraan roda dua/empat).\n" +
    //                             "5. Utamakan keselamatan diri dan patuhi rambu lalu lintas.";

    //     ChallengeData testData = new ChallengeData(
    //         uniqueName, 
    //         "10", 
    //         description, 
    //         terms, 
    //         "10K Var1",
    //         true // Private Mode
    //     );

    //     // 3. ACTION: Fill the entire form in one line
    //     challengePage.fillCreateChallengeForm(testData, false);

    //     // 4. Submit & Confirm
    //     boolean isCreated = challengePage.submitAndConfirm();

    //     // 5. Assertions
    //     if (isCreated) {
    //         TestListener.getTest().pass("SUCCESS: Challenge '" + uniqueName + "' created.");
            
    //         // Optional: Validate we are on the Detail Page
    //         Assert.assertTrue(challengePage.isTextVisible(uniqueName), "Detail page title mismatch");
    //     } else {
    //         TestListener.getTest().fail("FAILURE: Submit flow failed.");
    //     }

    //     Assert.assertTrue(isCreated, "Challenge Creation Failed!");

    //     challengePage.scrollAndCapture(1, 0.8, 0.2, "Challenge Created Successfully");

    //     // 6. Cleanup and documentation of post-creation navigation
    //     challengePage.navigateBackToDashboardSafe();
    //     challengePage.areElementsDisplayed(uniqueName);
    //     // challengePage.tapButtonByTextOrId(uniqueName, uniqueName);
    //     // challengePage.navigateBackToDashboardSafe();
    //     challengePage.navigateToDetailChallenge(uniqueName, false);
    //     challengePage.areElementsDisplayed(uniqueName);
    //     challengePage.navigateBackToDashboardSafe();
    //     challengePage.navigateToBeranda(false);
    //     challengePage.areElementsDisplayed(uniqueName);
    //     // challengePage.tapButtonByTextOrId(uniqueName, uniqueName);
    //     // driver.navigate().back();
    //     challengePage.navigateToChallengeDashboard(false);
    // } 

    // @Test(priority = 2, description = "Pengguna membuka halaman Challenge dan membuat Public Challenge")
    // @TestInfo(
    //     expected = "Pengguna dapat membuka formulir untuk membuat Public Challenge. Setelah seluruh informasi diisi dengan benar dan lengkap, sistem akan memproses dan membuat challenge tersebut atas nama pengguna.", 
    //     group = "Challenge", 
    //     testType = "Positive Case")
    // public void testCreateChallengePublic() {
    //     System.out.println("=== TEST 2: Create Challenge Public ===");

    //     // 1. Navigate
    //     challengePage.navigateToCreateMenu(true);
        
    //     // 2. Prepare Data
    //     String uniqueName = "Lari Publik " + (System.currentTimeMillis() % 10000);
    //     String description = "Tantangan lari virtual terbuka untuk seluruh pelari! Bergabunglah bersama ribuan peserta lainnya untuk menyelesaikan jarak yang ditentukan. Raih posisi terbaikmu di leaderboard global dan jadilah inspirasi bagi komunitas lari di seluruh nusantara.";
    //     String terms = "1. Terbuka untuk umum dari lokasi mana saja (Virtual Run).\n" +
    //                             "2. Jarak lari dapat diakumulasikan selama periode berlangsung.\n" +
    //                             "3. Peserta wajib memastikan GPS aktif.\n" +
    //                             "4. Hasil dengan pace tidak wajar akan dianulir otomatis.\n" +
    //                             "5. Keputusan penyelenggara bersifat mutlak.";

    //     ChallengeData testData = new ChallengeData(
    //         uniqueName, 
    //         "10", 
    //         description, 
    //         terms, 
    //         "10K Var1",
    //         false 
    //     );

    //     // 3. ACTION: Fill the entire form in one line
    //     challengePage.fillCreateChallengeForm(testData, false);

    //     // 4. Submit & Confirm
    //     boolean isCreated = challengePage.submitAndConfirm();

    //     // 5. Assertions
    //     if (isCreated) {
    //         TestListener.getTest().pass("SUCCESS: Challenge '" + uniqueName + "' created.");
            
    //         // Optional: Validate we are on the Detail Page
    //         Assert.assertTrue(challengePage.isTextVisible(uniqueName), "Detail page title mismatch");
    //     } else {
    //         TestListener.getTest().fail("FAILURE: Submit flow failed.");
    //     }

    //     Assert.assertTrue(isCreated, "Challenge Creation Failed!");

    //     challengePage.scrollAndCapture(1, 0.8, 0.2, "Challenge Created Successfully");

    //     // 6. Cleanup
    //     // 6. Cleanup and documentation of post-creation navigation
    //     challengePage.navigateBackToDashboardSafe();
    //     challengePage.areElementsDisplayed(uniqueName);
    //     // challengePage.tapButtonByTextOrId(uniqueName, uniqueName);
    //     // challengePage.navigateBackToDashboardSafe();
    //     challengePage.navigateToDetailChallenge(uniqueName, false);
    //     challengePage.areElementsDisplayed(uniqueName);
    //     challengePage.navigateBackToDashboardSafe();
    //     challengePage.navigateToBeranda(false);
    //     challengePage.areElementsDisplayed(uniqueName);
    //     // challengePage.tapButtonByTextOrId(uniqueName, uniqueName);
    //     // driver.navigate().back();
    //     challengePage.navigateToChallengeDashboard(false);
    // }

    // @Test(priority = 3, description = "Buat Challenge Lari - Mengganti hasil upload Banner challenge sebelumnya dengan mengupload Banner challenge yang baru")
    // @TestInfo(
    //     expected = "Saat membuat Challenge Lari, Pengguna berhasil mengganti banner yang sudah diupload sebelumnya dengan banner baru dan banner baru berhasil terupload", 
    //     group = "Challenge",
    //     testType = "Positive Case"
    // )
    // public void testReplaceBannerOnly() {
    //     System.out.println("=== TEST 2: Upload & Replace Banner ===");

    //     // 1. Navigate to the Create Challenge Page
    //     challengePage.navigateToCreateMenu(false);
    //     TestListener.getTest().info("1. Opened Create Challenge Menu",
    //         MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

    //     // 2. Upload the FIRST Poster
    //     TestListener.getTest().info("Action: Uploading the FIRST Poster...");
    //     challengePage.uploadPoster(1, false); // Selects Picture 1
        
    //     TestListener.getTest().info("2. First Poster Uploaded Successfully",
    //         MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());

    //     // 3. Upload the SECOND Poster (To Replace)
    //     TestListener.getTest().info("Action: Replacing with the SECOND Poster...");
    //     challengePage.uploadPoster(2, true); // Selects Picture 2
        
    //     logPass("SUCCESS: Second Poster successfully replaced the first one!");
        
    //     // 4. CLEANUP: Cancel/Navigate back without saving
    //     System.out.println("   -> Test complete. Navigating back...");
    //     challengePage.navigateBackToDashboardSafe();
    // }

    // @Test(priority = 4, description = "Pengguna mengedit poster Challenge buatannya sendiri")
    // @TestInfo(
    //     expected = "Dengan membuka halaman Edit Challenge, pengguna dapat mengganti poster Challenge yang telah dibuat dengan poster yang baru", 
    //     group = "Challenge",
    //     testType = "Positive Case"
    // )
    // public void testEditChallengeReplacePoster() {
    //     System.out.println("=== TEST 3: Edit Poster Challenge ===");

    //     challengePage.navigateToDetailChallenge("Lari Publik", false); 
    //     challengePage.navigateToEditChallenge(true);
        
    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("1. Berhasil masuk ke Halaman Edit Challenge",
    //             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    //     }

    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Mengganti poster lama dengan gambar baru...");
    //     }
        
    //     challengePage.uploadPoster(2, true); 

    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Menyimpan perubahan Edit Challenge...");
    //     }

    //     challengePage.tapButtonByTextOrId("Simpan", "Simpan", true);

    //     // Handle Success Modal ("Oke")
    //     boolean isModalSuccess = challengePage.confirmEditSaved();
    //     boolean isEditSaved = isModalSuccess && challengePage.isDetailChallengePage();
    //     // 4. Verifikasi dan Assert
    //     if (isEditSaved) {
    //         if (TestListener.getTest() != null) {
    //             logPass("SUCCESS: Perubahan poster berhasil disimpan dan divalidasi.");
    //         }
    //     } else {
    //         if (TestListener.getTest() != null) {
    //             logFail("FAILURE: Gagal menyimpan perubahan poster pada halaman Edit.");
    //         }
    //     }

    //     // Hard Assert untuk TestNG
    //     Assert.assertTrue(isEditSaved, "Gagal menyimpan Edit Challenge setelah mengganti poster!");

    //     challengePage.navigateBackToDashboardSafe();
    // }

    // @Test(priority = 5, description = "Pengguna mengedit Nama Challenge buatannya sendiri")
    // @TestInfo(
    //     expected = "Dengan membuka halaman Edit Challenge, pengguna dapat mengganti Nama Challenge yang telah dibuat dengan Nama yang baru", 
    //     group = "Challenge",
    //     testType = "Positive Case"
    // )
    // public void testEditChallengeReplaceName() {
    //     System.out.println("=== TEST 4: Edit Nama Challenge ===");

    //     // 1. Navigasi ke Halaman Edit Challenge
    //     challengePage.navigateToDetailChallenge("Lari Publik", false); 
    //     challengePage.navigateToEditChallenge(true);
        
    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("1. Berhasil masuk ke Halaman Edit Challenge",
    //             MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotBase64()).build());
    //     }

    //     // 2. Buat Nama Baru yang Unik & Ganti Nama Lama
    //     String newChallengeName = "Lari Publik Update " + (System.currentTimeMillis() % 10000);
        
    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Mengganti Nama Challenge menjadi: " + newChallengeName);
    //     }
        
    //     // Gunakan fungsi fillInputField (yang sudah memiliki fitur clear & screenshot otomatis)
    //     challengePage.updateChallengeName(newChallengeName);

    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Menyimpan perubahan Edit Challenge...");
    //     }

    //     // 3. Klik Simpan
    //     challengePage.tapButtonByTextOrId("Simpan", "Simpan", true);

    //     // 4. Handle Success Modal ("Oke") dan simpan statusnya
    //     boolean isModalSuccess = challengePage.confirmEditSaved();
        
    //     // 5. Verifikasi ganda: Modal sukses DAN kembali ke halaman Detail
    //     boolean isEditSaved = isModalSuccess && challengePage.isDetailChallengePage();
        
    //     // 6. (Opsional & Sangat Disarankan) Verifikasi nama baru muncul di Halaman Detail
    //     boolean isNewNameVisible = false;
    //     if (isEditSaved) {
    //         // Asumsi Anda memiliki helper text visibility seperti isElementPresent(By.xpath("//*[@text='...']"))
    //         isNewNameVisible = challengePage.areElementsDisplayed(newChallengeName); 
    //     }

    //     // 7. Verifikasi dan Assert Report
    //     if (isEditSaved && isNewNameVisible) {
    //         if (TestListener.getTest() != null) {
    //             TestListener.getTest().pass("SUCCESS: Nama Challenge berhasil diubah menjadi '" + newChallengeName + "' dan tampil di Halaman Detail.");
    //         }
    //     } else if (isEditSaved) {
    //          if (TestListener.getTest() != null) {
    //             TestListener.getTest().pass("SUCCESS (Partial): Perubahan disimpan, namun validasi nama baru di UI halaman detail gagal/terlewat.");
    //         }
    //     } else {
    //         if (TestListener.getTest() != null) {
    //             TestListener.getTest().fail("FAILURE: Gagal menyimpan perubahan nama atau gagal kembali ke halaman Detail.");
    //         }
    //     }

    //     // Hard Assert untuk TestNG (Kita pastikan minimal proses simpannya berhasil)
    //     Assert.assertTrue(isEditSaved, "Gagal menyimpan Edit Challenge setelah mengganti nama!");

    //     // 8. Cleanup
    //     challengePage.navigateBackToDashboardSafe();
    // }

    // @Test(priority = 6, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge membagikan challenge global")
    // @TestInfo(
    //     expected = "Challenge berhasil dibagikan sesuai tujuan Pengguna admin.\n" + //
    //                     "Admin challenge membagikan challenge global, dan proses berbagi berhasil sesuai tujuan yang diinginkan oleh admin", 
    //     group = "Challenge",
    //     testType = "Positive Case"
    // )
    // public void testShareChallengeToWhatsAppPublic() {
    //     System.out.println("=== TEST 5: Share Challenge ke WhatsApp ===");

    //     // 1. Tentukan target kontak (Sesuaikan dengan nama persis di buku telepon HP/Emulator)
    //     String targetContact = "+62 821-1830-0442 (Anda)"; 
        
    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Memulai proses Share Challenge ke WhatsApp target: " + targetContact);
    //     }

    //     // 2. Eksekusi fungsi Share
    //     // Fungsi ini sudah mencakup navigasi ke Kelola Challenge -> Klik Share -> Pilih WA -> Pilih Kontak -> Send
    //     challengePage.shareChallengeToWhatsApp(targetContact, "Lari Publik", false);

    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Proses share selesai. Mencoba kembali ke aplikasi utama...");
    //     }

    //     logPass("Share berhasil.");

    //     challengePage.navigateBackToDashboardSafe();

    // }

    // @Test(priority = 7, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge membagikan challenge private")
    // @TestInfo(
    //     expected = "Challenge berhasil dibagikan dan pada teks pesan akan ada kode undangan challenge yang tersedia.\n" + //
    //                     "dan proses berbagi berhasil sesuai tujuan yang diinginkan oleh admin", 
    //     group = "Challenge",
    //     testType = "Positive Case"
    // )
    // public void testShareChallengeToWhatsAppPrivate() {
    //     System.out.println("=== TEST 5: Share Challenge ke WhatsApp ===");

    //     // 1. Tentukan target kontak (Sesuaikan dengan nama persis di buku telepon HP/Emulator)
    //     String targetContact = "+62 821-1830-0442 (Anda)"; 
        
    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Memulai proses Share Challenge ke WhatsApp target: " + targetContact);
    //     }

    //     // 2. Eksekusi fungsi Share
    //     // Fungsi ini sudah mencakup navigasi ke Kelola Challenge -> Klik Share -> Pilih WA -> Pilih Kontak -> Send
    //     challengePage.shareChallengeToWhatsApp(targetContact, "Lari Privat", false);

    //     if (TestListener.getTest() != null) {
    //         TestListener.getTest().info("Action: Proses share selesai. Mencoba kembali ke aplikasi utama...");
    //     }

    //     logPass("Share berhasil.");

    //     challengePage.navigateBackToDashboardSafe();

    // }

    @Test(priority = 6, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge menyetujui permintaan bergabung dari peserta baru ke challenge privatenya")
    @TestInfo(
        expected = "Peserta yang sudah di setujui oleh admin challenge akan masuk ke dalam daftar peserta challenge untuk mengikuti challenge privatenya", 
        group = "Challenge",
        testType = "Positive Case"
    )
    public void testAcceptPeserta() {
        System.out.println("=== TEST 6: Terima Peserta Challenge ===");

        try {
            challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            
            // Jalankan alur terima 1 peserta
            challengePage.acceptOneParticipant("Lari Private Accept", true);
            
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menerima 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menerima 1 peserta");
            
            // Jalankan alur terima semua peserta sisanya
            challengePage.acceptAllParticipants("Lari Private Accept", true);
            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menerima 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menerima semua peserta");
            
            if (TestListener.getTest() != null) {
                TestListener.getTest().pass("SUCCESS: Alur persetujuan peserta berjalan lancar (Satuan & Massal).");
            }
        } catch (Exception e) {
            if (TestListener.getTest() != null) TestListener.getTest().fail("FAILURE: Terjadi kesalahan saat memproses peserta.");
            Assert.fail("Alur Accept Peserta gagal!");
        }

        challengePage.navigateBackToDashboardSafe();
    }

}