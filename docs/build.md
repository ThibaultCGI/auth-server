# Build

## Objectif

Ce document décrit :

- les prérequis nécessaires au développement ;
- la structure Maven du projet ;
- les commandes de build ;
- les commandes de lancement ;
- les bonnes pratiques associées.

---

# Prérequis

Les outils suivants doivent être installés :

- Java 21
- Maven 3.9+
- PostgreSQL
- Git

---

# Organisation Maven

Le projet est organisé sous la forme d'un projet Maven multi-modules.

```text
auth-server
├── auth-core
├── auth-infrastructure
└── auth-boot
```

Le `pom.xml` racine référence l'ensemble des modules.

---

# Modules

## auth-core

Contient :

- le domaine ;
- les use cases ;
- les ports ;
- les exceptions métier ;
- les utilitaires métier.

Le module ne dépend d'aucune technologie d'infrastructure.

---

## auth-infrastructure

Contient :

- les entités JPA ;
- les repositories Spring Data ;
- les adapters ;
- Liquibase ;
- les configurations Spring ;
- les implémentations techniques des ports.

---

## auth-boot

Contient :

- la classe de démarrage Spring Boot ;
- les fichiers de configuration ;
- l'assemblage final de l'application.

C'est le module exécutable.

---

# Construction du projet

Depuis la racine :

```bash
mvn clean install
```

Cette commande :

- compile tous les modules ;
- exécute les tests ;
- construit les artefacts Maven.

---

# Construction sans les tests

```bash
mvn clean install -DskipTests
```

Cette commande est réservée aux besoins ponctuels.

Les tests doivent normalement être exécutés dans la CI et avant chaque merge.

---

# Exécution des tests

## Tous les modules

```bash
mvn test
```

---

## Module spécifique

Exemple :

```bash
mvn test -pl auth-core
```

---

# Lancement de l'application

Depuis la racine :

```bash
mvn spring-boot:run -pl auth-boot
```

ou :

```bash
cd auth-boot
mvn spring-boot:run
```

---

# Gestion de la base de données

## Base utilisée

PostgreSQL

---

## Schéma applicatif

```text
auth_server
```

---

## Migrations

Les migrations sont gérées par Liquibase.

Au démarrage de l'application :

1. Spring Boot démarre ;
2. Liquibase vérifie l'historique des migrations ;
3. les migrations manquantes sont appliquées ;
4. l'application poursuit son démarrage.

---

# Intégration continue

À terme, le pipeline CI devra exécuter :

```bash
mvn clean verify
```

Cette commande permettra :

- la compilation ;
- l'exécution des tests ;
- les contrôles de qualité ;
- l'analyse Sonar ;
- la génération du rapport de couverture.

---

# Sonar

Le projet utilise Sonar pour :

- la qualité du code ;
- la détection de bugs potentiels ;
- l'identification des problèmes de maintenabilité ;
- le suivi de la dette technique.

Les remontées Sonar doivent être analysées au cas par cas.

Un warning Sonar ne doit pas être appliqué aveuglément s'il dégrade la lisibilité ou ne correspond pas à un risque réel.

---

# Gestion du temps

Le projet utilise :

```
Clock
```

injecté par Spring.

Configuration actuelle :

```
Clock.systemUTC()
```

Cela garantit :

- des comportements prévisibles ;
- une meilleure testabilité ;
- l'indépendance vis-à-vis du fuseau horaire de la machine.

---

# Couverture de tests

Les tests unitaires utilisent :

- JUnit 5 ;
- Mockito.

Les tests du core :

- ne démarrent pas Spring ;
- mockent les ports ;
- vérifient la logique métier des use cases.

---

# Convention de build

Avant toute Pull Request :

```bash
mvn clean verify
```

doit être exécuté avec succès.

Les conditions minimum pour ouvrir une Pull Request sont :

- compilation réussie ;
- tests verts ;
- analyse Sonar acceptable ;
- documentation mise à jour si nécessaire.

---

# Artefact principal

L'application exécutable est produite par :

```text
auth-boot
```

Ce module assemble :

- le domaine ;
- l'infrastructure ;
- la configuration Spring.

---

# Évolution future

À terme, le build pourra être enrichi avec :

- GitHub Actions ;
- SonarCloud ;
- JaCoCo ;
- vérification des dépendances ;
- génération automatique de documentation ;
- build Docker.
