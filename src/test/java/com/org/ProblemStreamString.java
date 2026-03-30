package com.org;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class ProblemStreamString {

    @Test
    @DisplayName("")
    public void occurrenceNumber() {
        System.out.println("");
        String name = "HimansuNayak";
        var occurrence = Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurrence);
    }

    @Test
    @DisplayName("")
    public void filterNonNullString() {
        System.out.println("***filterNonNullString***");
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    @Test
    @DisplayName("")
    public void upperCase() {
        System.out.println("***upperCase***");
        List.of("apple", "orange", "banana", "kiwi", "kiwi")
                .stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("")
    public void removeDup() {
        System.out.println("***removeDup***");
        List.of(2, 2, 4, 3, 4, 5, 2, 5)
                .stream()
                .distinct()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("")
    public void mergeListAndDeDup() {
        System.out.println("***mergeListAndDeDup***");
        var firstList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        var secondList = List.of(5, 6, 7, 8, 9, 10);

        var mergeList = Stream.concat(firstList.stream(), secondList.stream())
                .distinct()
                .toList();
        System.out.println(mergeList);
    }

    @Test
    @DisplayName("")
    public void longestWord() {
        System.out.println("***longestWord***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        Optional<String> size = list.stream()
                .max(Comparator.comparingInt(String::length));
        if (size.isPresent()) {
            System.out.println("Longest word " + size.get().length());
        }
    }

    @Test
    @DisplayName("")
    public void reverseAndSortString() {
        System.out.println("***reverseAndSortString***");
        var list = List.of("alpha", "Medicine", "Encylopedia", "Hippopotamus", "Dolphin");
        List<String> reverseAndSorted = list.stream()
                .map(i -> new StringBuilder(i).reverse().toString())
                .sorted()
                .toList();
        System.out.println("Actual List of String " + list);
        System.out.println("Reverse and Sorted " + reverseAndSorted);
    }

    @Test
    @DisplayName("")
    public void mergeList() {
        System.out.println("***mergeList***");
        var listA = List.of(2, 3, 4, 2);
        var listB = List.of(1, 4, 2, 1, 5);

        List<Integer> listOfOne = listB.stream()
                .filter(i -> i == 1)
                .toList();

        var collectedList = Stream.concat(listA.stream(), listOfOne.stream()).toList();
        System.out.println("Merge List " + collectedList);
    }

    @Test
    @DisplayName("")
    public void moveAllOneToEnd() {
        System.out.println("***moveAll111ToEnd***");
        var list = List.of(1, 2, 1, 3, 1, 4, 1);

        var collectedOne = list.stream().filter(i -> i == 1).toList();
        var filterList = list.stream().filter(i -> i != 1).toList();
        var mergeList = Stream.concat(filterList.stream(), collectedOne.stream());
    }

    @Test
    @DisplayName("")
    public void stringReverse() {
        System.out.println("***stringReverse***");
        var name = "BankOfAmerica";
        System.out.println("String : " + name);
        var reverseString = Arrays.stream(name.split(""))
                .reduce((a, b) -> b + a);
        System.out.println("Reverse String : " + reverseString.get());
    }

    @Test
    @DisplayName("")
    public void sortStrings() {
        System.out.println("***sortStrings***");
        var list = List.of("Test", "Mat", "RAT", "Apple", "Zebra");

        list.stream().sorted().forEach(System.out::println);
        list.stream().sorted(String::compareTo).forEach(System.out::println);
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    @Test
    @DisplayName("")
    public void occurrenceOfWord() {
        System.out.println("***occurrenceOfWord***");
        var list = List.of("apple", "apple", "apple", "banana", "banana", "mango");
        var collect = list.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        System.out.println(collect);
    }

    @Test
    @DisplayName("")
    public void commonWord() {
        System.out.println("***commonWord***");
        var listA = List.of("I", "am", "a", "Hero");
        var listB = List.of("Hero", "are", "good");
        var listC = List.of("Good", "Hero", "are", "paid", "well");

        var commonWord = listA.stream()
                .filter(i -> listB.contains(i) && listC.contains(i))
                .toList();
        System.out.println(commonWord);
    }

    @Test
    @DisplayName("")
    public void filterNumbersFromAlphaNumericStringAndSum() {
        System.out.println("filterNumbersFromAlphaNumericStringAndSum()");
        Character[] alphaNum = {'A', '1', 'l', '2', 'p', '3', 'h'};

        var numbers = Arrays.stream(alphaNum)
                .filter(i -> Character.isDigit(i))
                .toList();
        System.out.println(numbers);
    }

    @Test
    @DisplayName("")
    public void countMinOccurrence() {
        System.out.println("countMinOccurrence()");
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
    @DisplayName("")
    public void findLongestString() {
        System.out.println("findLongestString()");
        var strings = List.of("Hello", "Encylopedia", "Tiger", "Architecture");

        List<String> sortedString = strings.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
        System.out.println(sortedString.get(0));
    }

    @Test
    @DisplayName("")
    public void lenghtOfLastWordInASentence() {
        System.out.println("lenghtOfLastWordInASentence()");
        var string = "Good Morning. How are you?";
        Optional<Integer> stringLength = Arrays.stream(string.split(" "))
                .reduce((a, b) -> b)
                .map(String::length);

        System.out.println(stringLength.get());
    }

    @Test
    @DisplayName("")
    public void stringSplit() {

        Arrays.stream("Himansu".split(""))
                .collect(Collectors.joining(",", "[", "]"));

    }
}
