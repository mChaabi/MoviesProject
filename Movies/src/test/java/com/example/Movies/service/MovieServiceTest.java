package com.example.Movies.service;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.entity.User;
import com.example.Movies.mapper.MovieMapper;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Utilise Mockito pour JUnit 5
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService; // Injecte les mocks ci-dessus dans le service

    private User user;
    private Movie movie;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Inception");
        movie.setAuthor(user);

        movieDTO = MovieDTO.builder()
                .id(1L)
                .title("Inception")
                .videoUrl("http://video.com")
                .description("Sci-fi")
                .authorId(1L)
                .authorName("Auteur")
                .build(); // Pas besoin de mettre les 12 autres champs, ils seront null par défaut
    }

    @Test
    void createMovie_Success() throws IOException {
        // Créez un faux fichier pour le test
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.mp4", "video/mp4", "data".getBytes());

        // Arrange (Correction de la syntaxe any)
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieMapper.toEntity(any(MovieDTO.class))).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie); // Correction ici
        when(movieMapper.toDTO(any(Movie.class))).thenReturn(movieDTO);

        // Act - Passez les deux arguments
        MovieDTO result = movieService.createMovie(movieDTO, mockFile);

        // Assert
        assertNotNull(result);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void getMovieById_ThrowException_WhenNotFound() {
        // Arrange
        when(movieRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.getMovieById(10L);
        });

        assertEquals("Film non trouvé avec l'ID : 10", exception.getMessage());
    }

    @Test
    void deleteMovie_ShouldCallRepository_WhenExists() {
        // Arrange
        when(movieRepository.existsById(10L)).thenReturn(true);

        // Act
        movieService.deleteMovie(10L);

        // Assert
        verify(movieRepository, times(1)).deleteById(10L);
    }
}