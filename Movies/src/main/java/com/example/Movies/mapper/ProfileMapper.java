package com.example.Movies.mapper;

import com.example.Movies.entity.Profile;
import com.example.Movies.dto.ProfileDTO;
import com.example.Movies.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    // Transformer l'Entité en DTO (Pour envoyer au Front-end)
    public ProfileDTO toDTO(Profile profile) {
        if (profile == null) return null;

        return new ProfileDTO(
                profile.getId(),
                profile.getUsername(),
                profile.getBio(),
                profile.getAvatarUrl(),
                profile.getUser() != null ? profile.getUser().getId() : null
        );
    }

    // Transformer le DTO en Entité (Pour enregistrer en Base de données)
    public Profile toEntity(ProfileDTO dto) {
        if (dto == null) return null;

        Profile profile = new Profile();
        profile.setId(dto.id());
        profile.setUsername(dto.username());
        profile.setBio(dto.bio());
        profile.setAvatarUrl(dto.avatarUrl());

        // Note : L'association avec l'User se fera dans le Service
        // car on a besoin d'aller chercher l'User en base via son ID.

        return profile;
    }
}