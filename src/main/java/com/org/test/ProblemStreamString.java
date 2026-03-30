package com.org.test;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProblemStreamString {
    void main() {

        // find the occurrence
        occurrenceNumber();

        // filter non null string
        filterNonNullString();

        // uppercase
        upperCase();

        // remove duplicate
        removeDup();

        // merge 2 list and remove duplidate
        mergeListAndDeDup();

        // longest word from a list of string
        longestWord();

        // reverse and sorted a list of string
        reverseAndSortString();

        // merge 111 to the end of existing list
        mergeList();

        // move all 111 to the end
        moveAllOneToEnd();

        // reverse a string
        stringReverse();

        // sort a list of string
        sortStrings();

        // occurrence of word
        occurrenceOfWord();

        // common word across 3 list
        commonWord();

        // identify numbers from a alpha-numeric string and sum it
        filterNumbersFromAlphaNumericStringAndSum();

        // find the min occurence of a character in array of chars
        countMinOccurrence();

        // find the longest string in a list of string
        findLongestString();

        // length of last word in a sentence
        lenghtOfLastWordInASentence();
    }

    private static void occurrenceNumber() {
        System.out.println("***occurrenceNumber***");
        String name = "HimansuNayak";
        var occurrence = Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurrence);
    }

    private static void filterNonNullString() {
        System.out.println("***filterNonNullString***");
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    private static void upperCase() {
        System.out.println("***upperCase***");
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
        var firstList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        var secondList = List.of(5, 6, 7, 8, 9, 10);

        var mergeList = Stream.concat(firstList.stream(), secondList.stream())
                .distinct()
                .toList();
        System.out.println(mergeList);
    }

    private static void longestWord() {
        System.out.println("***longestWord***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        Optional<String> size = list.stream()
                .max(Comparator.comparingInt(String::length));
        if (size.isPresent()) {
            System.out.println("Longest word " + size.get().length());
        }
    }

    private static void reverseAndSortString() {
        System.out.println("***reverseAndSortString***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        List<String> reverseAndSorted = list.stream()
                .map(i -> new StringBuilder(i).reverse().toString())
                .sorted()
                .toList();
        System.out.println("Actual List of String " + list);
        System.out.println("Reverse and Sorted " + reverseAndSorted);
    }

    private static void mergeList() {
        System.out.println("***mergeList***");
        var listA = List.of(2, 3, 4, 2);
        var listB = List.of(1, 4, 2, 1, 5);

        List<Integer> listOfOne = listB.stream()
                .filter(i -> i == 1)
                .toList();

        var collectedList = Stream.concat(listA.stream(), listOfOne.stream()).toList();
        System.out.println("Merge List " + collectedList);
    }

    private static void moveAllOneToEnd() {
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

    private static void sortStrings() {
        System.out.println("***sortStrings***");
        var list = List.of("Test", "Mat", "RAT", "Apple", "Zebra");

        list.stream().sorted().forEach(System.out::println);
        list.stream().sorted(String::compareTo).forEach(System.out::println);
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    private static void occurrenceOfWord() {
        System.out.println("***occurrenceOfWord***");
        var list = List.of("apple", "apple", "apple", "banana", "banana", "mango");
        var collect = list.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        System.out.println(collect);
    }

    private static void commonWord() {
        System.out.println("***commonWord***");
        var listA = List.of("I", "am","a","Hero");
        var listB = List.of("Hero","are","good");
        var listC = List.of("Good","Hero","are","paid","well");

        var commonWord = listA.stream()
                .filter(i -> listB.contains(i) && listC.contains(i))
                .toList();
        System.out.println(commonWord);
    }

    private static void filterNumbersFromAlphaNumericStringAndSum() {
        System.out.println("filterNumbersFromAlphaNumericStringAndSum()");
        Character[] alphaNum = {'A','1','l','2','p','3','h'};

        var numbers = Arrays.stream(alphaNum)
                .filter(i -> Character.isDigit(i))
                .toList();
        System.out.println(numbers);
    }

    private static void countMinOccurrence() {
        System.out.println("countMinOccurrence()");
        Character [] characters = {'a', 'b', 'a', 'b', 'c','d','d','d'};
        Map<Character, Long> collect = Arrays.stream(characters)
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        // sort and transfer to a linked hashmap
        var result = collect.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(result);
    }

    private static void findLongestString() {
        System.out.println("findLongestString()");
        var strings = List.of("Hello", "Encylopedia", "Tiger", "Architecture");

        List<String> sortedString = strings.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
        System.out.println(sortedString.get(0));
    }

    private static void lenghtOfLastWordInASentence() {
        System.out.println("lenghtOfLastWordInASentence()");
        Optional<Integer> stringLength = Arrays.stream(string.split(" "))
                .reduce((a, b) -> b)
                .map(String::length);

        System.out.println(stringLength.get());
    }
}
