# 🎬 MovieApp

Une application simple pour gérer une bibliothèque de films.

---

## 📸 Project Preview

### 🎬 User Experience
| Authentication | Admin Dashboard |
|---|---|
| <img width="978" height="742" alt="User Authentication - Login Interface" src="https://github.com/user-attachments/assets/504ab153-ec88-4da5-8868-8b1f4e245e8b" />|

### 🔐 Authentication & Onboarding
| User Login | Account Registration |
|---|---|
| <img width="832" height="741" alt="User Onboarding - Registration Flow" src="https://github.com/user-attachments/assets/ea7ed720-00e5-4711-a378-319c72582080" />|
| *Secure authentication with role-based access.* | *Smooth onboarding process for new users.* |

### 📊 Admin & Analytics
| Dashboard Overview | Analytics & Statistics |
|---|---|
|<img width="1882" height="827" alt="Admin Dashboard - Analytics   Overview" src="https://github.com/user-attachments/assets/cf50a384-13a8-4b53-82b3-f173845941b7" />|
| *Centralized hub for managing the movie catalog.* | *Real-time statistics and data visualization.* |

### 📱 Media Details
| Media Details | Content Management |
|---|---|
|<img width="850" height="625" alt="Movie Details View   Media Player Integration" src="https://github.com/user-attachments/assets/55a394d3-9f7e-4d10-9a55-4bb95a7eb106" />|

🛠️ Content Management System (CMS)
| Dashboard Overview | Content Management |
|---|---|
| <img width="1884" height="828" alt="Content Management System (CMS) - Explorer View" src="https://github.com/user-attachments/assets/f1ef0b5b-cc8f-4f01-b542-7dfedf5b547e" />|
| *Real-time statistics and data visualization.* | *Centralized hub for managing the movie catalog.* |

### 🔍 Explore & Discovery
| Media Details & Interaction | Category Filtering |
|---|---|
|<img width="1879" height="823" alt="Dynamic Category Filtering   Catalog Browsing" src="https://github.com/user-attachments/assets/ce9cf109-6f0d-4a68-8797-564c83aeb52d" /> |
| *Interactive modal with full metadata and player access.* | *Dynamic browsing by genre and media type.* |




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

