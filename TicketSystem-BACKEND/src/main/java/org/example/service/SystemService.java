package org.example.service;

import org.example.model.Configuration;
import org.example.model.Response;

public interface SystemService {
    Response saveSystemConfiguration(Configuration configuration);
}
