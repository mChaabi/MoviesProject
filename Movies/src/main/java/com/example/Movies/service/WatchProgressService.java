package com.example.Movies.service;

import com.example.Movies.dto.WatchProgressDTO;
import com.example.Movies.entity.Episode;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.entity.WatchProgress;
import com.example.Movies.repository.EpisodeRepository;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.UserRepository;
import com.example.Movies.repository.WatchProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 🆕 NOUVEAU FICHIER
@Service
@RequiredArgsConstructor
public class WatchProgressService {

    private final WatchProgressRepository watchProgressRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final MovieRepository movieRepository;

    // Sauvegarder ou mettre à jour la progression sur un épisode
    @Transactional
    public WatchProgressDTO saveEpisodeProgress(Long userId, Long episodeId, int percent, int positionSeconds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new RuntimeException("Épisode non trouvé"));

        WatchProgress progress = watchProgressRepository
                .findByUserIdAndEpisodeId(userId, episodeId)
                .orElse(WatchProgress.builder().user(user).episode(episode).build());

        progress.setProgressPercent(percent);
        progress.setLastPositionSeconds(positionSeconds);
        progress.setCompleted(percent >= 90); // marqué terminé à 90%

        return toDTO(watchProgressRepository.save(progress));
    }

    // Sauvegarder ou mettre à jour la progression sur un film
    @Transactional
    public WatchProgressDTO saveMovieProgress(Long userId, Long movieId, int percent, int positionSeconds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film non trouvé"));

        WatchProgress progress = watchProgressRepository
                .findByUserIdAndMovieId(userId, movieId)
                .orElse(WatchProgress.builder().user(user).movie(movie).build());

        progress.setProgressPercent(percent);
        progress.setLastPositionSeconds(positionSeconds);
        progress.setCompleted(percent >= 90);

        return toDTO(watchProgressRepository.save(progress));
    }

    // Récupérer la progression d'un épisode
    public WatchProgressDTO getEpisodeProgress(Long userId, Long episodeId) {
        return watchProgressRepository
                .findByUserIdAndEpisodeId(userId, episodeId)
                .map(this::toDTO)
                .orElse(new WatchProgressDTO(null, userId, episodeId, null, 0, 0, false));
    }

    // Récupérer la progression d'un film
    public WatchProgressDTO getMovieProgress(Long userId, Long movieId) {
        return watchProgressRepository
                .findByUserIdAndMovieId(userId, movieId)
                .map(this::toDTO)
                .orElse(new WatchProgressDTO(null, userId, null, movieId, 0, 0, false));
    }

    private WatchProgressDTO toDTO(WatchProgress wp) {
        return new WatchProgressDTO(
                wp.getId(),
                wp.getUser().getId(),
                wp.getEpisode() != null ? wp.getEpisode().getId() : null,
                wp.getMovie() != null ? wp.getMovie().getId() : null,
                wp.getProgressPercent(),
                wp.getLastPositionSeconds(),
                wp.getCompleted()
        );
    }
}
