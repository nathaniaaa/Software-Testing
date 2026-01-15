package tests;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ActionHelper {
    // 1. Declare the variables here
    protected  AndroidDriver driver; 
    protected  WebDriverWait wait; 

    // 2. Initialize them in the Constructor
    public ActionHelper(AndroidDriver driver) {
        this.driver = driver;
        
        // This is the missing part: Initialize 'wait' with a 10-second timeout
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    };
    // ========================================================================
    // 1. TAP & CLICK ACTIONS (PENTING BUAT GRAFIK & BUTTON)
    // ========================================================================

    /**
     * Tap pada koordinat spesifik (X, Y).
     * SANGAT PENTING untuk kasus CHART/GRAFIK MyTelkomsel.
     */
    public void tapByCoordinates(int x, int y) {
        try {
            System.out.println("   -> Tapping at: " + x + ", " + y);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(finger, Duration.ofMillis(100)));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(tap));
        } catch (Exception e) {
            System.err.println("   -> Failed to tap by coordinates: " + e.getMessage());
        }
    }

    public void doubleTap(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + (size.getWidth() / 2);
        int centerY = location.getY() + (size.getHeight() / 2);

        System.out.println("Double Tapping Element...");
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTap = new Sequence(finger, 1);

        // Tap 1
        doubleTap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        // Pause antar tap
        doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
        // Tap 2
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(doubleTap));
    }

    public void longPress(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + (size.getWidth() / 2);
        int centerY = location.getY() + (size.getHeight() / 2);

        System.out.println("Long Pressing Element...");
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);

        longPress.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(new Pause(finger, Duration.ofSeconds(2))); // Tahan 2 detik
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPress));
    }
       /**
     * Tap layar berdasarkan RASIO (Persentase).
     * Contoh: xRatio 0.5 (Tengah), yRatio 0.2 (Atas).
     */
    public void tapAtScreenRatio(double xRatio, double yRatio) {
        try {
            // 1. Boundary Check: Ensure ratios are valid (0.0 to 1.0) to prevent coordinate errors
            if (xRatio < 0 || xRatio > 1 || yRatio < 0 || yRatio > 1) {
                throw new IllegalArgumentException("Ratios must be between 0.0 and 1.0. Received: x=" + xRatio + ", y=" + yRatio);
            }

            // 2. Calculation
            Dimension size = driver.manage().window().getSize();
            int x = (int) (size.width * xRatio);
            int y = (int) (size.height * yRatio);

            System.out.println("  -> Tapping at Ratio: " + xRatio + ", " + yRatio + " (Pixel: " + x + ", " + y + ")");

            // 3. Define W3C Action
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            
            // Move to coordinates -> Press Down -> Wait 100ms -> Lift Up
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(finger, Duration.ofMillis(100))); 
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // 4. Perform Action
            driver.perform(Collections.singletonList(tap));

        } catch (Exception e) {
            // 5. Error Handling
            System.err.println("  -> Failed to tap at ratio (" + xRatio + ", " + yRatio + "): " + e.getMessage());
            // e.printStackTrace(); // Uncomment if you need the full stack trace for debugging
        }
    }

    /**
     * Finds text on screen, gets its position, and taps the center pixel.
     * Bypasses 'clickable=false' or layout blocking issues.
     */
    public void tapByTextPosition(String visibleText) {
        try {
            System.out.println("   -> [Sniper] Searching for text: '" + visibleText + "'...");
            
            // XPath is okay for generic use, but keep in mind '//*' can be slow
            By locator = AppiumBy.xpath("//*[contains(@text, '" + visibleText + "') or contains(@content-desc, '" + visibleText + "')]");
            
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // OPTIMIZATION: Use getRect() to save network calls
            Rectangle rect = element.getRect();
            int centerX = rect.x + (rect.width / 2);
            int centerY = rect.y + (rect.height / 2);
            
            System.out.println("   -> Found at [" + centerX + "," + centerY + "]. Tapping...");
            
            // Ensure this calls your modern W3C implementation
            tapByCoordinates(centerX, centerY); 

        } catch (Exception e) {
            System.err.println("   -> Failed to tap text '" + visibleText + "': " + e.getMessage());
        }
    }

    protected void tapByExactText(String exactText) {
        try {
            System.out.println("   -> [Sniper Exact] Searching for exact text: '" + exactText + "'...");

            // 1. Locator Strategy
            // Note: strict equality (@text='...') is good, but consider 'normalize-space()' if whitespace varies.
            By locator = io.appium.java_client.AppiumBy.xpath("//*[@text='" + exactText + "' or @content-desc='" + exactText + "']");

            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // 2. Optimization: use getRect() (1 server call) instead of getLocation+getSize (4 calls)
            Rectangle rect = element.getRect();
            int centerX = rect.x + (rect.width / 2);
            int centerY = rect.y + (rect.height / 2);

            System.out.println("   -> Found exact match at [" + centerX + "," + centerY + "]. Tapping...");

            // 3. W3C Action (Replaces actions.tapByCoordinates)
            tapByCoordinates(centerX, centerY);

        } catch (Exception e) {
            System.err.println("   -> Failed to tap exact text '" + exactText + "': " + e.getMessage());
        }
    }

    protected void tapElementCenter(WebElement element) {
        try {
            // Optimization: Use getRect() (1 call) instead of Location + Size (4 calls)
            Rectangle rect = element.getRect();
            int centerX = rect.x + (rect.width / 2);
            int centerY = rect.y + (rect.height / 2);

            System.out.println("   -> Tapping center: " + centerX + ", " + centerY);
            tapByCoordinates(centerX, centerY);
        } catch (Exception e) {
            System.err.println("   -> Failed to tap element center: " + e.getMessage());
        }
    }

    // Overloaded method to tap by locator
    protected void tapElementCenter(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        tapElementCenter(el); // Reuses the logic above
    }

    // ========================================================================
    // 2. SCROLL & SWIPE ACTIONS
    // ========================================================================

    public void scrollVertical() {
        System.out.println("Scrolling Down...");
        Dimension size = driver.manage().window().getSize();
        int anchorX = (int) (size.width * 0.1);
        int startY = (int) (size.height * 0.70);
        int endY = (int) (size.height * 0.30);
        performSwipe(anchorX, startY, anchorX, endY, 800);
    }

    /**
     * [BARU] Swipe Vertical Fleksibel (Input Angka Rasio).
     * Berguna untuk scroll di area aman (safe zone) agar tidak kena Peta.
     * @param startYRatio Titik Awal Y (0.0 - 1.0). Contoh: 0.6 (Bawah)
     * @param endYRatio Titik Akhir Y (0.0 - 1.0). Contoh: 0.3 (Atas)
     */
    public void swipeVertical(double startYRatio, double endYRatio) {
        Dimension size = driver.manage().window().getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        int startX = width / 2; // Selalu di tengah X
        int startY = (int) (height * startYRatio);
        int endY = (int) (height * endYRatio);

        System.out.println("Swiping Vertical Custom: " + startYRatio + " -> " + endYRatio);
        performSwipe(startX, startY, startX, endY, 800);
    }

    /**
     * Swipe Horizontal dengan Ketinggian (Y) yang bisa diatur.
     * @param startXRatio Mulai X (0.9 = Kanan)
     * @param endXRatio Selesai X (0.1 = Kiri)
     * @param yRatio Posisi Ketinggian Swipe (0.0 atas - 1.0 bawah).
     * Untuk Card Challenge biasanya sekitar 0.2 atau 0.3
     */
    public void swipeHorizontal(double startXRatio, double endXRatio, double yRatio) {
        Dimension size = driver.manage().window().getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        int startX = (int) (width * startXRatio);
        int endX = (int) (width * endXRatio);
        
        // Y ditentukan oleh yRatio (bukan lagi fix di tengah)
        int anchorY = (int) (height * yRatio); 

        System.out.println("Swiping Horizontal: X(" + startXRatio + "->" + endXRatio + ") di tinggi Y=" + yRatio);
        
        performSwipe(startX, anchorY, endX, anchorY, 1000);
    }
    
    // Drag/Pan Peta (Geser Peta pelan-pelan, bukan swipe cepat)
    public void dragMap(int startX, int startY, int endX, int endY) {
        System.out.println("Dragging Map...");
        performSwipe(startX, startY, endX, endY, 2000); // 2000ms = 2 detik (gerak lambat)
    }

    public void scrollToText(String visibleText) {
        System.out.println("Smart Scrolling to: " + visibleText);
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                ".scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\").instance(0))"
            ));
        } catch (Exception e) {
            Assert.fail("Failed to scroll to text: " + visibleText);
        }
    }

    public void scrollToExactText(String visibleText) {
        System.out.println("Smart Scrolling to (EXACT): " + visibleText);
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))" +
                // Use .text() instead of .textContains()
                ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\").instance(0))"
            ));
        } catch (Exception e) {
            Assert.fail("Failed to scroll to exact text: " + visibleText);
        }
    }

        /**
     * Scrolls to the very top of the screen using Android Native command.
     * It will swipe down up to 5 times to reach the top.
     */
    public void scrollToTop() {
        System.out.println("   -> Scrolling to TOP...");
        try {
            // "scrollToBeginning(5)" means: Try scrolling up to 5 times to reach the start.
            driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollToBeginning(5)"
            ));
        } catch (Exception e) {
            System.out.println("WARN: Native scroll to top failed. Trying manual swipe...");
            // Fallback: Manually swipe down from top of screen to bottom 3 times
            manualScrollToTop();
        }
    }

    /**
     * Manually swipes the screen from Top-Center to Bottom-Center
     * (This pulls the page content DOWN, moving the view to the TOP).
     */
    public void manualScrollToTop() {
        int startX = driver.manage().window().getSize().getWidth() / 2;
        int startY = (int) (driver.manage().window().getSize().getHeight() * 0.2); // Start near top (20%)
        int endY = (int) (driver.manage().window().getSize().getHeight() * 0.8);   // Drag to bottom (80%)

        // Perform the swipe 3 times
        for (int i = 0; i < 3; i++) {
            org.openqa.selenium.interactions.PointerInput finger = 
                new org.openqa.selenium.interactions.PointerInput(
                    org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                    
            org.openqa.selenium.interactions.Sequence swipe = 
                new org.openqa.selenium.interactions.Sequence(finger, 1);

            // Move finger to Top (20%)
            swipe.addAction(finger.createPointerMove(java.time.Duration.ZERO, 
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
            
            // Touch Down
            swipe.addAction(finger.createPointerDown(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
            
            // Move finger to Bottom (80%) - This pulls the page down
            swipe.addAction(finger.createPointerMove(java.time.Duration.ofMillis(600), 
                org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
            
            // Lift Finger
            swipe.addAction(finger.createPointerUp(
                org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(java.util.Collections.singletonList(swipe));
        }
    }

    // ========================================================================
    // 3. ZOOM GESTURES (MULTI-TOUCH)
    // ========================================================================

    public void zoomIn() {
        System.out.println("Zooming IN (Pinch Open)...");
        performPinch(true);
    }

    public void zoomOut() {
        System.out.println("Zooming OUT (Pinch Close)...");
        performPinch(false);
    }

    // ========================================================================
    // PRIVATE HELPERS (W3C LOGIC)
    // ========================================================================

    private void performSwipe(int startX, int startY, int endX, int endY, int durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        
        sequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, Duration.ofMillis(200))); 
        // Durasi swipe menentukan kecepatan (makin besar makin pelan/drag)
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));
    }

    private void performPinch(boolean zoomIn) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;
        
        // Finger 1 (Kiri)
        int f1StartX = centerX - 50;
        int f1EndX = zoomIn ? centerX - 300 : centerX - 50; 
        if (!zoomIn) f1StartX = centerX - 300; 
        
        // Finger 2 (Kanan)
        int f2StartX = centerX + 50;
        int f2EndX = zoomIn ? centerX + 300 : centerX + 50;
        if (!zoomIn) f2StartX = centerX + 300;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence sequence1 = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), f1StartX, centerY))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger1.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), f1EndX, centerY - 100))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence sequence2 = new Sequence(finger2, 1)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), f2StartX, centerY))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger2.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), f2EndX, centerY + 100))
                .addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence1, sequence2));
    }

    /**
     * Melakukan Gerakan Memutar (Rotation) dengan 2 jari.
     * Berguna untuk memunculkan tombol Kompas/Reset Bearing di Peta.
     */
    /**
     * Melakukan Gerakan Memutar (Rotation) PADA ELEMEN TERTENTU.
     * Lebih aman karena koordinat dihitung dari lokasi elemen, bukan layar.
     */
    public void rotateMap(WebElement mapElement) {
        System.out.println("Performing Map Rotation on specific element...");

        // 1. Ambil Lokasi & Ukuran Peta yg sebenarnya
        Point location = mapElement.getLocation();
        Dimension size = mapElement.getSize();

        // 2. Hitung Titik Tengah Peta
        int centerX = location.getX() + (size.getWidth() / 2);
        int centerY = location.getY() + (size.getHeight() / 2);

        // 3. Tentukan Jari-jari putaran (30% dari lebar peta biar aman gak keluar batas)
        int radius = (int) (size.getWidth() * 0.3);

        System.out.println("Center: " + centerX + "," + centerY + " | Radius: " + radius);

        // Jari 1: Mulai dari Kiri (Jam 9) ke Atas (Jam 12)
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence1 = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - radius, centerY))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofMillis(100))); // Tambah pause dikit biar touch register
        
        // Gerakan melengkung manual (Kuadran Kiri Atas)
        // Kita simulasikan geser lurus aja diagonal, biasanya map udah nangkep
        sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(1500), PointerInput.Origin.viewport(), centerX, centerY - radius));
        sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Jari 2: Mulai dari Kanan (Jam 3) ke Bawah (Jam 6)
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");
        Sequence sequence2 = new Sequence(finger2, 1)
                .addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + radius, centerY))
                .addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger2, Duration.ofMillis(100)));
        
        // Gerakan melengkung manual (Kuadran Kanan Bawah)
        sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(1500), PointerInput.Origin.viewport(), centerX, centerY + radius));
        sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(sequence1, sequence2));
    }

        /**
     * Checks if an element exists without crashing the test.
     * Use this for assertions or optional elements (like Ads).
     * @param locator The Selenium/Appium locator (By.id, By.xpath, etc.)
     * @param timeoutSeconds How long to check before giving up (e.g., 1 or 5)
     * @return true if found, false if not found
     */
    public boolean isElementPresent(org.openqa.selenium.By locator, int timeoutSeconds) {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for an element to be visible and returns it.
     * Use this instead of "driver.findElement" when you need to be safe.
     */
    public org.openqa.selenium.WebElement waitForElement(org.openqa.selenium.By locator) {
        return new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(20))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));
    }


}