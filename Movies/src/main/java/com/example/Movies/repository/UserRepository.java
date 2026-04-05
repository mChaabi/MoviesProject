package com.example.Movies.repository;

import com.example.Movies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring génère automatiquement la requête SQL pour chercher par email
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà (utile pour l'inscription)
    Boolean existsByEmail(String email);
}