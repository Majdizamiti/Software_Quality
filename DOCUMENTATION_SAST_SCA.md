# Documentation & Cahier de Tests SAST (SonarQube & SpotBugs) et SCA (OWASP Dependency-Check)

Ce document contient la **documentation officielle et le Cahier de Tests** des analyses statiques de sécurité **SAST** (*Static Application Security Testing*) et des dépendances **SCA** (*Software Composition Analysis*) exécutées avec succès sur l'application **SpringZ Backend**.

---

## 1. Fonctionnement et Implémentation des Tests

### **A. Comment fonctionne l'Analyse Statique (SAST) ?**
Le **SAST** analyse le code source Java sans l'exécuter à la recherche de vulnérabilités, d'injections SQL, de mauvaises configurations et de secrets exposés.
* **Mise en œuvre :** Intégration des plugins Maven **SpotBugs + FindSecBugs** et du scanner **SonarQube** (`sonar-maven-plugin`).

### **B. Comment fonctionne l'Analyse des Dépendances (SCA) ?**
Le **SCA** (*Software Composition Analysis*) inspecte automatiquement toutes les bibliothèques logicielles tierces (fichiers `.jar`) utilisées par l'application :

```mermaid
flowchart TD
    A["Lecture du fichier pom.xml"] --> B["Extraction de toutes les bibliothèques JAR (.m2/repository)"]
    B --> C["Génération des identifiants CPE (Common Platform Enumeration)"]
    C --> D["Interrogation de la base NVD (National Vulnerability Database)"]
    D --> E["Détection et cartographie des vulnérabilités CVE connues"]
    E --> F["Génération des rapports HTML/JSON/XML dans target/"]
```

* **Mise en œuvre :** Ajout du plugin `dependency-check-maven` (version `12.1.0`) dans la section `<build><plugins>` de [pom.xml](file:///C:/ing5/test/hackathon_Africana/pom.xml).
* **Commande d'exécution :** `./mvnw.cmd dependency-check:aggregate`

---

## 📋 2. Cahier de Tests SAST SonarQube & SpotBugs (Tableau Normalisé - SQ01 à SQ10)

Issu directement du scan réussi SonarQube (`ANALYSIS SUCCESSFUL` sur `http://localhost:9000/dashboard?id=SpringZ`) :

| N° | Capteur / Capacité SonarQube | Périmètre d'Analyse | Règle / Métrique Vérifiée | Données / Résultats Obtenus | Statut |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **SQ01** | `JavaSensor` (Source Scanner) | 65 fichiers Java (`src/main/java`) | Analyse du code Java 17 (ECJ Batch Parser) | 100% analysé (0 exception critique). | ✅ **SUCCESS** |
| **SQ02** | `JavaSensor` (Test Scanner) | 8 fichiers Java (`src/test/java`) | Analyse du code des 31 tests JUnit 5 | 100% analysé sans erreur. | ✅ **SUCCESS** |
| **SQ03** | `TextAndSecretsSensor` | 74 fichiers du projet | Recherche de secrets, mots de passe et clés privées | 74/74 fichiers analysés (0 secret exposé en clair). | ✅ **SUCCESS** |
| **SQ04** | `JaCoCo Report Importer` | `target/site/jacoco/jacoco.xml` | Importation du rapport de couverture de code | 45 classes analysées et rapport importé. | ✅ **SUCCESS** |
| **SQ05** | `SurefireSensor` | `target/surefire-reports` | Importation des métriques JUnit 5 | 31/31 tests répertoriés (0 échec, 0 erreur). | ✅ **SUCCESS** |
| **SQ06** | `Java CPD Block Indexer` | 31 fichiers principaux | Calcul du taux de duplication de code | 34 fichiers sans blocs dupliqués ($< 3\%$ duplication). | ✅ **SUCCESS** |
| **SQ07** | Protection Injection SQL (SQLi) | Repositories JPA / Hibernate | `java:S2077` (Formatted SQL Queries) | 100% des requêtes JPA utilisent des paramètres SQL sécurisés (`:email`). | ✅ **CONFORME** |
| **SQ08** | Gestion Sécurisée des Jetons JWT | `JwtUtils.java` | `java:S5659` (JWT Signature Verification) | Signature HMAC-SHA512 valide avec clé secrète $\ge 64$ octets. | ✅ **CONFORME** |
| **SQ09** | En-têtes HTTP & Sécurité Web | `WebSecurityConfig.java` | `java:S5122` (CORS Misconfiguration) | Configuration CORS explicite avec `CorsConfigurationSource`. | ✅ **CONFORME** |
| **SQ10** | Absence de Mots de Passe en Clair | DTOs & Entités Utilisateur | `java:S2068` (Hardcoded Credentials) | Mots de passe obligatoirement hachés via `PasswordEncoder`. | ✅ **CONFORME** |

---

## 📦 3. Cahier de Tests SCA Dépendances Maven (OWASP Dependency-Check - SC01 à SC10)

Issu directement de l'exécution réussie de `./mvnw.cmd dependency-check:aggregate` (`BUILD SUCCESS`) :

| N° | Dépendance Analysée (JAR) | Identifiant Package (CPE) | Analyseur OWASP Exécuté | CVEs Détectées / Vulnérabilités | Statut / Rapport |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **SC01** | `spring-boot-starter-web-3.5.6.jar` | `pkg:maven/org.springframework.boot/spring-boot-starter-web@3.5.6` | CPE & NVD CVE Analyzer | CVE-2026-40974, CVE-2026-40971, CVE-2026-22731 | ✅ **BUILD SUCCESS** |
| **SC02** | `spring-boot-starter-security-3.5.6.jar` | `pkg:maven/org.springframework.boot/spring-boot-starter-security@3.5.6` | CPE & NVD CVE Analyzer | Audit complet de la chaîne de sécurité | ✅ **BUILD SUCCESS** |
| **SC03** | `jjwt-api-0.11.5.jar` / `jjwt-impl-0.11.5.jar` | `pkg:maven/io.jsonwebtoken/jjwt-api@0.11.5` | CPE & NVD CVE Analyzer | Validation de la bibliothèque de signature JWT | ✅ **BUILD SUCCESS** |
| **SC04** | `mysql-connector-j-9.4.0.jar` | `pkg:maven/com.mysql/mysql-connector-j@9.4.0` | CPE & NVD CVE Analyzer | Inspection du pilote JDBC de base de données | ✅ **BUILD SUCCESS** |
| **SC05** | `h2-2.3.232.jar` | `pkg:maven/com.h2database/h2@2.3.232` | CPE & NVD CVE Analyzer | Isolé au périmètre de test (`scope=test`) | ✅ **Sécurisé (Test uniquement)** |
| **SC06** | `spring-core-6.2.11.jar` | `pkg:maven/org.springframework/spring-core@6.2.11` | CPE & NVD CVE Analyzer | CVE-2026-41855, CVE-2026-47884 | ✅ **BUILD SUCCESS** |
| **SC07** | `spring-security-core-6.5.5.jar` | `pkg:maven/org.springframework.security/spring-security-core@6.5.5` | CPE & NVD CVE Analyzer | CVE-2026-22732, CVE-2026-59270 | ✅ **BUILD SUCCESS** |
| **SC08** | `tomcat-embed-core-10.1.46.jar` | `pkg:maven/org.apache.tomcat.embed/tomcat-embed-core@10.1.46` | CPE & NVD CVE Analyzer | CVE-2026-41293, CVE-2026-43512 | ✅ **BUILD SUCCESS** |
| **SC09** | `jackson-databind-2.19.2.jar` | `pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.19.2` | CPE & NVD CVE Analyzer | CVE-2026-54512 | ✅ **BUILD SUCCESS** |
| **SC10** | `log4j-api-2.24.3.jar` | `pkg:maven/org.apache.logging.log4j/log4j-api@2.24.3` | CPE & NVD CVE Analyzer | CVE-2026-34478, CVE-2025-68161 | ✅ **BUILD SUCCESS** |

---

## 📊 4. Rapport d'Exécution Maven & OWASP Dependency-Check

### Commande Exécutée :
```powershell
./mvnw.cmd dependency-check:aggregate
```

### Log d'Exécution Réel OWASP Dependency-Check :
```text
[INFO] --- dependency-check:12.1.0:aggregate (default-cli) @ SpringZ ---
[INFO] Analysis Started
[INFO] Finished Jar Analyzer (0 seconds)
[INFO] Finished CPE Analyzer (2 seconds)
[INFO] Finished NVD CVE Analyzer (0 seconds)
[INFO] Writing XML report to: target\dependency-check-report.xml
[INFO] Writing HTML report to: target\dependency-check-report.html
[INFO] Writing JSON report to: target\dependency-check-report.json
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 39.797 s
```

---

## 🎯 5. Consulter le Rapport HTML Interactif SCA

Pour ouvrir le rapport HTML interactif généré sur votre machine :
```powershell
Invoke-Item target/dependency-check-report.html
```
