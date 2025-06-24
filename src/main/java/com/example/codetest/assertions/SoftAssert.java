package com.example.codetest.assertions;

import java.util.ArrayList;
import java.util.List;

public class SoftAssert {

    private final List<String> errors = new ArrayList<>();

    public void assertTrue(boolean condition, String message) {
        if (!condition) {
            errors.add("assertTrue failed: " + message);
        }
    }

    public void assertFalse(boolean condition, String message) {
        if (condition) {
            errors.add("assertFalse failed: " + message);
        }
    }

    public void assertEquals(Object expected, Object actual, String message) {
        if ((expected == null && actual != null) || (expected != null & !expected.equals(actual))) {
            errors.add("assertEquals failed: " + message + " | expected: <" + expected + ">, actual: <" + actual + ">");
        }
    }

    public void assertNotNull(Object object, String message) {
        if (object == null) {
            errors.add("assertNotNull failed: " + message);
        }
    }

    public void assertNull(Object object, String message) {
        if (object != null) {
            errors.add("assertNull failed: " + message);
        }
    }

    public void assertAll() {
        if (!errors.isEmpty()) {
            throw new AssertionError("Soft assertion errors:/n" + String.join("\n", errors));
        }
    }
}
