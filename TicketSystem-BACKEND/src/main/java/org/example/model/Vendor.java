package org.example.model;

public class Vendor  implements Runnable{
    private String vendorId;
    private int totalTicket;
    private int ticketReleaseRate;
    private TicketPool ticketPool;

    public Vendor(String vendorId, int totalTicket, int ticketReleaseRate,TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.totalTicket = totalTicket;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
    }

    public String getVendorId() {
        return vendorId;
    }

    public int getTotalTicket() {
        return totalTicket;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }


    @Override
    public void run() {
        for (int i = 1; i <=totalTicket; i++) {
            Ticket ticket = new Ticket(String.format("T%03d", i), 500);
            ticketPool.add(ticket,this);
            try {
                Thread.sleep(ticketReleaseRate*1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
