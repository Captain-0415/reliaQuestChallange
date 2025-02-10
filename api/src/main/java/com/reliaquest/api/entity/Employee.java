package com.reliaquest.api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Employee {
    @JsonIgnore
    private final String id;

    private final String employee_name;

    private final int employee_salary;

    private final int employee_age;

    private final String employee_title;
    @JsonIgnore
    private final String employee_email;

    public Employee(
            String employee_name,
            int employee_salary,
            int employee_age,
            String employee_title) {
        this.id = null;
        this.employee_name = employee_name;
        this.employee_salary = employee_salary;
        this.employee_age = employee_age;
        this.employee_title = employee_title;
        this.employee_email = null;
    }
    @JsonCreator
    public Employee(
            @JsonProperty("id") String id,
            @JsonProperty("employee_name") String employee_name,
            @JsonProperty("employee_salary") int employee_salary,
            @JsonProperty("employee_age") int employee_age,
            @JsonProperty("employee_title") String employee_title,
            @JsonProperty("employee_email") String employee_email) {

        this.id = (id != null) ? id : UUID.randomUUID().toString();
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

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", employee_name='" + employee_name + '\'' +
                ", employee_salary=" + employee_salary +
                ", employee_age=" + employee_age +
                ", employee_title='" + employee_title + '\'' +
                ", employee_email='" + employee_email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(employee_email, employee.employee_email);
    }

    @Override
    public int hashCode(){
        return Objects.hash(employee_email);
    }
}
