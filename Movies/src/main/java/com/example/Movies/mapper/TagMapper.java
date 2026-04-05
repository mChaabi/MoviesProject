package com.example.Movies.mapper;

import com.example.Movies.dto.MovieSummaryDTO;
import com.example.Movies.dto.TagDTO;
import com.example.Movies.dto.TagDetailsDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TagMapper {

    // Entité -> DTO simple (sans la liste des films)
    public TagDTO toDTO(Tag tag) {
        if (tag == null) return null;
        return new TagDTO(tag.getId(), tag.getLabel());
    }

    // Entité -> DTO détaillé (avec la liste des films transformée)
    public TagDetailsDTO toDetailsDTO(Tag tag) {
        if (tag == null) return null;

        return new TagDetailsDTO(
                tag.getId(),
                tag.getLabel(),
                tag.getMovies().stream()
                        .map(this::toMovieSummaryDTO)
                        .collect(Collectors.toSet())
        );
    }

    // DTO -> Entité (pour la création/mise à jour)
    public Tag toEntity(TagDTO dto) {
        if (dto == null) return null;
        Tag tag = new Tag();
        tag.setId(dto.id());
        tag.setLabel(dto.label());
        return tag;
    }

    // Helper interne pour transformer un Movie en résumé
    private MovieSummaryDTO toMovieSummaryDTO(Movie movie) {
        return new MovieSummaryDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear() // Assure-toi que ce champ existe dans ton entité Movie
        );
    }
}