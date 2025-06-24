package com.example.codetest.examples;

import com.example.codetest.annotations.Disabled;
import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

@Disabled("The entire class is disabled for refactoring")
public class DisabledClassExampleTest {

    @Test
    public void testShouldNotRun() {
        System.out.println("This test should not be run");
        Assert.assertTrue(false);
    }
}