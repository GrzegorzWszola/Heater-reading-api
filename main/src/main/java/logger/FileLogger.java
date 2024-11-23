package logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class FileLogger {
    private static Logger logger;
    private static boolean isConfigured = false; // To ensure the logger is configured only once

    public FileLogger(String logFilePath) {
        deleteLogFile(logFilePath);
        if (!isConfigured) {
            configureLogger(logFilePath);
            isConfigured = true; // Mark the logger as configured
        }
    }

    // Delete the log file if it exists, ensuring fresh logs each time
    private void deleteLogFile(String logFilePath) {
        File logFile = new File(logFilePath);
        if (logFile.exists()) {
            logFile.delete(); // Deletes the log file to start fresh
        }
    }

    // Configure the logger with a file handler and a custom formatter
    private static void configureLogger(String logFilePath) {
        logger = Logger.getLogger(FileLogger.class.getName());

        // Remove the default console handler to prevent logs from being written to the console
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        try {
            // Check if logger already has a file handler, prevent adding multiple handlers
            Handler[] handlers = logger.getHandlers();
            if (handlers.length == 0) {
                // Create a file handler that appends to the log file
                FileHandler fileHandler = new FileHandler(logFilePath, true);
                fileHandler.setFormatter(new CustomLogFormatter()); // Set formatter for readability
                logger.addHandler(fileHandler);
                logger.setLevel(Level.ALL); // Log all levels
            }
        } catch (IOException e) {
            System.err.println("Failed to configure logger: " + e.getMessage());
        }
    }

    // Log an info message
    public void logInfo(String message) {
        logger.info(message);
    }

    // Log a warning message
    public void logWarning(String message) {
        logger.warning(message);
    }

    // Log a severe message
    public void logSevere(String message) {
        logger.severe(message);
    }
}
