package com.example.Movies.dto;

import java.time.LocalDateTime;

public record LikeResponseDTO(
        Long id,
        Long userId,
        // Pratique pour afficher "Liké par [Nom]"
        Long movieId,
        String movieTitle, // Pratique pour la liste des favoris
        LocalDateTime createdAt
) {}