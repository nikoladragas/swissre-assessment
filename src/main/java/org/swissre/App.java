package org.swissre;

import org.swissre.csv.CsvEmployeeParser;
import org.swissre.csv.CsvEmployeeValidator;
import org.swissre.csv.CsvReader;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;
import org.swissre.model.report.InappropriateReportingLineReport;
import org.swissre.model.report.InappropriateSalaryReport;
import org.swissre.service.EmployeeAnalyzerImpl;

import java.io.IOException;
import java.util.List;

public class App
{
    public static void main( String[] args ) {
        System.out.println("Company analyzer application\n\n");

        List<Employee> employeeList;

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
        List<InappropriateSalaryReport> salaryReports = analyzer.getManagersWithInappropriateSalary();
        List<InappropriateReportingLineReport> reportingLineReports = analyzer.getEmployeesWithInappropriateReportingLine();

        System.out.println("Managers with inappropriate salaries\n");
        for (InappropriateSalaryReport report : salaryReports) {
            System.out.println(report);
        }

        System.out.println("----------------------------------------------\n");
        System.out.println("Employees with inappropriate manager hierarchy\n");
        for (InappropriateReportingLineReport report : reportingLineReports) {
            System.out.println(report);

        }
    }
}
