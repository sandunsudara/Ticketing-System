package org.example.cli;

import java.io.IOException;
import java.util.logging.*;
import java.nio.file.*;

public class OpenTerminalAndLog {
    public static void main(String[] args) {
        // Set up logger
        Logger logger = Logger.getLogger(OpenTerminalAndLog.class.getName());
        FileHandler fileHandler;
        try {
            // Write logs to a file
            fileHandler = new FileHandler("src/main/java/org/example/cli/application.log", true);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);

            // Set the logging level and format
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Log some messages
            logger.info("Application started");
            logger.warning("This is a warning!");
            logger.severe("This is an error message!");

            // Start the terminal to tail the log file
            startTerminalToMonitorLogFile();

            // Simulate application log generation
            for (int i = 0; i < 5; i++) {
                Thread.sleep(2000);  // Simulate some work
                logger.info("Logging message #" + (i + 1));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startTerminalToMonitorLogFile() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = "";

            if (os.contains("win")) {
                // Windows - Use PowerShell to monitor the log file
                command = "powershell Get-Content application.log -Wait";
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Linux/macOS - Use tail to monitor the log file
                System.out.println("mac");
                command = "open -a Terminal -f \"src/main/java/org/example/cli/application.log\"";
            }

            // Run the terminal command to monitor the log file
            if (!command.isEmpty()) {
                System.out.println("saa");
                Runtime.getRuntime().exec(command);
            } else {
                System.out.println("Unsupported OS for terminal monitoring.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
