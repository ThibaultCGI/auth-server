# auth-server-security

## Responsabilité

Le module `auth-server-security` implémente les mécanismes de sécurité de l'application.

Il adapte Spring Security et Spring Authorization Server aux besoins métier exposés par le Core tout en respectant les principes de l'architecture hexagonale.

Le module est responsable de :

- l'authentification des utilisateurs ;
- l'authentification des clients OAuth2 ;
- l'autorisation des accès ;
- l'implémentation de l'Authorization Server OAuth2 ;
- l'implémentation du fournisseur OpenID Connect (OpenID Provider) ;
- la génération et la personnalisation des tokens JWT ;
- la gestion des clés cryptographiques ;
- l'intégration de Spring Security avec le domaine métier.

---

# Position dans l'architecture

```text
auth-server-security
          │
          ▼
auth-server-core

auth-server-security
          │
          ▼
Spring Security

auth-server-security
          │
          ▼
Spring Authorization Server
```

Le module joue le rôle d'adaptateur sortant vers les frameworks de sécurité.

---

# Contenu

```text
security
├── encoder
├── oauth2
├── properties
├── userdetails
└── ...
```

---

# Responsabilités

## Authentification utilisateur

Le module fournit :

- l'intégration Spring Security ;
- le chargement des utilisateurs ;
- la gestion du formulaire de connexion ;
- la gestion des sessions d'authentification.

---

## OAuth2 Authorization Server

Le module expose les endpoints OAuth2 standards :

```text
/oauth2/authorize
/oauth2/token
```

Il est responsable :

- de la validation des clients OAuth2 ;
- de la gestion des scopes ;
- de la génération des Authorization Codes ;
- de l'émission des Access Tokens.

---

## OpenID Connect

Le module expose les fonctionnalités OpenID Connect :

```text
/.well-known/openid-configuration
/oauth2/jwks
```

Il prend en charge :

- le scope `openid` ;
- la génération des ID Tokens ;
- la découverte automatique du fournisseur OIDC ;
- l'exposition des clés publiques JWT.

---

## Gestion des JWT

Le module :

- génère les Access Tokens ;
- génère les ID Tokens ;
- signe les JWT en RSA ;
- personnalise les claims via `OAuth2JwtCustomizer` ;
- charge les clés de signature depuis un keystore PKCS12 ;
- sélectionne explicitement la clé active utilisée pour signer les nouveaux JWT.

---

## Gestion des clients OAuth2

Spring Authorization Server repose sur :

```text
RegisteredClientRepository
```

Le projet fournit :

```text
OAuth2RegisteredClientRepository
```

Cette implémentation s'appuie sur les ports métier du Core et non sur des repositories JPA.

---

## Encodage des mots de passe

Le module fournit les composants nécessaires à :

- l'encodage des mots de passe ;
- la vérification des mots de passe ;
- l'intégration avec Spring Security.

---

# Security Filter Chains

Le projet utilise plusieurs `SecurityFilterChain` spécialisées.

## Order 1

Authorization Server OAuth2 / OIDC.

Endpoints concernés :

```text
/oauth2/**
/.well-known/**
```

Responsabilités :

- Authorization Code Flow ;
- Client Credentials Flow ;
- OpenID Connect ;
- émission des tokens.

---

## Order 2

Authentification utilisateur.

Endpoints concernés :

```text
/login
```

Responsabilités :

- affichage du formulaire d'authentification ;
- traitement du login utilisateur.

---

## Order 3

API REST et endpoints applicatifs.

Responsabilités :

- contrôle d'accès ;
- HTTP Basic ;
- gestion des erreurs de sécurité ;
- sécurisation des APIs.

---

# Dépendances autorisées

- auth-server-core
- Spring Security
- Spring Authorization Server

---

# Dépendances interdites

- repositories JPA ;
- entités de persistance ;
- accès direct à PostgreSQL ;
- dépendance vers `auth-server-persistence`.

---

# Principe d'architecture

Les composants de sécurité utilisent exclusivement les ports définis par le Core.

```text
Security
      │
      ▼

Ports métier

      ▲
      │

Persistence
```

Ainsi le module de sécurité reste totalement indépendant des choix techniques de persistance.

---

# OAuth2

## Flows supportés

Le module supporte actuellement :

```text
Client Credentials
Authorization Code + PKCE
```

---

## Scopes

Les scopes OAuth2 sont récupérés depuis le domaine métier.

Exemples :

```text
trs:produit-api.read
trs:produit-api.write
```

---

# OpenID Connect

## Scope système

Le scope suivant est traité de manière spécifique :

```text
openid
```

Sa présence active les fonctionnalités OpenID Connect.

---

## ID Token

Lorsqu'un client demande :

```text
scope=openid
```

le module génère un ID Token contenant les claims de l'utilisateur authentifié.

---

## Discovery Endpoint

Le fournisseur OIDC expose :

```text
/.well-known/openid-configuration
```

permettant aux clients de découvrir automatiquement :

- l'issuer ;
- les endpoints OAuth2 ;
- les endpoints OIDC ;
- le JWKS endpoint.

---

## JWKS Endpoint

Les clés publiques utilisées pour signer les JWT sont exposées via :

```text
/oauth2/jwks
```

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

# Gestion des clés cryptographiques

## Keystore PKCS12

Les clés RSA utilisées pour signer les JWT sont stockées dans un keystore PKCS12.

Exemple :

```text
auth-server.p12
├── old-key
└── auth-server
```

Le keystore est chargé au démarrage de l'application.

---

## Clé active

Une clé active est utilisée pour signer les nouveaux JWT.

Configuration :

```properties
jwt.keystore.active-alias=auth-server
```

Exemple :

```text
Keystore
├── old-key
└── auth-server

Clé active
└── auth-server
```

---

## Rotation des clés

Le module supporte plusieurs clés RSA simultanément.

Principe :

```text
Anciennes clés
       │
       ▼
publiées dans le JWKS

Nouvelle clé active
       │
       ▼
utilisée pour signer les JWT
```

Cette approche permet la rotation progressive des clés cryptographiques sans invalider immédiatement les anciens tokens.

---

## Signature des JWT

Les nouveaux JWT sont signés à l'aide de la clé active configurée.

Exemple de header JWT :

```json
{
  "alg": "RS256",
  "kid": "auth-server"
}
```

Le champ :

```text
kid
```

permet aux clients de retrouver automatiquement la clé publique correspondante dans le JWKS.

---

# JWT

Les JWT sont personnalisés via :

```text
OAuth2JwtCustomizer
```

Ce composant permet d'ajouter des claims métier dans les tokens émis.

Exemple :

```text
application_code
client_id
```

---

# État actuel

## Fonctionnel

✅ Authentification utilisateur

✅ Spring Security

✅ OAuth2 Authorization Server

✅ Client Credentials Flow

✅ Authorization Code Flow

✅ PKCE

✅ OpenID Connect

✅ Discovery Endpoint

✅ JWKS Endpoint

✅ Access Tokens JWT

✅ ID Tokens JWT

✅ Signature RSA

✅ Keystore PKCS12

✅ Clé active configurable

✅ JWKS multi-clés

✅ Préparation à la rotation des clés

✅ Personnalisation des claims JWT

---

# Évolutions envisagées

- Refresh Tokens
- Token Revocation
- Token Introspection
- Endpoint UserInfo personnalisé
- OIDC Logout
- Rotation automatique des clés cryptographiques
- Audit de sécurité

---

# Conclusion

Le module `auth-server-security` regroupe l'ensemble des mécanismes OAuth2, OpenID Connect et Spring Security du projet.

Son rôle est d'intégrer les frameworks de sécurité au domaine métier tout en respectant les principes de l'architecture hexagonale et en conservant une indépendance totale vis-à-vis des détails de persistance.

Le module supporte désormais :

- OAuth2 ;
- OpenID Connect ;
- JWT signés en RSA ;
- JWKS multi-clés ;
- keystore PKCS12 ;
- clé active configurable ;
- préparation à la rotation des clés cryptographiques.

Ces capacités rapprochent le projet du comportement attendu d'un Authorization Server OAuth2/OpenID Connect utilisé en environnement réel.
