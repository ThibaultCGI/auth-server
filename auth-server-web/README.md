# auth-server-web

## Responsabilité

Le module `auth-server-web` expose les fonctionnalités de l'application via une API REST.

Il implémente les contrats HTTP définis dans le module `auth-server-openapi` et adapte les échanges HTTP vers la couche applicative.

---

## Position dans l'architecture

```text
Client HTTP
      │
      ▼
auth-server-web
      ├────────► auth-server-openapi
      │
      ▼
auth-server-application
      │
      ▼
auth-server-core
```

---

## Dépendances

```text
auth-server-web
├── auth-server-application
└── auth-server-openapi
```

---

## Contenu

```text
web
├── api
│   └── v1
│       ├── controller
│       ├── dto
│       ├── response
│       ├── mapper
│       └── error
│
└── security
```

---

## Responsabilités

- exposition des endpoints REST ;
- implémentation des contrats OpenAPI ;
- désérialisation des requêtes HTTP ;
- sérialisation des réponses HTTP ;
- transformation DTO ↔ métier ;
- gestion des erreurs HTTP ;
- configuration de la sécurité Web ;
- adaptation des échanges HTTP vers la couche applicative.

---

## Contrats OpenAPI

Les contrats HTTP sont définis dans le module :

```text
auth-server-openapi
```

Le module Web fournit les implémentations associées.

Exemples :

```
ApplicationController
        implements
ApplicationApi
```

```
OAuth2ClientController
        implements
OAuth2ClientApi
```

```
OAuth2ScopeController
        implements
OAuth2ScopeApi
```

Les DTO REST peuvent également implémenter les interfaces documentaires définies dans le module OpenAPI.

Exemple :

```
ApplicationResponse
        implements
ApplicationResponseApi
```

---

## Organisation des packages

### api.v1.controller

Contient les contrôleurs REST.

Exemples :

- ApplicationController
- OAuth2ClientController
- OAuth2ScopeController
- UserController
- RoleController

### api.v1.dto

Contient les objets représentant les requêtes HTTP.

Exemples :

- CreateApplicationRequest
- CreateOAuth2ClientRequest
- CreateOAuth2ScopeRequest
- CreateUserRequest
- AssignRoleRequest

### api.v1.response

Contient les objets représentant les réponses HTTP.

Exemples :

- ApplicationResponse
- OAuth2ClientResponse
- OAuth2ScopeResponse
- UserResponse
- RoleResponse

### api.v1.mapper

Contient les conversions entre les objets métier et les objets HTTP.

Exemples :

- ApplicationWebMapper
- OAuth2ClientWebMapper
- OAuth2ScopeWebMapper
- UserWebMapper
- RoleWebMapper

### api.v1.error

Contient la gestion centralisée des erreurs HTTP.

Exemples :

- ApiExceptionHandler
- ApiErrorResponse

### security

Contient les composants de sécurité spécifiques à la couche Web.

Exemples :

- SecurityFilterChainConfiguration
- ApiAccessDeniedHandler
- ApiAuthenticationEntryPoint

---

## Dépendances autorisées

- auth-server-application
- auth-server-openapi
- Spring MVC
- Spring Security

---

## Dépendances interdites

- auth-server-core
- auth-server-persistence
- auth-server-security

---

## Principe

Cette couche :

- reçoit les requêtes HTTP ;
- applique les règles liées au transport ;
- invoque les services applicatifs ;
- transforme les objets métier en réponses HTTP ;
- implémente les contrats définis dans le module OpenAPI.

Elle ne contient aucune logique métier.

Toute règle métier doit être implémentée dans :

```text
auth-server-core
```

ou orchestrée par :

```text
auth-server-application
```
