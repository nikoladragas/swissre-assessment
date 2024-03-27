# Employee Analyzer - SwissRe Assessment

# Description

BIG COMPANY is employing a lot of employees. Company would like to analyze its organizational
structure and identify potential improvements. Board wants to make sure that every manager earns
at least 20% more than the average salary of its direct subordinates, but no more than 50% more
than that average. Company wants to avoid too long reporting lines, therefore we would like to
identify all employees which have more than 4 managers between them and the CEO.

You are given a CSV file which contains information about all the employees. File structure looks like
this:

```csv
Id,firstName,lastName,salary,managerId
123,Joe,Doe,66000,
124,Martin,Chekov,52000,123
125,Bob,Ronstad,48000,123
300,Alice,Hasacat,42000,124
305,Brett,Hardleaf,34000,300
```

Each line represents an employee (CEO included). CEO has no manager specified. Number of rows
can be up to 1000.
Write a simple program which will read the file and report:
- which managers earn less than they should, and by how much
- which managers earn more than they should, and by how much
- which employees have a reporting line which is too long, and by how much

# Implementation

## CSV package
Set of classes used to read employee information from the chosen file from resources folder, validate it's content, and parse it into a list of Employee objects.

## Employee Analyzer service
Provides two crucial functionalities for analyzing the company.
### Get managers with inappropriate salary
Creates a report that consists of information about managers which have an inappropriate salary.
Salary is inappropriate if it's above 50% more or below 20% more than average direct subordinate salary.
### Get employees with inappropriate report line
Creates a report that consists of information about managers which have an inappropriate report line.
Report line is inappropriate if there are more than 4 managers between an employee and the CEO.

## Report package
Set of classes constructed by EmployeeAnalyzer service and used to show information about the company/employees to the user.

## Possible improvements
### CSV validation
- Can be improved by defining a contract(schema) of required values, types, etc.
- Error essages can be improved by specifying line numbers with underlying issues.

### Reporting line manager count
Algorithm for counting amount of managers between each employee and CEO can be improved. Currently it's counting the amount of managers for every employee, and basically goes through some employees multiple times because they are in the hierarchy.

The hierarchy can be examined with less complexity when DFS algorithm is used, which will not go through the same employee multiple times.

However, time wise, there is not a big difference if number of employees is less than 1000, as described in the task.
