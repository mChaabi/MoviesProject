package com.example.Movies.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// 🆕 NOUVEAU FICHIER
@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer episodeNumber; // 1, 2, 3...

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    // URL de la vidéo de l'épisode
    @Column(name = "video_url")
    private String videoUrl;

    // Durée en minutes
    private Integer durationMinutes;

    // Relation avec la saison parente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
