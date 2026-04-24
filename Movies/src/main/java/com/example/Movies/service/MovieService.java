package com.example.Movies.service;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.dto.SeasonDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.Movie.MediaType;
import com.example.Movies.entity.Season;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.MovieMapper;
import com.example.Movies.mapper.SeasonMapper;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.SeasonRepository;
import com.example.Movies.repository.UserRepository;
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

// ✅ MODIFIÉ — Ajout de la gestion des séries et des filtres
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final MovieMapper movieMapper;
    private final SeasonMapper seasonMapper;
    private final String UPLOAD_DIR = "uploads/reels/";

    // ✅ EXISTANT — inchangé
    @Transactional
    public MovieDTO createMovie(MovieDTO dto, MultipartFile file) throws IOException {
        User user = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé avec l'ID : " + dto.authorId()));

        String fileName = saveVideo(file);
        Movie movie = movieMapper.toEntity(dto);
        movie.setAuthor(user);
        movie.setVideoUrl("/api/uploads/reels/" + fileName);
        // 🆕 Forcer type MOVIE si non précisé
        if (movie.getType() == null) movie.setType(MediaType.MOVIE);

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

    // 🆕 NOUVEAU — Créer une série (sans vidéo directe)
    @Transactional
    public MovieDTO createSeries(MovieDTO dto) {
        User user = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé"));

        Movie series = movieMapper.toEntity(dto);
        series.setAuthor(user);
        series.setType(MediaType.SERIES);
        series.setVideoUrl(null); // Pas de vidéo directe pour une série

        Movie saved = movieRepository.save(series);
        return movieMapper.toDTO(saved);
    }

    // ✅ EXISTANT — inchangé
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(movieMapper::toDTO).toList();
    }

    // 🆕 NOUVEAU — Filtrer par type
    public List<MovieDTO> getByType(MediaType type) {
        return movieRepository.findByTypeOrderByCreatedAtDesc(type)
                .stream().map(movieMapper::toDTO).toList();
    }

    // 🆕 NOUVEAU — Récupérer les saisons d'une série
    public List<SeasonDTO> getSeasonsOfSeries(Long seriesId) {
        return seasonRepository.findBySeriesIdOrderBySeasonNumberAsc(seriesId)
                .stream().map(seasonMapper::toDTO).toList();
    }

    // ✅ EXISTANT — inchangé
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'ID : " + id));
        return movieMapper.toDTO(movie);
    }

    // ✅ EXISTANT — inchangé
    @Transactional
    public MovieDTO updateMovie(Long id, MovieDTO dto) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film non trouvé"));
        existing.setTitle(dto.title());
        existing.setDescription(dto.description());
        existing.setVideoUrl(dto.videoUrl());
        // 🆕 Mise à jour des nouveaux champs
        if (dto.rating() != null) existing.setRating(dto.rating());
        if (dto.releaseYear() != null) existing.setReleaseYear(dto.releaseYear());
        if (dto.durationMinutes() != null) existing.setDurationMinutes(dto.durationMinutes());
        return movieMapper.toDTO(movieRepository.save(existing));
    }

    // ✅ EXISTANT — inchangé
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id))
            throw new RuntimeException("Film non trouvé");
        movieRepository.deleteById(id);
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
