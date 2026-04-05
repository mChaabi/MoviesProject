package com.example.Movies.controller;

import com.example.Movies.dto.CommentDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MovieRepository movieRepository; // Nécessaire pour récupérer l'objet Movie avant le save

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(commentService.getCommentsByMovie(movieId));
    }

    @PostMapping("/movie/{movieId}/add")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long movieId,
            @RequestBody CommentDTO commentDTO) {

        // On récupère le film pour l'associer au commentaire
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        CommentDTO savedComment = commentService.saveComment(commentDTO, movie);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
