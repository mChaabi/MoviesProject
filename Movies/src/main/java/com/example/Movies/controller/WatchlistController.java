package com.example.Movies.controller;

import com.example.Movies.dto.WatchlistDTO;
import com.example.Movies.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 🆕 NOUVEAU FICHIER
@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WatchlistController {

    private final WatchlistService watchlistService;

    // GET toute la liste d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchlistDTO>> getUserWatchlist(@PathVariable Long userId) {
        return ResponseEntity.ok(watchlistService.getUserWatchlist(userId));
    }

    // GET vérifier si un contenu est dans la liste
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkInWatchlist(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        return ResponseEntity.ok(watchlistService.isInWatchlist(userId, movieId));
    }

    // POST ajouter à la liste
    @PostMapping("/add")
    public ResponseEntity<WatchlistDTO> addToWatchlist(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        return ResponseEntity.ok(watchlistService.addToWatchlist(userId, movieId));
    }

    // DELETE retirer de la liste
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWatchlist(
            @RequestParam Long userId,
            @RequestParam Long movieId) {
        watchlistService.removeFromWatchlist(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}
