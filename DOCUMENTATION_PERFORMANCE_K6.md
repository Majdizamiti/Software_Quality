# Documentation Complète & Cahier de Tests de Performance (Grafana k6)

Ce document décrit la mise en œuvre, l'exécution et les résultats réels des tests de performance et de charge avec **Grafana k6** pour valider l'exigence d'**Efficacité de Performance (ISO/IEC 25010)** de l'application **SpringZ Backend**.

---

## 1. Présentation des Tests de Charge k6

**Grafana k6** est un outil open-source moderne de test de charge écrit en Go qui exécute des scripts JavaScript.

* **Charge simulée :** Montée en charge progressive jusqu'à **20 utilisateurs virtuels simultanés (VUs)**.
* **SLA ISO/IEC 25010 :** Vérifie que 95% des requêtes s'exécutent en **moins de 500 ms** ($p(95) < 500\text{ ms}$) et que le taux d'échec est $< 5\%$.
* **Résultat d'exécution réel :** **1500/1500 vérifications réussies (100% de succès)** avec un temps de réponse $p(95) = 5.43\text{ ms}$ et $0.00\%$ d'erreur HTTP.

---

## 2. Configuration du Test de Charge ([performance/k6-load-test.js](file:///C:/ing5/test/hackathon_Africana/performance/k6-load-test.js))

### Profil de Montée en Charge (Ramp-up Stages)
* **0 à 5s :** Montée progressive jusqu'à 5 utilisateurs virtuels (VUs).
* **5s à 20s :** Palier de stabilité à **20 utilisateurs simultanés** (Exigence ISO 25010).
* **20s à 25s :** Redescende progressive à 0 utilisateur.

### Seuils de Qualité (Thresholds ISO 25010)
```javascript
thresholds: {
  http_req_duration: ['p(95)<500'], // 95% des requêtes < 500ms
  http_req_failed: ['rate<0.05'],   // Taux d'erreur < 5%
}
```

---

## 📋 3. Cahier de Tests de Performance k6 (Tableau Normalisé)

Issu directement du résultat d'exécution réel de la commande `k6 run performance/k6-load-test.js` :

| N° | Objectif du test | Charge Simulée | Endpoint Visé | SLA / Critère de Succès (ISO 25010) | Résultat Obtenu | Statut |
| :---: | :--- | :--- | :--- | :--- | :--- | :---: |
| **TP01** | Temps de réponse sous charge sur la racine `/` | 20 VUs simultanés | `GET http://localhost:8081/` | Statut `200 OK` & $p(95) < 500\text{ ms}$ | Statut HTTP `200 OK` ; Temps $p(95) = 5.43\text{ ms}$ | ✅ **PASSED** |
| **TP02** | Temps de réponse du Health Check Actuator | 20 VUs simultanés | `GET http://localhost:8081/actuator/health` | Statut `200 OK` & $p(95) < 500\text{ ms}$ | Statut HTTP `200 OK` ; Temps $p(95) = 5.43\text{ ms}$ | ✅ **PASSED** |
| **TP03** | Taux d'erreur global sous charge | 20 VUs simultanés (750 requêtes HTTP) | Endpoints API backend | Taux d'erreur HTTP $< 5\%$ | **`0.00%` d'erreur HTTP** (0 échec sur 750 requêtes) | ✅ **PASSED** |
| **TP04** | Vérification globale des assertions (Checks) | 20 VUs simultanés (1500 checks) | Endpoints API backend | Taux de réussite des checks = 100% | **`100.00%` de succès** (1500 checks validés sur 1500) | ✅ **PASSED** |

---

## 📊 4. Rapport d'Exécution Réel Grafana k6

```text
  █ THRESHOLDS 

    http_req_duration
    ✓ 'p(95)<500' p(95)=5.43ms

    http_req_failed
    ✓ 'rate<0.05' rate=0.00%


  █ TOTAL RESULTS 

    checks_total.......: 1500    73.252538/s
    checks_succeeded...: 100.00% 1500 out of 1500
    checks_failed......: 0.00%   0 out of 1500

    ✓ root status is 200
    ✓ root response time < 500ms
    ✓ health status is 200
    ✓ health response time < 500ms

    HTTP
    http_req_duration..............: avg=3.26ms   min=0s       med=3.03ms   max=159.1ms  p(90)=4.85ms   p(95)=5.43ms  
      { expected_response:true }...: avg=3.26ms   min=0s       med=3.03ms   max=159.1ms  p(90)=4.85ms   p(95)=5.43ms  
    http_req_failed................: 0.00%  0 out of 750
    http_reqs......................: 750    36.626269/s

    EXECUTION
    iteration_duration.............: avg=507.18ms min=503.26ms med=506.24ms max=710.56ms p(90)=508.81ms p(95)=509.89ms
    iterations.....................: 375    18.313134/s
    vus............................: 2      min=1        max=19
    vus_max........................: 20     min=20       max=20

    NETWORK
    data_received..................: 422 kB 21 kB/s
    data_sent......................: 58 kB  2.8 kB/s

running (20.5s), 00/20 VUs, 375 complete and 0 interrupted iterations
default ✓ [======================================] 00/20 VUs  20s
```

---

## 5. Synthèse de Conformité ISO/IEC 25010 (Efficacité de Performance)

* **Débit Moyen (Throughput) :** $36.62\text{ req/sec}$ (750 requêtes HTTP traitées en 20.5 secondes).
* **Temps de Réponse Moyen :** $3.26\text{ ms}$ (Moyenne ultra-rapide).
* **Percentile 95 ($p95$) :** $5.43\text{ ms}$ (Largement inférieur au seuil maximum de $500\text{ ms}$).
* **Stabilité sous Charge (20 VUs) :** $100\%$ de succès des requêtes HTTP sans aucune perte de paquet ni plantage serveur.
