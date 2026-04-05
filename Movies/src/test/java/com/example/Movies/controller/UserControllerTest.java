package com.example.Movies.controller;

import com.example.Movies.dto.UserDTO;
import com.example.Movies.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) // Teste uniquement la couche Web
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simule les appels HTTP

    @MockitoBean
    private UserService userService; // Mock du service injecté dans le contrôleur

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les Records en JSON

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Initialisation d'un UserDTO de test (Record Java 21)
        userDTO = new UserDTO(1L, "test@example.com", "password123", LocalDateTime.now());
    }

    @Test
    @WithMockUser // Simule un utilisateur authentifié
    @DisplayName("GET /users - Succès")
    void getAllUsers_ShouldReturnList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /users - Création avec succès")
    void createUser_ShouldReturnCreated() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                        .with(csrf()) // Indispensable avec Spring Security activé
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /users/{id} - Mise à jour")
    void updateUser_ShouldReturnOk() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /users/{id} - Suppression")
    void deleteUser_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /users - Erreur 401 si non connecté")
    void getAllUsers_Unauthorized() throws Exception {
        // Sans @WithMockUser, Spring Security doit bloquer l'accès
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }
}
