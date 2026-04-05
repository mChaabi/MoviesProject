package com.example.Movies.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profiles")
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Le nom d'affichage (ex: @cinema_fan)

    private String bio; // Une petite description

    private String avatarUrl; // Lien vers l'image de profil

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Relation avec ton entité User existante
}