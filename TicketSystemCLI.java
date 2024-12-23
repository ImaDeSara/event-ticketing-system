package com.example.ticketing;

import com.example.ticketing.config.ConfigurationManager;
import com.example.ticketing.config.SystemConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketSystemCLI {
    private static TicketPool ticketPool;
    private static Thread vendorThread;
    private static Thread customerThread;
    private static Thread monitoringThread;
    private static Vendor vendor;
    private static Customer customer;
    private static boolean running = false;
    // AtomicInteger for Unique ID Generation
    private static final AtomicInteger vendorIdGenerator = new AtomicInteger(1);
    private static final AtomicInteger customerIdGenerator = new AtomicInteger(1);

    // Lists to manage all vendor and customer threads
    private static final List<Vendor> vendors = new ArrayList<>();
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Collect system configuration
        System.out.println("Welcome to the Ticketing System CLI");
        System.out.print("Enter total tickets: ");
        int totalTickets = scanner.nextInt();

        System.out.print("Enter max ticket capacity: ");
        int maxCapacity = scanner.nextInt();

        System.out.print("Enter ticket release rate: ");
        int ticketReleaseRate = scanner.nextInt();

        System.out.print("Enter customer retrieval rate: ");
        int customerRetrievalRate = scanner.nextInt();

        // Initialize TicketPool
        ticketPool = new TicketPool(maxCapacity, totalTickets);

        // Command interface
        while (true) {
            System.out.println("\nCommands: start | stop | status | saveconfig | loadconfig | logs | exit");
            System.out.print("Enter command: ");
            String command = scanner.next().toLowerCase();

            switch (command) {
                case "start":
                    running = true;
                    resumeThreads();
                    // generate unique IDs using the vendorIdGenerator and customerIdGenerator
                    // create multiple threads dynamically at runtime, simulate multiple vendors and customers concurrently, each with unique vendorId/customerId, operating independently
                    // Start multiple vendors
                    for (int i = 1; i <= ticketReleaseRate; i++) {
                        Vendor vendor = new Vendor(ticketPool, ticketReleaseRate, vendorIdGenerator.getAndIncrement());
                        Thread vendorThread = new Thread(vendor);
                        vendors.add(vendor);
                        vendorThreads.add(vendorThread);
                        vendorThread.start();
                    }
                    // Start multiple customers
                    for (int i = 1; i <= customerRetrievalRate; i++) {
                        Customer customer = new Customer(ticketPool, customerRetrievalRate, customerIdGenerator.getAndIncrement());
                        Thread customerThread = new Thread(customer);
                        customers.add(customer);
                        customerThreads.add(customerThread);
                        customerThread.start();
                    }

                    // Initializing the monitoring thread
                    initializeMonitoringThread();
                    System.out.println("System started!");

                    break;

                case "stop":
                    if (running) {
                        running = false;
                        // Pause all vendor and customer threads
                        pauseThreads();
                        System.out.println("System stopped!");
                    } else {
                        System.out.println("System is not running!");
                    }
                    break;

                case "status":
                    // System.out.println("Current ticket count: " + ticketPool.getTicketCount());
                    Logger.log("Current ticket count: " + ticketPool.getTicketCount());                    break;

                case "saveconfig":
                    SystemConfig saveConfig = new SystemConfig(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);
                    ConfigurationManager.saveConfiguration(saveConfig);
                    break;

                case "loadconfig":
                    SystemConfig loadConfig = ConfigurationManager.loadConfiguration();
                    if (loadConfig != null) {
                        totalTickets = loadConfig.getTotalTickets();
                        maxCapacity = loadConfig.getMaxCapacity();
                        ticketReleaseRate = loadConfig.getTicketReleaseRate();
                        customerRetrievalRate = loadConfig.getCustomerRetrievalRate();

                        ticketPool = new TicketPool(maxCapacity, totalTickets); // Reinitialize TicketPool
                        // System.out.println("[LOG] Configuration loaded into the system.");
                        Logger.log("[LOG] Configuration loaded into the system.");
                    }
                    break;
                case "logs":
                    try (BufferedReader reader = new BufferedReader(new FileReader("system_logs.txt"))) {
                        System.out.println("Log file content:");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        System.err.println("[ERROR] Failed to read log file: " + e.getMessage());
                    }
                    break;

                case "exit":
                    running = false; // Stop monitoring thread
                    // pauseThreads(); // Pause vendor and customer threads

                    // Interrupt all threads
                    if (monitoringThread != null) monitoringThread.interrupt();
                    vendorThreads.forEach(Thread::interrupt);
                    customerThreads.forEach(Thread::interrupt);

                    // Wait for threads to finish
                    try {
                        if (vendorThread != null) vendorThread.join();
                        if (customerThread != null) customerThread.join();
                        if (monitoringThread != null) monitoringThread.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Handle interrupted exception
                        // System.err.println("[ERROR] Thread termination interrupted.");
                        Logger.log("[ERROR] Thread termination interrupted.");
                    }

                    // System.out.println("Exiting system...");
                    Logger.log("Exiting system...");
                    scanner.close(); // Close scanner to free resources
                    System.exit(0); // Ensure termination
                    return;

                default:
                    // System.out.println("Invalid command. Try again.");
                    Logger.log("Invalid command. Try again.");
            }
        }
    }

    // Initialize Monitoring thread
    private static void initializeMonitoringThread() {
        if (monitoringThread != null && monitoringThread.isAlive()) {
            monitoringThread.interrupt(); // Stop the previous thread if running
        }
        // Create a new monitoring thread
        monitoringThread = new Thread(() -> {
            // System.out.println("[LOG] Monitoring thread started.");
            Logger.log("Monitoring thread started.");
            while (running) {
                synchronized (System.out) { // Prevent log interleaving
                    System.out.println("[STATUS] Tickets in pool: " + ticketPool.getTicketCount());
                }
                try {
                    Thread.sleep(1000); // Log every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // System.out.println("[LOG] Monitoring thread interrupted.");
                    Logger.log("Monitoring thread interrupted.");
                    return; // Exit thread
                }
            }
            // System.out.println("[LOG] Monitoring thread stopped.");
            Logger.log("Monitoring thread stopped.");
        });
        monitoringThread.start();
    }

    // Pause all the threads
    // iterate through all vendors and customers in the respective lists
    private static void pauseThreads() {
        vendors.forEach(Vendor::pause);
        customers.forEach(Customer::pause);
    }

    // Resume all the threads
    // iterate through all vendors and customers in the respective lists
    private static void resumeThreads() {
        vendors.forEach(Vendor::resume);
        customers.forEach(Customer::resume);
    }

    public static boolean isRunning() {
        return running;
    }

}
