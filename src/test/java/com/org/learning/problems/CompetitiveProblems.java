package com.org.learning.problems;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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


}
