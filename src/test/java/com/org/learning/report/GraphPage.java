package com.org.learning.report;

import com.org.learning.report.JavaSourceIndex.JavaType;
import com.org.learning.report.JavaSourceIndex.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Builds the graph from the source index and the run's outcomes: its nodes, its links and the
 * markdown shown in the side panel. Then fills {@code test-graph/template.html} with it.
 */
final class GraphPage {

    /** One test method's result. Parameterized and repeated invocations merge into their method's outcome. */
    record Outcome(String status, long millis, String displayName, String message, String trace) {

        Outcome worse(Outcome other) {
            Outcome worst = rank(other.status) > rank(status) ? other : this;
            return new Outcome(worst.status, millis + other.millis, displayName, worst.message, worst.trace);
        }

        private static int rank(String status) {
            return switch (status) {
                case "failed" -> 3;
                case "aborted" -> 2;
                case "skipped" -> 1;
                default -> 0;
            };
        }
    }

    /** The legend: what a node is, and for tests, how the last run went. */
    private enum Group {
        PROJECT("Project", "#e7e5e4"),
        PACKAGE("Package", "#9085e9"),
        TEST_CLASS("Test class", "#3987e5"),
        SUPPORT("Support class", "#14b8a6"),
        PASSED("Passed", "#22c55e"),
        FAILED("Failed", "#ef4444"),
        SKIPPED("Skipped / aborted", "#f59e0b"),
        NOT_RUN("Not run", "#6b7280");

        final String label;
        final String color;

        Group(String label, String color) {
            this.label = label;
            this.color = color;
        }
    }

    private static final List<Group> RESULTS = List.of(Group.PASSED, Group.FAILED, Group.SKIPPED, Group.NOT_RUN);
    private static final String REPORT_PACKAGE = GraphPage.class.getPackageName();
    private static final Pattern PROJECT_FRAME = Pattern.compile("^\\s*at (com\\.org\\.learning\\.|[A-Z]\\w*\\.)");
    private static final String GITHUB =
            System.getProperty("test.graph.github", "https://github.com/himnay/learning-code/blob/main");

    private final List<Map<String, Object>> nodes = new ArrayList<>();
    private final List<String> content = new ArrayList<>();
    private final Set<List<Integer>> edges = new LinkedHashSet<>();
    private final Map<Group, Integer> totals = new EnumMap<>(Group.class);
    private final List<String> failed = new ArrayList<>();

    static String render(JavaSourceIndex index, Map<String, Outcome> outcomes, Map<String, Outcome> classOutcomes,
                         long runMillis) throws IOException {
        var page = new GraphPage();
        page.build(index, outcomes, classOutcomes, runMillis);
        return page.html();
    }

    /** The printed stack trace, cut after the last frame of this project's code. */
    static String trace(Throwable failure) {
        var out = new StringWriter();
        failure.printStackTrace(new PrintWriter(out));
        List<String> lines = out.toString().lines().toList();
        int lastProjectFrame = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (PROJECT_FRAME.matcher(lines.get(i)).find()) {
                lastProjectFrame = i;
            }
        }
        int end = Math.min(Math.min(lines.size(), 40), lastProjectFrame >= 0 ? lastProjectFrame + 1 : 12);
        String kept = String.join("\n", lines.subList(0, end));
        return end < lines.size() ? kept + "\n\t... " + (lines.size() - end) + " more" : kept;
    }

    private void build(JavaSourceIndex index, Map<String, Outcome> outcomes, Map<String, Outcome> classOutcomes,
                       long runMillis) {
        int root = node("learning-code", "learning-code", Group.PROJECT, List.of(), List.of());

        Map<String, List<JavaType>> packages = new TreeMap<>();
        for (JavaType type : index.types().values()) {
            if (!type.packageName().equals(REPORT_PACKAGE)) {
                packages.computeIfAbsent(type.packageName(), name -> new ArrayList<>()).add(type);
            }
        }
        List<JavaType> types = packages.values().stream().flatMap(List::stream).toList();
        Map<JavaType, List<JavaType>> uses = new HashMap<>();
        Map<JavaType, List<JavaType>> usedBy = new HashMap<>();
        for (JavaType type : types) {
            for (JavaType other : types) {
                if (other != type && mentions(type, other)) {
                    uses.computeIfAbsent(type, t -> new ArrayList<>()).add(other);
                    usedBy.computeIfAbsent(other, t -> new ArrayList<>()).add(type);
                }
            }
        }

        Map<JavaType, Integer> classNodes = new HashMap<>();
        Set<String> shown = new HashSet<>();
        for (var entry : packages.entrySet()) {
            String name = entry.getKey().isEmpty() ? "(default package)" : entry.getKey();
            int pkg = node("pkg:" + name, name, Group.PACKAGE, List.of(entry.getValue().size() + " classes"), List.of());
            link(root, pkg);
            var table = new StringBuilder("| Class | Kind | Tests | Result |\n|---|---|---|---|\n");
            for (JavaType type : entry.getValue()) {
                Map<Group, Integer> results = new EnumMap<>(Group.class);
                int cls = classNode(type, name, outcomes, classOutcomes, uses, usedBy, results, shown);
                classNodes.put(type, cls);
                link(pkg, cls);
                int tests = type.tests().size();
                table.append("| [").append(type.simpleName()).append("](").append(type.qualifiedName()).append(") | ")
                        .append(tests == 0 ? "support" : "tests").append(" | ").append(tests).append(" | ")
                        .append(tests == 0 ? "" : breakdown(results)).append(" |\n");
            }
            content.set(pkg, "**Package:** `" + name + "`\n\n" + table);
        }
        uses.forEach((user, used) -> used.forEach(other -> link(classNodes.get(user), classNodes.get(other))));

        // Tests that ran but whose source wasn't found still get a node.
        outcomes.forEach((key, outcome) -> {
            if (!shown.contains(key)) {
                Group group = groupOf(outcome, null);
                count(totals, group);
                int test = node(key.replace("#", "::"), outcome.displayName(), group,
                        List.of(resultLabel(group, outcome)), List.of());
                content.set(test, "> " + outcome.displayName() + "\n\n**Result:** " + resultLabel(group, outcome)
                        + "\n\nThe source of `" + key + "` was not found under src/test/java.\n");
                link(root, test);
            }
        });

        nodes.get(root).put("tags", List.of(summary()));
        content.set(root, rootMarkdown(runMillis));
        int[] degree = new int[nodes.size()];
        edges.forEach(edge -> {
            degree[edge.get(0)]++;
            degree[edge.get(1)]++;
        });
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).put("d", degree[i]);
        }
    }

    private int classNode(JavaType type, String packageName, Map<String, Outcome> outcomes,
                          Map<String, Outcome> classOutcomes, Map<JavaType, List<JavaType>> uses,
                          Map<JavaType, List<JavaType>> usedBy, Map<Group, Integer> results, Set<String> shown) {
        List<Method> tests = type.tests();
        String id = type.qualifiedName();
        Outcome classOutcome = classOutcomes.get(id);
        int cls = node(id, type.simpleName(), tests.isEmpty() ? Group.SUPPORT : Group.TEST_CLASS,
                List.of(tests.isEmpty() ? "support class" : tests.size() + " tests"), links(type.file(), 0));

        var table = new StringBuilder("| # | Test | Description | Result |\n|---|---|---|---|\n");
        int row = 0;
        for (Method method : tests) {
            String key = id + "#" + method.name();
            shown.add(key);
            Outcome outcome = outcomes.get(key);
            Group group = groupOf(outcome, classOutcome);
            count(results, group);
            count(totals, group);
            String description = description(method, outcome);
            String testId = id + "::" + method.name();
            int test = node(testId, description, group,
                    List.of(method.name() + "()", resultLabel(group, outcome)), links(type.file(), method.declarationLine()));
            content.set(test, testMarkdown(type, method, outcome, classOutcome, group, description));
            link(cls, test);
            if (group == Group.FAILED) {
                failed.add("- [" + description + "](" + testId + ") in `" + type.simpleName() + "`");
            }
            table.append("| ").append(++row).append(" | [").append(method.name()).append("](").append(testId)
                    .append(") | ").append(description.replace('|', '¦')).append(" | ")
                    .append(resultLabel(group, outcome)).append(" |\n");
        }

        var md = new StringBuilder();
        md.append("**Package:** [`").append(packageName).append("`](pkg:").append(packageName).append(") · **File:** `")
                .append(relative(type.file())).append("`\n\n");
        if (!tests.isEmpty()) {
            md.append("**Tests:** ").append(tests.size()).append(" · ").append(breakdown(results)).append("\n\n");
            if (classOutcome != null) {
                md.append("**Class-level problem:** ").append(classOutcome.message()).append("\n\n");
            }
            md.append(table).append('\n');
        }
        md.append(typeLinks("Uses", uses.get(type))).append(typeLinks("Used by", usedBy.get(type)));
        md.append("## Source\n\n```java\n").append(type.source().stripTrailing()).append("\n```\n");
        content.set(cls, md.toString());
        return cls;
    }

    private String testMarkdown(JavaType type, Method method, Outcome outcome, Outcome classOutcome, Group group,
                                String description) {
        var md = new StringBuilder();
        md.append("**Result:** ").append(resultLabel(group, outcome)).append("\n\n");
        md.append("**Test:** `").append(method.name()).append("()` in [").append(type.simpleName()).append("](")
                .append(type.qualifiedName()).append("), line ").append(method.declarationLine()).append("\n\n");
        if (method.comment() != null && !method.comment().equals(description)) {
            md.append("**Notes:** ").append(method.comment()).append("\n\n");
        }
        Outcome problem = outcome != null ? outcome : classOutcome;
        if (problem != null && problem.trace() != null) {
            md.append("## Failure\n\n```text\n").append(problem.trace()).append("\n```\n\n");
        } else if (problem != null && problem.message() != null && group != Group.PASSED) {
            md.append("**Reason:** ").append(problem.message()).append("\n\n");
        }
        md.append("## Source\n\n```java\n").append(method.source()).append("\n```\n");
        return md.toString();
    }

    private String rootMarkdown(long runMillis) {
        var md = new StringBuilder("Knowledge graph of the **learning-code** tests, written after the last test run.\n\n");
        md.append("| Result | Tests |\n|---|---|\n");
        RESULTS.forEach(group -> md.append("| ").append(group.label).append(" | ")
                .append(totals.getOrDefault(group, 0)).append(" |\n"));
        md.append("\n**Run:** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .append(" · ").append(String.format(Locale.ROOT, "%.1f s", runMillis / 1000.0))
                .append(" · Java ").append(Runtime.version()).append("\n\n");
        if (!failed.isEmpty()) {
            md.append("## Failed tests\n\n").append(String.join("\n", failed)).append("\n\n");
        }
        md.append("## How to read it\n\n")
                .append("- Packages link to their classes. Classes link to their tests and to the classes they use.\n")
                .append("- A test's colour is its last result. Click it to see its description (`@DisplayName`), ")
                .append("result and source.\n")
                .append("- Search matches descriptions, method names and class names. The legend chips filter by ")
                .append("kind or result.\n\n");
        md.append("## Regenerate\n\n")
                .append("Every test run rewrites `target/test-graph/index.html`, whether it's `mvn test` or a run ")
                .append("from the IDE. Tests outside the run show as not run.\n\n")
                .append("The Allure report comes from `mvn allure:report`: ")
                .append("[target/site/allure-maven-plugin/index.html](../site/allure-maven-plugin/index.html).\n");
        return md.toString();
    }

    private String summary() {
        int total = RESULTS.stream().mapToInt(group -> totals.getOrDefault(group, 0)).sum();
        return total + " tests · " + breakdown(totals);
    }

    private static String breakdown(Map<Group, Integer> results) {
        List<String> parts = new ArrayList<>();
        RESULTS.forEach(group -> {
            int n = results.getOrDefault(group, 0);
            if (n > 0) {
                parts.add(n + " " + group.label.toLowerCase(Locale.ROOT));
            }
        });
        return parts.isEmpty() ? "no tests" : String.join(" · ", parts);
    }

    private static String typeLinks(String label, List<JavaType> types) {
        if (types == null || types.isEmpty()) {
            return "";
        }
        return "**" + label + ":** " + String.join(", ", types.stream()
                .map(type -> "[" + type.simpleName() + "](" + type.qualifiedName() + ")").toList()) + "\n\n";
    }

    private static boolean mentions(JavaType type, JavaType other) {
        return Pattern.compile("\\b" + Pattern.quote(other.simpleName()) + "\\b").matcher(type.source()).find();
    }

    private static Group groupOf(Outcome outcome, Outcome classOutcome) {
        Outcome effective = outcome != null ? outcome : classOutcome;
        if (effective == null) {
            return Group.NOT_RUN;
        }
        return switch (effective.status()) {
            case "passed" -> Group.PASSED;
            case "failed" -> Group.FAILED;
            default -> Group.SKIPPED;
        };
    }

    private static String resultLabel(Group group, Outcome outcome) {
        String time = outcome != null ? " in " + outcome.millis() + " ms" : "";
        return switch (group) {
            case PASSED -> "✅ passed" + time;
            case FAILED -> "❌ failed" + time;
            case SKIPPED -> "⏭️ " + (outcome != null ? outcome.status() : "skipped with its class");
            default -> "⚪ not run";
        };
    }

    /** {@code @DisplayName} first, then a real JUnit display name, then the comment above the method. */
    private static String description(Method method, Outcome outcome) {
        if (method.displayName() != null) {
            return method.displayName();
        }
        if (outcome != null && outcome.displayName() != null && !outcome.displayName().endsWith(")")) {
            return outcome.displayName();
        }
        if (method.comment() != null) {
            return method.comment();
        }
        return method.name().replaceAll("([a-z0-9])([A-Z])", "$1 $2").replace('_', ' ').toLowerCase(Locale.ROOT);
    }

    private static List<Map<String, Object>> links(Path file, int line) {
        String absolute = file.toAbsolutePath().normalize().toString().replace('\\', '/');
        return List.of(
                Map.of("label", "GitHub", "href", GITHUB + "/" + relative(file) + (line > 0 ? "#L" + line : "")),
                Map.of("label", "VS Code", "href",
                        "vscode://file" + (absolute.startsWith("/") ? "" : "/") + absolute + (line > 0 ? ":" + line : "")));
    }

    private static String relative(Path file) {
        return Path.of("").toAbsolutePath().relativize(file.toAbsolutePath().normalize()).toString().replace('\\', '/');
    }

    private static void count(Map<Group, Integer> counts, Group group) {
        counts.merge(group, 1, Integer::sum);
    }

    private int node(String id, String title, Group group, List<String> tags, List<Map<String, Object>> links) {
        var node = new LinkedHashMap<String, Object>();
        node.put("id", id);
        node.put("t", title);
        node.put("g", group.ordinal());
        node.put("tags", tags);
        node.put("links", links);
        if (RESULTS.contains(group)) {
            node.put("test", true);
        }
        nodes.add(node);
        content.add("");
        return nodes.size() - 1;
    }

    private void link(int a, int b) {
        if (a != b) {
            edges.add(List.of(Math.min(a, b), Math.max(a, b)));
        }
    }

    private String html() throws IOException {
        var data = new LinkedHashMap<String, Object>();
        data.put("nodes", nodes);
        data.put("edges", edges);
        data.put("groups", Arrays.stream(Group.values()).map(g -> Map.of("label", g.label, "color", g.color)).toList());
        data.put("content", content);
        data.put("summary", summary());
        try (InputStream template = GraphPage.class.getResourceAsStream("/test-graph/template.html")) {
            if (template == null) {
                throw new IOException("test-graph/template.html is not on the test classpath");
            }
            var json = new StringBuilder();
            json(data, json);
            return new String(template.readAllBytes(), StandardCharsets.UTF_8).replace("/*DATA*/", json);
        }
    }

    private static void json(Object value, StringBuilder out) {
        switch (value) {
            case null -> out.append("null");
            case String s -> quote(s, out);
            case Number n -> out.append(n);
            case Boolean b -> out.append(b);
            case Map<?, ?> map -> {
                out.append('{');
                String separator = "";
                for (var entry : map.entrySet()) {
                    out.append(separator);
                    quote(String.valueOf(entry.getKey()), out);
                    out.append(':');
                    json(entry.getValue(), out);
                    separator = ",";
                }
                out.append('}');
            }
            case Collection<?> items -> {
                out.append('[');
                String separator = "";
                for (Object item : items) {
                    out.append(separator);
                    json(item, out);
                    separator = ",";
                }
                out.append(']');
            }
            default -> quote(value.toString(), out);
        }
    }

    private static void quote(String s, StringBuilder out) {
        out.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                // Keeps a "</script>" inside some source code from closing the page's script element.
                case '<' -> out.append("\\u003c");
                case ' ', ' ' -> out.append(String.format("\\u%04x", (int) c));
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        out.append('"');
    }
}
