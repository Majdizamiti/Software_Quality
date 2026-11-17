# Documentation Complète - Tests Unitaires (JUnit 5 + Mockito)

Ce document est la **documentation officielle des Tests Unitaires** du projet **SpringZ Backend**. Il détaille le fonctionnement, les outils, l'architecture et inclut le **Cahier de Tests Unitaires** au format normalisé.

---

## 1. Présentation et Objectifs des Tests Unitaires

Les **Tests Unitaires** permettent de vérifier le bon fonctionnement de chaque composant de manière **100% isolée**.

* **Aucune dépendance réseau ni base de données :** Les tests n'utilisent ni MySQL ni H2.
* **Aucun démarrage du serveur Spring :** Le contexte Spring Boot n'est pas chargé (`@SpringBootTest` n'est pas utilisé), ce qui garantit une vitesse d'exécution extrême.
* **Exécution ultra-rapide :** Les 25 tests unitaires s'exécutent en **moins de 2 secondes**.

---

## 2. Outils et Technologies Utilisés

* **JUnit 5 (Jupiter) :** Framework de test standard pour Java (`@Test`, `@BeforeEach`, `assertEquals`, `assertThrows`).
* **Mockito :** Framework de création d'objets simulés (Mocks) :
  * `@ExtendWith(MockitoExtension.class)` : Active l'extension Mockito pour JUnit 5.
  * `@Mock` : Crée une fausse instance d'un composant (ex: `CategoryRepository`).
  * `@InjectMocks` : Instancie la vraie classe à tester et lui injecte les faux composants `@Mock`.
  * `when(...).thenReturn(...)` : Définit le comportement simulé d'une méthode.
  * `verify(...)` : Vérifie qu'une méthode a été appelée un nombre précis de fois.

---

## 3. Architecture et Implémentation Étape par Étape

### Étape 1 : Isolation des Services Métier (Service Layer)
Chaque service (`CategoryServiceImpl`, `ProductServiceImpl`, `CustomerServiceImpl`, `UserServiceImpl`) est testé en simulant les appels vers la couche de données (Repositories JPA).

### Étape 2 : Isolation des Contrôleurs REST (`AuthControllerUnitTest`)
Le contrôleur `AuthController` est testé directement en instanciant la classe Java et en simulant le `PasswordEncoder`, `AuthenticationManager`, `UserRepository` et `JwtUtils`.

### Étape 3 : Isolation de la Sécurité JWT (`JwtUtilsTest`)
Validation unitaire des méthodes de signature, d'expiration et de vérification des jetons JWT.

---

## 4. Cahier de Tests Unitaires (Tableau Normalisé)

| N° | Objectif du test | Données / État initial | Entrées / Paramètres | Résultat attendu | Méthode / Chemin |
| :---: | :--- | :--- | :--- | :--- | :--- |
| **TU01** | Création d'une catégorie valide | Repository vide | `Category(title="Electronics", description="Devices")` | `Category(id=1, title="Electronics")` | `CategoryService#createCategory` |
| **TU02** | Récupération de la liste des catégories | 1 catégorie présente en base | Aucune | `List<Category>` de taille 1 | `CategoryService#getAllCategories` |
| **TU03** | Recherche de catégorie par ID existant | Category(id=1, title="Electronics") | `id = 1L` | `Category(id=1, title="Electronics")` | `CategoryService#getCategoryByID` |
| **TU04** | Recherche de catégorie par ID inexistant | Aucune catégorie pour id=99 | `id = 99L` | Exception `IllegalArgumentException` ("Category not found") | `CategoryService#getCategoryByID` |
| **TU05** | Suppression d'une catégorie par ID | Category(id=1) | `id = 1L` | `repository.deleteById(1L)` appelé 1 fois | `CategoryService#deleteCategory` |
| **TU06** | Création d'un produit valide | Repository vide | `Product(name="Laptop", price=1200.0)` | `Product(id=1, name="Laptop", price=1200.0)` | `ProductService#createProduct` |
| **TU07** | Récupération de la liste des produits | 1 produit présent en base | Aucune | `List<Product>` de taille 1 | `ProductService#getAllProducts` |
| **TU08** | Recherche de produit par ID existant | Product(id=1, name="Laptop") | `id = 1L` | `Product(id=1, name="Laptop")` | `ProductService#getProductByID` |
| **TU09** | Recherche de produit par ID inexistant | Aucun produit pour id=99 | `id = 99L` | Exception `IllegalArgumentException` ("Product not found") | `ProductService#getProductByID` |
| **TU10** | Suppression d'un produit par ID | Product(id=1) | `id = 1L` | `repository.deleteById(1L)` appelé 1 fois | `ProductService#deleteProduct` |
| **TU11** | Création d'un client valide | Repository vide | `Customer(name="John Doe", email="john@ex.com")` | `Customer(id=1, name="John Doe")` | `CustomerService#createCustomer` |
| **TU12** | Récupération de la liste des clients | 1 client présent en base | Aucune | `List<Customer>` de taille 1 | `CustomerService#getAllCustomers` |
| **TU13** | Recherche de client par ID existant | Customer(id=1, name="John Doe") | `id = 1L` | `Customer(id=1, name="John Doe")` | `CustomerService#getCustomerByID` |
| **TU14** | Recherche de client par ID inexistant | Aucun client pour id=99 | `id = 99L` | Exception `IllegalArgumentException` ("Customer not found") | `CustomerService#getCustomerByID` |
| **TU15** | Suppression d'un client par ID | Customer(id=1) | `id = 1L` | `repository.deleteById(1L)` appelé 1 fois | `CustomerService#deleteCustomer` |
| **TU16** | Création d'un utilisateur valide | Repository vide | `User(name="Alice", email="alice@ex.com")` | `User(id=1, name="Alice")` | `UserService#createUser` |
| **TU17** | Récupération de la liste des utilisateurs | 1 utilisateur présent en base | Aucune | `List<User>` de taille 1 | `UserService#getAllUsers` |
| **TU18** | Recherche d'utilisateur par ID existant | User(id=1, name="Alice") | `id = 1L` | `User(id=1, name="Alice")` | `UserService#getUserByID` |
| **TU19** | Recherche d'utilisateur par ID inexistant | Aucun utilisateur pour id=99 | `id = 99L` | Exception `IllegalArgumentException` ("User not found") | `UserService#getUserByID` |
| **TU20** | Suppression d'un utilisateur par ID | User(id=1) | `id = 1L` | `repository.deleteById(1L)` appelé 1 fois | `UserService#deleteUser` |
| **TU21** | Inscription d'un utilisateur (unit) | Email non utilisé | `SignupRequest("Bob", "bob@ex.com", "pass")` | ResponseEntity `200 OK` ("User registered successfully") | `AuthController#signup` |
| **TU22** | Inscription avec email déjà existant (unit) | Email déjà en base | `SignupRequest("Bob", "existing@ex.com", "pass")` | ResponseEntity `400 Bad Request` ("Email is already in use!") | `AuthController#signup` |
| **TU23** | Connexion & génération jeton JWT (unit) | User existe en base | `LoginRequest("user@ex.com", "pass")` | ResponseEntity `200 OK` + `JwtResponse(token)` | `AuthController#signin` |
| **TU24** | Génération & extraction sujet JWT | Config JWT active | `subject = "test@example.com"` | Jeton non nul ; `getSubject()` == "test@example.com" | `JwtUtils#generateTokenFromSubject` |
| **TU25** | Validation de jeton JWT malformé | Config JWT active | `token = "invalid.token.string"` | `validateJwtToken()` == `false` | `JwtUtils#validateJwtToken` |

---

## 5. Guide d'Exécution et Résultat du Bilan

Pour exécuter uniquement les tests unitaires :
```powershell
./mvnw.cmd test -Dtest=*Test,*UnitTest
```

### Bilan d'Exécution :
```text
[INFO] Results:
[INFO] 
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
