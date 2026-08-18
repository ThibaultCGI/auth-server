# OAuth2 et OpenID Connect

## Objectif

Ce document décrit l'implémentation OAuth 2.1 et OpenID Connect actuellement présente dans le projet.

Le projet fournit un serveur d'autorisation moderne basé sur :

- OAuth 2.1
- OpenID Connect 1.0
- Spring Authorization Server
- Spring Security

---

# Vue d'ensemble

Le projet agit comme un Authorization Server et un OpenID Provider.

Il est responsable :

- de l'authentification des utilisateurs ;
- de l'authentification des clients OAuth2 ;
- de la validation des scopes ;
- de l'émission des Access Tokens ;
- de l'émission des Refresh Tokens ;
- de l'émission des ID Tokens ;
- de la gestion des autorisations ;
- de l'exposition des endpoints OAuth2 et OIDC.

---

# Architecture

## Modules impliqués

```text
auth-server-security
        │
        ▼
auth-server-core

auth-server-persistence
        │
        ▼
auth-server-core
```

Les composants OAuth2 et OIDC sont regroupés dans :

```text
auth-server-security
└── oauth2
```

La persistance des données OAuth2 est réalisée dans :

```text
auth-server-persistence
```

---

# Flows supportés

Le projet supporte actuellement :

```text
Client Credentials
Authorization Code + PKCE
Refresh Token
OpenID Connect
```

---

# Flow Client Credentials

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

Retour de l'Access Token
```

---

# Flow Authorization Code + PKCE

## Principe

```text
OAuth2 Client
        │
        ▼

GET /oauth2/authorize

        │
        ▼

Authentification utilisateur

        │
        ▼

Authorization Code

        │
        ▼

POST /oauth2/token

        │
        ▼

Access Token
+
Refresh Token
+
ID Token (OIDC)

        │
        ▼

Retour au client
```

---

# Flow Refresh Token

## Principe

```text
OAuth2 Client
        │
        ▼

Refresh Token

        │
        ▼

POST /oauth2/token

grant_type=refresh_token

        │
        ▼

Validation du Refresh Token

        │
        ▼

Nouvel Access Token

        │
        ▼

Retour au client
```

---

## Cas d'usage

Ce flow permet à un client OAuth2 d'obtenir un nouvel Access Token sans réauthentifier l'utilisateur.

Il est disponible uniquement pour les clients disposant simultanément des grant types :

```text
authorization_code
refresh_token
```

---

## Validation métier

Le grant type :

```text
refresh_token
```

nécessite également la présence du grant type :

```text
authorization_code
```

La configuration suivante est invalide :

```text
refresh_token
```

---

## Renouvellement automatique

Les clients Spring Security configurés avec OAuth2 Client peuvent utiliser automatiquement le Refresh Token lorsque l'Access Token expire.

Le renouvellement est alors transparent pour l'utilisateur.

Scénario validé :

```text
Access Token expiré
        │
        ▼

Refresh Token

        │
        ▼

Nouvel Access Token

        │
        ▼

Mise à jour automatique
du OAuth2AuthorizedClient
```

---

# OpenID Connect

Le projet expose les principales fonctionnalités OIDC.

## Scope OpenID

Le scope suivant est supporté :

```text
openid
```

Sa présence active les mécanismes OpenID Connect.

---

## ID Token

Lorsqu'un client demande le scope :

```text
openid
```

un ID Token JWT est généré.

Exemple de claims :

```json
{
  "sub": "admin",
  "iss": "http://auth-server.local:8080",
  "aud": [
    "client-id"
  ]
}
```

---

## OIDC Discovery

Le serveur expose :

```text
/.well-known/openid-configuration
```

Permettant aux clients de découvrir automatiquement :

- l'issuer ;
- l'endpoint d'autorisation ;
- l'endpoint de token ;
- le JWKS endpoint ;
- les informations OIDC.

---

## JWKS

Le serveur expose les clés publiques via :

```text
/oauth2/jwks
```

Les clients peuvent ainsi vérifier cryptographiquement les JWT et les ID Tokens.

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

Spring Authorization Server utilise :

```text
RegisteredClientRepository
```

Le projet fournit :

```text
OAuth2RegisteredClientRepository
```

Cette implémentation utilise les ports métier :

```text
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

```text
OAuth2ClientCredentialsGeneratorPort
```

L'implémentation actuelle est :

```text
OAuth2ClientCredentialsGeneratorAdapter
```

---

## Génération du client_id

Format actuel :

```text
XXXXXX-XXXXXX-XXXXXX-XXXXXX
```

---

## Génération du client_secret

Le secret est généré aléatoirement puis encodé avant persistance.

---

# Gestion des scopes

Les scopes sont définis au niveau métier.

Exemples :

```text
trs:produit-api.read
trs:produit-api.write
openid
```

Les scopes métier sont associés à une application :

```text
trs:produit-api.read
```

Le scope système :

```text
openid
```

est utilisé pour activer OpenID Connect.

---

# JWT

Le projet utilise Spring Authorization Server pour générer :

- les Access Tokens ;
- les Refresh Tokens ;
- les ID Tokens.

---

## Personnalisation des tokens

Le composant :

```text
OAuth2JwtCustomizer
```

permet d'enrichir les JWT avec des claims spécifiques.

Exemple :

```text
application_code
client_id
```

---

## Signature

Les tokens sont signés en RSA à l'aide d'une clé stockée dans un keystore PKCS12.

Le serveur supporte :

- plusieurs clés RSA simultanément ;
- un JWKS multi-clés ;
- une clé active configurable ;
- la rotation progressive des clés cryptographiques.

Exemple :

```text
auth-server.p12
├── old-key
└── auth-server
```

Configuration :

```properties
jwt.keystore.active-alias=auth-server
```

---

## JWKS multi-clés

Le endpoint :

```text
/oauth2/jwks
```

peut exposer plusieurs clés simultanément.

Exemple :

```json
{
  "keys": [
    {
      "kid": "old-key"
    },
    {
      "kid": "auth-server"
    }
  ]
}
```

---

# Token Settings

Les durées de vie des tokens sont configurées explicitement via :

```text
TokenSettings
```

---

## Durées de vie

Configuration actuelle :

```text
Access Token  : 5 minutes
Refresh Token : 30 jours
```

---

## Réutilisation des Refresh Tokens

Configuration actuelle :

```text
reuseRefreshTokens = true
```

Le Refresh Token reste valide après son utilisation et peut être réutilisé jusqu'à expiration.

Exemple :

```text
Refresh Token
        │
        ▼
Utilisation

        │
        ▼
Toujours valide
```

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

La couche sécurité dépend uniquement des ports :

```text
OAuth2ClientRepositoryPort
OAuth2ScopeRepositoryPort
```

---

## Séparation Security / Persistence

Le module :

```text
auth-server-security
```

ne dépend pas directement du module :

```text
auth-server-persistence
```

Le découplage est assuré via les ports métier.

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

✅ Authentification des utilisateurs

✅ Client Credentials Flow

✅ Authorization Code Flow

✅ PKCE

✅ Refresh Tokens

✅ Grant Type Refresh Token

✅ Renouvellement automatique des Access Tokens

✅ OpenID Connect

✅ Discovery Endpoint

✅ JWKS Endpoint

✅ Access Tokens JWT

✅ Refresh Tokens

✅ ID Tokens JWT

✅ Keystore PKCS12

✅ JWKS multi-clés

✅ Clé active configurable

✅ TokenSettings explicites

---

# Évolutions envisagées

## OAuth2

- Token Revocation
- Token Introspection
- Rotation des Refresh Tokens

## OpenID Connect

- UserInfo Endpoint
- Claims personnalisées enrichies
- Logout OIDC

## Administration

- Gestion avancée des clients
- Rotation des secrets
- Rotation automatique des clés cryptographiques
- Audit des autorisations

---

# Conclusion

Le projet dispose désormais d'une implémentation fonctionnelle d'un Authorization Server OAuth 2.1 et OpenID Connect basée sur Spring Authorization Server.

Les fonctionnalités supportées incluent notamment :

- Client Credentials ;
- Authorization Code + PKCE ;
- Refresh Tokens ;
- OpenID Connect ;
- JWT signés en RSA ;
- JWKS multi-clés ;
- keystore PKCS12 ;
- renouvellement automatique des Access Tokens.

L'architecture hexagonale retenue permet d'isoler complètement les règles métier des choix techniques tout en intégrant les mécanismes OAuth2/OIDC modernes attendus d'un serveur d'autorisation.
