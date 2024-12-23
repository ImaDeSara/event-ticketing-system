package com.example.ticketing;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private int vendorTicketNumber = 1; // Local ticket numbering for the vendor
    private boolean paused = false; // Flag to control thread execution
    private final int vendorId; // Unique Vendor ID
    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    // Pause the vendor thread
    public synchronized void pause() {
        paused = true;
    }

    // Resume the vendor thread
    public synchronized void resume() {
        paused = false;
        notifyAll(); // Notify the thread to resume processing
    }

    private synchronized void checkPaused() {
        while (paused) {
            try {
                wait(); // Wait until the thread is resumed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && TicketSystemCLI.isRunning()) {
            checkPaused(); // Pause the thread if needed
            try {
                ticketPool.addTickets(ticketReleaseRate, vendorId); // Add tickets with ID tracking
                // Logger.log("Vendor " + vendorId + " added " + ticketReleaseRate + " tickets.");
                Thread.sleep(6000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[LOG] Vendor " + vendorId + " thread interrupted.");
                return; // Exit thread
            }
        }
        System.out.println("[LOG] Vendor " + vendorId + " thread stopped.");
    }

}
