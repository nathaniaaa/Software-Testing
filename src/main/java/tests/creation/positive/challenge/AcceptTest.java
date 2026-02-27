package tests.creation.positive.challenge;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.testng.Assert;
import io.appium.java_client.android.AndroidDriver;
import tests.BaseTest;
import tests.creation.ChallengeActionHelper;
import tests.utils.TestInfo;
import tests.utils.TestListener;

public class AcceptTest extends BaseTest {
    private ChallengeActionHelper challengePage;

    @BeforeClass
    public void setupPage() {
        challengePage = new ChallengeActionHelper((AndroidDriver) driver);
    }

    @Test(priority = 1, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge menyetujui satu permintaan bergabung dari peserta baru ke challenge privatenya")
    @TestInfo(
        expected = "Peserta yang sudah di setujui oleh admin challenge akan masuk ke dalam daftar peserta challenge untuk mengikuti challenge privatenya", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Positive Case"
    )
    public void testAcceptOnePeserta() {
        System.out.println("=== TEST 6: Terima Satu Peserta Challenge ===");

        try {
            challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            
            // Jalankan alur terima 1 peserta
            boolean applicant = challengePage.acceptOneParticipant("Lari Private Accept", true);
            
            if(!applicant){
                logSkip("Peserta yang mendaftar belum ada");
            }

            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menerima 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menerima 1 peserta");
            
            boolean participantAdded = challengePage.areElementsDisplayed("2/10 Peserta");  
            
            if (participantAdded) {
                if (TestListener.getTest() != null) {
                    TestListener.getTest().pass("SUCCESS: Peserta berhasil disetujui dan masuk ke daftar.");
                }
            } else {
                if (TestListener.getTest() != null) {
                    TestListener.getTest().fail("FAILURE: Tulisan '2/10 Peserta' tidak ditemukan.");
                }
            }
            
            Assert.assertTrue(participantAdded, "Gagal memverifikasi jumlah peserta bertambah menjadi 2/10!");

        } catch (org.testng.SkipException s) {
            throw s;
            
        } catch (Exception e) {
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("FAILURE: Terjadi kesalahan saat memproses peserta. Error: " + e.getMessage());
            }
            Assert.fail("Alur Accept Peserta gagal!");
            
        } finally {
            challengePage.navigateBackToDashboardSafe();
        }
    }
    
    @Test(priority = 2, description = "Kelola Challenge di halaman Rincian Challenge - Pengguna admin challenge menyetujui semua permintaan bergabung dari peserta baru ke challenge privatenya")
    @TestInfo(
        expected = "Semua peserta yang sudah di setujui oleh admin challenge akan masuk ke dalam daftar peserta challenge untuk mengikuti challenge privatenya", 
        group = "SET & LIHAT CHALLENGE",
        testType = "Positive Case"
    )
    public void testAcceptAllPeserta() {
        System.out.println("=== TEST 7: Terima Semua Peserta Challenge ===");

        try {
            challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            
            // Jalankan alur terima semua peserta sisanya
            challengePage.acceptAllParticipants("Lari Private Accept", true);

            boolean applicant = challengePage.acceptOneParticipant("Lari Private Accept", true);
            
            if(!applicant){
                logSkip("Peserta yang mendaftar belum ada");
            }

            driver.navigate().back(); // Kembali ke halaman Kelola Challenge setelah menerima 1 peserta
            if (!challengePage.isDetailChallengePage()) {
                challengePage.navigateBackToDashboardSafe();
                challengePage.navigateToDetailChallenge("Lari Private Accept", false);
            }
            challengePage.scrollAndCapture(1, 0.8, 0.2, "Kondisi setelah menerima semua peserta");
            
            boolean participantAdded = challengePage.areElementsDisplayed("4/10 Peserta");  
            
            if (participantAdded) {
                if (TestListener.getTest() != null) {
                    TestListener.getTest().pass("SUCCESS: Peserta berhasil disetujui dan masuk ke daftar.");
                }
            } else {
                if (TestListener.getTest() != null) {
                    TestListener.getTest().fail("FAILURE: Tulisan '4/10 Peserta' tidak ditemukan.");
                }
            }
            
            Assert.assertTrue(participantAdded, "Gagal memverifikasi jumlah peserta bertambah menjadi 4/10!");

        } catch (org.testng.SkipException s) {
            throw s;
            
        } catch (Exception e) {
            if (TestListener.getTest() != null) {
                TestListener.getTest().fail("FAILURE: Terjadi kesalahan saat memproses peserta. Error: " + e.getMessage());
            }
            Assert.fail("Alur Semua Accept Peserta gagal!");
            
        } finally {
            challengePage.navigateBackToDashboardSafe();
        }
    }
}
