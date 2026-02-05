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
    private String getExpectedResult(ITestResult result) {
        try {
            // Get the method that just ran
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            
            // Check for your custom annotation
            TestInfo info = method.getAnnotation(TestInfo.class);
            
            if (info != null) {
                return info.expected(); // Return the text you wrote in the test file
            }
        } catch (Exception e) {
            // Ignore errors if annotation is missing
        }
        return "-"; // Default if no annotation found
    }

    private String getGroup(ITestResult result) {
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            TestInfo info = method.getAnnotation(TestInfo.class);
            return (info != null) ? info.group() : "";
        } catch (Exception e) { return ""; }
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
        String expected = getExpectedResult(result);
        String group = getGroup(result);
        String note = getNote(result); // <--- Get Note
        String actual = "Test Passed Successfully";
        List<String> screenshots = BaseTest.getScreenshotList();

        // Pass 'note' to Excel
        ExcelReportManager.logToExcel(group, testName, expected, actual, note, screenshots, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getDescription();
        if (testName == null) testName = result.getMethod().getMethodName();

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

        String expected = getExpectedResult(result);
        String note = getNote(result); 
        String group = getGroup(result);
        String actual = "FAILED: " + result.getThrowable().getMessage();

        List<String> screenshots = BaseTest.getScreenshotList();
        if (screenshots == null) screenshots = new ArrayList<>();
        if (base64Screenshot != null) screenshots.add(base64Screenshot);

        // Pass 'note' to Excel
        ExcelReportManager.logToExcel(group, testName, expected, actual, note, screenshots, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (test.get() != null) test.get().log(Status.SKIP, "Skipped");
        
        String testCaseName = result.getMethod().getMethodName();
        String group = getGroup(result);
        ExcelReportManager.logToExcel(group, testCaseName, "-", "Test dilewati (Skipped)", "-", null, "SKIP");
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