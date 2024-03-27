package org.swissre.model.report;

import org.swissre.model.Employee;

public class InappropriateReportingLineReport {
    private final Employee employee;
    private final Integer sufficientManagersCount;

    public Employee getEmployee() {
        return employee;
    }

    public Integer getSufficientManagersCount() {
        return sufficientManagersCount;
    }

    public InappropriateReportingLineReport(Employee employee, Integer sufficientManagersCount) {
        this.employee = employee;
        this.sufficientManagersCount = sufficientManagersCount;
    }

    @Override
    public String toString() {
        return "Name: " + this.employee.getFirstName() + " " + this.employee.getLastName() +
                "\nId: " + this.employee.getId() +
                "\nHas " + this.sufficientManagersCount + " more managers in hierarchy than they should.\n";
    }
}
