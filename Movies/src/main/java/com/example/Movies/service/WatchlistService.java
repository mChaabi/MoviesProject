package com.example.Movies.service;

import com.example.Movies.dto.WatchlistDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.entity.Watchlist;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.UserRepository;
import com.example.Movies.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 🆕 NOUVEAU FICHIER
@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    // Récupérer toute la liste d'un utilisateur
    public List<WatchlistDTO> getUserWatchlist(Long userId) {
        return watchlistRepository.findByUserId(userId)
                .stream().map(this::toDTO).toList();
    }

    // Ajouter un film/série à la liste
    @Transactional
    public WatchlistDTO addToWatchlist(Long userId, Long movieId) {
        if (watchlistRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new RuntimeException("Déjà dans votre liste");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));

        Watchlist entry = Watchlist.builder().user(user).movie(movie).build();
        return toDTO(watchlistRepository.save(entry));
    }

    // Retirer de la liste
    @Transactional
    public void removeFromWatchlist(Long userId, Long movieId) {
        watchlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    // Vérifier si déjà dans la liste
    public boolean isInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    private WatchlistDTO toDTO(Watchlist w) {
        return new WatchlistDTO(
                w.getId(),
                w.getUser().getId(),
                w.getMovie().getId(),
                w.getMovie().getTitle(),
                w.getMovie().getType().name(),
                w.getAddedAt()
        );
    }
}
