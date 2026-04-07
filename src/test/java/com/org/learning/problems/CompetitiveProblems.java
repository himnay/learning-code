package com.org.learning.problems;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class CompetitiveProblems {

    @Test
    @DisplayName("swap two numbers using 4 different ways")
    public void swapTwoNumbers() {
        // Option 1
        int a = 5, b = 10;
        int temp = a;
        a = b;
        b = temp;
        System.out.println("Option 1 : " + a + " " + b);

        // Option 2
        a = 5; b = 10;
        a = a + b; // a = 15
        b = a - b; // b = 15 - 10 = 5
        a = a - b; // a = 15 - 5 = 10
        System.out.println("Option 2 : " + a + " " + b);

        // Option 3
        a = 5; b = 10;
        a = a * b; // a = 50
        b = a / b; // b = 50 / 10 = 5
        a = a / b; // a = 50 / 5 = 10
        System.out.println("Option 3 : " + a + " " + b);

        // Option 4
        a = 5; b = 10;
        a = a ^ b; // a = 15 (1111)
        b = a ^ b; // b = 5 (0101)
        a = a ^ b; // a = 10 (1010)
        System.out.println("Option 4 : " + a + " " + b);

        // Option 5
        a = 5; b = 10;
        a = (a + b) - (b = a); // a = 10, b = 5
        System.out.println("Option 5 : " + a + " " + b);

    }

    @Test
    @DisplayName("find if 2 strings are anagrams")
    public void findIfTwoStringAreAnagrams() {

        String first = "LISTEN";
        String second = "SILENT";

        if (first == null || second == null) {
            System.out.println("Both strings should not be null");
        } else if (first.length() != second.length()) {
            System.out.println("Both strings should have same length");
        } else {
            String sortedFristString = Arrays.stream(first.split("")).sorted().filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.joining());
            String sortedSecondString = Arrays.stream(second.split("")).sorted().filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.joining());

            System.out.println("first = " + first + ", second = " + second);
            System.out.println("SortedFirstString = " + sortedFristString + ", SortedSecondString = " + sortedSecondString);
            System.out.println("Are both anagrams : " + sortedFristString.equalsIgnoreCase(sortedSecondString));
        }

    }

}
