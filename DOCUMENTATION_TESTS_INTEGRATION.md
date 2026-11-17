# Documentation Complète - Tests d'Intégration (Spring Boot Test + MockMvc)

Ce document est la **documentation officielle des Tests d'Intégration** du projet **SpringZ Backend**. Il détaille le fonctionnement, les outils, l'architecture et inclut le **Cahier de Tests d'Intégration** au format normalisé.

---

## 1. Présentation et Objectifs des Tests d'Intégration

Les **Tests d'Intégration** permettent de valider le fonctionnement global de l'application en faisant interagir toutes ses couches réelles :

1. **La couche Réseau & HTTP :** Traitement des requêtes REST (`POST`, `GET`, `PUT`, `DELETE`) et conversion des corps de requêtes JSON.
2. **La couche Sécurité :** Filtrage des requêtes par Spring Security et vérification de l'en-tête `Authorization: Bearer <token_jwt>`.
3. **La couche Métier :** Exécution des règles de gestion des contrôleurs et services.
4. **La couche Persistance :** Enregistrement et lecture en base de données relationnelle en mémoire **H2**.

---

## 2. Outils et Technologies Utilisés

* **`@SpringBootTest` :** Charge le contexte d'application Spring Boot complet pour simuler l'environnement de production.
* **`MockMvc` & `@AutoConfigureMockMvc` :** Permet d'envoyer de fausses requêtes HTTP et de vérifier la réponse sans avoir besoin de démarrer un vrai serveur Tomcat externe.
* **Base de données H2 (In-Memory) :** Base de données SQL éphémère configurée dans `src/test/resources/application.properties` (`jdbc:h2:mem:testdb`).
* **`@Transactional` :** Réinitialise automatiquement la base de données après chaque test (annulation des transactions) pour qu'aucun test ne pollue les données du suivant.

---

## 3. Architecture et Implémentation Étape par Étape

### Étape 1 : Configuration de la Base In-Memory ([application.properties](file:///C:/ing5/test/hackathon_Africana/src/test/resources/application.properties))
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

### Étape 2 : Test d'Intégration de l'Authentification (`AuthControllerIntegrationTest`)
Validation de la chaîne HTTP complète pour l'inscription (`/api/auth/signup`) et la connexion (`/api/auth/signin`), incluant la détection des doublons d'email et les erreurs de mots de passe.

### Étape 3 : Test d'Intégration des API Sécurisées JWT (`CategoryApiControllerIntegrationTest`)
Validation de la sécurité Spring :
1. Une requête sans jeton JWT renvoie immédiatement un statut **HTTP 401 Unauthorized**.
2. Une requête avec jeton JWT valide exécute le cycle CRUD complet (`POST` -> `GET` -> `PUT` -> `DELETE` -> `GET 404`).

---

## 4. Cahier de Tests d'Intégration (Tableau Normalisé)

| N° | Objectif du test | Données / État initial | Entrées / Paramètres | Résultat attendu | Méthode / Chemin |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **TI01** | Inscription HTTP utilisateur valide | Base H2 vide | `POST /api/auth/signup` + JSON user | HTTP `200 OK` ; JSON `$.message` = "User registered successfully" | `MockMvc -> POST /api/auth/signup` |
| **TI02** | Détection de doublon d'email HTTP | Email "duplicate@ex.com" inscrit | `POST /api/auth/signup` avec même email | HTTP `400 Bad Request` ; JSON `$.message` = "Email is already in use!" | `MockMvc -> POST /api/auth/signup` |
| **TI03** | Connexion HTTP & émission jeton JWT | User inscrit en base H2 | `POST /api/auth/signin` + JSON credentials | HTTP `200 OK` ; champ `$.token` présent dans le JSON | `MockMvc -> POST /api/auth/signin` |
| **TI04** | Connexion HTTP avec mot de passe erroné | User inscrit en base H2 | `POST /api/auth/signin` + mauvais mot de passe | HTTP `401 Unauthorized` | `MockMvc -> POST /api/auth/signin` |
| **TI05** | Protection API sans jeton JWT | Aucun jeton HTTP | `GET /api/categories` sans header Auth | HTTP `401 Unauthorized` | `MockMvc -> GET /api/categories` |
| **TI06** | Lifecycle CRUD Catégorie avec Jeton JWT | Jeton JWT valide | Requests `POST`, `GET`, `PUT`, `DELETE` `/api/categories` | HTTP `201` -> `200` -> `200` -> `204` -> `404` | `MockMvc -> /api/categories` |

---

## 5. Guide d'Exécution et Résultat du Bilan

Pour exécuter uniquement les tests d'intégration :
```powershell
./mvnw.cmd test -Dtest=*IntegrationTest
```

### Bilan d'Exécution :
```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
