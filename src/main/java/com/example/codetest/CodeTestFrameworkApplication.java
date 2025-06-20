package com.example.codetest;

import com.example.codetest.runner.TestRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeTestFrameworkApplication {

	public static void main(String[] args) {
		TestRunner.runAllTests("com.example.codetest.examples");
	}

}
