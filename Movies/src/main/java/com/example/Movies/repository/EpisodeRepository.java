package com.example.Movies.repository;

import com.example.Movies.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeasonIdOrderByEpisodeNumberAsc(Long seasonId);
    long countBySeasonId(Long seasonId);
}
