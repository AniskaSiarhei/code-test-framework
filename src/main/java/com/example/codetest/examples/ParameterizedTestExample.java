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

    @ParameterizedTest({
            "5, true",
            "10, true",
            "0, false"
    })
    public void testMultipleParams(int number, boolean expectedResult) {
        Assert.assertEquals(expectedResult, number > 0);
    }
}



