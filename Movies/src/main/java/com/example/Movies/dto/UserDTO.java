package com.example.Movies.dto;

import com.example.Movies.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UserDTO(
        Long id,

        @NotBlank(message = "Le nom est obligatoire") // Add this
        String name,

        @Email(message = "Email invalide")
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        String password,

        Role role,            // 🆕 AJOUTE CE CHAMP — pour renvoyer le rôle au frontend

        LocalDateTime createdAt
) {}