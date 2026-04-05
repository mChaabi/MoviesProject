package com.example.Movies.repository;

import com.example.Movies.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Pour vérifier si l'abonnement existe déjà
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // Pour compter les abonnés d'un profil
    long countByFollowingId(Long followingId);

    // Pour compter les abonnements d'un utilisateur
    long countByFollowerId(Long followerId);

    // Pour récupérer la liste des gens que je suis
    List<Follow> findByFollowerId(Long followerId);


}