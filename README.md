# SpringZ

SpringZ is an e-commerce application split into two independently runnable parts:

- `backend/` - Spring Boot REST/MVC application, Maven build, and Java tests
- `frontend/` - React + Vite application and Playwright E2E tests

## Run the backend

From the repository root:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend uses port `8081` by default. See [backend/README.md](backend/README.md) for database configuration, API details, and backend tests.

## Run the frontend

```powershell
cd frontend
npm install
npm run dev
```

The frontend expects the backend at `http://localhost:8081/api`.

## Repository layout

```text
backend/      Spring Boot application and Maven project
frontend/     React/Vite application
docs/         Quality, testing, security, and performance documentation
performance/  k6 load tests
postman/      API collection
```