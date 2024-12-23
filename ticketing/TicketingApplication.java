package com.ticketing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
/**
 * The main class for the Ticketing Application.
 * This class serves as the entry point for the Spring Boot application.
 * It includes configuration for scanning entity classes in the specified package.
 */
@SpringBootApplication
@EntityScan(basePackages = "com.ticketing")  // Ensures the package containing entity classes like TransactionLog is scanned
public class TicketingApplication {
    /**
     * The main method to launch the Spring Boot application.
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);
    }
}

