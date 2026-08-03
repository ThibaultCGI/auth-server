# auth-server-persistence

## Responsabilité

Le module `auth-server-persistence` implémente les ports de persistance définis par le Core.

Il est responsable de l'accès aux données.

## Position dans l'architecture

```text
auth-server-persistence
          ↓
auth-server-core
```

## Contenu

```text
persistence
├── adapter
├── entity
├── mapper
├── repository
└── resources
    └── db
        └── changelog
```

## Responsabilités

- stockage des données ;
- récupération des données ;
- mapping domaine ↔ JPA ;
- gestion du schéma via Liquibase.

## Dépendances autorisées

- auth-server-core
- Spring Data JPA
- PostgreSQL
- Liquibase

## Structure

### adapter

Implémentation des ports du Core.

```text
UserRepositoryPort
        ↓
UserRepositoryAdapter
```

### repository

Repositories Spring Data JPA.

### entity

Entités techniques JPA.

### mapper

Conversions :

```text
Domain ↔ Entity
```

## Principe

Le Core ne connaît jamais :

- JPA ;
- PostgreSQL ;
- Liquibase ;
- les entités techniques.