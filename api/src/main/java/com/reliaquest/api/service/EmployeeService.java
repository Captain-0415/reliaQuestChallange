package com.reliaquest.api.service;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private final RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";

    private final Set<Employee> employeeCache = ConcurrentHashMap.newKeySet();

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * This method will be called externally by EmployeeInitializer
     * to populate cache when the application starts.
     */
    public void initializeEmployeeCache() {
        logger.info("[initializeEmployeeCache] : Initializing employee cache...");
        employeeCache.addAll(fetchAllEmployeesFromAPI());
        logEmployeeRecords();
    }

    /**
     * Fetch employees from API and log them.
     */
    private List<Employee> fetchAllEmployeesFromAPI() {
        logger.info("[fetchAllEmployeesFromAPI] : Fetching all employees from API...");
        ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL, EmployeeResponse.class);
        return response.getBody().getData();
    }

    /**
     * Log all employees.
     */
    private void logEmployeeRecords() {
        logger.info("[logEmployeeRecords] : Employee records retrieved:");
        employeeCache.forEach(emp -> logger.info(emp.toString()));
    }

    @Cacheable(value = "employees")
    public List<Employee> getAllEmployees() {
        if (employeeCache.isEmpty()) {
            employeeCache.addAll(fetchAllEmployeesFromAPI());
            logEmployeeRecords();
        }
        return new ArrayList<>(employeeCache);
    }

    public List<Employee> getEmployeesByName(String searchString) {
        List<Employee> employees = getAllEmployees().stream()
                .filter(emp -> emp.getEmployee_name().toLowerCase().contains(searchString.toLowerCase()))
                .toList();

        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employee found with name: " + searchString);
        }
        return employees;
    }

    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployeeById(String id) {
        return getAllEmployees().stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No employee found with id: " + id));
    }

    public Integer getHighestSalary() {
        return getAllEmployees().stream()
                .mapToInt(Employee::getEmployee_salary)
                .max()
                .orElse(0);
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        return getAllEmployees().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getEmployee_salary(), e1.getEmployee_salary()))
                .limit(10)
                .map(Employee::getEmployee_name)
                .toList();
    }

    @CachePut(value = "employee", key = "#result.id")
    @CacheEvict(value = "employees", allEntries = true)
    public Employee createEmployee(Employee employee) {
        ResponseEntity<EmployeeSingleResponse> response =
                restTemplate.postForEntity(BASE_URL, employee, EmployeeSingleResponse.class);
        Employee newEmployee = response.getBody().getData();
        employeeCache.add(newEmployee);
        return newEmployee;
    }

    @CacheEvict(
            value = {"employee", "employees"},
            allEntries = true)
    public void deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            restTemplate.delete(BASE_URL + "/" + id);
            employeeCache.remove(employee);
        } else {
            throw new ResourceNotFoundException("No employee found with id: " + id);
        }
    }

    static class EmployeeResponse {
        private List<Employee> data;

        public List<Employee> getData() {
            return data;
        }

        public void setData(List<Employee> employees) {
            this.data = employees;
        }
    }

    static class EmployeeSingleResponse {
        private Employee data;

        public Employee getData() {
            return data;
        }

        public void setData(Employee newEmployee) {
            this.data = newEmployee;
        }
    }
}
