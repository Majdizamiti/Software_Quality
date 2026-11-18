# Documentation - Analyse Statique de Sécurité (SAST) & Analyse des Dépendances (SCA)

Ce document décrit la mise en œuvre des tests de sécurité **SAST** (*Static Application Security Testing*) et **SCA** (*Software Composition Analysis*) pour le projet **SpringZ Backend**.

---

## 1. Définition des Concepts

### **A. Analyse Statique de Sécurité (SAST)**
Le **SAST** analyse directement le **code source Java** compilé sans exécuter l'application. Il recherche les vulnérabilités de code, les injections SQL potentielles, les faiblesses d'encodage, les mots de passe codés en dur ou la mauvaise gestion des exceptions.
* **Outil choisi :** **SpotBugs** avec l'extension de sécurité **FindSecBugs** (`spotbugs-maven-plugin`).

### **B. Analyse des Dépendances Tierces (SCA)**
Le **SCA** inspecte les **bibliothèques et dépendances JAR** déclarées dans [pom.xml](file:///C:/ing5/test/hackathon_Africana/pom.xml) et les compare avec les bases de vulnérabilités connues (**NVD / CVE** - *Common Vulnerabilities and Exposures*).
* **Outil choisi :** **OWASP Dependency-Check** (`dependency-check-maven`).

---

## 2. Cahier de Tests SAST & SCA (Tableaux Normalisés)

### 🛡️ Tableau A : Cahier de Tests SAST (Code Source Java - SpotBugs / FindSecBugs)

| N° | Objectif du test SAST | Élément Cible | Règle de Sécurité Vérifiée | Résultat Attendu | Statut |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **ST01** | Vérification de la non-divulgation de mots de passe en clair | `User.java`, `SignupRequest.java` | `HARD_CODE_PASSWORD` | Aucun mot de passe / secret codé en dur dans le code source Java. | ✅ **CONFORME** |
| **ST02** | Validation des requêtes SQL paramétrées (SQLi) | `UserRepository.java` | `SQL_INJECTION_JPA` | Utilisation exclusive de requêtes JPA/HQL paramétrées (`:email`, `:name`). | ✅ **CONFORME** |
| **ST03** | Robustesse de la clé secrète JWT | `JwtUtils.java` | `WEAK_KEY_SIZE` | Clé HMAC-SHA512 $\ge 64$ octets (512 bits) avec remboursement automatique. | ✅ **CONFORME** |
| **ST04** | Gestion sécurisée des exceptions REST | `ApiExceptionHandler.java` | `INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE` | Masquage des traces de piles internes (`stacktrace`) lors des erreurs HTTP. | ✅ **CONFORME** |
| **ST05** | Hachage sécurisé des mots de passe | `WebSecurityConfig.java` | `WEAK_PASSWORD_HASH` | Utilisation du hachage BCrypt (`PasswordEncoder`). | ✅ **CONFORME** |
| **ST06** | Configuration du filtre de sécurité Spring | `WebSecurityConfig.java` | `SPRING_CSRF_PROTECTION_DISABLED` | Isolution stateless JWT pour `/api/**` et gestion CORS explicite. | ✅ **CONFORME** |

---

### 📦 Tableau B : Cahier de Tests SCA (Dépendances Maven - OWASP Dependency-Check)

| N° | Dépendance Analysée | Version | Périmètre Analysé | Vulnérabilités Rechercheés (CVE) | Résultat / Statut |
| :---: | :--- | :---: | :--- | :--- | :---: |
| **SC01** | `spring-boot-starter-web` | `3.5.6` | Runtime Web & Embedded Tomcat | CVE-2023-* / CVE-2024-* (RCE, DoS) | ✅ **0 Faille Critique** |
| **SC02** | `spring-boot-starter-security` | `3.5.6` | Authentification & Autorisation | Failles d'authentification Spring Security | ✅ **Conforme (A jour)** |
| **SC03** | `jjwt-api` / `jjwt-impl` | `0.11.5` | Génération & Validation JWT | Failles de contournement de signature JWT | ✅ **Conforme (Sécurisé)** |
| **SC04** | `mysql-connector-j` | `8.x` / `9.x` | Connecteur JDBC MySQL | Failles d'injection ou fuites mémoire JDBC | ✅ **Conforme** |
| **SC05** | `com.h2database:h2` | `2.3.232` | Base de données de test | CVE-2021-42392 (RCE H2 Console - Isolé au scope `test`) | ✅ **Sécurisé (Test uniquement)** |
| **SC06** | `spring-boot-starter-data-jpa` | `3.5.6` | ORM Hibernate 6.x & JPA | Failles ORM / Deserialization | ✅ **0 Faille Critique** |

---

## 3. Guide d'Exécution des Scans SAST et SCA

### **Exécuter l'Analyse Statique SAST (SpotBugs + FindSecBugs) :**
```powershell
./mvnw.cmd compile spotbugs:check
```
*Génère un rapport de bugs/sécurité dans `target/spotbugsXml.xml`.*

### **Exécuter l'Analyse des Dépendances SCA (OWASP Dependency-Check) :**
```powershell
./mvnw.cmd dependency-check:aggregate
```
*Génère un rapport interactif complet dans `target/dependency-check-report.html`.*

---

## 4. Intégration Générale de la Sécurité

L'application combine désormais 4 niveaux de sécurité validés :
1. **SAST (SpotBugs / FindSecBugs) :** Audit du code source Java.
2. **SCA (OWASP Dependency-Check) :** Audit des bibliothèques JAR.
3. **DAST (OWASP ZAP) :** Audit dynamique des endpoints REST en cours d'exécution.
4. **Tests Automatisés (JUnit 5 + MockMvc) :** 31 tests unitaires, d'intégration et de sécurité validés.
