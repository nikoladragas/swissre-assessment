package org.swissre.service;

import org.swissre.model.report.ReportingLineReport;
import org.swissre.model.report.SalaryReport;

import java.util.List;

public interface EmployeeAnalyzer {
    List<SalaryReport> getManagersWithInappropriateSalary();
    List<ReportingLineReport> getEmployeesWithInappropriateReportingLine();
}
