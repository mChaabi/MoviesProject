package com.example.Movies.mapper;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList; // À ajouter
import java.util.List;      // À ajouter
import java.util.stream.Collectors; // Optionnel (pour les streams)

@Component
public class MovieMapper {

    public MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getVideoUrl(),
                movie.getDescription(),
                movie.getAuthor() != null ? movie.getAuthor().getId() : null,
                movie.getAuthor() != null ? movie.getAuthor().getEmail() : "Auteur inconnu"
        );
    }

    // --- NOUVELLE MÉTHODE ---
    public List<MovieDTO> toDtoList(List<Movie> movies) {
        if (movies == null) {
            return new ArrayList<>();
        }

        // Version propre avec Stream (plus moderne)
        return movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    // ------------------------

    public Movie toEntity(MovieDTO dto) {
        if (dto == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setId(dto.id());
        movie.setTitle(dto.title());
        movie.setVideoUrl(dto.videoUrl());
        movie.setDescription(dto.description());

        return movie;
    }
}