# SpringZ Quality Report and Test Status

This file maps the ISO/IEC 25010 quality goals to tests and tools present in the repository. It is a working checklist. It does not claim that the tests or scans have been run on the current machine.

## Project stack

- Backend: Java 17, Spring Boot, Spring Security, JWT, and Spring Data JPA.
- Frontend: React, Vite, and React Router.
- Test database: H2 in memory. The application can also use MySQL.

## Test and quality status

| Quality area | Existing test/tool | Current evidence status |
|---|---|---|
| Functional suitability | JUnit/Mockito, MockMvc integration tests, five Playwright tests | Tests exist; rerun them and save fresh output |
| Performance efficiency | k6 script with 20 virtual users and response thresholds | Script exists; run it against a test backend |
| Compatibility | Playwright configured for Chromium | One browser configured; second browser still needs testing |
| Usability | Basic dashboard and auth page checks | No accessibility or user acceptance evidence documented |
| Reliability | H2-backed integration tests with transaction rollback | Add explicit error and recovery scenarios |
| Security | Spring Security/JWT tests, SpotBugs and Dependency-Check plugins, Postman collection | Some checks are configured; run scans and add missing security tests |
| Maintainability | JaCoCo and SonarQube plugins | Generate fresh coverage and analysis reports |
| Portability | Maven wrapper; no full application Docker Compose setup found | Verify setup on another machine or add a reproducible container setup |

## Functional requirements still needing attention

The project brief asks for three user profiles, a status workflow, an audit log, search/filtering, and at least five explicit business rules. The current code gives users a single `ROLE_USER` authority; these requirements are not demonstrated by the existing tests and some are not implemented. Implement the missing behavior before claiming full functional compliance.

## Suggested run order

Follow the copyable commands in [README.md](README.md): run backend tests, install and run the frontend tests, then run optional k6 and security/quality scans. Save the real output and report files. Replace this table's evidence status only after each command has completed.

## Evidence to keep

- Maven console output and `backend/target/surefire-reports/`.
- Playwright HTML report in `frontend/playwright-report/`.
- k6 console summary, including `http_req_duration` and `http_req_failed`.
- JaCoCo, SonarQube, SpotBugs, Dependency-Check, or ZAP reports for scans actually run.
- Date, command, environment, result, and any failures for each run.
