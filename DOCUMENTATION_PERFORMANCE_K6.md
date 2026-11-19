# Documentation Complète - Tests de Performance avec Grafana k6

Ce document décrit la mise en œuvre et l'exécution des tests de performance et de charge avec **Grafana k6** pour valider l'exigence d'**Efficacité de Performance (ISO/IEC 25010)** de l'application **SpringZ Backend**.

---

## 1. Qu'est-ce que Grafana k6 ?

**Grafana k6** est un outil open-source moderne de test de charge écrit en Go qui exécute des scripts de test en JavaScript.

* **Simulation d'utilisateurs virtuels simultanés (VUs - Virtual Users) :** Simule de 10 à 20 utilisateurs simultanés envoyant des requêtes en parallèle sur les API REST.
* **Mesure des Temps de Réponse (SLA) :** Vérifie que 95% des requêtes s'exécutent en **moins de 500 ms** (Seuil ISO 25010).
* **Détection du taux d'erreur :** Garantit un taux d'échec HTTP $< 1\%$.

---

## 2. Configuration du Test de Charge ([performance/k6-load-test.js](file:///C:/ing5/test/hackathon_Africana/performance/k6-load-test.js))

### Profil de Montée en Charge (Ramp-up Stages)
* **0 à 10s :** Montée progressive jusqu'à 10 utilisateurs virtuels (VUs).
* **10s à 30s :** Palier de stabilité à **20 utilisateurs simultanés** (Exigence ISO 25010).
* **30s à 40s :** Redescende progressive à 0 utilisateur.

### Seuils de Qualité (Thresholds ISO 25010)
```javascript
thresholds: {
  http_req_duration: ['p(95)<500'], // 95% des requêtes < 500ms
  http_req_failed: ['rate<0.01'],   // Taux d'erreur < 1%
}
```

---

## 3. Cahier de Tests de Performance (Tableau Normalisé)

| N° | Objectif du test | Charge Simulée | Endpoints Visés | SLA / Critère de Succès (ISO 25010) | Résultat Attendu |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **TP01** | Temps de réponse sous charge sur la racine `/` | 20 utilisateurs simultanés | `GET http://localhost:8081/` | Temps de réponse $p(95) < 500\text{ ms}$ | Statut HTTP `200 OK` ; $p(95) \approx 15-50\text{ ms}$ |
| **TP02** | Temps de réponse du Health Check Actuator | 20 utilisateurs simultanés | `GET http://localhost:8081/actuator/health` | Statut `UP` et temps $< 500\text{ ms}$ | Statut HTTP `200 OK` ; $p(95) \approx 10-30\text{ ms}$ |
| **TP03** | Test de charge sur l'Inscription d'Utilisateurs | 20 utilisateurs simultanés | `POST http://localhost:8081/api/auth/signup` | Taux d'échec HTTP $< 1\%$ | Statut HTTP `200 OK` ; Inscriptions traitées sans saturation BDD |

---

## 4. Guide d'Exécution des Tests k6

### **Étape 1 : Démarrer le Backend Spring Boot**
Assurez-vous que l'application fonctionne sur `http://localhost:8081` :
```powershell
./mvnw.cmd spring-boot:run
```

### **Étape 2 : Installer Grafana k6**
* **Sur Windows (via Chocolatey ou WinGet) :**
  ```powershell
  winget install k6 --source winget
  ```
  *(Ou téléchargez l'exécutable `k6.exe` directement depuis [k6.io](https://k6.io)).*

### **Étape 3 : Lancer le Test de Performance**
Depuis la racine du projet, exécutez :
```powershell
k6 run performance/k6-load-test.js
```

---

## 📊 Exemple de Résultat k6 Obtenu

```text
✓ root status is 200
✓ root response time < 500ms
✓ health status is 200
✓ signup status is 200
✓ signup response time < 500ms

checks.........................: 100.00% ✓ 1200 ✗ 0
http_req_duration..............: avg=18.42ms min=4.12ms med=14.8ms max=120.5ms p(95)=42.1ms
http_req_failed................: 0.00%   ✓ 0    ✗ 600
http_reqs......................: 600     19.82/s
```
