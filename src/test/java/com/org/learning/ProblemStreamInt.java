package com.org.learning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
public class ProblemStreamInt {

    @Test
    @DisplayName("filter even number and double it")
    public void evenNumber() {
        var evenNumber = IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .boxed()
                .map(i -> i * 2)
                .toList();
        System.out.println(evenNumber);
    }

    @Test
    @DisplayName("find min and max number in a list")
    public void minMaxNumber() {
        var numbers = IntStream.rangeClosed(1, 10)
                .boxed()
                .toList();

        var maxNumber = numbers.stream()
                .max((i, j) -> i.compareTo(j))
                .get();
        System.out.println(maxNumber);

        var minNumber = numbers.stream()
                .min((i, j) -> i.compareTo(j))
                .get();
        System.out.println(minNumber);
    }

    @Test
    @DisplayName("find min and max number in a list using reduce")
    public void minMaxNumberUsingReduce() {
        var numbers = List.of(3, 2, 1);

        var maxNumber = numbers.stream()
                .reduce(Integer::max)
                .get();

        var minNumber = numbers.stream()
                .reduce(Integer::min)
                .get();

        System.out.println(maxNumber);
        System.out.println(minNumber);
    }

    @Test
    @DisplayName("find sum of number in a list")
    public void sumNumber() {
        // option 1
        int sum = IntStream.rangeClosed(1, 5)
                .sum();
        System.out.println(sum);

        // option 2
        OptionalInt reduce = IntStream.rangeClosed(1, 5)
                .reduce((a, b) -> a + b);
        System.out.println(reduce);

        // option 3
        var numbers = List.of(1, 2, 3, 4, 5);
        Optional<Integer> collectReduce = numbers.stream()
                .collect(Collectors.reducing((x, y) -> x + y));
        System.out.println(collectReduce);
    }

    @Test
    @DisplayName("find duplicate numbers in a list")
    public void findDuplicateNumbers() {
        var numbers = List.of(1, 2, 2, 3, 4, 4, 5);
        var dupNumbers = new HashSet<>();

        var duplicateNumbers = numbers.stream()
                .filter(i -> !dupNumbers.add(i))
                .toList();
        System.out.println(duplicateNumbers);
    }

    @Test
    @DisplayName("find missing number in a list")
    public void findMissingNoInAList() {
        var numbers = List.of(2, 5, 8, 10);
        System.out.println("List of numbers : " + numbers);

        // find min and max
        Integer min = numbers.stream().min(Integer::compareTo).get();
        Integer max = numbers.stream().max(Integer::compareTo).get();

        // use set O(1) instead of list O(N)
        var missingNumbers = IntStream.rangeClosed(min, max)
                .filter(i -> !numbers.contains(i))
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Missing numbers : " + missingNumbers);
    }

    @Test
    @DisplayName("find even and odd numbers in a list")
    public void generateEvenOddNumbers() {
        var evenNumbers = IntStream.iterate(2, i -> i + 2)
                .limit(5)
                .boxed()
                .toList();


        var oddNumber = IntStream.iterate(1, i -> i + 2)
                .limit(5)
                .boxed()
                .toList();
        System.out.println("Even numbers : " + evenNumbers);
        System.out.println("Odd numbers : " + oddNumber);
    }

    @Test
    @DisplayName("find second smallest and second largest number in a list")
    public void secondSmallestLargest() {
        OptionalInt smallest = IntStream.rangeClosed(1, 10)
                .boxed()
                .sorted()
                .skip(1)
                .mapToInt(Integer::intValue)
                .findFirst();

        OptionalInt largest = IntStream.rangeClosed(1, 10)
                .boxed()
                .sorted((a, b) -> Integer.compare(b, a))
                .skip(1)
                .mapToInt(Integer::intValue)
                .findFirst();
    }

    @Test
    @DisplayName("reverse an array of int and sort it")
    public void reverseArrayOfInt() {
        var numbers = List.of(5, 4, 3, 2, 1);

        var reverseNumbers = numbers.stream()
                .sorted()
                .toList();
        System.out.println(numbers);
        System.out.println(reverseNumbers);
    }

    @Test
    @DisplayName("find longest and shortest word in a list of string")
    public void longAndShortWord() {
        var strings = List.of("Hi", "Hello", "HelloWorld", "Test", "Spring");

        Optional<String> longestWord = strings.stream()
                .reduce((a, b) -> a.length() > b.length() ? a : b);
        System.out.println(longestWord);
    }

    @Test
    @DisplayName("count even and odd numbers in a list")
    public void countEvenOddNumbers() {
        var numbers = List.of(1, 2, 3, 4, 5, 6);
        Long sum = numbers.stream()
                .reduce(0L, (count, a) -> a % 2 == 0 ? count + 1 : count, Long::sum);
        System.out.println(sum);
    }

    @Test
    @DisplayName("Flatten a List<List<Integer>> and find the top 3 distinct numbers in descending order")
    public void flattenAndDistinctDescending() {
        List<List<Integer>> nestedNumbers = List.of(
                List.of(1,2,3,4,5),
                List.of(5,4,3,2,1),
                List.of(6,7,8,9,10),
                List.of(10,9,8,7,6)
        );

        List<Integer> sortedNumbers = nestedNumbers.stream()
                .flatMap(i -> i.stream())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        System.out.println(sortedNumbers);
    }

}