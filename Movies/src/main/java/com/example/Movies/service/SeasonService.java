package com.example.Movies.service;

import com.example.Movies.dto.SeasonDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.Season;
import com.example.Movies.mapper.SeasonMapper;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final MovieRepository movieRepository;
    private final SeasonMapper seasonMapper;

    public List<SeasonDTO> getSeasonsBySeries(Long seriesId) {
        return seasonRepository.findBySeriesIdOrderBySeasonNumberAsc(seriesId)
                .stream().map(seasonMapper::toDTO).toList();
    }

    @Transactional
    public SeasonDTO createSeason(SeasonDTO dto) {
        Movie series = movieRepository.findById(dto.seriesId())
                .orElseThrow(() -> new RuntimeException("Série non trouvée"));

        Season season = Season.builder()
                .seasonNumber(dto.seasonNumber())
                .title(dto.title())
                .description(dto.description())
                .series(series)
                .build();

        return seasonMapper.toDTO(seasonRepository.save(season));
    }

    @Transactional
    public void deleteSeason(Long id) {
        if (!seasonRepository.existsById(id))
            throw new RuntimeException("Saison non trouvée");
        seasonRepository.deleteById(id);
    }
}
