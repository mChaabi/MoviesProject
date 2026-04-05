package com.example.Movies.mapper;

import com.example.Movies.dto.FollowResponseDTO;
import com.example.Movies.entity.Follow;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowMapper {

    // Transforme l'entité Follow en FollowResponseDTO
    public FollowResponseDTO toResponseDTO(Follow follow) {
        if (follow == null) return null;

        return new FollowResponseDTO(
                follow.getId(),
                follow.getFollowing().getId(),
                follow.getFollowing().getEmail(), // .getEmail() au lieu de .getUsername()
                follow.getCreatedAt()
        );
    }

    // Pour transformer une liste d'entités (très utile pour findByFollowerId)
    public List<FollowResponseDTO> toResponseDTOList(List<Follow> follows) {
        return follows.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
