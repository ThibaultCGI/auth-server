# Roadmap

## Objectif

Ce document décrit l'état d'avancement du projet et les évolutions envisagées.

L'objectif du projet est de construire un serveur d'autorisation moderne basé sur :

- Java 25
- Spring Boot 4
- Spring Security
- Spring Authorization Server
- PostgreSQL
- Liquibase

tout en conservant une architecture hexagonale modulaire.

---

# État actuel

## Architecture

✅ Terminé

### Réalisations

- Architecture hexagonale
- Architecture multi-modules
- Découpage métier / sécurité / persistance
- Composition Root centralisée
- Séparation Security / Persistence

Modules actuels :

```text
auth-server-core
auth-server-application
auth-server-web
auth-server-persistence
auth-server-security
auth-server-boot
```

---

## Gestion des utilisateurs

✅ Terminé

### Fonctionnalités

- Création d'utilisateur
- Recherche d'utilisateur
- Authentification utilisateur
- Encodage des mots de passe
- Validation métier des données utilisateur

---

## Gestion des rôles

✅ Terminé

### Fonctionnalités

- Création de rôles
- Suppression de rôles
- Attribution de rôles à un utilisateur
- Consultation des rôles utilisateur

---

## Gestion des applications

✅ Terminé

### Fonctionnalités

- Création d'applications
- Recherche d'applications

---

## Gestion OAuth2

✅ Première version terminée

### Fonctionnalités

- Création de clients OAuth2
- Recherche de clients OAuth2
- Création de scopes OAuth2
- Attribution de scopes à un client
- Authentification des clients
- Flux Client Credentials
- Génération de JWT

---

# Priorités court terme

## Bean Validation

### Objectif

Ajouter une validation standard des requêtes HTTP.

### Fonctionnalités

- @NotBlank
- @Size
- @Valid
- Gestion centralisée des erreurs de validation

### État

📋 Prévu

---

## Tests d'intégration

### Objectif

Compléter les tests unitaires existants par des tests d'intégration.

### Fonctionnalités

- Tests API REST
- Tests Spring Security
- Tests Persistence
- Tests OAuth2

### État

📋 Prévu

---

## Documentation

### Objectif

Finaliser la documentation du projet.

### Fonctionnalités

- Mise à jour des diagrammes
- Documentation d'architecture
- Documentation OAuth2
- Documentation OpenAPI

### État

🚧 En cours

---

# Priorités moyen terme

## Refresh Tokens

### Objectif

Supporter le renouvellement des jetons.

### Fonctionnalités

- Refresh Token
- Rotation des refresh tokens
- Gestion des expirations

### État

📋 Prévu

---

## Révocation de tokens

### Objectif

Permettre l'invalidation de jetons avant leur expiration.

### Fonctionnalités

- Endpoint de révocation
- Gestion du cycle de vie des tokens

### État

📋 Prévu

---

## Introspection

### Objectif

Permettre à une ressource protégée de vérifier un token.

### Fonctionnalités

- Endpoint d'introspection OAuth2

### État

📋 Prévu

---

# Priorités long terme

## OpenID Connect

### Objectif

Transformer le serveur OAuth2 en fournisseur d'identité compatible OIDC.

### Fonctionnalités

- ID Token
- UserInfo Endpoint
- Discovery Endpoint
- Claims standards
- Support OIDC complet

### État

📋 Prévu

---

## Administration avancée

### Objectif

Faciliter l'exploitation de la plateforme.

### Fonctionnalités

- Rotation des secrets OAuth2
- Audit
- Historisation des opérations
- Gestion avancée des clients

### État

📋 Prévu

---

## Industrialisation

### Objectif

Préparer une utilisation dans un environnement plus proche de la production.

### Fonctionnalités

- Docker
- Docker Compose
- GitHub Actions
- Pipeline CI/CD
- Monitoring
- Observabilité

### État

📋 Prévu

---

# Vision cible

```text
✅ Architecture hexagonale
✅ Gestion utilisateurs
✅ Gestion rôles
✅ Spring Security
✅ OAuth2 Authorization Server
✅ Client Credentials

⬜ Refresh Tokens
⬜ Token Revocation
⬜ Token Introspection
⬜ OpenID Connect
⬜ Audit
⬜ Industrialisation avancée
```

---

# Prochaine étape recommandée

L'étape qui apporte aujourd'hui le plus de valeur est :

```text
Bean Validation
        ↓
Tests d'intégration
        ↓
Refresh Tokens
```

Ces travaux amélioreront la robustesse du projet avant d'aborder OpenID Connect et les fonctionnalités OAuth2 avancées.