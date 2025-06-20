package com.example.codetest.examples;

import com.example.codetest.annotations.ParameterizedTest;
import com.example.codetest.assertions.Assert;
import org.springframework.stereotype.Component;

@Component
public class ParameterizedTestExample {

    @ParameterizedTest({"hello", "world", "java"})
    public void testStringIsNotEmpty(String input) {
        Assert.assertTrue(input.length() > 0);
    }
}
