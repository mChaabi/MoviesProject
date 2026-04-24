package com.example.Movies.mapper;

import com.example.Movies.dto.SeasonDTO;
import com.example.Movies.entity.Season;
import org.springframework.stereotype.Component;

// 🆕 NOUVEAU FICHIER
@Component
public class SeasonMapper {

    private final EpisodeMapper episodeMapper;

    public SeasonMapper(EpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    public SeasonDTO toDTO(Season season) {
        return new SeasonDTO(
                season.getId(),
                season.getSeasonNumber(),
                season.getTitle(),
                season.getDescription(),
                season.getSeries().getId(),
                season.getEpisodes() != null
                        ? season.getEpisodes().stream().map(episodeMapper::toDTO).toList()
                        : null,
                season.getEpisodes() != null ? season.getEpisodes().size() : 0
        );
    }

    public Season toEntity(SeasonDTO dto) {
        return Season.builder()
                .seasonNumber(dto.seasonNumber())
                .title(dto.title())
                .description(dto.description())
                .build();
    }
}
