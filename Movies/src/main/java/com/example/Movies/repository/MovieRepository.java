package com.example.Movies.repository;

import com.example.Movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Cette méthode permettra de récupérer les films du plus récent au plus ancien
    List<Movie> findAllByOrderByCreatedAtDesc();

    // Pour afficher les films d'un auteur spécifique (profil)
    List<Movie> findByAuthorId(Long authorId);

    // Spring Data JPA générera la requête SQL automatiquement
    List<Movie> findByCategoryId(Long categoryId);
}