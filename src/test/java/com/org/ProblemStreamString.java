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
    @DisplayName("occurrence of a character in a string")
    public void occurrenceNumber() {
        System.out.println("");
        String name = "HimansuNayak";
        var occurrence = Arrays.stream(name.split(""))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        System.out.println(occurrence);
    }

    @Test
    @DisplayName("filter non null and non empty string")
    public void filterNonNullString() {
        System.out.println("***filterNonNullString***");
        var names = Arrays.asList("Alice", " ", "Bob", "", "Charlie", "David", null);
        names.stream()
                .filter(name -> StringUtils.isNotBlank(name))
                .forEach(i -> System.out.println("i = " + i));
    }

    @Test
    @DisplayName("upper case string")
    public void upperCase() {
        System.out.println("***upperCase***");
        List.of("apple", "orange", "banana", "kiwi", "kiwi")
                .stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("remove duplicate numbers")
    public void removeDup() {
        System.out.println("***removeDup***");
        List.of(2, 2, 4, 3, 4, 5, 2, 5)
                .stream()
                .distinct()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("merge two list and remove duplicate numbers")
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
    @DisplayName("find the longest word in a list of string")
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
    @DisplayName("reverse each string in a list and sort the list based on the reversed string")
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
    @DisplayName("merge two list and remove all the numbers except 1 from the second list" )
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
    @DisplayName("move all the 1 from a list to the end of the list")
    public void moveAllOneToEnd() {
        System.out.println("***moveAll111ToEnd***");
        var list = List.of(1, 2, 1, 3, 1, 4, 1);

        var collectedOne = list.stream().filter(i -> i == 1).toList();
        var filterList = list.stream().filter(i -> i != 1).toList();
        var mergeList = Stream.concat(filterList.stream(), collectedOne.stream());
    }

    @Test
    @DisplayName("reverse a string")
    public void stringReverse() {
        System.out.println("***stringReverse***");
        var name = "BankOfAmerica";
        System.out.println("String : " + name);
        var reverseString = Arrays.stream(name.split(""))
                .reduce((a, b) -> b + a);
        System.out.println("Reverse String : " + reverseString.get());
    }

    @Test
    @DisplayName("sort a list of string in natural order and reverse order")
    public void sortStrings() {
        System.out.println("***sortStrings***");
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
        System.out.println("***occurrenceOfWord***");
        var list = List.of("apple", "apple", "apple", "banana", "banana", "mango");
        var collect = list.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        System.out.println(collect);
    }

    @Test
    @DisplayName("find common word in three list of string")
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
    @DisplayName("filter numbers from an alpha numeric string and sum the numbers")
    public void filterNumbersFromAlphaNumericStringAndSum() {
        System.out.println("filterNumbersFromAlphaNumericStringAndSum()");
        Character[] alphaNum = {'A', '1', 'l', '2', 'p', '3', 'h'};

        var numbers = Arrays.stream(alphaNum)
                .filter(i -> Character.isDigit(i))
                .toList();
        System.out.println(numbers);
    }

    @Test
    @DisplayName("count the minimum occurrence of a character in a string")
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
    @DisplayName("find the longest string in a list of string")
    public void findLongestString() {
        System.out.println("findLongestString()");
        var strings = List.of("Hello", "Encylopedia", "Tiger", "Architecture");

        List<String> sortedString = strings.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
        System.out.println(sortedString.get(0));
    }

    @Test
    @DisplayName("length of last word in a sentence")
    public void lengthOfLastWordInASentence() {
        System.out.println("lenghtOfLastWordInASentence()");
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


}
