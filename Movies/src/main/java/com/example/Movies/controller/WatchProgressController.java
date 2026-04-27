package com.example.Movies.controller;

import com.example.Movies.dto.WatchProgressDTO;
import com.example.Movies.service.WatchProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")  // ✅ Sera accessible via /api/progress
@RequiredArgsConstructor
// ❌ SUPPRIME @CrossOrigin — géré globalement dans SecurityConfig
public class WatchProgressController {

    private final WatchProgressService watchProgressService;

    @GetMapping("/movie")
    public ResponseEntity<?> getMovieProgress(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        try {
            return ResponseEntity.ok(watchProgressService.getMovieProgress(userId, movieId));
        } catch (Exception e) {
            // ✅ Retourner 0% si pas de progression (pas d'erreur 403)
            return ResponseEntity.ok(new WatchProgressDTO(null, userId, null, movieId, 0, 0, false));
        }
    }

    @GetMapping("/episode")
    public ResponseEntity<?> getEpisodeProgress(
            @RequestParam Long userId,
            @RequestParam Long episodeId) {
        try {
            return ResponseEntity.ok(watchProgressService.getEpisodeProgress(userId, episodeId));
        } catch (Exception e) {
            return ResponseEntity.ok(new WatchProgressDTO(null, userId, episodeId, null, 0, 0, false));
        }
    }

    @PostMapping("/movie/save")
    public ResponseEntity<?> saveMovieProgress(
            @RequestParam Long userId,
            @RequestParam Long movieId,
            @RequestParam int percent,
            @RequestParam int positionSeconds) {
        try {
            return ResponseEntity.ok(
                    watchProgressService.saveMovieProgress(userId, movieId, percent, positionSeconds)
            );
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/episode/save")
    public ResponseEntity<?> saveEpisodeProgress(
            @RequestParam Long userId,
            @RequestParam Long episodeId,
            @RequestParam int percent,
            @RequestParam int positionSeconds) {
        try {
            return ResponseEntity.ok(
                    watchProgressService.saveEpisodeProgress(userId, episodeId, percent, positionSeconds)
            );
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }
}