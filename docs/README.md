# SpringZ Test Documentation

Use this page to find the frontend and backend test instructions. Run the commands from the project root unless a block tells you to change directory.

Track implementation and rerun status in the [test checklist](TEST_CHECKLIST.md).

## Prerequisites

- JDK 17 for the backend.
- Node.js and npm for the frontend.
- Playwright Chromium for frontend browser tests.
- Maven is included through `backend/mvnw.cmd`; no separate Maven install is needed.
- Backend tests use H2 in memory. MySQL is needed only to run the application with its normal database configuration.
- k6 is needed only for the optional load test. Docker is optional for local SonarQube.
- Docker Desktop with Compose v2 is needed for the reproducible deployment / portability test.

## Backend tests

Run the backend unit and integration suites together:

```powershell
.\backend\mvnw.cmd clean test
```

Guides for the backend tests and scans:

- [Unit tests](backend/unit-tests.md)
- [JUnit reference](backend/junit-reference.md)
- [Integration tests](backend/integration-tests.md)
- [API contract tests with Postman](backend/postman-api-contract-tests.md)
- [Security tests](backend/security-tests.md)
- [SAST and dependency scanning](backend/sast-sca.md)
- [SonarQube and JaCoCo coverage](backend/sonarqube-coverage.md)
- [Performance testing with k6](backend/performance-k6.md)

## Frontend tests

Install dependencies, install Chromium, build the frontend, and run the E2E suite:

```powershell
cd frontend
npm ci
npx playwright install chromium
npm run build
npm run test:e2e
```

See the [Playwright E2E guide](frontend/e2e-playwright.md). The current E2E configuration runs Chromium only and starts Vite automatically.

## Shared ISO 25010 report

The [quality report](RAPPORT_QUALITE_ISO25010.md) summarizes the whole project. It is a checklist, not proof of a fresh run. After testing, record the date, command, result, and generated report path.

## Optional k6 load test

Follow the [k6 guide](backend/performance-k6.md) to start SpringZ with a test database first. The k6 runner can use the Docker Compose service:

```powershell
docker compose run --rm k6
```

Or install k6 locally with Chocolatey and run it without Docker:

```powershell
choco install k6
k6 run .\performance\k6-load-test.js
```

Both methods require SpringZ and the test database to be running. The script creates accounts in `ecommerce_k6`; never point it at production.

## Portability deployment test

Use [the Docker portability guide](portability-deployment.md) to build and start the frontend, backend, and MySQL from a fresh checkout with Docker Compose. Record a pass only after starting and checking the deployment on another machine or fresh environment.

