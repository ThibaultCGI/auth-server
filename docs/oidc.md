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

```
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
  "iss": "http://localhost:8080",
  "aud": [
    "client-id"
  ],
  "iat": 1786519200,
  "exp": 1786521000
}
```

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
  "issuer": "http://localhost:8080",
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
http://localhost:8080
```

Les clients OIDC utilisent généralement :

```properties
spring.security.oauth2.client.provider.auth-server.issuer-uri=http://localhost:8080
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

Cet endpoint expose la ou les clés publiques utilisées pour signer les JWT.

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

        │
        ▼

JWKS Endpoint

        │
        ▼

Validation cryptographique
par le client
```

---

# Authentification utilisateur

L'authentification des utilisateurs repose sur Spring Security.

Lorsque l'utilisateur tente d'accéder à :

```text
/oauth2/authorize
```

sans être authentifié :

```text
Redirection vers /login
```

---

# Session utilisateur

Le flow Authorization Code nécessite une session HTTP utilisateur.

Le projet utilise :

```text
SessionCreationPolicy.IF_REQUIRED
```

sur les endpoints OAuth2 / OIDC.

---

# PKCE

Le projet supporte PKCE.

Le client peut fournir :

```text
code_challenge
code_challenge_method
```

lors de la requête d'autorisation.

Le serveur valide ensuite le :

```text
code_verifier
```

lors de l'échange du code.

---

# Sécurité

Les mécanismes de sécurité actuellement utilisés sont :

✅ Authorization Code Flow

✅ PKCE

✅ Signature RSA des JWT

✅ Validation cryptographique via JWKS

✅ Gestion de session Spring Security

✅ Protection contre la Session Fixation

✅ Validation des scopes

---

# Limitations actuelles

Les fonctionnalités suivantes ne sont pas encore implémentées :

```text
UserInfo Endpoint personnalisé
Refresh Tokens OIDC
OIDC Logout
Federation
Social Login
```

---

# Évolutions envisagées

## OpenID Connect

- Endpoint UserInfo personnalisé
- Claims enrichies
- OIDC Logout
- Rotation des clés cryptographiques

## Sécurité

- Persistance des clés RSA
- Rotation automatique des clés
- Audit des authentifications

---

# Conclusion

Le projet fournit désormais une implémentation fonctionnelle d'OpenID Connect basée sur Spring Authorization Server.

Les clients peuvent :

- découvrir automatiquement le fournisseur ;
- authentifier des utilisateurs ;
- obtenir un ID Token ;
- valider sa signature ;
- exploiter les claims de l'utilisateur connecté.

L'implémentation repose entièrement sur les standards OpenID Connect et s'intègre naturellement avec les clients Spring Security.