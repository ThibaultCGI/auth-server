# auth-server-security

## Responsabilité

Le module `auth-server-security` implémente les besoins de sécurité de l'application.

Il adapte Spring Security et Spring Authorization Server aux besoins du Core.

## Position dans l'architecture

```text
auth-server-security
          ↓
auth-server-core

auth-server-security
          ↓
Spring Security

auth-server-security
          ↓
Spring Authorization Server
```

## Contenu

```text
security
├── encoder
├── oauth2
└── userdetails
```

## Responsabilités

- authentification ;
- autorisation ;
- OAuth2 Authorization Server ;
- gestion des utilisateurs Spring Security ;
- génération des identifiants OAuth2 ;
- encodage des mots de passe.

## Dépendances autorisées

- auth-server-core
- Spring Security
- Spring Authorization Server

## Dépendances interdites

- repositories JPA ;
- entités de persistance ;
- accès direct à PostgreSQL.

## Principe

Les composants de sécurité utilisent exclusivement les ports définis dans le Core.

```text
Security
      ↓
Port
      ↑
Persistence
```

Ainsi la sécurité ne dépend jamais directement de la persistance.