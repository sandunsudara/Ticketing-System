package org.example.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.*;
import java.nio.file.*;

public class OpenTerminalAndLog {
    public static void main(String[] args) {
        // Specify the path of the log file
        String logFilePath = "src/main/java/org/example/util/ticket-pool.log";

        // Open the log file for reading
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(logFilePath))) {
            String line;
            // Continuously read the log file
            while (true) {
                // Check if a new line is available
                if ((line = reader.readLine()) != null) {
                    // Print the new line from the log file
                    System.out.println(line);
                } else {
                    // Sleep for a while before checking again if the file has been updated
                    try {
                        Thread.sleep(1000); // 1 second delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the log file: " + e.getMessage());
        }
    }
}
