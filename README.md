# Java-Based URL Monitoring and HTML Report Generator using Eclipce IDE
**Overview:**
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
- Java IO (for writing HTML Reports. File, FileWriter)
- JSON processing (org.JSON)

To compile:
javac -cp .;<path> com\reports\automation\HourlyReportGenerator.java

To run:
java -cp .;<path> com.reports.automation.HourlyReportGenerator

**Sample Output - The generated report includes:**
- URL name
- Status (Working / Not Working)
- HTTP status code
- Status description

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# FileBasedReportGenerator.java 
# Recommended Online compiler: OnlineGDB
**Purpose:**
Unlike the previous "HourlyReportGenerator.java", this script is designed for flexibility and speed. It removes the need to hardcode URLs into the Java source code, allowing system administrators to update the monitoring list simply by editing a text file.

**How it works:**
The program utilizes the java.nio.file library to interact with the file system, follows speific logic flow:
Locate: It looks for a file named urls.txt in the root directory.
Parse: It reads each line, skipping empty lines, and splits the data using a comma (,) as the delimiter.
Store: It loads the data into a LinkedHashMap to ensure the report order matches the file order.

**Configuration file:**
urls.txt - This file must follow a specific comma seperated value format.

**Steps to Run**:
- **Create the Java File:** Copy the code from FileBasedReportGenerator.java and paste it into the Main.java tab.
- **Create the Data File:** Click the "New File" icon (or '+' button), name it urls.txt, and paste your list of sites (Format: Name,URL).
- **Run:** Click the green Run button. The program will read from the virtual urls.txt and display the report in the bottom console.


