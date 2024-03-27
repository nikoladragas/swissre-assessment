package org.swissre.csv;

import org.swissre.EmployeeParser;
import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvEmployeeParser implements EmployeeParser {
    private final CsvReader reader;
    private final CsvEmployeeValidator validator;

    public CsvEmployeeParser(CsvReader reader, CsvEmployeeValidator validator) {
        this.reader = reader;
        this.validator = validator;
    }

    public List<Employee> parseEmployees() throws IOException, EmployeeValidationException {
        List<Employee> employees = new ArrayList<>();
        String[] line = reader.readLine();
        validator.validateHeaders(line);

        while ((line = reader.readLine()) != null) {
            validator.validateLine(line);
            Employee employee = new Employee(Long.parseLong(line[0]), line[1], line[2],
                    Long.parseLong(line[3]), line.length == 5 ? Long.parseLong(line[4]) : null);
            employees.add(employee);
        }
        return employees;
    }
}
