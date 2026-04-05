package com.example.Movies.controller;

import com.example.Movies.dto.CommentDTO;
import com.example.Movies.entity.Movie;
import com.example.Movies.repository.MovieRepository;
import com.example.Movies.service.CommentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class) // Ne charge que ce contrôleur
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc; // Pour simuler les requêtes HTTP

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets en JSON

    @MockitoBean
    private CommentService commentService; // Simule le service

    @MockitoBean
    private MovieRepository movieRepository; // Simule le repository

    @Test
    void createComment_ShouldReturnCreatedStatus() throws Exception {
        // --- GIVEN ---
        Long movieId = 10L;
        Movie mockMovie = new Movie();
        mockMovie.setId(movieId);

        // Attention : On respecte bien les 5 arguments du Record
        CommentDTO inputDto = new CommentDTO(null, "Excellent !", 1L, movieId, null);
        CommentDTO savedDto = new CommentDTO(1L, "Excellent !", 1L, movieId, LocalDateTime.now());

        // Simulation du comportement du repo et du service
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(mockMovie));
        when(commentService.saveComment(any(CommentDTO.class), any(Movie.class))).thenReturn(savedDto);

        // --- WHEN & THEN (Exécution et Vérification) ---
        mockMvc.perform(post("/comments/movie/{movieId}/add", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto))) // Objet -> JSON
                .andExpect(status().isCreated()) // Vérifie le code 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Excellent !"));
    }
}
