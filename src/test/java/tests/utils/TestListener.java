package tests.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList; 
import java.util.List;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    // Map untuk menyimpan Parent Test (Supaya report HTML rapi per Class)
    private static Map<String, ExtentTest> classLevelTests = new HashMap<>();

    // --- HELPER: Ambil data dari @TestInfo ---
    private String getExpected(ITestResult result) {
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            TestInfo info = method.getAnnotation(TestInfo.class);
            return (info != null) ? info.expected() : "-";
        } catch (Exception e) { return "-"; }
    }

    private String getNote(ITestResult result) {
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            TestInfo info = method.getAnnotation(TestInfo.class);
            return (info != null) ? info.note() : "-";
        } catch (Exception e) { return "-"; }
    }

    @Override
    public void onStart(ITestContext context) {
        // Setup Excel saat suite mulai jalan
        ExcelReportManager.setupExcel();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create HTML Report Node
        String className = result.getTestClass().getRealClass().getSimpleName();
        ExtentTest parentTest = extent.createTest(className); // Simplified for this example
        String methodName = result.getMethod().getDescription();
        if (methodName == null) methodName = result.getMethod().getMethodName();
        
        test.set(parentTest.createNode(methodName));
        
        // Note: BaseTest.clearEvidence() is handled by @BeforeMethod in BaseTest
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (test.get() != null) test.get().log(Status.PASS, "Test Passed");

        // --- WRITE TO EXCEL (ONE ROW) ---
        String testName = result.getMethod().getDescription();
        if (testName == null) testName = result.getMethod().getMethodName();

        // 1. Get ALL screenshots collected during the test
        List<String> screenshots = BaseTest.getScreenshotList();

        // 2. Log to Excel
        ExcelReportManager.logToExcel(testName, "-", "Test Passed Successfully", screenshots, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // --- HTML LOGGING ---
        String base64Screenshot = null;
        try {
            Object currentClass = result.getInstance();
            if (currentClass instanceof BaseTest) {
                base64Screenshot = ((BaseTest) currentClass).getScreenshotBase64();
            }
        } catch (Exception e) {}

        if (test.get() != null) {
            test.get().fail(result.getThrowable());
            if (base64Screenshot != null) {
                test.get().fail("Failure Snapshot", 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
        }

        // --- EXCEL LOGGING (ONE ROW) ---
        String testName = result.getMethod().getDescription();
        if (testName == null) testName = result.getMethod().getMethodName();

        // 1. Get screenshots collected so far + Add the Failure screenshot
        List<String> screenshots = BaseTest.getScreenshotList();
        if (base64Screenshot != null) {
            screenshots.add(base64Screenshot);
        }

        // 2. Log to Excel
        ExcelReportManager.logToExcel(testName, "-", "FAILED: " + result.getThrowable().getMessage(), screenshots, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (test.get() != null) test.get().log(Status.SKIP, "Skipped");
        
        String testCaseName = result.getMethod().getMethodName();
        ExcelReportManager.logToExcel(testCaseName, "-", "Test dilewati (Skipped)", null, "SKIP");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) extent.flush(); 
        ExcelReportManager.saveExcel();
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}