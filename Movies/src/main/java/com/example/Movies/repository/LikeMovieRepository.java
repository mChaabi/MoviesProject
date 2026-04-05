package com.example.Movies.repository;

import com.example.Movies.entity.LikeMovie;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LikeMovieRepository extends JpaRepository<LikeMovie, Long> {

    // 1. Trouver un like spécifique par utilisateur et film
    // Utile pour savoir si l'utilisateur actuel a déjà liké ce film
    Optional<LikeMovie> findByUserAndMovie(User user, Movie movie);

    // 2. Compter le nombre de likes pour un film donné
    // Utile pour afficher "124 Likes" sur la fiche du film
    long countByMovie(Movie movie);

    // 3. Vérifier si un like existe (renvoie un boolean)
    boolean existsByUserAndMovie(User user, Movie movie);

    // 4. Récupérer tous les films likés par un utilisateur
    // Utile pour afficher une page "Mes films favoris"
    List<LikeMovie> findByUserId(Long userId);

    // 5. Supprimer un like (Unliked)
    void deleteByUserAndMovie(User user, Movie movie);
}