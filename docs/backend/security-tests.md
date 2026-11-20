# Security Test Guide

The repository has Spring Security/JWT checks within the backend test classes and a Postman API collection at `postman/SpringZ_API.postman_collection.json`. A separate `SecurityTestSuiteTest` class is not present in this checkout, although older documentation referred to one. Do not use that class name as a runnable command.

## Run the existing backend checks

```powershell
cd backend
.\mvnw.cmd test
```

These tests cover selected authentication, JWT, and unauthorized-request behavior. They do not cover every security scenario below.

## Security scenarios to add or verify

| Scenario | Current status |
|---|---|
| Request without JWT is rejected | Covered by integration tests |
| Invalid or expired JWT is rejected | Some JWT utility checks exist; add an API-level test for each case |
| SQL injection input does not bypass authentication or damage data | Not established by the current test suite |
| Stored XSS input is safely displayed | Not established by the current test suite |
| CORS preflight behavior is correct | Not established by a dedicated test |
| Passwords are stored hashed | Verify with an explicit test |
| Role-based access is enforced | Not implemented as multiple roles in the current application |

OWASP ZAP is optional for a dynamic scan. Start the application in a disposable test environment, scan only that local instance, and save the ZAP report. Do not claim a DAST scan was run unless you have its output.
