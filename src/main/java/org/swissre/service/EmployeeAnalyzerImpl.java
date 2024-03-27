package org.swissre.service;

import org.swissre.model.Employee;
import org.swissre.model.ThresholdConstants;
import org.swissre.model.report.InappropriateReportingLineReport;
import org.swissre.model.report.InappropriateSalaryReport;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeAnalyzerImpl implements EmployeeAnalyzer {

    private final List<Employee> employees;

    public EmployeeAnalyzerImpl(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * Retrieves a list of reports for managers with salaries deemed inappropriate per set threshold.
     *
     * @return A list of InappropriateSalaryReport instances containing details of managers with inappropriate salaries.
     */
    @Override
    public List<InappropriateSalaryReport> getManagersWithInappropriateSalary() {
        List<InappropriateSalaryReport> reports = new ArrayList<>();

        getAvgSubordinateSalaryPerManager().forEach((managerId, avgSalary) ->
            getEmployeeById(managerId).ifPresent(manager -> {
                if (isSalaryInappropriate(manager.getSalary(), avgSalary)) {
                    reports.add(new InappropriateSalaryReport(manager, manager.getSalary(), avgSalary));
                }
            })
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
    public List<InappropriateReportingLineReport> getEmployeesWithInappropriateReportingLine() {
        List<InappropriateReportingLineReport> reports = new ArrayList<>();
        getManagersToCeoCount().forEach((employeeId, managerCount) -> {
            if (managerCount <= ThresholdConstants.MGR_COUNT_THRESHOLD) {
                return;
            }
            getEmployeeById(employeeId).ifPresent(employee ->
                    reports.add(new InappropriateReportingLineReport(employee, managerCount - ThresholdConstants.MGR_COUNT_THRESHOLD))
            );
        });
        return reports;
    }

    private Map<Long, Double> getAvgSubordinateSalaryPerManager() {
        return this.employees.stream()
                .filter(employee -> employee.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId, Collectors.averagingDouble(Employee::getSalary)));
    }

    private boolean isSalaryInappropriate(Long managerSalary, Double avgSalary) {
        return managerSalary > avgSalary * ThresholdConstants.UPPER || managerSalary < avgSalary * ThresholdConstants.LOWER;
    }

    private Optional<Employee> getEmployeeById(Long id) {
        return this.employees.stream().filter(employee -> employee.getId().equals(id)).findAny();
    }

    /**
     * Retrieves a mapping of employee IDs to the number of managers leading up to the CEO in their reporting hierarchy.
     */
    private Map<Long, Integer> getManagersToCeoCount() {
        Map<Long, Integer> managersToCEO = new HashMap<>();

        employees.forEach(employee -> {
            Integer managersCount = 0;
            Long currentEmployeeId = employee.getId();
            Employee currentEmployee = getEmployeeById(employee.getManagerId()).orElse(null);

            while (currentEmployee != null && currentEmployee.getManagerId() != null) {
                managersCount++;
                currentEmployee = getEmployeeById(currentEmployee.getManagerId()).orElse(null);
            }

            managersToCEO.put(currentEmployeeId, managersCount);
        });

        return managersToCEO;
    }

}
