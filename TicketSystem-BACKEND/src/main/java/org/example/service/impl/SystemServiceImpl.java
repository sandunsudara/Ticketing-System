package org.example.service.impl;

import org.example.cli.ApplicationCLI;
import org.example.model.Configuration;
import org.example.model.Response;
import org.example.service.SystemService;
import org.springframework.stereotype.Service;

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


}
