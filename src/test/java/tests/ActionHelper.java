package tests;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class ActionHelper {

    private AndroidDriver driver;

    public ActionHelper(AndroidDriver driver) {
        this.driver = driver;
    }

    // ========================================================================
    // 1. TAP & CLICK ACTIONS (PENTING BUAT GRAFIK & BUTTON)
    // ========================================================================

    /**
     * Tap pada koordinat spesifik (X, Y).
     * SANGAT PENTING untuk kasus CHART/GRAFIK MyTelkomsel.
     */
    public void tapByCoordinates(int x, int y) {
        System.out.println("Tapping at: " + x + ", " + y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(100))); // Jeda dikit biar stabil
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Collections.singletonList(tap));
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

    public void scrollHorizontal() {
        System.out.println("Scrolling Right to Left...");
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int centerY = size.height / 2;
        performSwipe(startX, centerY, endX, centerY);
    }

    // --- HELPER UNTUK PAKSA SCROLL MANUAL ---
    // --- HELPER UNTUK PAKSA SCROLL MANUAL (REVISI) ---
    public void swipeUp() {
        // Ambil ukuran layar
        Dimension size = driver.manage().window().getSize();
        int width = size.getWidth();
        int height = size.getHeight();

        // LOGIC BARU: SWIPE DI TENGAH (SAFE ZONE)
        // Hindari 20% atas (Header) dan 30% bawah (Menu Bar)
        int startX = width / 2;
        int startY = (int) (height * 0.60); // Mulai dari 60% layar (tengah agak bawah)
        int endY = (int) (height * 0.25);   // Geser sampai 25% layar (atas)
        
        // Buat pointer input (jari telunjuk)
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        
        // Gerakkan jari: Tekan -> Geser Cepat -> Lepas
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        
        // Pause dikit banget biar touch register (jangan lama-lama)
        swipe.addAction(new Pause(finger, Duration.ofMillis(100))); 
        
        // Durasinya dipersingkat jadi 400ms biar jadi "FLING" bukan "DRAG"
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        // Eksekusi
        driver.perform(Collections.singletonList(swipe));
        System.out.println("Swiping up (Fling Mode: 60% -> 25%)...");
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

    private void performSwipe(int startX, int startY, int endX, int endY) {
        performSwipe(startX, startY, endX, endY, 800); // Default speed 800ms
    }

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
}