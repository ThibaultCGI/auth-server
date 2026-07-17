# Roadmap

## Objectif

Ce document décrit les grandes étapes de construction du projet `auth-server`.

L'objectif est de suivre une progression incrémentale permettant :

- de construire un socle métier solide ;
- de conserver une architecture propre ;
- de livrer des fonctionnalités exploitables à chaque étape ;
- d'introduire progressivement les mécanismes de sécurité avancés.

---

# Vision du projet

Construire un serveur d'authentification moderne reposant sur :

- Spring Boot
- PostgreSQL
- Liquibase
- Spring Security
- OAuth2
- OpenID Connect

Le projet doit rester :

- modulaire ;
- testable ;
- maintenable ;
- indépendant des détails techniques.

---

# Phase 1 - Fondations techniques

## Objectif

Mettre en place le socle technique du projet.

## État

✅ Terminé

## Réalisations

### Architecture

- architecture hexagonale ;
- découpage multi-modules Maven ;

Modules :

```text
auth-core
auth-infrastructure
auth-boot
```

---

### Base de données

- PostgreSQL ;
- schéma dédié :

```text
auth_server
```

---

### Migrations

- intégration Liquibase ;
- changelog principal ;
- première migration versionnée.

---

### Modèle relationnel

Création des tables :

```text
users
roles
users_roles
```

---

# Phase 2 - Gestion des utilisateurs

## Objectif

Construire les premières fonctionnalités métier autour des utilisateurs.

## État

✅ Terminé

## Réalisations

### Domaine

Création :

```text
User
```

---

### Validation

Création :

```text
UserRules
UserValidationUtils
```

Règles implémentées :

- validation du username ;
- normalisation du username ;
- validation du mot de passe.

---

### Persistance

Création :

```text
UserEntity
UserJpaRepository
UserRepositoryAdapter
UserMapper
```

---

### Cas d'usage

Implémentation :

```text
CreateUserUseCase
GetUserUseCase
```

---

### Sécurité

Création :

```text
PasswordEncoderPort
PasswordEncoderAdapter
```

Implémentation actuelle :

```text
BCrypt
```

---

### Tests

Tests unitaires :

- validation métier ;
- orchestration ;
- persistance simulée via Mockito.

---

# Phase 3 - Authentification utilisateur

## Objectif

Permettre l'authentification d'un utilisateur à partir de son username et de son mot de passe.

## État

🚧 À faire

## Fonctionnalités prévues

### PasswordEncoderPort

Ajout :

```java
boolean matches(
        String rawPassword,
        String encodedPassword
);
```

---

### AuthenticateUserUseCase

Création :

```text
AuthenticateUserUseCase
```

Responsabilités :

- validation des entrées ;
- récupération de l'utilisateur ;
- vérification du mot de passe ;
- vérification de l'état du compte ;
- retour de l'utilisateur authentifié.

---

### Tests

Création des tests unitaires associés.

---

# Phase 4 - Gestion des rôles

## Objectif

Permettre l'association de rôles aux utilisateurs.

## État

🚧 À faire

## Fonctionnalités prévues

### Domaine

Création :

```text
Role
```

---

### Persistance

Création :

```text
RoleEntity
RoleJpaRepository
RoleRepositoryAdapter
RoleMapper
```

---

### Cas d'usage

Création :

```text
AssignRoleToUserUseCase
```

et autres cas d'usage nécessaires.

---

# Phase 5 - Intégration Spring Security

## Objectif

Connecter le domaine métier au framework de sécurité.

## État

🚧 À faire

## Fonctionnalités prévues

### Configuration

Création :

```text
SecurityConfiguration
```

---

### Authentification

Intégration :

```text
SecurityFilterChain
UserDetailsService
```

---

### Contrôle d'accès

Support :

- authentification HTTP ;
- autorisations basées sur les rôles.

---

# Phase 6 - OAuth2 Authorization Server

## Objectif

Transformer l'application en serveur OAuth2.

## État

🚧 À faire

## Fonctionnalités prévues

### Clients OAuth2

Gestion :

```text
OAuthClient
```

---

### Scopes

Gestion :

```text
Scopes
```

---

### Tokens

Gestion :

```text
Access Token
Refresh Token
```

---

### Autorisations

Gestion :

```text
OAuth Authorizations
```

---

# Phase 7 - OpenID Connect

## Objectif

Exposer une couche d'identité compatible OpenID Connect.

## État

🚧 À faire

## Fonctionnalités prévues

### OIDC

Support :

- ID Token ;
- UserInfo Endpoint ;
- Claims standards ;
- Discovery Endpoint.

---

# Phase 8 - Qualité et industrialisation

## Objectif

Préparer une exploitation durable du projet.

## État

🚧 En cours

## Fonctionnalités prévues

### Couverture

Amélioration :

- couverture des tests ;
- tests d'intégration ;
- tests de persistance.

---

### Analyse statique

Renforcement :

- Sonar ;
- dette technique ;
- qualité du code.

---

### CI/CD

Mise en place :

- GitHub Actions ;
- build automatique ;
- exécution des tests ;
- analyse Sonar.

---

### Conteneurisation

Ajout :

```text
Docker
Docker Compose
```

---

# Priorités actuelles

Les prochaines tâches recommandées sont :

1. AuthenticateUserUseCase
2. Extension de PasswordEncoderPort avec matches(...)
3. Tests d'authentification
4. Domaine Role
5. Intégration Spring Security

---

# Long terme

L'objectif final reste :

```text
Utilisateur
    ↓
Authentification
    ↓
Rôles
    ↓
Spring Security
    ↓
OAuth2
    ↓
OpenID Connect
```

Chaque étape doit fournir une fonctionnalité complète, testée et documentée avant de passer à la suivante.