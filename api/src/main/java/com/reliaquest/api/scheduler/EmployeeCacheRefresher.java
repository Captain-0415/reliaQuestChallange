package com.reliaquest.api.scheduler;

import com.reliaquest.api.service.EmployeeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmployeeCacheRefresher {

    private final EmployeeService employeeService;

    public EmployeeCacheRefresher(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Scheduled(fixedRate = 300000) // Refresh every 5 minutes
    public void refreshEmployees() {
        employeeService.initializeEmployeeCache();
    }
}
