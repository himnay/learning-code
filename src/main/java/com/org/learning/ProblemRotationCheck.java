package com.org.learning;

public class ProblemRotationCheck {
    public static void main(String[] args) {
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
