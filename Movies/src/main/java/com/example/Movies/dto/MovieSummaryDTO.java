package com.example.Movies.dto;

import jakarta.validation.constraints.NotBlank;

public record MovieSummaryDTO(
        Long id,

        @NotBlank
        String title,

        Integer releaseYear
) {}