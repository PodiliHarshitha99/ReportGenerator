# ReportGenerator
Overview:
Java-based automation tool that checks status of list of URLs and generates a HTML report

**For every run:**
- Sends HTTP GET requests to configured URLs
- It captures HTTP status codes and descriptions
- Marks if the URLs are Working or Not working
- Saves a HTML report to a reports/ directory along with the timestamp

**Tech Stack and Libraries:**
- Java 11+
- Java HTTP Client (java.net.http)
- Java Collections (Maps to store URL names and Links)
- Java IO (for writing HTML Reports)
- JSON processing (org.JSON)

**Sample Output - The generated report includes:**
- URL name
- Status (Working / Not Working)
- HTTP status code
- Status description
