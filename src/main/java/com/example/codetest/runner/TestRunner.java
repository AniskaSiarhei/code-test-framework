package com.example.codetest.runner;

import com.example.codetest.annotations.After;
import com.example.codetest.annotations.Before;
import com.example.codetest.annotations.Disabled;
import com.example.codetest.annotations.ParameterizedTest;
import com.example.codetest.annotations.Test;
import com.example.codetest.annotations.Timeout;
import com.example.codetest.report.TestReportGenerator;
import com.example.codetest.report.TestResult;
import com.example.codetest.utils.MetricsCollector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class TestRunner {

    private static final List<TestResult> results = new ArrayList<>();

    public static void runAllTests(String basePackage) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.scan(basePackage);  // scan all tests and components
            context.refresh();          // Raising the context


            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

            TypeFilter typestMethodFilter = (metadataReader, metadataReaderFactory) -> {
                try {
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Test.class) || method.isAnnotationPresent(ParameterizedTest.class)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            };

            scanner.addIncludeFilter(typestMethodFilter);

            Set<org.springframework.beans.factory.config.BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (var candidate : candidates) {
                String className = candidate.getBeanClassName();
                Class<?> clazz = Class.forName(className);

                // We either get the bean from Spring, or we create it manually
                Object testInstance = context.getBeanProvider(clazz).getIfAvailable();
                if (testInstance == null) {
                    testInstance = clazz.getDeclaredConstructor().newInstance();
                }

                runTests(clazz, testInstance);
            }

            // We generate reports after all the tests
            TestReportGenerator.generateJsonReport(results, "target/test-report.json");
            TestReportGenerator.generateHtmlReport(results, "target/test-report.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runTests(Class<?> testClass, Object testInstance) {

        try {
            Method beforeMethod = null;
            Method afterMethod = null;

            // Checking @Disabled at the class level
            boolean classDisabled = testClass.isAnnotationPresent(Disabled.class);
            String classDisabledReason = classDisabled ? testClass.getAnnotation(Disabled.class).value() : "";

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethod = method;
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethod = method;
                }
            }

            for (Method method : testClass.getDeclaredMethods()) {

                boolean methodDisabled = method.isAnnotationPresent(Disabled.class);
                String methodDisabledReason = methodDisabled ? method.getAnnotation(Disabled.class).value() : "";

                if (method.isAnnotationPresent(Test.class) || method.isAnnotationPresent(ParameterizedTest.class)) {
                    Timeout timeoutAnnotation = method.getAnnotation(Timeout.class);
                    long timeout = (timeoutAnnotation == null) ? 0 : timeoutAnnotation.value();

                    if (method.isAnnotationPresent(Test.class)) {

                        if (classDisabled || methodDisabled) {
                            String reason = classDisabled ? classDisabledReason : methodDisabledReason;
                            System.out.println("⚠ Test " + method.getName() + " is disabled. Reason: " + reason);
                            results.add(new TestResult(
                                    testClass.getName(),
                                    method.getName(),
                                    true,
                                    "Disabled: " + reason,
                                    0, 0.0, 0.0
                            ));
                            continue;
                        }

                        if (beforeMethod != null) beforeMethod.invoke(testInstance);

                        System.out.println("Running test: " + method.getName());

                        long startTime = System.nanoTime();
                        double startCpu = MetricsCollector.getProcessCpuLoad();
                        double startMem = MetricsCollector.getMemoryUsageMb();

                        boolean passed = true;
                        String errorMessage = null;

                        try {
                            if (timeout > 0) {
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                Future<?> future = executor.submit(() -> {
                                    try {
                                        method.invoke(testInstance);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });

                                try{
                                    future.get(timeout, TimeUnit.MILLISECONDS);
                                } catch (TimeoutException e) {
                                    passed = false;
                                    errorMessage = "Test timed out after " + timeout + " ms";
                                    future.cancel(true);
                                    System.out.println("❌ " + errorMessage);
                                } finally {
                                    List<Runnable> droppedTasks = executor.shutdownNow();
                                    if (!droppedTasks.isEmpty()) {
                                        System.out.println("⚠ Some tasks were not executed: " + droppedTasks.size());
                                    }
                                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                                        System.out.println("⚠ Executor did not terminate in time.");
                                    }
                                }
                            } else {
                                method.invoke(testInstance);
                            }
                            if (errorMessage == null) {
                                System.out.println("✔ Test passed");
                            }
                        } catch (Exception e) {
                            passed = false;
                            errorMessage = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            System.out.println("❌ Test failed: " + e.getCause());
                        }

                        long endTime = System.nanoTime();
                        double endCpu = MetricsCollector.getProcessCpuLoad();
                        double endMem = MetricsCollector.getMemoryUsageMb();

                        long execTimesMs = (endTime - startTime) / 1_000_000;
                        double cpuUsed = Math.max(0, endCpu - startCpu);
                        double memUsed = endMem - startMem;

                        results.add(new TestResult(
                                testClass.getName(),
                                method.getName(),
                                passed,
                                errorMessage,
                                execTimesMs,
                                cpuUsed,
                                memUsed
                        ));

                        if (afterMethod != null) afterMethod.invoke(testInstance);
                        System.out.println("--------------------------------------------------");
                    }

                    if (method.isAnnotationPresent(ParameterizedTest.class)) {

                        if (classDisabled || methodDisabled) {
                            String reason = classDisabled ? classDisabledReason : methodDisabledReason;
                            System.out.println("⚠ Parameterized test " + method.getName() + " is disabled. Reason: " + reason);
                            results.add(new TestResult(
                                    testClass.getName(),
                                    method.getName() + " [parameterized]",
                                    true,
                                    "Disabled: " + reason,
                                    0, 0.0, 0.0
                            ));
                            continue;
                        }

                        ParameterizedTest annotation = method.getAnnotation(ParameterizedTest.class);
                        String[] params = annotation.value();

                        for (String paramSet : params) {
                            if (beforeMethod != null) beforeMethod.invoke(testInstance);

                            String[] paramValues = paramSet.split(",");
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            Object[] convertedParams = new Object[parameterTypes.length];

                            for (int i = 0; i < parameterTypes.length; i++) {
                                String value = paramValues[i].trim();
                                convertedParams[i] = convertToType(value, parameterTypes[i]);
                            }

                            System.out.println("Running parameterized test: " + method.getName() + " with param: " + paramSet);

                            long startTime = System.nanoTime();
                            double startCpu = MetricsCollector.getProcessCpuLoad();
                            double startMem = MetricsCollector.getMemoryUsageMb();

                            boolean passed = true;
                            String errorMessage = null;

                            try {
                                if (timeout > 0) {
                                    ExecutorService executor = Executors.newSingleThreadExecutor();
                                    Future<?> future = executor.submit(() -> {
                                        try {
                                            method.invoke(testInstance, convertedParams);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

                                    try {
                                        future.get(timeout, TimeUnit.MILLISECONDS);
                                    } catch (TimeoutException e) {
                                        passed = false;
                                        errorMessage = "Test timed out after " + timeout + " ms";
                                        future.cancel(true);
                                        System.out.println("❌ " + errorMessage);
                                    } finally {
                                        List<Runnable> droppedTasks = executor.shutdownNow();
                                        if (!droppedTasks.isEmpty()) {
                                            System.out.println("⚠ Some tasks were not executed: " + droppedTasks.size());
                                        }
                                        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                                            System.out.println("⚠ Executor did not terminate in time");
                                        }
                                    }
                                } else {
                                    method.invoke(testInstance, convertedParams);
                                }
                                if (errorMessage == null) {
                                    System.out.println("✔ Test passed with param: " + paramSet);
                                }
                            } catch (Exception e) {
                                passed = false;
                                errorMessage = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                                System.out.println("❌ Test failed with param " + paramSet + ": " + e.getCause());
                            }

                            long endTime = System.nanoTime();
                            double endCpu = MetricsCollector.getProcessCpuLoad();
                            double endMem = MetricsCollector.getMemoryUsageMb();

                            long execTimeMs = (endTime - startTime) / 1_000_000;
                            double cpuUsed = Math.max(0, endCpu - startCpu);
                            double memUsed = endMem - startMem;

                            results.add(new TestResult(
                                    testClass.getName(),
                                    method.getName() + " [param: " + paramSet + "]",
                                    passed,
                                    errorMessage,
                                    execTimeMs,
                                    cpuUsed,
                                    memUsed
                            ));

                            if (afterMethod != null) afterMethod.invoke(testInstance);
                        }
                        System.out.println("--------------------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object convertToType(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == String.class) {
            return value;
        }
        throw new IllegalArgumentException("Unsupported parameter type: " + type.getName());
    }

}