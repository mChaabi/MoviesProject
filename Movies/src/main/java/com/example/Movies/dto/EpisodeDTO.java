package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 🆕 NOUVEAU FICHIER
public record EpisodeDTO(
        Long id,

        @NotNull
        Integer episodeNumber,

        @NotBlank(message = "Le titre est obligatoire")
        String title,

        String description,
        String videoUrl,
        Integer durationMinutes,

        // ID de la saison parente
        @NotNull
        Long seasonId
) {}
