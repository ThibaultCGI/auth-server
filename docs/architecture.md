# Architecture

## Objectif

Le projet `auth-server` a pour objectif de fournir un serveur d’authentification moderne basé sur :

- Spring Boot
- PostgreSQL
- Liquibase
- Spring Security
- OAuth2 / OpenID Connect à terme

L’architecture retenue est une architecture hexagonale afin de séparer clairement :

- le métier ;
- les cas d’usage ;
- les ports ;
- les adaptateurs techniques ;
- la configuration Spring ;
- la persistance ;
- la sécurité.

---

## Découpage multi-modules

Le projet est organisé sous la forme de plusieurs modules Maven :

- `auth-core`
- `auth-infrastructure`
- `auth-boot`

### Rôle de `auth-core`

Le module `auth-core` contient exclusivement les éléments métier.

Il ne dépend :

- ni de Spring ;
- ni de JPA ;
- ni de PostgreSQL ;
- ni de Spring Security.

#### Contenu

- domaine métier ;
- ports ;
- use cases ;
- exceptions métier ;
- règles métier partagées.

---

### Rôle de `auth-infrastructure`

Le module `auth-infrastructure` contient tous les détails techniques.

Il dépend :

- de Spring ;
- de Spring Data JPA ;
- de PostgreSQL ;
- de Liquibase ;
- de Spring Security.

#### Contenu

- entités JPA ;
- repositories Spring Data ;
- adapters qui implémentent les ports ;
- configurations Spring ;
- implémentation technique de l’encodage des mots de passe ;
- persistance PostgreSQL ;
- mappers entre domaine et entités.

---

### Rôle de `auth-boot`

Le module `auth-boot` contient :

- la classe de démarrage Spring Boot ;
- les propriétés applicatives ;
- l’assemblage final de l’application.

---

## Architecture hexagonale

### Ports

Les ports représentent les besoins du métier.

Exemples :

- `UserRepositoryPort`
- `PasswordEncoderPort`

Les ports sont définis dans le module `auth-core`.

---

### Adapters

Les adapters implémentent les ports et relient le core aux technologies concrètes.

Exemples :

- `UserRepositoryAdapter`
- `PasswordEncoderAdapter`

Les adapters sont situés dans `auth-infrastructure`.

---

## Gestion des utilisateurs

### Username

Le `username` est :

- obligatoire ;
- trimé ;
- normalisé en lowercase ;
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
- l’utilisateur est créé avec `enabled = true` ;
- la date de création est injectée via un `Clock`.

---

## Décisions techniques prises

### Encodage des mots de passe

Le core ne connaît pas l’algorithme d’encodage concret.

Le hashage est abstrait derrière :

- `PasswordEncoderPort`

L’implémentation technique actuelle utilise BCrypt côté infrastructure.

---

### Gestion du temps

Le temps courant n’est pas obtenu directement via `LocalDateTime.now()`.

Les use cases reçoivent un :

- `Clock`

injecté par Spring.

L’application utilise :

- `Clock.systemUTC()`

Cela améliore la testabilité et évite les dépendances implicites au fuseau système.

---

### Identifiants

Les identifiants métier sont de type :

- `UUID`

---

### Schéma de base de données

Les tables applicatives sont créées dans le schéma :

- `auth_server`

La base PostgreSQL utilisée est :

- `local`

---

## Persistance

### Outil de migration

Les migrations sont gérées via :

- Liquibase

### Format des migrations

Les migrations sont écrites en :

- XML

### Tables actuellement présentes

- `users`
- `roles`
- `users_roles`

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

### Utilitaires métier

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

- `SpringConfiguration`
- `PersistenceConfiguration`
- `PasswordEncoderConfiguration`
- `UserUseCaseConfiguration`

---

## Tests

Les tests unitaires utilisent :

- JUnit 5
- Mockito

### Philosophie retenue

Les tests sont organisés en plusieurs niveaux :

1. tests des sous-méthodes métier ;
2. tests d’orchestration des use cases ;
3. tests comportementaux plus globaux lorsque nécessaire.

---

## État actuel du métier

Les use cases actuellement implémentés sont :

- `CreateUserUseCase`
- `GetUserUseCase`

Les prochaines étapes prévues sont :

- `AuthenticateUserUseCase`
- gestion des rôles
- intégration Spring Security
- OAuth2 / OpenID Connect

---

## Principes directeurs

Les principes suivants guident le développement du projet :

1. Le métier ne dépend pas du framework.
2. Les règles métier sont centralisées.
3. Le mot de passe n’est jamais stocké en clair.
4. Les détails techniques restent dans l’infrastructure.
5. Les use cases contiennent l’orchestration métier.
6. La base de données est versionnée via Liquibase.
7. Le temps est injecté via un `Clock`.
8. Les tests doivent rester simples, lisibles et maintenables.
