package com.example.Movies.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@Entity
@Table(name = "seasons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numéro de la saison : 1, 2, 3...
    @Column(nullable = false)
    private Integer seasonNumber;

    @Column(length = 200)
    private String title; // Ex: "Saison 1 : Les Débuts"

    @Column(length = 500)
    private String description;

    // Relation vers la série parente (Movie avec type=SERIES)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    private Movie series;

    // Liste des épisodes de cette saison
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("episodeNumber ASC")
    private List<Episode> episodes;
}
