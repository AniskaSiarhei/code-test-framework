package com.example.codetest.runner;

import com.example.codetest.annotations.*;
import com.example.codetest.report.*;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.reflect.Method;
import java.util.*;

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

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethod = method;
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethod = method;
                }
            }

            for (Method method : testClass.getDeclaredMethods()) {

                if (method.isAnnotationPresent(Test.class)) {
                    if (beforeMethod != null) beforeMethod.invoke(testInstance);

                    System.out.println("Running test: " + method.getName());
                    boolean passed = true;
                    String errorMessage = null;
                    try {
                        method.invoke(testInstance);
                        System.out.println("✔ Test passed");
                    } catch (Exception e) {
                        System.out.println("❌ Test failed: " + e.getCause());
                        passed = false;
                        errorMessage = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                    }

                    results.add(new TestResult(
                            testClass.getName(),
                            method.getName(),
                            passed,
                            errorMessage
                    ));

                    if (afterMethod != null) afterMethod.invoke(testInstance);
                    System.out.println("--------------------------------------------------");
                }

                if (method.isAnnotationPresent(ParameterizedTest.class)) {
                    ParameterizedTest annotation = method.getAnnotation(ParameterizedTest.class);
                    String[] params = annotation.value();

                    for (String param : params) {
                        if (beforeMethod != null) beforeMethod.invoke(testInstance);

                        System.out.println("Running parameterized test: " + method.getName() + " with param: " + param);
                        boolean passed = true;
                        String errorMessage = null;

                        try {
                            method.invoke(testInstance, param);
                            System.out.println("✔ Test passed with param: " + param);
                        } catch (Exception e) {
                            System.out.println("❌ Test failed with param " + param + ": " + e.getCause());
                            passed = false;
                            errorMessage = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                        }

                        results.add(new TestResult(
                                testClass.getName(),
                                method.getName() + " [param: " + param + "]",
                                passed,
                                errorMessage
                        ));

                        if (afterMethod != null) afterMethod.invoke(testInstance);
                    }
                    System.out.println("--------------------------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}