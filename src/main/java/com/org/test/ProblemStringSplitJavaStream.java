package com.org.test;

import java.util.Arrays;
import java.util.stream.Collectors;

// split string with delimiter
public class ProblemStringSplitJavaStream {
    public static void main(String[] args) {
        Arrays.stream("Himansu".split(""))
                .collect(Collectors.joining(",","[", "]"));
    }
}
