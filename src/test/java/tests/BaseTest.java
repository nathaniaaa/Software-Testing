package tests;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class BaseTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected ActionHelper actions; // Available for all tests

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setUdid("RRCTA02QJAR")
                .setDeviceName("Sam Biru")
                .setAdbExecTimeout(Duration.ofSeconds(60))
                .setAppPackage("com.android.settings") 
                .setAppActivity(".Settings")
                .setAutoGrantPermissions(true)
                .setNoReset(true);

        driver = new AndroidDriver(
                URI.create("http://127.0.0.1:4723").toURL(), options
        );

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        // Initialize the ActionHelper here!
        actions = new ActionHelper(driver);

        masukKeMenuAyoLari();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void masukKeMenuAyoLari() {
        System.out.println("Navigasi ke AyoLari...");
        try {
            driver.activateApp("com.telkomsel.telkomselcm");
            String xpathAyoLari = "(//android.widget.FrameLayout[@resource-id='com.telkomsel.telkomselcm:id/cvDigitalService'])[5]";
            WebElement btnAyoLari = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpathAyoLari)));
            btnAyoLari.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("Mulai Lari")));
        } catch (Exception e) {
            System.out.println("Navigasi Auto Gagal/Sudah di page: " + e.getMessage());
        }
    }
}