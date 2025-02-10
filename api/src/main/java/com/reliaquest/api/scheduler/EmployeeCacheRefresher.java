package com.reliaquest.api.scheduler;

import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmployeeCacheRefresher {

    private final Logger logger = LoggerFactory.getLogger(EmployeeCacheRefresher.class);


    private final EmployeeService employeeService;

    public EmployeeCacheRefresher(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Scheduled(fixedRate = 300000) // Refresh every 5 minutes
    public void refreshEmployees() {
        logger.info("[refreshEmployees] : refreshing employees data.");
        employeeService.initializeEmployeeCache();
    }
}
