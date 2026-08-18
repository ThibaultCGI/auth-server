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
- le UserInfo Endpoint ;
- la découverte automatique du fournisseur ;
- les endpoints standardisés OIDC.

---

# Vue d'ensemble

Le projet implémente les fonctionnalités suivantes :

✅ Authentification des utilisateurs

✅ Scope `openid`

✅ ID Token

✅ Refresh Token

✅ UserInfo Endpoint

✅ Discovery Endpoint

✅ JWKS Endpoint

✅ Validation des signatures JWT

✅ JWKS multi-clés

✅ Clé active de signature configurable

✅ Renouvellement automatique des Access Tokens

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
Refresh Token
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
Refresh Token
+
ID Token
```

---

# Refresh Token

## Objectif

Le Refresh Token permet à un client OpenID Connect d'obtenir un nouvel Access Token sans réauthentifier l'utilisateur.

---

## Principe

```text
Access Token expiré

        │
        ▼

Refresh Token

        │
        ▼

POST /oauth2/token

grant_type=refresh_token

        │
        ▼

Nouvel Access Token

        │
        ▼

Poursuite de la session
```

---

## Support actuel

Les clients configurés avec les grant types :

```text
authorization_code
refresh_token
```

peuvent recevoir un Refresh Token lors de l'authentification OIDC.

---

## Renouvellement automatique

Le projet a été validé avec :

```text
test-oauth2-client
```

qui renouvelle automatiquement les Access Tokens expirés à l'aide du Refresh Token stocké dans le `OAuth2AuthorizedClient`.

Le renouvellement est transparent pour l'utilisateur.

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

# UserInfo Endpoint

Le serveur expose automatiquement :

```text
/userinfo
```

---

## Objectif

Le UserInfo Endpoint permet à un client OIDC de récupérer les informations de l'utilisateur authentifié à partir d'un Access Token.

---

## Exemple

```http
GET /userinfo
Authorization: Bearer <access-token>
```

Réponse actuelle :

```json
{
  "sub": "admin"
}
```

---

## Validation

L'Access Token est validé avant l'émission des informations utilisateur.

Le UserInfo Endpoint est découvert automatiquement via :

```text
/.well-known/openid-configuration
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
- le UserInfo Endpoint ;
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
  "userinfo_endpoint": "...",
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

        │
        ▼

JWKS Endpoint

        │
        ▼

Validation cryptographique
par le client
```

---

## JWKS multi-clés

Le serveur peut exposer plusieurs clés simultanément.

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

---

## Clé active

Une clé active est utilisée pour signer les nouveaux JWT.

Configuration :

```properties
jwt.keystore.active-alias=auth-server
```

---

## Rotation des clés

Le serveur supporte plusieurs clés RSA simultanément.

Principe :

```text
Anciennes clés
       │
       ▼
publiées dans le JWKS

Nouvelle clé active
       │
       ▼
utilisée pour signer les nouveaux JWT
```

Cette approche permet une rotation progressive des clés sans invalider immédiatement les anciens tokens.

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

# Token Settings

Les durées de vie des tokens sont configurées explicitement via :

```text
TokenSettings
```

---

## Configuration actuelle

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

Le même Refresh Token peut être utilisé plusieurs fois tant qu'il n'a pas expiré.

---

# Sécurité

Les mécanismes de sécurité actuellement utilisés sont :

✅ Authorization Code Flow

✅ PKCE

✅ Refresh Token

✅ UserInfo Endpoint

✅ Renouvellement automatique des Access Tokens

✅ Signature RSA des JWT

✅ Keystore PKCS12

✅ JWKS multi-clés

✅ Validation cryptographique via JWKS

✅ Gestion de session Spring Security

✅ Protection contre la Session Fixation

✅ Validation des scopes

---

# Limitations actuelles

Les fonctionnalités suivantes ne sont pas encore implémentées :

```text
Claims utilisateur enrichies
OIDC Logout
Federation
Social Login
```

---

# Évolutions envisagées

## OpenID Connect

- Enrichissement du UserInfo Endpoint
- Claims personnalisées supplémentaires
- OIDC Logout

## OAuth2

- Rotation des Refresh Tokens

## Sécurité

- Rotation automatique des clés
- Audit des authentifications
- Révocation de tokens
- Introspection de tokens

---

# Conclusion

Le projet fournit désormais une implémentation fonctionnelle d'OpenID Connect basée sur Spring Authorization Server.

Les clients peuvent :

- découvrir automatiquement le fournisseur ;
- authentifier des utilisateurs ;
- obtenir un ID Token ;
- obtenir un Refresh Token ;
- appeler le UserInfo Endpoint ;
- renouveler automatiquement leurs Access Tokens ;
- valider les signatures JWT ;
- exploiter les claims de l'utilisateur connecté.

L'implémentation repose entièrement sur les standards OpenID Connect et s'intègre naturellement avec les clients Spring Security.

Le serveur supporte désormais :

- OpenID Connect ;
- Authorization Code + PKCE ;
- Refresh Tokens ;
- UserInfo Endpoint ;
- ID Tokens JWT ;
- JWKS multi-clés ;
- keystore PKCS12 ;
- clé active configurable ;
- renouvellement automatique des Access Tokens ;
- préparation à la rotation des clés cryptographiques.
