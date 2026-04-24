package com.example.Movies.controller;

import com.example.Movies.dto.SeasonDTO;
import com.example.Movies.service.SeasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 🆕 NOUVEAU FICHIER
@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SeasonController {

    private final SeasonService seasonService;

    // GET toutes les saisons d'une série
    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<SeasonDTO>> getSeasonsBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(seasonService.getSeasonsBySeries(seriesId));
    }

    // POST ajouter une saison à une série
    @PostMapping("/add")
    public ResponseEntity<SeasonDTO> addSeason(@Valid @RequestBody SeasonDTO dto) {
        return ResponseEntity.ok(seasonService.createSeason(dto));
    }

    // DELETE supprimer une saison
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        seasonService.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }
}
