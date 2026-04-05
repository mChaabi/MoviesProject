package com.example.Movies.repository;

import com.example.Movies.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // Méthode utile pour vérifier si un tag existe déjà par son label (unique)
    Optional<Tag> findByLabel(String label);

    // Vérifier l'existence pour éviter les doublons avant insertion
    boolean existsByLabel(String label);
}