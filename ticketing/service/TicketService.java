package com.ticketing.service;
import com.ticketing.controller.TicketRequest;
import com.ticketing.pool.TicketPool;
import com.ticketing.thread.VendorThread;
import com.ticketing.thread.CustomerThread;
import com.ticketing.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * Service class for managing the high-level operations of the ticketing system.
 * Responsibilities include starting/stopping threads, handling ticket operations,
 * saving and loading configurations, and managing logs.
 */
@Service
public class TicketService {
    private final TicketPool ticketPool;
    private final Logger logger;
    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private boolean running = false;
    private static final String CONFIG_FILE = "config.properties";
    /**
     * Constructor for injecting dependencies.
     * @param ticketPool    Shared ticket pool for managing ticket availability.
     * @param logger        Logger for logging operations.
     */
    @Autowired
    public TicketService(TicketPool ticketPool, Logger logger) {
        this.ticketPool = ticketPool;
        this.logger = logger;
    }
    /**
     * Starts vendor and customer threads to manage ticket release and retrieval.
     * @param totalTickets          Total number of tickets.
     * @param maxTicketCapacity     Maximum capacity of the ticket pool.
     * @param ticketReleaseRate     Number of tickets released per interval.
     * @param customerRetrievalRate Number of tickets retrieved by customers per interval.
     * @param releaseInterval       Interval (in milliseconds) for ticket release.
     * @param retrievalInterval     Interval (in milliseconds) for ticket retrieval.
     * @param noOfVendors           Number of vendor threads to start.
     * @param noOfCustomers         Number of customer threads to start.
     * @throws IllegalStateException if threads are already running.
     */
    @Transactional
    public synchronized void startThreads(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate, int releaseInterval, int retrievalInterval, int noOfVendors, int noOfCustomers) {
        if (running) throw new IllegalStateException("Threads are already running!");
        running = true;
        // each thread can run independently and simultaneously
        // Start multiple vendor threads to periodically add tickets
        for (int i = 1; i <= noOfVendors; i++) {
            VendorThread vendorThread = new VendorThread(logger, i, ticketReleaseRate, releaseInterval, maxTicketCapacity, ticketPool);
            Thread thread = new Thread(vendorThread);
            vendorThreads.add(thread); //All the created thread objects are stored in the vendorThreads list, which can be used later to manage or monitor these threads
            thread.start();
        }
        // Start multiple customer threads to purchase tickets
        for (int i = 1; i <= noOfCustomers; i++) {
            CustomerThread customerThread = new CustomerThread(logger, i, customerRetrievalRate, retrievalInterval, ticketPool);
            Thread thread = new Thread(customerThread);
            customerThreads.add(thread);
            thread.start();
        }
        logger.log("[LOG] Threads started successfully.");
    }
    /**
     * Stops all running threads.
     */
    @Transactional
    public synchronized void stopThreads() {
        vendorThreads.forEach(Thread::interrupt);
        customerThreads.forEach(Thread::interrupt);
        vendorThreads.clear();
        customerThreads.clear();
        running = false;
        logger.log("[LOG] All threads stopped.");
    }
    /**
     * Retrieves the logs from the logger.
     * @return Logs as a string.
     */
    @Transactional
    public String getLogs() {
        return logger.getLogs(); // Fetch logs from the logger
    }
    /**
     * Saves the configuration to a properties file.
     * @param totalTickets          Total number of tickets.
     * @param maxTicketCapacity     Maximum capacity of the ticket pool.
     * @param ticketReleaseRate     Number of tickets released per interval.
     * @param customerRetrievalRate Number of tickets retrieved by customers per interval.
     * @param releaseInterval       Interval (in milliseconds) for ticket release.
     * @param retrievalInterval     Interval (in milliseconds) for ticket retrieval.
     * @param noOfVendors           Number of vendor threads.
     * @param noOfCustomers         Number of customer threads.
     * @return A success message if the configuration is saved successfully.
     * @throws RuntimeException If an error occurs while saving the configuration.
     */
    @Transactional
    public synchronized String saveConfig(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate, int releaseInterval, int retrievalInterval, int noOfVendors, int noOfCustomers) {
        Properties properties = new Properties();
        try (FileOutputStream fos = new FileOutputStream("config.properties")) {
            properties.setProperty("totalTickets", String.valueOf(totalTickets));
            properties.setProperty("maxTicketCapacity", String.valueOf(maxTicketCapacity));
            properties.setProperty("ticketReleaseRate", String.valueOf(ticketReleaseRate));
            properties.setProperty("customerRetrievalRate", String.valueOf(customerRetrievalRate));
            properties.setProperty("releaseInterval", String.valueOf(releaseInterval));
            properties.setProperty("retrievalInterval", String.valueOf(retrievalInterval));
            properties.setProperty("noOfVendors", String.valueOf(noOfVendors));
            properties.setProperty("noOfCustomers", String.valueOf(noOfCustomers));
            properties.store(fos, "Ticket System Configuration");
            logger.log("[LOG] Configuration saved.");
            return "Configuration saved successfully!";
        } catch (IOException e) {
            logger.log("[LOG] Failed to save configuration: " + e.getMessage());
            throw new RuntimeException("Failed to save configuration", e);
        }
    }
    /**
     * Loads the configuration from a properties file.
     * @return A {@link TicketRequest} object containing the loaded configuration.
     * @throws RuntimeException If an error occurs while loading the configuration.
     */
    @Transactional
    public synchronized TicketRequest loadConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            TicketRequest config = new TicketRequest();
            config.setTotalTickets(Integer.parseInt(properties.getProperty("totalTickets", "0")));
            config.setMaxTicketCapacity(Integer.parseInt(properties.getProperty("maxTicketCapacity", "0")));
            config.setTicketReleaseRate(Integer.parseInt(properties.getProperty("ticketReleaseRate", "0")));
            config.setCustomerRetrievalRate(Integer.parseInt(properties.getProperty("customerRetrievalRate", "0")));
            config.setReleaseInterval(Integer.parseInt(properties.getProperty("releaseInterval", "0")));
            config.setRetrievalInterval(Integer.parseInt(properties.getProperty("retrievalInterval", "0")));
            config.setNoOfVendors(Integer.parseInt(properties.getProperty("noOfVendors", "0")));
            config.setNoOfCustomers(Integer.parseInt(properties.getProperty("noOfCustomers", "0")));
            logger.log("[LOG] Configuration loaded.");
            return config;
        } catch (IOException e) {
            logger.log("[LOG] Failed to load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}

