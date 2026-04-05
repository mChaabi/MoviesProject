package com.example.Movies.dto;



import java.time.LocalDateTime;

public record CommentDTO(
        Long id,
        String content,
        Long userId,
        Long movieId,
        LocalDateTime createdAt
) {}