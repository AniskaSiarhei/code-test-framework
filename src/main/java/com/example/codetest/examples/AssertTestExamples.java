package com.example.codetest.examples;

import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

public class AssertTestExamples {

    @Test
    public void testAssertTrue() {
        Assert.assertTrue(5 > 1);
    }

    @Test
    public void testAssertFalse() {
        Assert.assertFalse(3 < 2);
    }

    @Test
    public void testAssertEqualsObjects() {
        Assert.assertEquals("hello", "hello");
    }

    @Test
    public void testAssertEqualsDoubles() {
        Assert.assertEquals(3.14, 3.141, 0.01);
    }

    @Test
    public void testAssertNotNull() {
        Assert.assertNotNull("I am not null");
    }

    @Test
    public void testAssertNull() {
        Assert.assertNull(null);
    }

    @Test
    public void testAssertThrows() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Expected exception");
        });
    }
}