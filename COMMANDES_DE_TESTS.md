# Guide des Commandes de Test - Copier / Coller (Cheatsheet)

Ce document répertorie **toutes les commandes nécessaires** pour exécuter chaque couche de test du projet **SpringZ (Backend + Frontend)**. Vous pouvez les copier-coller directement dans votre terminal PowerShell.

---

## 🧪 1. Tests Unitaires (JUnit 5 + Mockito)

### Exécuter l'intégralité des 25 tests unitaires :
```powershell
./mvnw.cmd test -Dtest=*Test,*UnitTest
```

### Exécuter une classe de test unitaire spécifique :
```powershell
./mvnw.cmd test -Dtest=CategoryServiceImplTest
./mvnw.cmd test -Dtest=ProductServiceImplTest
./mvnw.cmd test -Dtest=CustomerServiceImplTest
./mvnw.cmd test -Dtest=UserServiceImplTest
./mvnw.cmd test -Dtest=AuthControllerUnitTest
./mvnw.cmd test -Dtest=JwtUtilsTest
```

---

## 🌐 2. Tests d'Intégration (Spring Boot Test + MockMvc + Base H2)

### Exécuter uniquement les tests d'intégration API :
```powershell
./mvnw.cmd test -Dtest=*IntegrationTest
```

### Exécuter une classe de test d'intégration spécifique :
```powershell
./mvnw.cmd test -Dtest=AuthControllerIntegrationTest
./mvnw.cmd test -Dtest=CategoryApiControllerIntegrationTest
```

---

## ⚡ 3. Exécution Globale des Tests Backend (31 Tests Unitaires & Intégration)

### Exécuter la suite complète des 31 tests Java :
```powershell
./mvnw.cmd test
```

---

## 🖥️ 4. Tests Fonctionnels / End-to-End (Playwright)

### Installer le navigateur Chromium pour Playwright (À faire une seule fois) :
```powershell
npx --prefix frontend playwright install chromium
```

### Exécuter tous les tests E2E Playwright (Mode Headless) :
```powershell
npm --prefix frontend run test:e2e
```

### Exécuter les tests E2E avec l'interface graphique interactive (UI Mode) :
```powershell
npx --prefix frontend playwright test --ui
```

### Afficher le rapport HTML complet des tests Playwright :
```powershell
npx --prefix frontend playwright show-report
```

---

## ⚡ 5. Tests de Performance & de Charge (Grafana k6)

### Étape 1 : Démarrer le serveur Backend Spring Boot (Terminal 1)
```powershell
./mvnw.cmd spring-boot:run
```

### Étape 2 : Lancer le script de charge k6 à 20 VUs (Terminal 2)
```powershell
k6 run performance/k6-load-test.js
```

---

## 🛡️ 6. Analyse Statique SAST & Analyse des Dépendances SCA

### Exécuter l'Analyse Statique SAST avec SpotBugs + FindSecBugs :
```powershell
./mvnw.cmd compile spotbugs:check
```

### Exécuter l'Analyse des Dépendances SCA avec OWASP Dependency-Check :
```powershell
./mvnw.cmd org.owasp:dependency-check-maven:aggregate "-DautoUpdate=false"
```

### Ouvrir le rapport HTML de l'analyse SCA :
```powershell
Invoke-Item target/dependency-check-report.html
```

---

## 📊 7. Analyse SAST SonarQube & Couverture de Code JaCoCo

### Étape 1 : Démarrer le conteneur Docker SonarQube (si éteint) :
```powershell
docker run -d --name sonarqube -p 9000:9000 sonarqube:community
```

### Étape 2 : Exécuter la compilation, la couverture JaCoCo et l'envoi du rapport SAST à SonarQube :
```powershell
./mvnw.cmd clean verify sonar:sonar "-Dsonar.token=squ_4a4654bae973c7b35a141925ce8557cea71a8d6b"
```

### Ouvrir le rapport interactif de Couverture de Code JaCoCo :
```powershell
Invoke-Item target/site/jacoco/index.html
```

---

## 🚀 8. Lancement Général du Projet Fullstack

### Démarrer le Backend (Spring Boot sur http://localhost:8081) :
```powershell
./mvnw.cmd spring-boot:run
```

### Démarrer le Frontend (React sur http://localhost:5173) :
```powershell
cd frontend
npm run dev
```
