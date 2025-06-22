package com.example.codetest.examples;

import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomMessageAssertExamples {

    @Test
    public void testAssertEqualsMessage() {
        Assert.assertEquals(5, 5, "The values should be equal");
    }

    @Test
    public void testAssertListEqualsWithMessage() {
        List<String> expected = Arrays.asList("a", "b", "c");
        List<String> actual = Arrays.asList("a", "b", "c");
        Assert.assertListEquals(expected, actual, "The lists must match");
    }

    @Test
    public void testAssertMapEqualsWithMessage() {
        Map<String, Integer> expected = Map.of("One", 1, "Two", 2);
        Map<String, Integer> actual = Map.of("One", 1, "Two", 2);
        Assert.assertMapEquals(expected, actual, "The maps must match");
    }

    @Test
    public void testAssertContainsWithMessage() {
        List<String> list = Arrays.asList("a", "b", "c");
        Assert.assertTrue(list.contains("a"), "The list should contain 'a'");
    }
}














