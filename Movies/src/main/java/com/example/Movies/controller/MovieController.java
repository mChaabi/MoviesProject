package com.example.Movies.controller;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.dto.SeasonDTO;
import com.example.Movies.entity.Movie.MediaType;
import com.example.Movies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// ✅ MODIFIÉ — Ajout des endpoints pour séries et filtres
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    private final MovieService movieService;

    // ✅ EXISTANT — GET tous les films ET séries
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // 🆕 NOUVEAU — GET uniquement les films
    @GetMapping("/films")
    public ResponseEntity<List<MovieDTO>> getOnlyMovies() {
        return ResponseEntity.ok(movieService.getByType(com.example.Movies.entity.Movie.MediaType.MOVIE));
    }

    // 🆕 NOUVEAU — GET uniquement les séries
    @GetMapping("/series")
    public ResponseEntity<List<MovieDTO>> getOnlySeries() {
        return ResponseEntity.ok(movieService.getByType(com.example.Movies.entity.Movie.MediaType.SERIES));
    }

    // ✅ EXISTANT — GET par ID
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // 🆕 NOUVEAU — GET les saisons d'une série
    @GetMapping("/{id}/seasons")
    public ResponseEntity<List<SeasonDTO>> getSeasons(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getSeasonsOfSeries(id));
    }

    // ✅ EXISTANT — POST ajouter un film (avec fichier vidéo)
    @PostMapping(value = "/add", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MovieDTO> addMovie(
            @RequestPart("movie") MovieDTO dto,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(movieService.createMovie(dto, file));
    }

    // 🆕 NOUVEAU — POST créer une série (sans vidéo directe)
    @PostMapping("/series/add")
    public ResponseEntity<MovieDTO> addSeries(@Valid @RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.createSeries(dto));
    }

    // ✅ EXISTANT — PUT mettre à jour
    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }

    // ✅ EXISTANT — DELETE supprimer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
