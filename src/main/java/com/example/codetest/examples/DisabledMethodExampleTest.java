package com.example.codetest.examples;

import com.example.codetest.annotations.Disabled;
import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

public class DisabledMethodExampleTest {

    @Disabled("Awaiting bug fix")
    @Test
    public void disabledMethod() {
        Assert.assertTrue(false);
    }
}