package tests.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            // 1. Generate a timestamp (e.g., "20260121_133000")
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            
            // 2. Add timestamp to the filename
            String reportName = "AyoLari-Report_" + timestamp + ".html";
            String reportPath = System.getProperty("user.dir") + "/test-output/" + reportName;
            
            // 3. Setup the reporter
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setDocumentTitle("Ayo Lari QA Report");
            htmlReporter.config().setReportName("Run: " + timestamp); // Shows time in the dashboard header

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("Tester", "QA Team");
            extent.setSystemInfo("Environment", "Android");
        }
        return extent;
    }
}