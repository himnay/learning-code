package com.org.learning.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Just enough Java source reading for the test graph.
 *
 * <p>For each file it keeps the package, the top-level type and the methods, with their line ranges,
 * source, {@code @DisplayName} and leading comment. It uses regexes and brace matching, not a
 * parser, which is enough for this repo's IntelliJ-formatted code.
 */
final class JavaSourceIndex {

    record Method(String name, int firstLine, int declarationLine, int lastLine, String source,
                  String displayName, String comment, boolean test) {
    }

    record JavaType(String packageName, String simpleName, Path file, String source, List<Method> methods) {

        String qualifiedName() {
            return packageName.isEmpty() ? simpleName : packageName + "." + simpleName;
        }

        List<Method> tests() {
            return methods.stream().filter(Method::test).toList();
        }

        /** The method with this name, preferring a test method when a nested type declares the same name. */
        Optional<Method> method(String name) {
            return methods.stream()
                    .filter(method -> method.name().equals(name))
                    .min(Comparator.comparing(method -> !method.test()));
        }
    }

    private static final Pattern PACKAGE = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;", Pattern.MULTILINE);
    // Modifiers, optional type parameters, a return type, then "name(": a declaration, not a call.
    private static final Pattern DECLARATION = Pattern.compile(
            "^\\s*(?:(?:public|protected|private|static|final|synchronized|abstract|default|native|strictfp)\\s+)*"
                    + "(?:<[^>]+>\\s+)?([\\w.$]+(?:<[^;=(){}]*>)?(?:\\[])*)\\s+(\\w+)\\s*\\(");
    private static final Set<String> NOT_A_RETURN_TYPE = Set.of("return", "new", "throw", "else", "case", "yield",
            "assert", "record", "class", "interface", "enum", "package", "import");
    private static final Pattern TEST_ANNOTATION =
            Pattern.compile("@(?:Test|ParameterizedTest|RepeatedTest|TestFactory|TestTemplate)\\b");

    private final Map<String, JavaType> types = new TreeMap<>();

    static JavaSourceIndex scan(Path root) throws IOException {
        var index = new JavaSourceIndex();
        if (Files.isDirectory(root)) {
            try (Stream<Path> files = Files.walk(root)) {
                for (Path file : files.filter(f -> f.toString().endsWith(".java")).sorted().toList()) {
                    index.add(file);
                }
            }
        }
        return index;
    }

    /** Top-level types by qualified name. */
    Map<String, JavaType> types() {
        return types;
    }

    private void add(Path file) throws IOException {
        String source = Files.readString(file);
        Matcher pkg = PACKAGE.matcher(source);
        String simpleName = file.getFileName().toString().replaceFirst("\\.java$", "");
        var type = new JavaType(pkg.find() ? pkg.group(1) : "", simpleName, file, source, methods(source));
        types.put(type.qualifiedName(), type);
    }

    private static List<Method> methods(String source) {
        List<String> lines = Arrays.asList(source.split("\n", -1));
        List<Method> methods = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Matcher declaration = DECLARATION.matcher(lines.get(i));
            if (!declaration.find() || NOT_A_RETURN_TYPE.contains(declaration.group(1))) {
                continue;
            }
            int last = endOfBody(lines, i);
            if (last < 0) {
                continue;
            }
            int first = i;
            while (first > 0 && isPreamble(lines.get(first - 1))) {
                first--;
            }
            String head = String.join("\n", lines.subList(first, i + 1));
            methods.add(new Method(declaration.group(2), first + 1, i + 1, last + 1,
                    dedent(lines.subList(first, last + 1)),
                    displayName(head),
                    comment(lines.subList(first, i)),
                    TEST_ANNOTATION.matcher(head).find()));
            i = last; // skip the body: methods of anonymous or local classes in it are not interesting here
        }
        return methods;
    }

    /**
     * Annotations and comments directly above a declaration belong to it, and so do the continuation
     * lines of a multi-line annotation such as {@code @DisplayName("..." + "...")}.
     */
    private static boolean isPreamble(String line) {
        String t = line.strip();
        return t.startsWith("@") || t.startsWith("//") || t.startsWith("/*") || t.startsWith("*")
                || t.startsWith("\"") || t.startsWith("+") || t.startsWith(")");
    }

    /** The {@code @DisplayName} text, joining the string literals of a concatenated value. */
    private static String displayName(String head) {
        int at = head.indexOf("@DisplayName(");
        if (at < 0) {
            return null;
        }
        var text = new StringBuilder();
        for (int i = at + "@DisplayName(".length(); i < head.length() && head.charAt(i) != ')'; i++) {
            if (head.charAt(i) == '"') {
                int end = endOfLiteral(head, i, '"');
                text.append(unescape(head.substring(i + 1, Math.min(end, head.length()))));
                i = end;
            }
        }
        return text.toString();
    }

    private static String comment(List<String> preamble) {
        var text = new StringBuilder();
        for (String line : preamble) {
            String t = line.strip();
            if (!(t.startsWith("//") || t.startsWith("/*") || t.startsWith("*"))) {
                continue; // an annotation or its continuation line
            }
            t = t.replaceFirst("^(//+|/\\*\\*?|\\*/|\\*)", "").replaceFirst("\\*/$", "").strip();
            if (!t.isEmpty()) {
                text.append(text.isEmpty() ? "" : " ").append(t);
            }
        }
        return text.isEmpty() ? null : text.toString();
    }

    /**
     * The line that closes the body opened after the declaration on line {@code from}, skipping braces
     * in strings, chars, text blocks and comments. A {@code ;} before any brace means no body.
     */
    private static int endOfBody(List<String> lines, int from) {
        int depth = 0;
        boolean opened = false;
        boolean blockComment = false;
        boolean textBlock = false;
        for (int i = from; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (blockComment) {
                    if (line.startsWith("*/", j)) {
                        blockComment = false;
                        j++;
                    }
                } else if (textBlock) {
                    if (line.startsWith("\"\"\"", j)) {
                        textBlock = false;
                        j += 2;
                    }
                } else if (line.startsWith("//", j)) {
                    break;
                } else if (line.startsWith("/*", j)) {
                    blockComment = true;
                    j++;
                } else if (line.startsWith("\"\"\"", j)) {
                    textBlock = true;
                    j += 2;
                } else if (c == '"' || c == '\'') {
                    j = endOfLiteral(line, j, c);
                } else if (c == '{') {
                    depth++;
                    opened = true;
                } else if (c == '}') {
                    depth--;
                    if (opened && depth == 0) {
                        return i;
                    }
                } else if (c == ';' && !opened) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int endOfLiteral(String line, int start, char quote) {
        for (int j = start + 1; j < line.length(); j++) {
            char c = line.charAt(j);
            if (c == '\\') {
                j++;
            } else if (c == quote) {
                return j;
            }
        }
        return line.length();
    }

    private static String dedent(List<String> lines) {
        int indent = lines.stream()
                .filter(line -> !line.isBlank())
                .mapToInt(line -> line.length() - line.stripLeading().length())
                .min()
                .orElse(0);
        return lines.stream()
                .map(line -> line.isBlank() ? "" : line.substring(Math.min(indent, line.length())))
                .collect(Collectors.joining("\n"));
    }

    private static String unescape(String javaString) {
        return javaString.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
