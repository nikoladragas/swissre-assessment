package org.swissre.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CsvEmployeeParserTest {
    @Mock
    private CsvReader reader;
    @Mock
    private CsvEmployeeValidator validator;
    private CsvEmployeeParser parser;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        parser = new CsvEmployeeParser(reader, validator);
    }

    @Test
    void testParseEmployees_NoData_ReturnsEmptyList() throws IOException, EmployeeValidationException, EmployeeValidationException, IOException {
        // Given
        when(reader.readLine()).thenReturn(null);
        doThrow(new EmployeeValidationException("Invalid headers.")).when(validator).validateHeaders(any());

        // Then
        assertThrows(EmployeeValidationException.class, () -> parser.parseEmployees());
    }

    @Test
    void testParseEmployees_OnlyHeaders_ReturnsEmptyList() throws IOException, EmployeeValidationException {
        // Given
        String[] header = {"Id", "firstName", "lastName", "salary", "managerId"};
        when(reader.readLine()).thenReturn(header).thenReturn(null);
        doCallRealMethod().when(validator).validateHeaders(any());
        doCallRealMethod().when(validator).validateLine(any());

        // When
        List<Employee> employees = parser.parseEmployees();

        // Then
        assertEquals(0, employees.size());
        verify(validator).validateHeaders(header);
    }

    @Test
    void testParseEmployees_ValidData_ReturnsEmployees() throws IOException, EmployeeValidationException {
        // Given
        String[] header = {"Id", "firstName", "lastName", "salary", "managerId"};
        String[] data1 = {"1", "John", "Doe", "50000"};
        String[] data2 = {"2", "Jane", "Smith", "60000", "1"};
        when(reader.readLine()).thenReturn(header).thenReturn(data1).thenReturn(data2).thenReturn(null);
        doCallRealMethod().when(validator).validateHeaders(any());
        doCallRealMethod().when(validator).validateLine(any());

        // When
        List<Employee> employees = parser.parseEmployees();

        // Then
        assertEquals(2, employees.size());
        verify(validator).validateHeaders(header);
        verify(validator, times(2)).validateLine(any());
    }
}
