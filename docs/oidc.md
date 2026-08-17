# OpenID Connect

## Objectif

Ce document décrit l'implémentation OpenID Connect (OIDC) présente dans le projet.

L'objectif est de permettre l'authentification des utilisateurs au-dessus d'OAuth 2.1 en exposant les mécanismes standards définis par OpenID Connect.

Le projet agit ainsi à la fois comme :

- OAuth2 Authorization Server ;
- OpenID Provider (OP).

---

# Qu'est-ce qu'OpenID Connect ?

OpenID Connect (OIDC) est une couche d'identité construite au-dessus d'OAuth2.

OAuth2 répond à la question :

```text
Une application peut-elle accéder à une ressource ?
```

OpenID Connect répond à la question :

```text
Qui est l'utilisateur connecté ?
```

OIDC ajoute notamment :

- le scope `openid` ;
- l'ID Token ;
- les claims utilisateur ;
- la découverte automatique du fournisseur ;
- les endpoints standardisés OIDC.

---

# Vue d'ensemble

Le projet implémente les fonctionnalités suivantes :

✅ Authentification des utilisateurs

✅ Scope `openid`

✅ ID Token

✅ Discovery Endpoint

✅ JWKS Endpoint

✅ Validation des signatures JWT

✅ JWKS multi-clés

✅ Clé active de signature configurable

✅ Authentification OIDC avec Spring Security

---

# Architecture

## OpenID Provider

Le projet joue le rôle suivant :

```text
+--------------------+
| auth-server        |
| OpenID Provider    |
+--------------------+
```

Les clients OIDC peuvent s'authentifier auprès de cette application.

---

## Client OIDC de démonstration

Le projet est compatible avec :

```text
test-oauth2-client
```

qui utilise :

```text
OAuth2 Login
Authorization Code Flow
PKCE
OpenID Connect
```

---

# Activation d'OpenID Connect

OpenID Connect est activé dans Spring Authorization Server via :

```java
.oidc(Customizer.withDefaults())
```

Cette configuration active automatiquement les endpoints OIDC standards.

---

# Scope openid

Le scope :

```text
openid
```

est le scope fondamental d'OpenID Connect.

Lorsqu'un client demande :

```text
scope=openid
```

Spring Authorization Server bascule du mode OAuth2 au mode OIDC.

---

## Exemple

```text
scope=openid trs:produit-api.read
```

---

# Authorization Code Flow OIDC

## Séquence simplifiée

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

# ID Token

## Définition

L'ID Token est un JWT contenant l'identité de l'utilisateur authentifié.

Il est destiné au client OAuth2.

Contrairement à l'Access Token :

```text
Access Token
→ accès aux APIs
```

```text
ID Token
→ identité utilisateur
```

---

## Exemple

```json
{
  "sub": "admin",
  "iss": "http://auth-server.local:8080",
  "aud": [
    "client-id"
  ],
  "iat": 1786519200,
  "exp": 1786521000
}
```

---

## Header JWT

Les ID Tokens sont signés en RSA.

Exemple de header :

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

permet au client de retrouver la clé publique utilisée pour signer le token.

---

# Claims

## Claims standard

Les claims OIDC standards actuellement émises sont notamment :

```text
sub
iss
aud
iat
exp
auth_time
nonce
sid
```

---

## Claims applicatives

Le projet ajoute également des claims spécifiques :

```text
application_code
client_id
```

au travers de :

```text
OAuth2JwtCustomizer
```

---

# Discovery Endpoint

Le serveur expose automatiquement :

```text
/.well-known/openid-configuration
```

Cet endpoint permet aux clients de découvrir automatiquement :

- l'issuer ;
- l'Authorization Endpoint ;
- le Token Endpoint ;
- le JWKS Endpoint ;
- les fonctionnalités OIDC supportées.

---

## Exemple

```text
GET /.well-known/openid-configuration
```

Retour :

```json
{
  "issuer": "http://auth-server.local:8080",
  "authorization_endpoint": "...",
  "token_endpoint": "...",
  "jwks_uri": "..."
}
```

---

# Issuer

L'issuer représente l'identité du fournisseur OIDC.

Dans l'environnement local :

```text
http://auth-server.local:8080
```

Les clients OIDC utilisent généralement :

```properties
spring.security.oauth2.client.provider.auth-server.issuer-uri=http://auth-server.local:8080
```

afin de découvrir automatiquement toute la configuration du fournisseur.

---

# JWKS Endpoint

Le projet expose :

```text
/oauth2/jwks
```

---

## Objectif

Cet endpoint expose les clés publiques utilisées pour signer les JWT.

Les clients OIDC utilisent ces clés pour :

```text
Vérifier la signature
des ID Tokens
```

---

## Schéma

```text
Authorization Server
        │
        ▼

ID Token signé
```
