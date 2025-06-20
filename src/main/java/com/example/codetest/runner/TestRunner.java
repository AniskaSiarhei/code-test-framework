package com.example.codetest.runner;

import com.example.codetest.annotations.After;
import com.example.codetest.annotations.Before;
import com.example.codetest.annotations.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class TestRunner {

    public static void runAllTests(String basePackage) {

        try {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

            TypeFilter typeMethodFilter = new TypeFilter() {

                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    try {
                        String className = metadataReader.getClassMetadata().getClassName();
                        Class<?> clazz = Class.forName(className);
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(Test.class)) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };

            scanner.addIncludeFilter(typeMethodFilter);

            Set<org.springframework.beans.factory.config.BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (org.springframework.beans.factory.config.BeanDefinition candidate : candidates) {
                String className = candidate.getBeanClassName();
                Class<?> clazz = Class.forName(className);
                runTests(clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public static void runTests(Class<?> testClass) {
    try {
        Object testInstance = testClass.getDeclaredConstructor().newInstance();

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
                try {
                    method.invoke(testInstance);
                    System.out.println("✔ Test passed");
                } catch (Exception e) {
                    System.out.println("❌ Test failed: " + e.getCause());
                }

                if (afterMethod != null) afterMethod.invoke(testInstance);
                System.out.println("--------------------------------------------------");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}