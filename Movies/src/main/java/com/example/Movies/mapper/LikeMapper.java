package com.example.Movies.mapper;

import com.example.Movies.dto.LikeResponseDTO;
import com.example.Movies.entity.LikeMovie;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    public LikeResponseDTO toDTO(LikeMovie like) {
        if (like == null) {
            return null;
        }

        return new LikeResponseDTO(
                like.getId(),
                like.getUser().getId(),
                like.getMovie().getId(),
                like.getMovie().getTitle(),   // Récupère le titre du film
                like.getCreatedAt()
        );
    }
}