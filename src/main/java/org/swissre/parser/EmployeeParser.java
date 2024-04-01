package org.swissre.parser;

import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;

import java.io.IOException;
import java.util.Map;

public interface EmployeeParser {
    Map<Long, Employee> parseEmployees() throws IOException, EmployeeValidationException;
}
