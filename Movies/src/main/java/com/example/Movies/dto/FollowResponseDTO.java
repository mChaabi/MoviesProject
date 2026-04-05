package com.example.Movies.dto;

import java.time.LocalDateTime;

public record FollowResponseDTO(
        Long id,
        Long followingId,
        String email, // On utilise email ici
        LocalDateTime createdAt
) {}