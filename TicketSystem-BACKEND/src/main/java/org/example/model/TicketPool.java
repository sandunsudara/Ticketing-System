package org.example.model;

import org.example.service.LoggerService;
import org.example.util.LogFormat;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;

public class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final Lock lock = new ReentrantLock();
    private final Condition ticketPoolEmpty = lock.newCondition();
    private final Condition ticketPoolFull = lock.newCondition();
    private int maxTicketCapacity;
    private Queue<Ticket> tickets;
    private PriorityQueue<Customer> priorityCustomer;
    private LoggerService loggerService;

    static {
        try {
            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }

            // Configure a file handler
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileHandler fileHandler = new FileHandler("src/main/java/org/example/util/ticket-pool.log", true);
            fileHandler.setFormatter(new LogFormat());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public TicketPool(int maxTicketCapacity, LoggerService loggerService) {
        this.maxTicketCapacity = maxTicketCapacity;
        tickets = new LinkedList<>();
        priorityCustomer = new PriorityQueue<>((c1 , c2)->{
            if(c1.getIsVip() && !c2.getIsVip()) return -1;
            if(!c1.getIsVip() && c2.getIsVip()) return 1;
            return 0;
        });
        this.loggerService = loggerService;
    }

    public void add(Ticket ticket, Vendor vendor) {
        lock.lock();
        try {
            while (tickets.size() >= maxTicketCapacity) {
                ticketPoolFull.await();
            }
            tickets.add(ticket);
            String message = "Added ticket [" + ticket.getTicketID() + "] BY Vendor [" + vendor.getVendorId() + "]\n";
            logger.log(Level.WARNING, message);
            sendLoggerToClient(message);
            ticketPoolEmpty.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    public  void remove(Customer customer) {
        lock.lock();
        try {
            priorityCustomer.offer(customer);
            while (tickets.isEmpty()) {
                ticketPoolEmpty.await();
            }
            Customer poll = priorityCustomer.poll();
            Ticket removedTicket = tickets.poll();
            String message = "Removed ticket [" + removedTicket.getTicketID() + "] BY Customer [" + poll.getCustomerId() + "]\n";
            logger.log(Level.WARNING, message);
            sendLoggerToClient(message);
            ticketPoolFull.signalAll();
        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
    }

    public void sendLoggerToClient(String message) {
        if(loggerService != null) {
            String substring = message.substring(0, message.length() - 1);
            loggerService.sendLoggerToClient(substring);
        }
    }
}