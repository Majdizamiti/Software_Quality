# SonarQube and Code Coverage

The Maven project includes the SonarQube scanner and JaCoCo coverage plugins. JaCoCo runs during the Maven test lifecycle and writes its report under `backend/target/site/jacoco/`.

## Run tests and generate coverage

```powershell
cd backend
java -version
.\mvnw.cmd clean verify
```

Open `target/site/jacoco/index.html` to review coverage. This local coverage report does not require a SonarQube server.

## Run a SonarQube analysis

### 1. Start a local SonarQube server

Make sure Docker Desktop is running, then start the SonarQube container:

```powershell
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
```

Wait ~1–2 minutes for the service to initialize, then navigate to `http://localhost:9000` in your browser.

#### Default Admin Credentials

- **Username:** `admin`
- **Password:** `admin`

_(On first login, SonarQube will prompt you to replace the default password with a new one)._

#### Creating the Project Manually in SonarQube

To prevent authorization errors (`You're not authorized to analyze this project or the project doesn't exist on SonarQube...`):

1. Navigate to `http://localhost:9000` and log in as `admin`.
2. Click **Projects** (top bar) &rarr; **Create Project** &rarr; select **Manually**.
3. Fill in the project parameters:
   - **Project display name:** `SpringZ E-Commerce Backend`
   - **Project key:** `SpringZ` _(⚠️ Case-sensitive: must match `<sonar.projectKey>` in `backend/pom.xml`)_
   - **Main branch name:** `master` (or `main`)
4. Click **Set Up**.
5. Under _"How do you want to analyze your repository?"_, choose **Locally** (or _With Maven_).

#### Generating an Analysis Token

5. When setting up the project (or via **Avatar (top-right) &rarr; My Account &rarr; Security**):
6. Under **Generate Token**, enter a token name (e.g. `SpringZ-token`), select **User Token** (or _Project Analysis Token_), and click **Generate**.
7. Copy the generated token (starts with `squ_`).

### 2. Execute Maven scan with token

From the `backend` directory, run:

```powershell
cd backend
.\mvnw.cmd clean verify sonar:sonar "-Dsonar.token=<YOUR_SONAR_TOKEN>"
```

If the server requires a token, pass it securely as a Maven property or configure it in your local Maven settings. Do not commit tokens to the repository or paste them into reports.

Check the Maven output for successful upload and review the project on the SonarQube server. A server must be running and reachable; otherwise run `clean verify` to get tests and JaCoCo without SonarQube.

## Reports

- JaCoCo: `target/site/jacoco/index.html` when running Maven from `backend`
- SonarQube: the project dashboard on the server configured for your environment

Record the actual date and findings after the scan. Old dashboard links and sample metrics are not evidence of a current analysis.
