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
- les contrats HTTP ;
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
         ┌──────────────────────────────────────────────────────┴───────────────┬─────────────────────────────────────┐
         │                                                                      │                                     │
         ▼                                                                      ▼                                     ▼

┌─────────────────┐             ┌─────────────────────┐             ┌──────────────────────┐             ┌─────────────────────────┐
│ auth-server-web ├─────────►   │ auth-server-openapi │             │ auth-server-security │             │ auth-server-persistence │
└────────┬────────┘             └──────────┬──────────┘             └───────────┬──────────┘             └────────────┬────────────┘
         │                                 │                                    │                                     │
         │                                 │                                    │                                     │
         ▼                                 │                                    ▼                                     │
                                           │                                                                          │
┌─────────────────────────┐                │                           ┌─────────────────┐                            │
│ auth-server-application │────────────────┴───────────────────────►   │ auth-server-core│   ◄────────────────────────┘
└─────────────────────────┘                                            └─────────────────┘
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
- ni de Spring Authorization Server ;
- ni d'OpenAPI.

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
- CreateOAuth2ScopeUseCase
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

- orchestration des cas d'usage ;
- gestion des transactions ;
- exposition des services applicatifs ;
- intégration du Core avec Spring.

### Exemples

- UserService
- RoleService
- ApplicationService
- OAuth2ClientService
- OAuth2ScopeService

---

## auth-server-openapi

### Responsabilité

Le module `auth-server-openapi` centralise les contrats HTTP et la documentation OpenAPI du projet.

### Contenu

```text
openapi
├── administration
├── iam
├── authorizationserver
└── common
```

### Responsabilités

- définition des contrats HTTP ;
- documentation OpenAPI ;
- DTO documentaires ;
- réponses documentaires ;
- constantes de documentation ;
- configuration des groupes Swagger.

### Organisation

```text
administration
├── api
├── dto
├── response
├── constants
└── config

iam
├── api
├── dto
├── response
├── constants
└── config

authorizationserver
├── api
├── dto
├── response
├── constants
└── config

common
├── constants
└── config
```

### Principe

Les contrats OpenAPI sont définis dans ce module puis implémentés par les adaptateurs HTTP.

Exemples :

- ApplicationApi
- OAuth2ClientApi
- OAuth2ScopeApi
- AuthorizationServerApi

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
- implémentation des contrats OpenAPI ;
- mapping HTTP ↔ métier ;
- gestion des erreurs API ;
- configuration de la sécurité Web.

### Exemples

- ApplicationController
- OAuth2ClientController
- OAuth2ScopeController
- UserController
- RoleController

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

### Exemples

- UserRepositoryAdapter
- RoleRepositoryAdapter
- ApplicationRepositoryAdapter
- OAuth2ClientRepositoryAdapter
- OAuth2ScopeRepositoryAdapter

---

## auth-server-security

### Responsabilité

Le module `auth-server-security` implémente les besoins de sécurité du système.

### Contenu

```text
security
├── configuration
├── repository
├── adapter
├── authentication
└── authorizationserver
```

### Responsabilités

- authentification ;
- autorisation ;
- configuration Spring Security ;
- configuration OAuth2 Authorization Server ;
- gestion des clients OAuth2 ;
- émission des jetons OAuth2 ;
- implémentation des ports de sécurité du Core.

### Exemples

- SecurityAdapterConfiguration
- OAuth2AuthorizationServerConfiguration
- OAuth2RegisteredClientRepository
- PasswordEncoderAdapter

---

## auth-server-boot

### Responsabilité

Le module `auth-server-boot` joue le rôle de Composition Root.

### Responsabilités

- démarrage de l'application ;
- assemblage des modules ;
- chargement de la configuration Spring ;
- démarrage du conteneur Spring Boot.

### Principe

Le module `auth-server-boot` est le seul module autorisé à connaître simultanément :

- auth-server-web ;
- auth-server-security ;
- auth-server-persistence.

Il assure l'assemblage complet du système.