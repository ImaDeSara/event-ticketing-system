Real-Time Ticketing System
Introduction
The Real-Time Ticketing System is a multi-threaded web application designed to manage ticket creation, distribution, and purchase operations. It features a configurable system that allows users to set up ticket parameters, monitor ticket status, view system logs, and control the ticketing process. It leverages multithreading and real-time updates to handle simultaneous customer and vendor interactions. 

Setup Instructions

Prerequisites

Ensure you have the following installed on your system:

Backend Prerequisites
Java Development Kit (JDK): Version 17 or higher
IntelliJ IDEA: For backend development
Maven: Included with Spring Boot for dependency management
MySQL: Version 8.0 or higher

Frontend Prerequisites
Node.js: Version 18.0 or higher
Angular CLI: Version 18.2.12
Visual Studio Code: For frontend development

Backend Setup

Setup in IntelliJ:

 -Open the backend directory as a project in IntelliJ.
 -Ensure the JDK version is set to 17 or above (File -> Project Structure -> SDK).
 -Run the Maven clean install goal:

  -Open the Maven tab on the right panel.
  -Navigate to Lifecycle -> clean and Lifecycle -> install.
  -Run these goals sequentially.

 -Once the build is successful, run the main class (e.g., TicketsSystemApplication.java).

Database Configuration:

 -Create a MySQL database named realTimeTicketingSystem.
 -Update application.properties with your database credentials:
spring.datasource.url=jdbc:mysql://localhost:3306/realTimeTicketingSystem
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword

Build and Run the Backend:

-Run the TicketingApplication class to start the server.
-The backend will be accessible at http://localhost:8080.

Verify Backend:

-Use Postman or a browser to test APIs, e.g., http://localhost:8080/api/tickets/count.

Frontend Setup in VS Code
Install Node.js and Angular CLI:

Verify installations:

 node -v
 npm install -g @angular/cli@18.2.12

Open the Frontend:

 Navigate to the frontend folder in Visual Studio Code.
 Install Dependencies:

Run:
 npm install

Run the Frontend:
Start the Angular application:
 ng serve --open

The frontend will open in your browser at http://localhost:4200.

Verify Frontend:
 Confirm the UI loads and displays the Configuration, Control Panel, Logs Display, and Ticket Status components.

Usage Instructions
  Configure and Start the System
    Open the frontend application at http://localhost:4200.
    Go to the Configuration tab.
    Fill in the required parameters:
      Total Tickets
      Max Ticket Capacity
      Ticket Release Rate
      Customer Retrieval Rate
      Release Interval (minutes)
      Retrieval Interval (minutes)
      Number of Vendors
      Number of Customers
    Click Submit to send the configuration to the backend.
    Navigate to the Control Panel tab and click Start System to begin ticketing operations.


Explanation of UI Controls
	Configuration Form
		Submit: Submits configuration to the backend.
		Save Config: Saves the configuration for future use.
		Load Config: Loads a saved configuration.
	Control Panel
		Start System: Begins ticketing operations.
		Stop System: Halts all active threads.
		Reset: Stops threads, clears the database, and resets input fields.
	Logs Display
		Shows logs of operations, including ticket creation and purchases.
	Ticket Status
		Displays the number of available tickets in real-time.

Project Structure

 Backend
  Controllers: API endpoints 
(src/main/java/com/ticketing/controller)
  Services: Business logic 
(src/main/java/com/ticketing/service)
  Models: Entities and custom classes 
(src/main/java/com/ticketing/model)
  Repositories: Database operations 
(src/main/java/com/ticketing/repository)

 Frontend

  Components:
   Configuration Panel (\src\app\components\configuration-form)
   Control Panel (src\app\components\control-panel)
   Log Display  (src\app\components\log-display)
   Ticket Status  (src\app\components\ticket-status)

  Routing: Configured in app.routes.ts.

API Endpoints
Base URL
http://localhost:8080/api/tickets

Endpoints
	Submit Configuration:
		POST /submit
		Request Body: JSON object with configuration details.
	Start Threads:
		POST /start - Starts vendor and customer threads.
	Stop Threads:
		POST /stop - Stops all active threads.
	Reset System:
		POST /reset - Clears tickets and stops operations.
	Get Ticket Count:
		GET /count - Returns the number of available tickets.
	Get Logs:
		GET /logs - Returns logs as an array of strings.
	Save Configuration:
		POST /saveConfig - Saves the current configuration.
	Load Configuration:
		GET /loadConfig - Retrieves the saved configuration.

Contributors
[Imasha De Saram]: Backend and Frontend Development

License
This project is licensed under the MIT License.



