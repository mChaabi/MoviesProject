package com.example.Movies.mapper;

import com.example.Movies.dto.EpisodeDTO;
import com.example.Movies.entity.Episode;
import org.springframework.stereotype.Component;

// 🆕 NOUVEAU FICHIER
@Component
public class EpisodeMapper {

    public EpisodeDTO toDTO(Episode episode) {
        return new EpisodeDTO(
                episode.getId(),
                episode.getEpisodeNumber(),
                episode.getTitle(),
                episode.getDescription(),
                episode.getVideoUrl(),
                episode.getDurationMinutes(),
                episode.getSeason().getId()
        );
    }

    public Episode toEntity(EpisodeDTO dto) {
        return Episode.builder()
                .episodeNumber(dto.episodeNumber())
                .title(dto.title())
                .description(dto.description())
                .durationMinutes(dto.durationMinutes())
                .build();
    }
}
