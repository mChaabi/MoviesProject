package com.example.Movies.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    // ✅ MODIFIÉ : videoUrl devient nullable car les séries n'ont pas de vidéo directe
    @Column(name = "video_url")
    private String videoUrl;

    // 🆕 NOUVEAU : type MOVIE ou SERIES
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private MediaType type = MediaType.MOVIE;

    // 🆕 NOUVEAU : note et année
    private Double rating;
    private Integer releaseYear;

    // 🆕 NOUVEAU : durée en minutes (pour les films)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(optional = true) // Autorise le null au niveau JPA
    @JoinColumn(name = "author_id", nullable = true) // Autorise le null au niveau SQL
    private User author;

    // 🆕 NOUVEAU : relation avec les saisons (pour les séries)
    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Season> seasons;

    @ManyToMany
    @JoinTable(
            name = "movie_tags",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>(); // <-- Ce nom DOIT être "tags"

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeMovie> likes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 🆕 NOUVEAU : enum pour le type de média
    public enum MediaType {
        MOVIE, SERIES
    }
}
