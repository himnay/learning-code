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
 *
 * <p>Each package is a group, and the legend chips show and hide a package with its classes and
 * tests. A test's circle is coloured by its last result instead of by its package.
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

    /** A test's last result, which is the colour of its circle. */
    private enum Status {
        PASSED("passed", "#22c55e"),
        FAILED("failed", "#ef4444"),
        SKIPPED("skipped", "#f59e0b"),
        NOT_RUN("not run", "#6b7280");

        final String label;
        final String color;

        Status(String label, String color) {
            this.label = label;
            this.color = color;
        }
    }

    // Package colours avoid green, red, amber and grey, which mean a test result.
    private static final List<String> PACKAGE_COLORS =
            List.of("#3987e5", "#9085e9", "#14b8a6", "#d55181", "#38bdf8", "#a78bfa", "#2dd4bf", "#f472b6");
    private static final String PROJECT_COLOR = "#e7e5e4";
    private static final String BASE_PACKAGE = "com.org.learning.";
    private static final String REPORT_PACKAGE = GraphPage.class.getPackageName();
    private static final Pattern PROJECT_FRAME = Pattern.compile("^\\s*at (com\\.org\\.learning\\.|[A-Z]\\w*\\.)");
    private static final String GITHUB =
            System.getProperty("test.graph.github", "https://github.com/himnay/learning-code/blob/main");

    private final List<Map<String, Object>> groups = new ArrayList<>();
    private final List<Map<String, Object>> nodes = new ArrayList<>();
    private final List<String> content = new ArrayList<>();
    private final Set<List<Integer>> edges = new LinkedHashSet<>();
    private final Map<Status, Integer> totals = new EnumMap<>(Status.class);
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
        int projectGroup = group("Project", PROJECT_COLOR, false);
        int root = node("learning-code", "learning-code", projectGroup, null, List.of(), List.of());

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
            String label = name.startsWith(BASE_PACKAGE) ? name.substring(BASE_PACKAGE.length()) : name;
            int group = group(label, PACKAGE_COLORS.get((groups.size() - 1) % PACKAGE_COLORS.size()), true);
            int pkg = node("pkg:" + name, label, group, null, List.of(entry.getValue().size() + " classes"), List.of());
            link(root, pkg);
            var table = new StringBuilder("| Class | Kind | Tests | Result |\n|---|---|---|---|\n");
            for (JavaType type : entry.getValue()) {
                Map<Status, Integer> results = new EnumMap<>(Status.class);
                int cls = classNode(type, name, group, outcomes, classOutcomes, uses, usedBy, results, shown);
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

        // Tests that ran but whose source wasn't found still get a node, next to the project node.
        outcomes.forEach((key, outcome) -> {
            if (!shown.contains(key)) {
                Status status = statusOf(outcome, null);
                count(totals, status);
                int test = node(key.replace("#", "::"), outcome.displayName(), projectGroup, status.color,
                        List.of(resultLabel(status, outcome)), List.of());
                nodes.get(test).put("test", true);
                content.set(test, "**Result:** " + resultLabel(status, outcome)
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

    private int classNode(JavaType type, String packageName, int group, Map<String, Outcome> outcomes,
                          Map<String, Outcome> classOutcomes, Map<JavaType, List<JavaType>> uses,
                          Map<JavaType, List<JavaType>> usedBy, Map<Status, Integer> results, Set<String> shown) {
        List<Method> tests = type.tests();
        String id = type.qualifiedName();
        Outcome classOutcome = classOutcomes.get(id);
        int cls = node(id, type.simpleName(), group, null,
                List.of(tests.isEmpty() ? "support class" : tests.size() + " tests"), links(type.file(), 0));

        var table = new StringBuilder("| # | Test | Description | Result |\n|---|---|---|---|\n");
        int row = 0;
        for (Method method : tests) {
            String key = id + "#" + method.name();
            shown.add(key);
            Outcome outcome = outcomes.get(key);
            Status status = statusOf(outcome, classOutcome);
            count(results, status);
            count(totals, status);
            String description = description(method, outcome);
            String testId = id + "::" + method.name();
            int test = node(testId, description, group, status.color,
                    List.of(method.name() + "()", resultLabel(status, outcome)), links(type.file(), method.declarationLine()));
            nodes.get(test).put("test", true);
            content.set(test, testMarkdown(type, method, outcome, classOutcome, status));
            link(cls, test);
            if (status == Status.FAILED) {
                failed.add("- [" + description + "](" + testId + ") in `" + type.simpleName() + "`");
            }
            table.append("| ").append(++row).append(" | [").append(method.name()).append("](").append(testId)
                    .append(") | ").append(description.replace('|', '¦')).append(" | ")
                    .append(resultLabel(status, outcome)).append(" |\n");
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

    private String testMarkdown(JavaType type, Method method, Outcome outcome, Outcome classOutcome, Status status) {
        var md = new StringBuilder();
        md.append("**Result:** ").append(resultLabel(status, outcome)).append("\n\n");
        md.append("**Test:** `").append(method.name()).append("()` in [").append(type.simpleName()).append("](")
                .append(type.qualifiedName()).append("), line ").append(method.declarationLine()).append("\n\n");
        if (method.comment() != null && !method.comment().equals(description(method, outcome))) {
            md.append("**Notes:** ").append(method.comment()).append("\n\n");
        }
        Outcome problem = outcome != null ? outcome : classOutcome;
        if (problem != null && problem.trace() != null) {
            md.append("## Failure\n\n```text\n").append(problem.trace()).append("\n```\n\n");
        } else if (problem != null && problem.message() != null && status != Status.PASSED) {
            md.append("**Reason:** ").append(problem.message()).append("\n\n");
        }
        md.append("## Source\n\n```java\n").append(method.source()).append("\n```\n");
        return md.toString();
    }

    private String rootMarkdown(long runMillis) {
        var md = new StringBuilder("Knowledge graph of the **learning-code** tests, written after the last test run.\n\n");
        md.append("| Result | Tests |\n|---|---|\n");
        for (Status status : Status.values()) {
            md.append("| ").append(status.label).append(" | ").append(totals.getOrDefault(status, 0)).append(" |\n");
        }
        md.append("\n**Run:** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .append(" · ").append(String.format(Locale.ROOT, "%.1f s", runMillis / 1000.0))
                .append(" · Java ").append(Runtime.version()).append("\n\n");
        if (!failed.isEmpty()) {
            md.append("## Failed tests\n\n").append(String.join("\n", failed)).append("\n\n");
        }
        md.append("## How to read it\n\n")
                .append("- Each package has a colour, and its classes share it. The legend chips show or hide a ")
                .append("package with its classes and tests.\n")
                .append("- A test's circle shows its last result: green passed, red failed, amber skipped, ")
                .append("grey not run.\n")
                .append("- Click any circle to see its details: a test's description (`@DisplayName`), result ")
                .append("and source; a class's tests and source.\n")
                .append("- Search matches descriptions, method names and class names.\n\n");
        md.append("## Regenerate\n\n")
                .append("Every test run rewrites `target/test-graph/index.html`, whether it's `mvn test` or a run ")
                .append("from the IDE. Tests outside the run show as not run.\n\n")
                .append("The Allure report comes from `mvn allure:report`: ")
                .append("[target/site/allure-maven-plugin/index.html](../site/allure-maven-plugin/index.html).\n");
        return md.toString();
    }

    private String summary() {
        int total = totals.values().stream().mapToInt(Integer::intValue).sum();
        return total + " tests · " + breakdown(totals);
    }

    private static String breakdown(Map<Status, Integer> results) {
        List<String> parts = new ArrayList<>();
        for (Status status : Status.values()) {
            int n = results.getOrDefault(status, 0);
            if (n > 0) {
                parts.add(n + " " + status.label);
            }
        }
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

    private static Status statusOf(Outcome outcome, Outcome classOutcome) {
        Outcome effective = outcome != null ? outcome : classOutcome;
        if (effective == null) {
            return Status.NOT_RUN;
        }
        return switch (effective.status()) {
            case "passed" -> Status.PASSED;
            case "failed" -> Status.FAILED;
            default -> Status.SKIPPED;
        };
    }

    private static String resultLabel(Status status, Outcome outcome) {
        String time = outcome != null ? " in " + outcome.millis() + " ms" : "";
        return switch (status) {
            case PASSED -> "✅ passed" + time;
            case FAILED -> "❌ failed" + time;
            case SKIPPED -> "⏭️ " + (outcome != null ? outcome.status() : "skipped with its class");
            case NOT_RUN -> "⚪ not run";
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

    private static void count(Map<Status, Integer> counts, Status status) {
        counts.merge(status, 1, Integer::sum);
    }

    /** A legend group; {@code inLegend=false} keeps it out of the chips (the project node's). */
    private int group(String label, String color, boolean inLegend) {
        var group = new LinkedHashMap<String, Object>();
        group.put("label", label);
        group.put("color", color);
        group.put("legend", inLegend);
        groups.add(group);
        return groups.size() - 1;
    }

    /** A node filled with its group's colour, or with {@code color} when given (a test's result). */
    private int node(String id, String title, int group, String color, List<String> tags,
                     List<Map<String, Object>> links) {
        var node = new LinkedHashMap<String, Object>();
        node.put("id", id);
        node.put("t", title);
        node.put("g", group);
        if (color != null) {
            node.put("c", color);
        }
        node.put("tags", tags);
        node.put("links", links);
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
        data.put("groups", groups);
        data.put("content", content);
        data.put("summary", summary());
        List<Map<String, Object>> status = new ArrayList<>();
        for (Status s : Status.values()) {
            status.add(Map.of("label", s.label, "color", s.color, "count", totals.getOrDefault(s, 0)));
        }
        data.put("status", status);
        try (InputStream template = GraphPage.class.getResourceAsStream("/test-graph/template.html")) {
            if (template == null) {
                throw new IOException("test-graph/template.html is not on the test classpath");
            }
            var json = new StringBuilder();
            json(data, json);
            return new String(template.readAllBytes(), StandardCharsets.UTF_8).replace("/*DATA*/null", json);
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
