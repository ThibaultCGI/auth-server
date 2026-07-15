# Architecture du projet

## Objectif

L'objectif du projet est de mettre en œuvre un serveur d'authentification OAuth2/OpenID Connect moderne à l'aide de :

- Java 25
- Spring Boot 4
- Spring Security
- Spring Authorization Server
- PostgreSQL
- Liquibase

Le projet a un objectif pédagogique et vise l'apprentissage :

- d'OAuth2
- d'OpenID Connect
- de Spring Security
- de l'architecture hexagonale

---

# Architecture cible

```text
                    +------------------+
                    |   auth-boot      |
                    |------------------|
                    | Spring Boot      |
                    | Configuration    |
                    | Démarrage        |
                    +---------+--------+
                              |
              +---------------+---------------+
              |                               |
              v                               v

+----------------------+     +---------------------------+
|     auth-core        |     |   auth-infrastructure     |
