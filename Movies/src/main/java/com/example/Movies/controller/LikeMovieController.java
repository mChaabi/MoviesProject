package com.example.Movies.controller;

import com.example.Movies.dto.LikeRequestDTO;
import com.example.Movies.dto.LikeResponseDTO;
import com.example.Movies.service.LikeMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LikeMovieController {

    private final LikeMovieService likeMovieService;

    // --- 1. ACTION DE TOGGLE (LIKE/UNLIKE) ---
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleLike(@Valid @RequestBody LikeRequestDTO dto) {
        String result = likeMovieService.toggleLike(dto);
        return ResponseEntity.ok(result);
    }

    // --- 2. RÉCUPÉRER LE NOMBRE DE LIKES D'UN FILM ---
    @GetMapping("/count/{movieId}")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long movieId) {
        return ResponseEntity.ok(likeMovieService.getLikesCount(movieId));
    }

    // --- 3. VÉRIFIER SI L'UTILISATEUR ACTUEL A LIKÉ CE FILM ---
    @GetMapping("/status")
    public ResponseEntity<Boolean> checkLikeStatus(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        return ResponseEntity.ok(likeMovieService.isMovieLikedByUser(userId, movieId));
    }

    // --- 4. LISTE DES FAVORIS D'UN UTILISATEUR ---
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(likeMovieService.getUserFavorites(userId));
    }
}