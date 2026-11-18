# Complete Unit Testing Guide (JUnit 5 + Mockito)

This documentation provides an easy-to-understand explanation of the unit test suite created for the **SpringZ E-Commerce Backend**.

---

## 1. What is a Unit Test? (Concept & Analogies)

### Simple Definition
A **Unit Test** tests a single piece of code (like a method in a class) in total isolation from the rest of the system.

* **No Database:** Unit tests do not connect to MySQL or H2 databases.
* **No Web Server:** Unit tests do not start Spring Boot or Tomcat.
* **Ultra-Fast:** Because there is no database or server startup, all **25 tests run in 4 seconds**.

### The Mockito Concept (Fake Dependencies)
Imagine testing a cashier machine. You don't want to insert a real credit card or contact a real bank for every test. Instead, you use a **Fake Bank (Mock)** that instantly answers *"Yes, payment approved!"*.

In Java unit testing:
* **`@Mock`**: Creates a fake object (e.g. a fake `UserRepository` or `CategoryRepository`).
* **`when(...).thenReturn(...)`**: Teaches the fake object how to respond when a method is called.
* **`@InjectMocks`**: Creates the real service class and automatically injects the fake objects into it.

---

## 2. Key Annotations & Keywords Explained

| Annotation / Keyword | What it Does in Plain Words |
| :--- | :--- |
| `@ExtendWith(MockitoExtension.class)` | Tells JUnit 5 to enable Mockito mock objects. |
| `@Mock` | Creates a fake version of a dependency (like a database repository). |
| `@InjectMocks` | Creates the real class under test and plugs in the fake `@Mock` dependencies. |
| `@BeforeEach` | Runs code setup before every single `@Test` method. |
| `@Test` | Marks a method as a test that JUnit will run. |
| `when(mock.method()).thenReturn(value)` | "When this fake method is called, return this fake result." |
| `assertEquals(expected, actual)` | Checks if two values are equal. If not, the test fails. |
| `assertNotNull(object)` | Checks that an object is not null. |
| `assertThrows(Exception.class, lambda)` | Checks that calling a method throws a specific exception (e.g., "Category not found"). |
| `verify(mock, times(1)).method()` | Verifies that a method on the fake object was called exactly 1 time. |

---

## 3. Detailed Explanation of All 25 Unit Tests

### A. Category Service Unit Tests ([CategoryServiceImplTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/service/CategoryServiceImplTest.java))

* **Tested Class:** `CategoryServiceImpl`
* **Fake Dependency:** `CategoryRepository`

| Test Method Name | What it Tests |
| :--- | :--- |
| `createCategory_Success()` | Passes a new Category to the service and verifies that `save()` is called and the saved category is returned. |
| `getAllCategories_Success()` | Verifies that `getAllCategories()` calls `findAll()` on the repository and returns the category list. |
| `getCategoryByID_Found()` | Passes an ID (e.g., `1L`) that exists and verifies the category is returned. |
| `getCategoryByID_NotFound_ThrowsException()` | Passes an ID (e.g., `99L`) that does NOT exist and verifies `IllegalArgumentException` is thrown with message `"Category not found"`. |
| `deleteCategory_Success()` | Passes an ID to `deleteCategory()` and verifies `deleteById(1L)` was called on the repository. |

---

### B. Product Service Unit Tests ([ProductServiceImplTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/service/ProductServiceImplTest.java))

* **Tested Class:** `ProductServiceImpl`
* **Fake Dependency:** `ProductRepository`

| Test Method Name | What it Tests |
| :--- | :--- |
| `createProduct_Success()` | Verifies saving a Product (e.g. Laptop, Price: 1200.0) returns the saved Product object. |
| `getAllProducts_Success()` | Verifies that retrieving all products returns the correct list of products. |
| `getProductByID_Found()` | Verifies fetching a product by ID when it exists. |
| `getProductByID_NotFound_ThrowsException()` | Verifies an `IllegalArgumentException` is thrown when a product ID is not found. |
| `deleteProduct_Success()` | Verifies `deleteById()` is invoked correctly on the repository. |

---

### C. Customer Service Unit Tests ([CustomerServiceImplTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/service/CustomerServiceImplTest.java))

* **Tested Class:** `CustomerServiceImpl`
* **Fake Dependency:** `CustomerRepository`

| Test Method Name | What it Tests |
| :--- | :--- |
| `createCustomer_Success()` | Verifies customer creation (e.g. "John Doe") returns the created customer. |
| `getAllCustomers_Success()` | Verifies fetching the full customer list. |
| `getCustomerByID_Found()` | Verifies retrieving a customer by ID. |
| `getCustomerByID_NotFound_ThrowsException()` | Verifies an exception is thrown when looking up a non-existent customer. |
| `deleteCustomer_Success()` | Verifies repository deletion for customers. |

---

### D. User Service Unit Tests ([UserServiceImplTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/service/UserServiceImplTest.java))

* **Tested Class:** `UserServiceImpl`
* **Fake Dependency:** `UserRepository`

| Test Method Name | What it Tests |
| :--- | :--- |
| `createUser_Success()` | Verifies user creation (e.g. "Alice") and repository persistence call. |
| `getAllUsers_Success()` | Verifies fetching all users. |
| `getUserByID_Found()` | Verifies fetching a user by ID. |
| `getUserByID_NotFound_ThrowsException()` | Verifies `IllegalArgumentException` when searching for an invalid user ID. |
| `deleteUser_Success()` | Verifies deleting a user by ID. |

---

### E. Authentication Controller Unit Tests ([AuthControllerUnitTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/api/AuthControllerUnitTest.java))

* **Tested Class:** `AuthController`
* **Fake Dependencies:** `AuthenticationManager`, `UserRepository`, `PasswordEncoder`, `JwtUtils`

| Test Method Name | What it Tests |
| :--- | :--- |
| `signup_Success()` | Tests registering a new user (`POST /api/auth/signup`). Mocks email check (`existsByEmail = false`) and password encoding, then verifies HTTP 200 OK + "User registered successfully". |
| `signup_EmailAlreadyExists_ReturnsBadRequest()` | Tests registering with an email that is already taken (`existsByEmail = true`). Verifies HTTP 400 Bad Request + "Error: Email is already in use!". |
| `signin_Success()` | Tests signing in (`POST /api/auth/signin`). Mocks user authentication and JWT token generation, then verifies HTTP 200 OK + JWT token response. |

---

### F. Security & JWT Unit Tests ([JwtUtilsTest.java](file:///C:/ing5/test/hackathon_Africana/src/test/java/com/Shadows/SpringZ/security/JwtUtilsTest.java))

* **Tested Class:** `JwtUtils`

| Test Method Name | What it Tests |
| :--- | :--- |
| `generateTokenFromSubject_ReturnsValidToken()` | Generates a JWT token for `"test@example.com"`, verifies token is valid, and checks that extracting the subject returns `"test@example.com"`. |
| `validateJwtToken_InvalidToken_ReturnsFalse()` | Passes a malformed token string `"invalid.token.string"` and verifies `validateJwtToken()` returns `false`. |

---

## 4. How to Run the Unit Tests

Execute the unit test suite from your terminal:

```powershell
./mvnw.cmd test
```

### Understanding the Terminal Summary Output:

```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.187 s
```

* **`Tests run: 25`**: Executed all 25 unit test methods.
* **`Failures: 0`**: All assertions (`assertEquals`, `assertTrue`, etc.) evaluated to true.
* **`Errors: 0`**: No uncaught Java exceptions or crashes occurred.
* **`Total time: 4.187 s`**: Fast execution because no database or Spring server was loaded.
