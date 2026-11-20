# Backend Unit Tests

Unit tests use JUnit 5 and Mockito to test services, authentication logic, and JWT utilities. They use mocked dependencies and do not need MySQL or a running web server.

## Run the unit tests

From the project root:

```powershell
cd backend
.\mvnw.cmd test "-Dtest=*ImplTest,*UnitTest,*JwtUtilsTest"
```

To run the full backend suite instead:

```powershell
.\mvnw.cmd clean test
```

From the `backend` directory, Surefire reports are written to `target/surefire-reports/`.

## Existing unit test classes

| Test class | Main coverage |
|---|---|
| `CategoryServiceImplTest` | Category create, list, lookup, missing item, and delete |
| `ProductServiceImplTest` | Product create, list, lookup, missing item, and delete |
| `CustomerServiceImplTest` | Customer create, list, lookup, missing item, and delete |
| `UserServiceImplTest` | User create, list, lookup, missing item, and delete |
| `AuthControllerUnitTest` | Signup, duplicate email, and successful signin |
| `JwtUtilsTest` | JWT creation, subject extraction, and invalid token handling |

The number of passing tests can change as the code changes. Use the output from your own run rather than counts copied from an older report.
