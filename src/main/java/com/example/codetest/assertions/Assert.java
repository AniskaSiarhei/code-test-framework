package com.example.codetest.assertions;

public class Assert {

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed: Expected: <true> but was: <false>");
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Assertion failed: Expected: <false> but was: <true>");
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

    public static void fail(String message) {
        throw new AssertionError("Test failed: " + message);
    }
}
