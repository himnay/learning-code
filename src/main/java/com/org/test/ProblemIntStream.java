package com.org.test;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemIntStream {

    public static void main(String[] args) {

        // find odd numbers
        oddNumber();

        // find missing no in a list
        findMissingNoInAList();

        // find max number
        maxNumber();

        // generate even and odd numbers
        generateEvenOddNumbers();

    }

    private static void oddNumber() {
        System.out.println("***oddNumber***");
        IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .forEach(i -> System.out.print(" " + i));
    }

    private static void findMissingNoInAList() {
        System.out.println("***findMissingNoInAList***");
        var list = List.of(2, 5, 8, 10);
        System.out.println("List of numbers : " + list);

        // find min and max
        Integer min = list.stream().min(Integer::compareTo).get();
        Integer max = list.stream().max(Integer::compareTo).get();

        var missingNumbers = IntStream.rangeClosed(min, max)
                .filter(i -> list.contains(i))
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Missing numbers : " + missingNumbers);
    }

    private static void maxNumber() {
        // using random list
        System.out.println("***maxNumber***");
        OptionalInt max = IntStream.generate(() -> (int) Math.random() * 100) // random range should be within 100
                .limit(10) // generate 10 random numbers
                .max();
        if (max.isPresent()) {
            System.out.println(max.getAsInt());
        }

        // using max()
        IntStream.rangeClosed(1, 10).max().ifPresent(System.out::println);
    }

    private static void generateEvenOddNumbers() {
        System.out.println("generateEvenOddNumbers()");
        IntStream.iterate(2, i -> i + 2)
                .limit(5)
                .forEach(System.out::print);

        IntStream.iterate(1, i -> i + 2)
                .limit(5)
                .forEach(System.out::print);
    }

    private static void secondSmallestLargest() {
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

}
