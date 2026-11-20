# **Cahier des Charges : Projet Étudiant** 

### **Développement d'une application logicielle conforme à la norme ISO/IEC 25010** 

_Mode : Projet de groupe_ **_(2 à 3 étudiants max)_** _|   Durée indicative :_ **_4-6 semaines_** 

## 1. Objectifs pédagogiques 

- Concevoir et développer une application métier complète (backend + frontend + base de données). 

- Intégrer, dès la conception, les exigences de qualité logicielle définies par la norme ISO/IEC 25010. 

- Mettre en œuvre une stratégie de test multi-couches, outillée et documentée. 

- Produire une preuve de conformité qualité traçable (exigence ↔ test ↔ outil ↔ résultat). 

## 2. Cadre du projet 

Chaque groupe choisit un domaine métier (section 3) ou en propose un, sous validation de l'encadrant, puis conçoit librement le périmètre fonctionnel (entités, rôles, règles métier) et la stack technique (section 5). Le projet n'impose ni langage ni framework : il impose une couverture qualité et test complète, quel que soit le choix technique. 

## 3. Domaines métiers proposés (exemples, non exhaustifs) 

|**Domaine**|**Exemple d'application**|
|---|---|
|Gestion commerciale|Clients, produits, stock, commandes, facturation|
|Santé / clinique<br>|Gestion des rendez-vous, dossiers patients, ordonnances|
|Éducation|Plateforme e-learning, gestion de scolarité, réservation de salles|
|Ressources humaines|Gestion des congés, paie, recrutement|
|Immobilier|Gestion de biens, visites, contrats de location|
|Transport / mobilité|Covoiturage, réservation de trajets, gestion de flotte|
|Bibliothèque / médiathèque<br>|Catalogue, prêts, réservations, pénalités|
|Événementiel|Billetterie, gestion de salles, inscriptions|
|Restauration|Réservation de tables, commandes, gestion de menu|
|Associatif / bénévolat|Gestion des adhérents, missions, dons|



_Un groupe peut proposer un autre domaine s'il respecte la richesse fonctionnelle minimale (section 4)._ 

## 4. Exigences fonctionnelles minimales (indépendantes du domaine) 

Quel que soit le domaine choisi, l'application doit comporter : 

- Authentification et gestion des rôles (≥ 3 profils utilisateurs distincts, droits différenciés). 

- Au moins 4 entités métier liées par des relations réelles (1-N, N-N) avec CRUD complet. 

- Au moins un workflow à statuts (ex. brouillon → validé → clôturé) avec règles de transition. 

- Un tableau de bord (indicateurs agrégés, filtres). 

- Une recherche/filtrage sur au moins une entité à fort volume. 

- Un journal d'audit des actions sensibles (qui a fait quoi, quand). 

- Au moins 5 règles métier explicites, documentées et testables (ex. unicité, quotas, cohérence de stock/solde/planning). 

## 5. Boîte à outils technique : frameworks possibles 

Le groupe choisit un stack cohérent et justifie son choix (2–3 lignes) dans le rapport. 

|**Couche**|**Options possibles**|
|---|---|
|Backend|Python (FastAPI, Django) · Java (Spring Boot) · Node.js (NestJS, Express) · PHP<br>(Laravel)·.NET (ASP.NET Core)|
|Frontend|React·Angular·Vue.js·ou rendu serveur (Jinja2, Thymeleaf, Blade)|
|Mobile (optionnel)|Flutter·React Native|
|Base de données|PostgreSQL·MySQL·MongoDB·SQLite (petits volumes)|
|Authentification|JWT·OAuth2·Sessions serveur|
|Conteneurisation|Docker / Docker Compose (fortement recommandé pour la portabilité, cf.<br>section6.8)|



## 6. Exigences de qualité : norme ISO/IEC 25010 

Le modèle ISO 25010 définit 8 caractéristiques de qualité. Chaque groupe doit démontrer, pour chacune, une exigence concrète et au moins un test/outil qui la vérifie. 

|**#**|**Caractéristique**|**Exigence attendue dans le projet**|**Couche de test associée**|
|---|---|---|---|
|6.1|Aptitude fonctionnelle|Toutes les fonctionnalités du section4 implémentées et<br>exactes|Tests fonctionnels / E2E|
|6.2|Efficacité de performanc|e Temps de réponse mesuré sous charge (ex. < 500 ms à 20<br>utilisateurs simultanés)|<br>Tests de performance|
|6.3|Compatibilité|Fonctionnement sur ≥ 2 navigateurs et/ou coexistence<br>avec un système tiers (API externe)|Tests de compatibilité|
|6.4|Facilité d'utilisation|Parcours utilisateur validé (ergonomie, accessibilité de<br>base)|Tests fonctionnels /<br>recette utilisateur|
|6.5|Fiabilité|Comportement stable en cas d'erreur/panne (pas de perte<br>de données, reprise correcte)|Tests de fiabilité|
|6.6|Sécurité|Authentification robuste, contrôle d'accès par rôle,<br>absence de failles connues (OWASP Top 10)|Tests de sécurité<br>(SAST/SCA/DAST)|
|6.7|Maintenabilité|Code structuré en couches, complexité maîtrisée,<br>couverture de tests mesurée|Analyse statique / qualité<br>de code|
|6.8|Portabilité|Installation reproductible sur une autre machine (script<br>ou conteneur)|Tests de portabilité /<br>déploiement|



## 7. Boîte à outils de test : rôle de chaque outil 

|**Couche de test**|**Objectif**|**Outils possibles**|**Caractéristique ISO 25010**<br>**couverte**|
|---|---|---|---|
|Tests unitaires|Valider la logique métier isolée<br>(calculs, règles, validations)|pytest, JUnit, Jest, PHPUnit|Aptitude fonctionnelle,<br>Maintenabilité|
|Tests d'intégration|Valider les interactions entre<br>composants (API ↔ base de<br>données)|TestClient/httpx, Spring Test,<br>Supertest|Aptitude fonctionnelle,<br>Fiabilité|
|Tests API|Valider le contrat d'interface<br>(codes HTTP, structure des<br>réponses)|Postman/Newman, RestAssured,<br>Insomnia|<br>Aptitude fonctionnelle,<br>Compatibilité|
|Tests fonctionnels /<br>E2E|<br>Valider les parcours utilisateurs<br>réels dans l'interface|Selenium, Playwright, Cypress|Aptitude fonctionnelle,<br>Facilité d'utilisation|
|Tests de<br>performance|Mesurer temps de réponse, débit,<br>comportement sous charge|JMeter, Locust, Gatling, k6|Efficacité de performance|
|Tests de fiabilité|Vérifier la cohérence après échec,<br>transactions, répétition|Scénarios pytest/JUnit dédiés,<br>tests de chaos ciblés|Fiabilité|
|Analyse statique<br>(SAST)|Détecter des vulnérabilités et anti-<br>patterns dans le code source|Bandit, SonarQube, ESLint<br>(règles sécurité), SpotBugs|Sécurité, Maintenabilité|
|Analyse des<br>dépendances<br>(SCA)|Détecter les CVE connues dans<br>les librairies utilisées|pip-audit, Snyk, OWASP<br>Dependency-Check|Sécurité|
|Analyse<br>dynamique<br>(DAST)|Scanner l'application en cours<br>d'exécution (injections, en-têtes<br>manquants)|OWASP ZAP, Burp Suite|Sécurité|
|Qualité /<br>maintenabilité|Mesurer complexité, duplication,<br>couverture, dette technique|SonarQube, Ruff/Black,<br>Checkstyle|Maintenabilité|
|Tests de<br>compatibilité|Vérifier le comportement multi-<br>navigateur / multi-environnement|BrowserStack, tests manuels<br>multi-navigateurs|Compatibilité|
|Tests de portabilité|Vérifier l'installation/déploiement<br>reproductible|Docker, scripts d'installation<br>documentés|Portabilité|



_Principe pédagogique : aucun outil n'est imposé ; un équivalent dans l'écosystème choisi par le groupe est accepté, à condition que le rôle (colonne « Objectif ») soit assuré et prouvé par un résultat réel, jamais simulé ou inventé._ 

## 8. Livrables attendus 

- Code source complet, versionné (dépôt Git avec historique de commits). 

- Rapport de conception : architecture, modèle de données, choix techniques justifiés. 

- Matrice de traçabilité : exigence fonctionnelle/qualité ↔ test(s) ↔ outil(s) ↔ résultat obtenu. 

- Jeux de tests exécutables pour chaque couche du section7, avec rapports générés (couverture, résultats). 

- Rapport de conformité ISO 25010 : pour chacune des 8 caractéristiques, preuve concrète (capture, log, métrique). 

- Guide d'installation et d'exécution (reproductible par un tiers). 

- Soutenance avec démonstration live de l'application et d'au moins deux couches de test en exécution. 

### Règle d'or du projet 

**Aucun résultat de test ne doit être affirmé sans preuve d'exécution réelle. Un outil non exécutable dans l'environnement du groupe doit être documenté comme tel (procédure fournie, résultat non disponible), plutôt que simulé.** 

