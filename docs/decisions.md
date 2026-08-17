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
