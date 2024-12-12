package org.example.cli;

import com.google.gson.Gson;
import org.example.model.*;
import org.example.service.LoggerService;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

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

    /**
     * Method use for prompt the user to system configuration details and writes them to a configuration file.
     */
    public void configureSystem() {
        System.out.println("\n+----------------------------------+\n|         Configure System         |\n+----------------------------------+\n");


        int totalTickets = getValidInputForInteger("Enter total tickets for each vendor : ");
        int ticketReleaseRate = getValidInputForInteger("Enter ticket release rate (tickets per interval): ");
        int customerRetrievalRate = getValidInputForInteger("Enter customer retrieval rate (purchases per interval): ");
        int maxTicketCapacity = getValidInputForInteger("Enter max ticket capacity: ");

        Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        writeConfigToFile(config);

        System.out.println("Configuration complete.");

    }

    /**
     * Writes the system configuration to a JSON file.
     *
     * @param config The configuration object to be written to the file.
     * @return A Response object indicating the result of the file write operation.
     */
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


    /**
     * Reads the system configuration from a JSON file.
     *
     * @return A Response object containing the configuration data or an error message.
     */
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

    /**
     * Get valid integer input from user.
     *
     * @param que The question or prompt displayed to the user.
     * @return The valid integer input provided by the user.
     */
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

    /**
     * Starts the simulation by initializing the ticket pool and generating customers and vendors.
     * It also manages the simulation loop for adding new customers or vendors and stopping the simulation.
     */
    public void simulationProgram() {
        // read config file
        Configuration configuration = readConfigFromFile().getData();
        if (configuration == null) {
            System.out.println("\tSystem Configuration Not Done");
            return;
        }
        System.out.println("\n==========      Enter the credential for simulation      ==========\n");

        // initialise ticketPool & customer & vendor thread list
        initialise(configuration.getMaxTicketCapacity(), null);


        // get Customer credential
        int numberOfCustomer = getValidInputForInteger("How many Customer are need to simulate : ");
        System.out.print("\tDo you want to auto generated Customer [Y/N] : ");
        String isAutoGenerate = scanner.nextLine();
        if (isAutoGenerate.equalsIgnoreCase("Y")) {
            autoGenerateCustomer(numberOfCustomer, configuration.getCustomerRetrievalRate());
        } else {
            getCustomerDetail(numberOfCustomer, configuration.getCustomerRetrievalRate());
        }

        // get Vendor credential
        System.out.print("\nHow many Vendor are need to simulate: ");
        int numberOfVendor = scanner.nextInt();
        autoGenerateVendor(numberOfVendor, configuration);


        System.out.print("\nStart Simulation [Y/N] : ");
        String isStart = scanner.next();
        scanner.nextLine();
        if (isStart.equalsIgnoreCase("y")) {
            System.out.println("\n|...........Simulation Started ...........|");
            startSimulation();
            boolean innerLoop = true;
            while (innerLoop) {
                System.out.print("\n\n==== Operation for Simulation ====\n \t1. Add another Customer\n\t2. Add another vendor\n\t3. Stop simulation\n\n Enter your choice : ");
                String value = scanner.next();
                scanner.nextLine();
                switch (value) {
                    case "1":
                        getCustomerDetail(1, configuration.getCustomerRetrievalRate()).forEach(thread -> {
                           customerThreads.add(thread);
                           thread.start();
                        });
                        System.out.println("\nNew Customer added...");
                        break;
                    case "2":
                        autoGenerateVendor(1, configuration);
                        startSimulation();
                        System.out.println("\nNew Vendor added...");
                        break;
                    case "3":
                        try {
                            stopSimulation();
                            System.out.println("\n\tSimulation Stop ....");
                            innerLoop = false;
                        } catch (Exception e) {
                            System.out.println("Error in stop simulation - ["+e.getMessage()+"]");
                        }
                        break;
                }
            }
        } else if (isStart.equalsIgnoreCase("n")) {
            System.out.println("Operation cancel");
        }
    }

    /**
     * Starts the simulation by starting the customer and vendor threads.
     */
    public void startSimulation() {
        this.vendorThreads.forEach(thread -> {
            if (!thread.isAlive()) {
                thread.start();
            }
        });

        customerThreads.forEach(thread -> {
            if (!thread.isAlive()) {
                thread.start();
            }
        });

    }

    public void stopSimulation() throws Exception {
        try {
            this.vendorThreads.forEach(thread -> {
                thread.stop();
            });

            this.customerThreads.forEach(thread -> {
                thread.stop();
            });
        } catch (Exception e) {
            throw new Exception("Error stopping threads: " + e.getMessage(), e);
        }
    }


    /**
     * Prompts the user for customer details and returns a list of customer threads based on the input.
     *
     * @param numberOfCustomer      The number of customers to simulate.
     * @param customerRetrievalRate The rate at which customers retrieve tickets.
     * @return A list of customer threads.
     */
    private List<Thread> getCustomerDetail(int numberOfCustomer, int customerRetrievalRate) {
        List<Thread> threads = new ArrayList<>();
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
            Customer customer = new Customer(customerId, isVip, numberOfTicket, customerRetrievalRate, this.ticketPool);
            Thread thread = new Thread(customer, customer.getCustomerId());
            if (customer.getIsVip()) {
                thread.setPriority(Thread.MAX_PRIORITY);
            }
            threads.add(thread);
        }
        return threads;
    }

    /**
     * Generates a list of customer threads with randomly generated details.
     * The number of tickets and VIP status are randomly assigned for each customer.
     *
     * @param numberOfCustomer      The number of customer threads to generate.
     * @param customerRetrievalRate The rate at which customers will attempt to retrieve tickets.
     * @return A list of customer threads.
     */
    public void autoGenerateCustomer(int numberOfCustomer, int customerRetrievalRate) {
        for (int i = 1; i <= numberOfCustomer; i++) {
            String customerId = String.format("C%02d", i);
            boolean isVip = random.nextBoolean();
            int numberOfTicket = random.nextInt(10) + 1;
            Customer customer = new Customer(customerId, isVip, numberOfTicket, customerRetrievalRate, ticketPool);
            this.customerThreads.add(new Thread(customer, customer.getCustomerId()));
        }

    }

    /**
     * Generates a list of vendor threads with randomly generated details.
     * Each vendor is assigned a unique ID and is provided with the configuration
     * values for total tickets and ticket release rate.
     *
     * @param numberOfVendor The number of vendor threads to generate.
     * @param config         The configuration containing total tickets and ticket release rate.
     */
    public void autoGenerateVendor(int numberOfVendor, Configuration config) {
        for (int i = 1; i <= numberOfVendor; i++) {
            String vendorId = String.format("V%02d", i);
            Vendor vendor = new Vendor(vendorId, config.getTotalTickets(), config.getTicketReleaseRate(), this.ticketPool);
            this.vendorThreads.add(new Thread(vendor, vendor.getVendorId()));
        }
    }

    public void addCustomerToThreadList(Customer customer) {
        customer.setTicketPool(this.ticketPool);
        this.customerThreads.add(new Thread(customer, customer.getCustomerId()));
    }



    public Response<Object> addCustomerAndRun(Customer customer) {
        try {
            customer.setTicketPool(this.ticketPool);
            Thread thread = new Thread(customer, customer.getCustomerId());
            this.customerThreads.add(thread);
            thread.start();
            return new Response<>(true, "good", "Customer add successfully", null);

        } catch (Exception e) {
            return new Response<>(false, "error", e.getMessage(), null);

        }

    }

    public Response<Object> addVendorAndRun(Vendor vendor) {
        try {
            vendor.setTicketPool(this.ticketPool);
            Thread thread = new Thread(vendor, vendor.getVendorId());
            this.vendorThreads.add(thread);
            thread.start();
            return new Response<>(true, "good", "Vendor add successfully", null);
        } catch (Exception e) {
            return new Response<>(false, "error", e.getMessage(), null);
        }

    }


    public void initialise(int maxTicketCapacity, LoggerService loggerService) {
        this.ticketPool = new TicketPool(maxTicketCapacity, loggerService);
        this.vendorThreads = new LinkedList<>();
        this.customerThreads = new LinkedList<>();
    }

    public int getCustomerThreadLength() {
        return this.customerThreads.size();
    }

    public int getVendorThreadLength() {
        return this.vendorThreads.size();
    }

}