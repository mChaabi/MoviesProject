package com.example.Movies.controller;

import com.example.Movies.dto.TagDTO;
import com.example.Movies.dto.TagDetailsDTO;
import com.example.Movies.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Simule uniquement le service pour le controller
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTags_ShouldReturnList() throws Exception {
        List<TagDTO> tags = List.of(new TagDTO(1L, "Action"));
        when(tagService.getAllTags()).thenReturn(tags);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Action"));
    }

    @Test
    void getTagById_ShouldReturnDetails() throws Exception {
        // Pour un record, on utilise le constructeur complet
        TagDetailsDTO details = new TagDetailsDTO(1L, "Drama", Collections.emptySet());
        when(tagService.getTagById(1L)).thenReturn(details);

        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Drama")); // Accès JSON standard
    }

    @Test
    void createTag_ShouldReturnCreated() throws Exception {
        TagDTO inputDto = new TagDTO(null, "Sci-Fi");
        TagDTO outputDto = new TagDTO(1L, "Sci-Fi");

        when(tagService.createTag(any(TagDTO.class))).thenReturn(outputDto);

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.label").value("Sci-Fi"));
    }

    @Test
    void deleteTag_ShouldReturnNoContent() throws Exception {
        // doNothing est implicite pour les méthodes void
        mockMvc.perform(delete("/tags/1"))
                .andExpect(status().isNoContent());
    }
}