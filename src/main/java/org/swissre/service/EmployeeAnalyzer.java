package org.swissre.service;

import org.swissre.model.report.InappropriateReportingLineReport;
import org.swissre.model.report.InappropriateSalaryReport;

import java.util.List;

public interface EmployeeAnalyzer {
    List<InappropriateSalaryReport> getManagersWithInappropriateSalary();
    List<InappropriateReportingLineReport> getEmployeesWithInappropriateReportingLine();
}
