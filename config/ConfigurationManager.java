package com.example.ticketing.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// This class provides methods to write the object, save and load configurations
public class ConfigurationManager {
    private static final String CONFIG_FILE = "ticketing_config.txt";

    // Save the configuration to a plain text file
    public static void saveConfiguration(SystemConfig config) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write("totalTickets=" + config.getTotalTickets());
            writer.newLine();
            writer.write("maxCapacity=" + config.getMaxCapacity());
            writer.newLine();
            writer.write("ticketReleaseRate=" + config.getTicketReleaseRate());
            writer.newLine();
            writer.write("customerRetrievalRate=" + config.getCustomerRetrievalRate());
            writer.newLine();
            System.out.println("[LOG] Configuration saved successfully.");
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save configuration: " + e.getMessage());
        }
    }

    // Load the configuration from a plain text file
    public static SystemConfig loadConfiguration() {
        SystemConfig config = new SystemConfig();
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            Map<String, String> configMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }

            // Set the configuration values from the file
            config.setTotalTickets(Integer.parseInt(configMap.getOrDefault("totalTickets", "50")));
            config.setMaxCapacity(Integer.parseInt(configMap.getOrDefault("maxCapacity", "100")));
            config.setTicketReleaseRate(Integer.parseInt(configMap.getOrDefault("ticketReleaseRate", "5")));
            config.setCustomerRetrievalRate(Integer.parseInt(configMap.getOrDefault("customerRetrievalRate", "3")));

            System.out.println("[LOG] Configuration loaded successfully.");
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] Configuration file not found. Loading default configuration.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("[ERROR] Failed to load configuration: " + e.getMessage());
        }

        // Return the loaded configuration
        return config;
    }
}
