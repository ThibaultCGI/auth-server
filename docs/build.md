# Configuration du Build

## Présentation

Cette page documente les plugins Maven utilisés pour construire l'application.

Les plugins Maven interviennent durant les différentes phases du cycle de vie Maven :

```text
validate
compile
test
package
verify
install
deploy
```

Ils permettent d'ajouter des traitements lors du build.

---

# Spring Boot Maven Plugin

## Plugin

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

## Outil principal

Spring Boot Maven Plugin

## Rôle

Permet à Maven de construire et exécuter une application Spring Boot.

---

## Fonctionnalités

### Création d'un exécutable autonome

Le plugin produit un JAR exécutable contenant :

- l'application
- les dépendances
- le serveur embarqué

L'application peut être démarrée via :

```bash
java -jar authserver.jar
```

---

### Exécution de l'application

Le plugin fournit également :

```bash
mvn spring-boot:run
```

qui démarre l'application directement depuis les sources.

---

### Repackaging

Pendant la phase :

```text
package
```

le plugin transforme :

```text
JAR Maven classique
```

en :

```text
JAR Spring Boot exécutable
```

---

## Configuration présente dans le projet

```xml
<configuration>
    <excludes>
        <exclude>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </exclude>
    </excludes>
</configuration>
```

---

## Pourquoi exclure Lombok ?

Lombok est uniquement utilisé à la compilation.

Il ne doit pas être embarqué dans le JAR final car :

- il n'est plus nécessaire à l'exécution ;
- cela réduit légèrement la taille du livrable.

---

# Maven Compiler Plugin

## Plugin

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
</plugin>
```

## Outil principal

Maven Compiler Plugin

## Rôle

Permet à Maven de compiler le code source Java.

---

## Fonctionnalités

### Compilation du code

Durant la phase :

```text
compile
```

le plugin transforme :

```text
*.java*```

en :

```text
*.class
```

--*

### Compilation des tests

Duran* la phase :

```text
test*compile
```

le plugin compile les*classes de test*

---

### Gestion des Annotation *rocessors

Le projet utilise :

``*text
Project Lombok
```

qui repos* sur*un mécanisme d'annotation processi*g.

Exem*les :

```java
@Getter
@Setter
@Bu*lder
*``

Ces annotations génèrent du co*e pendant la compilation.

---

##*Configuration*présente*
### Compilation principale

```xm*
<execution>
    <id>default-compi*e*/id>
</execution>
```

Cette ex*cution est utilisée pour la compil*tion des sources de production.

-*-

### Compilation des tests

```x*l
<execution>
    <id>default-test*ompile</id>
</execution>
```

Cett* exécution est utilisée pour la co*pilation des tests.

---

### Anno*ation Processor Lombok

```xml
<an*otationProcessorPaths>
    <path>
*       <groupId>org.projectlombok<*groupId>
        <artifactId>lombo*</artifactId>
    </path>
</annota*ionProcessorPaths>
```

Cette conf*guration indique au compilateur :
*```text
Utiliser Lombok pendant la*compilation
```

afin de générer a*tomatiquement :

- Getters
- Sette*s
- Builders
* Constructeurs

---

# Cycle de bu*ld du projet

## Compilation

```b*sh
mvn compile
*``

Actions :

```text
- Compilati*n Java
* Exécution de Lombok
```

*--

*# Exécution des tests

```bash
mvn*test
```

Actions :

```text
- Com*ilation des tests
- Exécution*des tests unitaires
```

---

## C*éation*du JAR

```bash
mvn package
```

A*tions :

*``text
- Compilation
- Tests
- Gén*ration du*JAR Spring Boot
```

---

## Insta*lation locale

```bash
mvn install*```

Actions :

```text
- Package
* Installation dans le repository M*ven local
```

---

# Faut-il*conserver cette configuration ?

#* Spring Boot Maven Plugin

✅ Oui

*ndispensable pour :

```text
- Mav*n
- Spring Boot
- Création du*JAR*exécutable
```

---

## Maven Comp*ler Plugin

✅ Oui*
Indispensable pour :

```text
- J*va 25
- Lombok
- Annotation Proces*ing
```

---

#*Évolutions possibles

Lorsque le*projet grandira, il*pourra être pertinent d'ajouter :
*## Maven Sure*ire Plugin

Tests unitaires.

```t*xt
mvn test
```

---

## Maven Fai*safe Plugin

Tests d'intégration.
*```text
mvn verify*```

---

## JaCoCo

Couverture*de code.

```text
Rap*orts*de couverture
```

---

## Spot**gs

Analyse statique du code.

```*ext
Détection de bugs potentiels
`*`

---

##*Checkstyle

Contrôle du*respect*des conventions de codage.

---

#* PMD

Analyse de qualité du code.
*---

# Philosophie du projet

L'ob*ectif est*de*conserver un build simple :

```te*t
Maven
│
*── Spring Boot Maven Plugin
└── Ma*en Compiler Plugin
*``

et d'ajouter progressivement l*s autres outils lorsque les besoin* appara*tront.