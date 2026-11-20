# Portability test: reproducible Docker deployment

This test checks the ISO/IEC 25010 portability requirement: build and run the SpringZ frontend, backend, and MySQL database from the repository on another machine using Docker Compose.

## Requirements

- Git
- Docker Desktop with the Linux container engine and Compose v2
- Internet access for the first build (Docker downloads the base images and Maven/npm dependencies)
- About 3 GB of free disk space for images, build layers, and the database

No local Java, Maven, Node.js, npm, or MySQL installation is needed for this deployment test.

## Run from a clean checkout

In PowerShell, clone the repository (or use a fresh checkout), then run these commands from the repository root:

```powershell
git clone <repository-url>
cd <project-path>
docker compose config --quiet
docker compose build --no-cache
docker compose up -d
docker compose ps
```

`docker compose build --no-cache` performs clean image builds rather than relying on previously built application images. First builds can take several minutes.

Wait for MySQL to become healthy and SpringZ to start:

```powershell
docker compose logs --follow db backend
```

Once the backend log says it has started, press `Ctrl+C` to stop following logs (this does not stop the containers). Check the exposed API and frontend:

```powershell
Invoke-RestMethod http://localhost:8081/
Invoke-WebRequest http://localhost:5173/ -UseBasicParsing | Select-Object StatusCode
```

Expected: the API response reports `status: UP`; the frontend returns HTTP `200`. Open <http://localhost:5173> in a browser and confirm the SpringZ UI loads. The frontend container proxies `/api` requests to the backend container, and the backend connects to the Compose MySQL service by its service name (`db`).

## Record the result

The portability test passes only if the clean build succeeds, all three services start, and both checks return the expected results on a second machine or fresh environment. Save the command output and date as evidence. Do not mark it passed based only on successful configuration or image builds.

Record pass/fail, date, machine/OS, Docker version, and any errors in [the test checklist](TEST_CHECKLIST.md).

## Stop the deployment

Stop containers while preserving the database volume:

```powershell
docker compose down
```

To also delete the local test database volume and its data:

```powershell
docker compose down --volumes
```

The database password defaults to `springz-local-only` for local portability testing. Set `MYSQL_ROOT_PASSWORD` in the shell before `docker compose up` if you want a different local value. Do not use the default or commit real credentials for a public deployment.
