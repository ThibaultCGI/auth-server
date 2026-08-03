# Decisions

Ce document recense les principales décisions d'architecture et de conception prises au cours du projet.

L'objectif est de :

- conserver une trace des choix effectués ;
- documenter les motivations ;
- faciliter la compréhension de l'architecture ;
- éviter de remettre en question des décisions déjà validées sans raison.

---

# ADR-001 - Architecture hexagonale

## Décision

Le projet adopte une architecture hexagonale (Ports & Adapters).

## Motivation

Nous souhaitons :

- isoler le métier des technologies ;
- faciliter les tests unitaires ;
- limiter les dépendances aux frameworks ;
- permettre l'évolution des choix techniques sans impact sur le métier.

## Conséquences

Le métier dépend uniquement :

- des objets métier ;
- des use cases ;
- des ports.

Les détails techniques sont implémentés dans des adaptateurs spécialisés.

---

# ADR-002 - Architecture multi-modules

## Décision

Le projet est organisé sous la forme des modules Maven suivants :

- auth-server-core
- auth-server-application
- auth-server-web
- auth-server-persistence
- auth-server-security
- auth-server-boot

## Motivation

Séparer clairement :

- le métier ;
- l'orchestration applicative ;
- l'exposition HTTP ;
- la persistance ;
- la sécurité ;
- le démarrage de l'application.

## Conséquences

Chaque module possède une responsabilité unique et clairement identifiée.

---

# ADR-003 - PostgreSQL

## Décision

La persistance repose sur PostgreSQL.

## Motivation

PostgreSQL fournit :

- une excellente stabilité ;
- une gestion native des UUID ;
- un excellent support Spring ;
- des performances adaptées aux besoins du projet.

## Conséquences

Les données de production et de développement sont stockées dans une base PostgreSQL.

---

# ADR-004 - Liquibase

## Décision

Les migrations de base de données sont gérées via Liquibase.

## Motivation

Nous souhaitons :

- versionner le schéma ;
- reproduire facilement les environnements ;
- historiser les évolutions de la base.

## Conséquences

Toute modification du schéma doit être réalisée via un changelog Liquibase.

---

# ADR-005 - Format XML pour Liquibase

## Décision

Les changelogs Liquibase sont écrits en XML.

## Motivation

Le format XML :

- est bien supporté par les IDE ;
- bénéficie d'une validation XSD ;
- reste lisible et maintenable.

## Conséquences

Les migrations SQL pures ne sont utilisées qu'en cas de besoin spécifique.

---

# ADR-006 - Schéma PostgreSQL dédié

## Décision

Les objets applicatifs sont créés dans un schéma dédié.

## Motivation

Isoler les objets applicatifs du reste de la base.

## Conséquences

Toutes les tables métier sont regroupées dans le même périmètre fonctionnel.

---

# ADR-007 - Utilisation des UUID

## Décision

Les identifiants métier utilisent le type UUID.

## Motivation

Les UUID :

- évitent l'exposition d'identifiants séquentiels ;
- facilitent les évolutions futures ;
- sont nativement supportés par PostgreSQL.

## Conséquences

Les agrégats métier utilisent des UUID comme identifiants.

---

# ADR-008 - Encodage des mots de passe via un port

## Décision

L'encodage des mots de passe est abstrait derrière :

- PasswordEncoderPort

## Motivation

Le métier ne doit pas dépendre :

- de Spring Security ;
- de BCrypt ;
- d'un algorithme technique particulier.

## Conséquences

Le Core reste totalement indépendant de la technologie utilisée.

---

# ADR-009 - BCrypt comme implémentation actuelle

## Décision

L'implémentation actuelle du PasswordEncoderPort repose sur BCrypt.

## Motivation

BCrypt représente une solution robuste et largement éprouvée.

## Conséquences

Il est possible de remplacer BCrypt sans impacter le métier.

---

# ADR-010 - Le mot de passe n'est jamais stocké en clair

## Décision

Le domaine ne manipule jamais un mot de passe persistant en clair.

## Motivation

Respecter les bonnes pratiques de sécurité.

## Conséquences

Seul le hash du mot de passe est stocké.

---

# ADR-011 - Username normalisé

## Décision

Les usernames sont :

- trimés ;
- normalisés en minuscules.

## Motivation

Éviter les doublons fonctionnels.

Exemple :

```text
John.Doe
john.doe
JOHN.DOE
```

représentent le même utilisateur.

## Conséquences

Les recherches sont réalisées sur la forme normalisée.

---

# ADR-012 - Centralisation des règles métier

## Décision

Les règles de validation sont regroupées dans :

- *Rules
- *ValidationUtils

## Motivation

Éviter la duplication des validations.

## Conséquences

Les règles sont réutilisées par plusieurs use cases.

---

# ADR-013 - Utilisation systématique de mappers

## Décision

Les conversions entre domaine et technique passent par des mappers dédiés.

## Motivation

Éviter d'exposer :

- les entités JPA ;
- les DTO HTTP ;
- les objets Spring Security

au domaine métier.

## Conséquences

Chaque couche reste indépendante.

---

# ADR-014 - Assemblage Spring via configuration

## Décision

Les use cases et adaptateurs sont déclarés dans des classes de configuration Spring.

## Motivation

Conserver des use cases indépendants du framework.

## Conséquences

Les use cases ne sont pas annotés :

```java
@Component
@Service
@Repository
```

---

# ADR-015 - Introduction d'une couche Application

## Décision

Les services applicatifs sont regroupés dans :

- auth-server-application

## Motivation

Séparer :

- le métier ;
- les transactions ;
- l'orchestration applicative.

## Conséquences

Les transactions sont gérées dans la couche Application et non dans le Core.

---

# ADR-016 - Séparation Persistence / Security

## Décision

Les responsabilités techniques sont séparées dans deux modules :

- auth-server-persistence
- auth-server-security

## Motivation

Isoler :

- l'accès aux données ;
- les problématiques de sécurité.

## Conséquences

Chaque module possède une responsabilité technique unique.

---

# ADR-017 - Découplage Security / Persistence

## Décision

Le module auth-server-security ne dépend pas du module auth-server-persistence.

## Motivation

Préserver l'architecture hexagonale.

La sécurité doit dépendre des ports métier et non de la technologie de persistance.

## Conséquences

Les composants OAuth2 utilisent :

- OAuth2ClientRepositoryPort
- OAuth2ScopeRepositoryPort

au lieu de dépendre directement de JPA.

---

# ADR-018 - Composition Root centralisée

## Décision

Le module auth-server-boot joue le rôle de Composition Root.

## Motivation

Centraliser l'assemblage de l'application.

## Conséquences

Le module Boot est le seul module autorisé à connaître simultanément :

- web
- application
- persistence
- security

---

# ADR-019 - Mockito pour les tests unitaires

## Décision

Mockito est utilisé pour tester les use cases et services.

## Motivation

Isoler la logique métier des dépendances techniques.

## Conséquences

Les ports sont mockés dans les tests unitaires.

---

# ADR-020 - Stratégie de tests

## Décision

Les tests sont organisés selon plusieurs niveaux :

- validation métier ;
- tests de use cases ;
- tests de services ;
- tests d'intégration lorsque nécessaire.

## Motivation

Conserver une bonne couverture tout en gardant des tests lisibles.

## Conséquences

Chaque couche dispose de ses propres tests.

---

# ADR-021 - Pas de module de test partagé

## Décision

Les utilitaires de test restent dans les modules concernés.

## Motivation

Éviter l'introduction prématurée d'un module supplémentaire.

## Conséquences

Chaque module reste autonome sur ses besoins de test.

---

# ADR-022 - Conformité Sonar pragmatique

## Décision

Les recommandations Sonar sont étudiées au cas par cas.

## Motivation

Privilégier :

- la pertinence métier ;
- la lisibilité ;
- la maintenabilité.

## Conséquences

Les avertissements peuvent être conservés lorsqu'ils sont justifiés et documentés.

---

# Principes directeurs

Le développement du projet est guidé par les principes suivants :

- le métier ne dépend pas des frameworks ;
- les ports appartiennent au Core ;
- les adaptateurs implémentent les détails techniques ;
- les responsabilités sont clairement séparées ;
- les dépendances vont toujours vers le métier ;
- les transactions sont gérées dans la couche Application ;
- la sécurité reste indépendante de la persistance ;
- le code doit rester simple, lisible et testable.