package org.example.model;

public class Ticket {
    private String ticketID;
    private int ticketPrice;

    public Ticket(String ticketID, int ticketPrice) {
        this.ticketID = ticketID;
        this.ticketPrice = ticketPrice;
    }

    public String getTicketID() {
        return ticketID;
    }
    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }


    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
