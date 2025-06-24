package com.example.codetest.examples;

import com.example.codetest.annotations.Test;
import com.example.codetest.annotations.Timeout;

public class TimeoutExample {

    @Test
    @Timeout(1000)
    public void testWithTimeout() throws InterruptedException {
        Thread.sleep(2000);
    }
}