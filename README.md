# 🎬 MovieApp

Une application simple pour gérer une bibliothèque de films.

---

## 🛠️ Technologies utilisées

- **Backend** : Java 21, Spring Boot 3, Spring Data JPA, Spring Validation
- **Frontend** : Angular, Tailwind CSS, RxJS
- **Base de données** : PostgreSQL

---

## 🚀 Lancer le projet

### Backend

```bash
cd movie-backend
mvn spring-boot:run
```

> Configurer d'abord `application.properties` avec vos identifiants PostgreSQL :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/movie_db
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
```

### Frontend

```bash
cd movie-frontend
npm install
ng serve
```

> Ouvrir le navigateur sur `http://localhost:4200`

---

## 📡 API Endpoints

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/movies` | Liste tous les films |
| GET | `/api/movies/{id}` | Détails d'un film |
| POST | `/api/movies` | Ajouter un film |
| PUT | `/api/movies/{id}` | Modifier un film |
| DELETE | `/api/movies/{id}` | Supprimer un film |

---

## ✅ Règles de validation

- Le titre ne peut pas être vide
- L'année de sortie doit être dans le passé ou le présent
- La note doit être entre 0 et 10

---

