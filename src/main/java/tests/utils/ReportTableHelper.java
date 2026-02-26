package tests.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;

public class ReportTableHelper implements Markup {

    private String stepName;
    private String expected;
    private String actual;
    private String screenshotBase64;
    private String statusColor;

    public ReportTableHelper(String stepName, String expected, String actual, String screenshotBase64, boolean isPass) {
        this.stepName = stepName;
        this.expected = expected;
        this.actual = actual;
        this.screenshotBase64 = screenshotBase64;
        this.statusColor = isPass ? "green" : "red";
    }

    @Override
    public String getMarkup() {
        // This HTML mimics your PDF Table Structure
        String imgTag = "";
        if (screenshotBase64 != null && !screenshotBase64.isEmpty()) {
            imgTag = "<br><img src='" + screenshotBase64 + "' style='width: 150px; border: 1px solid #ccc; margin-top: 5px;' />";
        }

        return "<table style='width:100%; border-collapse: collapse; border: 1px solid #ddd;'>" +
               "  <tr style='background-color: #f9f9f9;'>" +
               "    <th style='border: 1px solid #ddd; padding: 8px; width: 30%;'>Test Case / Step</th>" +
               "    <th style='border: 1px solid #ddd; padding: 8px; width: 30%;'>Expected Result</th>" +
               "    <th style='border: 1px solid #ddd; padding: 8px; width: 30%;'>Actual Result (Evidence)</th>" +
               "    <th style='border: 1px solid #ddd; padding: 8px; width: 10%;'>Status</th>" +
               "  </tr>" +
               "  <tr>" +
               "    <td style='border: 1px solid #ddd; padding: 8px;'>" + stepName + "</td>" +
               "    <td style='border: 1px solid #ddd; padding: 8px;'>" + expected + "</td>" +
               "    <td style='border: 1px solid #ddd; padding: 8px;'>" + actual + imgTag + "</td>" +
               "    <td style='border: 1px solid #ddd; padding: 8px; color: white; font-weight: bold; background-color: " + statusColor + "; text-align: center;'>" + (statusColor.equals("green") ? "OK" : "FAIL") + "</td>" +
               "  </tr>" +
               "</table>";
    }
}