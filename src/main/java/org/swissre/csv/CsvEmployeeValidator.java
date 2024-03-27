package org.swissre.csv;

import org.swissre.exception.EmployeeValidationException;

public class CsvEmployeeValidator {
    private static final String[] EXPECTED_HEADERS = {"Id", "firstName", "lastName", "salary", "managerId"};
    private static final Integer[] REQUIRED_NUMBER_ATTRIBUTE_INDEXES = {0, 3};

    public void validateHeaders(String[] headers) throws EmployeeValidationException {
        if (headers == null || headers.length != EXPECTED_HEADERS.length) {
            throw new EmployeeValidationException("CSV file has invalid headers - Unexpected number of columns.");
        }
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            if (!EXPECTED_HEADERS[i].equals(headers[i])) {
                throw new EmployeeValidationException("CSV file has invalid headers - Unexpected columns.");
            }
        }
    }

    public void validateLine(String[] line) throws EmployeeValidationException {
        if (line.length != 4 && line.length != 5) {
            throw new EmployeeValidationException("Invalid CSV line.");
        }

        for (Integer index : REQUIRED_NUMBER_ATTRIBUTE_INDEXES) {
            try {
                Long.parseLong(line[index]);
            } catch (NumberFormatException e) {
                throw new EmployeeValidationException("Invalid attribute values in CSV line.");
            }
        }
    }

}
