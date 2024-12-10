package org.example.controller;


import org.example.model.Configuration;
import org.example.model.Customer;
import org.example.model.Response;
import org.example.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system")
@CrossOrigin
public class SystemController {

    @Autowired
    private SystemService systemService;

    @PostMapping("start-with-customer")
    public void startSimulation(@RequestBody List<Customer> customer,@RequestParam int noVendor){
        systemService.startSimulation(customer, noVendor);
    }

    @PostMapping("start-genCustomer")
    public void startSimulation(@RequestParam int noCustomer, @RequestParam int noVendor){
        System.out.println("gen");
//        systemService.startSimulation(noCustomer, noVendor);
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
