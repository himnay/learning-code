import com.org.learning.dto.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ScrapPad {
//Initialize a List with employee set and using Java streams perform the following:
//1. Get Minimum & Maximum Salary Employee details across all departments
//2. Get Minimum & Maximum Salary Employee details in each department
//3. Get total salary by department
//4. Get Average salary by department
//5. Process dataset in parallel
//6. Create 2 groups i.e. GROUP 1 has employees who has salary < = 3000 and GROUP 2 has employees > 3000

    void main() {
        Employee frank = new Employee("frank", "IT", 25, 3000.0, 9922001);
        Employee ace = new Employee("Ace", "IT", 24, 4000.0, 9922002);
        Employee keith = new Employee("Keith", "HR", 33, 2000.0, 9922323);
        Employee declan = new Employee("Declan", "Finance", 35, 5000.0, 9927652);
        Employee barry = new Employee("Barry", "Finance", 45, 8000.0, 9922876);

        var employees = List.of(frank, ace, keith, declan, barry);

        avg();
        caseChange();
        evenOddSum();
        removeDup();
        listStartsWith();
    }

    private static void avg() {
        double average = IntStream.rangeClosed(1, 10).average().getAsDouble();
        System.out.println(average);
    }

    private static void caseChange() {
        var strings = List.of("apple", "orange", "mango");
        List<String> upperCase = strings.stream().map(String::toUpperCase).toList();
        System.out.println(upperCase);
    }

    private static void evenOddSum() {
        IntStream.iterate(1, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
    }

    private static void removeDup() {
        IntStream.generate(() -> (int) Math.random() * 100) // random range should be within 100
                .limit(5)
                .distinct()
                .forEach(System.out::println);
    }

    private static void listStartsWith() {
        var list = List.of("Red", "Rose", "Mat", "Ring");

        list.stream().filter(i -> i.startsWith("R")).forEach(System.out::println);
    }

    private static void sortStrings() {
        var list = List.of("Test", "Mat", "Apple", "Zebra");

        list.stream().sorted().forEach(System.out::println);
        list.stream().sorted(String::compareTo).forEach(System.out::println);
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }
}
