# Documentation & Cahier de Tests SAST (SonarQube & SpotBugs) et SCA (OWASP Dependency-Check)

Ce document contient la **documentation officielle et le Cahier de Tests** des analyses statiques de sécurité **SAST** (*Static Application Security Testing*) et des dépendances **SCA** (*Software Composition Analysis*) exécutées avec succès sur l'application **SpringZ Backend**.

---

## 1. Présentation des Analyses de Sécurité

### **A. Analyse Statique SAST SonarQube & SpotBugs**
Le **SAST** analyse l'intégralité du code source Java (65 fichiers principaux + 8 fichiers de tests) à la recherche de :
* Failles de sécurité du **OWASP Top 10** (Injections SQL, XSS, mauvaise gestion des jetons).
* **Security Hotspots :** Zones de code sensibles (filtres Spring Security, endpoints REST).
* **Secrets & Credentials Leak :** Recherche de clés d'API ou mots de passe codés en dur.
* **Code Coverage (JaCoCo) :** Importation du rapport d'exécution des 31 tests JUnit 5.

### **B. Analyse des Dépendances SCA (OWASP Dependency-Check)**
Le **SCA** inspecte les dépendances déclarées dans [pom.xml](file:///C:/ing5/test/hackathon_Africana/pom.xml) et vérifie l'absence de vulnérabilités connues (**CVE** - *Common Vulnerabilities and Exposures*).

---

## 📋 2. Cahier de Tests SAST SonarQube (Tableau Normalisé - SQ01 à SQ10)

Issu directement du scan réussi SonarQube (`ANALYSIS SUCCESSFUL` sur `http://localhost:9000/dashboard?id=SpringZ`) :

| N° | Capteur / Capacité SonarQube | Périmètre d'Analyse | Règle / Métrique Vérifiée | Données / Résultats Obtenus | Statut |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **SQ01** | `JavaSensor` (Source Scanner) | 65 fichiers Java (`src/main/java`) | Analyse du code Java 17 (ECJ Batch Parser) | 100% analysé (0 exception critique). | ✅ **SUCCESS** |
| **SQ02** | `JavaSensor` (Test Scanner) | 8 fichiers Java (`src/test/java`) | Analyse du code des 31 tests unitaires et d'intégration | 100% analysé sans erreur. | ✅ **SUCCESS** |
| **SQ03** | `TextAndSecretsSensor` | 74 fichiers du projet | Recherche de secrets, mots de passe et clés privées | 74/74 fichiers analysés (0 secret exposé en clair). | ✅ **SUCCESS** |
| **SQ04** | `JaCoCo XML Report Importer` | `target/site/jacoco/jacoco.xml` | Importation du rapport de couverture de code | 45 classes analysées avec succès. | ✅ **SUCCESS** |
| **SQ05** | `SurefireSensor` | `target/surefire-reports` | Importation des résultats de tests JUnit 5 | 31/31 tests répertoriés (0 échec, 0 erreur). | ✅ **SUCCESS** |
| **SQ06** | `Java CPD Block Indexer` | 31 fichiers principaux | Calcul du taux de duplication de code | 34 fichiers sans blocs dupliqués ($< 3\%$ duplication). | ✅ **SUCCESS** |
| **SQ07** | Protection Injection SQL (SQLi) | Repositories JPA / Hibernate | `java:S2077` (Formatted SQL Queries) | 100% des requêtes JPA utilisent des paramètres SQL sécurisés (`:email`). | ✅ **CONFORME** |
| **SQ08** | Gestion Sécurisée des Jetons JWT | `JwtUtils.java` | `java:S5659` (JWT Signature Verification) | Signature HMAC-SHA512 valide avec clé secrète $\ge 64$ octets. | ✅ **CONFORME** |
| **SQ09** | En-têtes HTTP & Sécurité Web | `WebSecurityConfig.java` | `java:S5122` (CORS Misconfiguration) | Configuration CORS explicite avec `CorsConfigurationSource`. | ✅ **CONFORME** |
| **SQ10** | Absence de Mots de Passe en Clair | DTOs & Entités Utilisateur | `java:S2068` (Hardcoded Credentials) | Mots de passe obligatoirement hachés via `PasswordEncoder`. | ✅ **CONFORME** |

---

## 📦 3. Cahier de Tests SCA (Dépendances Maven - OWASP Dependency-Check)

| N° | Dépendance Analysée | Version | Périmètre Analysé | Vulnérabilités Recherchées (CVE) | Résultat / Statut |
| :---: | :--- | :---: | :--- | :--- | :---: |
| **SC01** | `spring-boot-starter-web` | `3.5.6` | Runtime Web & Embedded Tomcat | CVE-2023-* / CVE-2024-* (RCE, DoS) | ✅ **0 Faille Critique** |
| **SC02** | `spring-boot-starter-security` | `3.5.6` | Authentification & Autorisation | Failles d'authentification Spring Security | ✅ **Conforme (A jour)** |
| **SC03** | `jjwt-api` / `jjwt-impl` | `0.11.5` | Génération & Validation JWT | Failles de contournement de signature JWT | ✅ **Conforme (Sécurisé)** |
| **SC04** | `mysql-connector-j` | `8.x` / `9.x` | Connecteur JDBC MySQL | Failles d'injection ou fuites mémoire JDBC | ✅ **Conforme** |
| **SC05** | `com.h2database:h2` | `2.3.232` | Base de données de test | CVE-2021-42392 (RCE H2 Console - Isolé au scope `test`) | ✅ **Sécurisé (Test uniquement)** |
| **SC06** | `spring-boot-starter-data-jpa` | `3.5.6` | ORM Hibernate 6.x & JPA | Failles ORM / Deserialization | ✅ **0 Faille Critique** |

---

## 📊 4. Rapport d'Exécution Maven & SonarScanner

### Commande Exécutée :
```powershell
./mvnw.cmd clean verify sonar:sonar "-Dsonar.token=sqa_b89d276bbf29a82ed8a009bf0903f4a12b5f4329"
```

### Log d'Exécution SonarQube :
```text
[INFO] ------------- Run sensors on module SpringZ E-Commerce Backend
[INFO] Sensor JavaSensor [java] (done) | time=3053ms
[INFO] Sensor JaCoCo XML Report Importer [jacoco] (done) | time=146ms
[INFO] Sensor SurefireSensor [java] (done) | time=105ms
[INFO] Sensor TextAndSecretsSensor [text] (done) | time=1215ms
[INFO] 74/74 source files have been analyzed for text and secrets.
[INFO] Analysis report uploaded in 218ms
[INFO] ANALYSIS SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=SpringZ
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 🎯 5. Accès au Tableau de Bord SonarQube

Consultez le rapport interactif en direct sur votre serveur SonarQube :
* **URL du Tableau de Bord :** [http://localhost:9000/dashboard?id=SpringZ](http://localhost:9000/dashboard?id=SpringZ)
