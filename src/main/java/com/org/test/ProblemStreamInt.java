package com.org.test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemStreamInt {

    void main() {

        // find odd numbers
        evenNumber();

        // find max number
        minMaxNumber();

        // find min and max number using stream reduce
        minMaxNumberUsingReduce();

        // find sum
        sumNumber();

        // find duplicate numbers
        findDuplicateNumbers();

        // find missing no in a list
        findMissingNoInAList();

        // generate even and odd numbers
        generateEvenOddNumbers();

        // second smallest and largest number
        secondSmallestLargest();

        // reverse a array of int
        reverseArrayOfInt();

        // longest or shortest word in a list
        longAndShortWord();

    }

    private static void evenNumber() {
        System.out.println("***evenNumber***");
        var evenNumber = IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .boxed()
                .map(i -> i * 2)
                .toList();
        System.out.println(evenNumber);
    }

    private static void minMaxNumber() {
        System.out.println("minMaxNumber()");
        var numbers = IntStream.rangeClosed(1, 10)
                .boxed()
                .toList();

        var maxNumber = numbers.stream()
                .max((i,j) -> i.compareTo(j))
                .get();
        var minNumber = numbers.stream()
                        .min((i, j) -> i.compareTo(j))
                        .get();

        System.out.println(maxNumber);
        System.out.println(minNumber);
    }

    private static void minMaxNumberUsingReduce() {
        System.out.println("minMaxNumber()");
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

    private static void sumNumber(){
        // option 1
        System.out.println("sumNumber()");
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

    private static void findDuplicateNumbers() {
        System.out.println("***findDuplicateNumbers***");
        var numbers = List.of(1, 2, 2, 3, 4, 4, 5);
        var dupNumbers = new HashSet<>();

        var duplicateNumbers = numbers.stream()
                .filter(i -> !dupNumbers.add(i))
                .toList();
        System.out.println(duplicateNumbers);
    }

    private static void findMissingNoInAList() {
        System.out.println("***findMissingNoInAList***");
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

    private static void generateEvenOddNumbers() {
        System.out.println("generateEvenOddNumbers()");
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

    private static void secondSmallestLargest() {
        System.out.println("secondSmallestLargest()");
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

    private static void reverseArrayOfInt() {
        System.out.println("reverseArrayOfInt()");
        var numbers = List.of(5, 4, 3, 2, 1);

        var reverseNumbers = numbers.stream()
                .sorted()
                .toList();
        System.out.println(numbers);
        System.out.println(reverseNumbers);
    }

    private static void longAndShortWord() {
        System.out.println("longAndShortWord()");
        var strings = List.of("Hi", "Hello", "HelloWorld", "Test", "Spring");

        Optional<String> longestWord = strings.stream()
                .reduce((a, b) -> a.length() > b.length() ? a : b);
        System.out.println(longestWord);
    }

    private static void countEvenOddNumbers() {
        System.out.println("countEvenOddNumbers()");
        var numbers = List.of(1, 2, 3, 4, 5, 6);
        Long sum = numbers.stream()
                .reduce(0L, (count, a) -> a % 2 == 0 ? count + 1 : count, Long::sum);
        System.out.println(sum);
    }

}

