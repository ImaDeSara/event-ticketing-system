package com.ticketing.thread;
import com.ticketing.logging.Logger;
import com.ticketing.pool.TicketPool;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
/**
 * Represents a customer thread that attempts to fetch and purchase tickets.
 * The thread interacts with a shared {@link TicketPool} to retrieve tickets
 * and to purchase tickets. It handles concurrency
 * issues like locking and retries to avoid conflicts during database operations.
 */
public class CustomerThread implements Runnable {
    private final int customerId;
    private int customerRetrievalRate;
    private final int retrievalInterval;
    private final Logger logger;
    private boolean paused = false;
    private final TicketPool ticketPool;
    /**
     * Constructs a new CustomerThread.
     * @param logger               Logger for logging thread activity.
     * @param customerId           Unique identifier for the customer.
     * @param customerRetrievalRate Number of tickets to retrieve per interval.
     * @param retrievalInterval    Time interval (in milliseconds) between ticket retrievals.
     * @param ticketPool           Shared pool of tickets to manage availability.
     */
    public CustomerThread(Logger logger, int customerId, int customerRetrievalRate, int retrievalInterval, TicketPool ticketPool) {
        this.logger = logger;
        this.customerId = customerId;
        this.customerRetrievalRate = customerRetrievalRate;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
    }
    /**
     * Executes the thread's main logic.
     * Periodically attempts to retrieve and purchase tickets from the shared
     * {@link TicketPool} and database. Handles concurrency conflicts by retrying
     * operations on failures due to locking issues.
     */
    @Transactional
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
                try {
                    ticketPool.removeTicket(customerRetrievalRate, customerId); // Retrieve tickets from the shared pool
                    Thread.sleep( retrievalInterval );
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve the interrupt flag
                    logger.log("[LOG] Customer " + customerId + " thread interrupted.");
                }
        }
        logger.log("[LOG] Customer " + customerId + " thread stopped.");
    }
}