package com.org.test;

import java.util.Arrays;
import java.util.stream.Collectors;

// split string with delimiter
public class ProblemStringSplitJavaStream {
    void main() {
        Arrays.stream("Himansu".split(""))
                .collect(Collectors.joining(",","[", "]"));
    }
}
