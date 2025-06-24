package com.example.codetest.examples;

import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.SoftAssert;

public class SoftAssertExampleTest {

    @Test
    public void testWithSoftAssertions() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(2 > 3, "2 is not greater than 3");
        softAssert.assertEquals("Hello", "World", "Strings should match");
        softAssert.assertNotNull(null, "Object should not be null");

        softAssert.assertAll();
    }
}
