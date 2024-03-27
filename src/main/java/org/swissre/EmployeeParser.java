package org.swissre;

import org.swissre.exception.EmployeeValidationException;
import org.swissre.model.Employee;

import java.io.IOException;
import java.util.List;

public interface EmployeeParser {
    List<Employee> parseEmployees() throws IOException, EmployeeValidationException;
}
