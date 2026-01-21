package tests.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        // 1. Ambil Deskripsi
        String methodName = result.getMethod().getDescription();
        
        // [FIX PENTING] Cek Null DAN Cek Kosong ("")
        if (methodName == null || methodName.isEmpty()) {
            methodName = result.getMethod().getMethodName();
        }

        // [BAN SEREP] Kalau masih kosong juga (jarang terjadi), kasih nama default biar GAK CRASH
        if (methodName == null || methodName.isEmpty()) {
            methodName = "Test Tanpa Nama";
        }

        // 2. Ambil Nama Class dengan aman
        String className = "Unknown Class";
        try {
            className = result.getTestClass().getRealClass().getSimpleName();
        } catch (Exception e) {}

        // 3. Buat Test di Report
        try {
            ExtentTest extentTest = extent.createTest(methodName)
                                          .assignCategory(className);
            test.set(extentTest);
        } catch (Exception e) {
            System.out.println("Gagal membuat report: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (test.get() != null) {
            test.get().log(Status.PASS, "Test Passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // [FIX] Safety check: Kalau test gagal sebelum mulai (misal di @BeforeClass), init dulu
        if (test.get() == null) {
            onTestStart(result);
        }

        if (test.get() != null) {
            // 1. Log error message
            test.get().fail(result.getThrowable());

            // 2. Capture Screenshot automatically
            try {
                Object currentClass = result.getInstance();
                
                // [FIX] Cek tipe class biar aman (instanceof)
                if (currentClass instanceof BaseTest) {
                    BaseTest baseTest = (BaseTest) currentClass;
                    String screenshotCode = baseTest.getScreenshotBase64();

                    if (screenshotCode != null) {
                        test.get().fail("Bukti Screenshot:",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotCode).build());
                    }
                }
            } catch (Exception e) {
                System.out.println("Gagal attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // [FIX] Handle kalau test di-skip (biasanya karena error di setup)
        if (test.get() == null) {
            onTestStart(result);
        }
        if (test.get() != null) {
            test.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}