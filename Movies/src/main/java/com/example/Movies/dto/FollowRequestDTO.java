package com.example.Movies.dto;

public record FollowRequestDTO(
        Long followerId,
        Long followingId
) {}