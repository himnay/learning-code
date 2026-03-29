package com.org.test;

import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemStreamInt {

    public static void main(String[] args) {

        // find odd numbers
        oddNumber();

        // find max number
        minMaxNumber();

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

    }

    private static void oddNumber() {
        System.out.println("***oddNumber***");
        var oddNumbers = IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .boxed()
                .toList();
        System.out.println(oddNumbers);
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

    private static void sumNumber(){
        System.out.println("sumNumber()");
        int sum = IntStream.rangeClosed(1, 10)
                .sum();
        System.out.println(sum);
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
        var list = List.of(2, 5, 8, 10);
        System.out.println("List of numbers : " + list);

        // find min and max
        Integer min = list.stream().min(Integer::compareTo).get();
        Integer max = list.stream().max(Integer::compareTo).get();

        var missingNumbers = IntStream.rangeClosed(min, max)
                .filter(i -> !list.contains(i))
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

}
