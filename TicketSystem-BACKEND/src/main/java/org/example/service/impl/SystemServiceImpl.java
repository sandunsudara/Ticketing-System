package org.example.service.impl;

import org.example.cli.ApplicationCLI;
import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;
import org.example.model.TicketPool;
import org.example.service.SystemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {

    private ApplicationCLI applicationContext;

    public SystemServiceImpl(ApplicationCLI applicationContext) {
        this.applicationContext = applicationContext;

    }

    @Override
    public Response saveSystemConfiguration(Configuration configuration) {
        return applicationContext.writeConfigToFile(configuration);
    }

    @Override
    public Response<Configuration> readConfiguration() {
        return applicationContext.readConfigFromFile();

    }

    @Override
    public void startSimulation(List<Customer> customerList, int noVendor) {

    }

    @Override
    public void startSimulation(int noCustomer, int noVendor) {
        Configuration configuration = applicationContext.readConfigFromFile().getData();
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity());
        applicationContext.setTicketPool(ticketPool);
        List<Thread> customerThread = applicationContext.autoGenerateCustomer(noCustomer, ticketPool, configuration.getCustomerRetrievalRate());
        List<Thread> vendorThread = applicationContext.autoGenerateVendor(noVendor, ticketPool, configuration);

        applicationContext.startSimulation(customerThread,vendorThread);

    }


}
