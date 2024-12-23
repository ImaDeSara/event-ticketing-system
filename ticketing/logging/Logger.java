package com.ticketing.logging;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Handles logging functionality for the ticketing system.
 * The logger writes logs to a file and provides a method to retrieve logs as a string.
 * It timestamps all log messages and ensures thread-safe operations for logging.
 */
@Component
public class Logger {
    /**
     * The file where all logs are stored.
     */
    private static final String LOG_FILE = "ticketing_logs.txt";
    /**
     * The formatter used to timestamp log messages in the format "yyyy-MM-dd HH:mm:ss".
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Logs a message to both the console and a log file, with a timestamp.
     * This method ensures thread safety while logging messages.
     * @param message The message to log.
     */
    public synchronized void log(String message) {
        String timestampedMessage = "[" + LocalDateTime.now().format(DATE_FORMATTER) + "] " + message;
        System.out.println(timestampedMessage);
        writeToFile(timestampedMessage);
    }
    /**
     * Writes a log message to the log file.
     * Appends the message to the log file, creating the file if it does not exist.
     * @param message The message to write to the file.
     */
    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write log to file: " + e.getMessage());
        }
    }
    /**
     * Retrieves all logs stored in the log file as a single string.
     * If the log file cannot be read, an error message is returned instead.
     * @return The contents of the log file, or an error message if the file cannot be read.
     */
    public String getLogs() {
        try {
            return Files.readString(Paths.get(LOG_FILE));
        } catch (IOException e) {
            return "[ERROR] Unable to retrieve logs: " + e.getMessage();
        }
    }
}