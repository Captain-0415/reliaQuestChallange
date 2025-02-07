package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> mockEmployees;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if (testInfo.getTestMethod().isPresent()
                && testInfo.getTestMethod().get().getName().equals("testCreateEmployee_Success")) {
            return; // Skip setup for this specific test
        }
        employee1 = new Employee("1", "John Doe", 50000, 30, "Software Engineer", "johndoe@company.com");
        employee2 = new Employee("2", "Jane Doe", 60000, 28, "Product Manager", "janedoe@company.com");
        mockEmployees = new ArrayList<>(Arrays.asList(employee1, employee2));

        EmployeeService.EmployeeResponse response = new EmployeeService.EmployeeResponse();
        response.setData(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeService.EmployeeResponse.class)))
                .thenReturn(ResponseEntity.ok(response));
    }

    @Test
    void testGetAllEmployees_Success() {
        List<Employee> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeeById_Success() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        Employee result = employeeService.getEmployeeById("1");

        assertNotNull(result);
        assertEquals("John Doe", result.getEmployee_name());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById("99");
        });

        assertEquals("No employee found with id: 99", exception.getMessage());
    }

    @Test
    void testGetEmployeesByName_Success() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        List<Employee> result = employeeService.getEmployeesByName("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getEmployee_name());
    }

    @Test
    void testGetEmployeesByName_NotFound() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeesByName("Unknown");
        });

        assertEquals("No employee found with name: Unknown", exception.getMessage());
    }

    @Test
    void testGetHighestSalary_Success() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        int highestSalary = employeeService.getHighestSalary();

        assertEquals(60000, highestSalary);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_Success() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        List<String> topEarners = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(2, topEarners.size());
        assertEquals("Jane Doe", topEarners.get(0));
    }

    @Test
    void testCreateEmployee_Success() {
        Employee newEmployee = new Employee("3", "Alice", 70000, 32, "HR Manager", "alice@company.com");

        EmployeeService.EmployeeSingleResponse response = new EmployeeService.EmployeeSingleResponse();
        response.setData(newEmployee);

        when(restTemplate.postForEntity(anyString(), eq(newEmployee), eq(EmployeeService.EmployeeSingleResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        Employee createdEmployee = employeeService.createEmployee(newEmployee);

        assertNotNull(createdEmployee);
        assertEquals("Alice", createdEmployee.getEmployee_name());
    }

    @Test
    void testDeleteEmployeeById_Success() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        employeeService.deleteEmployeeById("1");

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById("1"));
    }

    @Test
    void testDeleteEmployeeById_NotFound() {
        employeeService.getAllEmployees().addAll(mockEmployees);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployeeById("99");
        });

        assertEquals("No employee found with id: 99", exception.getMessage());
    }
}
