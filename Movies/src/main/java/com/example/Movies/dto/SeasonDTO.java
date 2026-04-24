package com.example.Movies.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

// 🆕 NOUVEAU FICHIER
public record SeasonDTO(
        Long id,

        @NotNull(message = "Le numéro de saison est obligatoire")
        Integer seasonNumber,

        String title,
        String description,

        // ID de la série parente
        @NotNull
        Long seriesId,

        // Liste des épisodes
        List<EpisodeDTO> episodes,

        // Nombre total d'épisodes (pour affichage rapide)
        Integer totalEpisodes
) {}
