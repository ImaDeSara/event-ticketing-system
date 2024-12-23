package com.ticketing.controller;
import org.springframework.stereotype.Component;
/**
 * Data Transfer Object (DTO) class for managing ticketing system configuration requests.
 * This class maps the JSON request body from POST requests to Java objects,
 * allowing the server to handle and process the configuration of the ticketing system.
 * This class is a Spring-managed bean annotated with {@code @Component}.
 */
@Component
public class TicketRequest {
    /**
     * Total number of tickets in the system.
     */
    private int totalTickets;
    /**
     * Maximum capacity of tickets that can be held.
     */
    private int maxTicketCapacity;
    /**
     * Rate at which tickets are released into the system.
     */
    private int ticketReleaseRate;
    /**
     * Rate at which customers retrieve tickets.
     */
    private int customerRetrievalRate;
    /**
     * Interval (in minutes) between ticket releases.
     */
    private int releaseInterval;
    /**
     * Interval (in minutes) between customer retrieval actions.
     */
    private int retrievalInterval;
    /**
     * Number of vendor threads operating in the system.
     */
    private int noOfVendors;
    /**
     * Number of customer threads operating in the system.
     */
    private int noOfCustomers;
    /**
     * Retrieves the total number of tickets in the system.
     * @return Total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }
    /**
     * Sets the total number of tickets in the system.
     * @param totalTickets Total number of tickets.
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    /**
     * Retrieves the maximum ticket capacity.
     * @return Maximum ticket capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    /**
     * Sets the maximum ticket capacity.
     * @param maxTicketCapacity Maximum ticket capacity.
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
    /**
     * Retrieves the ticket release rate.
     * @return Ticket release rate.
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    /**
     * Sets the ticket release rate.
     * @param ticketReleaseRate Ticket release rate.
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }
    /**
     * Retrieves the customer retrieval rate.
     * @return Customer retrieval rate.
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    /**
     * Sets the customer retrieval rate.
     * @param customerRetrievalRate Customer retrieval rate.
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {this.customerRetrievalRate = customerRetrievalRate;}
    /**
     * Retrieves the ticket release interval.
     * @return Release interval in minutes.
     */
    public int getReleaseInterval() {return releaseInterval;}
    /**
     * Sets the ticket release interval.
     * @param releaseInterval Release interval in minutes.
     */
    public void setReleaseInterval(int releaseInterval) {this.releaseInterval = releaseInterval;}
    /**
     * Retrieves the ticket retrieval interval.
     * @return Retrieval interval in minutes.
     */
    public int getRetrievalInterval() {return retrievalInterval;}
    /**
     * Sets the ticket retrieval interval.
     * @param retrievalInterval Retrieval interval in minutes.
     */
    public void setRetrievalInterval(int retrievalInterval) {this.retrievalInterval = retrievalInterval;}
    /**
     * Retrieves the number of vendor threads.
     * @return Number of vendor threads.
     */
    public int getNoOfVendors() {return noOfVendors;}
    /**
     * Sets the number of vendor threads.
     * @param noOfVendors Number of vendor threads.
     */
    public void setNoOfVendors(int noOfVendors) {this.noOfVendors = noOfVendors;}
    /**
     * Retrieves the number of customer threads.
     * @return Number of customer threads.
     */
    public int getNoOfCustomers() {return noOfCustomers;}
    /**
     * Sets the number of customer threads.
     * @param noOfCustomers Number of customer threads.
     */
    public void setNoOfCustomers(int noOfCustomers) {this.noOfCustomers = noOfCustomers;}
}

