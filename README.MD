# RSS Reader API

API REST développée avec **Spring Boot** qui lit un flux RSS/Atom à partir de son URL et renvoie les articles au format JSON.

Projet d'apprentissage : l'objectif est de comprendre les concepts de Spring Boot (contrôleurs, services, injection de dépendances, DTO) en ajoutant une fonctionnalité à la fois.

## Technologies

- Java 17 ou 21
- Spring Boot (Spring Web)
- [ROME](https://github.com/rometools/rome) 2.1.0 pour parser les flux RSS et Atom
- Maven

## Lancer le projet

```bash
./mvnw spring-boot:run
```

L'application démarre sur `http://localhost:8080`.

## Endpoint disponible

### `GET /rss`

Récupère les articles d'un flux RSS.

| Paramètre | Obligatoire | Défaut | Description |
|---|---|---|---|
| `url` | oui | - | URL du flux RSS ou Atom |
| `limit` | non | `5` | Nombre maximum d'articles renvoyés |

**Exemple :**

```
http://localhost:8080/rss?url=https://techcrunch.com/category/artificial-intelligence/feed/&limit=3
```

**Réponse :**

```json
[
  {
    "title": "Titre de l'article",
    "link": "https://exemple.com/article",
    "description": "Résumé de l'article",
    "publishedDate": "2026-10-17T08:00:00.000+00:00"
  }
]
```

Si `url` est absent ou si `limit` n'est pas un nombre, Spring répond **400 Bad Request**.

## Structure du projet

```
src/main/java/kaktus/rss_reader_api/
├── controller/
│   └── RssController.java   # reçoit les requêtes HTTP
├── service/
│   └── RssService.java      # télécharge et parse le flux (ROME)
└── dto/
    └── ArticleDto.java      # objet renvoyé au client
```

- **Controller** : gère le HTTP (paramètres, réponse).
- **Service** : contient la logique RSS. Il est injecté dans le contrôleur par le constructeur.
- **DTO** : ne contient que les champs que l'on choisit d'exposer, au lieu de renvoyer l'objet `SyndEntry` de ROME.

## Concepts Spring Boot abordés

- `@RestController` et `@GetMapping`
- `@RequestParam` avec valeur par défaut
- `@Service` et injection de dépendances par constructeur
- Sérialisation JSON automatique (Jackson)
- Pattern DTO avec les `record` Java

## Limites actuelles

- La récupération du flux utilise `XmlReader(URL)`, qui peut échouer avec certains sites (pas de timeout, redirections limitées). Une version avec `HttpClient` est prévue si nécessaire.
- Les erreurs sont gérées par un simple `try/catch` dans le contrôleur et renvoient un faux article avec un code 200.
- Une valeur de `limit` négative n'est pas encore vérifiée.

## Prochaines étapes

- [ ] Gestion propre des erreurs (`@ExceptionHandler`, code 400 si l'URL est invalide)
- [ ] Validation de `limit` (minimum 1)
- [ ] Filtre par mot-clé : `?search=openai`
- [ ] Endpoint `/rss/subject` pour chercher par thème
- [ ] Lecture de plusieurs flux à la fois
- [ ] Sauvegarde de flux favoris (Spring Data JPA)
- [ ] Cache pour éviter de retélécharger le flux à chaque requête
- [ ] Interface front-end