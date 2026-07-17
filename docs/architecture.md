# Architecture

## Objectif

Le projet `auth-server` a pour objectif de fournir un serveur d’authentification basé sur Spring Boot, PostgreSQL, Liquibase et une architecture hexagonale.

L’objectif architectural principal est de maintenir une séparation claire entre :

- le **domaine métier** ;
- les **cas d’usage** ;
- les **ports** ;
- les **adaptateurs techniques** ;
- la **configuration Spring** ;
- la **persistance** ;
- la **sécurité**.

---

## Découpage multi-modules

Le projet est organisé en plusieurs modules Maven :

- `auth-core`
- `auth-infrastructure`
- `auth-boot`

### Rôle de `auth-core`

Le module `auth-core` contient :

- le modèle métier ;
- les ports ;
- les cas d’usage ;
- les exceptions métier ;
- les règles métier partagées.

Le module `auth-core` ne dépend pas de Spring, de JPA, ni de PostgreSQL.

### Rôle de `auth-infrastructure`

Le module `auth-infrastructure` contient :

- les entités JPA ;
- les repositories Spring Data JPA ;
- les adapters qui implémentent les ports ;
- les configurations Spring ;
- l’implémentation technique du `PasswordEncoderPort` ;
- la persistance PostgreSQL ;
- les mappers domaine ↔ entity.

### Rôle de `auth-boot`

Le module `auth-boot` contient :

- le point d’entrée Spring Boot ;
- l’assemblage de l’application ;
- les propriétés applicatives ;
- les profils ;
- les besoins d’exécution.

---

## Principes d’architecture

### 1. Le domaine ne dépend d’aucune technologie

Le domaine métier est isolé de tout détail technique :

- pas d’annotation JPA ;
- pas d’annotation Spring ;
- pas de dépendance à BCrypt ;
- pas de dépendance à PostgreSQL.

### 2. Les règles métier sont centralisées

Les règles métier liées à l’utilisateur sont centralisées dans :

- `UserValidationUtils`
- `UserRules`

Cela permet de réutiliser la logique entre plusieurs use cases sans dupliquer les règles.

### 3. Les use cases orchestrent le métier

Les use cases sont responsables de :

- valider les entrées ;
- appliquer les règles métier ;
- appeler les ports ;
- construire les objets métier ;
- retourner le résultat métier.

### 4. Les adapters réalisent le pont technique

Les adapters sont responsables de relier :

- le core métier ;
- et les technologies concrètes.

Exemples :

- `UserRepositoryAdapter` ↔ `UserJpaRepository`
- `PasswordEncoderAdapter` ↔ `BCryptPasswordEncoder`

---

## Modèle métier actuel

Le premier socle métier repose sur les entités suivantes :

- `User`
- `Role`

À ce stade, `User` est la brique principale déjà exploitée par le système.

---

## Règles métier retenues

### Username

Le `username` est :

- obligatoire ;
- normalisé en lowercase ;
- trimé ;
- unique ;
- limité à une longueur maximale de 100 caractères.

### Password

Le mot de passe est :

- obligatoire ;
- non vide ;
- non blanc ;
- limité à une longueur minimale de 12 caractères ;
- limité à une longueur maximale de 128 caractères.

### Création d’un utilisateur

Lors de la création d’un utilisateur :

- le `username` est normalisé ;
- le mot de passe est encodé via `PasswordEncoderPort` ;
- l’utilisateur est marqué comme `enabled = true` ;
- la date de création est positionnée via `Clock`.

---

## Décisions techniques prises

### 1. Encodage des mots de passe

Le core ne connaît pas l’algorithme d’encodage concret.

Le hashage est abstrait derrière :

- `PasswordEncoderPort`

L’implémentation technique actuelle utilise BCrypt côté infrastructure.

### 2. Gestion du temps

Le temps courant n’est pas obtenu via `LocalDateTime.now()` directement.

L’application injecte un :

- `Clock`

et utilise :

- `LocalDateTime.now(clock)`

Le clock est configuré en :

- `Clock.systemUTC()`

Cela améliore la testabilité et évite les dépendances implicites au fuseau système.

### 3. Stockage des dates

Le champ `createdAt` est géré comme une date technique de création, tout en restant simple dans le modèle actuel.

### 4. Identifiants

Les identifiants métier sont de type :

- `UUID`

### 5. Schéma de base de données

La base utilisée est :

- `local`

Le schéma applicatif est :

- `auth_server`

Liquibase crée et maintient les tables dans ce schéma.

---

## Structure logique du core

### Domaine

- `User`
- `Role`

### Ports

- `UserRepositoryPort`
- `PasswordEncoderPort`

### Use cases

- `CreateUserUseCase`
- `GetUserUseCase`

### Helpers métier

- `UserValidationUtils`
- `UserRules`

### Exceptions

- `AuthServerException`
- `AuthServerFunctionalException`

---

## Structure logique de l’infrastructure

### Persistance

- `UserEntity`
- `UserJpaRepository`
- `UserRepositoryAdapter`
- `UserMapper`

### Sécurité

- `PasswordEncoderAdapter`
- configuration BCrypt

### Configuration

- `PersistenceConfiguration`
- `PasswordEncoderConfiguration`
- `UserUseCaseConfiguration`
- `SpringConfiguration`

---

## Liquibase

Les migrations sont gérées par Liquibase XML.

### Emplacement

```text
auth-infrastructure/src/main/resources/db/changelog
