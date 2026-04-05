package com.example.Movies.controller;

import com.example.Movies.dto.MovieDTO;
import com.example.Movies.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class) // Test uniquement la couche Web
@AutoConfigureMockMvc(addFilters = false) // Désactive temporairement Spring Security pour le test
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService; // Simule le service

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets en JSON

    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        movieDTO = new MovieDTO(1L, "Inception", "http://video.com", "Sci-fi", 1L, "Auteur");
    }

    @Test
    void getAllMovies_ShouldReturnList() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(movieDTO));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Inception"));
    }

    @Test
    void addMovie_ShouldReturnCreated() throws Exception {
        // 1. Créer le faux fichier vidéo AVANT de définir le comportement du mock
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "test-video.mp4",
                "video/mp4", // Utiliser un type MIME vidéo est préférable
                "contenu-video".getBytes()
        );

        // 2. Créer la partie JSON (le DTO)
        MockMultipartFile moviePart = new MockMultipartFile(
                "movie",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(movieDTO)
        );

        // 3. Configurer le Mock du service
        // IMPORTANT : Utilisez 'any(MultipartFile.class)' ou la variable 'filePart' directement
        when(movieService.createMovie(any(MovieDTO.class), any(MultipartFile.class)))
                .thenReturn(movieDTO);

        // 4. Exécuter la requête multipart
        mockMvc.perform(multipart("/movies/add")
                        .file(filePart)
                        .file(moviePart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void updateMovie_ShouldReturnOk() throws Exception {
        when(movieService.updateMovie(eq(1L), any(MovieDTO.class))).thenReturn(movieDTO);

        mockMvc.perform(put("/movies/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMovie_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/movies/delete/1"))
                .andExpect(status().isNoContent());
    }
}