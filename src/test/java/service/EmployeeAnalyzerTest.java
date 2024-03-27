package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.swissre.EmployeeParser;
import org.swissre.csv.CsvEmployeeParser;
import org.swissre.csv.CsvEmployeeValidator;
import org.swissre.csv.CsvReader;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.report.InappropriateReportingLineReport;
import org.swissre.model.report.InappropriateSalaryReport;
import org.swissre.service.EmployeeAnalyzer;
import org.swissre.service.EmployeeAnalyzerImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeAnalyzerTest {

    private EmployeeParser parser;
    private EmployeeAnalyzer analyzer;
    @Mock
    private CsvEmployeeValidator validator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    void setupFromFile(String fileName) throws IOException, EmployeeValidationException {
        this.parser = new CsvEmployeeParser(new CsvReader(fileName), validator);
        this.analyzer = new EmployeeAnalyzerImpl(this.parser.parseEmployees());
    }

    @Test
    void testGetManagersWithInappropriateSalary_NoEmployees_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-only-headers.csv");
        // When
        List<InappropriateSalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetEmployeesWithInappropriateReportingLine_NoEmployees_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-only-headers.csv");
        // When
        List<InappropriateReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateSalary_AllAppropriate_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-appropriate.csv");
        // When
        List<InappropriateSalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetEmployeesWithInappropriateReportingLine_AllAppropriate_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-appropriate.csv");
        // When
        List<InappropriateReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateSalary_ReturnsEmployees() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-inappropriate.csv");
        // When
        List<InappropriateSalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(5, reports.size());

        assertEquals("Joe", reports.get(3).getManager().getFirstName());
        assertEquals("Doe", reports.get(3).getManager().getLastName());
        assertEquals(81000, reports.get(3).getSalaryDifference());
        assertEquals("more", reports.get(3).getAdverb());

        assertEquals("Martin", reports.get(4).getManager().getFirstName());
        assertEquals("Chekov", reports.get(4).getManager().getLastName());
        assertEquals(15000, reports.get(4).getSalaryDifference());
        assertEquals("less", reports.get(4).getAdverb());
    }

    @Test
    void testGetManagersWithInappropriateReportingLine_ReturnsEmployees() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-inappropriate.csv");
        // When
        List<InappropriateReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(2, reports.size());

        assertEquals("Carolin", reports.get(0).getEmployee().getFirstName());
        assertEquals("McAfee", reports.get(0).getEmployee().getLastName());
        assertEquals(2, reports.get(0).getSufficientManagersCount());

        assertEquals("Mike", reports.get(1).getEmployee().getFirstName());
        assertEquals("Smith", reports.get(1).getEmployee().getLastName());
        assertEquals(1, reports.get(1).getSufficientManagersCount());
    }
}
