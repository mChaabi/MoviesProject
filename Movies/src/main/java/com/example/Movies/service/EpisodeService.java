package com.example.Movies.service;

import com.example.Movies.dto.EpisodeDTO;
import com.example.Movies.entity.Episode;
import com.example.Movies.entity.Season;
import com.example.Movies.mapper.EpisodeMapper;
import com.example.Movies.repository.EpisodeRepository;
import com.example.Movies.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

// 🆕 NOUVEAU FICHIER
@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeMapper episodeMapper;
    private final String UPLOAD_DIR = "uploads/reels/";

    public List<EpisodeDTO> getEpisodesBySeason(Long seasonId) {
        return episodeRepository.findBySeasonIdOrderByEpisodeNumberAsc(seasonId)
                .stream().map(episodeMapper::toDTO).toList();
    }

    public EpisodeDTO getEpisodeById(Long id) {
        Episode ep = episodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Épisode non trouvé"));
        return episodeMapper.toDTO(ep);
    }

    @Transactional
    public EpisodeDTO createEpisode(EpisodeDTO dto, MultipartFile file) throws IOException {
        Season season = seasonRepository.findById(dto.seasonId())
                .orElseThrow(() -> new RuntimeException("Saison non trouvée"));

        String fileName = saveVideo(file);

        Episode episode = Episode.builder()
                .episodeNumber(dto.episodeNumber())
                .title(dto.title())
                .description(dto.description())
                .durationMinutes(dto.durationMinutes())
                .videoUrl("/api/uploads/reels/" + fileName)
                .season(season)
                .build();

        return episodeMapper.toDTO(episodeRepository.save(episode));
    }

    @Transactional
    public void deleteEpisode(Long id) {
        if (!episodeRepository.existsById(id))
            throw new RuntimeException("Épisode non trouvé");
        episodeRepository.deleteById(id);
    }

    private String saveVideo(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new RuntimeException("Fichier vide");
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
