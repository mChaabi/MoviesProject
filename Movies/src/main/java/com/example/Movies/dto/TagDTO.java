package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagDTO(
        Long id,

        @NotBlank(message = "Le label du tag est obligatoire")
        @Size(min = 2, max = 50, message = "Le label doit faire entre 2 et 50 caractères")
        String label
) {}