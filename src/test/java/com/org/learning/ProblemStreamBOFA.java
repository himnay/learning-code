package com.org.learning;

import com.org.learning.dto.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

//Initialize a List with employee set and using Java streams perform the following:
//1. Get Minimum & Maximum Salary Employee details across all departments
//2. Get Minimum & Maximum Salary Employee details in each department
//3. Get total salary by department
//4. Get Average salary by department
//5. Process dataset in parallel
//6. Create 2 groups i.e. GROUP 1 has employees who has salary < = 3000 and GROUP 2 has employees > 3000
public class ProblemStreamBOFA {
    void main() {
        Employee frank = new Employee("frank", "IT", 25, 3000.0, 9922001);
        Employee ace = new Employee("Ace", "IT", 24, 4000.0, 9922002);
        Employee keith = new Employee("Keith", "HR", 33, 2000.0, 9922323);
        Employee declan = new Employee("Declan", "Finance", 35, 5000.0, 9927652);
        Employee barry = new Employee("Barry", "Finance", 45, 8000.0, 9922876);

        var employees = List.of(frank, ace, keith, declan, barry);

        //1. Get Minimum & Maximum Salary Employee details across all departments
        employees.stream()
                .min(Comparator.comparing(Employee::salary));
        employees.stream()
                .max(Comparator.comparing(Employee::salary));

        //2. Get Minimum & Maximum Salary Employee details in each department
        employees.stream()
                .collect(groupingBy(Employee::department, Collectors.minBy(Comparator.comparing(Employee::salary))));
        employees.stream()
                .collect(groupingBy(Employee::department, Collectors.minBy(Comparator.comparing(Employee::department))));

        //3. Get total salary by department
        //5. Process dataset in parallel
        employees.stream()
                .collect(groupingByConcurrent(Employee::department, summingDouble(Employee::salary)));

        //4. Get Average salary by department
        //5. Process dataset in parallel
        employees.stream()
                .collect(groupingByConcurrent(Employee::department, averagingDouble(Employee::salary)));

        //6. Create 2 groups i.e. GROUP 1 has employees who have salary < = 3000 and GROUP 2 has employees > 3000
        employees.stream()
                .collect(groupingBy(e -> e.salary() <= 3000 ? "Low Salary" : "High Salary"));
        employees.stream()
                .collect(partitioningBy(e -> e.salary() <= 3000));
    }
}