package com.example.Movies.controller;

import com.example.Movies.dto.WatchProgressDTO;
import com.example.Movies.service.WatchProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 🆕 NOUVEAU FICHIER
@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WatchProgressController {

    private final WatchProgressService watchProgressService;

    // GET progression d'un film
    @GetMapping("/movie")
    public ResponseEntity<WatchProgressDTO> getMovieProgress(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        return ResponseEntity.ok(watchProgressService.getMovieProgress(userId, movieId));
    }

    // GET progression d'un épisode
    @GetMapping("/episode")
    public ResponseEntity<WatchProgressDTO> getEpisodeProgress(
            @RequestParam Long userId,
            @RequestParam Long episodeId) {
        return ResponseEntity.ok(watchProgressService.getEpisodeProgress(userId, episodeId));
    }

    // POST sauvegarder progression film
    @PostMapping("/movie/save")
    public ResponseEntity<WatchProgressDTO> saveMovieProgress(
            @RequestParam Long userId,
            @RequestParam Long movieId,
            @RequestParam int percent,
            @RequestParam int positionSeconds) {
        return ResponseEntity.ok(watchProgressService.saveMovieProgress(userId, movieId, percent, positionSeconds));
    }

    // POST sauvegarder progression épisode
    @PostMapping("/episode/save")
    public ResponseEntity<WatchProgressDTO> saveEpisodeProgress(
            @RequestParam Long userId,
            @RequestParam Long episodeId,
            @RequestParam int percent,
            @RequestParam int positionSeconds) {
        return ResponseEntity.ok(watchProgressService.saveEpisodeProgress(userId, episodeId, percent, positionSeconds));
    }
}
