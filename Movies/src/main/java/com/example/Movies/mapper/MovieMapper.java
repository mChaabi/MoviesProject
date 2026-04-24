package com.example.Movies.mapper;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Movie;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;



@Builder
// ✅ MODIFIÉ — Ajout des nouveaux champs type, rating, releaseYear, durationMinutes, seasons
@Component
@RequiredArgsConstructor
public class MovieMapper {

    private final SeasonMapper seasonMapper;

    public MovieDTO toDTO(Movie movie) {
        if (movie == null) return null;

        return MovieDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .videoUrl(movie.getVideoUrl())
                .description(movie.getDescription())
                .authorId(movie.getAuthor() != null ? movie.getAuthor().getId() : null)
                .authorName(movie.getAuthor() != null ? movie.getAuthor().getName() : null)
                .authorEmail(movie.getAuthor() != null ? movie.getAuthor().getEmail() : null)
                .categoryId(movie.getCategory() != null ? movie.getCategory().getId() : null)
                .categoryName(movie.getCategory() != null ? movie.getCategory().getName() : null)
                .type(movie.getType())
                .rating(movie.getRating())
                .releaseYear(movie.getReleaseYear())
                .durationMinutes(movie.getDurationMinutes())

                // 1. Pour la liste
                .seasons(movie.getSeasons() != null && movie.getType() == Movie.MediaType.SERIES
                        ? movie.getSeasons().stream().map(seasonMapper::toDTO).toList()
                        : null)

                // 2. Pour le compteur (CHANGEZ 'seasons' par le nom correct de votre champ dans MovieDTO, par ex: episodesCount)
                .episodesCount(movie.getSeasons() != null ? movie.getSeasons().size() : 0)

                .build();
    }

    public Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setVideoUrl(dto.videoUrl());
        // 🆕 Nouveaux champs
        if (dto.type() != null) movie.setType(dto.type());
        if (dto.rating() != null) movie.setRating(dto.rating());
        if (dto.releaseYear() != null) movie.setReleaseYear(dto.releaseYear());
        if (dto.durationMinutes() != null) movie.setDurationMinutes(dto.durationMinutes());
        return movie;
    }

    public List<MovieDTO> toDtoList(List<Movie> movies) {
        return movies.stream().map(this::toDTO).toList();
    }
}
