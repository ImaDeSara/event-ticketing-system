package com.ticketing.thread;
import com.ticketing.logging.Logger;
import com.ticketing.pool.TicketPool;
/**
 * Represents a vendor thread that periodically adds tickets to the database.
 * This thread interacts with a shared ticket pool and database via {@link TicketPool}.
 * It runs independently, adding tickets based on the specified release rate and interval.
 */
public class VendorThread implements Runnable {
    private final int vendorId;
    private int ticketReleaseRate;
    private final int releaseInterval;
    private final int maxTicketCapacity;
    private final Logger logger;
    private boolean paused = false;
    private final TicketPool ticketPool;
    /**
     * Constructs a new VendorThread.
     * @param logger            Logger to log thread activity.
     * @param vendorId          Unique identifier for the vendor.
     * @param ticketReleaseRate Number of tickets released per interval.
     * @param releaseInterval   Time interval (in milliseconds) between ticket releases.
     * @param maxTicketCapacity Maximum capacity of the ticket pool.
     * @param ticketPool        Shared pool of tickets to manage availability.
     */
    public VendorThread(Logger logger, int vendorId, int ticketReleaseRate, int releaseInterval, int maxTicketCapacity, TicketPool ticketPool) {
        this.logger = logger;
        this.vendorId = vendorId;
        this.ticketReleaseRate = ticketReleaseRate;
        this.releaseInterval = releaseInterval;
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketPool = ticketPool;
    }
    /**
     * Executes the thread's main logic.
     * Periodically adds tickets to the ticket pool and database, and logs the operations.
     * Handles interruptions and pauses gracefully.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ticketPool.addTickets(ticketReleaseRate, vendorId, maxTicketCapacity); // Add tickets to the shared pool
                Thread.sleep( releaseInterval ); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log("[LOG] Vendor " + vendorId + " thread interrupted.");
                return;
            }
        }
        logger.log("[LOG] Vendor " + vendorId + " thread stopped.");
    }
}

