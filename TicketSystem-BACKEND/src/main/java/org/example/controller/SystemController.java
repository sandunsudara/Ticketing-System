package org.example.controller;


import org.example.model.Configuration;
import org.example.model.Response;
import org.example.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system")
@CrossOrigin
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping
    public void startSimulation(){


    }



    @PostMapping("config-save")
    public Response configurationSystem(@RequestBody Configuration configuration){
        Response response = systemService.saveSystemConfiguration(configuration);
        return response;
    }

}
