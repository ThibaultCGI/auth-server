# OAuth2

## Objectif

Ce document décrit la vision OAuth2 du projet ainsi que les étapes prévues pour atteindre cet objectif.

L'objectif n'est pas d'introduire OAuth2 immédiatement, mais de construire progressivement un socle métier solide avant d'ajouter les fonctionnalités de sécurité avancées.

---

# Vision

À terme, le projet doit fournir un serveur d'authentification capable de :

- authentifier des utilisateurs ;
- gérer les rôles et permissions ;
- délivrer des tokens OAuth2 ;
- gérer des refresh tokens ;
- gérer des clients OAuth2 ;
- supporter OpenID Connect.

L'objectif est de disposer d'un véritable Authorization Server.

---

# État actuel

Les fondations du système sont déjà présentes.

## Utilisateurs

Implémenté :

- User
- UserRepositoryPort
- UserEntity
- UserMapper
- UserJpaRepository
- UserRepositoryAdapter

---

## Validation utilisateur

Implémenté :

- UserRules
- UserValidationUtils

---

## Cas d'usage

Implémenté :

- CreateUserUseCase
- GetUserUseCase

---

## Sécurité

Implémenté :

- PasswordEncoderPort
- PasswordEncoderAdapter
- BCryptPasswordEncoder

---

## Persistance

Implémenté :

- PostgreSQL
- Liquibase
- schéma `auth_server`

---

# Pourquoi OAuth2 n'est pas encore implémenté ?

OAuth2 repose sur plusieurs briques métier préalables.

Avant de générer un token, le système doit déjà savoir :

1. retrouver un utilisateur ;
2. vérifier un mot de passe ;
3. vérifier l'état du compte ;
4. connaître les rôles de l'utilisateur ;
5. gérer les autorisations.

Pour cette raison, OAuth2 a été volontairement repoussé après la mise en place du domaine utilisateur.

---

# Roadmap de sécurité

## Étape 1 - Authentification utilisateur

Prochaine étape prévue.

### Objectif

Permettre :

```text
username + password
        ↓
authentification
```

### Éléments à implémenter

- AuthenticateUserUseCase
- PasswordEncoderPort.matches(...)
- vérification du compte utilisateur

### Résultat attendu

Le système doit pouvoir répondre :

```text
Utilisateur authentifié
```

ou

```text
Identifiants invalides
```

---

## Étape 2 - Gestion des rôles

### Objectif

Associer des rôles aux utilisateurs.

### Éléments à implémenter

- Role
- RoleRepositoryPort
- RoleEntity
- RoleMapper
- RoleJpaRepository
- RoleRepositoryAdapter

### Cas d'usage futurs

- assignation d'un rôle
- récupération des rôles d'un utilisateur

---

## Étape 3 - Spring Security

### Objectif

Intégrer le domaine utilisateur avec Spring Security.

### Éléments prévus

- SecurityFilterChain
- UserDetailsService
- gestion de l'authentification HTTP

### Résultat attendu

Le système doit pouvoir protéger des ressources HTTP.

---

## Étape 4 - OAuth2 Authorization Server

### Objectif

Transformer l'application en serveur OAuth2.

### Dépendance envisagée

```
spring-security-oauth2-authorization-server
```

### Fonctionnalités visées

- gestion des clients OAuth2 ;
- génération de tokens ;
- gestion des scopes ;
- gestion des refresh tokens ;
- révocation de tokens.

---

## Étape 5 - OpenID Connect

### Objectif

Ajouter la gestion de l'identité.

### Fonctionnalités visées

- endpoint UserInfo ;
- ID Token ;
- claims standard ;
- authentification OpenID Connect.

---

# Architecture cible

## Domaine

Le domaine métier reste indépendant d'OAuth2.

Le core ne doit pas connaître :

- JWT ;
- OAuth2 ;
- OpenID Connect ;
- Spring Security.

---

## Infrastructure

L'infrastructure est responsable :

- de Spring Security ;
- d'OAuth2 ;
- des tokens ;
- des clients ;
- de la gestion technique des autorisations.

---

# Domaine utilisateur et OAuth2

Le domaine utilisateur constitue la fondation de l'ensemble du système.

## User

Le domaine utilisateur représente :

```text
Qui est l'utilisateur ?
```

---

## Role

Le domaine rôle représente :

```text
Que peut faire l'utilisateur ?
```

---

## OAuth2

OAuth2 représente :

```text
Comment accède-t-il à une ressource ?
```

Cette séparation est volontaire.

---

# Gestion des mots de passe

Le système applique les règles suivantes :

- aucun mot de passe en clair n'est stocké ;
- les mots de passe sont encodés avant persistance ;
- l'algorithme d'encodage est abstrait derrière PasswordEncoderPort.

L'implémentation actuelle repose sur BCrypt.

---

# Gestion des dates

Les dates techniques utilisent :

```
Clock
```

injecté par Spring.

Configuration actuelle :

```
Clock.systemUTC()
```

Cette stratégie sera conservée pour :

- les utilisateurs ;
- les tokens ;
- les refresh tokens ;
- les expirations futures.

---

# Modèle de données envisagé

## Déjà présent

```text
users
roles
users_roles
```

---

## Envisagé pour OAuth2

```text
oauth_clients
oauth_client_scopes
authorizations
authorization_scopes
refresh_tokens
```

Le modèle exact sera défini lorsque la phase OAuth2 débutera.

---

# Principes directeurs

Les principes suivants guideront l'intégration OAuth2 :

1. Le métier reste indépendant du framework.
2. OAuth2 ne doit pas polluer le domaine utilisateur.
3. Les use cases restent simples et testables.
4. Les règles métier restent dans le core.
5. Les détails OAuth2 restent dans l'infrastructure.
6. La sécurité est introduite progressivement.

---

# État de progression

## Réalisé

- architecture hexagonale ;
- domaine utilisateur ;
- persistance PostgreSQL ;
- migration Liquibase ;
- CreateUserUseCase ;
- GetUserUseCase ;
- PasswordEncoderPort ;
- BCrypt.

## En cours

- préparation de AuthenticateUserUseCase.

## Prévu

- gestion des rôles ;
- Spring Security ;
- OAuth2 ;
- OpenID Connect.

---

# Conclusion

OAuth2 constitue l'objectif final du projet, mais n'est volontairement pas la première fonctionnalité développée.

L'approche retenue consiste à construire progressivement :

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

Cette stratégie permet de garder un domaine métier simple, testable et robuste avant d'introduire des mécanismes de sécurité plus avancés.