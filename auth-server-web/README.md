# auth-server-web

## Responsabilité

Le module `auth-server-web` expose l'API REST.

Il adapte les requêtes HTTP vers les services applicatifs.

## Position dans l'architecture

```text
Client HTTP
      ↓
auth-server-web
      ↓
auth-server-application
      ↓
auth-server-core
```

## Contenu

```text
web
├── api
│   ├── controller
│   ├── dto
│   ├── mapper
│   ├── response
│   └── error
│
└── security
```

## Responsabilités

- exposition des endpoints REST ;
- validation des requêtes ;
- transformation DTO ↔ métier ;
- gestion des erreurs HTTP ;
- configuration de la sécurité web.

## Dépendances autorisées

- auth-server-application
- Spring MVC
- Spring Security

## Principe

Cette couche :

- reçoit des requêtes HTTP ;
- appelle les services applicatifs ;
- retourne des réponses HTTP.

Elle ne contient pas de logique métier.