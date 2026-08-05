# auth-server-boot

## Responsabilité

Le module `auth-server-boot` constitue le point d'entrée de l'application.

Il joue également le rôle de Composition Root.

## Vue globale de l'architecture

```text
                                                     ┌────────────────────┐
                                                     │ auth-server-boot   │
                                                     └──────────┬─────────┘
                                                                │
         ┌──────────────────────────────────────────────────────┴──────────────┬──────────────────────────────────────┐
         │                                                                     │                                      │
         ▼                                                                     ▼                                      ▼

┌─────────────────┐             ┌─────────────────────┐             ┌──────────────────────┐             ┌─────────────────────────┐
│ auth-server-web ├─────────►   │ auth-server-openapi │             │ auth-server-security │             │ auth-server-persistence │
└────────┬────────┘             └──────────┬──────────┘             └──────────┬───────────┘             └────────────┬────────────┘
         │                                 │                                   │                                      │
         │                                 │                                   │                                      │
         ▼                                 │                                   ▼                                      │
                                           │                                                                          │
┌─────────────────────────┐                │                           ┌─────────────────┐                            │
│ auth-server-application │────────────────┴───────────────────────►   │ auth-server-core│   ◄────────────────────────┘
└─────────────────────────┘                                            └─────────────────┘
```

## Contenu

```text
boot
├── AuthServerApplication
└── config
    └── RepositoryAdapterConfiguration
```

## Responsabilités

- démarrage Spring Boot ;
- assemblage des modules ;
- raccordement ports ↔ adapters ;
- composition de l'application.

## Dépendances

```text
auth-server-web
auth-server-security
auth-server-persistence
```

## Principe

Le module Boot est le seul module autorisé à connaître l'ensemble des autres modules.

Il est responsable de l'assemblage final de l'application.

Aucune logique métier ne doit être implémentée dans ce module.
`