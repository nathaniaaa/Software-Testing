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
        // --- 1. LOGIKA PARENT-CHILD (Grouping per Class) ---
        String className = result.getTestClass().getRealClass().getSimpleName();
        ExtentTest parentTest;

        if (classLevelTests.containsKey(className)) {
            parentTest = classLevelTests.get(className);
        } else {
            parentTest = extent.createTest(className);
            classLevelTests.put(className, parentTest);
        }

        // --- 2. SETUP CHILD TEST (Method) ---
        String methodName = result.getMethod().getDescription();
        if (methodName == null || methodName.isEmpty()) {
            methodName = result.getMethod().getMethodName();
        }
        
        // Buat Node dibawah Parent
        ExtentTest childTest = parentTest.createNode(methodName);
        test.set(childTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // 1. Log ke HTML (Wajib biar statusnya Hijau/Pass)
        if (test.get() != null) test.get().log(Status.PASS, "Test Case Selesai dengan Sukses.");

        // --- PENTING: BAGIAN EXCEL DIHAPUS ---
        // Kita TIDAK lagi menulis ke Excel di sini.
        // Kenapa? Karena step-step (Klik A, Klik B, Validasi C) sudah dicatat
        // oleh method `clickTest` di dalam BaseTest.java.
        // Jadi laporan Excel kamu nanti isinya detail per langkah, bukan cuma 1 baris di akhir.
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String base64Screenshot = null;

        // Ambil Screenshot saat Error (Biasanya tanpa kotak merah karena crash mendadak)
        try {
            Object currentClass = result.getInstance();
            if (currentClass instanceof BaseTest) {
                base64Screenshot = ((BaseTest) currentClass).getScreenshotBase64();
            }
        } catch (Exception e) {}

        // 1. Log ke HTML (Extent)
        if (test.get() != null) {
            test.get().fail(result.getThrowable());
            if (base64Screenshot != null) {
                test.get().fail("Bukti Error:", 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
        }

        // 2. Log ke EXCEL (FAIL) - Wajib ada biar ketahuan kalau test gagal
        String testCaseName = result.getMethod().getDescription();
        if(testCaseName == null) testCaseName = result.getMethod().getMethodName();

        String expected = getExpected(result);
        String note = getNote(result);
        String actual = "GAGAL: " + result.getThrowable().getMessage(); 

        ExcelReportManager.logToExcel(testCaseName, expected, actual, base64Screenshot, note, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (test.get() != null) test.get().log(Status.SKIP, "Skipped");
        
        String testCaseName = result.getMethod().getMethodName();
        ExcelReportManager.logToExcel(testCaseName, "-", "Test dilewati (Skipped)", null, "-", "SKIP");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) extent.flush(); // Save HTML
        ExcelReportManager.saveExcel();     // Save Excel
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}