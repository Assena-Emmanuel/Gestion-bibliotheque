# Cahier des charges complet — Application de gestion de bibliothèque

**Projet :** Library Management System
**Version :** 1.0
**Type :** API REST sécurisée (Spring Boot 3 + PostgreSQL + Docker)

---

## 1. Présentation du projet

### 1.1 Contexte

Une bibliothèque souhaite disposer d'une application permettant de gérer :

- les livres,
- les auteurs,
- les catégories,
- les lecteurs,
- les emprunts et retours,
- les réservations,
- les statistiques d'activité.

L'application sera développée sous forme d'une **API REST sécurisée** avec **Spring Boot 3**, **PostgreSQL** et **Docker**.

### 1.2 Public cible

- Bibliothécaires (gestion interne du fonds documentaire).
- Lecteurs/abonnés (consultation, réservation, emprunt).
- Administrateurs techniques (supervision, statistiques).

---

## 2. Objectifs du projet

### 2.1 Objectif principal

Développer une application backend permettant :

- la gestion du catalogue de livres,
- le suivi des exemplaires disponibles,
- la gestion des utilisateurs,
- le contrôle des emprunts,
- la gestion des réservations,
- la consultation des statistiques de la bibliothèque.

### 2.2 Objectifs techniques

- appliquer les bonnes pratiques Spring Boot (architecture en couches),
- utiliser JPA/Hibernate pour la persistance,
- sécuriser l'API avec JWT (authentification et autorisation par rôle),
- documenter l'API avec Swagger/OpenAPI,
- conteneuriser l'application et PostgreSQL avec Docker (Docker Compose),
- garantir la validation des données côté serveur (Bean Validation),
- gérer les exceptions de façon centralisée (`@ControllerAdvice`),
- assurer la traçabilité (logs, dates de création/modification).

---

## 3. Périmètre fonctionnel

Le système comporte **trois profils** :

### 3.1 Administrateur

Peut :

- gérer les comptes bibliothécaires,
- configurer les paramètres globaux (durée d'emprunt, nombre max d'emprunts, pénalités),
- accéder à toutes les fonctionnalités du bibliothécaire.

### 3.2 Bibliothécaire

Peut :

- gérer les auteurs,
- gérer les catégories,
- gérer les livres et leurs exemplaires,
- gérer les comptes lecteurs (activation, blocage),
- enregistrer les emprunts,
- enregistrer les retours,
- gérer les réservations (validation, annulation),
- appliquer des pénalités en cas de retard,
- consulter les statistiques,
- consulter l'historique complet de tous les utilisateurs.

### 3.3 Lecteur

Peut :

- créer un compte,
- se connecter / se déconnecter,
- consulter le catalogue,
- rechercher un livre,
- réserver un livre indisponible,
- emprunter un livre (si autorisé et disponible),
- retourner un livre,
- consulter son historique personnel (emprunts, réservations, pénalités),
- mettre à jour son profil.

---

## 4. Exigences fonctionnelles détaillées

### 4.1 Gestion des auteurs

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Ajouter un auteur | Création d'une fiche auteur |
| Modifier un auteur | Mise à jour des informations |
| Supprimer un auteur | Suppression si aucune dépendance |
| Consulter un auteur | Détail d'un auteur, y compris ses livres |
| Lister tous les auteurs | Liste paginée et triable |

**Entrées**

- nom (obligatoire)
- biographie (optionnelle)

**Règles métier**

- le nom est obligatoire ;
- un auteur ne peut pas être supprimé s'il possède encore des livres associés (erreur 409 Conflict) ;
- recherche possible par nom (partielle, insensible à la casse).

---

### 4.2 Gestion des catégories

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Ajouter une catégorie | Création d'une catégorie |
| Modifier une catégorie | Mise à jour du nom/description |
| Supprimer une catégorie | Suppression si aucune dépendance |
| Lister les catégories | Liste complète ou paginée |

**Règles métier**

- le nom doit être unique (contrainte `UNIQUE` en base) ;
- une catégorie utilisée par au moins un livre ne peut pas être supprimée.

---

### 4.3 Gestion des livres

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Ajouter un livre | Création d'une fiche livre avec auteur et catégorie |
| Modifier un livre | Mise à jour des informations |
| Supprimer un livre | Suppression si aucun emprunt en cours |
| Consulter un livre | Détail complet, avec disponibilité en temps réel |
| Lister les livres | Liste paginée, triable (titre, année, disponibilité) |
| Rechercher un livre | Recherche multicritère |

**Recherche par**

- titre (recherche partielle),
- auteur,
- catégorie,
- ISBN,
- disponibilité (livres disponibles uniquement).

**Pagination des résultats**

Exemple de requête :

```
GET /api/books?page=0&size=10&sortBy=title&direction=ASC
```

Exemple de réponse :

```json
{
  "content": [
    {
      "id": 12,
      "title": "Le Petit Prince",
      "isbn": "978-2-07-040850-4",
      "author": "Antoine de Saint-Exupéry",
      "category": "Littérature",
      "availableCopies": 3,
      "totalCopies": 5
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 128,
  "totalPages": 13
}
```

**Règles métier**

- `availableCopies` ne peut jamais dépasser `totalCopies` ;
- `availableCopies` ne peut jamais être négatif ;
- l'ISBN doit être unique ;
- un livre ne peut être supprimé s'il possède des emprunts en cours (`ONGOING`) ;
- lorsqu'un exemplaire est emprunté, `availableCopies` est décrémenté de 1 ;
- lorsqu'un exemplaire est retourné, `availableCopies` est incrémenté de 1.

---

### 4.4 Gestion des utilisateurs (lecteurs et bibliothécaires)

**Fonctionnalités**

| Fonctionnalité | Rôle | Description |
|---|---|---|
| Inscription (register) | Lecteur | Création de compte auto-service |
| Connexion (login) | Tous | Authentification, génération de JWT |
| Création de bibliothécaire | Admin | Création d'un compte interne |
| Modifier son profil | Tous | Mise à jour nom, email, mot de passe |
| Activer/désactiver un compte | Bibliothécaire/Admin | Blocage en cas d'abus ou de retards répétés |
| Lister les utilisateurs | Bibliothécaire/Admin | Liste paginée, filtrable par rôle |
| Consulter un utilisateur | Bibliothécaire/Admin | Détail avec historique |
| Supprimer un utilisateur | Admin | Suppression si aucun emprunt en cours |

**Règles métier**

- l'email doit être unique et valide (format vérifié) ;
- le mot de passe est stocké chiffré (BCrypt) et doit respecter une politique minimale (8 caractères, au moins 1 chiffre) ;
- un lecteur bloqué ne peut plus emprunter ni réserver ;
- un lecteur ayant des pénalités impayées peut être automatiquement restreint (règle configurable).

---

### 4.5 Gestion des emprunts

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Enregistrer un emprunt | Le bibliothécaire (ou le lecteur, si auto-service autorisé) enregistre l'emprunt d'un exemplaire |
| Définir la date de retour prévue | Calculée automatiquement (ex. +14 jours) ou saisie manuellement |
| Lister les emprunts en cours | Vue globale (bibliothécaire) ou personnelle (lecteur) |
| Lister les emprunts en retard | Filtrage automatique par comparaison avec la date du jour |
| Consulter le détail d'un emprunt | Livre, lecteur, dates, statut |

**Règles métier**

- un emprunt ne peut être créé que si `availableCopies > 0` ;
- un lecteur ne peut pas dépasser un nombre maximal d'emprunts simultanés (paramètre configurable, ex. 3) ;
- un lecteur avec des emprunts en retard ne peut pas emprunter de nouveaux livres ;
- la date d'échéance (`dueDate`) est calculée à partir de la date d'emprunt + durée standard (paramètre configurable) ;
- le statut passe automatiquement à `OVERDUE` si `dueDate` est dépassée et que le livre n'est pas retourné.

---

### 4.6 Gestion des retours

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Enregistrer un retour | Met à jour l'emprunt et incrémente `availableCopies` |
| Calcul automatique du retard | Différence entre date de retour effective et `dueDate` |
| Application d'une pénalité | Si retour en retard, calcul d'une pénalité (montant ou blocage temporaire) |
| Notifier la disponibilité | Si une réservation est en attente sur ce livre, notification/attribution automatique |

**Règles métier**

- `returnDate` est renseignée automatiquement à la date du jour lors du retour ;
- le statut de l'emprunt passe à `RETURNED` ;
- si une réservation est en attente pour ce livre, elle passe en priorité pour le prochain emprunt ;
- la pénalité est calculée selon une formule configurable (ex. : montant fixe par jour de retard).

---

### 4.7 Gestion des réservations

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Réserver un livre | Possible uniquement si tous les exemplaires sont empruntés |
| Annuler une réservation | Par le lecteur ou le bibliothécaire |
| Lister les réservations | Vue globale ou personnelle |
| Conversion réservation → emprunt | Lorsqu'un exemplaire redevient disponible |
| Expiration automatique | Une réservation non honorée sous X jours expire |

**Règles métier**

- une réservation ne peut être créée que si `availableCopies = 0` ;
- un lecteur ne peut pas réserver deux fois le même livre simultanément ;
- les réservations sont traitées par ordre chronologique (FIFO) ;
- une réservation expire si le lecteur ne récupère pas le livre dans un délai donné après notification (paramètre configurable, ex. 48h) ;
- statuts possibles : `PENDING`, `FULFILLED`, `CANCELLED`, `EXPIRED`.

---

### 4.8 Statistiques et tableaux de bord

**Fonctionnalités (accès bibliothécaire/admin)**

| Statistique | Description |
|---|---|
| Livres les plus empruntés | Top N sur une période donnée |
| Taux de disponibilité | Ratio exemplaires disponibles / total par catégorie |
| Emprunts en cours / en retard | Compteurs globaux |
| Nombre de lecteurs actifs | Sur une période donnée |
| Répartition par catégorie | Nombre de livres et d'emprunts par catégorie |
| Historique d'activité | Courbe d'évolution des emprunts/retours dans le temps |
| Pénalités cumulées | Total et détail par lecteur |

---

### 4.9 Authentification et autorisation

**Fonctionnalités**

| Fonctionnalité | Description |
|---|---|
| Inscription | Création de compte lecteur |
| Connexion | Retourne un token JWT (access token) + éventuellement un refresh token |
| Rafraîchissement de token | Génération d'un nouveau token sans ré-authentification complète |
| Déconnexion | Invalidation côté client (et liste noire optionnelle côté serveur) |
| Contrôle d'accès par rôle | `ROLE_ADMIN`, `ROLE_LIBRARIAN`, `ROLE_READER` |

**Règles métier**

- chaque requête aux endpoints protégés doit contenir un JWT valide dans l'en-tête `Authorization: Bearer <token>` ;
- les endpoints de gestion (auteurs, catégories, utilisateurs) sont réservés aux rôles `ADMIN`/`LIBRARIAN` ;
- les endpoints de consultation du catalogue sont accessibles à tous les utilisateurs authentifiés (voire publics selon config) ;
- un lecteur ne peut consulter/modifier que ses propres données (sauf bibliothécaire/admin).

---

## 5. Modèle de données (base PostgreSQL)

### 5.1 Vue d'ensemble des tables

| Table | Description |
|---|---|
| `authors` | Auteurs des livres |
| `categories` | Catégories/genres littéraires |
| `books` | Catalogue des livres |
| `users` | Lecteurs, bibliothécaires et administrateurs |
| `loans` | Emprunts effectués |
| `reservations` | Réservations en attente ou traitées |
| `penalties` | Pénalités appliquées aux lecteurs (optionnel, ou intégré à `loans`) |
| `settings` | Paramètres configurables du système (optionnel) |

### 5.2 Table `authors`

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| name | String (VARCHAR 150) | NOT NULL | Nom complet de l'auteur |
| biography | Text | NULL | Biographie (facultative) |
| nationality | String (VARCHAR 100) | NULL | Nationalité (optionnel) |
| birthDate | Date | NULL | Date de naissance (optionnel) |
| createdAt | Timestamp | NOT NULL, défaut = now() | Date de création de la fiche |
| updatedAt | Timestamp | NULL | Date de dernière modification |

**Relations** : un auteur possède plusieurs livres (`1..N` avec `books`).

### 5.3 Table `categories`

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| name | String (VARCHAR 100) | NOT NULL, UNIQUE | Nom de la catégorie |
| description | Text | NULL | Description de la catégorie |
| createdAt | Timestamp | NOT NULL, défaut = now() | Date de création |

**Relations** : une catégorie possède plusieurs livres (`1..N` avec `books`).

### 5.4 Table `books`

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| title | String (VARCHAR 255) | NOT NULL | Titre du livre |
| isbn | String (VARCHAR 20) | NOT NULL, UNIQUE | Code ISBN |
| description | Text | NULL | Résumé/description |
| publicationYear | Integer | NULL | Année de publication |
| publisher | String (VARCHAR 150) | NULL | Éditeur |
| language | String (VARCHAR 50) | NULL | Langue du livre |
| totalCopies | Integer | NOT NULL, ≥ 0 | Nombre total d'exemplaires |
| availableCopies | Integer | NOT NULL, ≥ 0, ≤ totalCopies | Nombre d'exemplaires disponibles |
| coverImageUrl | String (VARCHAR 500) | NULL | Lien vers la couverture |
| author_id | Long | FK → `authors.id`, NOT NULL | Auteur du livre |
| category_id | Long | FK → `categories.id`, NOT NULL | Catégorie du livre |
| createdAt | Timestamp | NOT NULL, défaut = now() | Date de création |
| updatedAt | Timestamp | NULL | Date de dernière modification |

**Relations** :
- `books.author_id` → `authors.id` (N..1)
- `books.category_id` → `categories.id` (N..1)
- un livre possède plusieurs emprunts et réservations (`1..N`).

### 5.5 Table `users`

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| firstName | String (VARCHAR 100) | NOT NULL | Prénom |
| lastName | String (VARCHAR 100) | NOT NULL | Nom |
| email | String (VARCHAR 150) | NOT NULL, UNIQUE | Adresse email (identifiant de connexion) |
| password | String (VARCHAR 255) | NOT NULL | Mot de passe chiffré (BCrypt) |
| phone | String (VARCHAR 20) | NULL | Numéro de téléphone |
| address | String (VARCHAR 255) | NULL | Adresse postale |
| role | Enum (`ADMIN`, `LIBRARIAN`, `READER`) | NOT NULL, défaut = `READER` | Rôle de l'utilisateur |
| status | Enum (`ACTIVE`, `BLOCKED`, `SUSPENDED`) | NOT NULL, défaut = `ACTIVE` | État du compte |
| registrationDate | Timestamp | NOT NULL, défaut = now() | Date d'inscription |
| updatedAt | Timestamp | NULL | Date de dernière modification |

**Relations** : un utilisateur possède plusieurs emprunts et réservations (`1..N`).

### 5.6 Table `loans` (emprunts)

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| book_id | Long | FK → `books.id`, NOT NULL | Livre emprunté |
| user_id | Long | FK → `users.id`, NOT NULL | Lecteur emprunteur |
| loanDate | Timestamp | NOT NULL, défaut = now() | Date de l'emprunt |
| dueDate | Date | NOT NULL | Date de retour prévue |
| returnDate | Timestamp | NULL | Date de retour effective |
| status | Enum (`ONGOING`, `RETURNED`, `OVERDUE`, `LOST`) | NOT NULL, défaut = `ONGOING` | Statut de l'emprunt |
| processedBy | Long | FK → `users.id`, NULL | Bibliothécaire ayant enregistré l'opération |
| fineAmount | Decimal (10,2) | NULL, défaut = 0 | Montant de la pénalité si retard |

**Relations** :
- `loans.book_id` → `books.id` (N..1)
- `loans.user_id` → `users.id` (N..1)

### 5.7 Table `reservations`

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK, auto-incrémenté | Identifiant unique |
| book_id | Long | FK → `books.id`, NOT NULL | Livre réservé |
| user_id | Long | FK → `users.id`, NOT NULL | Lecteur réservataire |
| reservationDate | Timestamp | NOT NULL, défaut = now() | Date de la réservation |
| expirationDate | Timestamp | NULL | Date limite de retrait après disponibilité |
| status | Enum (`PENDING`, `FULFILLED`, `CANCELLED`, `EXPIRED`) | NOT NULL, défaut = `PENDING` | Statut de la réservation |

**Relations** :
- `reservations.book_id` → `books.id` (N..1)
- `reservations.user_id` → `users.id` (N..1)

### 5.8 Table `settings` (optionnelle, paramétrage système)

| Attribut | Type | Contraintes | Description |
|---|---|---|---|
| id | Long (BIGSERIAL) | PK | Identifiant unique |
| key | String (VARCHAR 100) | NOT NULL, UNIQUE | Clé du paramètre (ex. `MAX_LOANS_PER_USER`) |
| value | String (VARCHAR 255) | NOT NULL | Valeur du paramètre |
| description | Text | NULL | Description du paramètre |

### 5.9 Schéma relationnel (résumé)

```
authors (1) ────< (N) books (N) >──── (1) categories
                     │
                     │ (1)
                     ▼
                    (N)
   users (1) ────< loans >──── books
                     
   users (1) ────< reservations >──── books
```

---

## 6. Exigences non fonctionnelles

| Catégorie | Exigence |
|---|---|
| Performance | Temps de réponse < 300 ms pour les requêtes courantes (hors recherche complexe) |
| Sécurité | Authentification JWT, mots de passe chiffrés (BCrypt), protection CSRF/CORS configurée |
| Disponibilité | Application déployable en environnement conteneurisé (Docker), haute disponibilité visée |
| Scalabilité | Architecture stateless permettant le scaling horizontal |
| Maintenabilité | Code structuré en couches (Controller / Service / Repository / Entity / DTO) |
| Portabilité | Déploiement via Docker Compose (API + PostgreSQL) |
| Traçabilité | Horodatage systématique (createdAt/updatedAt), logs applicatifs |
| Documentation | API documentée via Swagger/OpenAPI, accessible via `/swagger-ui.html` |
| Validation | Validation des entrées via Bean Validation (`@NotNull`, `@Email`, `@Size`, etc.) |
| Gestion des erreurs | Réponses d'erreur standardisées (code HTTP, message, timestamp) via `@ControllerAdvice` |

---

## 7. Architecture technique

### 7.1 Stack technique

| Composant | Technologie |
|---|---|
| Langage | Java 17+ |
| Framework | Spring Boot 3 |
| Persistance | Spring Data JPA / Hibernate |
| Base de données | PostgreSQL 15+ |
| Sécurité | Spring Security + JWT |
| Documentation API | Springdoc OpenAPI (Swagger UI) |
| Build | Maven ou Gradle |
| Conteneurisation | Docker, Docker Compose |
| Tests | JUnit 5, Mockito, Testcontainers |

### 7.2 Architecture en couches

```
Client (Postman / Frontend)
        │
        ▼
   Controller (REST API)
        │
        ▼
   Service (logique métier)
        │
        ▼
   Repository (Spring Data JPA)
        │
        ▼
   PostgreSQL (Docker)
```

### 7.3 Déploiement (Docker Compose)

- Un conteneur pour l'application Spring Boot.
- Un conteneur pour PostgreSQL avec volume persistant.
- Variables d'environnement pour la configuration (URL DB, secret JWT, etc.).

---

## 8. Sécurité

- Authentification par email/mot de passe → génération d'un token JWT signé.
- Le token contient : identifiant utilisateur, rôle, date d'expiration.
- Chaque endpoint protégé vérifie la validité du token via un filtre Spring Security.
- Gestion fine des autorisations par annotation (`@PreAuthorize("hasRole('LIBRARIAN')")`).
- Chiffrement des mots de passe avec BCrypt (jamais stocké en clair).
- Protection contre les injections SQL (usage exclusif de JPA/requêtes paramétrées).

---

## 9. Aperçu des principaux endpoints REST

| Ressource | Méthode | Endpoint | Accès |
|---|---|---|---|
| Auth | POST | `/api/auth/register` | Public |
| Auth | POST | `/api/auth/login` | Public |
| Auteurs | GET/POST/PUT/DELETE | `/api/authors` | Bibliothécaire/Admin (écriture) |
| Catégories | GET/POST/PUT/DELETE | `/api/categories` | Bibliothécaire/Admin (écriture) |
| Livres | GET/POST/PUT/DELETE | `/api/books` | Public (lecture), Bibliothécaire (écriture) |
| Livres | GET | `/api/books/search` | Public |
| Utilisateurs | GET/PUT/DELETE | `/api/users` | Bibliothécaire/Admin |
| Emprunts | POST | `/api/loans` | Bibliothécaire/Lecteur |
| Emprunts | PUT | `/api/loans/{id}/return` | Bibliothécaire |
| Réservations | POST/DELETE | `/api/reservations` | Lecteur |
| Statistiques | GET | `/api/statistics` | Bibliothécaire/Admin |

---

## 10. Livrables attendus

- Code source de l'API (dépôt Git).
- Documentation Swagger/OpenAPI.
- Scripts Docker Compose (application + base de données).
- Jeu de données de test (fichier SQL ou script d'initialisation).
- Documentation technique (README avec instructions d'installation).
- Tests unitaires et d'intégration.

---

## 11. Planning indicatif

| Phase | Contenu | Durée estimée |
|---|---|---|
| 1 | Modélisation des données et mise en place du projet | 1 semaine |
| 2 | Développement CRUD Auteurs/Catégories/Livres | 1 semaine |
| 3 | Gestion des utilisateurs et sécurité JWT | 1 semaine |
| 4 | Gestion des emprunts, retours, réservations | 2 semaines |
| 5 | Statistiques et tableaux de bord | 1 semaine |
| 6 | Tests, documentation, dockerisation | 1 semaine |
