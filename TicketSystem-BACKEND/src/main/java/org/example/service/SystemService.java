package org.example.service;

import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;

import java.util.List;

public interface SystemService {
    Response saveSystemConfiguration(Configuration configuration);
    Response<Configuration> readConfiguration();

    void startSimulation(List<Customer> customerList , int noVendor);
    void startSimulation(int noCustomer , int noVendor);
}
