package org.example.model;


/**
 * A configuration class that holds the settings related to the ticket pool.
 */
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int  customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Constructor to initialize the configuration with the given parameters.
     * @param totalTickets the total number of tickets available
     * @param ticketReleaseRate the rate at which tickets are released into the pool
     * @param customerRetrievalRate the rate at which customers retrieve tickets
     * @param maxTicketCapacity the maximum capacity of the ticket pool
     */
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


}
