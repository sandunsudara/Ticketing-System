package org.example.service.impl;

import org.example.cli.ApplicationCLI;
import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;
import org.example.model.Vendor;
import org.example.service.LoggerService;
import org.example.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService {

    private ApplicationCLI applicationContext;
    @Autowired
    private LoggerService loggerService;

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
    public Response<Object> startSimulation(List<Customer> customerList, int noVendor) {
        if (loggerService.checkSession()) {
            Configuration configuration = applicationContext.readConfigFromFile().getData();
            applicationContext.initialise(configuration.getMaxTicketCapacity(), loggerService);
            applicationContext.autoGenerateVendor(noVendor, configuration);
            customerList.forEach(customer -> {
                customer.setCustomerRetrievalRate(configuration.getCustomerRetrievalRate());
                applicationContext.addCustomerToThreadList(customer);
            });
            applicationContext.startSimulation();
            return new Response<>(true, "good", "Simulation started", null);
        } else {
            return new Response<>(false, "error", "Web socket Session closed. reload page", null);
        }

    }

    @Override
    public Response<Object> startSimulation(int noCustomer, int noVendor) {
        if (loggerService.checkSession()) {
            Configuration configuration = applicationContext.readConfigFromFile().getData();
            applicationContext.initialise(configuration.getMaxTicketCapacity(), loggerService);
            applicationContext.autoGenerateCustomer(noCustomer, configuration.getCustomerRetrievalRate());
            applicationContext.autoGenerateVendor(noVendor, configuration);
            applicationContext.startSimulation();
            return new Response<>(true, "good", "Simulation started", null);

        } else {
            return new Response<>(false, "error", "Web socket Session closed", null);
        }

    }

    @Override
    public Response<Object> stopSimulation() {
        try {
            applicationContext.stopSimulation();
            return new Response<>(true,"good","Simulation Stoped",null);
        } catch (Exception e) {
            return new Response<>(false,"error",e.getMessage(),null);
        }
    }

    @Override
    public Response<Object> addCustomer(Customer customer) {
        Configuration configuration = applicationContext.readConfigFromFile().getData();
        customer.setCustomerId("C-"+(applicationContext.getCustomerThreadLength()+1));
        customer.setCustomerName("Customer "+applicationContext.getCustomerThreadLength()+1);
        customer.setCustomerRetrievalRate(configuration.getCustomerRetrievalRate());
        return applicationContext.addCustomerAndRun(customer);

    }

    @Override
    public Response<Object> addVendor() {
        Configuration configuration = applicationContext.readConfigFromFile().getData();
        Vendor vendor =new Vendor("V-"+(applicationContext.getVendorThreadLength()+1),configuration.getTotalTickets(),configuration.getTicketReleaseRate(),null);
       return  applicationContext.addVendorAndRun(vendor);

    }

    @Override
    public Response<Map<String, Integer>> getCredentials() {
        Map<String, Integer> credentials = new HashMap<>();
        credentials.put("vendorThreadLength", applicationContext.getVendorThreadLength());
        credentials.put("customerThreadLength", applicationContext.getCustomerThreadLength());
        return new Response<Map<String, Integer>>(true,"good","all credentials retrieved",credentials);
    }


}
