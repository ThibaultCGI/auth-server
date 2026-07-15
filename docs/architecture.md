# Architecture du projet

## Objectif

L'objectif du projet est de mettre en place un serveur d'authentification OAuth2 / OpenID Connect avec :

- Java 25
- Spring Boot 4
- Spring Security
- Spring Authorization Server
- PostgreSQL
- Liquibase

Le projet a également un objectif pédagogique fort :

- apprendre OAuth2 ;
- comprendre OpenID Connect ;
- mettre en œuvre Spring Security ;
- pratiquer une architecture hexagonale ;
- structurer proprement un projet Maven multi-modules.

---

# Architecture cible

```text
                    +------------------+
                    |   auth-boot      |
                    |------------------|
                    | Spring Boot      |
                    | Point d'entrée   |
                    | Configuration    |
                    +--------+---------+
                             |
              +--------------+--------------+
              |                             |
              v                             v

+----------------------+     +---------------------------+
|      auth-core       |     |   auth-infrastructure     |
|----------------------|     |---------------------------|
| Domaine métier       |     | REST Controllers          |
| Cas d'utilisation    |     | Spring Security           |
| Ports                |     | OAuth2 Authorization      |
| Exceptions métier    |     | JPA / Hibernate           |
+----------------------+     | PostgreSQL               |
                             | Liquibase                |
                             +-------------+------------+
                                           |
                                           v

                             +---------------------------+
                             |        PostgreSQL         |
                             +---------------------------+
```

---

# Modules

## auth-core

Le module `auth-core` contient le cœur métier du projet.

### Responsabilités

- des modèles métier ;
- des cas d'utilisation ;
- des ports d’entrée ;
- des ports de sortie ;
- des exceptions métier.

### Ce qu’il ne doit pas contenir

- Spring ;
- JPA ;
- PostgreSQL ;
- HTTP ;
- OAuth2 technique ;
- configuration d’infrastructure.

### Exemple de contenu futur

```text
domain/
application/
port/
exception/
```

---

## auth-infrastructure

Le module `auth-infrastructure` contient les détails techniques.

### Responsabilités

- exposition HTTP ;
- configuration de sécurité ;
- configuration OAuth2 ;
- persistance JPA ;
- intégration PostgreSQL ;
- migrations Liquibase.

### Exemple de contenu futur

```text
controller/
security/
oauth/
persistence/
config/
```

---

## auth-boot

Le module `auth-boot` est le module de démarrage.

### Responsabilités

- contenir la classe principale Spring Boot ;
- assembler les modules ;
- charger les propriétés de configuration ;
- permettre le démarrage de l’application.

### Exemple de contenu futur

```text
AuthServerApplication.java
application.properties
application-local.properties
```

---

# Principes retenus

## Séparation des responsabilités

Chaque module a un rôle précis :

- `auth-core` = métier ;
- `auth-infrastructure` = technique ;
- `auth-boot` = démarrage.

---

## Inversion des dépendances

Le domaine ne doit pas dépendre des détails techniques.

La règle cible est :

```text
Infrastructure -> Core
Boot -> Core + Infrastructure
```

et jamais l’inverse.

---

## Testabilité

Le module `auth-core` doit rester testable sans :

- Spring ;
- base de données ;
- serveur HTTP.

---

# Répartition des dépendances

## `auth-core`

Doit contenir uniquement le métier.

Aucune dépendance Spring n’est souhaitée au départ.

---

## `auth-infrastructure`

Contient les dépendances techniques :

- Spring Web MVC ;
- Spring Security ;
- Spring Authorization Server ;
- Spring Data JPA ;
- PostgreSQL ;
- Liquibase.

---

## `auth-boot`

Contient :

- le point d’entrée Spring Boot ;
- le packaging exécutable ;
- les fichiers de configuration.

C’est également le seul module qui doit être packagé comme application Spring Boot autonome.

---

# État actuel

## Réalisé

- Projet initialisé ;
- Dépôt GitHub créé ;
- Documentation technique démarrée ;
- Choix d’une architecture hexagonale ;
- Choix d’un découpage multi-modules Maven.

## En cours

- Séparation effective en modules Maven ;
- Mise en place du rôle de chaque module ;
- Ajustement du build Maven.

## À venir

- Corriger la configuration des plugins Maven ;
- Déplacer la classe principale dans `auth-boot` ;
- Vérifier le démarrage de l’application ;
- Commencer la configuration Liquibase ;
- Créer les premiers éléments métier.