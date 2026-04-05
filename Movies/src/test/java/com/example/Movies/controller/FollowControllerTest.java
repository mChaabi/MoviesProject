package com.example.Movies.controller;

import com.example.Movies.dto.FollowResponseDTO;
import com.example.Movies.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FollowController.class) // Test uniquement la couche Web
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permet de simuler des requêtes HTTP

    @MockitoBean
    private FollowService followService; // Simule le service

    @Test
    void followUser_ShouldReturn201() throws Exception {
        // GIVEN
        FollowResponseDTO response = new FollowResponseDTO(100L, 2L, "test@mail.com", LocalDateTime.now());
        when(followService.follow(1L, 2L)).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/follows/1/to/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void unfollowUser_ShouldReturn204() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/follows/1/from/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getMyFollowingList_ShouldReturnList() throws Exception {
        // GIVEN
        List<FollowResponseDTO> list = List.of(
                new FollowResponseDTO(100L, 2L, "user2@mail.com", LocalDateTime.now())
        );
        when(followService.getFollowingList(1L)).thenReturn(list);

        // WHEN & THEN
        mockMvc.perform(get("/follows/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("user2@mail.com"));
    }

    @Test
    void getFollowersCount_ShouldReturnCount() throws Exception {
        // GIVEN
        when(followService.getFollowersCount(1L)).thenReturn(42L);

        // WHEN & THEN
        mockMvc.perform(get("/follows/count/followers/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
    }
}
