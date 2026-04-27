package com.example.Movies.mapper;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null,              // ✅ Ne jamais renvoyer le mot de passe haché
                user.getRole(),    // 🆕 Inclure le rôle
                user.getCreatedAt()
        );
    }

    public User toEntity(UserDTO dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .build();
    }
}