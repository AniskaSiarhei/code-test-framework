# 🧪 CodeTest Framework

> A powerful and lightweight Java testing framework with Spring support, parameterized tests, timeouts, soft assertions, custom assertions, and beautiful HTML/JSON reports.

---

## 🚀 Features

- ✅ **Annotations `@Test`, `@Before`, `@After`** — similar to JUnit.
- ✅ **Custom Assertions** — including `assertListEquals`, `assertMapEquals`, `assertContains`, `assertThrows`, and more.
- ✅ **Soft Assertions** — collect multiple failures without stopping execution.
- ✅ **Timeout Support** — mark tests as failed if they exceed the specified time.
- ✅ **Test Ignoring with `@Disabled`** — disable methods or entire classes with optional reason.
- ✅ **Parameterized Tests `@ParameterizedTest`** — support for strings, integers, booleans.
- ✅ **Spring Integration** — use dependency injection in your test classes.
- ✅ **Execution Metrics** — track execution time, CPU usage, and memory consumption.
- ✅ **HTML & JSON Reports** — visual feedback for test results and metrics.

---

## 📦 Usage Example

```java
@Test
public void testSimpleAssert() {
    Assert.assertEquals(2 + 2, 4);
}

@ParameterizedTest({"5, true", "0, false"})
public void checkNumberPositive(int number, boolean expected) {
    Assert.assertEquals(expected, number > 0);
}
```
---

## 📊 Report Example

After test execution:

-✅ **HTML report**: target/test-report.html

-✅ **JSON report**: target/test-report.json

Reports include test status, error messages, execution time, CPU usage, and memory usage.

---

## 🛠️ Running Tests

```java
// In your main class
TestRunner.runAllTests("com.example.codetest.examples");
```
---

## 📂 Project Structure

- annotations — custom annotations like @Test, @Before, @Disabled, etc.
- assertions — built-in assertion utilities and soft assertions
- report — report generation (HTML & JSON)
- runner — test execution engine
- utils — metrics collection (CPU, memory)
