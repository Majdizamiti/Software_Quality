# Rapport de Conformité et de Qualité Logicielles (ISO/IEC 25010)
**Projet :** SpringZ - E-Commerce Backend & React Frontend  
**Périmètre :** Application Métier Fullstack & Stratégie de Test Multi-couches  

---

## 1. Présentation du Projet et Choix de la Stack Technique

L'application **SpringZ** est un système de gestion commerciale (E-Commerce) permettant la gestion centralisée des utilisateurs, catégories, sous-catégories, produits, clients, fournisseurs et commandes.

### Stack Technique Choisie
* **Backend :** Java 17 + Spring Boot 3.5.6 (API REST JSON, Spring Security, JWT).
* **Frontend :** React 18 + Vite + React Router (SPA - Single Page Application).
* **Base de Données :** MySQL 8.x (Environnement de production / local) & Base H2 In-Memory (Environnement de test).

### Justification de la Stack
* **Spring Boot :** Offre un cadre éprouvé pour la création d'API REST hautement sécurisées avec Spring Security et JWT.
* **React + Vite :** Permet une expérience utilisateur dynamique, fluide et réactive.
* **Base H2 In-Memory :** Permet d'exécuter l'intégralité de la suite de tests automatisés de manière 100% autonome et reproductible sans dépendre d'une base de données externe.

---

## 2. Conformité aux Exigences Fonctionnelles Minimales

1. **Authentification & Gestion des Droits :** Authentification stateless basée sur des jetons JWT (`/api/auth/signup` et `/api/auth/signin`). Sécurisation de l'accès aux API par rôle et identité.
2. **Entités Métier Interliées :** 7 entités principales (`User`, `Category`, `Subcategory`, `Product`, `Customer`, `Provider`, `Order`) reliées par des relations JPA (`1-N` et `N-N`).
3. **Tableau de Bord :** Interface d'administration React (`DashboardPage.jsx`) affichant des indicateurs et cartes d'accès rapide.
4. **Règles Métier Explicites et Testées :**
   * Unicité stricte de l'adresse email lors de l'inscription (`400 Bad Request` en cas de doublon).
   * Robustesse de la clé de signature JWT ($\ge 64$ octets / 512 bits).
   * Hachage obligatoire des mots de passe avec `PasswordEncoder` (BCrypt).
   * Protection stricte des endpoints d'API métier (rejet HTTP `401 Unauthorized` si le jeton est absent ou invalide).

---

## 3. Matrice de Traçabilité & Conformité ISO/IEC 25010

Le tableau ci-dessous établit la correspondance directe entre les exigences de la norme ISO/IEC 25010, les types de tests réellement exécutés, les outils utilisés et les preuves de résultats obtenus.

| Caractéristique ISO 25010 | Exigence Qualité du Projet | Couche / Type de Test | Outil Utilisé | Preuve d'Exécution Réelle & Statut |
| :--- | :--- | :--- | :--- | :--- |
| **3.1 Aptitude Fonctionnelle** | Validation isolée de la logique métier des services. | Tests Unitaires | JUnit 5 + Mockito | **25 Tests exécutés et réussis** (`BUILD SUCCESS`). |
| **3.2 Aptitude Fonctionnelle** | Validation des contrats d'interface API et contrôleurs REST. | Tests d'Intégration API | Spring Boot Test + MockMvc + H2 | **6 Tests exécutés et réussis** (`BUILD SUCCESS`). |
| **3.3 Aptitude Fonctionnelle & Ergonomie** | Validation des parcours utilisateur réels dans l'interface React. | Tests Fonctionnels E2E | Playwright | **5 Scénarios E2E exécutés** sur Chromium ([playwright.config.js](file:///C:/ing5/test/hackathon_Africana/frontend/playwright.config.js)). |
| **3.4 Sécurité (SAST)** | Audit statique du code source Java (détection de failles OWASP Top 10). | Analyse Statique SAST | SonarQube & SpotBugs (FindSecBugs) | **Rapport SonarQube (`ANALYSIS SUCCESSFUL`)** sur `http://localhost:9000/dashboard?id=SpringZ` + SpotBugs (`0 Error`). |
| **3.5 Sécurité (SCA)** | Détection des vulnérabilités connues (CVE) dans les dépendances JAR. | Composition Analysis SCA | OWASP Dependency-Check | Plugin `dependency-check-maven` configuré et exécuté. |
| **3.6 Sécurité (DAST)** | Détection dynamique des failles d'injection et d'en-têtes HTTP sur le serveur actif. | Analyse Dynamique DAST | OWASP ZAP | Collection Postman analysée sur `http://localhost:8081`. |
| **3.7 Maintenabilité** | Mesure du taux de couverture de code par les tests. | Couverture de Code | JaCoCo | **Rapport JaCoCo généré (`jacoco.exec`)** couvrant 45 classes Java. |
| **3.8 Fiabilité** | Stabilité et isolation des données lors des échecs ou tests d'API. | Tests de Fiabilité | Spring Test + `@Transactional` | **Réinitialisation automatique des transactions H2** après chaque test. |

---

## 4. Détail des Couches de Tests Exécutées et Résultats

### 🧪 Couche 1 : Tests Unitaires (25 Tests)
* **Classes Testées :** `CategoryServiceImplTest` (5), `ProductServiceImplTest` (5), `CustomerServiceImplTest` (5), `UserServiceImplTest` (5), `AuthControllerUnitTest` (3), `JwtUtilsTest` (2).
* **Résultat Réel :**
  ```text
  [INFO] Results:
  [INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```

### 🌐 Couche 2 : Tests d'Intégration (6 Tests)
* **Classes Testées :** `AuthControllerIntegrationTest` (4), `CategoryApiControllerIntegrationTest` (2).
* **Résultat Réel :**
  ```text
  [INFO] Results:
  [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```

### 🖥️ Couche 3 : Tests End-to-End Playwright (5 Tests)
* **Fichiers de Test :** [auth.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/auth.spec.js), [dashboard.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/dashboard.spec.js).
* **Résultat Réel :** Tests exécutés sur Chromium via `npm --prefix frontend run test:e2e`.

### 🛡️ Couche 4 : Analyse SAST SonarQube & SpotBugs
* **Rapport SonarQube :**
  ```text
  [INFO] ANALYSIS SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=SpringZ
  [INFO] BUILD SUCCESS
  ```

### 📦 Couche 5 : Couverture de Code JaCoCo
* **Rapport JaCoCo :** Fichier `target/site/jacoco/jacoco.xml` généré (45 classes analysées).

---

## 5. Transparence sur les Caractéristiques Non Réalisées

Conformément à la règle d'or du projet (*"Aucun résultat de test ne doit être affirmé sans preuve d'exécution réelle"*), les catégories suivantes n'ont **pas été exécutées** dans le périmètre actuel et sont documentées comme telles :

* **Efficacité de Performance (Tests de charge sous JMeter/k6) :** Non réalisés.
* **Compatibilité Multi-navigateurs Cloud (BrowserStack) :** Non réalisés (seuls les tests E2E locaux sur Chromium ont été exécutés).
* **Conteneurisation Docker complète :** Seul le serveur SonarQube a été conteneurisé via Docker.
