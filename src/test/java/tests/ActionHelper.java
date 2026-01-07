package tests;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import org.openqa.selenium.Dimension;
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

    // --- SCROLL / SWIPE ---

    public void scrollVertical() {
        System.out.println("Scrolling Down...");
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        performSwipe(startX, startY, startX, endY);
    }

    public void scrollHorizontal() {
        System.out.println("Scrolling Right to Left...");
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int centerY = size.height / 2;
        performSwipe(startX, centerY, endX, centerY);
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

    // --- ZOOM GESTURES (Multi-Touch) ---

    public void zoomIn() {
        System.out.println("Zooming IN (Pinch Open)...");
        performPinch(true);
    }

    public void zoomOut() {
        System.out.println("Zooming OUT (Pinch Close)...");
        performPinch(false);
    }

    // --- PRIVATE HELPERS (The complex W3C Logic) ---

    private void performSwipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        
        sequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, Duration.ofMillis(200))); // Wait for touch to stabilize
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), endX, endY));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));
    }

    private void performPinch(boolean zoomIn) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;
        
        // Finger 1 (Left side)
        int f1StartX = centerX - 50;
        int f1EndX = zoomIn ? centerX - 300 : centerX - 50; // Move Out if Zoom In, Stay if Zoom Out
        if (!zoomIn) f1StartX = centerX - 300; // Start Far if Zoom Out
        
        // Finger 2 (Right side)
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
}
