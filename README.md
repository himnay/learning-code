# <span style="color:hsl(132,80%,58%)">learning-code</span>

## <span style="color:hsl(270,80%,58%)">Table of contents</span>

1. 📖 [Overview](#overview)
2. 🚀 [Build and run](#build-and-run)
    - 2.1 [Prerequisites](#prerequisites)
    - 2.2 [Running the tests](#running-the-tests)
    - 2.3 [Continuous integration](#continuous-integration)
3. 📊 [Test reports](#test-reports)
    - 3.1 [Test knowledge graph](#test-knowledge-graph)
    - 3.2 [Allure report](#allure-report)
4. 🗂️ [Project layout](#project-layout)
5. 🧱 [DTOs](#dtos)
6. 🌊 [Streams API](#streams-api)
    - 6.1 [ProblemStreamInt — numeric stream exercises](#problem-stream-int)
    - 6.2 [ProblemStreamString — string and collection stream exercises](#problem-stream-string)
    - 6.3 [Other stream and concurrency programs](#other-stream-programs)
7. 🧩 [Interview problems](#interview-problems)
    - 7.1 [Standalone problems](#standalone-problems)
    - 7.2 [CompetitiveProblems — Blind-75-style collection](#competitive-problems)
8. 🏗️ [Design patterns](#design-patterns)
9. 📝 [Scratch pad](#scratch-pad)

<a id="overview"></a>
## <span style="color:hsl(47,80%,50%)">1. 📖 Overview</span>

A practice project for Java coding problems: Streams API exercises, classic interview and
competitive-programming problems (Blind 75 style), concurrency, and design patterns. All code lives
under the **test** source root (`src/test/java`) and runs as JUnit tests, or as runnable `main()`
snippets.

This project used to be the `java-code/` module of the `learning` notes repository, and it moved here
with its git history. The notes link to these examples instead of copying code.

| Concern | Choice                                                                                              |
|---------|-----------------------------------------------------------------------------------------------------|
| Java    | 27, inherited from the `super-pom` 1.2.0 parent's `java.version`, so `maven.compiler.release` is 27 |
| Tests   | JUnit 6, AssertJ and Mockito, from `spring-boot-starter-test` (Spring Boot 4.1.1)                   |
| Helpers | Lombok 1.18.48 (provided); `commons-lang3` and `commons-collections4` in tests                      |
| Reports | Allure 2 and a test knowledge graph: see [Test reports](#test-reports)                              |

<a id="build-and-run"></a>
## <span style="color:hsl(185,80%,58%)">2. 🚀 Build and run</span>

<a id="prerequisites"></a>
### <span style="color:hsl(322,80%,58%)">2.1 Prerequisites</span>

JDK 27 and Maven 3.9+. The parent POM `com.org.llm:super-pom` and the `learning-bom` it imports are not on Maven Central, so install both once from their own repositories:

```bash
git clone https://github.com/himnay/learning-bom && (cd learning-bom && mvn -N install)
git clone https://github.com/himnay/super-pom && (cd super-pom && mvn -N install)
```

<a id="running-the-tests"></a>
### <span style="color:hsl(100,80%,58%)">2.2 Running the tests</span>

```bash
mvn test                                       # every exercise, from the repo root
mvn test -Dtest=CompetitiveProblems            # one class
mvn test -Dtest='CompetitiveProblems#twoSum'   # one test method
```

The exercises are named after the problem they solve (`Problem*`, `CompetitiveProblems`), not
`*Test`, so the pom adds those names to Surefire's includes. Without that, Surefire's default
patterns would skip them. From the IDE, they run like any other JUnit test.

<a id="continuous-integration"></a>
### <span style="color:hsl(237,80%,58%)">2.3 Continuous integration</span>

[GitHub Actions](.github/workflows/ci.yml) runs on every push and pull request to `main`:

1. It checks out `learning-bom` and `super-pom` and installs them, since they are not on Maven
   Central.
2. It runs `mvn verify` on Temurin 27.
3. It builds the Allure report and uploads it, with the knowledge graph, as the run's `test-reports`
   artifact. This step runs even when a test fails.

<a id="test-reports"></a>
## <span style="color:hsl(15,80%,58%)">3. 📊 Test reports</span>

Every test run feeds two reports. Each is a single self-contained HTML file that opens straight from
the file system, and CI uploads both as the `test-reports` artifact of every run.

| Report               | Produced by                              | Open                                         |
|----------------------|------------------------------------------|----------------------------------------------|
| Test knowledge graph | `mvn test`, or any test run from the IDE | `target/test-graph/index.html`               |
| Allure report        | `mvn test`, then `mvn allure:report`     | `target/site/allure-maven-plugin/index.html` |

<a id="test-knowledge-graph"></a>
### <span style="color:hsl(152,80%,58%)">3.1 Test knowledge graph</span>

A graph of packages → classes → tests, plus the classes each test class uses (the DTOs). Each package
has its own colour, which its classes share. A test's circle shows its last result instead: green
passed, red failed, amber skipped, grey not run. Clicking a node opens a panel:

- ***test***: its description (the `@DisplayName`), its result with the duration (and the stack trace
  of a failure), and its source code;
- ***class***: a table of its tests with descriptions and results, the classes it uses or is used by,
  and its source;
- ***package*** and ***project***: counts, with the failed tests listed first.

The legend chips are the packages: clicking one shows or hides that package with its classes and
tests. Search matches descriptions, method names and class names. Every test and class links to its
source on GitHub and in VS Code.

[`KnowledgeGraphListener`](src/test/java/com/org/learning/report/KnowledgeGraphListener.java) writes the
page. It is a JUnit Platform `TestExecutionListener`, registered in
[`META-INF/services`](src/test/resources/META-INF/services/org.junit.platform.launcher.TestExecutionListener)
the same way the Allure adapter hooks in, so no test needs extra code. A test without `@DisplayName`
is described by the comment above it, or else by its method name. `-Dtest.graph.enabled=false` turns
the page off.

Open `target/test-graph/index.html`, for example from the `[test-graph] file:///…` line that
`mvn test` prints. `src/test/resources/test-graph/template.html` is only the empty template the
listener fills in.

<a id="allure-report"></a>
### <span style="color:hsl(290,80%,58%)">3.2 Allure report</span>

The report is [Allure 2](https://github.com/allure-framework/allure2). The `allure-jupiter` adapter
(allure-java 3.0.0) records every test into `target/allure-results`, and the `allure-maven` plugin
(3.1.0, set to report version 2.46.1) builds the report from those results:

```bash
mvn test            # runs the tests and writes target/allure-results
mvn allure:report   # builds target/site/allure-maven-plugin/index.html (a single file)
mvn allure:serve    # builds the report into a temporary folder and opens it in the browser
```

Each test's `@DisplayName` becomes its name in the report. On first use the plugin downloads the Allure
2 command line into `.allure/`, which git ignores.

<a id="project-layout"></a>
## <span style="color:hsl(67,80%,50%)">4. 🗂️ Project layout</span>

```
src/test/java/
├── ScrapPad.java                          # scratch area for quick experiments
└── com/org/learning/
    ├── core/stream/                       # Java Streams API practice
    │   └── thread/singleton/              # singleton design-pattern variants
    ├── dto/                               # shared records/enums used by exercises
    ├── problems/                          # interview & competitive problems
    └── report/                            # test knowledge graph (a JUnit Platform listener)
src/test/resources/
├── META-INF/services/                     # registers the knowledge graph listener
├── allure.properties                      # where Allure writes its results
└── test-graph/template.html               # the knowledge graph page
```

<a id="dtos"></a>
## <span style="color:hsl(205,80%,58%)">5. 🧱 DTOs</span>

**Package:** [`com/org/learning/dto`](src/test/java/com/org/learning/dto)

| Type                                                           | Description                                                                                           |
|----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| [`Employee`](src/test/java/com/org/learning/dto/Employee.java) | Record with compact-constructor validation (name/age/salary) plus delegating convenience constructors |
| [`Student`](src/test/java/com/org/learning/dto/Student.java)   | Simple record of `name` + `City`                                                                      |
| [`City`](src/test/java/com/org/learning/dto/City.java)         | Enum: DUBLIN, GALWAY, LIMERICK, CORK                                                                  |

<a id="streams-api"></a>
## <span style="color:hsl(342,80%,58%)">6. 🌊 Streams API</span>

**Package:** [`com/org/learning/core/stream`](src/test/java/com/org/learning/core/stream)

<a id="problem-stream-int"></a>
### <span style="color:hsl(120,80%,58%)">6.1 ProblemStreamInt — numeric stream exercises</span>

**Class:** [`ProblemStreamInt`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java)

| Exercise                                   | Test                                                                                                    |
|--------------------------------------------|---------------------------------------------------------------------------------------------------------|
| Filter even numbers                        | [`evenNumber`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L18)                    |
| Min / max of a list                        | [`minMaxNumber`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L29)                  |
| Min / max via `reduce`                     | [`minMaxNumberUsingReduce`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L45)       |
| Sum of numbers                             | [`sumNumber`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L62)                     |
| Find duplicates                            | [`findDuplicateNumbers`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L90)          |
| Find missing number in a list              | [`findMissingNoInAList`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L102)         |
| Generate even / odd numbers                | [`generateEvenOddNumbers`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L120)       |
| Second smallest / largest                  | [`secondSmallestLargest`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L137)        |
| Reverse an int array                       | [`reverseArrayOfInt`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L155)            |
| Longest / shortest word                    | [`longAndShortWord`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L167)             |
| Count even vs odd                          | [`countEvenOddNumbers`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L177)          |
| Flatten nested lists, distinct, descending | [`flattenAndDistinctDescending`](src/test/java/com/org/learning/core/stream/ProblemStreamInt.java#L191) |

<a id="problem-stream-string"></a>
### <span style="color:hsl(257,80%,58%)">6.2 ProblemStreamString — string and collection stream exercises</span>

**Class:** [`ProblemStreamString`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java)

| Exercise                                        | Test                                                                                                                    |
|-------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| Character occurrence count                      | [`occurrenceNumber`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L24)                           |
| Filter out null strings                         | [`filterNonNullString`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L33)                        |
| Map to upper case                               | [`upperCase`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L43)                                  |
| Remove duplicates                               | [`removeDup`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L53)                                  |
| Merge lists and de-duplicate                    | [`mergeListAndDeDup`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L63)                          |
| Longest word in a sentence                      | [`longestWord`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L81)                                |
| Reverse then sort strings                       | [`reverseAndSortString`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L92)                       |
| Merge two lists                                 | [`mergeList`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L104)                                 |
| Move all 1s to the end                          | [`moveAllOneToEnd`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L118)                           |
| Reverse a string                                | [`stringReverse`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L127)                             |
| Sort strings (asc / desc)                       | [`sortStrings`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L137)                               |
| Word occurrence count                           | [`occurrenceOfWord`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L160)                          |
| Common words between lists                      | [`commonWord`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L169)                                |
| Extract digits from alphanumeric string and sum | [`filterNumbersFromAlphaNumericStringAndSum`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L182) |
| Character with minimum occurrence               | [`countMinOccurrence`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L194)                        |
| Find longest string                             | [`findLongestString`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L208)                         |
| Length of last word in a sentence               | [`lengthOfLastWordInASentence`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L223)               |
| Split a string                                  | [`stringSplit`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L234)                               |
| Unique characters                               | [`uniqueCharacters`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L243)                          |
| Second highest salary                           | [`secondHighestSalary`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L269)                       |
| Partition into odd / even                       | [`partitionOddEven`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L288)                          |
| Group employees by location                     | [`findEmployeeBasedOnLocation`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L297)               |
| Count and sort by frequency                     | [`countSortFrequency`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L327)                        |
| Group, separate, and sort                       | [`groupSeparateAndSort`](src/test/java/com/org/learning/core/stream/ProblemStreamString.java#L346)                      |

<a id="other-stream-programs"></a>
### <span style="color:hsl(35,80%,58%)">6.3 Other stream and concurrency programs</span>

| Class                                                                                                      | Description                                                                                                                                                                      |
|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`ProblemStreamBOFA`](src/test/java/com/org/learning/core/stream/ProblemStreamBOFA.java)                   | Employee-dataset exercise (interview style): min/max salary overall and per department, total & average salary by department, parallel processing, partition by salary threshold |
| [`ProblemStreamFindNonRepeat`](src/test/java/com/org/learning/core/stream/ProblemStreamFindNonRepeat.java) | First non-repeating character using `groupingBy` into a `LinkedHashMap` to preserve insertion order                                                                              |
| [`ProgramDeadLock`](src/test/java/com/org/learning/core/stream/ProgramDeadLock.java)                       | Deadlock-avoidance demo: two threads acquiring `ReentrantLock`s in opposite order, defused via `tryLock` with timeout                                                            |

<a id="interview-problems"></a>
## <span style="color:hsl(172,80%,58%)">7. 🧩 Interview problems</span>

**Package:** [`com/org/learning/problems`](src/test/java/com/org/learning/problems)

<a id="standalone-problems"></a>
### <span style="color:hsl(310,80%,58%)">7.1 Standalone problems</span>

| Problem                                             | Test                                                                                                                |
|-----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| String rotation check (is `s2` a rotation of `s1`?) | [`ProblemRotationCheck.stringRotationCheck`](src/test/java/com/org/learning/problems/ProblemRotationCheck.java#L11) |
| Best time to buy/sell stock                         | [`ProblemBuySellStock.buySellStock`](src/test/java/com/org/learning/problems/ProblemBuySellStock.java#L15)          |

<a id="competitive-problems"></a>
### <span style="color:hsl(87,80%,58%)">7.2 CompetitiveProblems — Blind-75-style collection</span>

**Class:** [`CompetitiveProblems`](src/test/java/com/org/learning/problems/CompetitiveProblems.java)

One JUnit test per problem, organized below by category. Helper data structures defined at the top of the class: [`ListNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L22), [`TreeNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L32), [`GraphNode`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L41), [`Trie`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L51), [`WordDictionary`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L79), [`MedianFinder`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L104), [`Codec`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L121).

**Categories:** [Array](#cp-array) · [Binary / bit manipulation](#cp-binary) · [Dynamic programming](#cp-dynamic-programming) · [Graph](#cp-graph) · [Interval](#cp-interval) · [Linked list](#cp-linked-list) · [Matrix](#cp-matrix) · [String](#cp-string) · [Tree](#cp-tree) · [Heap](#cp-heap) · [Misc](#cp-misc)

<a id="cp-array"></a>
#### <span style="color:hsl(225,80%,58%)">Array</span>

| Problem                              | Test                                                                                                       |
|--------------------------------------|------------------------------------------------------------------------------------------------------------|
| Two Sum                              | [`twoSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L154)                          |
| Best Time to Buy and Sell Stock      | [`bestTimeToBuyAndSellStock`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L172)       |
| Contains Duplicate                   | [`containsDuplicate`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L185)               |
| Product of Array Except Self         | [`productOfArrayExceptSelf`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L199)        |
| Maximum Subarray                     | [`maximumSubarray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L217)                 |
| Maximum Product Subarray             | [`maximumProductSubarray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L230)          |
| Find Minimum in Rotated Sorted Array | [`findMinimumInRotatedSortedArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L244) |
| Search in Rotated Sorted Array       | [`searchInRotatedSortedArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L262)      |
| 3Sum                                 | [`threeSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L284)                        |
| Container With Most Water            | [`containerWithMostWater`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L307)          |

<a id="cp-binary"></a>
#### <span style="color:hsl(2,80%,58%)">Binary / bit manipulation</span>

| Problem                      | Test                                                                                        |
|------------------------------|---------------------------------------------------------------------------------------------|
| Sum of Two Integers (no `+`) | [`sumOfTwoIntegers`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L320) |
| Number of 1 Bits             | [`numberOf1Bits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L332)    |
| Counting Bits                | [`countingBits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L343)     |
| Missing Number               | [`missingNumber`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L356)    |
| Reverse Bits                 | [`reverseBits`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L365)      |

<a id="cp-dynamic-programming"></a>
#### <span style="color:hsl(140,80%,58%)">Dynamic programming</span>

| Problem                        | Test                                                                                                    |
|--------------------------------|---------------------------------------------------------------------------------------------------------|
| Climbing Stairs                | [`climbingStairs`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L378)               |
| Coin Change                    | [`coinChange`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L391)                   |
| Longest Increasing Subsequence | [`longestIncreasingSubsequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L405) |
| Longest Common Subsequence     | [`longestCommonSubsequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L424)     |
| Word Break                     | [`wordBreak`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L438)                    |
| Combination Sum                | [`combinationSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L456)               |
| House Robber                   | [`houseRobber`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L479)                  |
| House Robber II                | [`houseRobberII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L492)                |
| Decode Ways                    | [`decodeWays`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L510)                   |
| Unique Paths                   | [`uniquePaths`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L525)                  |
| Jump Game                      | [`jumpGame`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L540)                     |

<a id="cp-graph"></a>
#### <span style="color:hsl(277,80%,58%)">Graph</span>

| Problem                        | Test                                                                                                   |
|--------------------------------|--------------------------------------------------------------------------------------------------------|
| Clone Graph                    | [`cloneGraph`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L549)                  |
| Course Schedule                | [`courseSchedule`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L572)              |
| Pacific Atlantic Water Flow    | [`pacificAtlanticWaterFlow`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L597)    |
| Number of Islands              | [`numberOfIslands`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L625)             |
| Longest Consecutive Sequence   | [`longestConsecutiveSequence`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L650)  |
| Alien Dictionary               | [`alienDictionary`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L667)             |
| Graph Valid Tree               | [`graphValidTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L702)              |
| Number of Connected Components | [`numberOfConnectedComponents`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L732) |

<a id="cp-interval"></a>
#### <span style="color:hsl(55,80%,50%)">Interval</span>

| Problem                   | Test                                                                                               |
|---------------------------|----------------------------------------------------------------------------------------------------|
| Insert Interval           | [`insertInterval`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L759)          |
| Merge Intervals           | [`mergeIntervals`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L777)          |
| Non-overlapping Intervals | [`nonOverlappingIntervals`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L795) |
| Meeting Rooms             | [`meetingRooms`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L808)            |
| Meeting Rooms II          | [`meetingRoomsII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L818)          |

<a id="cp-linked-list"></a>
#### <span style="color:hsl(192,80%,58%)">Linked list</span>

| Problem                          | Test                                                                                                  |
|----------------------------------|-------------------------------------------------------------------------------------------------------|
| Reverse a Linked List            | [`reverseALinkedList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L831)         |
| Detect Cycle in a Linked List    | [`detectCycleInALinkedList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L846)   |
| Merge Two Sorted Lists           | [`mergeTwoSortedLists`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L867)        |
| Merge K Sorted Lists             | [`mergeKSortedLists`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L886)          |
| Remove Nth Node From End of List | [`removeNthNodeFromEndOfList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L903) |
| Reorder List                     | [`reorderList`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L922)                |

<a id="cp-matrix"></a>
#### <span style="color:hsl(330,80%,58%)">Matrix</span>

| Problem           | Test                                                                                       |
|-------------------|--------------------------------------------------------------------------------------------|
| Set Matrix Zeroes | [`setMatrixZeroes`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L955) |
| Spiral Matrix     | [`spiralMatrix`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L981)    |
| Rotate Image      | [`rotateImage`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1001)    |
| Word Search       | [`wordSearch`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1023)     |

<a id="cp-string"></a>
#### <span style="color:hsl(107,80%,58%)">String</span>

| Problem                                        | Test                                                                                                                   |
|------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| Longest Substring Without Repeating Characters | [`longestSubstringWithoutRepeatingCharacters`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1046) |
| Longest Repeating Character Replacement        | [`longestRepeatingCharacterReplacement`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1060)       |
| Minimum Window Substring                       | [`minimumWindowSubstring`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1075)                     |
| Valid Anagram                                  | [`validAnagram`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1103)                               |
| Group Anagrams                                 | [`groupAnagrams`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1115)                              |
| Valid Parentheses                              | [`validParentheses`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1129)                           |
| Valid Palindrome                               | [`validPalindrome`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1143)                            |
| Longest Palindromic Substring                  | [`longestPalindromicSubstring`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1162)                |
| Palindromic Substrings                         | [`palindromicSubstrings`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1184)                      |
| Encode and Decode Strings                      | [`encodeAndDecodeStrings`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1206)                     |

<a id="cp-tree"></a>
#### <span style="color:hsl(245,80%,58%)">Tree</span>

| Problem                                         | Test                                                                                                                           |
|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| Maximum Depth of Binary Tree                    | [`maximumDepthOfBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1214)                           |
| Same Tree                                       | [`sameTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1230)                                           |
| Invert / Flip Binary Tree                       | [`invertFlipBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1248)                               |
| Binary Tree Maximum Path Sum                    | [`binaryTreeMaximumPathSum`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1271)                           |
| Binary Tree Level Order Traversal               | [`binaryTreeLevelOrderTraversal`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1292)                      |
| Serialize and Deserialize Binary Tree           | [`serializeAndDeserializeBinaryTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1318)                  |
| Subtree of Another Tree                         | [`subtreeOfAnotherTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1375)                               |
| Construct Binary Tree from Preorder and Inorder | [`constructBinaryTreeFromPreorderAndInorderTraversal`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1396) |
| Validate Binary Search Tree                     | [`validateBinarySearchTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1419)                           |
| Kth Smallest Element in a BST                   | [`kthSmallestElementInABST`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1434)                           |
| Lowest Common Ancestor of BST                   | [`lowestCommonAncestorOfBST`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1456)                          |
| Implement Trie (Prefix Tree)                    | [`implementTriePrefixTree`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1474)                            |
| Add and Search Word                             | [`addAndSearchWord`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1484)                                   |
| Word Search II                                  | [`wordSearchII`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1497)                                       |

<a id="cp-heap"></a>
#### <span style="color:hsl(22,80%,58%)">Heap</span>

| Problem                      | Test                                                                                                   |
|------------------------------|--------------------------------------------------------------------------------------------------------|
| Merge K Sorted Lists (heap)  | [`mergeKSortedListsUsingHeap`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1530) |
| Top K Frequent Elements      | [`topKFrequentElements`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1546)       |
| Find Median from Data Stream | [`findMedianFromDataStream`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1562)   |
| Find Kth Largest Element     | [`findKthLargestElement`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1573)      |

<a id="cp-misc"></a>
#### <span style="color:hsl(160,80%,58%)">Misc</span>

| Problem                                | Test                                                                                       |
|----------------------------------------|--------------------------------------------------------------------------------------------|
| Swap Two Numbers (multiple approaches) | [`swapTwoNumbers`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1584) |
| String Anagrams                        | [`stringAnagrams`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1626) |
| Two Sum (variant)                      | [`twoSumProblem`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1648)  |
| Rotate Array                           | [`rotateArray`](src/test/java/com/org/learning/problems/CompetitiveProblems.java#L1682)    |

<a id="design-patterns"></a>
## <span style="color:hsl(297,80%,58%)">8. 🏗️ Design patterns</span>

**Package:** [`com/org/learning/core/stream/thread/singleton`](src/test/java/com/org/learning/core/stream/thread/singleton)

| Class                                                                                                           | Description                                                                                                                       |
|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| [`ProblemSingleton`](src/test/java/com/org/learning/core/stream/thread/singleton/ProblemSingleton.java)         | Classic singleton hardened against reflection, serialization, and cloning attacks (`Serializable` + `Cloneable` with protections) |
| [`ProblemSingletonEnum`](src/test/java/com/org/learning/core/stream/thread/singleton/ProblemSingletonEnum.java) | Enum-based singleton (Effective Java approach) with a demo `Main`                                                                 |

<a id="scratch-pad"></a>
## <span style="color:hsl(75,80%,50%)">9. 📝 Scratch pad</span>

[`ScrapPad`](src/test/java/ScrapPad.java) (default package) — quick experiments: the BOFA employee exercise plus small helpers (average, case change, even/odd sum, de-dup, starts-with filtering).

---
*Note: links above point to specific line numbers and may drift as files are edited.*
