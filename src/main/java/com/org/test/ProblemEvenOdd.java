package com.org.test;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

// find even or odd number using stream
public class ProblemEvenOdd {
    public static void main(String[] args) {
        IntStream.range(1, 10)
                .filter(i -> i % 2 == 0)
                .boxed() // need boxed() as IntStream produce int and need to be wrap to Integer
                .collect(toList());
    }
}