# Decisions Log

## Présentation

Ce document recense les principales décisions d'architecture prises au cours du projet.

Chaque décision doit répondre aux questions :

- Quel problème cherchons-nous à résoudre ?
- Quelles étaient les alternatives ?
- Pourquoi ce choix a-t-il été retenu ?
- Quelles conséquences ce choix implique-t-il ?

---

# ADR-001 - Utilisation de Spring Boot

## Date

2026-07-15

## Décision

Utiliser Spring Boot comme socle applicatif.

## Alternatives envisagées

- Jakarta EE
- Micronaut
- Quarkus

## Justification

Le projet est centré sur l'apprentissage de :

- Spring Security
- OAuth2
- OpenID Connect
- Spring Authorization Server

Spring Boot constitue aujourd'hui l'écosystème le plus mature pour ces usages.

---

# ADR-002 - Utilisation de Java 25

## Date

2026-07-15

## Décision

Utiliser Java 25.

## Alternatives envisagées

- Java 21
- Java 24

## Justification

Projet personnel destiné à explorer les fonctionnalités les plus récentes du JDK.

---

# ADR-003 - Utilisation de PostgreSQL

## Date

2026-07-15

## Décision

Utiliser PostgreSQL comme base de données principale.

## Alternatives envisagées

- H2
- MySQL
- MariaDB

## Justification

PostgreSQL est largement utilisé en contexte professionnel et permet de travailler dans un environnement proche de la production.

---

# ADR-004 - Utilisation de Liquibase

## Date

2026-07-15

## Décision

Utiliser Liquibase pour gérer les évolutions de schéma.

## Alternatives envisagées

- Hibernate ddl-auto
- Flyway

## Justification

Les évolutions du schéma doivent être :

- versionnées
- historiques
- reproductibles

## Conséquences

Les migrations devront être créées explicitement.

---

# ADR-005 - Utilisation de Spring Authorization Server

## Date

2026-07-15

## Décision

Utiliser Spring Authorization Server.

## Alternatives envisagées

- Développement complet maison
- Keycloak
- Auth0
- Okta

## Justification

Apprendre les mécanismes OAuth2 tout en conservant une implémentation conforme aux standards.

---

# ADR-006 - Utilisation de JWT

## Date

2026-07-15

## Décision

Utiliser des Access Tokens au format JWT.

## Alternatives envisagées

- Tokens opaques

## Justification

Format largement adopté dans les architectures modernes et distribué.

---

# ADR-007 - Utilisation des profils Spring

## Date

2026-07-15

## Décision

Utiliser des profils Spring.

## Alternatives envisagées

- Configuration unique

## Justification

Séparer les environnements :

- local
- dev
- recette
- production

---

# ADR-008 - Utilisation de Lombok

## Date

2026-07-15

## Décision

Utiliser Lombok pour réduire le code répétitif.

## Alternatives envisagées

- Génération manuelle
- Records uniquement

## Justification

