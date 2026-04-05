package com.example.Movies.dto;

public record FollowStatsDTO(
        Long userId,
        long followersCount,
        long followingCount
) {}
