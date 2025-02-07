package com.reliaquest.api.entity;

public class Employee {

    private final String id;

    private final String employee_name;

    private final int employee_salary;

    private final int employee_age;

    private final String employee_title;

    private final String employee_email;

    public Employee(
            String id,
            String employee_name,
            int employee_salary,
            int employee_age,
            String employee_title,
            String employee_email) {
        this.id = id;
        this.employee_name = employee_name;
        this.employee_salary = employee_salary;
        this.employee_age = employee_age;
        this.employee_title = employee_title;
        this.employee_email = employee_email;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getId() {
        return id;
    }

    public int getEmployee_salary() {
        return employee_salary;
    }

    public int getEmployee_age() {
        return employee_age;
    }

    public String getEmployee_title() {
        return employee_title;
    }

    public String getEmployee_email() {
        return employee_email;
    }
}
