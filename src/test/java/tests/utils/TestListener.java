package tests.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest; // Import BaseTest

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        // 1. Get the Test Description
        String methodName = result.getMethod().getDescription();
        if (methodName == null) methodName = result.getMethod().getMethodName();

        // 2. Get the Class Name (e.g., "TargetTest")
        String className = result.getTestClass().getRealClass().getSimpleName();

        // 3. Create the test and assign the Category
        ExtentTest extentTest = extent.createTest(methodName)
                                    .assignCategory(className); // <--- THIS ADDS THE TAG

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // 1. Log the error message
        test.get().fail(result.getThrowable());

        // 2. Capture Screenshot automatically
        try {
            // We get the driver instance from the running test class
            Object currentClass = result.getInstance();
            BaseTest baseTest = (BaseTest) currentClass;

            String screenshotCode = baseTest.getScreenshotBase64();

            if (screenshotCode != null) {
                test.get().fail("Screenshot of Failure:",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotCode).build());
            }
        } catch (Exception e) {
            System.out.println("Error attaching screenshot to report: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // Write the file to disk
        extent.flush();
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}