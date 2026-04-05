package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieDTO(
        Long id,

        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
        String title,

        @NotBlank(message = "L'URL de la vidéo est obligatoire")
        String videoUrl,

        @Size(max = 500, message = "La description est trop longue")
        String description,

        @NotNull(message = "L'ID de l'auteur est obligatoire")
        Long authorId,

        String authorName // Utile pour l'affichage dans le Front-end sans refaire une requête User


) {}