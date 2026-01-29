package com.reports.automation;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.List;
import java.util.Map;

import org.json.JSONObject;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

public class HourlyReportGenerator {

	private static final Map<String, String> URLS = Map.of(
	        "Google", "https://www.google.com",
	        "Netflix", "https://www.netflix.com",
	        "LinkedIn", "https://www.linkedin.com",
	        "Goagle", "https://www.goagle.com",
	        "Example", "http://www.example.com/this-page-does-not-exist-12345"
	);

	private static final HttpClient httpClient = HttpClient.newHttpClient();

	public static void main(String[] args) {

//        ScheduledExecutorService scheduler =
//                Executors.newSingleThreadScheduledExecutor();
//
//        scheduler.scheduleAtFixedRate(
//                HourlyReportGenerator::generateReports,0,1,TimeUnit.HOURS
//        );
//
//        System.out.println("Hourly report generator started...");
		generateReports();
	}

	private static void generateReports() {
        System.out.println("Generating reports at: " + LocalDateTime.now());

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Hourly URL Status Report</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                        }
                        h1 {
                            color: #333;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }
                        th, td {
                            padding: 10px;
                            border: 1px solid #ccc;
                            text-align: left;
                        }
                        th {
                            background-color: #f4f4f4;
                        }
                        .working {
                            color: green;
                            font-weight: bold;
                        }
                        .not-working {
                            color: red;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                """);

        html.append("<h1>Hourly URL Status Report</h1>");
        html.append("<p>Generated at: ")
            .append(LocalDateTime.now())
            .append("</p>");

        html.append("""
                <table>
                    <tr>
                        <th>URL</th>
                        <th>Status</th>
                        <th>Status Code</th>
                        <th>Description</th>
                    </tr>
                """);

        for (var entry : URLS.entrySet()) {
            String name = entry.getKey();
            String url = entry.getValue();

            JSONObject status = checkUrlStatus(url);

            String statusText = status.optString("status", "unknown");
            String cssClass = statusText.equalsIgnoreCase("working")
                    ? "working"
                    : "not-working";

            html.append("<tr>");
            html.append("<td>").append(name).append("</td>");
            html.append("<td class='").append(cssClass).append("'>")
                .append(statusText)
                .append("</td>");
            html.append("<td>")
            .append(status.opt("status_code"))
            .append("</td>");
            html.append("<td>")
            .append(status.optString("description", "N/A"))
            .append("</td>");
            html.append("</tr>");
        }

        html.append("""
                </table>
                </body>
                </html>
                """);

        try {
            saveReportToFile(html.toString());
        } catch (Exception e) {
            System.err.println("Error saving report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static JSONObject checkUrlStatus(String url) {
        JSONObject urlStatus = new JSONObject();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String description = getDescriptionForStatusCode(statusCode);
            String status = (statusCode >= 200 && statusCode < 300)
                    ? "working"
                    : "not working";

            urlStatus.put("status", status);
            urlStatus.put("status_code", statusCode);
            urlStatus.put("description", description);

        } catch (Exception e) {
            urlStatus.put("status", "not working");
            urlStatus.put("status_code", "Error");
            urlStatus.put("description", e.getMessage());
        }

        return urlStatus;
    }

    private static String getDescriptionForStatusCode(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 301: return "Moved Permanently";
            case 302: return "Found (Redirect)";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 503: return "Service Unavailable";
            default: return "Unknown Status";
        }
    }

    private static void saveReportToFile(String reportContent) throws Exception {
        String timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        File directory = new File("reports");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = "reports/hourly_report_" + timestamp + ".html";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(reportContent);
        }

        System.out.println("HTML Report saved: " + fileName);
    }
}