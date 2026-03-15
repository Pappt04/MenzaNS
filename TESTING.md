# Testing Guide - MenzaNS Android App

This document outlines the testing strategy and guidelines for the MenzaNS Android application.

## Overview

The MenzaNS app uses a multi-layered testing approach:

- **Unit Tests** (`app/src/test/`) - Fast, isolated tests for repositories and ViewModels
- **Instrumentation Tests** (`app/src/androidTest/`) - Tests that run on Android devices/emulators
- **UI Tests** - Compose UI testing

## Unit Tests

### Running Unit Tests

```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run specific test class
./gradlew testDebugUnitTest --tests com.pappt04.menzans.repository.UserRepositoryTest

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

### Test Files Location

```
app/src/test/java/com/pappt04/menzans/
├── repository/
│   ├── UserRepositoryTest.kt
│   ├── MealRepositoryTest.kt
│   ├── CardRepositoryTest.kt
│   ├── SettingsRepositoryTest.kt
│   ├── WaitTimeRepositoryTest.kt
│   └── GeofenceRepositoryTest.kt
└── viewmodel/
    ├── MainViewModelTest.kt
    └── DashboardViewModelTest.kt
```

### Repository Test Structure

Each repository test follows this pattern:

```kotlin
class UserRepositoryTest {

    @Mock
    private lateinit var apiService: MenzaApiService

    @Mock
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFunctionName() = runTest {
        // Arrange
        val testData = ...

        // Act
        val result = ...

        // Assert
        assert(result == expected)
    }
}
```

### ViewModel Test Structure

ViewModel tests use `StandardTestDispatcher` for coroutine testing:

```kotlin
class MainViewModelTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testViewModelBehavior() = runTest(testDispatcher) {
        // Test implementation
    }
}
```

## Instrumentation Tests

### Running Instrumentation Tests

```bash
# Run all instrumentation tests (requires emulator/device)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew connectedAndroidTest --tests com.pappt04.menzans.ExampleInstrumentedTest
```

### Test Files Location

```
app/src/androidTest/java/com/pappt04/menzans/
└── ExampleInstrumentedTest.kt
```

## CI/CD Integration

### GitHub Actions Workflow

The project includes an automated GitHub Actions workflow (`.github/workflows/android-build.yml`) that:

- **Triggers**: On every push and pull request to `master` and `develop` branches
- **Steps**:
  1. Checks out code
  2. Sets up Java 17 and Gradle with caching
  3. Builds debug APK
  4. Runs unit tests
  5. Runs linting
  6. Uploads build reports on failure
  7. Comments on PR with build status

### Workflow Badge

Add this to your README.md:

```markdown
[![Android Build & Test](https://github.com/pappt04/MenzaNS/actions/workflows/android-build.yml/badge.svg)](https://github.com/pappt04/MenzaNS/actions/workflows/android-build.yml)
```

## Test Dependencies

The following testing libraries are included:

```toml
junit = "4.13.2"
mockito-core = "5.10.0"
mockito-kotlin = "5.4.0"
kotlinx-coroutines-test = "1.10.2"
androidx-junit = "1.3.0"
androidx-espresso-core = "3.7.0"
androidx-ui-test-junit4 = "compose-bom"
```

## Writing New Tests

### Guidelines

1. **Test One Thing**: Each test should verify one specific behavior
2. **Use Descriptive Names**: Test name should clearly describe what is being tested
3. **Follow AAA Pattern**: Arrange (setup), Act (execute), Assert (verify)
4. **Mock External Dependencies**: Use Mockito for API calls and data stores
5. **Test Both Success and Failure Cases**: Include tests for error scenarios

### Example: Writing a Repository Test

```kotlin
@Test
fun testGetUserBalance_WithValidUser_ReturnsCorrectBalance() = runTest {
    // Arrange
    val userId = "user123"
    val expectedBalance = 50.0
    whenever(apiService.getUserBalance(userId)).thenReturn(expectedBalance)

    // Act
    val result = userRepository.getUserBalance(userId)

    // Assert
    assertEquals(expectedBalance, result)
}
```

### Example: Writing a ViewModel Test

```kotlin
@Test
fun testLoadUserData_UpdatesUiState() = runTest(testDispatcher) {
    // Arrange
    val testUser = User(id = "123", name = "Test User")
    whenever(userRepository.getUser()).thenReturn(testUser)

    // Act
    mainViewModel.loadUser()

    // Assert - verify ViewModel state was updated
    assertEquals(testUser, mainViewModel.uiState.value.user)
}
```

## Best Practices

### Do's ✅

- Write tests **before** implementing features (TDD)
- Keep tests **independent** - no test should depend on another
- Use **meaningful assertions** - avoid empty assertions
- **Mock** external services and data sources
- Test **edge cases** and error conditions
- Keep tests **fast** - unit tests should run in milliseconds
- Use **descriptive test names** that explain the scenario

### Don'ts ❌

- Don't test **implementation details** - test behavior
- Don't **skip** tests that are flaky - fix them
- Don't create **test dependencies** - each test should be isolated
- Don't use **real APIs** or databases in unit tests
- Don't write **overly complex** tests
- Don't ignore **test failures** in CI/CD

## Test Coverage

To generate a coverage report:

```bash
./gradlew testDebugUnitTestCoverage
# Report location: app/build/reports/coverage/test/
```

## Troubleshooting

### Tests Not Running

1. Verify `testImplementation` dependencies in `build.gradle.kts`
2. Check that test files are in `app/src/test/` directory
3. Ensure test class names end with `Test`

### Mockito Issues

- Ensure `MockitoAnnotations.openMocks(this)` is called in `@Before`
- Use `whenever()` for mocking, not `Mockito.when()`
- Check that mocked objects are not null before use

### Coroutine Test Issues

- Use `runTest()` for suspending functions
- Use `StandardTestDispatcher` for ViewModel tests
- Always `advanceUntilIdle()` for testing delayed operations

## Resources

- [JUnit 4 Documentation](https://junit.org/junit4/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Kotlin Coroutines Testing](https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test)
- [Android Testing Guide](https://developer.android.com/training/testing)
