package com.example.codetest.assertions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Assert {

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed: Expected: <true> but was: <false>");
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Assertion failed: Expected: <false> but was: <true>");
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Assertion failed: Expected <" + expected + "> but was <" + actual + ">");
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError(message);
    }

    public static void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError("Assertion failed: Expected <" + expected + "> but was <" + actual + ">");
        }
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(Object object) {
        if (object == null) {
            throw new AssertionError("Assertion failed: Expected: <NOT null> but was: <null>");
        }
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object object) {
        if (object != null) {
            throw new AssertionError("Assertion failed: Expected: <null> but was <" + object + ">");
        }
    }

    public static void assertNull(Object object, String message) {
        if (object != null) {
            throw new AssertionError(message);
        }
    }

    public static void assertThrows(Class<? extends Throwable> exceptionClass, Executable executable) {
        try {
            executable.execute();
            throw new AssertionError("Assertion failed: Expected exception " + exceptionClass.getName() + " but none was thrown");
        } catch (Throwable actualException) {
            if (!exceptionClass.isInstance(actualException)) {
                throw new AssertionError("Assertion failed: Expected exception " + exceptionClass.getName() + " but got " + actualException.getClass().getName());
            }
        }
    }

    public static void assertThrows(Class<? extends Throwable> exceptionClass, Executable executable, String message) {
        try {
            executable.execute();
            throw new AssertionError(message);
        } catch (Throwable actualException) {
            if (!exceptionClass.isInstance(actualException)) {
                throw new AssertionError(message);
            }
        }
    }

    public static <T> void assertListEquals(List<T> expected, List<T> actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Assertion failed: Expected list: <" + expected + "> but was: <" + actual + ">");
    }

    public static <T> void assertListEquals(List<T> expected, List<T> actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError(message);
    }

    public static <K, V> void assertMapEquals(Map<K, V> expected, Map<K, V> actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Assertion failed: Expected map: <" + expected + "> but was: <" + actual + ">");
    }

    public static <K, V> void assertMapEquals(Map<K, V> expected, Map<K, V> actual, String message) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError(message);
    }

    public static <T> void assertContains(Collection <T> collection, T item) {
        if (collection == null || !collection.contains(item)) {
            throw new AssertionError("Assertion failed: Collection does not contain expected item: " + item);
        }
    }

    public static <T> void assertContains(Collection <T> collection, T item, String message) {
        if (collection == null || !collection.contains(item)) {
            throw new AssertionError(message);
        }
    }
}






















