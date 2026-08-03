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
auth-server-web
        ↓
auth-server-application
        ↓
auth-server-core

auth-server-persistence
        ↓
auth-server-core

auth-server-security
        ↓
auth-server-core

auth-server-boot
    ├── auth-server-web
    ├── auth-server-security
    └── auth-server-persistence
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
- Spring Data JPA
- PostgreSQL
- Liquibase
- OAuth2 Authorization Server

### Principe

Le Core doit rester totalement indépendant des technologies.

---

## auth-server-application

### Dépendances autorisées

- auth-server-core
- Spring

### Dépendances interdites

- auth-server-web
- auth-server-persistence
- auth-server-security

### Principe

Cette couche orchestre les use cases et gère les transactions.

---

## auth-server-web

### Dépendances autorisées

- auth-server-application
- Spring MVC
- Spring Security

### Dépendances interdites

- auth-server-persistence
- auth-server-security

### Principe

Le Web dépend uniquement de la couche applicative.

---

## auth-server-persistence

### Dépendances autorisées

- auth-server-core
- Spring Data JPA
- PostgreSQL
- Liquibase

### Principe

Cette couche implémente les ports de persistance.

---

## auth-server-security

### Dépendances autorisées

- auth-server-core
- Spring Security
- Spring Authorization Server

### Dépendances interdites

- auth-server-persistence

### Principe

La sécurité dépend des ports du Core et non des technologies de persistance.

---

## auth-server-boot

### Dépendances autorisées

- auth-server-web
- auth-server-security
- auth-server-persistence

### Principe

Le module Boot joue le rôle de Composition Root.

Il est le seul module autorisé à connaître l'ensemble du système.

---

# Dépendances techniques

## Java

### Version

Java 25

### Motivation

- Records
- Sealed Classes
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

## Spring Security

### Module

auth-server-security

### Rôle

- authentification ;
- autorisation ;
- gestion des utilisateurs ;
- gestion des rôles.

---

## Spring Authorization Server

### Module

auth-server-security

### Rôle

- OAuth2 Authorization Server ;
- gestion des clients OAuth2 ;
- émission des tokens ;
- validation des scopes.

---

## Lombok

### Modules

- auth-server-core
- auth-server-application
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

Utilisé pour les tests unitaires et d'intégration.

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
- les bibliothèques éprouvées ;
- la maintenabilité ;
- l'explicitation des dépendances ;
- l'indépendance du métier vis-à-vis de la technique.