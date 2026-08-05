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

Projet personnel visant à construire un serveur d'authentification OAuth 2.1 / OpenID Connect moderne basé sur Java et Spring.

L'objectif principal est d'approfondir la compréhension des mécanismes d'authentification et d'autorisation tout en mettant en œuvre une architecture hexagonale modulaire, testable et maintenable.

---

# Technologies

- Java 25
- Spring Boot 4
- Spring Security
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

- Comprendre OAuth2
- Comprendre OpenID Connect
- Comprendre Spring Security
- Comprendre Spring Authorization Server
- Approfondir Java 25
- Expérimenter l'architecture hexagonale
- Construire une application fortement modulaire
- Mettre en œuvre les bonnes pratiques de qualité logicielle

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

Cœur métier de l'application.

### Responsabilités

- Domaines métier
- Use cases
- Ports
- Exceptions métier
- Règles métier

### Documentation

```text
auth-server-core/README.md
```

---

## auth-server-application

Couche applicative.

### Responsabilités

- Orchestration des use cases
- Gestion des transactions
- Exposition des services applicatifs
- Configuration Spring des composants métier

### Documentation

```text
auth-server-application/README.md
```

---

## auth-server-openapi

Module de contrats HTTP et de documentation OpenAPI.

### Responsabilités

- Contrats d'API
- DTO documentaires
- Réponses documentaires
- Documentation Swagger
- Groupes OpenAPI
- Documentation OAuth2 Authorization Server

### Documentation

```text
auth-server-openapi/README.md
```

---

## auth-server-web

Adaptateur HTTP entrant.

### Responsabilités

- API REST
- Contrôleurs HTTP
- Implémentation des contrats OpenAPI
- DTO HTTP
- Gestion des erreurs HTTP
- Sécurisation des endpoints Web

### Documentation

```text
auth-server-web/README.md
```

---

## auth-server-persistence

Adaptateur de persistance.

### Responsabilités

- Entités JPA
- Repositories Spring Data
- Implémentations des ports de persistance
- Migrations Liquibase

### Documentation

```text
auth-server-persistence/README.md
```

---

## auth-server-security

Adaptateur de sécurité.

### Responsabilités

- OAuth2 Authorization Server
- Spring Security
- UserDetailsService
- PasswordEncoder
- Génération des identifiants OAuth2
- Gestion des tokens OAuth2
- Intégration Spring Security

### Documentation

```text
auth-server-security/README.md
```

---

## auth-server-boot

Point d'entrée de l'application.

### Responsabilités

- Démarrage Spring Boot
- Assemblage des modules
- Composition Root

### Documentation

```text
auth-server-boot/README.md
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

La documentation du projet est disponible dans :

```text
docs/
```

Par exemple :

```text
docs/
├── architecture.md
├── decisions.md
├── dependencies.md
├── diagrams
└── ...
```

---

# Qualité

Le projet est développé avec une forte attention portée à :

- la couverture de tests ;
- le découpage modulaire ;
- la lisibilité du code ;
- la maintenabilité ;
- l'analyse continue avec SonarCloud.

---

# État du projet

Projet en cours de développement.

## Fonctionnalités disponibles

- Gestion des utilisateurs
- Gestion des rôles
- Gestion des applications
- Gestion des scopes OAuth2
- Gestion des clients OAuth2
- OAuth2 Client Credentials Flow
- API REST sécurisée
- Documentation OpenAPI centralisée
- Swagger découpés par domaine fonctionnel
- Module dédié `auth-server-openapi`
- Persistance PostgreSQL
- Gestion des migrations Liquibase

## Fonctionnalités envisagées

- Bean Validation
- OpenID Connect
- Audit
- Révocation de jetons
- Consentement utilisateur
- Administration avancée

---

# Licence

Projet personnel à vocation pédagogique et expérimentale.