# Configuration du build

## Présentation

Ce document décrit la configuration Maven du projet et le rôle des plugins utilisés pour construire l'application.

Le projet est organisé en **multi-modules Maven** afin de séparer clairement :

- le cœur métier ;
- la couche d'infrastructure ;
- le module de démarrage Spring Boot.

Cette organisation améliore :

- la lisibilité ;
- la maintenabilité ;
- la testabilité ;
- la séparation des responsabilités.

---

# Structure Maven cible

```text
auth-server
│
├── pom.xml
├── auth-core
│   └── pom.xml
├── auth-infrastructure
│   └── pom.xml
└── auth-boot
    └── pom.xml
```

---

# Rôle du POM parent

Le `pom.xml` racine joue le rôle de **parent Maven**.

## Responsabilités

- centraliser la version du projet ;
- déclarer les modules ;
- partager les propriétés communes ;
- gérer les versions des dépendances ;
- gérer la configuration commune des plugins.

## Ce que le parent ne doit pas faire

Le parent ne doit pas transformer tous les modules en applications Spring Boot exécutables.

En particulier :

- `auth-core` doit rester un module bibliothèque ;
- `auth-infrastructure` doit rester un module technique réutilisable ;
- seul `auth-boot` doit produire l'application exécutable.

---

# Plugin Spring Boot Maven

## Outil principal

- Spring Boot Maven Plugin

## Rôle

Ce plugin permet :

- de construire un JAR exécutable Spring Boot ;
- de lancer l'application via Maven ;
- de préparer le packaging final de l'application.

## Utilisation correcte dans ce projet

Le plugin Spring Boot Maven doit être configuré **uniquement dans le module `auth-boot`**, car ce module contient :

- la classe `@SpringBootApplication` ;
- le point d'entrée de l'application ;
- l'assemblage final du serveur d'authentification.

## Pourquoi ne pas le mettre dans le parent ?

Si ce plugin est appliqué au parent Maven, il risque d’être exécuté sur tous les sous-modules, y compris :

- `auth-core`
- `auth-infrastructure`

Or ces modules n'ont pas vocation à être exécutables en tant qu'application Spring Boot autonome.

Cela provoque des erreurs du type :

```text
Unable to find main class
```

---

# Maven Compiler Plugin

## Outil principal

- Maven Compiler Plugin

## Rôle

Ce plugin gère la compilation du code Java.

Il permet notamment de :

- compiler le code source ;
- compiler les tests ;
- déclarer la version Java cible ;
- activer les annotation processors.

## Utilisation dans ce projet

La configuration du compilateur peut être centralisée dans le POM parent afin d’appliquer les mêmes règles à tous les modules.

Cela permet de garantir :

- une version Java cohérente ;
- une configuration uniforme ;
