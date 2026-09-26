package com.org.learning.problems;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProblemRotationCheck {

    @Test
    public void stringRotationCheck() {
        String s1 = "abcde";
        String s2 = "deabc";
        System.out.println(isRotation(s1, s2)); // Output: true
    }

    public static boolean isRotation(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        String doubled = s1 + s1;
        return doubled.contains(s2);
    }
}
