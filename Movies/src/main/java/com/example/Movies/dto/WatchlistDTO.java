package com.example.Movies.dto;

import java.time.LocalDateTime;

// 🆕 NOUVEAU FICHIER
public record WatchlistDTO(
        Long id,
        Long userId,
        Long movieId,
        String movieTitle,
        String movieType,   // MOVIE ou SERIES
        LocalDateTime addedAt
) {}
