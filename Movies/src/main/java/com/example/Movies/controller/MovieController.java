package com.example.Movies.controller;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Indispensable pour Angular
public class MovieController {

    private final MovieService movieService;

    // --- 1. RÉCUPÉRER TOUS LES FILMS ---
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // --- 2. RÉCUPÉRER UN FILM PAR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MovieDTO> addMovie(
            @RequestPart("movie") MovieDTO dto,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(movieService.createMovie(dto, file));
    }

    // --- 4. METTRE À JOUR UN FILM ---
    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }

    // --- 5. SUPPRIMER UN FILM ---
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
