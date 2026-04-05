package com.example.Movies.service;

import com.example.Movies.dto.LikeRequestDTO;
import com.example.Movies.dto.LikeResponseDTO;
import com.example.Movies.entity.LikeMovie;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.LikeMapper;
import com.example.Movies.repository.LikeMovieRepository;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeMovieService {

    private final LikeMovieRepository likeRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final LikeMapper likeMapper;

    /**
     * Logique de TOGGLE : Si le like existe, on le supprime. S'il n'existe pas, on le crée.
     */
    @Transactional
    public String toggleLike(LikeRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Movie movie = movieRepository.findById(dto.movieId())
                .orElseThrow(() -> new RuntimeException("Film non trouvé"));

        Optional<LikeMovie> existingLike = likeRepository.findByUserAndMovie(user, movie);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return "Like supprimé";
        } else {
            LikeMovie newLike = LikeMovie.builder()
                    .user(user)
                    .movie(movie)
                    .build();
            likeRepository.save(newLike);
            return "Like ajouté";
        }
    }

    /**
     * Récupère le nombre total de likes pour un film
     */
    public long getLikesCount(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film non trouvé"));
        return likeRepository.countByMovie(movie);
    }

    /**
     * Vérifie si un utilisateur spécifique a liké un film
     */
    public boolean isMovieLikedByUser(Long userId, Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Film non trouvé"));
        return likeRepository.existsByUserAndMovie(user, movie);
    }

    /**
     * Récupère la liste des films favoris d'un utilisateur
     */
    public List<LikeResponseDTO> getUserFavorites(Long userId) {
        return likeRepository.findByUserId(userId).stream()
                .map(likeMapper::toDTO)
                .toList();
    }
}