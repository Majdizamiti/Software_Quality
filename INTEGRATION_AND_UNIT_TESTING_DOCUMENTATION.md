# Complete Testing Guide: Unit Tests & Integration Tests

This document describes the testing architecture built for the **SpringZ E-Commerce Backend**, combining fast **Unit Tests** and full-stack **Integration Tests**.

---

## 1. What is the Difference Between Unit Tests & Integration Tests?

| Feature | Unit Tests | Integration Tests |
| :--- | :--- | :--- |
| **Scope** | Tests 1 class/method in total isolation. | Tests multiple components working together (HTTP + Security + DB). |
| **Database** | No database (uses Mockito fake objects). | In-Memory H2 Database (`jdbc:h2:mem:testdb`). |
| **Spring Server** | No Spring Boot context loaded. | Loads `@SpringBootTest` & `@AutoConfigureMockMvc`. |
| **Speed** | Instant (< 2 seconds for 25 tests). | Fast (~10-15 seconds total). |

---

## 2. Test Suite Breakdown (31 Tests Total)

### A. Pure Unit Tests (25 Tests)
Isolated unit tests using JUnit 5 and Mockito:

1. **`CategoryServiceImplTest.java` (5 Tests):**
   * Creation, listing, finding by ID, missing ID error handling, and deletion.
2. **`ProductServiceImplTest.java` (5 Tests):**
   * Product creation, price validation, listing, finding by ID, and deletion.
3. **`CustomerServiceImplTest.java` (5 Tests):**
   * Customer creation, listing, finding by ID, error handling, and deletion.
4. **`UserServiceImplTest.java` (5 Tests):**
   * User creation, listing, finding by ID, error handling, and deletion.
5. **`AuthControllerUnitTest.java` (3 Tests):**
   * Signup logic, duplicate email rejection (400), and Signin token generation.
6. **`JwtUtilsTest.java` (2 Tests):**
   * JWT signing, expiration/validity checks, and subject parsing.

---

### B. Integration Tests (6 Tests)
Full HTTP API tests using `@SpringBootTest` and `MockMvc`:

1. **`AuthControllerIntegrationTest.java` (4 Tests):**
   * `signup_Success()`: POST `/api/auth/signup` $\rightarrow$ HTTP 200 OK + Registration message.
   * `signup_DuplicateEmail_Returns400()`: POST `/api/auth/signup` twice $\rightarrow$ HTTP 400 Bad Request.
   * `signin_Success_ReturnsJwtToken()`: Signup & Signin $\rightarrow$ HTTP 200 OK + JWT bearer token.
   * `signin_InvalidPassword_Returns401()`: Signin with wrong password $\rightarrow$ HTTP 401 Unauthorized.

2. **`CategoryApiControllerIntegrationTest.java` (2 Tests):**
   * `accessCategoriesWithoutToken_Returns401()`: GET `/api/categories` without token $\rightarrow$ HTTP 401 Unauthorized.
   * `categoryCrudLifecycle_Success()`: Full CRUD lifecycle:
     1. Authenticate & obtain JWT token.
     2. POST `/api/categories` with JWT $\rightarrow$ HTTP 21 Created.
     3. GET `/api/categories` $\rightarrow$ HTTP 200 OK.
     4. GET `/api/categories/{id}` $\rightarrow$ HTTP 200 OK.
     5. PUT `/api/categories/{id}` $\rightarrow$ HTTP 200 OK.
     6. DELETE `/api/categories/{id}` $\rightarrow$ HTTP 24 No Content.
     7. GET `/api/categories/{id}` $\rightarrow$ HTTP 404 Not Found.

---

## 3. How to Run All Tests

Run all 31 unit and integration tests from your terminal:

```powershell
./mvnw.cmd test
```

### Execution Output Summary:

```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
