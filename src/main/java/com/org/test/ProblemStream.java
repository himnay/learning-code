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

        // find missing no in a list
        findMissingNoInAList();

        // merge 111 to the end of existing list
        merge111ToList();

        // move all 111 to the end
        moveAll111ToEnd();

        // reverse a string
        stringReverse();
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

    private static void merge111ToList() {
        System.out.println("***merge111ToList***");
        var list = List.of(2, 3, 4, 2);
        var list111 = List.of(1, 4, 2, 1, 5);

        List<Integer> listOfOne = list111.stream()
                .filter(i -> i == 1)
                .toList();

        var collectedList = Stream.concat(list.stream(), listOfOne.stream()).toList();
        System.out.println("Merge List " + collectedList);
    }

    private static void moveAll111ToEnd() {
        System.out.println("***moveAll111ToEnd***");
        var list = List.of(1, 2, 1, 3, 1, 4, 1);

        var collectedOne = list.stream().filter(i -> i == 1).toList();
        var filterList = list.stream().filter(i -> i != 1).toList();
        var mergeList = Stream.concat(filterList.stream(), collectedOne.stream());
    }

    private static void stringReverse() {
        System.out.println("***stringReverse***");
        var name = "BankOfAmerica";
        System.out.println("String : " + name);
        var reverseString = Arrays.stream(name.split(""))
                .reduce((a, b) -> b + a);
        System.out.println("Reverse String : " + reverseString.get());
    }
}
