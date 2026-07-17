# Décisions d’architecture

Ce document trace les principales décisions prises pendant la construction du projet.

---

## ADR-001 — Architecture hexagonale multi-modules

### Décision

Le projet est structuré en plusieurs modules Maven :

- `auth-core`
- `auth-infrastructure`
- `auth-boot`

### Raison

Cette séparation permet :

- d’isoler le domaine métier ;
- de limiter le couplage aux technologies ;
- de faciliter les tests ;
- d’améliorer la lisibilité et la maintenabilité.

### Conséquence

Le core ne dépend pas de Spring, de JPA ou de PostgreSQL.

---

## ADR-002 — Utilisation de Liquibase XML

### Décision

Les migrations sont écrites en XML avec Liquibase.

### Raison

Le format XML permet :

- une bonne lisibilité ;
- une bonne intégration dans IntelliJ ;
- une versionnage facile ;
- une bonne traçabilité des changements de schéma.

### Conséquence

Les changements de base de données sont versionnés dans :