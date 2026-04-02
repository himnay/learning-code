package com.org.learning;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProblemStreamFindNonRepeat {
    void main() {
        String test = "abcabcaaccddee";
        Optional<Integer> firstNonRepeat = test.chars()
                .mapToObj(c -> c)
                .collect(Collectors.groupingBy(                      // count occurrences
                        Function.identity(),
                        LinkedHashMap::new,                          // preserve insertion order
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == 1)                      // only non-repeating
                .map(Map.Entry::getKey)
                .findFirst();                                      // or throw if you prefer

        System.out.println(firstNonRepeat); // prints: d
    }
}
