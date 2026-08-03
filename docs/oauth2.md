# OAuth2

## Objectif

Ce document décrit l'implémentation OAuth2 actuellement présente dans le projet ainsi que les évolutions envisagées.

Le projet a pour objectif de fournir un serveur d'autorisation moderne basé sur :

- OAuth 2.1
- OpenID Connect (à terme)
- Spring Authorization Server
- Spring Security

---

# Vue d'ensemble

Le projet agit comme un Authorization Server.

Il est responsable :

- de l'authentification des utilisateurs ;
- de l'authentification des clients OAuth2 ;
- de la validation des scopes ;
- de l'émission des tokens ;
- de la gestion des autorisations.

---

# Architecture

## Modules impliqués

```text
auth-server-security
        ↓
auth-server-core

auth-server-persistence
        ↓
auth-server-core
```

Les composants OAuth2 sont regroupés dans :

```text
auth-server-security
└── oauth2
```

La persistance des données OAuth2 est réalisée dans :

```text
auth-server-persistence
```

---

# Flux Client Credentials

Le projet supporte actuellement le flux :

```text
Client Credentials
```

## Principe

```text
OAuth2 Client
        │
        ▼

POST /oauth2/token

        │
        ▼

Validation du client

        │
        ▼

Validation des scopes

        │
        ▼

Génération du JWT

        │
        ▼

Retour du token d'accès
```

---

# Gestion des clients OAuth2

## Domaine

Le domaine métier contient :

```text
OAuth2Client
OAuth2Scope
```

---

## Cas d'usage

Exemples :

```text
CreateOAuth2ClientUseCase
GetOAuth2ClientUseCase

CreateOAuth2ScopeUseCase
GetOAuth2ScopeUseCase

AssignOAuth2ScopesToOAuth2ClientUseCase
```

---

## Persistance

Les données OAuth2 sont stockées dans PostgreSQL.

Tables principales :

```text
oauth2_client
oauth2_scope
oauth2_client_scope
```

Les migrations sont gérées par Liquibase.

---

# RegisteredClientRepository

Spring Authorization Server utilise l'interface :

```
RegisteredClientRepository
```

Le projet fournit l'implémentation :

```
OAuth2RegisteredClientRepository
```

Cette implémentation utilise les ports métier :

```
OAuth2ClientRepositoryPort
OAuth2ScopeRepositoryPort
```

et ne dépend pas directement de JPA.

## Schéma

```text
Spring Authorization Server
            │
            ▼

OAuth2RegisteredClientRepository
            │
            ▼

OAuth2ClientRepositoryPort
OAuth2ScopeRepositoryPort
            │
            ▼

Adapters de persistance
```

---

# Génération des identifiants OAuth2

La génération des identifiants techniques est abstraite derrière :

```
OAuth2ClientCredentialsGeneratorPort
```

L'implémentation actuelle est :

```
OAuth2ClientCredentialsGeneratorAdapter
```

## Génération du client_id

Format actuel :

```text
XXXXXX-XXXXXX-XXXXXX-XXXXXX
```

où chaque caractère est aléatoire.

## Génération du client_secret

Le secret est généré aléatoirement puis encodé avant persistance.

---

# Gestion des scopes

Les scopes sont définis au niveau métier.

Exemple :

```text
trs:produit-api.read
trs:produit-api.write
```

Chaque scope est :

- associé à une application ;
- stocké en base ;
- attribué explicitement à des clients OAuth2.

---

# JWT

Le projet utilise :

```text
Spring Authorization Server
```

pour la génération des tokens.

Un composant dédié :

```
OAuth2JwtCustomizer
```

permet d'enrichir le contenu des tokens.

---

# Principes architecturaux

## Le Core ne connaît pas OAuth2

Le module :

```text
auth-server-core
```

ne connaît pas :

- Spring Security ;
- Spring Authorization Server ;
- JWT ;
- OAuth2 ;
- OpenID Connect.

---

## OAuth2 repose sur les ports métier

La sécurité dépend des ports du Core :

```
OAuth2ClientRepositoryPort
OAuth2ScopeRepositoryPort
```

et non des repositories JPA.

---

## Séparation Security / Persistence

Le module :

```text
auth-server-security
```

ne dépend pas du module :

```text
auth-server-persistence
```

Le découplage est réalisé via les ports métier.

---

# État actuel

## Fonctionnel

✅ Gestion des utilisateurs

✅ Gestion des rôles

✅ Gestion des applications

✅ Gestion des clients OAuth2

✅ Gestion des scopes OAuth2

✅ Attribution de scopes à un client

✅ Authentification des clients OAuth2

✅ Flux Client Credentials

✅ Génération de JWT

---

# Évolutions envisagées

## OAuth2

- Refresh Tokens
- Token Revocation
- Token Introspection

## OpenID Connect

- ID Token
- UserInfo Endpoint
- Standard Claims
- Discovery Endpoint

## Administration

- Gestion avancée des clients
- Rotation des secrets
- Audit des autorisations

---

# Conclusion

Le projet dispose désormais d'une première implémentation fonctionnelle d'un Authorization Server OAuth2 basé sur Spring Authorization Server.

L'architecture retenue permet de conserver un cœur métier indépendant tout en intégrant les mécanismes OAuth2 dans un module de sécurité d