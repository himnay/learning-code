# <span style="color:hsl(132,80%,58%)">java-code</span>

A practice module for Java coding problems — Streams API exercises, classic interview / competitive-programming problems (Blind 75 style), concurrency, and design patterns. All code lives under the **test** source root (`src/test/java`) and is executed as JUnit tests (or runnable `main()` snippets).

- **Java version:** 27 (inherited from the `super-pom` parent's `java.version`, so `maven.compiler.release` is 27)
- **Dependencies:** Lombok (provided), `spring-boot-starter-test` (JUnit 5 etc.)
- **Run all tests:** `mvn test` (from this module)
- **Run one class:** `mvn test -Dtest=CompetitiveProblems`

## <span style="color:hsl(270,80%,58%)">Layout</span>

```
src/test/java/
├── ScrapPad.java                          # scratch area for quick experiments
└── com/org/learning/
    ├── dto/                               # shared records/enums used by exercises
    ├── stream/                            # Java Streams API practice
    ├── problems/                          # interview & competitive problems
    └── singleton/                         # singleton design-pattern variants
```

## <span style="color:hsl(47,80%,50%)">DTOs — [`com/org/learning/dto`](src/test/java/com/org/learning/dto)</span>

| Type                                                           | Description                                                                                           |
|----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| [`Employee`](src/test/java/com/org/learning/dto/Employee.java) | Record with compact-constructor validation (name/age/salary) plus delegating convenience constructors |
| [`Student`](src/test/java/com/org/learning/dto/Student.java)   | Simple record of `name` + `City`                                                                      |
| [`City`](src/test/java/com/org/learning/dto/City.java)         | Enum: DUBLIN, GALWAY, LIMERICK, CORK                                                                  |

## <span style="color:hsl(185,80%,58%)">Streams API — [`com/org/learning/stream`](src/test/java/com/org/learning/stream)</span>

### <span style="color:hsl(322,80%,58%)">[`ProblemStreamInt`](src/test/java/com/org/learning/stream/ProblemStreamInt.java) — numeric stream exercises</span>

| Exercise                                   | Test                                                                                               |
|--------------------------------------------|----------------------------------------------------------------------------------------------------|
| Filter even numbers                        | [`evenNumber`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L18)                    |
| Min / max of a list                        | [`minMaxNumber`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L29)                  |
| Min / max via `reduce`                     | [`minMaxNumberUsingReduce`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L45)       |
| Sum of numbers                             | [`sumNumber`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L62)                     |
| Find duplicates                            | [`findDuplicateNumbers`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L90)          |
| Find missing number in a list              | [`findMissingNoInAList`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L102)         |
| Generate even / odd numbers                | [`generateEvenOddNumbers`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L120)       |
| Second smallest / largest                  | [`secondSmallestLargest`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L137)        |
| Reverse an int array                       | [`reverseArrayOfInt`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L155)            |
| Longest / shortest word                    | [`longAndShortWord`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L167)             |
| Count even vs odd                          | [`countEvenOddNumbers`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L177)          |
| Flatten nested lists, distinct, descending | [`flattenAndDistinctDescending`](src/test/java/com/org/learning/stream/ProblemStreamInt.java#L191) |

### <span style="color:hsl(100,80%,58%)">[`ProblemStreamString`](src/test/java/com/org/learning/stream/ProblemStreamString.java) — string & collection stream exercises</span>

| Exercise                                        | Test                                                                                                               |
|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| Character occurrence count                      | [`occurrenceNumber`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L24)                           |
| Filter out null strings                         | [`filterNonNullString`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L33)                        |
| Map to upper case                               | [`upperCase`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L43)                                  |
| Remove duplicates                               | [`removeDup`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L53)                                  |
| Merge lists and de-duplicate                    | [`mergeListAndDeDup`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L63)                          |
| Longest word in a sentence                      | [`longestWord`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L81)                                |
| Reverse then sort strings                       | [`reverseAndSortString`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L92)                       |
| Merge two lists                                 | [`mergeList`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L104)                                 |
| Move all 1s to the end                          | [`moveAllOneToEnd`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L118)                           |
| Reverse a string                                | [`stringReverse`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L127)                             |
| Sort strings (asc / desc)                       | [`sortStrings`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L137)                               |
| Word occurrence count                           | [`occurrenceOfWord`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L160)                          |
| Common words between lists                      | [`commonWord`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L169)                                |
| Extract digits from alphanumeric string and sum | [`filterNumbersFromAlphaNumericStringAndSum`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L182) |
| Character with minimum occurrence               | [`countMinOccurrence`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L194)                        |
| Find longest string                             | [`findLongestString`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L208)                         |
| Length of last word in a sentence               | [`lengthOfLastWordInASentence`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L223)               |
| Split a string                                  | [`stringSplit`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L234)                               |
| Unique characters                               | [`uniqueCharacters`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L243)                          |
| Second highest salary                           | [`secondHighestSalary`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L269)                       |
| Partition into odd / even                       | [`partitionOddEven`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L288)                          |
| Group employees by location                     | [`findEmployeeBasedOnLocation`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L297)               |
| Count and sort by frequency                     | [`countSortFrequency`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L327)                        |
| Group, separate, and sort                       | [`groupSeparateAndSort`](src/test/java/com/org/learning/stream/ProblemStreamString.java#L346)                      |

### <span style="color:hsl(237,80%,58%)">Other stream / concurrency programs</span>

| Class                                                                                                 | Description                                                                                                                                                                      |
|-------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`ProblemStreamBOFA`](src/test/java/com/org/learning/stream/ProblemStreamBOFA.java)                   | Employee-dataset exercise (interview style): min/max salary overall and per department, total & average salary by department, parallel processing, partition by salary threshold |
| [`ProblemStreamFindNonRepeat`](src/test/java/com/org/learning/stream/ProblemStreamFindNonRepeat.java) | First non-repeating character using `groupingBy` into a `LinkedHashMap` to preserve insertion order                                                                              |
| [`ProgramDeadLock`](src/test/java/com/org/learning/stream/ProgramDeadLock.java)                       | Deadlock-avoidance demo: two threads acquiring `ReentrantLock`s in opposite order, defused via `tryLock` with timeout                                                            |

## <span style="color:hsl(15,80%,58%)">Interview problems — [`com/org/learning/problems`](src/test/java/com/org/learning/problems)</span>

### <span style="color:hsl(152,80%,58%)">Standalone problems</span>

| Problem                                             | Test                                                                                                                |
|-----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| String rotation check (is `s2` a rotation of `s1`?) | [`ProblemRotationCheck.stringRotationCheck`](src/test/java/com/org/learning/problems/ProblemRotationCheck.java#L11) |
| Best time to buy/sell stock                         | [`ProblemBuySellStock.buySellStock`](src/test/java/com/org/learning/problems/ProblemBuySellStock.java#L15)          |

### <span style="color:hsl(290,80%,58%)">[`CompetitiveProblems`](src/test/java/com/org/learning/problems/CompetitiveProblems.java) — Blind-75-style collection</span>

One JUnit test per problem, organized below by category. Helper data structures defined at the top of the class: [`ListNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L18), [`TreeNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L24), [`GraphNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L31), [`Trie`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L42), [`WordDictionary`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L66), [`MedianFinder`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L86), [`Codec`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L100).

#### <span style="color:hsl(67,80%,50%)">Array</span>

| Problem                              | Test                                                                                                       |
|--------------------------------------|------------------------------------------------------------------------------------------------------------|
| Two Sum                              | [`twoSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L132)                          |
| Best Time to Buy and Sell Stock      | [`bestTimeToBuyAndSellStock`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L150)       |
| Contains Duplicate                   | [`containsDuplicate`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L163)               |
| Product of Array Except Self         | [`productOfArrayExceptSelf`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L173)        |
| Maximum Subarray                     | [`maximumSubarray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L191)                 |
| Maximum Product Subarray             | [`maximumProductSubarray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L204)          |
| Find Minimum in Rotated Sorted Array | [`findMinimumInRotatedSortedArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L218) |
| Search in Rotated Sorted Array       | [`searchInRotatedSortedArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L232)      |
| 3Sum                                 | [`threeSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L251)                        |
| Container With Most Water            | [`containerWithMostWater`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L274)          |

#### <span style="color:hsl(205,80%,58%)">Binary / bit manipulation</span>

| Problem                      | Test                                                                                        |
|------------------------------|---------------------------------------------------------------------------------------------|
| Sum of Two Integers (no `+`) | [`sumOfTwoIntegers`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L286) |
| Number of 1 Bits             | [`numberOf1Bits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L298)    |
| Counting Bits                | [`countingBits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L309)     |
| Missing Number               | [`missingNumber`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L322)    |
| Reverse Bits                 | [`reverseBits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L331)      |

#### <span style="color:hsl(342,80%,58%)">Dynamic programming</span>

| Problem                        | Test                                                                                                    |
|--------------------------------|---------------------------------------------------------------------------------------------------------|
| Climbing Stairs                | [`climbingStairs`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L344)               |
| Coin Change                    | [`coinChange`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L357)                   |
| Longest Increasing Subsequence | [`longestIncreasingSubsequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L371) |
| Longest Common Subsequence     | [`longestCommonSubsequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L389)     |
| Word Break                     | [`wordBreak`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L403)                    |
| Combination Sum                | [`combinationSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L421)               |
| House Robber                   | [`houseRobber`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L441)                  |
| House Robber II                | [`houseRobberII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L454)                |
| Decode Ways                    | [`decodeWays`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L472)                   |
| Unique Paths                   | [`uniquePaths`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L486)                  |
| Jump Game                      | [`jumpGame`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L501)                     |

#### <span style="color:hsl(120,80%,58%)">Graph</span>

| Problem                        | Test                                                                                                   |
|--------------------------------|--------------------------------------------------------------------------------------------------------|
| Clone Graph                    | [`cloneGraph`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L510)                  |
| Course Schedule                | [`courseSchedule`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L532)              |
| Pacific Atlantic Water Flow    | [`pacificAtlanticWaterFlow`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L557)    |
| Number of Islands              | [`numberOfIslands`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L579)             |
| Longest Consecutive Sequence   | [`longestConsecutiveSequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L601)  |
| Alien Dictionary               | [`alienDictionary`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L618)             |
| Graph Valid Tree               | [`graphValidTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L649)              |
| Number of Connected Components | [`numberOfConnectedComponents`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L676) |

#### <span style="color:hsl(257,80%,58%)">Interval</span>

| Problem                   | Test                                                                                               |
|---------------------------|----------------------------------------------------------------------------------------------------|
| Insert Interval           | [`insertInterval`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L696)          |
| Merge Intervals           | [`mergeIntervals`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L714)          |
| Non-overlapping Intervals | [`nonOverlappingIntervals`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L729) |
| Meeting Rooms             | [`meetingRooms`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L742)            |
| Meeting Rooms II          | [`meetingRoomsII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L752)          |

#### <span style="color:hsl(35,80%,58%)">Linked list</span>

| Problem                          | Test                                                                                                  |
|----------------------------------|-------------------------------------------------------------------------------------------------------|
| Reverse a Linked List            | [`reverseALinkedList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L765)         |
| Detect Cycle in a Linked List    | [`detectCycleInALinkedList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L780)   |
| Merge Two Sorted Lists           | [`mergeTwoSortedLists`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L795)        |
| Merge K Sorted Lists             | [`mergeKSortedLists`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L809)          |
| Remove Nth Node From End of List | [`removeNthNodeFromEndOfList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L826) |
| Reorder List                     | [`reorderList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L842)                |

#### <span style="color:hsl(172,80%,58%)">Matrix</span>

| Problem           | Test                                                                                       |
|-------------------|--------------------------------------------------------------------------------------------|
| Set Matrix Zeroes | [`setMatrixZeroes`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L870) |
| Spiral Matrix     | [`spiralMatrix`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L896)    |
| Rotate Image      | [`rotateImage`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L916)     |
| Word Search       | [`wordSearch`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L937)      |

#### <span style="color:hsl(310,80%,58%)">String</span>

| Problem                                        | Test                                                                                                                  |
|------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| Longest Substring Without Repeating Characters | [`longestSubstringWithoutRepeatingCharacters`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L960) |
| Longest Repeating Character Replacement        | [`longestRepeatingCharacterReplacement`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L974)       |
| Minimum Window Substring                       | [`minimumWindowSubstring`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L988)                     |
| Valid Anagram                                  | [`validAnagram`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1012)                              |
| Group Anagrams                                 | [`groupAnagrams`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1024)                             |
| Valid Parentheses                              | [`validParentheses`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1038)                          |
| Valid Palindrome                               | [`validPalindrome`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1052)                           |
| Longest Palindromic Substring                  | [`longestPalindromicSubstring`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1067)               |
| Palindromic Substrings                         | [`palindromicSubstrings`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1086)                     |
| Encode and Decode Strings                      | [`encodeAndDecodeStrings`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1107)                    |

#### <span style="color:hsl(87,80%,58%)">Tree</span>

| Problem                                         | Test                                                                                                                           |
|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| Maximum Depth of Binary Tree                    | [`maximumDepthOfBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1115)                           |
| Same Tree                                       | [`sameTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1131)                                           |
| Invert / Flip Binary Tree                       | [`invertFlipBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1145)                               |
| Binary Tree Maximum Path Sum                    | [`binaryTreeMaximumPathSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1165)                           |
| Binary Tree Level Order Traversal               | [`binaryTreeLevelOrderTraversal`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1186)                      |
| Serialize and Deserialize Binary Tree           | [`serializeAndDeserializeBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1212)                  |
| Subtree of Another Tree                         | [`subtreeOfAnotherTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1264)                               |
| Construct Binary Tree from Preorder and Inorder | [`constructBinaryTreeFromPreorderAndInorderTraversal`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1282) |
| Validate Binary Search Tree                     | [`validateBinarySearchTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1305)                           |
| Kth Smallest Element in a BST                   | [`kthSmallestElementInABST`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1320)                           |
| Lowest Common Ancestor of BST                   | [`lowestCommonAncestorOfBST`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1339)                          |
| Implement Trie (Prefix Tree)                    | [`implementTriePrefixTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1355)                            |
| Add and Search Word                             | [`addAndSearchWord`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1365)                                   |
| Word Search II                                  | [`wordSearchII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1376)                                       |

#### <span style="color:hsl(225,80%,58%)">Heap</span>

| Problem                      | Test                                                                                                   |
|------------------------------|--------------------------------------------------------------------------------------------------------|
| Merge K Sorted Lists (heap)  | [`mergeKSortedListsUsingHeap`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1408) |
| Top K Frequent Elements      | [`topKFrequentElements`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1424)       |
| Find Median from Data Stream | [`findMedianFromDataStream`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1440)   |
| Find Kth Largest Element     | [`findKthLargestElement`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1451)      |

#### <span style="color:hsl(2,80%,58%)">Misc</span>

| Problem                                | Test                                                                                       |
|----------------------------------------|--------------------------------------------------------------------------------------------|
| Swap Two Numbers (multiple approaches) | [`swapTwoNumbers`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1462) |
| String Anagrams                        | [`stringAnagrams`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1500) |
| Two Sum (variant)                      | [`twoSumProblem`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1522)  |
| Rotate Array                           | [`rotateArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1556)    |

## <span style="color:hsl(140,80%,58%)">Design patterns — [`com/org/learning/singleton`](src/test/java/com/org/learning/singleton)</span>

| Class                                                                                        | Description                                                                                                                       |
|----------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [`ProblemSingleton`](src/test/java/com/org/learning/singleton/ProblemSingleton.java)         | Classic singleton hardened against reflection, serialization, and cloning attacks (`Serializable` + `Cloneable` with protections) |
| [`ProblemSingletonEnum`](src/test/java/com/org/learning/singleton/ProblemSingletonEnum.java) | Enum-based singleton (Effective Java approach) with a demo `Main`                                                                 |

## <span style="color:hsl(277,80%,58%)">Scratch</span>

[`ScrapPad`](src/test/java/ScrapPad.java) (default package) — quick experiments: the BOFA employee exercise plus small helpers (average, case change, even/odd sum, de-dup, starts-with filtering).

---
*Note: links above point to specific line numbers and may drift as files are edited.*
