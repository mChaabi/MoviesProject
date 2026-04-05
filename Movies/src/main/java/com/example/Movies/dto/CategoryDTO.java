package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        Long id,
        @NotBlank(message = "Le nom ne peut pas être vide")
        String name,
        String description
) {}
