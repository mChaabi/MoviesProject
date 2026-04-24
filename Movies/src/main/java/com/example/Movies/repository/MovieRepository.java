package com.example.Movies.repository;

import com.example.Movies.entity.Movie;
import com.example.Movies.entity.Movie.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// ✅ MODIFIÉ — Ajout des méthodes de filtre par type
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Existant
    List<Movie> findAllByOrderByCreatedAtDesc();
    List<Movie> findByCategoryId(Long categoryId);

    // 🆕 Nouveaux
    List<Movie> findByTypeOrderByCreatedAtDesc(MediaType type);
    List<Movie> findByCategoryIdAndType(Long categoryId, MediaType type);
}
