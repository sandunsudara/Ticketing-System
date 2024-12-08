package org.example.model;

public class Customer implements Runnable {
    private String customerId;
    private boolean isVip;
    private int numberOfTicket;
    private int customerRetrievalRate;
    private TicketPool ticketPool;

    public Customer(String customerId, boolean isVip,int numberOfTicket, int customerRetrievalRate, TicketPool ticketPool) {
        this.customerId = customerId;
        this.isVip = isVip;
        this.numberOfTicket = numberOfTicket;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketPool = ticketPool;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean getIsVip() {
        return isVip;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfTicket; i++) {
            Ticket ticket = ticketPool.remove(this);
            try {
                Thread.sleep(customerRetrievalRate*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
