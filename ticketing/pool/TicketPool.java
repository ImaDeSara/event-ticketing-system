package com.ticketing.pool;
import com.ticketing.logging.Logger;
import java.util.LinkedList;
/**
 * Represents a pool of tickets that can be managed concurrently by vendors and customers.
 * The pool allows vendors to add tickets and customers to retrieve tickets, with synchronized methods
 * to ensure thread safety during operations.
 */
public class TicketPool implements java.io.Serializable {
    private LinkedList<String> tickets = new LinkedList<>();
    private final Logger logger;
    private final int maxTicketCapacity;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    /**
     * Constructs a TicketPool with the specified initial configuration.
     * @param logger               The logger used for logging operations.
     * @param maxTicketCapacity    The maximum capacity of tickets in the pool.
     * @param totalTickets         The initial number of tickets in the pool.
     * @param ticketReleaseRate    The rate at which vendors release tickets.
     * @param customerRetrievalRate The rate at which customers retrieve tickets.
     */
    public TicketPool(Logger logger, int maxTicketCapacity, int totalTickets, int ticketReleaseRate, int customerRetrievalRate) {
        this.logger = logger;
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;

        // Pre-fill the pool with the initial tickets
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add("Ticket " + i);
        }
    }
    /**
     * Adds tickets to the pool up to the specified release rate.
     * If the pool reaches its maximum capacity, the calling thread waits until space becomes available.
     * @param ticketReleaseRate The number of tickets to add.
     * @param vendorId          The ID of the vendor adding tickets.
     * @param maxTicketCapacity The maximum capacity of the pool.
     */
    public synchronized void addTickets(int ticketReleaseRate, int vendorId, int maxTicketCapacity) {
        while (tickets.size() + ticketReleaseRate > maxTicketCapacity) {
            try {
                wait();  // Wait if the pool is full
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log("[LOG] Vendor thread interrupted during addTickets.");
                return; // Exit if interrupted
            }
        }
        for (int i = 0; i < ticketReleaseRate; i++) {
            String ticket = "Ticket " + (tickets.size() + 1);
            tickets.add(ticket);
            totalTickets++;
            logger.log("[LOG] Vendor " + vendorId + " added " + ticket);
        }
        notifyAll(); // Notify waiting threads
    }
    /**
     * Removes tickets from the pool at the specified retrieval rate.
     * If the pool is empty, the calling thread waits until tickets are available.
     * @param customerRetrievalRate The number of tickets to retrieve.
     * @param customerId            The ID of the customer retrieving tickets.
     */
    public synchronized void removeTicket(int customerRetrievalRate, int customerId) {
        while (tickets.isEmpty()) {
            try {
                wait(); // Wait if no tickets are available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log("[LOG] Customer thread interrupted during removeTicket.");
                return; // Exit if interrupted
            }
        }
        for (int i = 0; i < customerRetrievalRate; i++) {
            String ticket = "Ticket " + (tickets.size() - 1);
            tickets.remove(ticket); //remove method can be used to remove a specific ticket, but it requires you to have the reference to that exact ticket object
            totalTickets--;
            logger.log("[LOG] Customer " + customerId + " purchased " + ticket);
        }
        notifyAll(); // Notify waiting threads
    }
    /**
     * Returns the current number of tickets available in the pool.
     * @return The number of tickets currently in the pool.
     */
    public synchronized int getTicketCount() {
        return tickets.size(); // tickets is the list of tickets // Number of available tickets in the pool
    }
    /**
     * Returns the total number of tickets managed by the pool so far.
     * @return The total number of tickets managed.
     */
    public synchronized int getTotalTickets() {
        return totalTickets;
    }
}

