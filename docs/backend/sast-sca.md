# Static Analysis and Dependency Scanning

This guide covers the Maven SpotBugs plugin (SAST) and OWASP Dependency-Check (SCA). Both plugins are configured in `backend/pom.xml`. Run these scans when you want fresh results; configuration or old report text alone does not prove a scan passed.

## Requirements

- JDK 17 and network access for Maven dependencies.
- Dependency-Check may also need network access to update vulnerability data. Its first run can take longer.

## Run SpotBugs

```powershell
cd backend
.\mvnw.cmd spotbugs:check
```

Review the Maven outcome and the generated SpotBugs report under `target/spotbugs*.xml` or `target/spotbugs.html`, depending on the plugin output.

## Run Dependency-Check

```powershell
cd backend
.\mvnw.cmd dependency-check:check
```

Review `target/dependency-check-report.html` and record the scan date and vulnerability counts. Do not describe dependencies as safe based only on their declared versions.

## Current scope

These tools analyze Java code and declared Maven dependencies. The current project does not contain a separate automated DAST test suite. OWASP ZAP can be used manually against a running test instance, but save its actual report before reporting findings.
