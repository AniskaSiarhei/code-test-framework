package com.example.codetest.examples;

import com.example.codetest.annotations.After;
import com.example.codetest.annotations.Before;
import com.example.codetest.annotations.Test;
import com.example.codetest.assertions.Assert;

public class SampleTest {

    private int value;

    @Before
    public void setUp() {
        System.out.println("Setup method called");
        value = 5;
    }

    @Test
     public void firstTest() {
        System.out.println("First test executed");
        Assert.assertTrue(value > 0);
    }

    @Test
    public void secondTest() {
        System.out.println("Second test executed");
        Assert.assertEquals(5, value);
    }

    @Test
    public void thirdTest() {
        System.out.println("Third test executed");
        Assert.assertFalse(value < 0);
    }

    @After
    public void teardown() {
        System.out.println("Teardown method called");
    }
}















