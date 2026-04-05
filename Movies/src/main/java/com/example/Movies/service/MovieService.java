package com.example.Movies.service;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.MovieMapper;
import com.example.Movies.repository.MovieRepository;
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

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final MovieMapper movieMapper;
    // Nouveau champ pour définir où stocker les vidéos
    private final String UPLOAD_DIR = "uploads/reels/";
    @Transactional
// Modifiez la ligne 33 pour ajouter le paramètre file
    public MovieDTO createMovie(MovieDTO dto, MultipartFile file) throws IOException {
        User user = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé avec l'ID : " + dto.authorId()));

        // 2. Gestion du fichier Vidéo
        // Maintenant, 'file' est reconnu car il est déclaré juste au-dessus dans les parenthèses
        String fileName = saveVideo(file);

        // 3. Mapper et fixer les infos
        Movie movie = movieMapper.toEntity(dto);

        movie.setAuthor(user);
        movie.setVideoUrl("/api/uploads/reels/" + fileName);

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
    }
    // Méthode utilitaire pour sauvegarder le fichier physiquement
    private String saveVideo(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new RuntimeException("Fichier vide");

        // Créer le dossier s'il n'existe pas
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // Générer un nom unique (ex: 167890123_video.mp4)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR).resolve(fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    // --- GET ALL ---
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(movieMapper::toDTO)
                .toList();
    }

    // --- GET ONE BY ID ---
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'ID : " + id));
        return movieMapper.toDTO(movie);
    }

    // --- UPDATE ---
    @Transactional
    public MovieDTO updateMovie(Long id, MovieDTO dto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossible de mettre à jour : Film non trouvé"));

        // Mise à jour des champs autorisés
        existingMovie.setTitle(dto.title());
        existingMovie.setDescription(dto.description());
        existingMovie.setVideoUrl(dto.videoUrl());

        // On sauvegarde les modifications
        Movie updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toDTO(updatedMovie);
    }

    // --- DELETE ---
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : Film non trouvé");
        }
        movieRepository.deleteById(id);
    }
}