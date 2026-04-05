package com.example.Movies.mapper;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt()
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        // On utilise le Builder de Lombok défini dans l'entité User
        return User.builder()
                .id(dto.id())           // Accès Record : pas de "get"
                .email(dto.email())     // Accès Record
                .password(dto.password())
                // createdAt est souvent géré par @CreationTimestamp,
                // donc on ne le mappe pas forcément ici
                .build();
    }
}
