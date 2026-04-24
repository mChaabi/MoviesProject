package com.example.Movies.dto;

// 🆕 NOUVEAU FICHIER
public record WatchProgressDTO(
        Long id,
        Long userId,
        Long episodeId,   // null si c'est un film
        Long movieId,     // null si c'est un épisode
        Integer progressPercent,
        Integer lastPositionSeconds,
        Boolean completed
) {}
