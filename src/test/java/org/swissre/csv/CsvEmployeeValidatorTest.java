package org.swissre.csv;

import org.junit.jupiter.api.Test;
import org.swissre.exception.EmployeeValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvEmployeeValidatorTest {

    @Test
    void validateHeaders_ValidHeaders_NoExceptionThrown() throws EmployeeValidationException {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] validHeaders = {"Id", "firstName", "lastName", "salary", "managerId"};
        validator.validateHeaders(validHeaders);
    }

    @Test
    void validateHeaders_InvalidHeadersMissingColumn_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidHeaders = {"Id", "firstName", "lastName", "salary"};
        assertThrows(EmployeeValidationException.class, () -> validator.validateHeaders(invalidHeaders));
    }

    @Test
    void validateHeaders_InvalidHeadersWrongColumnName_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidHeaders = {"EmployeeId", "firstName", "lastName", "salary",  "managerId"};
        assertThrows(EmployeeValidationException.class, () -> validator.validateHeaders(invalidHeaders));
    }

    @Test
    void validateHeaders_InvalidNumberOfColumns_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidHeaders = {"Id", "firstName", "lastName", "salary", "managerId", "extraColumn"};
        assertThrows(EmployeeValidationException.class, () -> validator.validateHeaders(invalidHeaders));
    }

    @Test
    void validateHeaders_NoColumns_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidHeaders = null;
        assertThrows(EmployeeValidationException.class, () -> validator.validateHeaders(invalidHeaders));
    }

    @Test
    void validateLine_ValidLineWithoutManagerId_NoExceptionThrown() throws EmployeeValidationException {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] validLine = {"1", "John", "Doe", "50000"};
        validator.validateLine(validLine);
    }

    @Test
    void validateLine_ValidLine_NoExceptionThrown() throws EmployeeValidationException {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] validLine = {"1", "John", "Doe", "50000", "2"};
        validator.validateLine(validLine);
    }

    @Test
    void validateLine_InvalidLineLength_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidLine = {"1", "John", "Doe"};
        assertThrows(EmployeeValidationException.class, () -> validator.validateLine(invalidLine));
    }

    @Test
    void validateLine_InvalidAttributeValues_ThrowsEmployeeValidationException() {
        CsvEmployeeValidator validator = new CsvEmployeeValidator();
        String[] invalidLine = {"1", "John", "Doe", "InvalidSalary"};
        assertThrows(EmployeeValidationException.class, () -> validator.validateLine(invalidLine));
    }
}
