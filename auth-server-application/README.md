# auth-server-application

## Responsabilité

Le module `auth-server-application` orchestre les use cases du Core.

Il constitue la couche applicative de l'architecture.

## Position dans l'architecture

```text
auth-server-web
        ↓
auth-server-application
        ↓
auth-server-core
```

## Contenu

```text
application
├── config
└── service
```

### config

Configuration Spring des use cases :

```
CreateUserUseCase
GetUserUseCase
CreateRoleUseCase
...
```

### service

Services applicatifs :

```
UserService
RoleService
OAuth2ClientService
...
```

## Responsabilités

- gestion des transactions ;
- orchestration de plusieurs use cases ;
- exposition de services applicatifs ;
- intégration du Core avec Spring.

## Dépendances autorisées

- auth-server-core
- Spring

## Dépendances interdites

- contrôleurs HTTP ;
- entités JPA ;
- repositories Spring Data ;
- implémentations de sécurité.

## Principe

Cette couche coordonne le métier mais ne contient pas de logique métier.