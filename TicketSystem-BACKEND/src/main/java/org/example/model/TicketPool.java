package org.example.model;

import org.example.util.LogFormat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.*;

public class TicketPool {
    private int maxTicketCapacity;
    private Queue<Ticket> tickets;
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    public TicketPool(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        tickets = new LinkedList<>();
    }

    static {
        try {
            // Configure a file handler
            FileHandler fileHandler = new FileHandler("src/main/java/org/example/util/ticket-pool.log", true);
            fileHandler.setFormatter(new LogFormat());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(Ticket ticket,Vendor vendor) {
        while (tickets.size() >= maxTicketCapacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        tickets.add(ticket);
        logger.log(Level.WARNING, "Added ticket ["+ticket.getTicketID()+"] BY Vendor ["+vendor.getVendorId()+"]\n");
        notifyAll();

    }

    public synchronized Ticket remove(Customer customer){
        while(tickets.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Ticket removedTicket = tickets.poll();
        logger.log(Level.WARNING, "Removed ticket ["+removedTicket.getTicketID()+"] BY Customer ["+customer.getCustomerId()+"]\n");
        notifyAll();
        return removedTicket;
    }
}
