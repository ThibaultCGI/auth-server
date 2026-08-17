# Architecture

## Objectif

Le projet `auth-server` vise à construire un serveur d'autorisation moderne basé sur :

- Java 25
- Spring Boot 4
- Spring Security 7
- Spring Authorization Server
- OpenID Connect
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
         ▼                                 │                                    ▼                                     │
                                           │                                                                          │
┌─────────────────────────┐                │                           ┌─────────────────┐                            │
│ auth-server-application │────────────────┴───────────────────────►   │ auth-server-core│   ◄────────────────────────┘
└─────────────────────────┘                                            └─────────────────┘
```

---

# Rôle du système

Le projet agit comme :

```text
OAuth2 Authorization Server
+
OpenID Provider (OIDC)
```

Il est responsable :

- de l'authentification des utilisateurs ;
- de l'authentification des clients OAuth2 ;
- de la gestion des scopes ;
- de l'émission des Access Tokens ;
- de l'émission des ID Tokens ;
- de l'exposition des endpoints OAuth2 ;
- de l'exposition des endpoints OpenID Connect.

---

# Flows supportés

## Client Credentials

```text
Client
   │
   ▼

/oauth2/token

   │
   ▼

Access Token
```

---

## Authorization Code + PKCE

```text
Client
   │
   ▼

/oauth2/authorize

   │
   ▼

Authentification utilisateur

   │
   ▼

Authorization Code

   │
   ▼

/oauth2/token

   │
   ▼

Access Token
+
ID Token
```

---

# Écosystème

Le projet peut être utilisé avec un client OpenID Connect.

Exemple :

```text
+----------------------+
| test-oauth2-client   |
+----------+-----------+
           |
           | OIDC
           |
           ▼
+----------------------+
| auth-server          |
+----------------------+
```

---

# Découpage des modules

## auth-server-core

### Responsabilité

Le module `auth-server-core` contient le cœur métier.

Il ne dépend :

- ni de Spring ;
- ni de Spring Security ;
- ni de Spring Authorization Server ;
- ni de JPA ;
- ni de PostgreSQL ;
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

### Domaines métier

- User
- Role
- Application
- OAuth2Client
- OAuth2Scope

### Ports

- UserRepositoryPort
- RoleRepositoryPort
- ApplicationRepositoryPort
- OAuth2ClientRepositoryPort
- OAuth2ScopeRepositoryPort
- PasswordEncoderPort
- OAuth2ClientCredentialsGeneratorPort

### Cas d'usage

- CreateUserUseCase
- AssignRoleToUserUseCase
- CreateApplicationUseCase
- CreateOAuth2ClientUseCase
- CreateOAuth2ScopeUseCase
- AuthenticateUserUseCase

---

## auth-server-application

### Responsabilité

Le module `auth-server-application` orchestre l'exécution des cas d'utilisation.

### Contenu

```text
application
├── service
└── config
```

### Responsabilités

- orchestration du métier ;
- gestion transactionnelle ;
- exposition des services applicatifs ;
- intégration Spring du Core.

---

## auth-server-openapi

### Responsabilité

Le module `auth-server-openapi` centralise les contrats HTTP et la documentation OpenAPI.

### Contenu

```text
openapi
├── administration
├── iam
├── authorizationserver
└── common
```

### Responsabilités

- contrats HTTP ;
- documentation Swagger ;
- documentation OAuth2 ;
- documentation OpenID Connect ;
- DTO documentaires.

---

## auth-server-web

### Responsabilité

Adaptateur HTTP entrant.

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

- contrôleurs REST ;
- implémentation des contrats OpenAPI ;
- mapping HTTP ↔ métier ;
- gestion des erreurs ;
- intégration Web.

---

## auth-server-persistence

### Responsabilité

Adaptateur de persistance.

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

- JPA ;
- PostgreSQL ;
- repositories Spring Data ;
- implémentations des ports ;
- Liquibase.

---

## auth-server-security

### Responsabilité

Adaptateur de sécurité.

### Contenu

```text
security
├── encoder
├── oauth2
├── userdetails
└── ...
```

### Responsabilités

- Spring Security ;
- OAuth2 Authorization Server ;
- OpenID Connect ;
- JWT ;
- JWKS ;
- gestion des sessions ;
- gestion des clients OAuth2 ;
- gestion des utilisateurs.

### Endpoints exposés

```text
/oauth2/authorize
/oauth2/token
/oauth2/jwks

/.well-known/openid-configuration
```

### SecurityFilterChains

#### Order 1

Authorization Server OAuth2 / OIDC

```text
/oauth2/**
/.well-known/**
```

#### Order 2

Authentification utilisateur

```text
/login
```

#### Order 3

API REST

```text
/**
```

---

## auth-server-boot

### Responsabilité

Composition Root du système.

### Responsabilités

- démarrage Spring Boot ;
- assemblage des modules ;
- chargement de la configuration ;
- exposition de l'application.

### Principe

Le module `auth-server-boot` est le seul module autorisé à connaître simultanément :

- auth-server-web ;
- auth-server-security ;
- auth-server-persistence ;
- auth-server-application.

---

# Dépendances

## Règle fondamentale

Le métier ne dépend d'aucune technologie.

```text
Core
 ▲
 │
 │
Application
Web
Security
Persistence
OpenAPI
```

Les dépendances pointent toujours vers le Core.

---

# Principes architecturaux

## Ports & Adapters

Les accès techniques sont réalisés via des ports :

```text
Core
  │
  ▼

Port

  ▲
  │

Adapter
```

---

## Séparation métier / technique

Le métier ignore :

- Spring ;
- JPA ;
- PostgreSQL ;
- Spring Security ;
- OpenID Connect ;
- OAuth2.

---

## Séparation Security / Persistence

Le module Security ne dépend jamais directement de Persistence.

```text
Security
   │
   ▼

Repository Port

   ▲
   │

Persistence Adapter
```

---

# Sécurité

Le projet met en œuvre :

✅ OAuth 2.1

✅ OpenID Connect

✅ Authorization Code Flow

✅ PKCE

✅ Client Credentials Flow

✅ JWT

✅ Signature RSA

✅ Endpoint JWKS

✅ Discovery Endpoint

✅ Sessions Spring Security

✅ Authentification utilisateur

✅ Authentification des clients OAuth2

---

# Conclusion

Le projet repose sur une architecture hexagonale modulaire dans laquelle les préoccupations métier, applicatives et techniques sont clairement séparées.

Cette organisation permet de construire un Authorization Server OAuth2/OpenID Connect moderne tout en conservant un cœur métier indépendant des frameworks et des choix d'infrastructure.
