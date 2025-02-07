package com.reliaquest.api.controller;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EmployeeController implements IEmployeeController<Employee, Employee> {

    private final EmployeeService employeeService;

    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            logger.trace("[getAllEmployee] : Fetching all employees.");
            List<Employee> employees = employeeService.getAllEmployees();
            Map<String, Object> response = new HashMap<>();
            response.put("data", employees);
            response.put("status", "Successfully processed request.");
            return ResponseEntity.ok().body((List<Employee>) response.get("data"));
        } catch (Exception e) {
            logger.error("[getAllEmployee] : Error fetching employees", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching employees", e);
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.trace("[getEmployeesByNameSearch] : Searching employees by name: {}", searchString);
        return ResponseEntity.ok(employeeService.getEmployeesByName(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.trace("[getEmployeeById] : Fetching employee by ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.trace("[getHighestSalaryOfEmployees] : Fetching highest salary of employees");
        try {
            return ResponseEntity.ok(employeeService.getHighestSalary());
        } catch (Exception e) {
            logger.error("[getHighestSalaryOfEmployees] : Error fetching highest salary", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching highest salary", e);
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("[getTopTenHighestEarningEmployeeNames] : Fetching top ten highest earning employee names");
        try {
            return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
        } catch (Exception e) {
            logger.error("[getTopTenHighestEarningEmployeeNames] : Error fetching top earners", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching top earners", e);
        }
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Employee employeeInput) {
        logger.trace("[createEmployee] : Creating employee: {}", employeeInput.getEmployee_name());
        try {
            return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
        } catch (Exception e) {
            logger.error("[createEmployee] : Error creating employee", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating employee", e);
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        logger.trace("[deleteEmployeeById] : Deleting employee by ID: {}", id);
        employeeService.deleteEmployeeById(id);
        logger.info("[deleteEmployeeById] : Employee deleted successfully: {}", id);
        return ResponseEntity.ok("Employee deleted successfully.");
    }
}
