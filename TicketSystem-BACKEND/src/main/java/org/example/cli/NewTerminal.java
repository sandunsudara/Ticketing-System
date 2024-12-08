package org.example.cli;

import java.io.IOException;

public class NewTerminal {
    public static void main(String[] args) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String command = "open -a Terminal --args 'java LoggerApp'";
        runtime.exec(command);
    }
}