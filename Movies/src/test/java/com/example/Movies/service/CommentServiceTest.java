package com.example.Movies.service;

import com.example.Movies.dto.CommentDTO;
import com.example.Movies.entity.Comment;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.CommentMapper;
import com.example.Movies.repository.CommentRepository;
import com.example.Movies.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Active Mockito avec JUnit 5
class CommentServiceTest {

    @Mock
    private CommentRepository repository;
    @Mock
    private CommentMapper mapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService; // Injecte les mocks ci-dessus dans le service

    @Test
    void saveComment_ShouldReturnSavedCommentDTO_WhenUserExists() {
        // --- GIVEN (Préparation des données) ---
        Long userId = 1L;
        Movie movie = new Movie();
        movie.setId(10L);

        // Ligne 44 : Ajoutez null pour movieId et LocalDateTime.now() (ou null) pour createdAt
        CommentDTO inputDto = new CommentDTO(null, "Super film !", userId, 10L, java.time.LocalDateTime.now());
        User user = new User();
        user.setId(userId);

        Comment commentEntity = new Comment();
        // Ligne 49 : Pareil pour le DTO attendu en sortie
        CommentDTO expectedDto = new CommentDTO(1L, "Super film !", userId, 10L, java.time.LocalDateTime.now());

        // Simulation des comportements (Stubbing)
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toEntity(inputDto, movie, user)).thenReturn(commentEntity);
        when(repository.save(commentEntity)).thenReturn(commentEntity);
        when(mapper.toDto(commentEntity)).thenReturn(expectedDto);

        // --- WHEN (Exécution de la méthode) ---
        CommentDTO result = commentService.saveComment(inputDto, movie);

        // --- THEN (Vérifications) ---
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo("Super film !");
        verify(repository, times(1)).save(any(Comment.class));
    }

    @Test
    void saveComment_ShouldThrowException_WhenUserDoesNotExist() {
        // GIVEN
        Long userId = 99L;
        // Dans le deuxième test (ligne 71 sur votre image)
        CommentDTO dto = new CommentDTO(null, "Test", userId, null, null);
        Movie movie = new Movie();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.saveComment(dto, movie);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found");
        verify(repository, never()).save(any());
    }
}