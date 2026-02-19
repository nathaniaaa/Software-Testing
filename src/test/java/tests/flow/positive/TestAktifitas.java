package tests.flow.positive;

import tests.BaseTest;
import tests.utils.TestInfo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestAktifitas extends BaseTest {

    // Ikon Aktivitas (Bottom Navigation)
    By navAktivitas = AppiumBy.xpath("//android.widget.Button[@text=\"Aktivitas Aktivitas\"]");
    
    By btnBack = AppiumBy.xpath("//android.widget.Button[1]"); 

    // **** BAGIAN RIWAYAT LARI ****
    By tabRiwayatLari = AppiumBy.accessibilityId("Riwayat Lari Riwayat Lari"); 

    // Header (Judul)
    By headerAktivitas = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Aktivitas Kamu')]");
    
    // Card Riwayat Lari -> Card Pertama
    By firstActivityCard = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Halaman Detail
    By titlePage = AppiumBy.xpath("//android.widget.TextView[@text='Rincian Lari']");
    
    // Tombol Unduh & Edit
    By btnUnduh = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View/android.view.View[1]/android.widget.Button[2]");
    By btnEditAktivitas = AppiumBy.xpath("(//android.widget.Image[@text='svg%3e'])[3]");
    
    // Modal Edit Nama Aktivitas
    By inputNamaAktivitas = AppiumBy.className("android.widget.EditText");
    By btnSimpan = AppiumBy.xpath("//android.widget.Button[@text='Simpan']");
    
    // Judul Modal (Buat dipencet biar keyboard nutup)
    By titleModalEdit = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Ubah Nama')]");

    // Element Peta
    By mapAreaLocator = AppiumBy.xpath("//android.view.View[@resource-id=\"root\"]/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View[1]/android.view.View[1]");
    By btnZoomIn = AppiumBy.xpath("//android.widget.Button[@content-desc=\"Zoom in\"]"); 
    By btnZoomOut = AppiumBy.accessibilityId("Zoom out");
    By btnInfoMap = AppiumBy.xpath("//android.view.View[@text='Toggle attribution']"); 
    By btnCompass = AppiumBy.accessibilityId("Reset bearing to north");
    
    // Element Grafik 
    By grafikAreaLocator = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.view.View");

    // Test Cases - Riwayat Lari
    @Test(priority = 1, description = "Navigasi ke Aktivitas")
    @TestInfo(
        testType = "Positive Case",
        expected = "Akan terlihat daftar riwayat lari yang pernah dilakukan oleh user",
        note = "",
        group = "Aktivitas"
    )
    public void testNavigasiAktivitas() {
        System.out.println("TEST 1: Navigasi ke Aktivitas");

        logInfo("Tampilan awal di Beranda.");

        // Klik ikon Aktivitas di Bottom Navigation
        System.out.println("Klik ikon Aktivitas");
        clickTest(navAktivitas, "Klik ikon Aktivitas");
        waitTime();

        logPass("Berhasil klik ikon Aktivitas");
    }

    @Test(priority = 2, description = "Riwayat Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Akan terlihat daftar riwayat lari yang pernah dilakukan oleh user",
        note = "",
        group = "Aktivitas"
    )
    public void testMasukKeRiwayatLari() {
        System.out.println("TEST 2: Riwayat Lari");

        clickTest(tabRiwayatLari, "Klik Tab Riwayat Lari");
        waitTime();

        logPass("Berhasil klik Tab Riwayat Lari");
    }

    @Test(priority = 3, description = "Pengguna menekan salah satu Riwayat Lari dari hasil record dan atau dari hasil sync smartwatch")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna menekan salah satu Riwayat Lari dari hasil record dan atau dari hasil sync smartwatch",
        note = "",
        group = "Aktivitas"
    )
    public void testKlikCard() {
        System.out.println("TEST 3: Pengguna menekan salah satu Riwayat Lari dari hasil record dan atau dari hasil sync smartwatch");

        if(driver.findElements(firstActivityCard).size() > 0) {
            System.out.println("Aktivitas ditemukan. Klik card pertama");
            clickTest(firstActivityCard, "Klik Card Aktivitas Pertama");
            
            System.out.println("Memuat halaman detail");
            try { Thread.sleep(4000); } catch (Exception e) {}

            wait.until(ExpectedConditions.visibilityOfElementLocated(titlePage));
            logPass("Berhasil Masuk ke Rincian Lari");
        } else {
            System.out.println("SKIP: Tidak ada Aktivitas lari di list!");
            logSkip("Tidak ada Aktivitas lari");
        }
    }

    private boolean isDiHalamanRincianLari() {
        return driver.findElements(titlePage).size() > 0;
    }

    @Test(priority = 4, description = "Validasi Elemen Statistik")
    @TestInfo(
        testType = "Positive Case",
        expected = "Semua elemen statistik seperti jarak (km) dan waktu/durasi tampil dengan benar pada halaman rincian lari",
        note = "",
        group = "Aktivitas"
    )
    public void testStatistikUI() {
        System.out.println("TEST 4: Validasi Elemen Statistik");

        // Pastikan sedang di halaman rincian lari, kalau engga skip
        if (!isDiHalamanRincianLari()) logSkip("Test dilewati: Tidak sedang di halaman Rincian Lari.");
        
        boolean isJarakAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'km')]")).size() > 0;
        boolean isWaktuAda = driver.findElements(AppiumBy.xpath("//android.widget.TextView[contains(@text, ':')]")).size() > 0;
        
        Assert.assertTrue(isJarakAda, "Data Jarak (km) tidak tampil!");
        Assert.assertTrue(isWaktuAda, "Data Waktu/Durasi tidak tampil!");
        
        System.out.println("Statistik aman.");
        capture.highlightRectangleByRatio(0.05, 0.26, 0.9, 0.25, "Statistik Jarak & Waktu tampil dengan benar.");
    }
    
    @Test(priority = 5, description = "Tombol Unduh")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat menggunakan tombol unduh pada halaman rincian lari",
        note = "",
        group = "Aktivitas"
    )
    public void testFiturUnduh() {
        System.out.println("TEST 5: Tombol Unduh");

        // Pastikan sedang di halaman rincian lari, kalau engga skip
        if (!isDiHalamanRincianLari()) logSkip("Test dilewati: Tidak sedang di halaman Rincian Lari.");
        
        // Tombol Unduh
        if (driver.findElements(btnUnduh).size() > 0) {
            System.out.println("Tombol Unduh ada. Klik");
            clickTest(btnUnduh, "Klik Tombol Unduh");
            logPass("Berhasil mengunduh data aktivitas");
            waitTime();
        } else {
            System.out.println("SKIP: Tombol Unduh tidak ditemukan.");
            logSkip("Test di Skip karena Tombol Unduh tidak ditemukan.");
        }
        waitTime();
    }

    @Test(priority = 6, description = "Tombol Edit")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat menggunakan tombol Edit pada halaman rincian lari",
        note = "",
        group = "Aktivitas"
    )
    public void testFiturEdit() {
        System.out.println("TEST 6: Tombol Edit");

        // Pastikan sedang di halaman rincian lari, kalau engga skip
        if (!isDiHalamanRincianLari()) logSkip("Test dilewati: Tidak sedang di halaman Rincian Lari.");
        
        // Tombol Edit & Isi Nama
        if (driver.findElements(btnEditAktivitas).size() > 0) {
            System.out.println("Tombol Edit ada. Klik");
            clickTest(btnEditAktivitas, "Klik Tombol Edit");
            
            System.out.println("Menunggu modal edit muncul");
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputNamaAktivitas));

            logInfo("Nama Aktivitas Sebelum di Edit");
            
            // Isi Nama Baru
            WebElement input = driver.findElement(inputNamaAktivitas);
            input.click(); 
            try { Thread.sleep(500); } catch (Exception e) {}
            
            input.clear(); 
            input.sendKeys("TEST TEST OKI123"); // Nama baru
            waitTime();
            System.out.println("Ketik nama baru selesai.");
            
            try {
                if(driver.findElements(titleModalEdit).size() > 0) {
                    System.out.println("Klik judul modal (biar keyboard nutup)");
                    clickTest(titleModalEdit, "Klik Judul Modal Edit");
                }
            } catch (Exception e) {}
            waitTime();
            
            // Klik Simpan
            if (driver.findElements(btnSimpan).size() > 0) {
                System.out.println("Klik tombol Simpan");
                wait.until(ExpectedConditions.elementToBeClickable(btnSimpan));
                clickTest(btnSimpan, "Klik Tombol Simpan");
                
                System.out.println("Menunggu proses simpan");
                try {
                    // Tunggu modal hilang 
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(inputNamaAktivitas));
                    System.out.println("Sukses: Modal Edit sudah tertutup.");
                    
                    logPass("Berhasil edit nama Aktivitas");
                } catch (Exception e) {
                    System.out.println("Warning: Modal masih nyangkut, paksa tap luar");
                    actions.tapAtScreenRatio(0.5, 0.15);
                }
            }
            
        } else {
            System.out.println("SKIP: Tombol Edit tidak ditemukan.");
            logSkip("Test di Skip karena Tombol Edit tidak ditemukan.");
        }
    }

    @Test(priority = 7, description = "Interaksi Peta")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat Berinteraksi dengan peta pada halaman rincian lari",
        note = "",
        group = "Aktivitas"
    )
    public void testInteraksiPeta() {
        System.out.println("TEST 7: Interaksi Peta (Map)");

        // Pastikan sedang di halaman rincian lari, kalau engga skip
        if (!isDiHalamanRincianLari()) logSkip("Test dilewati: Tidak sedang di halaman Rincian Lari.");

        waitTime();

        // Validasi ada Container Peta atau engga
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(mapAreaLocator));
        } catch (Exception e) {
            System.out.println("Peta tidak muncul setelah ditunggu.");
        }

        // Kalau ada peta, lanjut interaksi, kalau engga skip
        if(driver.findElements(mapAreaLocator).size() > 0) {
            logPass("Container Peta muncul.");
            
            // Zoom In
            if(driver.findElements(btnZoomIn).size() > 0) {
                System.out.println("Klik Zoom In");
                clickTest(btnZoomIn, "Klik Zoom In");
                logPass("Berhasil Zoom In Peta");
                waitTime(); 
            } else {
                System.out.println("Tombol Zoom In tidak ditemukan.");
                logSkip("Tombol Zoom In tidak ditemukan");
            }

            // Zoom Out
            if(driver.findElements(btnZoomOut).size() > 0) {
                System.out.println("Klik Zoom Out");
                clickTest(btnZoomOut, "Klik Zoom Out");
                logPass("Berhasil Zoom Out Peta");
                waitTime(); 
            } else {
                System.out.println("Tombol Zoom Out tidak ditemukan.");
                logSkip("Tombol Zoom Out tidak ditemukan");
            }
            
            // Info Map
            if(driver.findElements(btnInfoMap).size() > 0) {
                System.out.println("Klik Info Map");
                clickTest(btnInfoMap, "klik Info Map"); 
                logPass("Berhasil klik info map");
                waitTime();
            } else {
                System.out.println("Tombol Info Map tidak ditemukan.");
                logSkip("Tombol Info Map tidak ditemukan");
            }

            // Panning Map
            System.out.println("Simulasi Geser Peta (Panning)");
            int screenWidth = driver.manage().window().getSize().width;
            int screenHeight = driver.manage().window().getSize().height;
            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;
            
            actions.dragMap(centerX, centerY, centerX + 200, centerY + 200);
            waitTime();

            // Rotate
            System.out.println("Mencoba Memutar Peta");
            try {
                WebElement mapArea = driver.findElement(mapAreaLocator);
                actions.rotateMap(mapArea); 
                logPass("  Berhasil memutar Peta");
                waitTime(); 
            } catch (Exception e) {}

            // Compass
            try {
                if(driver.findElements(btnCompass).size() > 0) {
                    System.out.println("Klik Kompas");
                    clickTest(btnCompass, "Klik Kompas");
                    logPass("Berhasil Klik Kompas");
                    waitTime(); 
                } else {
                    System.out.println("Tombol Kompas tidak ditemukan.");
                    logInfo("Tombol Kompas tidak ditemukan");
                }
            } catch (Exception e) {}
            
            logPass("Interaksi Peta Sudah Selesai Dilakukan");
        } else {
            logSkip("Test dilewati: Peta tidak ditemukan (Kemungkinan lari manual / treadmill).");
        }        
    }

    @Test(priority = 8, description = "Interaksi Grafik Ketinggian")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat Berinteraksi dengan Grafik Ketinggian pada halaman rincian lari",
        note = "",
        group = "Aktivitas"
    )
    public void testScrapingGrafikKetinggian() {
        System.out.println("TEST 8: Interaksi Grafik Ketinggian");

        // Pastikan sedang di halaman rincian lari, kalau engga skip
        if (!isDiHalamanRincianLari()) logSkip("Test dilewati: Tidak sedang di halaman Rincian Lari.");

        actions.swipeVertical(0.4, 0.2);
        try { Thread.sleep(3000); } catch (Exception e) {}

        if (driver.findElements(grafikAreaLocator).size() > 0) {
             System.out.println("Grafik ditemukan!");
             WebElement grafikContainer = driver.findElement(grafikAreaLocator);

             int startX = grafikContainer.getLocation().getX();
             int totalWidth = grafikContainer.getSize().getWidth();
             int centerY = grafikContainer.getLocation().getY() + (grafikContainer.getSize().getHeight() / 2);
             //  int endX = startX + totalWidth;

             // Tap tap Grafik
             System.out.println("Tap data grafik");
             int point1 = startX + (int)(totalWidth * 0.20); 
             int point2 = startX + (int)(totalWidth * 0.50); 
             int point3 = startX + (int)(totalWidth * 0.80); 
             
             int[] tapPoints = {point1, point2, point3};
             
             for (int pointX : tapPoints) {
                 actions.tapByCoordinates(pointX, centerY);
                 logPass("Klik Grafik Ketinggian");
                 waitTime();
             }

             // Drag horizontal
             System.out.println("Drag horizontal di grafik (Scrubbing)");
             
             int dragStartX = startX + (int)(totalWidth * 0.15); 
             int dragEndX = startX + (int)(totalWidth * 0.85); 
             
             actions.dragMap(dragStartX, centerY, dragEndX, centerY);
             logPass("Drag Grafik Ketinggian");
             
             waitTime();

             logPass("Intreaksi Grafik Ketinggian Selesai Dilakukan");
        } else {
             System.out.println("SKIP: Grafik tidak ditemukan.");
             logSkip("Grafik Ketinggian tidak dapat ditemukan");
        }
        waitTime();
        // Scroll balik ke atas
        actions.swipeVertical(0.1, 0.7);
    }
    
    @Test(priority = 9, description = "Navigasi Kembali ke List Riwayat Lari")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat kembali ke list riwayat setelah melihat detail aktivitas",
        note = "",
        group = "Aktivitas"
    )
    public void testKembaliKeMenu() {
        System.out.println("TEST 9: Navigasi Kembali ke List Riwayat Lari");

        // Cek dulu apakah memang di halaman rincian (kalau engga, gausah back)
        if (!isDiHalamanRincianLari()) {
            logSkip("Test dilewati: Posisi sudah berada di luar halaman Rincian Lari.");
        } else {
            clickTest(btnBack, "Klik Tombol Back");        
            waitTime();
            logPass("Berhasil kembali ke Halaman Aktivitas Utama.");
        }
    }

    // **** BAGIAN RIWAYAT CHALLENGE ****

    // Daftar Lokasi - Riwayat Challenge
    // Tab Challenge (Header Atas)
    By tabRiwayatChallenge = AppiumBy.accessibilityId("Leaderboard Riwayat Challenge");

    // Card Challenge 
    By cardChallengePertama = AppiumBy.xpath("//android.view.View[@resource-id='root']/android.view.View[2]");

    // Detail Page Elements
    // Button Deskripsi dan Leaderboard
    By btnDeskripsi = AppiumBy.xpath("//android.widget.TextView[@text='Deskripsi'] | //android.view.View[contains(@resource-id, 'trigger-deskripsi')]");
    By btnLeaderboard = AppiumBy.xpath("//android.widget.TextView[@text='Leaderboard'] | //android.view.View[contains(@resource-id, 'trigger-leaderboard')]");

    // Test Cases - Riwayat Challenge
    @Test(priority = 10, description = "Navigasi ke List Riwayat Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Riwayat challenge akan muncul setelah masa aktif challenge yang diikuti sudah berakhir, walaupun syarat Jarak dalam challenge tidak terpenuhi",
        note = "",
        group = "Aktivitas"
    )
    public void testNavigasiKeListChallenge() {
        System.out.println("TEST 10: Navigasi ke List Riwayat Challenge");

        logInfo("Tampilan awal di Aktivitas.");

        // Klik Riwayat Challenge di header
        System.out.println("Klik Riwayat Challenge");
        clickTest(tabRiwayatChallenge, "Klik Riwayat Challenge");
        waitTime();

        // Cek apakah ada card riwayat challenge?
        if (driver.findElements(cardChallengePertama).size() > 0) {
            logPass("Berhasil masuk dan List Riwayat Challenge tampil.");
        } else {
            logSkip("Test dilewati: Akun ini tidak memiliki riwayat Challenge (Empty State).");
        }
    }

    @Test(priority = 11, description = "Interaksi pada Halaman Detail Riwayat Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "Pengguna dapat melihat detail riwayat challenge dengan mengklik salah satu card riwayat challenge, lalu pengguna dapat berinteraksi dengan konten yang ada di dalamnya seperti melihat deskripsi challenge, melihat leaderboard, dan kembali ke list riwayat challenge",
        note = "",
        group = "Aktivitas"
    )
    public void testInteraksiDetailRiwayatChallenge() {
        System.out.println("TEST 11: Interaksi pada Halaman Detail Riwayat Challenge");

        waitTime();

        // Pastikan ada card riwayat challenge, kalau engga skip
        if (driver.findElements(cardChallengePertama).size() == 0) {
            logSkip("Test dilewati: Tidak ada riwayat challenge untuk dibuka.");
        }

        // Masuk ke card
        System.out.println("Masuk ke Riwayat Challenge Pertama");
        clickTest(cardChallengePertama, "Klik Card Riwayat Challenge Pertama");

        // Validasi masuk detail (tombol Deskripsi)
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnDeskripsi));
        logPass("Berhasil masuk ke halaman detail Riwayat Challenge (Card Pertama)");
        
        // Default halaman di Deskripsi -> scroll dulu
        System.out.println("Halaman Deskripsi terbuka, mencoba Scroll ke bawah");
        try {
            // Scroll ke bawah untuk baca deskripsi
            actions.swipeVertical(0.7, 0.3); 
            waitTime();

            logPass("Berhasil scroll halaman");
        } catch (Exception e) {
            System.out.println("Gagal scroll: " + e.getMessage());
            logFail("Gagal Scroll halaman");
        }

        // Pindah ke Leaderboard
        System.out.println("Klik Button Leaderboard");
        clickTest(btnLeaderboard, "Klik Button Leaderboard");
        waitTime();
        
        // Validasi: pastikan tombol deskripsi masih ada (tidak crash)
        Assert.assertTrue(driver.findElements(btnDeskripsi).size() > 0, "Tab Leaderboard gagal dimuat");
        logPass("Berhasil klik Tab Leaderboard");

        // Balik lagi ke Deskripsi
        System.out.println("Klik button ke Deskripsi");
        clickTest(btnDeskripsi, "Klik Button Deskripsi");
        waitTime();
        
        logPass("Berhasil klik Tab Deskripsi");
    }

    @Test(priority = 12, description = "Kembali ke List Riwayat Challenge")
    @TestInfo(
        testType = "Positive Case",
        expected = "",
        note = "",
        group = "Aktivitas"
    )
    public void testKembaliKeList() {
        System.out.println("TEST 12: Kembali ke List Riwayat Challenge");

        // Pastikan benar-benar ada di dalam Detail Riwayat Challenge 
        boolean isDiDetailChallenge = driver.findElements(btnDeskripsi).size() > 0;

        if (isDiDetailChallenge) {
            clickTest(btnBack, "Klik Tombol Back");
            waitTime();
            logPass("Berhasil kembali ke List Riwayat Challenge");
        } else {
            logSkip("Test dilewati: Posisi sudah berada di luar halaman detail.");
        }
    }

    // Helper
    public void waitTime() {
        try { Thread.sleep(1500); } catch (Exception e) {}
    }
}
