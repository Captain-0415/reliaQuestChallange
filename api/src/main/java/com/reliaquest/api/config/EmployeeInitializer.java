package com.reliaquest.api.config;

import com.reliaquest.api.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeInitializer.class);
    private final EmployeeService employeeService;

    public EmployeeInitializer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Running EmployeeInitializer...");
        employeeService.initializeEmployeeCache();
    }
}
