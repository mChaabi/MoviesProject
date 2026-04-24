package com.example.Movies.repository;

import com.example.Movies.entity.WatchProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// 🆕 NOUVEAU FICHIER
@Repository
public interface WatchProgressRepository extends JpaRepository<WatchProgress, Long> {
    Optional<WatchProgress> findByUserIdAndEpisodeId(Long userId, Long episodeId);
    Optional<WatchProgress> findByUserIdAndMovieId(Long userId, Long movieId);
    List<WatchProgress> findByUserId(Long userId);
}
