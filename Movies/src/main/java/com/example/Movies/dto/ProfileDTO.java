package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record ProfileDTO(
        Long id,

        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        @Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Le nom d'utilisateur ne peut contenir que des lettres, chiffres, points ou underscores")
        String username,

        @Size(max = 250, message = "La bio ne doit pas dépasser 250 caractères")
        String bio,

        String avatarUrl,

        Long userId // Pour lier le profil à l'utilisateur lors de la création
) {}