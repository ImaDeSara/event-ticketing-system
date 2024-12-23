package com.ticketing.config;

import com.ticketing.controller.TicketRequest;
import com.ticketing.logging.Logger;
import com.ticketing.pool.TicketPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Provides application-wide configurations and bean definitions.
 * This configuration initializes shared resources like the {@link TicketPool}
 * using values from {@link TicketRequest} and logs the initialization process using {@link Logger}.
 */
@Configuration
public class AppConfig {
    private final TicketRequest ticketRequest;
    private final Logger logger;
    /**
     * Constructs an instance of {@link AppConfig}.
     * @param ticketRequest A {@link TicketRequest} instance containing the system's ticket-related configuration.
     * @param logger        A {@link Logger} instance to log initialization details.
     */
    public AppConfig(TicketRequest ticketRequest, Logger logger) {
        this.ticketRequest = ticketRequest;
        this.logger = logger;
    }
    /**
     * Creates a {@link TicketPool} bean configured with parameters from {@link TicketRequest}.
     * The {@link TicketPool} is initialized with the maximum ticket capacity, total tickets,
     * ticket release rate, and customer retrieval rate.
     * @return A configured {@link TicketPool} instance.
     */
    @Bean
    public TicketPool ticketPool() {
        // Use values from TicketRequest to initialize TicketPool
        return new TicketPool(logger, ticketRequest.getMaxTicketCapacity(), ticketRequest.getTotalTickets(), ticketRequest.getTicketReleaseRate(), ticketRequest.getCustomerRetrievalRate());
    }
}
