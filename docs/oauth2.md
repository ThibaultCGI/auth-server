# OAuth2

## Objectif

Comprendre et implémenter les mécanismes OAuth2 et OpenID Connect.

---

# Acteurs

## Resource Owner

Utilisateur final.

Exemple :

```text
Thibault
```

---

## Client

Application qui demande un accès.

Exemples :

- SPA Angular
- Application React
- Mobile
- Backend Java

---

## Authorization Server

Responsable de :

- l'authentification
- la génération des tokens
- la gestion des clients

---

## Resource Server

API protégée.

Exemple :

```http
GET /api/users
Authorization: Bearer xxx
```

---

# Concepts importants

## Access Token

Jeton utilisé pour accéder à une ressource protégée.

Généralement un JWT.

---

## Refresh Token

Permet d'obtenir un nouvel Access Token.

---

## Scope

Permission accordée à un client.

Exemples :

```text
read
write
profile
email
```

---

# Flows à implémenter

## Client Credentials

Premier flow du projet.

Utilisé pour :

```text
Machine ↔ Machine
```

---

## Authorization Code

Flow standard moderne.

---

## Authorization Code + PKCE

Version sécurisée du flow Authorization Code.

À implémenter après les bases.

---

## Refresh Token

Permet le renouvellement des accès.

---

# JWT

Structure :

```text
header.payload.signature
```

Exemple de claims :

```json
{
  "sub": "user1",
  "scope": "read",
  "exp": 123456789
}
```

---

# OpenID Connect

Extension d'OAuth2 destinée à l'authentification.

Scopes :

```text
openid
profile
email
```

---

# Endpoints à exposer

```text
/oauth2/token
/oauth2/authorize
/oauth2/jwks
```

---

# État actuel

## Connaissances acquises

- Différence OAuth2 / OIDC
- JWT
- Scopes
- Clients OAuth2

## À approfondir

- PKCE
- OpenID Connect
- JWK
- Rotation de clés