package com.example.ticketing;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TicketPool {
    private final LinkedList<String> tickets = new LinkedList<>();
    private final int maxCapacity;
    private int totalTickets;
    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;

        // Pre-fill the pool with the initial tickets
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add("Ticket " + i);
        }
    }

    public synchronized void addTickets(int numberOfTickets, int vendorId) {
        while (tickets.size() + numberOfTickets > maxCapacity) {
            try {
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[LOG] Vendor thread interrupted during addTickets.");
                return; // Exit if interrupted
            }
        }
        for (int i = 0; i < numberOfTickets; i++) {
            String ticket = "Ticket " + (tickets.size() + 1);
            tickets.add(ticket);
            // System.out.println("[LOG] Vendor " + vendorId + " added " + ticket);
            Logger.log("[LOG] Vendor " + vendorId + " added " + ticket);

        }
        notifyAll(); // Notify customers waiting for tickets
    }

    public synchronized void removeTicket(int customerId){
        while (tickets.isEmpty()) {
            try {
                wait();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[LOG] Customer thread interrupted during removeTicket.");
                return; // Exit if interrupted
            }
        }
        String ticket = tickets.removeFirst();
        // System.out.println("[LOG] Customer " + customerId + " purchased " + ticket);
        Logger.log("[LOG] Customer " + customerId + " purchased " + ticket);
        notifyAll(); // Notify vendors waiting to add tickets
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }

    public synchronized int getTotalTickets() {
        return totalTickets;
    }

}
