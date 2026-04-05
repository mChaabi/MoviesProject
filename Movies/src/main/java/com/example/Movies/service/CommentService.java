package com.example.Movies.service;

import com.example.Movies.dto.CommentDTO;
import com.example.Movies.entity.Comment;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.CommentMapper;
import com.example.Movies.repository.CommentRepository;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;

    public List<CommentDTO> getCommentsByMovie(Long movieId) {
        return repository.findByMovieId(movieId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public CommentDTO saveComment(CommentDTO dto, Movie movie) {
        // 1. Récupérer l'utilisateur à partir de l'ID dans le DTO
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Passer les 3 arguments au mapper : dto, movie, ET user
        Comment comment = mapper.toEntity(dto, movie, user);

        return mapper.toDto(repository.save(comment));
    }

    public void deleteComment(Long id) {
        repository.deleteById(id);
    }
}
