# Auth Server

[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ThibaultCGI_auth-server&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=ThibaultCGI_auth-server)

Projet personnel visant à construire un serveur d'autorisation moderne basé sur :

- OAuth 2.1
- OpenID Connect 1.0
- Spring Authorization Server
- Spring Security

L'objectif est d'approfondir la compréhension des mécanismes d'authentification et d'autorisation tout en appliquant les principes de l'architecture hexagonale à une application fortement modulaire, testable et maintenable.

Le projet agit comme :

- OAuth2 Authorization Server ;
- OpenID Provider (OIDC).

---

# Technologies

- Java 25
- Spring Boot 4.1
- Spring Security 7.1
- Spring Authorization Server
- SpringDoc OpenAPI
- PostgreSQL
- Liquibase
- Maven
- JUnit 5
- Mockito
- SonarCloud

---

# Objectifs du projet

- Comprendre OAuth 2.1
- Comprendre OpenID Connect
- Comprendre Spring Security
- Comprendre Spring Authorization Server
- Approfondir Java 25
- Expérimenter l'architecture hexagonale
- Construire une application fortement modulaire
- Mettre en œuvre les bonnes pratiques de qualité logicielle

---

# Fonctionnalités implémentées

## Gestion métier

- Gestion des utilisateurs
- Gestion des rôles
- Gestion des applications
- Gestion des scopes OAuth2
- Gestion des clients OAuth2

## OAuth2

- Authorization Code Flow
- Client Credentials Flow
- PKCE
- Validation des clients OAuth2
- Gestion des scopes
- Génération d'Access Tokens JWT
- Signature RSA des JWT

## OpenID Connect

- Scope `openid`
- Émission d'ID Tokens
- Discovery Endpoint
- Endpoint JWKS
- Authentification utilisateur
- Intégration avec des clients OIDC Spring Security

## Infrastructure

- PostgreSQL
- Liquibase
- OpenAPI
- Swagger UI
- Architecture multi-modules
- Architecture hexagonale
- Analyse continue SonarCloud
- Couverture de tests

---

# Écosystème

Le projet est actuellement validé à l'aide d'un client OpenID Connect de démonstration.

```text
+----------------------+
| test-oauth2-client   |
| localhost:8082       |
+----------+-----------+
           |
           | OIDC
           |
           ▼
+----------------------+
| auth-server          |
| localhost:8080       |
+----------------------+
```

Le client utilise :

- Authorization Code Flow
- PKCE
- OpenID Connect
- Discovery Endpoint
- JWKS Endpoint

Cette intégration permet de valider l'interopérabilité du serveur avec un client OIDC réel.

---

# Endpoints standards exposés

## OAuth2

```text
/oauth2/authorize
/oauth2/token
```

## OpenID Connect

```text
/.well-known/openid-configuration
/oauth2/jwks
```

## Authentification utilisateur

```text
/login
```

---

# Architecture

Le projet est organisé selon les principes de l'architecture hexagonale (Ports & Adapters).

```text
                                                     ┌────────────────────┐
                                                     │ auth-server-boot   │
                                                     └──────────┬─────────┘
                                                                │
         ┌──────────────────────────────────────────────────────┴──────────────┬──────────────────────────────────────┐
         │                                                                     │                                      │
         ▼                                                                     ▼                                      ▼

┌─────────────────┐             ┌─────────────────────┐             ┌──────────────────────┐             ┌─────────────────────────┐
│ auth-server-web ├──────────►  │ auth-server-openapi │             │ auth-server-security │             │ auth-server-persistence │
└────────┬────────┘             └──────────┬──────────┘             └──────────┬───────────┘             └────────────┬────────────┘
         │                                 │                                   │                                      │
         ▼                                 │                                   ▼                                      │
                                           │                                                                          │
┌─────────────────────────┐                │                           ┌─────────────────┐                            │
│ auth-server-application │────────────────┴────────────────────────►  │ auth-server-core│  ◄─────────────────────────┘
└─────────────────────────┘                                            └─────────────────┘
```

---

# Modules

## auth-server-core

Responsable du domaine métier.

- Domaines
- Use cases
- Ports
- Exceptions métier
- Règles métier

Documentation :

```text
auth-server-core/README.md
```

---

## auth-server-application

Responsable de la couche applicative.

- Orchestration des cas d'usage
- Gestion transactionnelle
- Services applicatifs
- Configuration Spring métier

Documentation :

```text
auth-server-application/README.md
```

---

## auth-server-openapi

Responsable du contrat HTTP.

- Documentation OpenAPI
- Documentation OAuth2
- Documentation OpenID Connect
- Contrats REST
- DTO documentaires

Documentation :

```text
auth-server-openapi/README.md
```

---

## auth-server-web

Adaptateur HTTP entrant.

- Contrôleurs REST
- Gestion des erreurs
- Implémentation des contrats OpenAPI
- Mapping HTTP ↔ métier

Documentation :

```text
auth-server-web/README.md
```

---

## auth-server-persistence

Adaptateur de persistance.

- JPA
- PostgreSQL
- Repositories
- Liquibase

Documentation :

```text
auth-server-persistence/README.md
```

---

## auth-server-security

Adaptateur de sécurité.

- Spring Security
- Spring Authorization Server
- OpenID Connect
- JWT
- JWKS
- UserDetailsService
- OAuth2RegisteredClientRepository

Documentation :

```text
auth-server-security/README.md
```

---

## auth-server-boot

Composition Root.

- Démarrage de l'application
- Assemblage des modules
- Configuration Spring

Documentation :

```text
auth-server-boot/README.md
```

---

# Flows supportés

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

# Principes d'architecture

Le projet suit les règles suivantes :

- Le Core ne dépend d'aucun framework.
- Le métier ne connaît pas Spring.
- Le métier ne connaît pas JPA.
- Le métier ne connaît pas Spring Security.
- Le métier ne connaît pas OpenAPI.
- Les accès aux données passent par des ports.
- Les implémentations techniques sont fournies par des adaptateurs.
- Les contrats HTTP sont centralisés dans le module OpenAPI.
- Le module Boot est le seul autorisé à assembler l'ensemble des composants.

---

# Documentation

La documentation technique est disponible dans :

```text
docs/
├── architecture.md
├── decisions.md
├── dependencies.md
├── oauth2.md
├── oidc.md
└── diagrams/
```

---

# Qualité

Le projet est développé avec une attention particulière portée à :

- la couverture de tests ;
- le découpage modulaire ;
- la lisibilité du code ;
- la maintenabilité ;
- l'analyse continue avec SonarCloud.

---

# État du projet

Projet en développement actif.

## Fonctionnalités disponibles

- Gestion des utilisateurs
- Gestion des rôles
- Gestion des applications
- Gestion des scopes OAuth2
- Gestion des clients OAuth2
- Client Credentials Flow
- Authorization Code Flow
- PKCE
- OpenID Connect
- Access Tokens JWT
- ID Tokens JWT
- Discovery Endpoint
- JWKS Endpoint
- OpenAPI
- Swagger
- PostgreSQL
- Liquibase

## Fonctionnalités envisagées

### Court terme

- Refresh Tokens
- Révocation de jetons
- Consentement utilisateur
- Audit

### Long terme

- UserInfo Endpoint personnalisé
- OIDC Logout
- Rotation des clés cryptographiques
- Federation / Social Login

---

# Licence

Projet personnel à vocation pédagogique et expérimentale.
