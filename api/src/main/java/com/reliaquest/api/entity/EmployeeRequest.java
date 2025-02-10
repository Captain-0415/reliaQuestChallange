package com.reliaquest.api.entity;

public class EmployeeRequest {
    private final String name;
    private final int salary;
    private final int age;
    private final String title;

    public EmployeeRequest(Employee emp){
        this.name = emp.getEmployee_name();
        this.salary = emp.getEmployee_salary();
        this.age = emp.getEmployee_age();
        this.title = emp.getEmployee_title();
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    public String getTitle() {
        return title;
    }
}
