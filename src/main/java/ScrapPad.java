import com.org.test.dto.Employee;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScrapPad {
//Initialize a List with employee set and using Java streams perform the following:
//1. Get Minimum & Maximum Salary Employee details across all departments
//2. Get Minimum & Maximum Salary Employee details in each department
//3. Get total salary by department
//4. Get Average salary by department
//5. Process dataset in parallel
//6. Create 2 groups i.e. GROUP 1 has employees who has salary < = 3000 and GROUP 2 has employees > 3000

    public static void main(String[] args) {
        Employee frank = new Employee("frank", "IT", 25, 3000.0, 9922001);
        Employee ace = new Employee("Ace", "IT", 24, 4000.0, 9922002);
        Employee keith = new Employee("Keith", "HR", 33, 2000.0, 9922323);
        Employee declan = new Employee("Declan", "Finance", 35, 5000.0, 9927652);
        Employee barry = new Employee("Barry", "Finance", 45, 8000.0, 9922876);

        var employees = List.of(frank, ace, keith, declan, barry);

        employees.stream()
                .min(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);

        employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);

        var collect = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.minBy(Comparator.comparing(Employee::getSalary))));
        System.out.println("args = " + collect);

        collect = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.maxBy(Comparator.comparing(Employee::getSalary))));
        System.out.println("args = " + collect);

        var totalSalary = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.summingDouble(Employee::getSalary)));
        System.out.println("args = " + totalSalary);

        var avgSalary = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));
        System.out.println("args = " + avgSalary);

        var itNonIt = employees.stream()
                .collect(Collectors.partitioningBy(i -> i.getDepartment().equals("IT") ? true : false));
        System.out.println("IT :" + itNonIt.get(true));
        System.out.println("Non-IT :" + itNonIt.get(false));

        // find odd/even
        IntStream.rangeClosed(1, 100)
                .filter(i -> i % 2 == 0)
                .forEach(System.out::println);
    }
}
