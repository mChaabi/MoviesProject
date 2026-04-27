package com.example.Movies.dto;

import com.example.Movies.entity.Movie.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;


@Builder
// ✅ MODIFIÉ — Ajout de type, rating, releaseYear, durationMinutes, seasons
public record MovieDTO(
        Long id,

        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
        String title,

        List<SeasonDTO> seasonsDto,  // Pour la liste des saisons
        Integer episodesCount,

        // Nullable car les séries n'ont pas de videoUrl directe
        String videoUrl,
        String coverUrl,      // 🆕

        @Size(max = 500, message = "La description est trop longue")
        String description,

        @NotNull(message = "L'ID de l'auteur est obligatoire")
        Long authorId,

        String authorName,
        String authorEmail,

        Long categoryId,
        String categoryName,

        // 🆕 NOUVEAU
        MediaType type,        // MOVIE ou SERIES
        Double rating,         // Note sur 10
        Integer releaseYear,   // Année de sortie
        Integer durationMinutes, // Durée en minutes (films)

        // 🆕 NOUVEAU : saisons pour les séries
        List<SeasonDTO> seasons,

        // 🆕 NOUVEAU : nombre de saisons (lecture rapide)
        Integer totalSeasons,

        Long authorId_relation // ignoré, juste pour compatibilité mapper

) {}
