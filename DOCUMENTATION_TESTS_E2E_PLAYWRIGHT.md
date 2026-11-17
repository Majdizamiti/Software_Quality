# Documentation Complète - Tests End-to-End (E2E) avec Playwright

Ce document est la **documentation officielle des Tests End-to-End (E2E)** créés pour l'application **SpringZ** à l'aide du framework **Playwright**.

---

## 1. Qu'est-ce qu'un Test End-to-End (E2E) ?

Un **Test End-to-End (E2E)** simule le comportement exact d'un **véritable utilisateur humain** interagissant avec votre application via un vrai navigateur web (Chrome, Chromium, Firefox, WebKit/Safari).

* **Du navigateur jusqu'au backend :** Il clique sur les boutons, remplit les formulaires, vérifie les redirections d'URL et les messages affichés.
* **Test de l'expérience utilisateur complète (UI + API + Navigation) :** Valide que l'application React fonctionne parfaitement dans un navigateur réel.

---

## 2. Configuration de Playwright

* **Package installé :** `@playwright/test`
* **Fichier de configuration :** [frontend/playwright.config.js](file:///C:/ing5/test/hackathon_Africana/frontend/playwright.config.js)
* **Serveur Web Automatique :** Playwright démarre automatiquement le serveur de dev Vite sur `http://localhost:5173` lors du lancement des tests.

---

## 3. Cahier de Tests End-to-End (Tableau Normalisé)

| N° | Objectif du test | État initial / URL | Actions / Interactions Utilisateur | Résultat attendu | Fichier de Test |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **TE01** | Affichage du Dashboard principal | `http://localhost:5173/` | Chargement de la page d'accueil. | Titre "SpringZ Admin" et sous-titre "Client Dashboard" visibles. | [auth.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/auth.spec.js) |
| **TE02** | Basculement entre Connexion et Inscription | `http://localhost:5173/auth` | 1. Clic sur "Need an account?".<br>2. Clic sur "Already have an account?". | 1. Affichage du formulaire "Create account" + champ "Name".<br>2. Retour au formulaire "Sign in". | [auth.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/auth.spec.js) |
| **TE03** | Redirection des pages protégées sans connexion | `http://localhost:5173/categories` | Tentative d'accès direct à `/categories` sans jeton de connexion. | Redirection automatique vers `/auth` avec formulaire "Sign in" affiché. | [auth.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/auth.spec.js) |
| **TE04** | Navigation via la barre de navigation (Nav) | `http://localhost:5173/` | Clic sur le lien "Sign in / Sign up" de la barre de navigation. | Navigation vers la page d'authentification (`/auth`). | [dashboard.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/dashboard.spec.js) |
| **TE05** | Affichage des cartes de ressources | `http://localhost:5173/` | Consultation des modules sur le tableau de bord. | Cartes "Categories", "Products", "Customers", "Orders" visibles. | [dashboard.spec.js](file:///C:/ing5/test/hackathon_Africana/frontend/e2e/dashboard.spec.js) |

---

## 4. Guide d'Exécution des Tests Playwright

### Étape 1 : Exécuter les tests E2E
Depuis la racine du projet, lancez la commande npm :
```powershell
npm --prefix frontend run test:e2e
```

### Étape 2 : Mode Graphique (Interface visuelle Playwright)
Pour voir le navigateur exécuter les clics et interactions en direct :
```powershell
npx --prefix frontend playwright test --ui
```

### Étape 3 : Afficher le rapport de test HTML
Après l'exécution, vous pouvez générer et afficher un rapport interactif complet :
```powershell
npx --prefix frontend playwright show-report



💻 Commandes d'Exécution Playwright
Pour installer les navigateurs Playwright (Chromium/Chrome) et exécuter vos tests E2E :

# 1. Installer le navigateur Chromium Playwright
npx --prefix frontend playwright install chromium

# 2. Lancer les tests E2E
npm --prefix frontend run test:e2e

# 3. Mode Graphique interactif (Interface visuelle Playwright)
npx --prefix frontend playwright test --ui
```
npm --prefix frontend run test:e2e -- --ui