package com.example.Movies.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// 🆕 NOUVEAU FICHIER — Suivi de progression de visionnage
@Entity
@Table(name = "watch_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "episode_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Pour les épisodes de séries
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id")
    private Episode episode;

    // Pour les films directs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    // Pourcentage visionné : 0 à 100
    @Column(nullable = false)
    private Integer progressPercent = 0;

    // Temps exact en secondes (pour reprendre là où on s'est arrêté)
    private Integer lastPositionSeconds = 0;

    private Boolean completed = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
