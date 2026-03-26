package com.org.test;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProblemStream {
    public static void main(String[] args) {

        // find the occurrence
        occurrenceNumber();

        // find max number
        maxNumber();

        // filter non null string
        filterNonNullString();

        // find odd numbers
        oddNumber();

        // uppercase
        uppercase();

        // remove duplicate
        removeDup();
    }

    private static void occurrenceNumber() {
        String name = "HimansuNayak";
        var occurence =Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurence);
    }

    private static void maxNumber() {
        // using random list
        OptionalInt max = Arrays.stream(ThreadLocalRandom.current()
                .ints(10, 1, 101) // count=10, min=1 (inclusive), max=101 (exclusive)
                .toArray()).max();
        if(max.isPresent()) {
            System.out.println(max.getAsInt());
        }

        // using max()
        IntStream.rangeClosed(1, 10).max().ifPresent(System.out::println);
    }

    private static void filterNonNullString() {
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    private static void oddNumber() {
        IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .forEach(i -> System.out.print(" " + i));
    }

    private static void uppercase() {
        List.of("apple", "orange", "banana", "kiwi", "kiwi")
                .stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    private static void removeDup() {
        List.of(2, 2, 4, 3, 4, 5, 2, 5)
                .stream()
                .distinct()
                .forEach(System.out::println);
    }
}
