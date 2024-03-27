package org.swissre.model;


public class Employee {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final Long salary;
    private final Long managerId;

    public Employee(Long id, String firstName, String lastName, Long salary, Long managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getSalary() {
        return salary;
    }

    public Long getManagerId() {
        return managerId;
    }
}
