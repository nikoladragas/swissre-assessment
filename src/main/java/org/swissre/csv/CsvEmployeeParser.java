package org.swissre.csv;

import org.swissre.parser.EmployeeParser;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvEmployeeParser implements EmployeeParser {
    private final CsvReader reader;
    private final CsvEmployeeValidator validator;

    public CsvEmployeeParser(CsvReader reader, CsvEmployeeValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    public Map<Long, Employee> parseEmployees() throws IOException, EmployeeValidationException {
        Map<Long, Employee> employees = new HashMap<>();
        String[] line = reader.readLine();
        validator.validateHeaders(line);

        while ((line = reader.readLine()) != null) {
            validator.validateLine(line);
            Employee employee = new Employee(Long.parseLong(line[0]), line[1], line[2],
                    Long.parseLong(line[3]), line.length == 5 ? Long.parseLong(line[4]) : null);
            if (employee.getId() != null) {
                employees.put(employee.getId(), employee);
            }
        }
        return employees;
    }
}
