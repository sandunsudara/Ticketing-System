package org.example.cli;

import com.google.gson.Gson;
import org.example.model.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents the command-line interface (CLI) for managing the TicketPlace System.
 * It provides functionalities to configure the system, run simulations, and manage customer and vendor actions.
 */
@Component
public class ApplicationCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private TicketPool ticketPool;
    private List<Thread> customerThreads;
    private List<Thread> vendorThreads;

    /**
     * The main method that initializes and runs the CLI menu.
     */
    public static void main(String[] args) throws IOException {
        ApplicationCLI app = new ApplicationCLI();
        app.menu();
    }
    /**
     * Displays the main menu of the application and handles user input.
     * It provides options for configuring the system, starting the simulation, or stopping the system.
     */
    public void menu() {
        boolean loop = true;
        while (loop) {
            System.out.println("\n+----------------------------------------------+\n|      Admin Panel For TicketPlace System      |\n+----------------------------------------------+");
            System.out.println("\n1. Configure System\n2. Start Simulation\n3. Stop System");
            System.out.print("\nEnter your choice : ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    configureSystem();
                    break;
                case "2":
                    simulationProgram();
                    break;
                case "3":
                    loop = false;
                    System.out.println("\nProgram terminated...");
                    break;
                default:
                    System.out.println("\tInvalid choice...");
            }

        }
    }
    
    public void configureSystem() {
        System.out.println("\n+----------------------------------+\n|         Configure System         |\n+----------------------------------+\n");


        int totalTickets = getValidInputForInteger("Enter total tickets available in system : ");
        int ticketReleaseRate = getValidInputForInteger("Enter ticket release rate (tickets per interval): ");
        int customerRetrievalRate = getValidInputForInteger("Enter customer retrieval rate (purchases per interval): ");
        int maxTicketCapacity = getValidInputForInteger("Enter max ticket capacity: ");

        Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        writeConfigToFile(config);

        System.out.println("Configuration complete.");

    }

    public Response writeConfigToFile(Configuration config) {
        Gson gson = new Gson();
        gson.toJson(config, Configuration.class);
        try {
            FileWriter fileWriter = new FileWriter("src/main/java/org/example/util/config.json");
            gson.toJson(config, fileWriter);
            fileWriter.close();
            return new Response<>(true, "success", "Configuration saved successfully.", null);
        } catch (IOException e) {
            return new Response<>(false, "error", "Failed to save configuration: " + e.getMessage(), null);
        }
    }


    public Response<Configuration> readConfigFromFile() {
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader("src/main/java/org/example/util/config.json")) {
            Configuration configuration = gson.fromJson(fileReader, Configuration.class);
            if (configuration != null) {
                return new Response<>(true, "success", "Configuration loaded successfully.", configuration);
            } else {
                return new Response<>(false, "error", "Configuration file is empty or invalid.", null);
            }
        } catch (FileNotFoundException e) {
            return new Response<>(false, "error", "Configuration file not found: " + e.getMessage(), null);
        } catch (IOException e) {
            return new Response<>(false, "error", "Error reading configuration file: " + e.getMessage(), null);
        }
    }

    private int getValidInputForInteger(String que) {
        int value;
        while (true) {
            try {
                System.out.print(que);
                value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    break;
                } else {
                    System.out.print("\nInvalid input, Enter the positive number, Try again..\n");
                }
            } catch (NumberFormatException e) {
                System.out.print("\nInvalid input, Try again..\n");
            }
        }
        return value;
    }

    public void simulationProgram() {
        // read config file
        Configuration configuration = readConfigFromFile().getData();
        if (configuration == null) {
            System.out.println("\tSystem Configuration Not Done");
            return;
        }
        System.out.println("\n==========      Enter the credential for simulation      ==========\n");

        // initialise ticketPool
        ticketPool = new TicketPool(configuration.getMaxTicketCapacity());

        // get Customer credential
        int numberOfCustomer = getValidInputForInteger("How many Customer are need to simulate : ");
        System.out.print("\tDo you want to auto generated Customer [Y/N] : ");
        String isAutoGenerate = scanner.nextLine();
        if (isAutoGenerate.equalsIgnoreCase("Y")) {
            customerThreads = autoGenerateCustomer(numberOfCustomer, ticketPool, configuration.getCustomerRetrievalRate());
        } else {
            customerThreads = getCustomerDetail(numberOfCustomer, ticketPool, configuration.getCustomerRetrievalRate());
        }

        // get Vendor credential
        System.out.print("\nHow many Vendor are need to simulate: ");
        int numberOfVendor = scanner.nextInt();
        vendorThreads = autoGenerateVendor(numberOfVendor, ticketPool, configuration);


        System.out.print("\nStart Simulation [Y/N] : ");
        String isStart = scanner.next();
        scanner.nextLine();
        if (isStart.equalsIgnoreCase("y")) {
            System.out.println("\n|...........Simulation Started ...........|");
            startSimulation(customerThreads, vendorThreads);
            boolean innerLoop = true;
            while (innerLoop) {
                System.out.print("\n\n==== Operation for Simulation ====\n \t1. Add another Customer\n\t2. Add another vendor\n\t3. Stop simulation\n\n Enter your choice : ");
                String value = scanner.next();
                scanner.nextLine();
                switch (value) {
                    case "1":
                        List<Thread> newCustomerThread = getCustomerDetail(1, ticketPool, configuration.getCustomerRetrievalRate());
                        newCustomerThread.forEach(Thread::start);
                        System.out.println("\nNew Customer added...");
                        break;
                    case "2":
                        List<Thread> newVendorThread = autoGenerateVendor(1, ticketPool, configuration);
                        newVendorThread.forEach(Thread::start);
                        System.out.println("\nNew Vendor added...");
                        break;
                    case "3":
                        vendorThreads.forEach(Thread::stop);
                        customerThreads.forEach(Thread::stop);
                        System.out.println("\n\tSimulation Stop ....");
                        innerLoop = false;
                        break;
                }
            }
        } else if (isStart.equalsIgnoreCase("n")) {
            System.out.println("Operation cancel");
        }
    }

    public void startSimulation(List<Thread> customerThreads, List<Thread> vendorThreads) {
        vendorThreads.forEach(Thread::start);
        customerThreads.forEach(Thread::start);

    }


    private List<Thread> getCustomerDetail(int numberOfCustomer, TicketPool ticketPool, int customerRetrievalRate) {
        List<Thread> customerThreadList = new ArrayList<>();
        for (int i = 1; i <= numberOfCustomer; i++) {
            String customerId = String.format("C%03d", i);
            System.out.println("\nEnter Details for customer : " + customerId + "\n");
            boolean isVip = false;
            while (true) {
                System.out.print("\tIs the customer VIP? (true/false): ");
                if (scanner.hasNextBoolean()) {
                    isVip = scanner.nextBoolean();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("\t\tInvalid input. Please enter true or false...\n");
                }
            }
            int numberOfTicket = getValidInputForInteger("\n\tHow many tickets do you want to purchase: ");
            Customer customer = new Customer(customerId, isVip, numberOfTicket, customerRetrievalRate, ticketPool);
            Thread thread = new Thread(customer, customer.getCustomerId());
            if (customer.getIsVip()) {
                thread.setPriority(Thread.MAX_PRIORITY);
            }
            customerThreadList.add(thread);

        }
        return customerThreadList;
    }

    public List<Thread> autoGenerateCustomer(int numberOfCustomer, TicketPool ticketPool, int customerRetrievalRate) {
        List<Thread> customerThreadList = new ArrayList<>();
        for (int i = 1; i <= numberOfCustomer; i++) {
            String customerId = String.format("C%02d", i);
            boolean isVip = random.nextBoolean();
            int numberOfTicket = random.nextInt(10) + 1;
            Customer customer = new Customer(customerId, isVip, numberOfTicket, customerRetrievalRate, ticketPool);
            customerThreadList.add(new Thread(customer, customer.getCustomerId()));
        }
        return customerThreadList;

    }

    public List<Thread> autoGenerateVendor(int numberOfVendor, TicketPool ticketPool, Configuration config) {
        List<Thread> vendorThreadList = new ArrayList<>();
        for (int i = 1; i <= numberOfVendor; i++) {
            String vendorId = String.format("V%02d", i);
            Vendor vendor = new Vendor(vendorId, config.getTotalTickets(), config.getTicketReleaseRate(), ticketPool);
            vendorThreadList.add(new Thread(vendor, vendor.getVendorId()));
        }
        return vendorThreadList;
    }

    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }
}