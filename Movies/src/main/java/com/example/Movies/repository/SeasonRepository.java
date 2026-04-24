package com.example.Movies.repository;

import com.example.Movies.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findBySeriesIdOrderBySeasonNumberAsc(Long seriesId);
}
