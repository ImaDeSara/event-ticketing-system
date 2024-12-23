package com.ticketing.controller;

import com.ticketing.logging.Logger;
import com.ticketing.pool.TicketPool;
import com.ticketing.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
/**
 * REST controller for managing ticketing system operations.
 * This controller provides endpoints to configure, start, stop, reset, and retrieve
 * information about the ticketing system.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    private TicketRequest ticketRequest;
    private final TicketPool ticketPool;
    private final Logger logger;
    /**
     * Constructs a new instance of {@code TicketController}.
     *
     * @param ticketService The service managing ticketing system operations.
     * @param ticketPool
     * @param logger        Logger for recording system events.
     */
    public TicketController(TicketService ticketService, TicketPool ticketPool, Logger logger) {
        this.ticketService = ticketService;
        this.ticketPool = ticketPool;
        this.logger = logger;
    }
    /**
     * Endpoint to submit the system configuration.
     * @param request The {@code TicketRequest} containing configuration details.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/submit")
    public ResponseEntity<String> submit(@RequestBody TicketRequest request) {
        try {
            this.ticketRequest = request; // Store the received TicketRequest
            return ResponseEntity.ok("Configuration submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting configuration");
        }
    }
    /**
     * Endpoint to start the ticketing system threads.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startThreads() {
        try {
            ticketService.startThreads(ticketRequest.getTotalTickets(), ticketRequest.getMaxTicketCapacity(),
                    ticketRequest.getTicketReleaseRate(), ticketRequest.getCustomerRetrievalRate(), ticketRequest.getReleaseInterval()*60000,
                    ticketRequest.getRetrievalInterval()*60000, ticketRequest.getNoOfVendors(), ticketRequest.getNoOfCustomers());
            return ResponseEntity.ok("Threads started successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /**
     * Endpoint to stop all ticketing system threads.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopThreads() {
        try {
            ticketService.stopThreads();
            return ResponseEntity.ok("Threads stopped successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to stop threads: " + e.getMessage());
        }
    }
    /**
     * Endpoint to retrieve the current ticket count.
     * @return The number of tickets available in the system.
     */
    @GetMapping("/count")
    public int getTicketCount() {
        return ticketPool.getTicketCount();
    }
    /**
     * Endpoint to retrieve system logs.
     * @return A list of log messages.
     */
    @GetMapping("/logs")
    public List<String> getLogs() {
        String rawLogs = ticketService.getLogs();
        // Split the raw log content into lines, trim whitespace, and remove any empty lines
        return Arrays.stream(rawLogs.split("\r?\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }
    /**
     * Endpoint to reset the ticketing system.
     * @return A response indicating the success or failure of the reset operation.
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetSystem() {
        try {
            ticketService.stopThreads();
            if (ticketRequest != null) {
                ticketRequest.setTotalTickets(0);
                ticketRequest.setMaxTicketCapacity(0);
                ticketRequest.setTicketReleaseRate(0);
                ticketRequest.setCustomerRetrievalRate(0);
                ticketRequest.setReleaseInterval(0);
                ticketRequest.setRetrievalInterval(0);
                ticketRequest.setNoOfVendors(0);
                ticketRequest.setNoOfCustomers(0);
            }
            logger.log("System reset successful");
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body("System reset successfully!");
        } catch (Exception e) {
            logger.log("Error resetting the system: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "text/plain")
                    .body("Error resetting the system: " + e.getMessage());
        }
    }
    /**
     * Endpoint to save the current system configuration.
     * @param request The {@code TicketRequest} containing the configuration details.
     * @return A response indicating the success or failure of the save operation.
     */
    @PostMapping("/saveConfig")
    public ResponseEntity<String> saveConfig(@RequestBody TicketRequest request) {
        try {
            String responseMessage = ticketService.saveConfig(
                    request.getTotalTickets(),
                    request.getMaxTicketCapacity(),
                    request.getTicketReleaseRate(),
                    request.getCustomerRetrievalRate(),
                    request.getReleaseInterval(),
                    request.getRetrievalInterval(),
                    request.getNoOfVendors(),
                    request.getNoOfCustomers()
            );
            return ResponseEntity.ok(responseMessage); // Return plain text response
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    /**
     * Endpoint to load the saved system configuration.
     * @return The saved {@code TicketRequest} or an error response.
     */
    @GetMapping("/loadConfig")
    public ResponseEntity<TicketRequest> loadConfig() {
        try {
            TicketRequest config = ticketService.loadConfig();
            return ResponseEntity.ok(config); // Return JSON response
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

