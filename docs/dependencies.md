# Dependencies

## Objectif

Ce document décrit :

- les dépendances techniques utilisées par le projet ;
- les dépendances entre modules ;
- les règles architecturales à respecter ;
- les dépendances autorisées et interdites.

---

# Dépendances entre modules

## Vue globale

```text
auth-server-boot
    ├── auth-server-web
    ├── auth-server-security
    └── auth-server-persistence

auth-server-web
    ├── auth-server-application
    └── auth-server-openapi

auth-server-security
    └── auth-server-core

auth-server-persistence
    └── auth-server-core

auth-server-openapi
    └── auth-server-core

auth-server-application
    └── auth-server-core
```

---

# Règles de dépendances

## auth-server-core

### Dépendances autorisées

- Lombok

### Dépendances de test autorisées

- JUnit 5
- Mockito

### Dépendances interdites

- Spring Framework
- Spring Boot
- Spring Security
- Spring Authorization Server
- Spring Data JPA
- PostgreSQL
- Liquibase
- SpringDoc OpenAPI

### Principe

Le Core doit rester totalement indépendant des technologies.

Il constitue le centre de l'architecture.

---

## auth-server-application

### Dépendances autorisées

- auth-server-core
- Spring

### Dépendances interdites

- auth-server-web
- auth-server-openapi
- auth-server-persistence
- auth-server-security

### Principe

Cette couche orchestre les use cases et gère les transactions.

---

## auth-server-openapi

### Dépendances autorisées

- auth-server-core
- SpringDoc OpenAPI

### Dépendances interdites

- auth-server-web
- auth-server-application
- auth-server-persistence
- auth-server-security

### Principe

Ce module centralise les contrats HTTP et la documentation OpenAPI.

Il contient :

- les interfaces API ;
- les DTO documentaires ;
- les réponses documentaires ;
- les constantes OpenAPI ;
- les configurations Swagger.

Les adaptateurs exposant des points d'entrée HTTP dépendent de ce module.

---

## auth-server-web

### Dépendances autorisées

- auth-server-application
- auth-server-openapi
- Spring MVC
- Spring Security

### Dépendances interdites

- auth-server-persistence
- auth-server-security

### Principe

Le Web expose les fonctionnalités applicatives via HTTP.

Il implémente :

- les contrats OpenAPI ;
- les contrôleurs REST ;
- les DTO HTTP ;
- la gestion des erreurs API.

---

## auth-server-persistence

### Dépendances autorisées

- auth-server-core
- Spring Data JPA
- PostgreSQL
- Liquibase

### Dépendances interdites

- auth-server-web
- auth-server-security
- auth-server-application
- auth-server-openapi

### Principe

Cette couche implémente les ports de persistance définis dans le Core.

---

## auth-server-security

### Dépendances autorisées

- auth-server-core
- Spring Security
- Spring Authorization Server

### Dépendances interdites

- auth-server-web
- auth-server-persistence
- auth-server-application
- auth-server-openapi

### Principe

La sécurité dépend uniquement du Core.

Elle implémente :

- l'authentification ;
- l'autorisation ;
- l'Authorization Server OAuth2 ;
- les ports de sécurité définis dans le Core.

---

## auth-server-boot

### Dépendances autorisées

- auth-server-web
- auth-server-security
- auth-server-persistence

### Dépendances interdites

Aucune.

### Principe

Le module Boot joue le rôle de Composition Root.

Il est le seul module autorisé à connaître simultanément l'ensemble des adaptateurs techniques.

---

# Dépendances techniques

## Java

### Version

Java 25

### Motivation

- Records
- Pattern Matching
- Virtual Threads
- Améliorations de performances
- Dernières fonctionnalités du langage

---

## Maven

### Usage

Gestion :

- du build ;
- des modules ;
- des dépendances ;
- des tests ;
- de l'intégration continue.

---

## Spring Boot

### Dépendance

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
</parent>
```

### Rôle

- gestion cohérente des versions ;
- auto-configuration ;
- intégration de l'écosystème Spring.

---

## Spring MVC

### Module

auth-server-web

### Rôle

- exposition des endpoints REST ;
- sérialisation JSON ;
- gestion des requêtes HTTP ;
- gestion des réponses HTTP.

---

## Spring Security

### Module

auth-server-security

### Rôle

- authentification ;
- autorisation ;
- gestion des utilisateurs ;
- gestion des rôles ;
- sécurisation des endpoints.

---

## Spring Authorization Server

### Module

auth-server-security

### Rôle

- OAuth2 Authorization Server ;
- gestion des clients OAuth2 ;
- émission des tokens ;
- révocation des tokens ;
- introspection des tokens ;
- publication des métadonnées OAuth2.

---

## SpringDoc OpenAPI

### Module

auth-server-openapi

### Rôle

- génération de la documentation OpenAPI ;
- génération de Swagger UI ;
- documentation des endpoints ;
- gestion des groupes OpenAPI.

---

## Spring Data JPA

### Module

auth-server-persistence

### Rôle

- mapping ORM ;
- repositories Spring Data ;
- intégration Hibernate.

---

## PostgreSQL

### Module

auth-server-persistence

### Rôle

- stockage des données ;
- gestion des UUID ;
- persistance relationnelle.

---

## Liquibase

### Module

auth-server-persistence

### Rôle

- gestion des migrations ;
- versionnement du schéma ;
- reproductibilité des environnements.

### Règle

Toute modification du schéma doit être réalisée via Liquibase.

---

## Lombok

### Modules

- auth-server-core
- auth-server-application
- auth-server-openapi
- auth-server-web
- auth-server-persistence
- auth-server-security

### Usage

Réduction du code répétitif.

Exemples :

- @Builder
- @RequiredArgsConstructor
- @Getter
- @Setter
- @UtilityClass

---

# Dépendances de test

## JUnit 5

Utilisé pour :

- les tests unitaires ;
- les tests d'intégration ;
- les tests applicatifs.

---

## Mockito

Utilisé pour :

- mocker les ports ;
- simuler les composants techniques ;
- tester les use cases de manière isolée.

---

# Politique d'ajout de dépendances

Avant d'ajouter une dépendance :

1. Vérifier si Java fournit déjà la fonctionnalité.
2. Vérifier si Spring fournit déjà la fonctionnalité.
3. Vérifier que le besoin est réel.
4. Vérifier l'impact sur l'architecture.
5. Vérifier qu'elle ne crée pas de couplage inutile.

---

# Principes directeurs

Le projet privilégie :

- la simplicité ;
- le faible couplage ;
- la séparation stricte des responsabilités ;
- les bibliothèques éprouvées ;
- la maintenabilité ;
- l'explicitation des dépendances ;
- l'indépendance du métier vis-à-vis de la technique.

Les dépendances doivent toujours tendre vers le cœur métier :

```text
web         ──► application ──► core
openapi     ──────────────────► core
security    ──────────────────► core
persistence ──────────────────► core
```