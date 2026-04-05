package com.example.Movies.repository;

import com.example.Movies.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // Retrouver le profil d'un utilisateur spécifique
    Optional<Profile> findByUserId(Long userId);

    // Vérifier si un nom d'utilisateur est déjà pris
    boolean existsByUsername(String username);
}