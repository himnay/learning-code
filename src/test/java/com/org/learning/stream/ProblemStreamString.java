package com.org.learning.stream;

import com.org.learning.dto.City;
import com.org.learning.dto.Employee;
import com.org.learning.dto.Student;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class ProblemStreamString {

    @Test
    @DisplayName("occurrence of a character in a string")
    public void occurrenceNumber() {
        String name = "HimansuNayak";
        var occurrence = Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurrence);
    }

    @Test
    @DisplayName("filter non null and non empty string")
    public void filterNonNullString() {
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    @Test
    @DisplayName("upper case string")
    public void upperCase() {
        List.of("apple", "orange", "banana", "kiwi", "kiwi")
                .stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("remove duplicate numbers")
    public void removeDup() {
        List.of(2, 2, 4, 3, 4, 5, 2, 5)
                .stream()
                .distinct()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("merge two list and remove duplicate numbers")
    public void mergeListAndDeDup() {
        var firstList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        var secondList = List.of(5, 6, 7, 8, 9, 10);

        var mergeList = Stream.concat(firstList.stream(), secondList.stream())
                .distinct()
                .toList();
        System.out.println(mergeList);
    }

    @Test
    @DisplayName("find the longest word in a list of string")
    public void longestWord() {
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        Optional<String> size = list.stream()
                .max(Comparator.comparingInt(String::length));
        if (size.isPresent()) {
            System.out.println("Longest word " + size.get().length());
        }
    }

    @Test
    @DisplayName("reverse each string in a list and sort the list based on the reversed string")
    public void reverseAndSortString() {
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        List<String> reverseAndSorted = list.stream()
                .map(i -> new StringBuilder(i).reverse().toString())
                .sorted()
                .toList();
        System.out.println("Actual List of String " + list);
        System.out.println("Reverse and Sorted " + reverseAndSorted);
    }

    @Test
    @DisplayName("merge two list and remove all the numbers except 1 from the second list" )
    public void mergeList() {
        var listA = List.of(2, 3, 4, 2);
        var listB = List.of(1, 4, 2, 1, 5);

        List<Integer> listOfOne = listB.stream()
                .filter(i -> i == 1)
                .toList();

        var collectedList = Stream.concat(listA.stream(), listOfOne.stream()).toList();
        System.out.println("Merge List " + collectedList);
    }

    @Test
    @DisplayName("move all the 1 from a list to the end of the list")
    public void moveAllOneToEnd() {
        var list = List.of(1, 2, 1, 3, 1, 4, 1);

        var collectedOne = list.stream().filter(i -> i == 1).toList();
        var filterList = list.stream().filter(i -> i != 1).toList();
        var mergeList = Stream.concat(filterList.stream(), collectedOne.stream());
    }

    @Test
    @DisplayName("reverse a string")
    public void stringReverse() {
        var name = "BankOfAmerica";
        System.out.println("String : " + name);
        var reverseString = Arrays.stream(name.split(""))
                .reduce((a, b) -> b + a);
        System.out.println("Reverse String : " + reverseString.get());
    }

    @Test
    @DisplayName("sort a list of string in natural order and reverse order")
    public void sortStrings() {
        var list = List.of("Test", "Mat", "RAT", "Apple", "Zebra");

        // natural order
        var sortedNaturalOrder = list.stream()
                .sorted()
                .toList();
        System.out.println(sortedNaturalOrder);

        sortedNaturalOrder = list.stream()
                .sorted(String::compareTo)
                .toList();
        System.out.println(sortedNaturalOrder);

        // reverse order
        sortedNaturalOrder = list.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.println(sortedNaturalOrder);
    }

    @Test
    @DisplayName("occurrence of a word in a list of string")
    public void occurrenceOfWord() {
        var list = List.of("apple", "apple", "apple", "banana", "banana", "mango");
        var collect = list.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        System.out.println(collect);
    }

    @Test
    @DisplayName("find common word in three list of string")
    public void commonWord() {
        var listA = List.of("I", "am", "a", "Hero");
        var listB = List.of("Hero", "are", "good");
        var listC = List.of("Good", "Hero", "are", "paid", "well");

        var commonWord = listA.stream()
                .filter(i -> listB.contains(i) && listC.contains(i))
                .toList();
        System.out.println(commonWord);
    }

    @Test
    @DisplayName("filter numbers from an alpha numeric string and sum the numbers")
    public void filterNumbersFromAlphaNumericStringAndSum() {
        Character[] alphaNum = {'A', '1', 'l', '2', 'p', '3', 'h'};

        var numbers = Arrays.stream(alphaNum)
                .filter(i -> Character.isDigit(i))
                .mapToInt(Character::getNumericValue)
                .sum();
        System.out.println(numbers);
    }

    @Test
    @DisplayName("count the minimum occurrence of a character in a string")
    public void countMinOccurrence() {
        Character[] characters = {'a', 'b', 'a', 'b', 'c', 'd', 'd', 'd'};
        Map<Character, Long> collect = Arrays.stream(characters)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        // sort and transfer to a linked hashmap
        var result = collect.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(result);
    }

    @Test
    @DisplayName("find the longest string in a list of string")
    public void findLongestString() {
        var strings = List.of("Hello", "Encylopedia", "Tiger", "Architecture");

        List<String> sortedString = strings.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
        System.out.println(sortedString.get(0));
    }

    @Test
    @DisplayName("length of last word in a sentence")
    public void lengthOfLastWordInASentence() {
        var string = "Good Morning. How are you?";
        Optional<Integer> stringLength = Arrays.stream(string.split(" "))
                .reduce((a, b) -> b)
                .map(String::length);

        System.out.println(stringLength.get());
    }

    @Test
    @DisplayName("split a string and join with comma and add square bracket at the beginning and end of the string")
    public void stringSplit() {

        Arrays.stream("Himansu".split(""))
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Test
    @DisplayName("find the string which has unique characters")
    public void uniqueCharacters() {
        // option 1
        Set set = new HashSet();
        String string = "beta";
        List<String> dupCharacter = Arrays.stream(string.split(""))
                .filter(i -> set.add(i) == false)
                .toList();
        if(CollectionUtils.isNotEmpty(dupCharacter)) {
            System.out.println("Duplicate character in string " + string);
        } else {
            System.out.println("Unique character in string " + string);
        }

        // option 2
        long count = "alpha".chars()
                .mapToObj(c -> (char) c)
                .distinct()
                .count();

        if("alpha".length() != count) {
            System.out.println("Duplicate character in string " + "alpha");
        }
    }

    @Test
    @DisplayName("Second highest salary of an employee")
    public void secondHighestSalary() {
        Employee frank = new Employee("frank", "IT", 25, 3000.0, 9922001);
        Employee ace = new Employee("Ace", "IT", 24, 4000.0, 9922002);
        Employee keith = new Employee("Keith", "HR", 33, 2000.0, 9922323);
        Employee declan = new Employee("Declan", "Finance", 35, 5000.0, 9927652);
        Employee barry = new Employee("Barry", "Finance", 45, 8000.0, 9922876);

        var employees = List.of(frank, ace, keith, declan, barry);

        Employee employee = employees.stream()
                .sorted(Comparator.comparing(Employee::salary).reversed())
                .skip(1)
                .toList()
                .get(0);
        System.out.println(employee);
    }

    @Test
    @DisplayName("Partition numbers to even and odd")
    public void partitionOddEven() {
        var partition = IntStream.rangeClosed(1, 20)
                .boxed() // convert IntStream to Stream<Integer>
                .collect(Collectors.partitioningBy(i -> i % 2 == 0));
        System.out.println(partition);
    }

    @Test
    @DisplayName("Find student based on location or city and sort them alphabetically using stream API.")
    public void findEmployeeBasedOnLocation() {
        Student student_1 = new Student("John", City.DUBLIN);
        Student student_2 = new Student("Alpha", City.DUBLIN);
        Student student_3 = new Student("Mickey", City.GALWAY);
        Student student_4 = new Student("Donald", City.GALWAY);
        Student student_5 = new Student("Kiaan", City.LIMERICK);
        Student student_6 = new Student("Kyra", City.CORK);
        Student student_7 = new Student("Himansu", City.CORK);

        var students = List.of(student_1, student_2, student_3, student_4, student_5, student_6, student_7);

        // style 1
        var sortedStudents = students.stream()
                .filter(s -> s.city() == City.DUBLIN)
                .sorted(Comparator.comparing(Student::name))
                .map(Student::name)// map before collecting
                .toList();

        // style 2
        sortedStudents = students.stream()
                .filter(s -> s.city() == City.DUBLIN)
                .sorted(Comparator.comparing(Student::name))
                .collect(Collectors.mapping(Student::name, Collectors.toList())); // map during collecting

        System.out.println(sortedStudents);
    }

    @Test
    @DisplayName("Given a List<String> of sentences, count the total frequency of each word across all sentences (case-insensitive), " +
            "and return a Map<String, Long> sorted by frequency descending")
    public void countSortFrequency() {
        List<String> listOfSentence = List.of(
                "When I wake up in the morning",
                "When I wake up in the morning",
                "When I wake up in the morning");

        var sortedMap = listOfSentence.stream()
                .map(i -> Arrays.stream(i.split(" ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        sortedMap.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));
    }

    @Test
    @DisplayName("Given a List<Employee>, group employees by department and then for each department return a comma-separated string of employee names sorted alphabetically.")
    public void groupSeparateAndSort() {
        Student student_1 = new Student("John", City.DUBLIN);
        Student student_2 = new Student("Alpha", City.DUBLIN);
        Student student_3 = new Student("Mickey", City.GALWAY);        Student student_4 = new Student("Donald", City.GALWAY);
        Student student_5 = new Student("Kiaan", City.LIMERICK);
        Student student_6 = new Student("Kyra", City.CORK);
        Student student_7 = new Student("Himansu", City.CORK);

        var students = List.of(student_1, student_2, student_3, student_4, student_5, student_6, student_7);

        Function<List<Student>,  String> sortAndCollectStudentName = i -> i.stream()
                .sorted(Comparator.comparing(Student::name))
                .map(Student::name)
                .collect(Collectors.joining(","));

        Map<City, String> result = students.stream()
                .collect(Collectors.groupingBy(Student::city,
                        Collectors.collectingAndThen(Collectors.toList(), list -> sortAndCollectStudentName.apply(list))));

        System.out.println(result);
    }
}
