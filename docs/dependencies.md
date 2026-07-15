# Dépendances du projet Auth Server

## Présentation

Ce projet a pour objectif de mettre en œuvre un serveur d'authentification OAuth2/OpenID Connect avec Spring Boot.

Les dépendances ci-dessous constituent le socle technique de l'application.

---

# Dépendances de production

## Spring Data JPA

### Dépendance

```xml
<artifactId>spring-boot-starter-data-jpa</artifactId>
```

### Outil principal

- Spring Data JPA
- Hibernate

### Rôle

Permet l'accès à la base de données via le paradigme ORM (Object Relational Mapping).

### Utilisation

- Déclaration des entités JPA
- Repositories
- Gestion des transactions
- Génération des requêtes SQL

### Concepts à maîtriser

- `@Entity`
- `@Id`
- `@OneToMany`
- `@ManyToOne`
- `JpaRepository`

---

## Spring Security

### Dépendance

```xml
<artifactId>spring-boot-starter-security</artifactId>
```

### Outil principal

- Spring Security

### Rôle

Fournit les mécanismes de sécurité de l'application.

### Utilisation

- Authentification
- Autorisation
- Gestion des rôles
- Protection des endpoints HTTP

### Concepts à maîtriser

- `SecurityFilterChain`
- `Authentication`
- `AuthenticationProvider`
- `SecurityContext`

---

## Spring Authorization Server

### Dépendance

```xml
<artifactId>spring-boot-starter-security-oauth2-authorization-server</artifactId>
```

### Outil principal

- Spring Authorization Server

### Rôle

Implémentation du serveur OAuth2/OpenID Connect.

### Utilisation

- Émission de JWT
- Gestion des clients OAuth2
- Gestion des scopes
- OpenID Connect

### Endpoints fournis

```text
/oauth2/authorize
/oauth2/token
/oauth2/jwks
/oauth2/revoke
/oauth2/introspect
```

### Concepts à maîtriser

- OAuth2
- OpenID Connect
- JWT
- Client Credentials
- Authorization Code
- PKCE

---

## Spring Validation

### Dépendance

```xml
<artifactId>spring-boot-starter-validation</artifactId>
```

### Outil principal

- Jakarta Validation
- Hibernate Validator

### Rôle

Valide automatiquement les données reçues par l'application.

### Utilisation

- Validation de DTO
- Vérification des formats
- Contrôle des données utilisateur

### Annotations courantes

```java
@NotNull
@NotBlank
@Email
@Size
```

---

## Spring MVC

### Dépendance

```xml
<artifactId>spring-boot-starter-webmvc</artifactId>
```

### Outil principal

- Spring MVC

### Rôle

Expose les APIs HTTP de l'application.

### Utilisation

- Contrôleurs REST
- Mapping HTTP
- Sérialisation JSON

### Annotations courantes

```java
@RestController
@RequestMapping
@GetMapping
@PostMapping
```

---

## PostgreSQL Driver

### Dépendance

```xml
<artifactId>postgresql</artifactId>
```

### Outil principal

- PostgreSQL JDBC Driver

### Rôle

Permet à l'application de communiquer avec PostgreSQL.

### Utilisation

- Connexion à la base
- Exécution des requêtes SQL
- Communication avec Hibernate

---

## Liquibase

### Dépendance

```xml
<artifactId>liquibase-core</artifactId>
```

### Outil principal

- Liquibase

### Rôle

Gère le versionnement du schéma de base de données.

### Utilisation

- Création des tables
- Évolution du schéma
- Historisation des migrations

### Concepts à maîtriser

- Changelog
- Changeset
- Migration
- Rollback

### Pourquoi est-il utilisé ?

Remplace l'utilisation de :

```properties
spring.jpa.hibernate.ddl-auto=update
```

Les évolutions du schéma deviennent traçables et reproductibles.

---

## Lombok

### Dépendance

```xml
<artifactId>lombok</artifactId>
```

### Outil principal

- Project Lombok

### Rôle

Réduit le code répétitif.

### Utilisation

- Getters / Setters
- Builders
- Constructeurs

### Annotations courantes

```java
@Getter
@Setter
@Builder
@RequiredArgsConstructor
```

### Remarque

Lombok est utilisé principalement pour accélérer le développement.
Les Records Java peuvent parfois constituer une alternative plus moderne pour les DTO.

---

# Dépendances de test

## Tests JPA

Permettent de tester :

- Entités
- Repositories
- Persistance

---

## Tests Spring Security

Permettent de tester :

- Authentification
- Autorisation
- Règles de sécurité

Annotations utiles :

```java
@WithMockUser
```

---

## Tests Authorization Server

Permettent de vérifier :

- Génération des tokens
- Flows OAuth2
- Configuration OIDC

---

## Tests Validation

Permettent de vérifier :

- Contraintes Jakarta Validation
- Cohérence des DTO

---

## Tests Web MVC

Permettent de simuler des requêtes HTTP.

Outil principal :

```java
MockMvc
```

Exemples :

```http
GET /users
POST /oauth2/token
```

---

# Architecture cible

```text
Client
    │
    ▼
Authorization Server
    │
    ├── Spring Security
    ├── OAuth2 Authorization Server
    ├── Spring MVC
    ├── Validation
    ├── JPA / Hibernate
    ├── PostgreSQL
    └── Liquibase
```

---

# Ordre d'apprentissage recommandé

1. Spring MVC
2. Spring Security
3. JPA / Hibernate
4. PostgreSQL
5. Liquibase
6. OAuth2
7. OpenID Connect
8. JWT
9. Spring Authorization Server