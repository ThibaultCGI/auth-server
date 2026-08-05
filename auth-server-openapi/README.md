# auth-server-openapi

## Responsabilité

Le module `auth-server-openapi` centralise les contrats HTTP et la documentation OpenAPI du projet.

Il constitue le point d'entrée documentaire de toutes les API exposées par le système.

Son rôle est comparable à celui du module `auth-server-core` :

- `auth-server-core` définit les contrats métier ;
- `auth-server-openapi` définit les contrats HTTP.

---

## Position dans l'architecture

```text
                         auth-server-web
                                │
                                ▼

                     auth-server-openapi
                                │
                                ▼

                       auth-server-core
```

Le module OpenAPI est utilisé par les adaptateurs HTTP afin d'exposer une documentation cohérente et centralisée.

---

## Dépendances

```text
auth-server-openapi
└── auth-server-core
```

Le module OpenAPI dépend uniquement du Core.

Il ne dépend pas :

- de Web ;
- de Security ;
- de Persistence ;
- d'Application.

---

## Objectifs

Le module OpenAPI a été introduit afin de :

- centraliser la documentation HTTP ;
- réduire le couplage avec les implémentations REST ;
- partager les contrats documentaires ;
- simplifier l'évolution de la documentation ;
- structurer les groupes Swagger par domaine fonctionnel.

---

## Organisation

```text
openapi
├── administration
├── authorizationserver
├── iam
└── common
```

---

## administration

Documentation des API d'administration OAuth2.

```text
administration
├── api
├── dto
├── response
├── constants
└── config
```

### Responsabilités

- gestion des applications ;
- gestion des clients OAuth2 ;
- gestion des scopes OAuth2.

### Exemples

#### API

- ApplicationApi
- OAuth2ClientApi
- OAuth2ScopeApi

#### DTO

- CreateApplicationRequestApi
- CreateOAuth2ClientRequestApi
- CreateOAuth2ScopeRequestApi
- AssignOAuth2ScopeRequestApi

#### Responses

- ApplicationResponseApi
- OAuth2ClientResponseApi
- OAuth2ScopeResponseApi
- ApiErrorResponseApi

---

## iam

Documentation des API de gestion des identités et des rôles.

```text
iam
├── api
├── dto
├── response
├── constants
└── config
```

### Responsabilités

- gestion des utilisateurs ;
- gestion des rôles ;
- gestion des habilitations.

### Exemples

#### API

- UserApi
- RoleApi

---

## authorizationserver

Documentation des endpoints standard OAuth2 et OpenID Connect.

```text
authorizationserver
├── api
├── dto
├── response
├── constants
└── config
```

### Responsabilités

- documentation des endpoints OAuth2 ;
- documentation OIDC ;
- configuration du groupe Swagger dédié à l'Authorization Server.

### Endpoints concernés

- `/oauth2/token`
- `/oauth2/jwks`
- `/oauth2/introspect`
- `/oauth2/revoke`
- `/.well-known/**`

---

## common

Éléments communs à l'ensemble des groupes OpenAPI.

```text
common
├── constants
└── config
```

### Responsabilités

- constantes globales ;
- configuration OpenAPI commune ;
- éléments partagés entre les domaines.

---

## Contrats HTTP

Le module définit les contrats documentaires de référence.

Exemple :

```java
public interface ApplicationApi {
}
```

Ces contrats sont ensuite implémentés par les adaptateurs HTTP.

Exemple :

```java
@RestController
public class ApplicationController
        implements ApplicationApi {
}
```

---

## DTO documentaires

Le module contient les DTO utilisés pour décrire les requêtes et réponses exposées dans la documentation.

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

## Groupes Swagger

Le projet est organisé autour de plusieurs groupes OpenAPI :

### OAuth2 Administration API

Documentation des API d'administration :

- applications ;
- clients OAuth2 ;
- scopes OAuth2.

### IAM Administration API

Documentation des API IAM :

- utilisateurs ;
- rôles.

### OAuth2 Authorization Server API

Documentation des endpoints standard OAuth2/OpenID Connect :

- token ;
- introspection ;
- révocation ;
- métadonnées ;
- JWKS.

---

## Dépendances autorisées

- auth-server-core
- SpringDoc OpenAPI

---

## Dépendances interdites

- auth-server-web
- auth-server-application
- auth-server-security
- auth-server-persistence

---

## Principe

Le module OpenAPI ne contient aucune logique métier.

Il décrit uniquement :

- les contrats HTTP ;
- la documentation OpenAPI ;
- l'organisation Swagger ;
- les modèles documentaires.

Il constitue le contrat HTTP du système, de la même manière que le Core constitue le contrat métier.
