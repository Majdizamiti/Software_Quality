# JUnit Test Quick Reference

The backend uses JUnit 5 with Mockito. Service tests mock their repositories. Integration tests use Spring Boot Test, MockMvc, and H2.

## Commands

Run unit tests:

```powershell
cd backend
.\mvnw.cmd test "-Dtest=*ImplTest,*UnitTest,*JwtUtilsTest"
```

Run integration tests:

```powershell
.\mvnw.cmd test "-Dtest=*IntegrationTest"
```

Run the complete backend suite and coverage report:

```powershell
.\mvnw.cmd clean verify
```

Read text and XML results in `target/surefire-reports/`. Read the JaCoCo coverage report at `target/site/jacoco/index.html`.

## Reading the result

- `Tests run`: number of tests executed.
- `Failures`: assertions that did not match the expected result.
- `Errors`: tests that stopped because of an unexpected exception or setup problem.
- `Skipped`: tests intentionally not run.
- `BUILD SUCCESS`: Maven completed the requested command successfully.

Keep the console output or generated report as evidence. Do not copy example totals from an old run.
