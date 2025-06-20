package com.example.codetest.report;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestReportGenerator {

    public static void generateJsonReport(List<TestResult> results, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), results);
            System.out.println("✅ JSON report generated at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateHtmlReport(List<TestResult> results, String filePath) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Test Report</title></head><body>");
        html.append("<h1>Test Report</h1>");
        html.append("<table border='1'><tr><th>Class</th><th>Method</th><th>Status</th><th>Error</th></tr>");

        for (TestResult result : results) {
            html.append("<tr>");
            html.append("<td>").append(result.getClassName()).append("</td>");
            html.append("<td>").append(result.getMethodName()).append("</td>");
            html.append("<td style='color:")
                    .append(result.isPassed() ? "green'>PASSED" : "red'>FAILED").append("</td>");
            html.append("<td>").append(result.getErrorMessage() == null ? "" : result.getErrorMessage()).append("</td>");
            html.append("</tr>");
        }

        html.append("</table></body></html>");

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(html.toString());
            System.out.println("✅ HTML report generated at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}