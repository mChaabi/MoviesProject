package com.example.Movies.dto;
import java.util.Set;



public record TagDetailsDTO(
        Long id,
        String label,
        Set<MovieSummaryDTO> movies // On utilise un autre DTO léger pour Movie pour éviter la récursion
) {}
