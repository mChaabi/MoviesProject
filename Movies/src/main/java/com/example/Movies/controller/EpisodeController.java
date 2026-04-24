package com.example.Movies.controller;

import com.example.Movies.dto.EpisodeDTO;
import com.example.Movies.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@RestController
@RequestMapping("/episodes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EpisodeController {

    private final EpisodeService episodeService;

    // GET tous les épisodes d'une saison
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesBySeason(@PathVariable Long seasonId) {
        return ResponseEntity.ok(episodeService.getEpisodesBySeason(seasonId));
    }

    // GET un épisode par ID
    @GetMapping("/{id}")
    public ResponseEntity<EpisodeDTO> getEpisodeById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getEpisodeById(id));
    }

    // POST ajouter un épisode avec sa vidéo
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EpisodeDTO> addEpisode(
            @RequestPart("episode") EpisodeDTO dto,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(episodeService.createEpisode(dto, file));
    }

    // DELETE supprimer un épisode
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }
}
