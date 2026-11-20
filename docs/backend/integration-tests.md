# Backend Integration Tests

Integration tests use Spring Boot Test, MockMvc, Spring Security Test, and an in-memory H2 database. They start the Spring application context but do not require a separate MySQL server.

## Run the integration tests

From the project root:

```powershell
cd backend
.\mvnw.cmd test "-Dtest=*IntegrationTest"
```

To run all backend tests, including unit tests:

```powershell
.\mvnw.cmd clean test
```

From the `backend` directory, reports are written to `target/surefire-reports/`.

## Existing integration coverage

| Test class | Coverage |
|---|---|
| `AuthControllerIntegrationTest` | Signup, duplicate email, successful signin, and wrong-password response |
| `CategoryApiControllerIntegrationTest` | Category API behavior, including JWT-protected requests and CRUD |

The test database uses `backend/src/test/resources/application.properties`. Results shown in older reports must be rerun on your machine before you report them as current.
