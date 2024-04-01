package org.swissre.model.report;

import org.swissre.model.Employee;

public class ReportingLineReport {
    private final Employee employee;
    private final Integer sufficientManagersCount;

    public Employee getEmployee() {
        return employee;
    }

    public Integer getSufficientManagersCount() {
        return sufficientManagersCount;
    }

    public ReportingLineReport(Employee employee, Integer sufficientManagersCount) {
        this.employee = employee;
        this.sufficientManagersCount = sufficientManagersCount;
    }

    public String prettyPrint() {
        return "Name: " + this.employee.getFirstName() + " " + this.employee.getLastName() +
                "\nId: " + this.employee.getId() +
                "\nHas " + this.sufficientManagersCount + " more managers in hierarchy than they should.\n";
    }
}
