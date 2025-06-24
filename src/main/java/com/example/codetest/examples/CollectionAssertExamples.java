package com.example.codetest.examples;

import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CollectionAssertExamples {

    @Test
    public void testAssertListEquals() {
        List<String> expected = Arrays.asList("A", "B", "C");
        List<String> actual = Arrays.asList("A", "B", "C");
        Assert.assertListEquals(expected, actual);
    }

    @Test
    public void testAssertMapEquals() {
        Map<String, Integer> expected = Map.of("One", 1, "Two", 2);
        Map<String, Integer> actual = Map.of("One", 1, "Two", 2);

        Assert.assertMapEquals(expected, actual);
    }

    @Test
    public void testAssertContains() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Assert.assertContains(list, "banana");
    }

}