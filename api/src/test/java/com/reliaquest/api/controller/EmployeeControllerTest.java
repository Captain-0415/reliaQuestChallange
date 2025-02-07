package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.exceptions.ResourceNotFoundException;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee(
                UUID.randomUUID().toString(), "Raj Kamal", 50000, 24, "Software Engineer", "raj.kamal@reliaquest.com");
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById_success() {
        String id = employee.getId().toString();
        when(employeeService.getEmployeeById(id)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);

        assertNotNull(response);
        assertEquals(employee.getId(), response.getBody().getId());
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        String id = UUID.randomUUID().toString();
        when(employeeService.getEmployeeById(id))
                .thenThrow(new ResourceNotFoundException("No employee found with id: " + id));

        assertThrows(ResourceNotFoundException.class, () -> employeeController.getEmployeeById(id));
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    void testGetEmployeesByNameSearch_success() {
        String searchString = "John";
        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getEmployeesByName(searchString)).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(employeeService, times(1)).getEmployeesByName(searchString);
    }

    @Test
    void testGetHighestSalaryOfEmployees_success() {
        when(employeeService.getHighestSalary()).thenReturn(50000);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertNotNull(response);
        assertEquals(50000, response.getBody());
        verify(employeeService, times(1)).getHighestSalary();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_success() {
        List<String> topEmployees = Arrays.asList("John Doe", "Jane Smith");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEmployees);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee_success() {
        when(employeeService.createEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee);

        assertNotNull(response);
        assertEquals(employee.getId(), response.getBody().getId());
        verify(employeeService, times(1)).createEmployee(employee);
    }

    @Test
    void testDeleteEmployeeById_success() {
        String id = employee.getId();
        doNothing().when(employeeService).deleteEmployeeById(id);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        assertNotNull(response);
        assertEquals("Employee deleted successfully.", response.getBody());
        verify(employeeService, times(1)).deleteEmployeeById(id);
    }

    @Test
    void testDeleteEmployeeById_NotFound() {
        String id = UUID.randomUUID().toString();
        doThrow(new ResourceNotFoundException("No employee found with id: " + id))
                .when(employeeService)
                .deleteEmployeeById(id);

        assertThrows(ResourceNotFoundException.class, () -> employeeController.deleteEmployeeById(id));
        verify(employeeService, times(1)).deleteEmployeeById(id);
    }
}
