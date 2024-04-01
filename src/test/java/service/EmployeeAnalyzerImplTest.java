package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.swissre.parser.EmployeeParser;
import org.swissre.csv.CsvEmployeeParser;
import org.swissre.csv.CsvEmployeeValidator;
import org.swissre.csv.CsvReader;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.report.ReportingLineReport;
import org.swissre.model.report.SalaryReport;
import org.swissre.service.EmployeeAnalyzer;
import org.swissre.service.EmployeeAnalyzerImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeAnalyzerImplTest {

    private EmployeeParser parser;
    private EmployeeAnalyzer analyzer;
    @Mock
    private CsvEmployeeValidator validator;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStreamCaptor));
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
        List<SalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetEmployeesWithInappropriateReportingLine_NoEmployees_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-only-headers.csv");
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateSalary_EmployeesIsNull_ReturnsEmptyList() {
        //Given
        this.analyzer = new EmployeeAnalyzerImpl(null);
        // When
        List<SalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateReportingLine_EmployeesIsNull_ReturnsEmptyList() {
        //Given
        this.analyzer = new EmployeeAnalyzerImpl(null);
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateSalary_AllAppropriate_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-appropriate.csv");
        // When
        List<SalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetEmployeesWithInappropriateReportingLine_AllAppropriate_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-appropriate.csv");
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());
    }

    @Test
    void testGetManagersWithInappropriateSalary_ReturnsEmployees() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-inappropriate.csv");
        // When
        List<SalaryReport> reports = analyzer.getManagersWithInappropriateSalary();
        // Then
        assertEquals(5, reports.size());

        assertEquals("Joe", reports.get(3).getManager().getFirstName());
        assertEquals("Doe", reports.get(3).getManager().getLastName());
        assertEquals(81000, reports.get(3).getSalaryDifference());
        assertEquals("more than", reports.get(3).getAdverb());

        assertEquals("Martin", reports.get(4).getManager().getFirstName());
        assertEquals("Chekov", reports.get(4).getManager().getLastName());
        assertEquals(15000, reports.get(4).getSalaryDifference());
        assertEquals("less than", reports.get(4).getAdverb());
    }

    @Test
    void testGetManagersWithInappropriateReportingLine_ReturnsEmployees() throws IOException, EmployeeValidationException {
        // Given
        setupFromFile("company-all-inappropriate.csv");
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(2, reports.size());

        assertEquals("Carolin", reports.get(0).getEmployee().getFirstName());
        assertEquals("McAfee", reports.get(0).getEmployee().getLastName());
        assertEquals(2, reports.get(0).getSufficientManagersCount());

        assertEquals("Mike", reports.get(1).getEmployee().getFirstName());
        assertEquals("Smith", reports.get(1).getEmployee().getLastName());
        assertEquals(1, reports.get(1).getSufficientManagersCount());
    }

    @Test
    void testGetManagersWithInappropriateReportingLine_CircularReferences_SkipsInvalidEmployees() throws EmployeeValidationException, IOException {

        // Given
        setupFromFile("company-circular-references.csv");
        Pattern pattern = Pattern.compile("(Skipping processing this employee - Employee can't be in their own manager hierarchy.\r\n){4}");
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());

        assertTrue(pattern.matcher(outputStreamCaptor.toString()).matches());
    }

    @Test
    void testGetManagersWithInappropriateReportingLine_InvalidReferences_SkipsInvalidEmployees() throws EmployeeValidationException, IOException {

        // Given
        setupFromFile("company-invalid-references.csv");
        Pattern pattern = Pattern.compile("(Skipping processing this employee - Employee's manager references non-existent employee.\r\n){3}");
        // When
        List<ReportingLineReport> reports = analyzer.getEmployeesWithInappropriateReportingLine();
        // Then
        assertEquals(0, reports.size());

        assertTrue(pattern.matcher(outputStreamCaptor.toString()).matches());
    }
}
