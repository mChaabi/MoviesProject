package com.example.Movies.dto;

import jakarta.validation.constraints.NotNull;

public record LikeRequestDTO(
        @NotNull(message = "L'ID de l'utilisateur est requis")
        Long userId,

        @NotNull(message = "L'ID du film est requis")
        Long movieId
) {}