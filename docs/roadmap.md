# Roadmap

## Phase 0 - Architecture

### Objectifs

- [ ] Transformer le projet en multi-modules Maven
- [ ] Créer le module auth-core
- [ ] Créer le module auth-infrastructure
- [ ] Créer le module auth-boot
- [ ] Vérifier le démarrage de l'application

---

## Phase 1 - Initialisation

### Objectifs

- [x] Créer le projet Spring Boot
- [x] Configurer Maven
- [x] Ajouter les dépendances principales
- [x] Ajouter PostgreSQL
- [x] Ajouter Liquibase
- [x] Créer la documentation technique

---

## Phase 2 - Base de données

### Objectifs

- [ ] Configurer Liquibase
- [ ] Créer le changelog principal
- [ ] Créer la première migration
- [ ] Créer la table USERS
- [ ] Créer la table ROLES

---

## Phase 3 - Gestion des utilisateurs

### Objectifs

- [ ] Créer le modèle métier User
- [ ] Créer UserRepositoryPort
- [ ] Créer l'implémentation JPA
- [ ] Créer UserService
- [ ] Ajouter BCrypt

---

## Phase 4 - Spring Security

### Objectifs

- [ ] Comprendre SecurityFilterChain
- [ ] Créer SecurityConfig
- [ ] Protéger les endpoints
- [ ] Tester les mécanismes d'authentification

---

## Phase 5 - Authorization Server

### Objectifs

- [ ] Configurer Spring Authorization Server
- [ ] Déclarer un premier client OAuth2
- [ ] Configurer JWT
- [ ] Exposer les endpoints OAuth2

---

## Phase 6 - Premier Access Token

### Objectifs

- [ ] Implémenter Client Credentials Grant
