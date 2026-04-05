package com.example.Movies.mapper;

import com.example.Movies.dto.CommentDTO;
import com.example.Movies.entity.Comment;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public CommentDTO toDto(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),    // OK: On récupère l'ID du User
                comment.getMovie().getId(),   // OK: On récupère l'ID du Movie
                comment.getCreatedAt()
        );
    }

    // AJOUTE "User user" dans les paramètres ici :
    public Comment toEntity(CommentDTO dto, Movie movie, User user) {
        return Comment.builder()
                .content(dto.content())       // Correction: .content() (sans "get") car c'est un record
                .movie(movie)
                .user(user)                   // Maintenant 'user' est reconnu
                .createdAt(LocalDateTime.now())
                .build();
    }
}