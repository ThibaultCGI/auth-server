# Decisions

Ce document recense les principales décisions d'architecture et de conception prises au cours du projet.

L'objectif est de conserver une trace des choix effectués ainsi que de leurs motivations.

---

# ADR-001 - Architecture hexagonale

## Décision

Le projet adopte une architecture hexagonale.

## Motivation

Nous souhaitons :

- isoler le métier de la technique ;
- limiter les dépendances envers les frameworks ;
- faciliter les tests unitaires ;
- permettre l'évolution des choix techniques sans impacter le métier.

## Conséquences

Le domaine dépend uniquement :

- des objets métier ;
- des use cases ;
- des ports.

Les détails techniques sont implémentés dans le module infrastructure.

---

# ADR-002 - Architecture multi-modules

## Décision

Le projet est découpé en plusieurs modules Maven :

- auth-core
- auth-infrastructure
- auth-boot

## Motivation

Séparer clairement :

- le métier ;
- l'infrastructure ;
- le bootstrap Spring Boot.

## Conséquences

Le core ne dépend pas de Spring.

---

# ADR-003 - PostgreSQL

## Décision

La persistance repose sur PostgreSQL.

## Motivation

PostgreSQL fournit :

- une excellente stabilité ;
- un support avancé des types ;
- une gestion native des UUID ;
- un excellent support avec Spring Boot.

## Conséquences

Les entités sont stockées dans une base PostgreSQL locale durant le développement.

---

# ADR-004 - Liquibase

## Décision

Les migrations de base de données sont gérées via Liquibase.

## Motivation

Nous souhaitons :

- versionner le schéma ;
- reproduire facilement un environnement ;
- suivre l'historique des évolutions.

## Conséquences

Toute modification de base doit être réalisée via un changelog Liquibase.

---

# ADR-005 - Format XML pour Liquibase

## Décision

Les changelogs Liquibase sont écrits en XML.

## Motivation

Le format XML :

- est bien supporté par IntelliJ ;
- offre une bonne lisibilité ;
- fournit une validation XSD.

## Conséquences

Les migrations SQL pures ne sont utilisées qu'en cas de besoin spécifique.

---

# ADR-006 - Schéma dédié

## Décision

Les objets applicatifs sont créés dans le schéma :

```text
auth_server
```

## Motivation

Isoler les objets applicatifs du reste de la base.

## Conséquences

Toutes les tables métier sont créées dans ce schéma.

---

# ADR-007 - Utilisation des UUID

## Décision

Les identifiants métier sont de type UUID.

## Motivation

Les UUID :

- évitent l'exposition d'identifiants séquentiels ;
- facilitent les futures évolutions distribuées ;
- sont bien supportés par PostgreSQL.

## Conséquences

Les entités User et Role utilisent des UUID.

---

# ADR-008 - PasswordEncoderPort

## Décision

Le mécanisme d'encodage des mots de passe est abstrait derrière un port :

```
PasswordEncoderPort
```

## Motivation

Le métier ne doit pas dépendre directement :

- de BCrypt ;
- de Spring Security ;
- d'un algorithme particulier.

## Conséquences

L'infrastructure choisit l'implémentation concrète.

---

# ADR-009 - BCrypt

## Décision

L'implémentation actuelle du PasswordEncoderPort repose sur BCrypt.

## Motivation

BCrypt constitue une solution éprouvée et largement utilisée.

## Conséquences

Le domaine n'est pas couplé à BCrypt.

---

# ADR-010 - Le mot de passe n'est jamais stocké en clair

## Décision

Le domaine manipule un attribut :

```
passwordHash
```

## Motivation

Un mot de passe ne doit jamais être stocké en clair.

## Conséquences

Le hash est calculé avant la création du User.

---

# ADR-011 - Username normalisé

## Décision

Les usernames sont normalisés :

- trim ;
- lowercase.

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

Les recherches sont systématiquement réalisées sur le username normalisé.

---

# ADR-012 - Centralisation des règles utilisateur

## Décision

Les règles de validation utilisateur sont centralisées dans :

```text
UserValidationUtils
```

et

```text
UserRules
```

## Motivation

Éviter la duplication entre les use cases.

## Conséquences

Les validations de username et password sont réutilisées par plusieurs cas d'usage.

---

# ADR-013 - Mappers dédiés

## Décision

Les conversions entre domaine et persistance passent par des mappers.

Exemple :

```
UserMapper
```

## Motivation

Éviter d'exposer les entités JPA dans le domaine.

## Conséquences

Le domaine reste indépendant de la persistance.

---

# ADR-014 - Assemblage Spring via configuration

## Décision

Les use cases et adapters sont assemblés dans des classes de configuration Spring.

## Motivation

Conserver des classes métier indépendantes du framework.

## Conséquences

Les use cases ne sont pas annotés :

```
@Component
@Service
```

---

# ADR-015 - Gestion du temps via Clock

## Décision

L'application utilise :

```
Clock
```

injecté par Spring.

## Motivation

Améliorer :

- la testabilité ;
- la reproductibilité ;
- la maîtrise de la notion de temps.

## Conséquences

Les use cases utilisent :

```
LocalDateTime.now(clock)
```

et non :

```
LocalDateTime.now()
```

---

# ADR-016 - UTC comme référence technique

## Décision

Le Clock fourni par Spring utilise :

```
Clock.systemUTC()
```

## Motivation

Éviter les comportements dépendants de la timezone de la machine.

## Conséquences

Les dates techniques sont produites en UTC.

Les conversions de timezone éventuelles seront réalisées aux frontières du système.

---

# ADR-017 - Mockito pour les tests unitaires

## Décision

Mockito est utilisé pour tester les use cases.

## Motivation

Isoler la logique métier.

## Conséquences

Les ports sont mockés dans les tests unitaires.

---

# ADR-018 - Stratégie de tests

## Décision

Les tests sont organisés en plusieurs niveaux :

1. validation des règles métier ;
2. tests d'orchestration ;
3. tests comportementaux lorsque nécessaire.

## Motivation

Limiter la duplication tout en conservant une bonne couverture.

## Conséquences

Les sous-comportements peuvent être testés indépendamment des use cases complets.

---

# ADR-019 - Pas de module de test partagé pour le moment

## Décision

Les constantes et utilitaires de test restent dans chaque module.

## Motivation

La taille actuelle du projet ne justifie pas l'introduction d'un module supplémentaire.

## Conséquences

Chaque module possède ses propres classes de test.

---

# ADR-020 - Conformité Sonar pragmatique

## Décision

Les recommandations Sonar sont étudiées au cas par cas.

## Motivation

Un warning Sonar n'est pas forcément synonyme d'erreur.

## Conséquences

Nous privilégions :

- la pertinence métier ;
- la lisibilité ;
- la maintenabilité.

plutôt qu'une suppression systématique de tous les warnings.