# SpringZ Test Checklist

Use this checklist to track what is implemented, what has been rerun on this checkout, and what remains for the ISO 25010 brief. A test file or old report does not count as a fresh passing result.

## Implemented test assets

- [x] Backend unit tests exist for category, product, customer, and user services, authentication, and JWT utilities. The report describes 25 unit tests.
- [x] Backend integration tests exist for authentication and category API behavior. The report describes 6 integration tests.
- [x] Five Playwright browser tests exist for dashboard display, auth form switching, protected-page redirect, navigation, and dashboard cards.
- [x] A k6 load-test script exists for the root endpoint, health endpoint, and signup endpoint.
- [x] Maven configuration includes JaCoCo, SpotBugs, OWASP Dependency-Check, and SonarQube plugins.
- [x] A Postman API collection exists.
- [x] A focused Postman contract collection exists with auth and category lifecycle assertions.
- [x] Postman test scripts added to all requests in the full API collection, including exact submitted/returned data comparisons for create and update responses.
- [x] Docker Compose deployment assets exist for MySQL, backend, and frontend, with a documented portability test in [the Docker portability guide](portability-deployment.md).

## Run status on this checkout

- [x] Frontend dependencies installed with `npm ci`.
- [x] Frontend production build passed with `npm run build`.
- [x] Playwright tests run by the user. Pass/fail summary has not been recorded here.
- [x] An earlier k6 attempt failed before SpringZ was reachable: connection refused, 100% HTTP failures, and 0 bytes received.
- [x] A later Docker Compose k6 run passed: p95 150.33 ms, HTTP failures 0.00% (0 of 1,182), all 1,970 checks passed, maximum 20 VUs. Result supplied by the user.
- [ ] Backend tests freshly passed. The run stalled during Mockito self-attach on the installed Java 21 runtime and was interrupted. No fresh pass/fail result was produced.
- [ ] Playwright outcome recorded in this checklist (share the pass/fail summary to update this accurately).
- [ ] Postman API contract collection run and results saved.
- [ ] SpotBugs freshly run and reviewed.
- [ ] OWASP Dependency-Check freshly run and reviewed.
- [ ] SonarQube analysis freshly run and reviewed.
- [ ] OWASP ZAP dynamic scan freshly run and report saved.

Old Surefire reports and statements in the earlier report are historical evidence only. Rerun each check and save its actual output before marking it passed here.

## Functional and quality work still needed

- [ ] Add the three distinct user profiles required by the project brief. The current implementation grants users only `ROLE_USER`.
- [ ] Implement and test a workflow with status transitions.
- [ ] Implement and test an audit log for sensitive actions.
- [ ] Implement and test search/filtering for a high-volume entity.
- [ ] Define at least five business rules and add tests for them.
- [ ] Expand API CRUD tests beyond the category lifecycle to the other business entities.
- [ ] Add authenticated frontend flows that exercise the UI through the API, including create/update/delete behavior.
- [ ] Add tests for invalid and expired JWTs at the protected API level.
- [ ] Add security tests for injection inputs, CORS, stored XSS handling, and password hashing. These scenarios are listed in older documentation but are not all implemented as automated tests.
- [ ] Run E2E tests in a second browser to provide compatibility evidence; current Playwright configuration uses Chromium only.
- [ ] Perform and record basic accessibility/usability checks.
- [ ] Add reliability tests for error handling and recovery, beyond transaction rollback.
- [ ] Verify the Docker deployment on another machine or fresh environment and record the result.

Portability status: Docker deployment is implemented, but the portability requirement remains unverified until the documented clean-checkout run passes on another machine or fresh environment and its output is recorded.

## Suggested execution order

1. [ ] Select JDK 17, then run the backend suite: `.\backend\mvnw.cmd clean test`.
2. [ ] Install frontend dependencies and Chromium, build, then run Playwright. See [Frontend E2E tests](frontend/e2e-playwright.md).
3. [ ] Complete the functional gaps above before expanding end-to-end coverage.
4. [x] Run k6 against a disposable test database (passed; see metrics above).
5. [ ] Run the static analysis and dependency scans, then review their reports.
6. [ ] Update this checklist with the date, exact command, outcome, and report path for every completed run.
