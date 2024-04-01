package org.swissre;

import org.swissre.csv.CsvEmployeeParser;
import org.swissre.csv.CsvEmployeeValidator;
import org.swissre.csv.CsvReader;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;
import org.swissre.model.report.ReportingLineReport;
import org.swissre.model.report.SalaryReport;
import org.swissre.service.EmployeeAnalyzerImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class App
{
    public static void main( String[] args ) {
        System.out.println("Company analyzer application\n\n");

        Map<Long, Employee> employeeList;

        try {
            CsvReader fileReader = new CsvReader("company-all-inappropriate.csv");
            CsvEmployeeParser employeeParser = new CsvEmployeeParser(fileReader, new CsvEmployeeValidator());
            employeeList = employeeParser.parseEmployees();
        } catch (EmployeeValidationException e) {
            System.out.println("Employee parsing failed: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Reading from company file failed.");
            return;
        }

        EmployeeAnalyzerImpl analyzer = new EmployeeAnalyzerImpl(employeeList);
        List<SalaryReport> salaryReports = analyzer.getManagersWithInappropriateSalary();
        List<ReportingLineReport> reportingLineReports = analyzer.getEmployeesWithInappropriateReportingLine();

        System.out.println("Managers with inappropriate salaries\n");
        for (SalaryReport report : salaryReports) {
            System.out.println(report.prettyPrint());
        }

        System.out.println("----------------------------------------------\n");
        System.out.println("Employees with inappropriate manager hierarchy\n");
        for (ReportingLineReport report : reportingLineReports) {
            System.out.println(report.prettyPrint());

        }
    }
}
