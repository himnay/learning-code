package com.org.learning.report;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Writes {@code target/test-graph/index.html} after every test run. The page is a knowledge graph of
 * the test packages, classes and tests, and it opens straight from the file system. Clicking a test
 * shows its description ({@code @DisplayName}), its result (status, duration, failure) and its source.
 *
 * <p>The JUnit Platform finds this listener through
 * {@code META-INF/services/org.junit.platform.launcher.TestExecutionListener}. That is the same
 * ServiceLoader mechanism the Allure adapter uses, so Maven runs and IDE runs both write the page.
 *
 * <p>System properties:
 * <ul>
 *   <li>{@code test.graph.dir}: output directory, default {@code target/test-graph};</li>
 *   <li>{@code test.graph.sources}: default {@code src/test/java};</li>
 *   <li>{@code test.graph.github}: base URL of the source links;</li>
 *   <li>{@code test.graph.enabled=false}: switches it off.</li>
 * </ul>
 */
public class KnowledgeGraphListener implements TestExecutionListener {

    private final Map<String, Long> startedAt = new ConcurrentHashMap<>();
    private final Map<String, String> displayNames = new ConcurrentHashMap<>();
    private final Map<String, GraphPage.Outcome> outcomes = new ConcurrentHashMap<>();
    private final Map<String, GraphPage.Outcome> classOutcomes = new ConcurrentHashMap<>();
    private volatile long planStartedAt = System.nanoTime();

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        planStartedAt = System.nanoTime();
    }

    @Override
    public void executionStarted(TestIdentifier id) {
        startedAt.put(id.getUniqueId(), System.nanoTime());
        // A parameterized method's container starts before its invocations, so its name wins.
        methodKey(id).ifPresent(key -> displayNames.putIfAbsent(key, id.getDisplayName()));
    }

    @Override
    public void executionSkipped(TestIdentifier id, String reason) {
        var skipped = new GraphPage.Outcome("skipped", 0, id.getDisplayName(), reason, null);
        methodKey(id).ifPresentOrElse(
                key -> outcomes.merge(key, skipped, GraphPage.Outcome::worse),
                () -> classKey(id).ifPresent(key -> classOutcomes.put(key, skipped)));
    }

    @Override
    public void executionFinished(TestIdentifier id, TestExecutionResult result) {
        String status = switch (result.getStatus()) {
            case SUCCESSFUL -> "passed";
            case ABORTED -> "aborted";
            case FAILED -> "failed";
        };
        Throwable failure = result.getThrowable().orElse(null);
        String message = failure == null ? null : failure.toString();
        String trace = failure == null ? null : GraphPage.trace(failure);
        long millis = (System.nanoTime() - startedAt.getOrDefault(id.getUniqueId(), System.nanoTime())) / 1_000_000;
        if (id.isTest()) {
            methodKey(id).ifPresent(key -> outcomes.merge(key,
                    new GraphPage.Outcome(status, millis, displayNames.getOrDefault(key, id.getDisplayName()), message, trace),
                    GraphPage.Outcome::worse));
        } else if (failure != null) {
            // For example a failing @BeforeAll: the class's tests never ran.
            classKey(id).ifPresent(key -> classOutcomes.put(key,
                    new GraphPage.Outcome(status, millis, id.getDisplayName(), message, trace)));
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if ("false".equalsIgnoreCase(System.getProperty("test.graph.enabled"))) {
            return;
        }
        Path page = Path.of(System.getProperty("test.graph.dir", "target/test-graph"), "index.html");
        try {
            var index = JavaSourceIndex.scan(Path.of(System.getProperty("test.graph.sources", "src/test/java")));
            long runMillis = (System.nanoTime() - planStartedAt) / 1_000_000;
            Files.createDirectories(page.toAbsolutePath().getParent());
            Files.writeString(page, GraphPage.render(index, outcomes, classOutcomes, runMillis));
            System.out.println("[test-graph] " + page.toAbsolutePath().normalize().toUri());
        } catch (IOException | RuntimeException e) {
            // A report must never fail the build.
            System.err.println("[test-graph] not written: " + e);
        }
    }

    private static Optional<String> methodKey(TestIdentifier id) {
        return id.getSource()
                .filter(MethodSource.class::isInstance)
                .map(MethodSource.class::cast)
                .map(source -> topLevel(source.getClassName()) + "#" + source.getMethodName());
    }

    private static Optional<String> classKey(TestIdentifier id) {
        return id.getSource()
                .filter(ClassSource.class::isInstance)
                .map(ClassSource.class::cast)
                .map(source -> topLevel(source.getClassName()));
    }

    /** Nested test classes ({@code Outer$Inner}) live in their outer class's file. */
    private static String topLevel(String binaryName) {
        int dollar = binaryName.indexOf('$');
        return dollar < 0 ? binaryName : binaryName.substring(0, dollar);
    }
}
