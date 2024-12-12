package org.example.service;

import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;

import java.util.List;
import java.util.Map;

public interface SystemService {
    Response saveSystemConfiguration(Configuration configuration);

    Response<Configuration> readConfiguration();

    Response<Object> startSimulation(List<Customer> customerList, int noVendor);

    Response<Object> startSimulation(int noCustomer, int noVendor);

    Response<Object> stopSimulation();

    Response<Object> addCustomer(Customer customer);

    Response<Object> addVendor();

    Response<Map<String, Integer>> getCredentials();
}
