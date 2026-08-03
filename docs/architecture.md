# Architecture

## Objectif

Le projet `auth-server` vise à construire un serveur d'authentification moderne basé sur :

- Java 25
- Spring Boot 4
- Spring Security
- Spring Authorization Server
- PostgreSQL
- Liquibase

L'architecture retenue est une architecture hexagonale (Ports & Adapters) permettant de séparer clairement :

- le métier ;
- les cas d'utilisation ;
- les services applicatifs ;
- les adaptateurs techniques ;
- la persistance ;
- la sécurité ;
- la configuration Spring.

---

# Vue d'ensemble

```text
                ┌────────────────────┐
                │ auth-server-boot   │
                └──────────┬─────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼

auth-server-web   auth-server-security   auth-server-persistence
        │                  │                  │
        └──────────┬───────┴──────────┬───────┘
                   ▼                  ▼

            auth-server-application
                        │
                        ▼

                 auth-server-core
```

---

# Découpage des modules

## auth-server-core

### Responsabilité

Le module `auth-server-core` contient le cœur métier de l'application.

Il ne dépend :

- ni de Spring ;
- ni de JPA ;
- ni de PostgreSQL ;
- ni de Spring Security ;
- ni d'OAuth2.

### Contenu

```text
core
├── domain
├── port
├── usecase
├── exception
├── constants
└── utils
```

### Exemples

#### Domain

- User
- Role
- Application
- OAuth2Client
- OAuth2Scope

#### Ports

- UserRepositoryPort
- RoleRepositoryPort
- ApplicationRepositoryPort
- OAuth2ClientRepositoryPort
- OAuth2ScopeRepositoryPort
- PasswordEncoderPort
- OAuth2ClientCredentialsGeneratorPort

#### Use cases

- CreateUserUseCase
- AssignRoleToUserUseCase
- CreateApplicationUseCase
- CreateOAuth2ClientUseCase
- AuthenticateUserUseCase

---

## auth-server-application

### Responsabilité

Le module `auth-server-application` orchestre l'exécution des use cases.

Il constitue la couche applicative.

### Contenu

```text
application
├── service
└── config
```

### Responsabilités

- gestion des transactions ;
- orchestration des cas d'usage ;
- exposition de services applicatifs ;
- intégration du Core avec Spring.

### Exemples

- UserService
- RoleService
- ApplicationService
- OAuth2ClientService
- OAuth2ScopeService

---

## auth-server-web

### Responsabilité

Le module `auth-server-web` expose les fonctionnalités via une API REST.

### Contenu

```text
web
├── controller
├── dto
├── response
├── mapper
├── error
└── security
```

### Responsabilités

- endpoints REST ;
- validation des requêtes ;
- mapping HTTP ↔ métier ;
- gestion des erreurs API ;
- configuration de la sécurité Web.

---

## auth-server-persistence

### Responsabilité

Le module `auth-server-persistence` implémente les ports de persistance définis dans le Core.

### Contenu

```text
persistence
├── entity
├── repository
├── adapter
├── mapper
└── db/changelog
```

### Responsabilités

- persistance PostgreSQL ;
- entités JPA ;
- repositories Spring Data ;
- implémentations des ports ;
- migrations Liquibase.

---

## auth-server-security

### Responsabilité

Le module `auth-server-security` implémente les besoins de sécurité