# Dependencies

## Objectif

Ce document décrit les principales dépendances utilisées par le projet ainsi que leur rôle.

L'objectif est de comprendre rapidement :

- pourquoi une dépendance est présente ;
- dans quel module elle est utilisée ;
- quel problème elle résout ;
- quelles sont les dépendances autorisées dans chaque module.

---

# Dépendances globales

## Java

### Version

```text
Java 21
```

### Motivation

Utiliser une version LTS récente fournissant :

- les records ;
- les améliorations de performances ;
- les API modernes ;
- un support long terme.

---

## Maven

### Version

```text
Maven 3.9+
```

### Motivation

Gestion :

- du build ;
- des modules ;
- des dépendances ;
- des tests ;
- de l'intégration continue.

---

# Spring Boot

## Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
</dependency>
```

### Module

```text
parent
```

### Rôle

Fournit :

- le dependency management ;
- la gestion cohérente des versions ;
- les réglages recommandés par Spring Boot.

---

# Spring Framework

## Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
```

### Modules

```text
auth-boot
auth-infrastructure
```

### Rôle

Fournit :

- l'injection de dépendances ;
- le conteneur Spring ;
- la gestion des beans ;
- la configuration applicative.

---

# Spring Data JPA

## Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Module

```text
auth-infrastructure
```

### Rôle

Permet :

- le mapping JPA ;
- l'utilisation des repositories Spring Data ;
- l'intégration Hibernate.

### Utilisation actuelle

Exemples :

- UserEntity
- UserJpaRepository

---

# PostgreSQL

## Dépendance

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

### Module

```text
auth-infrastructure
```

### Rôle

Driver JDBC PostgreSQL.

Permet :

- la connexion à la base ;
- l'utilisation des UUID PostgreSQL ;
- l'exécution des requêtes SQL.

---

# Liquibase

## Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-liquibase</artifactId>
</dependency>
```

### Module

```text
auth-infrastructure
```

### Rôle

Intégration de Liquibase avec Spring Boot.

Permet :

- l'exécution automatique des migrations au démarrage ;
- la gestion du changelog ;
- le suivi des migrations déjà exécutées ;
- la synchronisation du schéma de base de données.

### Utilisation actuelle

Les migrations sont écrites au format XML.

Exemples :

```text
changelog.xml
V001-create-users-roles.xml
```

### Règle du projet

Toute modification du schéma doit être réalisée via Liquibase.

Les bases de données partagées ne doivent jamais être modifiées manuellement.

---

# Spring Security

## Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Module

```text
auth-infrastructure
```

### Rôle

Fournit :

- PasswordEncoder ;
- BCrypt ;
- les briques nécessaires aux futures évolutions de sécurité.

### Utilisation actuelle

Principalement :

```
BCryptPasswordEncoder
```

via :

```
PasswordEncoderAdapter
```

---

# Lombok

## Dépendance

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

### Modules

```text
auth-core
auth-infrastructure
auth-boot
```

### Rôle

Réduction du code répétitif.

### Utilisation actuelle

Exemples :

```
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UtilityClass
```

### Remarque

Lombok doit améliorer la lisibilité du code.

La logique métier ne doit jamais être masquée derrière Lombok.

---

# Tests

## Spring Boot Test

### Dépendance

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Rôle

Fournit :

- JUnit 5 ;
- Mockito ;
- AssertJ ;
- Spring Test ;
- Hamcrest.

---

## JUnit 5

### Utilisation

Les tests unitaires sont écrits avec JUnit Jupiter.

Exemples :

```
@Test
@ExtendWith(...)
assertThrows(...)
```

---

## Mockito

### Utilisation

Mockito est utilisé pour :

- mocker les ports ;
- réaliser des spies ;
- simuler des comportements techniques.

Exemples :

```
@Mock
@InjectMocks
@Spy
MockedStatic
```

---

# Dépendances du module auth-core

## Objectif

Le module auth-core doit rester indépendant de tout framework technique.

### Dépendances autorisées

- Lombok
- Mockito (tests)
- JUnit (tests)

### Dépendances interdites

- Spring Framework
- Spring Data JPA
- Hibernate
- PostgreSQL
- Liquibase
- Spring Security

---

# Dépendances du module auth-infrastructure

## Objectif

Implémenter les détails techniques du système.

### Dépendances principales

- Spring Boot
- Spring Data JPA
- PostgreSQL
- Liquibase
- Spring Security
- Lombok

---

# Dépendances du module auth-boot

## Objectif

Assembler l'application finale.

### Dépendances principales

- auth-core
- auth-infrastructure
- Spring Boot

---

# Gestion du temps

Le projet utilise :

```
java.time.Clock
```

injecté par Spring.

### Configuration actuelle

```
Clock.systemUTC()
```

### Motivation

Éviter l'utilisation directe de :

```
LocalDateTime.now()
```

afin d'améliorer :

- la testabilité ;
- la reproductibilité ;
- l'indépendance vis-à-vis du fuseau horaire de la machine.

---

# Dépendances futures envisagées

## OAuth2 Authorization Server

Dépendance envisagée :

```
spring-security-oauth2-authorization-server
```

### Usage prévu

Implémentation :

- d'un Authorization Server OAuth2 ;
- de la gestion des clients ;
- des tokens ;
- des scopes ;
- des refresh tokens.

---

## OpenID Connect

Support envisagé à terme :

```text
OIDC
```

afin de fournir :

- l'identité utilisateur ;
- les claims standard ;
- les endpoints OIDC.

---

# Politique d'ajout de dépendances

Avant d'ajouter une dépendance, il convient de vérifier :

1. si Java fournit déjà une solution adaptée ;
2. si Spring fournit déjà cette fonctionnalité ;
3. si la dépendance répond à un besoin réel ;
4. si elle n'alourdit pas inutilement le projet.

---

# Philosophie générale

Le projet privilégie :

- la simplicité ;
- les bibliothèques éprouvées ;
- le faible couplage ;
- une architecture explicite ;
- la maîtrise du code produit.

L'introduction d'une bibliothèque doit toujours être justifiée par une réelle valeur ajoutée.