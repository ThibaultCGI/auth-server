# Decisions

Ce document recense les principales décisions d'architecture et de conception prises au cours du projet.

L'objectif est de :

- conserver une trace des choix effectués ;
- documenter les motivations ;
- faciliter la compréhension de l'architecture ;
- éviter de remettre en question des décisions déjà validées sans raison.

---

# ADR-001 - Architecture hexagonale

## Décision

Le projet adopte une architecture hexagonale (Ports & Adapters).

## Motivation

Nous souhaitons :

- isoler le métier des technologies ;
- faciliter les tests unitaires ;
- limiter les dépendances aux frameworks ;
- permettre l'évolution des choix techniques sans impact sur le métier.

## Conséquences

Le métier dépend uniquement :

- des objets métier ;
- des use cases ;
- des ports.

Les détails techniques sont implémentés dans des adaptateurs spécialisés.

---

# ADR-002 - Architecture multi-modules

## Décision

Le projet est organisé sous la forme des modules Maven suivants :

- auth-server-core
- auth-server-application
- auth-server-openapi
- auth-server-web
- auth-server-persistence
- auth-server-security
- auth-server-boot

## Motivation

Séparer clairement :

- le métier ;
- l'orchestration applicative ;
- les contrats HTTP ;
- l'exposition HTTP ;
- la persistance ;
- la sécurité ;
- le démarrage de l'application.

## Conséquences

Chaque module possède une responsabilité unique et clairement identifiée.

---

# ADR-003 - PostgreSQL

## Décision

La persistance repose sur PostgreSQL.

## Motivation

PostgreSQL fournit :

- une excellente stabilité ;
- une gestion native des UUID ;
- un excellent support Spring ;
- des performances adaptées aux besoins du projet.

## Conséquences

Les données de production et de développement sont stockées dans une base PostgreSQL.

---

# ADR-004 - Liquibase

## Décision

Les migrations de base de données sont gérées via Liquibase.

## Motivation

Nous souhaitons :

- versionner le schéma ;
- reproduire facilement les environnements ;
- historiser les évolutions de la base.

## Conséquences

Toute modification du schéma doit être réalisée via un changelog Liquibase.

---

# ADR-005 - Format XML pour Liquibase

## Décision

Les changelogs Liquibase sont écrits en XML.

## Motivation

Le format XML :

- est bien supporté par les IDE ;
- bénéficie d'une validation XSD ;
- reste lisible et maintenable.

## Conséquences

Les migrations SQL pures ne sont utilisées qu'en cas de besoin spécifique.

---

# ADR-006 - Schéma PostgreSQL dédié

## Décision

Les objets applicatifs sont créés dans un schéma dédié.

## Motivation

Isoler les objets applicatifs du reste de la base.

## Conséquences

Toutes les tables métier sont regroupées dans le même périmètre fonctionnel.

---

# ADR-007 - Utilisation des UUID

## Décision

Les identifiants métier utilisent le type UUID.

## Motivation

Les UUID :

- évitent l'exposition d'identifiants séquentiels ;
- facilitent les évolutions futures ;
- sont nativement supportés par PostgreSQL.

## Conséquences

Les agrégats métier utilisent des UUID comme identifiants.

---

# ADR-008 - Encodage des mots de passe via un port

## Décision

L'encodage des mots de passe est abstrait derrière :

- PasswordEncoderPort

## Motivation

Le métier ne doit pas dépendre :

- de Spring Security ;
- de BCrypt ;
- d'un algorithme technique particulier.

## Conséquences

Le Core reste totalement indépendant de la technologie utilisée.

---

# ADR-009 - BCrypt comme implémentation actuelle

## Décision

L'implémentation actuelle du PasswordEncoderPort repose sur BCrypt.

## Motivation

BCrypt représente une solution robuste et largement éprouvée.

## Conséquences

Il est possible de remplacer BCrypt sans impacter le métier.

---

# ADR-010 - Le mot de passe n'est jamais stocké en clair

## Décision

Le domaine ne manipule jamais un mot de passe persistant en clair.

## Motivation

Respecter les bonnes pratiques de sécurité.

## Conséquences

Seul le hash du mot de passe est stocké.

---

# ADR-011 - Username normalisé

## Décision

Les usernames sont :

- trimés ;
- normalisés en minuscules.

## Motivation

Éviter les doublons fonctionnels.

Exemple :

```text
John.Doe
john.doe
JOHN.DOE
```

représentent le même utilisateur.

## Conséquences

Les recherches sont réalisées sur la forme normalisée.

---

# ADR-012 - Centralisation des règles métier

## Décision

Les règles de validation sont regroupées dans :

- *Rules
- *ValidationUtils

## Motivation

Éviter la duplication des validations.

## Conséquences

Les règles sont réutilisées par plusieurs use cases.

---

# ADR-013 - Utilisation systématique de mappers

## Décision

Les conversions entre domaine et technique passent par des mappers dédiés.

## Motivation

Éviter d'exposer :

- les entités JPA ;
- les DTO HTTP ;
- les objets Spring Security

au domaine métier.

## Conséquences

Chaque couche reste indépendante.

---

# ADR-014 - Assemblage Spring via configuration

## Décision

Les use cases et adaptateurs sont déclarés dans des classes de configuration Spring.

## Motivation

Conserver des use cases indépendants du framework.

## Conséquences

Les use cases ne sont pas annotés :

```
@Component
@Service
@Repository
```

---

# ADR-015 - Introduction d'une couche Application

## Décision

Les services applicatifs sont regroupés dans :

- auth-server-application

## Motivation

Séparer :

- le métier ;
- les transactions ;
- l'orchestration applicative.

## Conséquences

Les transactions sont gérées dans la couche Application et non dans le Core.

---

# ADR-016 - Séparation Persistence / Security

## Décision

Les responsabilités techniques sont séparées dans deux modules :

- auth-server-persistence
- auth-server-security

## Motivation

Isoler :

- l'accès aux données ;
- les problématiques de sécurité.

## Conséquences

Chaque module possède une responsabilité technique unique.

---

# ADR-017 - Découplage Security / Persistence

## Décision

Le module auth-server-security ne dépend pas du module auth-server-persistence.

## Motivation

Préserver l'architecture hexagonale.

La sécurité doit dépendre des ports métier et non de la technologie de persistance.

## Conséquences

Les composants OAuth2 utilisent :

- OAuth2ClientRepositoryPort
- OAuth2ScopeRepositoryPort

au lieu de dépendre directement de JPA.

---

# ADR-018 - Composition Root centralisée

## Décision

Le module auth-server-boot joue le rôle de Composition Root.

## Motivation

Centraliser l'assemblage de l'application.

## Conséquences

Le module Boot est le seul module autorisé à connaître simultanément :

- web ;
- security ;
- persistence.

---

# ADR-019 - Mockito pour les tests unitaires

## Décision

Mockito est utilisé pour tester les use cases et services.

## Motivation

Isoler la logique métier des dépendances techniques.

## Conséquences

Les ports sont mockés dans les tests unitaires.

---

# ADR-020 - Stratégie de tests

## Décision

Les tests sont organisés selon plusieurs niveaux :

- validation métier ;
- tests de use cases ;
- tests de services ;
- tests d'intégration lorsque nécessaire.

## Motivation

Conserver une bonne couverture tout en gardant des tests lisibles.

## Conséquences

Chaque couche dispose de ses propres tests.

---

# ADR-021 - Pas de module de test partagé

## Décision

Les utilitaires de test restent dans les modules concernés.

## Motivation

Éviter l'introduction prématurée d'un module supplémentaire.

## Conséquences

Chaque module reste autonome sur ses besoins de test.

---

# ADR-022 - Conformité Sonar pragmatique

## Décision

Les recommandations Sonar sont étudiées au cas par cas.

## Motivation

Privilégier :

- la pertinence métier ;
- la lisibilité ;
- la maintenabilité.

## Conséquences

Les avertissements peuvent être conservés lorsqu'ils sont justifiés et documentés.

---

# ADR-023 - Introduction du module auth-server-openapi

## Décision

Les contrats HTTP et la documentation OpenAPI sont regroupés dans un module dédié :

- auth-server-openapi

## Motivation

Initialement, la documentation OpenAPI était portée directement par les modules exposant les APIs.

L'introduction d'un module dédié permet de :

- centraliser les contrats HTTP ;
- partager les définitions OpenAPI ;
- réduire le couplage entre la documentation et les implémentations ;
- considérer OpenAPI comme un contrat indépendant.

## Conséquences

Le module :

- auth-server-web

dépend désormais du module :

- auth-server-openapi.

Le module OpenAPI contient notamment :

- les interfaces API ;
- les DTO documentaires ;
- les réponses documentaires ;
- les constantes OpenAPI ;
- les groupes Swagger.

Les implémentations HTTP restent localisées dans le module :

- auth-server-web.

Le module OpenAPI dépend uniquement de :

- auth-server-core.

L'architecture distingue désormais explicitement :

- le contrat métier (`auth-server-core`) ;
- le contrat HTTP (`auth-server-openapi`) ;
- les implémentations techniques (`web`, `security`, `persistence`).

---

---

# ADR-024 - Adoption de Spring Authorization Server

## Décision

Le serveur d'autorisation repose sur :

```text
Spring Authorization Server
```

## Motivation

Nous souhaitons :

- nous appuyer sur une implémentation standard ;
- limiter le code spécifique ;
- bénéficier des évolutions de Spring Security ;
- rester conforme à OAuth 2.1 et OpenID Connect.

## Conséquences

Les endpoints standards sont fournis par Spring Authorization Server :

```text
/oauth2/authorize
/oauth2/token
/oauth2/jwks
/.well-known/openid-configuration
```

L'intégration métier est réalisée via des composants spécialisés.

---

# ADR-025 - Adoption d'OpenID Connect

## Décision

Le projet supporte OpenID Connect en complément d'OAuth2.

## Motivation

OAuth2 permet de gérer les autorisations mais ne fournit pas d'identité utilisateur standardisée.

OpenID Connect apporte :

- l'identification de l'utilisateur ;
- l'ID Token ;
- les claims standardisées ;
- les endpoints de découverte.

## Conséquences

Le projet agit désormais comme :

```text
OAuth2 Authorization Server
+
OpenID Provider
```

Le scope système suivant est supporté :

```text
openid
```

---

# ADR-026 - Utilisation de l'Authorization Code Flow

## Décision

Le projet supporte le flow :

```text
Authorization Code
```

## Motivation

Ce flow constitue aujourd'hui la recommandation standard pour les applications Web.

Il permet :

- l'authentification utilisateur ;
- l'obtention d'un Access Token ;
- l'obtention d'un ID Token ;
- une bonne séparation entre client et fournisseur d'identité.

## Conséquences

Le projet expose :

```text
/oauth2/authorize
/oauth2/token
```

et gère les sessions utilisateur nécessaires à ce flow.

---

# ADR-027 - Utilisation systématique de PKCE

## Décision

Le projet supporte PKCE pour le flow Authorization Code.

## Motivation

PKCE réduit les risques liés à l'interception des Authorization Codes.

Il s'agit aujourd'hui de la recommandation de sécurité standard.

## Conséquences

Les clients peuvent transmettre :

```text
code_challenge
code_challenge_method
```

lors de la demande d'autorisation.

Le serveur valide ensuite :

```text
code_verifier
```

lors de l'échange du code.

---

# ADR-028 - Utilisation d'ID Tokens JWT

## Décision

Les informations d'identité sont transmises via des ID Tokens JWT.

## Motivation

OpenID Connect repose sur les ID Tokens pour transporter l'identité utilisateur vers les clients.

Les JWT permettent :

- une signature cryptographique ;
- une interopérabilité standard ;
- une validation locale par les clients.

## Conséquences

Les clients OIDC reçoivent :

```text
Access Token
+
ID Token
```

lorsqu'ils demandent le scope :

```text
openid
```

---

# ADR-029 - Exposition des clés publiques via JWKS

## Décision

Les clés publiques de signature sont exposées via :

```text
/oauth2/jwks
```

## Motivation

Permettre aux clients OAuth2/OIDC de vérifier la signature des JWT émis.

Cette approche est conforme aux standards OpenID Connect.

## Conséquences

Les clients peuvent découvrir automatiquement les clés publiques nécessaires à la validation des tokens.

---

# ADR-030 - Découverte automatique OIDC

## Décision

Le projet expose :

```text
/.well-known/openid-configuration
```

## Motivation

Permettre aux clients OIDC de découvrir automatiquement :

- l'issuer ;
- les endpoints OAuth2 ;
- les endpoints OIDC ;
- le JWKS endpoint.

## Conséquences

Les clients Spring Security peuvent être configurés grâce à :

```properties
spring.security.oauth2.client.provider.xxx.issuer-uri=...
```

sans devoir renseigner individuellement chaque endpoint.

---

# ADR-031 - Distinction entre scopes métier et scopes système

## Décision

Le projet distingue :

- les scopes métier ;
- les scopes système.

## Motivation

Certains scopes possèdent une signification fonctionnelle.

Exemple :

```text
trs:produit-api.read
```

D'autres activent des fonctionnalités du protocole.

Exemple :

```text
openid
```

## Conséquences

Le scope :

```text
openid
```

est traité de manière spécifique.

Il n'est pas préfixé par un code application.

---

# ADR-032 - SecurityFilterChain spécialisées

## Décision

La sécurité est organisée autour de plusieurs SecurityFilterChain.

## Motivation

Séparer clairement :

- les endpoints OAuth2/OIDC ;
- l'authentification utilisateur ;
- les APIs REST.

## Conséquences

Trois chaînes de sécurité existent.

### Order 1

```text
OAuth2 / OIDC
```

### Order 2

```text
/login
```

### Order 3

```text
API REST
```

Cette séparation améliore la lisibilité et limite les effets de bord liés à la sécurité.

---

# ADR-033 - Validation fonctionnelle via un client OIDC dédié

## Décision

Un client de démonstration dédié est utilisé pour valider l'interopérabilité du serveur.

Projet concerné :

```text
test-oauth2-client
```

## Motivation

Tester le serveur d'autorisation avec un client réel.

Valider :

- OAuth2 ;
- Authorization Code ;
- PKCE ;
- OpenID Connect ;
- Discovery ;
- JWKS.

## Conséquences

Toute évolution du serveur peut être validée au travers d'un scénario utilisateur complet.

---

# ADR-034 - Persistance des clés de signature JWT

## Décision

Les clés RSA utilisées pour signer les JWT sont stockées dans un keystore PKCS12.

## Motivation

Initialement, les clés RSA étaient générées à chaque démarrage de l'application.

Cette approche présentait plusieurs inconvénients :

- invalidation des JWT après chaque redémarrage ;
- instabilité du endpoint JWKS ;
- comportement éloigné d'un Authorization Server de production ;
- impossibilité de préparer une stratégie de rotation de clés.

## Conséquences

Les clés RSA sont désormais :

- générées une seule fois ;
- stockées dans un keystore PKCS12 ;
- chargées au démarrage de l'application ;
- utilisées pour construire le JWKSet exposé par le serveur.

Exemple :

```text
auth-server.p12
├── auth-server
└── old-key
```

Les JWT restent valides après le redémarrage du serveur tant que la clé active demeure inchangée.

---

# ADR-035 - Support de la rotation des clés JWT

## Décision

Le serveur supporte plusieurs clés RSA simultanément.

Une clé active est utilisée pour signer les nouveaux JWT tandis que les anciennes clés continuent d'être exposées via le endpoint JWKS.

## Motivation

Permettre la rotation des clés de signature sans invalider immédiatement les tokens déjà émis.

Cette approche est celle utilisée par les principaux fournisseurs OAuth2/OpenID Connect.

## Conséquences

Le serveur distingue :

- les clés publiées ;
- la clé active de signature.

La clé active est configurée via :

```properties
jwt.keystore.active-alias=auth-server
```

Les nouveaux JWT sont signés avec cette clé.

Les anciennes clés restent publiées afin de permettre la validation des anciens tokens.

Exemple :

```text
Keystore
├── old-key
└── auth-server
```

```text
Clé active
└── auth-server
```

```text
JWKS
├── old-key
└── auth-server
```

Les nouveaux JWT possèdent un header similaire à :

```json
{
  "alg": "RS256",
  "kid": "auth-server"
}
```

Cette organisation prépare l'introduction future d'une véritable stratégie de rotation des clés cryptographiques.

--- 

# Principes directeurs

Le développement du projet est guidé par les principes suivants :

- le métier ne dépend pas des frameworks ;
- les ports appartiennent au Core ;
- les adaptateurs implémentent les détails techniques ;
- les responsabilités sont clairement séparées ;
- les dépendances vont toujours vers le métier ;
- les transactions sont gérées dans la couche Application ;
- la sécurité reste indépendante de la persistance ;
- les contrats HTTP sont centralisés dans le module OpenAPI ;
- le code doit rester simple, lisible et testable.
