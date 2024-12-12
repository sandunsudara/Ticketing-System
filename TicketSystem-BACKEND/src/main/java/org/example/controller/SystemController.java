package org.example.controller;


import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;
import org.example.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("system")
@CrossOrigin()
public class SystemController {

    @Autowired
    private SystemService systemService;

    @PostMapping("start-with-customer")
    public Response<Object> startSimulation(@RequestBody List<Customer> customers, @RequestParam int noVendor){
        return systemService.startSimulation(customers, noVendor);
    }

    @PostMapping("start-genCustomer")
    public Response<Object> startSimulation(@RequestParam int noCustomer, @RequestParam int noVendor){
        return systemService.startSimulation(noCustomer, noVendor);
    }

    @GetMapping("stop-simulation")
    public Response<Object> stopSimulation(){
        return systemService.stopSimulation();
    }

    @PutMapping("add-customer")
    public Response<Object> addCustomer(@RequestBody Customer customer){
         return systemService.addCustomer(customer);
    }

    @PutMapping("add-vendor")
    public Response<Object> addVendor(){
         return systemService.addVendor();
    }

    @GetMapping("get-credential")
    public Response<Map<String, Integer>> getCredentials(){
        return systemService.getCredentials();
    }




    @PostMapping("save-config")
    public Response configurationSystem(@RequestBody Configuration configuration){
        return systemService.saveSystemConfiguration(configuration);
    }

    @GetMapping("get-config")
    public Response<Configuration> readConfiguration(){
        return systemService.readConfiguration();

    }

}
