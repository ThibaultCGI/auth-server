# auth-server-openapi

## Responsabilité

Le module `auth-server-openapi` centralise les contrats HTTP et la documentation OpenAPI du projet.

Il constitue le contrat HTTP de référence de l'application et rassemble :

- les interfaces documentant les APIs REST ;
- les DTO documentaires ;
- les réponses documentaires ;
- les configurations Swagger/OpenAPI ;
- la documentation OAuth2 ;
- la documentation OpenID Connect.

Son rôle est comparable à celui du module `auth-server-core` :

```text
auth-server-core
→ contrats métier

auth-server-openapi
→ contrats HTTP
```

---

# Position dans l'architecture

```text
                         auth-server-web
                                │
                                ▼

                     auth-server-openapi
                                │
                                ▼

                       auth-server-core
```

Le module OpenAPI est utilisé par les adaptateurs HTTP afin d'exposer une documentation cohérente, centralisée et indépendante des implémentations techniques.

---

# Dépendances

```text
auth-server-openapi
└── auth-server-core
```

Le module dépend uniquement du Core.

Il ne dépend pas :

- de Web ;
- de Security ;
- de Persistence ;
- d'Application.

---

# Objectifs

Le module OpenAPI a été introduit afin de :

- centraliser la documentation HTTP ;
- réduire le couplage avec les contrôleurs REST ;
- partager les contrats documentaires ;
- simplifier l'évolution de la documentation ;
- structurer les groupes Swagger par domaine fonctionnel ;
- documenter OAuth2 et OpenID Connect.

---

# Organisation

```text
openapi
├── administration
├── authorizationserver
├── iam
└── common
```

---

# administration

Documentation des APIs d'administration.

```text
administration
├── api
├── dto
├── response
├── constants
└── config
```

## Responsabilités

- gestion des applications ;
- gestion des clients OAuth2 ;
- gestion des scopes OAuth2.

## Exemples

### API

- ApplicationApi
- OAuth2ClientApi
- OAuth2ScopeApi

### DTO

- CreateApplicationRequestApi
- CreateOAuth2ClientRequestApi
- CreateOAuth2ScopeRequestApi
- AssignOAuth2ScopeRequestApi

### Réponses

- ApplicationResponseApi
- OAuth2ClientResponseApi
- OAuth2ScopeResponseApi
- ApiErrorResponseApi

---

# iam

Documentation des APIs de gestion des identités et des rôles.

```text
iam
├── api
├── dto
├── response
├── constants
└── config
```

## Responsabilités

- gestion des utilisateurs ;
- gestion des rôles ;
- gestion des habilitations.

## Exemples

### API

- UserApi
- RoleApi

---

# authorizationserver

Documentation des endpoints standards OAuth2 et OpenID Connect.

```text
authorizationserver
├── api
├── dto
├── response
├── constants
└── config
```

## Responsabilités

- documentation OAuth2 ;
- documentation OpenID Connect ;
- documentation des tokens ;
- documentation des endpoints standardisés ;
- configuration du groupe Swagger dédié à l'Authorization Server.

---

## Endpoints OAuth2

```text
/oauth2/authorize
/oauth2/token
```

### Flows documentés

- Authorization Code
- Authorization Code + PKCE
- Client Credentials

---

## Endpoints OpenID Connect

```text
/.well-known/openid-configuration
/oauth2/jwks
```

### Fonctionnalités documentées

- Discovery Endpoint ;
- JWKS Endpoint ;
- ID Tokens ;
- Scope `openid`.

---

# common

Composants communs à l'ensemble des groupes OpenAPI.

```text
common
├── constants
└── config
```

## Responsabilités

- constantes partagées ;
- configuration OpenAPI commune ;
- modèles réutilisables ;
- schémas de sécurité communs.

---

# Contrats HTTP

Le module définit les contrats HTTP de référence.

Exemple :

```java
public interface ApplicationApi {
}
```

Ces contrats sont implémentés dans le module Web.

Exemple :

```java
@RestController
public class ApplicationController
        implements ApplicationApi {
}
```

---

# DTO documentaires

Le module contient les DTO utilisés exclusivement pour la documentation OpenAPI.

Exemple :

```java
public interface ApplicationResponseApi {

    String code();

    String name();

    String description();
}
```

Les implémentations concrètes restent dans le module Web.

Exemple :

```java
public record ApplicationResponse(
        String code,
        String name,
        String description
) implements ApplicationResponseApi {
}
```

---

# Groupes Swagger

Le projet est organisé autour de plusieurs groupes OpenAPI.

## OAuth2 Administration API

Documentation des APIs d'administration :

- applications ;
- clients OAuth2 ;
- scopes OAuth2.

---

## IAM Administration API

Documentation des APIs IAM :

- utilisateurs ;
- rôles ;
- habilitations.

---

## Authorization Server API

Documentation des endpoints standards OAuth2 et OpenID Connect :

### OAuth2

- authorization endpoint ;
- token endpoint ;
- scopes ;
- grant types.

### OpenID Connect

- discovery endpoint ;
- JWKS endpoint ;
- ID Tokens ;
- claims OIDC.

---

# Documentation OAuth2

Le module documente les flows actuellement supportés :

```text
Client Credentials
Authorization Code + PKCE
```

ainsi que les schémas de sécurité OpenAPI associés.

---

# Documentation OpenID Connect

Le module documente les fonctionnalités OIDC exposées par l'application :

```text
openid
ID Token
Discovery
JWKS
```

et les endpoints standards associés.

---

# Dépendances autorisées

- auth-server-core
- SpringDoc OpenAPI

---

# Dépendances interdites

- auth-server-web
- auth-server-application
- auth-server-security
- auth-server-persistence

---

# Principes d'architecture

Le module OpenAPI ne contient aucune logique métier.

Il décrit uniquement :

- les contrats HTTP ;
- la documentation OpenAPI ;
- l'organisation Swagger ;
- les modèles documentaires ;
- les schémas de sécurité ;
- les spécifications OAuth2 et OpenID Connect exposées par le système.

Le module constitue le contrat HTTP du projet, de la même manière que le Core constitue son contrat métier.

```text
Core
  │
  └── Contrat métier

OpenAPI
  │
  └── Contrat HTTP
```

---

# État actuel

## Fonctionnel

✅ Documentation des APIs IAM

✅ Documentation des APIs d'administration

✅ Documentation OAuth2

✅ Documentation OpenID Connect

✅ Documentation des flows Authorization Code + PKCE

✅ Documentation du flow Client Credentials

✅ Swagger UI

✅ Groupes OpenAPI par domaine

✅ Contrats HTTP isolés du module Web

---

# Évolutions envisagées

- Documentation des Refresh Tokens
- Documentation de la révocation de tokens
- Documentation de l'introspection
- Documentation UserInfo
- Exemples complets OAuth2/OIDC
- Guides d'intégration pour les clients externes

---

# Conclusion

Le module `auth-server-openapi` constitue la référence documentaire du projet.

Il centralise l'ensemble des contrats HTTP et permet de documenter les APIs métier, les endpoints OAuth2 et les fonctionnalités OpenID Connect tout en conservant un découplage fort avec les implémentations techniques.
``