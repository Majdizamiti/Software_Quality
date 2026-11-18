# Documentation Complète - Analyse Statique de Sécurité (SAST) avec SonarQube

Ce document explique la mise en œuvre et l'exécution des tests d'analyse statique de sécurité **SAST (Static Application Security Testing)** avec **SonarQube** et **JaCoCo** pour le projet **SpringZ Backend**.

---

## 1. Qu'est-ce que SonarQube ?

**SonarQube** est la plateforme industrielle de référence pour :
* **La Sécurité du Code (SAST) :** Détection automatique des failles du **OWASP Top 10** (Injections SQL, XSS, faiblesses cryptographiques, mauvaises configurations de sécurité).
* **Les Security Hotspots :** Identification des blocs de code critiques nécessitant une revue de sécurité (ex: configurations Spring Security, filtres JWT).
* **La Couverture de Code (Code Coverage) :** Calcul du pourcentage de code testé par nos 31 tests JUnit 5 grâce au plugin **JaCoCo**.
* **Les Bugs & Code Smells :** Détection de la dette technique et des mauvaises pratiques de développement Java.

---

## 2. Configuration du Projet ([pom.xml](file:///C:/ing5/test/hackathon_Africana/pom.xml))

Les plugins suivants ont été intégrés dans le fichier `pom.xml` :

### **A. Plugin de Couverture de Code (JaCoCo)**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### **B. Plugin SonarQube Scanner**
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>5.0.0.4389</version>
</plugin>
```

---

## 3. Guide d'Exécution de l'Analyse SonarQube

### **Option A : Avec un Serveur SonarQube Local (Docker - Recommandé)**

1. **Démarrer SonarQube via Docker :**
   ```powershell
   docker run -d --name sonarqube -p 9000:9000 sonarqube:community
   ```
2. **Accéder à l'interface Web :**
   Ouvrez votre navigateur sur **`http://localhost:9000`** (Identifiants par défaut : `admin` / `admin`).
3. **Lancer le Scan SonarQube avec Maven :**
   ```powershell
   ./mvnw.cmd clean verify sonar:sonar
   ```

---

### **Option B : Avec SonarCloud (SaaS / Dépôt GitHub)**

Si vous utilisez SonarCloud pour votre dépôt GitHub (`https://github.com/Majdizamiti/Software_Quality`) :

```powershell
./mvnw.cmd clean verify sonar:sonar `
  -Dsonar.host.url=https://sonarcloud.io `
  -Dsonar.organization=VOTRE_ORGANISATION_SONAR `
  -Dsonar.projectKey=SpringZ `
  -Dsonar.token=VOTRE_TOKEN_SONARCLOUD
```

---

## 4. Métriques Mesurées par SonarQube

| Métrique | Description | Objectif |
| :--- | :--- | :--- |
| **Vulnerabilities** | Failles de sécurité directement exploitables (ex: SQLi, RCE). | 0 Faille (A) |
| **Security Hotspots** | Zones de code sensibles devant être auditées manuellement. | 100% Vérifiés |
| **Code Coverage** | Taux de couverture de code calculé par JaCoCo lors des 31 tests JUnit. | $> 80\%$ |
| **Code Smells** | Mauvaises pratiques de développement et dette technique. | Minimisés (Rating A) |
| **Duplications** | Pourcentage de code dupliqué dans le projet. | $< 3\%$ |
