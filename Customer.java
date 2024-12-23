package com.example.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private boolean paused = false; // Flag to control thread execution
    private final int customerId; // Unique Customer ID
    public Customer(TicketPool ticketPool, int customerRetrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerId;
    }

    // Pause the customer thread
    public synchronized void pause() {
        paused = true;
    }

    // Resume the customer thread
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
                ticketPool.removeTicket(customerId); // Remove ticket with ID tracking // Consume tickets
                // Logger.log("Customer " + customerId + " purchased a ticket.");
                Thread.sleep(4000 / customerRetrievalRate); // Control retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[LOG] Customer " + customerId + " thread interrupted.");
                return; // Exit thread
            }
        }
        System.out.println("[LOG] Customer " + customerId + " thread stopped.");
    }


}

