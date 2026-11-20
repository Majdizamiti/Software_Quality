# Performance Testing with k6

The script in `performance/k6-load-test.js` checks SpringZ's root, Actuator health, and signup endpoints. It ramps to 20 virtual users and enforces a p95 response time below 500 ms and an HTTP failure rate below 1%.

Choose one MySQL option, then start SpringZ. The k6 runner can be either the Docker Compose service or a local k6 installation.

## 1. Start a test database

### Option A: MySQL in Docker

This is the method used for the successful run recorded below. Run from the project root. Docker Desktop must be running, and port 3306 must be free.

```powershell
docker run --rm -d --name springz-k6-db -e MYSQL_ROOT_PASSWORD=k6localpass -e MYSQL_ROOT_HOST=% -e MYSQL_DATABASE=ecommerce_k6 -p 3306:3306 mysql:8
```

Wait until MySQL says it is ready for connections, then press Ctrl+C to stop following the logs. The database container keeps running.

### Option B: XAMPP MySQL

Start MySQL in the XAMPP Control Panel. Make sure its port is 3306 and use the XAMPP root password in the backend setup below. The MySQL user must be allowed to create the `ecommerce_k6` schema.

Do not start both options on port 3306 at the same time. The test creates signup accounts in `ecommerce_k6`; do not use a production database.

## 2. Start SpringZ with the test schema

In a PowerShell window, from the project root:

```powershell
cd backend
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/ecommerce_k6?createDatabaseIfNotExist=true&useSSl=false&serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME = 'root'
$env:SPRING_DATASOURCE_PASSWORD = 'k6localpass'
.\mvnw.cmd spring-boot:run
```

For XAMPP with its default empty root password, set `$env:SPRING_DATASOURCE_PASSWORD = ''` instead. If you set a password in XAMPP, use that value. Keep this window open and wait for Spring Boot to report it has started on port 8081.

## 3. Verify the API

In another PowerShell window, run:

```powershell
Invoke-RestMethod http://127.0.0.1:8081/
Invoke-RestMethod http://127.0.0.1:8081/actuator/health
```

Both requests should return a response before you start the load test.

## 4. Run k6 using Docker Compose

The root `compose.yaml` uses the already-pulled `grafana/k6:latest` image, mounts the script read-only, and sets the target to `host.docker.internal:8081`.

From the project root:

```powershell
Remove-Item Env:BASE_URL -ErrorAction SilentlyContinue
docker compose run --rm k6
```

The first command clears any PowerShell override so Compose uses its default host address. `--rm` removes the temporary k6 runner container when the test ends. It does not remove the MySQL database container or image.

## 5. Run k6 without Docker

Install k6 once using Chocolatey:

```powershell
choco install k6
```

With SpringZ still running, open a PowerShell window at the project root and run:

```powershell
k6 run .\performance\k6-load-test.js
```

The script defaults to `http://localhost:8081`, so no `BASE_URL` override is needed for a local k6 installation.

## Recorded run

The successful user-reported Docker Compose run met both thresholds:

| Metric                    |                 Result |       Threshold |
| ------------------------- | ---------------------: | --------------: |
| p95 HTTP response time    |              150.33 ms |    under 500 ms |
| HTTP request failure rate |     0.00% (0 of 1,182) |        under 1% |
| Checks                    | 1,970 passed, 0 failed | all checks pass |
| Maximum virtual users     |                     20 |              20 |

Save the full console output with the project evidence. These figures describe that run; record new results separately for future runs.

## Cleanup

Press Ctrl+C in the SpringZ window. If you used the Docker MySQL option, stop its temporary container:

```powershell
docker stop springz-k6-db
```

The `--rm` option removes the database container and its data when stopped. If you used XAMPP, remove the test-only `ecommerce_k6` schema in phpMyAdmin when you no longer need it.
