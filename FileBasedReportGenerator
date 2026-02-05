import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static void main(String[] args) {
        
        Map<String, String> urlsFromFile = loadUrlsFromFile("urls.txt");

        if (urlsFromFile.isEmpty()) {
            System.err.println("No URLs found in urls.txt or file missing.");
            return;
        }

        generateConsoleReport(urlsFromFile);
    }

    private static Map<String, String> loadUrlsFromFile(String fileName) {
        Map<String, String> map = new LinkedHashMap<>(); // LinkedHashMap keeps the file order
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                if (line.trim().isEmpty() || !line.contains(",")) continue;
                
                String[] parts = line.split(",", 2);
                String name = parts[0].trim();
                String url = parts[1].trim();
                map.put(name, url);
            }
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
        }
        return map;
    }

    private static void generateConsoleReport(Map<String, String> urls) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("\n===============================================================");
        System.out.println("        DYNAMIC URL STATUS REPORT - " + dtf.format(LocalDateTime.now()));
        System.out.println("===============================================================");
        System.out.printf("%-12s | %-12s | %-12s | %-20s%n", "SITE", "STATUS", "CODE", "DESCRIPTION");
        System.out.println("---------------------------------------------------------------");

        urls.entrySet().parallelStream().forEach(entry -> {
            Map<String, Object> result = checkUrlStatus(entry.getValue());

            System.out.printf("%-12s | %-12s | %-12s | %-20s%n", 
                entry.getKey(), 
                ((String) result.get("status")).toUpperCase(), 
                result.get("status_code"), 
                result.get("description"));
        });
        System.out.println("===============================================================\n");
    }

    private static Map<String, Object> checkUrlStatus(String url) {
        Map<String, Object> urlStatus = new HashMap<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            boolean isWorking = (statusCode >= 200 && statusCode < 400);

            urlStatus.put("status", isWorking ? "working" : "not working");
            urlStatus.put("status_code", statusCode);
            urlStatus.put("description", getDescriptionForStatusCode(statusCode));

        } catch (Exception e) {
            urlStatus.put("status", "error");
            urlStatus.put("status_code", "FAIL");
            urlStatus.put("description", (e instanceof java.net.UnknownHostException) ? "Invalid Domain" : "Request Failed");
        }
        return urlStatus;
    }

    private static String getDescriptionForStatusCode(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 301, 302 -> "Redirected";
            case 404 -> "Not Found";
            case 500 -> "Server Error";
            case 503 -> "Service Unavailable";
            default -> "Status: " + statusCode;
        };
    }
}
