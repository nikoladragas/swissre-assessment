package org.swissre.service;

import org.swissre.model.Employee;
import org.swissre.model.ThresholdConstants;
import org.swissre.model.report.ReportingLineReport;
import org.swissre.model.report.SalaryReport;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeAnalyzerImpl implements EmployeeAnalyzer {

    private final Map<Long, Employee> employees;

    public EmployeeAnalyzerImpl(Map<Long, Employee> employees) {
        this.employees = employees;
    }

    /**
     * Retrieves a list of reports for managers with salaries deemed inappropriate per set threshold.
     *
     * @return A list of InappropriateSalaryReport instances containing details of managers with inappropriate salaries.
     */
    @Override
    public List<SalaryReport> getManagersWithInappropriateSalary() {
        List<SalaryReport> reports = new ArrayList<>();

        getAvgSubordinateSalaryPerManager().forEach((managerId, avgSalary) ->
                {
                    Employee manager = getEmployeeById(managerId);
                    if (manager != null && isSalaryInappropriate(manager.getSalary(), avgSalary)) {
                        reports.add(new SalaryReport(manager, manager.getSalary(), avgSalary));
                    }
                }
        );
        return reports;
    }

    /**
     * Retrieves a list of reports for employees with inappropriate reporting lines.
     * Reporting line is inappropriate if the amount of managers in hierarchy is higher than set threshold.
     *
     * @return A list of InappropriateReportingLineReport instances containing details of employees with inappropriate reporting lines.
     */
    @Override
    public List<ReportingLineReport> getEmployeesWithInappropriateReportingLine() {
        List<ReportingLineReport> reports = new ArrayList<>();
        getManagersToCeoCount().forEach((employeeId, managerCount) -> {
            if (managerCount <= ThresholdConstants.MGR_COUNT) {
                return;
            }
            Employee employee = getEmployeeById(employeeId);
            if (employee != null) {
                reports.add(new ReportingLineReport(employee, managerCount - ThresholdConstants.MGR_COUNT));
            }
        });
        return reports;
    }

    private Map<Long, Double> getAvgSubordinateSalaryPerManager() {
        if (this.employees == null) {
            return new HashMap<>();
        }
        return this.employees.values().stream()
                .filter(employee -> employee.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId, Collectors.averagingDouble(Employee::getSalary)));
    }

    private boolean isSalaryInappropriate(Long managerSalary, Double avgSalary) {
        return managerSalary > avgSalary * ThresholdConstants.UPPER || managerSalary < avgSalary * ThresholdConstants.LOWER;
    }

    private Employee getEmployeeById(Long id) {
        if (this.employees == null) {
            return null;
        }
        return this.employees.getOrDefault(id, null);
    }

    /**
     * Retrieves a mapping of employee IDs to the number of managers leading up to the CEO in their reporting hierarchy.
     */
    private Map<Long, Integer> getManagersToCeoCount() {
        Map<Long, Integer> managersToCEO = new HashMap<>();
        if (this.employees == null) {
            return managersToCEO;
        }

        this.employees.values().forEach(employee -> {
            // CEO doesn't report
            if (employee.getManagerId() == null) {
                return;
            }

            Integer mgrCount = countManagersInHierarchy(employee);
            if (mgrCount != null) {
                managersToCEO.put(employee.getId(), mgrCount);
            }
        });
        return managersToCEO;
    }

    /**
     * Count managers between specified employee and CEO
     * @param employee - specifies from which employee to count from
     * @return
     */
    private Integer countManagersInHierarchy(Employee employee) {
        Employee currentEmployee = getEmployeeById(employee.getManagerId());
        if (currentEmployee == null) {
            System.out.println("Skipping processing this employee - Employee's manager references non-existent employee.");
            return null;
        }

        Integer managersCount = 0;
        while (currentEmployee.getManagerId() != null) {
            managersCount++;
            if (employee.getId().equals(currentEmployee.getId())) {
                System.out.println("Skipping processing this employee - Employee can't be in their own manager hierarchy.");
                return null;
            }
            currentEmployee = getEmployeeById(currentEmployee.getManagerId());
            if (currentEmployee == null) {
                System.out.println("Skipping processing this employee - Employee's manager references non-existent employee.");
                return null;
            }
        }
        return managersCount;
    }

}
