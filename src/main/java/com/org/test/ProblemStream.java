package com.org.test;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        // merge 2 list and remove duplidate
        mergeListAndDeDup();

        // longest word from a list of string
        longestWord();

        // reverse and sorted a list of string
        reverseAndSortString();
    }

    private static void occurrenceNumber() {
        System.out.println("***occurrenceNumber***");
        String name = "HimansuNayak";
        var occurence =Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurence);
    }

    private static void maxNumber() {
        // using random list
        System.out.println("***maxNumber***");
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
        System.out.println("***filterNonNullString***");
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    private static void oddNumber() {
        System.out.println("***oddNumber***");
        IntStream.rangeClosed(1, 10)
                .filter(i -> i % 2 == 0)
                .forEach(i -> System.out.print(" " + i));
    }

    private static void uppercase() {
        System.out.println("***uppercase***");
        List.of("apple", "orange", "banana", "kiwi", "kiwi")
                .stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    private static void removeDup() {
        System.out.println("***removeDup***");
        List.of(2, 2, 4, 3, 4, 5, 2, 5)
                .stream()
                .distinct()
                .forEach(System.out::println);
    }

    private static void mergeListAndDeDup() {
        System.out.println("***mergeListAndDeDup***");
        var firstList = List.of(1,2,3,4,5,6,7,8,9);
        var secondList = List.of(5,6,7,8,9,10);

        Stream.concat(firstList.stream(), secondList.stream()).distinct().forEach(System.out::println);
    }

    private static void longestWord() {
        System.out.println("***longestWord***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        Optional<String> size = list.stream().max(Comparator.comparingInt(String::length));
        if(size.isPresent()) {
            System.out.println("Longest word " + size.get().length());
        }
    }

    private static void reverseAndSortString() {
        System.out.println("***reverseAndSortString***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        List<String> reverseAndSorted = list.stream().map(i -> new StringBuilder(i).reverse().toString())
                .sorted()
                .toList();
        System.out.println("Actual List of String " + list);
        System.out.println("Reverse and Sorted " + reverseAndSorted);
    }
}
