package com.example.Movies.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ajoutez ceci :
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(unique = true)
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Dans User.java (Optionnel mais recommandé)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Movie> movies;

    // Un utilisateur peut avoir plusieurs likes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LikeMovie> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>(); // Les gens que JE suis

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>(); // Les gens qui ME suivent
}

