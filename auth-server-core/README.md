# auth-server-core

## Responsabilité

Le module `auth-server-core` contient le cœur métier de l'application.

Il définit :

- les objets métier ;
- les règles métier ;
- les use cases ;
- les ports ;
- les exceptions métier.

Ce module est totalement indépendant des frameworks et des technologies techniques.

## Position dans l'architecture

```text
auth-server-web
        ↓
auth-server-application
        ↓
auth-server-core

auth-server-persistence
        ↓
auth-server-core

auth-server-security
        ↓
auth-server-core
```

Le Core est le centre du système.

Tous les autres modules dépendent de lui.

## Contenu

```text
core
├── constants
├── domain
├── exception
├── port
├── usecase
└── utils
```

## Dépendances autorisées

- Lombok

## Dépendances interdites

- Spring
- JPA
- Hibernate
- Spring Security
- OAuth2
- PostgreSQL
- Liquibase

## Principe

Le Core exprime :

> Ce que l'application doit faire.

Il ne connaît jamais :

> Comment cela est fait.