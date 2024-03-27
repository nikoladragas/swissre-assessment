package org.swissre.model.report;

import org.swissre.model.Employee;
import org.swissre.model.ThresholdConstants;

public class InappropriateSalaryReport {
    private final Employee manager;
    private final Long managerSalary;
    private final Double avgSubordinateSalary;
    private final Double salaryDifference;
    private final String adverb;

    public Employee getManager() {
        return manager;
    }

    public Double getSalaryDifference() {
        return salaryDifference;
    }

    public String getAdverb() {
        return adverb;
    }

    public InappropriateSalaryReport(Employee manager, Long managerSalary, Double avgSubordinateSalary) {
        this.manager = manager;
        this.managerSalary = managerSalary;
        this.avgSubordinateSalary = avgSubordinateSalary;

        if (managerSalary > avgSubordinateSalary * ThresholdConstants.UPPER) {
            this.salaryDifference = managerSalary - avgSubordinateSalary * ThresholdConstants.UPPER;
            this.adverb = "more";
        } else if (managerSalary < avgSubordinateSalary * ThresholdConstants.LOWER) {
            this.salaryDifference = avgSubordinateSalary * ThresholdConstants.LOWER - managerSalary;
            this.adverb = "less";
        } else {
            this.salaryDifference = 0.0;
            this.adverb = "";
        }
    }

    @Override
    public String toString() {
        return "Name: " + this.manager.getFirstName() + " " + this.manager.getLastName() +
                "\nId: " + this.manager.getId() +
                "\nEarning " + Math.abs(this.salaryDifference) + " " + this.adverb + " than they should.\n";
    }
}
