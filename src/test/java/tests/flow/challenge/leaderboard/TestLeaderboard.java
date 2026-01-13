package tests.flow.challenge.leaderboard;

import tests.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestLeaderboard extends BaseTest {

    // Daftar Lokasi
    
    // Ikon Challenge (Bottom Navigation)
    By navChallenge = AppiumBy.xpath("//android.widget.Button[@text='Challenge Challenge']");

    // Tab Leaderboard (Header Atas)
    By tabLeaderboard = AppiumBy.xpath("//android.view.View[@content-desc='Leaderboard Leaderboard']");

    // Validasi (Global Leaderboard)
    By textGlobal = AppiumBy.xpath("//android.view.View[contains(@content-desc, 'Global') or contains(@text, 'Global')] | //android.widget.TextView[contains(@text, 'Global')]");


    // Test Cases
    @Test(priority = 1)
    public void testMasukMenuChallenge() {
        System.out.println("TEST 1: Buka Tab Challenge");
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Klik Challenge di Bottom Navigation
        driver.findElement(navChallenge).click();
        
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        // Validasi: pastikan tombol Leaderboard di atas muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabLeaderboard));
        Assert.assertTrue(driver.findElements(tabLeaderboard).size() > 0, "Menu Challenge gagal terbuka!");
        
        takeScreenshot("Halaman_Challenge_Utama");
    }

    @Test(priority = 2)
    public void testMasukLeaderboard() {
        System.out.println("TEST 2: Klik Ikon Leaderboard");

        // Klik ikon piala (Leaderboard)
        wait.until(ExpectedConditions.elementToBeClickable(tabLeaderboard)).click();
        
        // Jeda loading data leaderboard 
        System.out.println("Menunggu data leaderboard");
        try { Thread.sleep(4000); } catch (Exception e) {} 
        
        takeScreenshot("Halaman_Leaderboard_Terbuka");
    }

    @Test(priority = 3)
    public void testScrollLeaderboard() {
        System.out.println("TEST 3: Scroll Daftar Leaderboard");

        // Scroll ke bawah pelan-pelan (Swipe 1)
        System.out.println("Scroll ke bawah");
        actions.swipeFromBottom(); 
        try { Thread.sleep(2000); } catch (Exception e) {}
        
        takeScreenshot("Leaderboard_Scrolled");
        System.out.println("Selesai cek Leaderboard.");
    }
}